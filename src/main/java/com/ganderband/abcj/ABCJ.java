package  com.ganderband.abcj ;

/**
 * The main class to instantiate ABCJ.
 *
 * <br><br><hr><br>
 * <big><b>Version History</b></big>
 * <ul>
 * <li><b>1.0</b> - Base Version January 2006.<br>
 * <li><b>1.1</b> - Add ABCJunior mode.<br>
 * <li><b>1.2</b> - Fix benign <code>FileNotFoundException</code> on ABCJ.LST on clean install.<br>
 * 					Package ABCJHelp.htm in the install package.<br>
 * 					Make ignore line breaks and tempo indication work on tune printing as well
 * 						as display.<br>
 * 					Add Undo/Redo support.<br>
 * <li><b>1.3</b> - Preserve existing configs when installing new version.<br>
 * <li><b>1.4</b> - Reduce vertical space used for a stave when chords are not present.
 * 						i.e. Get more music in the same space !!!<br>
 * 					Allow the various header information fields to be shown/hidden (N:,S:, ...).<br>
 * 					Add option to show clef on all staves.<br>
 * 					Add lyrics support.<br>
 * <li><b>1.5</b> - Display dotted hornpipes and mazurkas in the conventional manner (or indeed
 * 						anything similar !).<br>
 * 					Add the F: and S: header fields.<br>
 * 					Add version number into title bar !<br>
 * <li><b>1.6</b> - Change music background from light yellow to white so color printers
 * 						don't use so much ink !!!<br>
 * <li><b>1.7</b> - Check whether a midi player can be created as there are some conflicts with later
 * 						JVMs if JMF has been installed.<br>
 * <li><b>1.8</b> - Fix NullPointerException when trying to display invalid ABC as music.
 * 						This was common with the use of the %% print control stuff which is not
 * 						strictly speaking valid ABC.<br>
 * </ul>
 * <hr>
 */

import  java.awt.* ;
import  java.util.* ;
import  java.io.* ;
import  java.awt.event.* ;
import  javax.swing.* ;
import  javax.sound.midi.* ;
import  com.ganderband.abcj.ui.* ;
import  com.ganderband.abcj.ui.music.* ;
import  com.ganderband.abcj.model.* ;
import  com.ganderband.abcj.model.abc.* ;
import  com.ganderband.abcj.model.music.* ;
import  com.ganderband.abcj.midi.* ;
import  com.ganderband.abcj.util.* ;

public class ABCJ extends WindowAdapter implements ABCJConstants
{
/**
 * The current version number.
 */
	public static String  VERSION = "1.9-SNAPSHOT" ;
/**
 * The main frame for the application.
 */
	protected JFrame  mFrame ;
/**
 * The Main GUI panel as an application.
 */
	private MainPane  mMainPane ;
/**
 * The Main GUI interface.
 */
	private MainGUI  mMainGUI ;
/**
 * The menu bar.
 */
	private ABCJMenuBar  mMenuBar ;
/**
 * The status bar.
 */
	private StatusBar  mStatusBar ;
/**
 * The library of tune books.
 */
	private Library  mLibrary ;
/**
 * The currently selected tune book.
 */
	private TuneBook  mSelectedBook ;
/**
 * The currently selected tune.
 */
	private Tune  mSelectedTune ;
/**
 * The currently selected list.
 */
	private TuneList  mSelectedList ;
/**
 * The thread used to do the ABC parsing and music building asynchronously.
 */
	private ParserThread  mParserThread ;
/**
 * The parser used to parse all ABC.
 */
	private ABCParser  mParser ;
/**
 * A music builder instance to convert ABC to musical notation.
 */
	private MusicBuilder  mMusicBuilder ;


/**
 * The default constructor.
 */
public  ABCJ()
{
//  Nothing needed at present.
}


/**
 * Initialize as an application.
 */
public void  initApplication()
{

//  Load the properties

	ABCJProperties.load() ;

//  Initialise the library from properties

	mLibrary = new Library() ;
	mLibrary.initialize( ABCJProperties.getLibrary() ) ;
	
//	Create and set up the standard GUI.
	
	createGUI() ;

//  Now create the worker threads
	
	createWorkerThreads() ;

//	Finally display the frame full screen to activate the application.

//	mFrame.pack() ;
	mFrame.setVisible(true) ;

	setStatusBar(null) ;
}


/**
 * Create the standard GUI.
 * 
 * <p>This is used when running as an application.   See the <code>abcj.applet.ABCJApplet</code>
 * class for running as an applet.
 */
public void  createGUI()
{
	
//	Create and set up the Swing Frame.
		
	mFrame = new JFrame( "ABCJ " + VERSION ) ;

	mFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE) ;
	mFrame.addWindowListener(this) ;

//  Build the frame GUI structure with menu at top, main panel in the middle
//  and status bar at the bottom

	mMenuBar = new ABCJMenuBar(this) ;
	mMainPane = new MainPane(this) ;
	mStatusBar = new StatusBar() ;
	setStatusBar("Loading ...") ;

	JPanel  p = new JPanel() ;
	p.setLayout( new BoxLayout(p, BoxLayout.Y_AXIS) ) ;
	p.add(mMainPane) ;
	p.add(mStatusBar) ;

	mFrame.setJMenuBar(mMenuBar) ;
	mFrame.getContentPane().add(p) ;
	
	setGUIInterface(mMainPane) ;

//  Set the initial GUI state from the properties file

	setInitialGUIState() ;
}


/**
 * Create the worker threads.
 */
public void  createWorkerThreads()
{

//  Now the GUI has been initiated, create a parser and start the parsing thread.

//	ABCParser.setDebug(false) ;

	mParser = new ABCParser() ;
	mMusicBuilder = new MusicBuilder() ;

	int  w = ABCJProperties.getPropertyInt("Score.StaveWidth") ;
	if ( w > 0 )   MusicBuilder.setStaveWidth(w) ;
	w = ABCJProperties.getPropertyInt("Score.ShowTempo") ;
	MusicBuilder.setShowTempo( w != 0 ) ;
	w = ABCJProperties.getPropertyInt("Score.IgnoreBreaks") ;
	MusicBuilder.setIgnoreBreaks( w != 0 ) ;
	w = ABCJProperties.getPropertyInt("Score.ShowInfoFields") ;
	MusicBuilder.setShowInfoFields( w != 0 ) ;
	w = ABCJProperties.getPropertyInt("Score.ShowLyrics") ;
	MusicBuilder.setShowLyrics( w != 0 ) ;
	w = ABCJProperties.getPropertyInt("Score.ShowClef") ;
	MusicBuilder.setShowClef( w != 0 ) ;

	mParserThread = new ParserThread( this, mParser, mMusicBuilder ) ;
	mParserThread.start() ;
}


/**
 * Set the GUI interface to use.
 * 
 * <p>This provides the standard method of communicating with the GUI.
 */
public void  setGUIInterface( MainGUI g )
{	mMainGUI = g ;   }


/**
 * Get the library instance.
 */
public Library  getLibrary()
{   return  mLibrary ;   }

/**
 * Get the current selected book.
 */
public TuneBook  getSelectedTuneBook()
{   return  mSelectedBook ;   }

/**
 * Get the current selected list.
 */
public TuneList  getSelectedList()
{	return  mSelectedList ;   }


/**
 * Get the current selected tune.
 */
public Tune  getSelectedTune()
{   return  mSelectedTune ;   }


/**
 * Set the current edit mode.
 */
public void  setEditMode( int EditMode )
{   mMainGUI.setEditMode(EditMode) ;   }


/**
 * Set the current look and feel.
 */
public void  setLookAndFeel( int LookAndFeel )
{
	ABCJProperties.setLookAndFeel(LookAndFeel) ;
	if ( mMenuBar != null )   mMenuBar.setLookAndFeel(LookAndFeel) ;

//	Change the look and feel as requested

	switch ( LookAndFeel ) {

	case JAVA_LAF :
		setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ;
		break ;
	case WINDOWS_LAF :
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel") ;
		break ;
	case MOTIF_LAF :
		setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel") ;
		break ;
	case GTK_LAF :
		setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel") ;
		break ;
	}

	mMainGUI.resetDefaults() ; // So the index pane cell renderer follows

//	Reset any selections as the UI is back to square one !

	selectTuneBook(null) ;
	selectTune(null) ;
}


/**
 * Set the current look and feel.
 */
public void  setLookAndFeel( String s )
{
	try {
		UIManager.setLookAndFeel(s) ;
		SwingUtilities.updateComponentTreeUI(mFrame) ;
	}
	catch ( Exception e ) {
		System.out.println("* ERROR * - Unable to set Look And Feel") ;
		e.printStackTrace(System.out) ;
	}
}


/**
 * Select a specified tune book.
 */
public void selectTuneBook( TuneBook Book )
{
	if ( Book == mSelectedBook )   return ;

	mSelectedBook = Book ;
	mSelectedTune = null ;
	mSelectedList = null ;
	mMainGUI.selectTune(null) ;
	refreshMenu() ;

//	Tell the parser its now invalid

	mParserThread.queueTuneChanged(null) ;
}


/**
 * Select a specified tune.
 */
public void  selectTune( Tune Tune )
{
	if ( Tune == mSelectedTune )   return ;

	mSelectedBook = null ;
	mSelectedList = null ;
	mSelectedTune = Tune ;
	mMainGUI.selectTune(Tune) ;
	refreshMenu() ;

//	Tell the parser its now invalid and parse the new tune

	if ( mSelectedTune != null )
		mParserThread.queueTuneChanged( mSelectedTune.getABCText() ) ;
}


/**
 * Select a specified tune list.
 */
public void  selectTuneList( TuneList List )
{
	if ( List == mSelectedList )   return ;

	mSelectedList = List ;

	mSelectedTune = null ;
	mSelectedBook = null ;
	mMainGUI.selectTune(null) ;
	refreshMenu() ;

//	Tell the parser its now invalid

	mParserThread.queueTuneChanged(null) ;
}

/**
 * The selected tune has been changed in the editor.
 */
public void  tuneEditted( String NewText )
{

//  Set new text for the tune instance.
//  Note that this will cause the relevant modification flags to be updated

	mSelectedTune.setABCText(NewText) ;
	mMainGUI.tuneEditted(mSelectedTune) ;

//  Tell the parser to parse it

	mParserThread.queueParse(NewText) ;
}


/**
 * Save the whole library back to file.
 */
public void  saveLibrary()
{
	disableGUI() ;
	setStatusBarImmediately("Saving Library ...") ;
	mLibrary.save() ;
	setStatusBar("Done") ;
	enableGUI() ;
}


/**
 * Ensure the library gets saved on exit if modified.
 */
public void  windowClosing( WindowEvent e )
{

//	Save the GUI state to the property file and then save the properties

	saveGUIState() ;
	ABCJProperties.save() ;

//  Exit immediately if not modifed

	if ( mLibrary == null  ||  ! mLibrary.isModified() )   System.exit(0) ;

//  Do a dialog to prompt for confirmation and handle results accordingly

	int  Response = SaveConfirmDialog.doDialog(mFrame) ;

	if ( Response == JOptionPane.CANCEL_OPTION )   return ;
	if ( Response == JOptionPane.YES_OPTION )	   saveLibrary() ;

	System.exit(0) ;
}


/**
 * Open an ABC file.
 * 
 * <p>Prompt for a file name and add it to the library.
 */
public void  openABCFile()
{

//  Get the default path name to start the dialog

	String  PathName = ABCJProperties.getChooserPathName() ;
	if ( PathName == null )   PathName = "." ;

//  Do a dialog prompting for the file to add and return if cancelled

	JFileChooser  Dlg = new JFileChooser(PathName) ;
	Dlg.setFileFilter( new ABCFileFilter() ) ;

	if ( Dlg.showDialog( mFrame, "Open ABC File" ) != JFileChooser.APPROVE_OPTION )
		return ;

//  Store the new path name in the properties file

	ABCJProperties.setChooserPathName( Dlg.getCurrentDirectory().getAbsolutePath() ) ;

//  Check that the file actually exists

	String  FileName = Dlg.getSelectedFile().getAbsolutePath() ;

	if ( ! ( new File(FileName) ).exists() ) {
		ErrorDialog.doDialog( mFrame,
							  "File does not exist.   It cannot be added !" ) ;
		return ;
	}
	
//  Convert file name to relative if possible (to the current directory)
	
	FileName = Utils.toRelative(FileName) ;

//  Process the response and flag error if required

	TuneBook  Book = mLibrary.addTuneBook( FileName, false ) ;
	if ( Book == null ) {
		ErrorDialog.doDialog( mFrame,
							  "File already exists in the library."
								 + "   It will not be added again !");
		return ;
	}

//  Tune book has now been added at the end of the list 
//  so we should update the GUI too

	mMainGUI.addTuneBook(Book) ;
}


/**
 * Create a new ABC file.
 * 
 * <p>Prompt for a file name, create it and add it to the library.
 */
public void  createABCFile()
{

//	Get the default path name to start the dialog

	String  PathName = ABCJProperties.getChooserPathName() ;
	if ( PathName == null )   PathName = "." ;

//	Do a dialog prompting for the file to add and return if cancelled

	JFileChooser  Dlg = new JFileChooser(PathName) ;
	Dlg.setFileFilter( new ABCFileFilter() ) ;

	if ( Dlg.showDialog(mFrame, "Create ABC File") != JFileChooser.APPROVE_OPTION )
		return ;

//	Store the new path name in the properties file

	ABCJProperties.setChooserPathName( Dlg.getCurrentDirectory().getAbsolutePath() ) ;

//	Check that the file does not exists

	String  FileName = Dlg.getSelectedFile().getAbsolutePath() ;

	if ( ( new File(FileName) ).exists() ) {
		ErrorDialog.doDialog( mFrame,
							  "File already exists.   It cannot be created !" ) ;
		return ;
	}
	
//  Convert file name to relative if possible (to the current directory)
	
	FileName = Utils.toRelative(FileName) ;

//	Create the tune book in the library and add it to the GUI index

	mMainGUI.addTuneBook( mLibrary.createTuneBook(FileName) ) ;
}


/**
 * Delete a specified tunebook (an ABC file).
 * 
 * <p>Delete the selected ABC file entry.
 */
public void  removeSelectedTuneBook()
{

//  First remove it from the GUI and then the library

	TuneBook  Book = getSelectedTuneBook() ;
	mMainGUI.removeTuneBook(Book) ;
	mLibrary.removeTuneBook(Book) ;

	selectTuneBook(null) ;		// Ensure GUI has nothing
	selectTune(null) ;			// selected either
}


/**
 * Set the title of a specified tunebook.
 */
public void  setSelectedTuneBookTitle() 
{

//  Do a dialog to get the new title value and return if cancelled	

	String NewTitle = TuneBookTitleDialog.doDialog(	mFrame,
					mSelectedBook.getFileName(), mSelectedBook.getTitle() ) ;

	if ( NewTitle == null )   return ;

//  First update the title of the selected book, tell the library it has also
//  changed so that properties can be updated and finally tell the GUI
//  that the title has changed so the index tree can be redrawn

	mSelectedBook.setTitle(NewTitle) ;
	mLibrary.changeTuneBookTitle(mSelectedBook) ;

	mMainGUI.changeTuneBookTitle(mSelectedBook) ;
}


/**
 * Create a new Tune List.
 */
public void  createTuneList()
{

//	Create the tune book in the library and add it to the GUI index

	mMainGUI.addTuneList( mLibrary.createTuneList() ) ;
}


/**
 * Delete a specified tune list.
 */
public void  removeSelectedTuneList()
{

//	First remove it from the GUI and then the library

	TuneList  List = getSelectedList() ;
	mMainGUI.removeTuneList(List) ;
	mLibrary.removeTuneList(List) ;
}


/**
 * Set the title of a specified tune list.
 */
public void  setSelectedTuneListTitle() 
{

//	Do a dialog to get the new title value and return if cancelled	

	String NewTitle = TuneListTitleDialog.doDialog(	mFrame,
													mSelectedList.getTitle() ) ;
	if ( NewTitle == null )   return ;

//	First update the title of the selected list, tell the library it has also
//	changed so that properties can be updated and finally tell the GUI
//	that the title has changed so the index tree can be redrawn

	mSelectedList.setTitle(NewTitle) ;
	mLibrary.setModified(true) ;

	mMainGUI.changeTuneListTitle(mSelectedList) ;
}


/**
 * Refresh the menu.
 * 
 * <p>This should be called to enable/disable relevant items.
 */
public void  refreshMenu()
{   if(  mMenuBar != null )   mMenuBar.refresh() ;   }


/**
 * Set the initial GUI state.
 */
public void  setInitialGUIState()
{
	setEditMode( ABCJProperties.getEditMode() ) ;
	setLookAndFeel( ABCJProperties.getLookAndFeel() ) ;

//  Set the state of the main pane (e.g. divider locations)

	mMainPane.setInitialGUIState() ;

//  Set the initial frame rectangle from the properties file
//  This defaults to filling the screen

		Rectangle  r = ABCJProperties.getPropertyRect("FrameRect") ;

	if ( r.isEmpty() ) {
		Dimension  d = Toolkit.getDefaultToolkit().getScreenSize() ;
		r = new Rectangle( 0, 0, d.width, d.height ) ;
	}

	mFrame.setBounds(r) ;
}


/**
 * Save the GUI state to the properties file.
 */
public void  saveGUIState()
{
	mMainPane.saveGUIState() ;

	ABCJProperties.setProp( "FrameRect", mFrame.getBounds() ) ;
}


/**
 * Add a new tune to the selected book.
 */
public void  addNewTune()
{
	Tune  t = getSelectedTuneBook().createNewTune() ;

//  Now update the GUI as required

	mMainGUI.addNewTune(t) ;
}


/**
 * Delete selected tune.
 */
public void  deleteSelectedTune()
{
	Tune  t = getSelectedTune() ;
	mMainGUI.removeTune(t) ;
	mLibrary.removeTune(t) ;

	selectTuneBook(null) ;		// Ensure GUI has nothing
	selectTune(null) ;			// selected either
}


/**
 * Set the current status in the status bar.
 */
public void  setStatusBar( String Text )
{
	if ( Text == null )   Text = " " ;
	if ( mStatusBar != null )   mStatusBar.setText(Text) ;
}


/**
 * Set the current status in the status bar immediately.
 */
public void  setStatusBarImmediately( String Text )
{
	if ( Text == null )   Text = " " ;
	if ( mStatusBar != null )   mStatusBar.setTextImmediately(Text) ;
}


/**
 * Search the library for tunes.
 */
public void  searchLibrary( String SearchText, TuneSearchListener Target )
{
	setStatusBarImmediately("Searching ...") ;
	mLibrary.search( new SearchRequest(SearchText), Target ) ;
	setStatusBar("Done") ;
}


/**
 * Disable the GUI.
 * 
 * <p>This currently just sets an hourglass cursor.
 */
public void  disableGUI()
{   mFrame.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) ) ;   }


/**
 * Enable the GUI.
 * 
 * <p>This currently just sets a default cursor.
 */
public void  enableGUI()
{	mFrame.setCursor( Cursor.getDefaultCursor() ) ;   }


/**
 * The parser thread has communicated new status info.
 * 
 * <p>This method will be called on the main GUI thread using invokeLater.
 */
public void  receiveParserInfo( String[] Text, int Status )
{
	

//	Ignore it if no tune selected as it's just been deleted

	if ( mSelectedTune == null ) {
		Text = new String[] { "INothing to parse" } ; 
		Status = ABCParser.NO_RESULTS ; 
	}

//  Setup the status bar accordingly

	switch ( Status ) {
	case ABCParser.ABC_OK :			setStatusBar( "ABC is OK") ;		   break ;
	case ABCParser.ABC_WARNINGS :   setStatusBar( "ABC has Warnings" ) ;   break ;
	case ABCParser.ABC_ERRORS :		setStatusBar("ABC has Errors") ;	   break ;
	default :						setStatusBar("") ;					   break ;
	}

//  Pass the info through to the main pane for display

	mMainGUI.setEditorInfo( Text, Status ) ;

//  If no results then we should also clear the music pane

	mMainGUI.clearMusic() ;
}

/**
 * The parser thread has communicated new builder info.
 * 
 * <p>This method will be called on the main GUI thread using invokeLater.
 */
public void  receiveBuilderInfo( int Status )
{

//  Ignore it if no tune selected as it's just been deleted

	if ( mSelectedTune == null )   return ; 

//	Pass the info through to the main pane for display

	mMainGUI.showMusic( mMusicBuilder.getGlyphs(), mMusicBuilder.getDimension() ) ;
}


/**
 * Play selected tune.
 */
public void  playSelectedTune()
{

//  Wait till the parser has completed and is valid

	mParser.waitForResult() ;

	mParserThread.setPaused(true) ;		// So the data is locked

//  Build the necessary MIDI sequence and then play

	ABCSequence  Seq = new ABCSequence( mParser.getABCHeader(),
									    mParser.getPhrases() ) ;

	PlayerDialog  Dlg = new PlayerDialog( mFrame, mMainGUI, Seq ) ;
	
//  Check the dialog for correct initialization
	
	if ( ! Dlg.isOK() ) {
		JOptionPane.showMessageDialog( mFrame,
									   "Cannot create a MIDI PLayer.\n\n" 
									 + "This may because you have JMF installed which\n"
									 + "has some compatibility issues with versions of\n"
									 + "Java beyond 1.4.2.   Plese uninstall JMF or\n"
									 + "use Java 1.4.2 for correct functioning of the\n"
									 + "ABCJ Player or go to the ABCJ website for further"
									 + "options.",
									   "Player Problem", JOptionPane.ERROR_MESSAGE ) ;
		Dlg.dispose() ;
		return ;
	}
	
//  All OK - show the dialog

	Dlg.show() ;

	mParserThread.setPaused(false) ;		// So the data is unlocked
}


/**
 * Transpose selected tune.
 */
public void  transposeSelectedTune()
{

//  Wait till the parser has completed and is valid

	mParser.waitForResult() ;

//  Do the transpose dialog
	
	int  Transpose = TuneTransposeDialog.doDialog( mFrame,
								mParser.getABCHeader().getKeyElement().getKey() ) ;
	
	if ( Transpose == 0 )   return ;
	
//  Process a transposition
	
	mParserThread.setPaused(true) ;		// So the data is locked
		
	ABCHeader  Hdr   = mParser.getABCHeader() ;
	ABCMusic   Music = mParser.getABCMusic() ;
		
	ABCTransposer  Transposer = new ABCTransposer(Transpose) ;
	
	if ( ! Transposer.transpose(Hdr) ) {
		mParserThread.setPaused(false) ;
		ErrorDialog.doDialog( mFrame, "Cannot transpose key signature" ) ;
		return ;
	}
	
	if ( ! Transposer.transpose(Music) ) {
		mParserThread.setPaused(false) ;
		ErrorDialog.doDialog( mFrame, "Cannot transpose music" ) ;
		return ;
	}
	
//  Apply the transposition to the tune and reparse

	mSelectedTune.setABCText( Hdr.format() + Music.format() ) ;
	
	mParserThread.setPaused(false) ;		// So the data is unlocked
	
	mMainGUI.selectTune(mSelectedTune) ;	// To force text update
	mParserThread.queueTuneChanged( mSelectedTune.getABCText() ) ;
}


/**
 * Create Midi File from selected tune.
 */
public void  createMidiFileFromSelectedTune()
{

//	Wait till the parser has completed and is valid

	mParser.waitForResult() ;

//	Get the default path name to start the dialog

	String  PathName = ABCJProperties.getChooserPathName() ;
	if ( PathName == null )   PathName = "." ;

//	Do a dialog prompting for the file to build and return if cancelled

	JFileChooser  Dlg = new JFileChooser(PathName) ;
	Dlg.setFileFilter( new MidiFileFilter() ) ;
	Dlg.setDialogType(JFileChooser.SAVE_DIALOG) ;

	if ( Dlg.showDialog(mFrame, "Create MIDI File") != JFileChooser.APPROVE_OPTION )
		return ;

//	Store the new path name in the properties file

	ABCJProperties.setChooserPathName( Dlg.getCurrentDirectory().getAbsolutePath() ) ;

//	Check if the file actually exists

	String  FileName = Dlg.getSelectedFile().getAbsolutePath() ;

	if ( ( new File(FileName) ).exists() )
		if ( FileOverwriteDialog.doDialog( mFrame, FileName )
													== JOptionPane.NO_OPTION )
			return ;

//	Build the necessary MIDI sequence

	mParserThread.setPaused(true) ;		// So the data is locked

	ABCSequence Seq = new ABCSequence( mParser.getABCHeader(),
									   mParser.getPhrases() ) ;

//	Finally write it out to the given file

	try {
		File  OutFile = new File(FileName) ;
		MidiSystem.write( Seq.getMidiSequence(), Seq.getMidiFileFormat(), OutFile ) ;
	}
	catch ( IOException e ) {
		System.out.println( "Error creating MIDI file " + FileName ) ;
		e.printStackTrace() ;
	}

	mParserThread.setPaused(false) ;		// So the data is unlocked
}


/**
 * Prompt for and set a zoom factor.
 */
public void  setZoom()
{

//	Do a dialog prompting for the zoom factor and respond accordingly

	float  ZoomFactor = ZoomDialog.doDialog( mFrame, mMainGUI.getZoomFactor() ) ;

	if ( ZoomFactor > 0 )   mMainGUI.setZoomFactor(ZoomFactor) ;
}


/**
 * Prompt for and set the player options.
 */
public void  setPlayerOptions()
{
	( new PlayerOptionsDialog(mFrame) ).show() ;
}


/**
 * Prompt for and set the score options.
 */
public void  setScoreOptions()
{
	( new ScoreOptionsDialog(mFrame) ).show() ;

//  Update the builder with the new values
	
	int  w = ABCJProperties.getPropertyInt("Score.StaveWidth") ;
	MusicBuilder.setStaveWidth(w);
	w = ABCJProperties.getPropertyInt("Score.ShowTempo") ;
	MusicBuilder.setShowTempo( w != 0 );
	w = ABCJProperties.getPropertyInt("Score.IgnoreBreaks") ;
	MusicBuilder.setIgnoreBreaks( w != 0 ) ;
	w = ABCJProperties.getPropertyInt("Score.ShowInfoFields") ;
	MusicBuilder.setShowInfoFields( w != 0 ) ;
	w = ABCJProperties.getPropertyInt("Score.ShowLyrics") ;
	MusicBuilder.setShowLyrics( w != 0 ) ;
	w = ABCJProperties.getPropertyInt("Score.ShowClef") ;
	MusicBuilder.setShowClef( w != 0 ) ;

//	Force an update of the score	

	if ( mSelectedTune != null )
		mParserThread.queueTuneChanged( mSelectedTune.getABCText() ) ;
}


/**
 * Print the currently selected book.
 */
public void  printSelectedBook()
{
	
//	Create a music printer for the list of tunes in the book and print them

	( new MusicPrinter( mSelectedBook.getTunes() ) ).print() ; 
}


/**
 * Print the currently selected tune.
 */
public void  printSelectedTune()
{

//	Create a music printer for the single tune and print it

	( new MusicPrinter(mSelectedTune) ).print() ; 
}


/**
 * Print the currently selected list.
 */
public void  printSelectedList()
{
	
//  Create a music printer for the list of tunes and print them

	( new MusicPrinter( mSelectedList.getTunes() ) ).print() ; 
}


/**
 * Add tunes to a tune list.
 */
public void  addTunesToList( ArrayList Tunes, TuneList List, Tune AfterTune,
							 boolean AddAtEnd )
{	

//  Add the tunes to the list

	List.addTunes( Tunes, AfterTune, AddAtEnd ) ; 

//  Now force the GUI to refresh this list completely

	mMainGUI.refreshTuneList(List) ;
}


/**
 * Remove a tune from a list.
 */
public void  deleteTuneFromList( TuneList List, Tune Tune )
{
	List.removeTune(Tune) ;	

//	Now force the GUI to refresh this list completely

	mMainGUI.refreshTuneList(List) ;
}


/**
 * Show the help frame.
 */
public void  showHelp() 
{
	( new HelpFrame() ).setVisible(true) ;
}


/**
 * Show the About dialog.
 */
public void  showAboutDialog()
{
	AboutDialog.doDialog(mFrame) ;
}


/**
 * The main executable method for this application.
 */
public static void  main( String[] args )
{

//  Put up a splash window whilst the application loads
//  This will disappear after 5 seconds or when it is clicked

	new SplashWindow( "images/splash.gif", null, 5000 ) ;
	
	Utils.sleep(1000) ;

//  Check for the ABCJ Junior flag at startup
	
	boolean  JuniorFlag = false ;
	if ( args.length > 0
	 &&  args[0].toUpperCase().charAt(0) == 'J' )
		JuniorFlag = true ;
	
//	Schedule a job for the event-dispatching thread:
//	creating and showing this application's GUI.
//
//  The job to schedule will be full ABCJ or just ABCJunior.

//  Put the creation of this object on the event dispatching Queue
//  so that everything is OK when it gets finally created.

	if ( JuniorFlag ) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ABCJunior  a = new ABCJunior() ;
				a.initApplication() ;
			}
		} ) ;
	}
	else {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ABCJ  a = new ABCJ() ;
				a.initApplication() ;
			}
		} ) ;
	}
}

}
