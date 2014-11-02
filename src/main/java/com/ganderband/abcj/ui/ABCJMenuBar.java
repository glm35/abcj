/**
 * The ABCJ MenuBar class.
 */
package  abcj.ui ;

import  java.awt.* ;
import  java.awt.event.* ;
import  javax.swing.* ;
import  abcj.* ;

public class ABCJMenuBar extends JMenuBar implements ActionListener, ABCJConstants
{
/**
 * The owning ABCJ application instance.
 */
	ABCJ  mABCJ ;
/**
 * The Books menu.
 */
	private JMenu  mBooksMenu ;
/**
 * The Tunes menu.
 */
	private JMenu  mTunesMenu ;
/**
 * The Lists menu.
 */
	private JMenu  mListsMenu ;
/**
 * The File - Open ABC File menu item. 
 */
	private JMenuItem  mFileOpen ;
/**
 * The File - New ABC File menu item. 
 */
	private JMenuItem  mFileNew ;
/**
 * The File - Save menu item. 
 */
	private JMenuItem  mFileSave ;
/**
 * The File - Print Book menu item. 
 */
	private JMenuItem  mFilePrintBook ;
/**
 * The File - Print Tune menu item. 
 */
	private JMenuItem  mFilePrintTune ;
/**
 * The File - Print List menu item. 
 */
	private JMenuItem  mFilePrintList ;
/**
 * The File - Exit menu item. 
 */
	private JMenuItem  mFileExit ;
/**
 * The Books - Add menu item. 
 */
	private JMenuItem  mBooksAdd ;
/**
 * The Books - Create menu item. 
 */
	private JMenuItem  mBooksCreate ;
/**
 * The Books - Remove menu item. 
 */
	private JMenuItem  mBooksRemove ;
/**
 * The Books - Set Title menu item. 
 */
	private JMenuItem  mBooksSetTitle ;
/**
 * The Books - Print menu item. 
 */
	private JMenuItem  mBooksPrint ;
/**
 * The Tunes - New Tune menu item. 
 */
	private JMenuItem  mTunesNew ;
/**
 * The Tunes - Delete Tune menu item. 
 */
	private JMenuItem  mTunesDelete ;
/**
 * The Tunes - Play Tune menu item. 
 */
	private JMenuItem  mTunesPlay ;
/**
 * The Tunes - Create Midi File menu item. 
 */
	private JMenuItem  mTunesMidiFile ;
/**
 * The Tunes - Transpose Tune menu item. 
 */
	private JMenuItem  mTunesTranspose ;
/**
 * The Tunes - Print menu item. 
 */
	private JMenuItem  mTunesPrint ;
/**
 * The Listss - Create menu item. 
 */
	private JMenuItem  mListsCreate ;
/**
 * The Lists - Remove menu item. 
 */
	private JMenuItem  mListsRemove ;
/**
 * The Lists - Set Title menu item. 
 */
	private JMenuItem  mListsSetTitle ;
/**
 * The Lists - Print menu item. 
 */
	private JMenuItem  mListsPrint ;
/**
 * The Options - Set Zoom Factor menu item. 
 */
	private JMenuItem  mOptionsZoom ;
/**
 * The Options - Player Options menu item. 
 */
	private JMenuItem  mOptionsPlayerOptions ;
/**
 * The Options - Score Options menu item. 
 */
	private JMenuItem  mOptionsScoreOptions ;
/**
 * The Options - Java LAF menu item. 
 */
	private JMenuItem  mOptionsJavaLAF ;
/**
 * The Options - Windows LAF menu item. 
 */
	private JMenuItem  mOptionsWindowsLAF ;
/**
 * The Options - Motif LAF menu item. 
 */
	private JMenuItem  mOptionsMotifLAF ;
/**
 * The Options - GTK LAF menu item. 
 */
	private JMenuItem  mOptionsGTKLAF ;
/**
 * The Help - Help menu item. 
 */
	private JMenuItem  mHelpHelp ;
/**
 * The Help - About menu item. 
 */
	private JMenuItem  mHelpAbout ;


/**
 * The standard constructor.
 */
public  ABCJMenuBar( ABCJ App )
{
	mABCJ = App ;
	
//  Construct the file menu

	JMenu  Menu = new JMenu("File") ;
	add(Menu) ;

//  Add the open, new, save and exit options to the file menu

	mFileOpen = new JMenuItem("Open ...") ;
	mFileOpen.addActionListener(this) ;
	Menu.add(mFileOpen) ;
	
	mFileNew = new JMenuItem("New ...") ;
	mFileNew.addActionListener(this) ;
	Menu.add(mFileNew) ;
	
	Menu.addSeparator() ;

	mFileSave = new JMenuItem("Save") ;
	mFileSave.addActionListener(this) ;
	mFileSave.setAccelerator(
			KeyStroke.getKeyStroke( 'S',
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false ) ) ;
	Menu.add(mFileSave) ;
	
	Menu.addSeparator() ;

	mFilePrintBook = new JMenuItem("Print Selected Book ...") ;
	mFilePrintBook.addActionListener(this) ;
	Menu.add(mFilePrintBook) ;

	mFilePrintTune = new JMenuItem("Print Selected Tune ...") ;
	mFilePrintTune.addActionListener(this) ;
	Menu.add(mFilePrintTune) ;

	mFilePrintList = new JMenuItem("Print Selected List ...") ;
	mFilePrintList.addActionListener(this) ;
	Menu.add(mFilePrintList) ;
	
	Menu.addSeparator() ;

	mFileExit = new JMenuItem("Exit") ;
	mFileExit.addActionListener(this) ;
	Menu.add(mFileExit) ;

//  Construct the books menu

	mBooksMenu = new JMenu("Books") ;
	add(mBooksMenu) ;

//  Add the options

	mBooksAdd = new JMenuItem("Add (from ABC File) ...") ;
	mBooksAdd.addActionListener(this) ;
	mBooksMenu.add(mBooksAdd) ;

	mBooksCreate = new JMenuItem("Create (new ABC File) ...") ;
	mBooksCreate.addActionListener(this) ;
	mBooksMenu.add(mBooksCreate) ;

	mBooksRemove = new JMenuItem("Remove Selected Book") ;
	mBooksRemove.addActionListener(this) ;
	mBooksMenu.add(mBooksRemove) ;

	mBooksSetTitle = new JMenuItem("Set Title for Selected Book ...") ;
	mBooksSetTitle.addActionListener(this) ;
	mBooksMenu.add(mBooksSetTitle) ;

	mBooksPrint = new JMenuItem("Print Selected Book ...") ;
	mBooksPrint.addActionListener(this) ;
	mBooksMenu.add(mBooksPrint) ;

//	Construct the tunes menu

	mTunesMenu = new JMenu("Tunes") ;
	add(mTunesMenu) ;

//	Add the options

	mTunesNew = new JMenuItem("New Tune (in Selected Book)") ;
	mTunesNew.addActionListener(this) ;
	mTunesMenu.add(mTunesNew) ;

	mTunesDelete = new JMenuItem("Delete Selected Tune") ;
	mTunesDelete.addActionListener(this) ;
	mTunesMenu.add(mTunesDelete) ;

	mTunesPlay = new JMenuItem("Play Selected Tune") ;
	mTunesPlay.setAccelerator(
			KeyStroke.getKeyStroke( 'P',
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false ) ) ;
	mTunesPlay.addActionListener(this) ;
	mTunesMenu.add(mTunesPlay) ;

	mTunesTranspose = new JMenuItem("Transpose Selected Tune") ;
	mTunesTranspose.setAccelerator(
			KeyStroke.getKeyStroke( 'T',
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false ) ) ;
	mTunesTranspose.addActionListener(this) ;
	mTunesMenu.add(mTunesTranspose) ;

	mTunesMidiFile = new JMenuItem("Create MIDI File from Selected Tune ...") ;
	mTunesMidiFile.addActionListener(this) ;
	mTunesMenu.add(mTunesMidiFile) ;

	mTunesPrint = new JMenuItem("Print Selected Tune ...") ;
	mTunesPrint.addActionListener(this) ;
	mTunesMenu.add(mTunesPrint) ;

//	Construct the lists menu

	mListsMenu = new JMenu("Lists") ;
	add(mListsMenu) ;

//	Add the options

	mListsCreate = new JMenuItem("Create New List") ;
	mListsCreate.addActionListener(this) ;
	mListsMenu.add(mListsCreate) ;

	mListsRemove = new JMenuItem("Remove Selected List") ;
	mListsRemove.addActionListener(this) ;
	mListsMenu.add(mListsRemove) ;

	mListsSetTitle = new JMenuItem("Set Title for Selected List ...") ;
	mListsSetTitle.addActionListener(this) ;
	mListsMenu.add(mListsSetTitle) ;

	mListsPrint = new JMenuItem("Print Selected List ...") ;
	mListsPrint.addActionListener(this) ;
	mListsMenu.add(mListsPrint) ;
	
//	Construct the options menu

	Menu = new JMenu("Options") ;
	add(Menu) ;

//  Add the options

	mOptionsZoom = new JMenuItem("Zoom ...") ;
	mOptionsZoom.addActionListener(this) ;
	Menu.add(mOptionsZoom) ;
	
	mOptionsPlayerOptions = new JMenuItem("Player Options ...") ;
	mOptionsPlayerOptions.addActionListener(this) ;
	Menu.add(mOptionsPlayerOptions) ;
	
	mOptionsScoreOptions = new JMenuItem("Score Options ...") ;
	mOptionsScoreOptions.addActionListener(this) ;
	Menu.add(mOptionsScoreOptions) ;
	
//	Add the Look and Feel radio buttons to the options menu

	Menu.addSeparator() ;	

	ButtonGroup  Group = new ButtonGroup() ;
	
	mOptionsJavaLAF = new JRadioButtonMenuItem("Java Look and Feel") ;
	mOptionsJavaLAF.setSelected(true) ;
	mOptionsJavaLAF.addActionListener(this) ;
	Group.add(mOptionsJavaLAF) ;
	Menu.add(mOptionsJavaLAF) ;
	
	mOptionsWindowsLAF = new JRadioButtonMenuItem("Windows Look and Feel") ;
	mOptionsWindowsLAF.addActionListener(this) ;
	Group.add(mOptionsWindowsLAF) ;
	Menu.add(mOptionsWindowsLAF) ;
	
	mOptionsMotifLAF = new JRadioButtonMenuItem("Motif Look and Feel") ;
	mOptionsMotifLAF.addActionListener(this) ;
	Group.add(mOptionsMotifLAF) ;
	Menu.add(mOptionsMotifLAF) ;
	
	mOptionsGTKLAF = new JRadioButtonMenuItem("GTK Look and Feel") ;
	mOptionsGTKLAF.addActionListener(this) ;
	Group.add(mOptionsGTKLAF) ;
	Menu.add(mOptionsGTKLAF) ;
	
//	Construct the help menu

	Menu = new JMenu("Help") ;
	add(Menu) ;

//  Add the help menu items

	mHelpHelp = new JMenuItem("Help Contents") ;
	mHelpHelp.addActionListener(this) ;
	Menu.add(mHelpHelp) ;

	Menu.addSeparator() ;	
	
	mHelpAbout = new JMenuItem("About ...") ;
	mHelpAbout.addActionListener(this) ;
	Menu.add(mHelpAbout) ;
	
//  Ensure the relevant items are enabled or disabled.

	refresh() ;
}


/**
 * Set the current look and feel.
 */
public void  setLookAndFeel( int LookAndFeel )
{
	
//	Select the correct menu item

	switch ( LookAndFeel ) {
	
	case  JAVA_LAF :
		mOptionsJavaLAF.setSelected(true) ;
		break ;
		
	case  WINDOWS_LAF :
		mOptionsWindowsLAF.setSelected(true) ;
		break ;
		
	case  MOTIF_LAF :
		mOptionsMotifLAF.setSelected(true) ;
		break ;
	case  GTK_LAF :
		mOptionsGTKLAF.setSelected(true) ;
		break ;
	}
}


/**
 * Handle menu actions as they are performed.
 */
public void  actionPerformed( ActionEvent e )
{
	
//  Handle File->Open option (same as Books->Add)

	if ( e.getSource() == mFileOpen )
		mABCJ.openABCFile() ;
	
//	Handle File->New option (same as Books->Create)

	if ( e.getSource() == mFileNew )
		mABCJ.createABCFile() ;
	
//	Handle File->Save option

	if ( e.getSource() == mFileSave )
		mABCJ.saveLibrary() ;
	
//	Handle File->Print Book option

	if ( e.getSource() == mFilePrintBook )
		mABCJ.printSelectedBook() ;
	
//	Handle File->Print Tune option

	if ( e.getSource() == mFilePrintTune )
		mABCJ.printSelectedTune() ;
	
//	Handle File->Print List option

	if ( e.getSource() == mFilePrintList )
		mABCJ.printSelectedList() ;
	
//	Handle File->Exit option

	if ( e.getSource() == mFileExit )
		mABCJ.windowClosing(null) ;
	
//	Handle Books->Add option (Same as File->OPen)

	if ( e.getSource() == mBooksAdd )
		mABCJ.openABCFile() ;
	
//	Handle Books->Create option (Same as File->New)

	if ( e.getSource() == mBooksCreate )
		mABCJ.createABCFile() ;
	
//	Handle Books->Remove option

	if ( e.getSource() == mBooksRemove )
		mABCJ.removeSelectedTuneBook() ;
	
//	Handle Books->Set Title option

	if ( e.getSource() == mBooksSetTitle )
		mABCJ.setSelectedTuneBookTitle() ;
	
//	Handle Books->Print option

	if ( e.getSource() == mBooksPrint )
		mABCJ.printSelectedBook() ;
	
//	Handle Tunes->New Tune option

	if ( e.getSource() == mTunesNew )
		mABCJ.addNewTune() ;
	
//	Handle Tunes->Delete Tune option

	if ( e.getSource() == mTunesDelete )
		mABCJ.deleteSelectedTune() ;
	
//	Handle Tunes->Play Tune option

	if ( e.getSource() == mTunesPlay )
		mABCJ.playSelectedTune() ;
	
//	Handle Tunes->Transpose Tune option

	if ( e.getSource() == mTunesTranspose )
		mABCJ.transposeSelectedTune() ;

//	Handle Tunes->Create Midi File option

	if ( e.getSource() == mTunesMidiFile )
		mABCJ.createMidiFileFromSelectedTune() ;
	
//	Handle Lists->Create option

	if ( e.getSource() == mListsCreate )
		mABCJ.createTuneList() ;
	
//	Handle Lists->Remove option

	if ( e.getSource() == mListsRemove )
		mABCJ.removeSelectedTuneList() ;

//	Handle Lists->Set Title option

	if ( e.getSource() == mListsSetTitle )
		mABCJ.setSelectedTuneListTitle() ;
	
//	Handle Lists->Print List option

	if ( e.getSource() == mListsPrint )
		mABCJ.printSelectedList() ;
	
//	Handle File->Print option

	if ( e.getSource() == mTunesPrint )
		mABCJ.printSelectedTune() ;
	
//	Handle Options->Zoom

	if ( e.getSource() == mOptionsZoom )
		mABCJ.setZoom() ;
	
//	Handle Options->Player Options

	if ( e.getSource() == mOptionsPlayerOptions )
		mABCJ.setPlayerOptions() ;
	
//	Handle Options->Score Options

	if ( e.getSource() == mOptionsScoreOptions )
		mABCJ.setScoreOptions() ;
	
//	Handle Options->Java Look and Feel

	if ( e.getSource() == mOptionsJavaLAF )
		mABCJ.setLookAndFeel(JAVA_LAF) ;
	
//	Handle Options->Windows Look and Feel

	if ( e.getSource() == mOptionsWindowsLAF )
		mABCJ.setLookAndFeel(WINDOWS_LAF) ;
	
//	Handle Options->Motif Look and Feel

	if ( e.getSource() == mOptionsMotifLAF )
		mABCJ.setLookAndFeel(MOTIF_LAF) ;
	
//	Handle Options->GTK Look and Feel

	if ( e.getSource() == mOptionsGTKLAF )
		mABCJ.setLookAndFeel(GTK_LAF) ;
	
//	Handle Help->Help Contents

	if ( e.getSource() == mHelpHelp )
		mABCJ.showHelp() ;
	
//	Handle Help->About

	if ( e.getSource() == mHelpAbout )
		mABCJ.showAboutDialog() ;
}


/**
 * Refresh menu.
 * 
 * <p>This method enables or disables options relating to what is selected.
 */
public void  refresh()
{
	boolean  IsBookSelected = ( mABCJ.getSelectedTuneBook() != null ) ;
	boolean  IsTuneSelected = ( mABCJ.getSelectedTune() != null ) ;
	boolean  IsListSelected = ( mABCJ.getSelectedList() != null ) ;
		
//  Enable or disable the book related menu items

	mBooksRemove.setEnabled(IsBookSelected) ;
	mBooksSetTitle.setEnabled(IsBookSelected) ;
	mBooksPrint.setEnabled(IsBookSelected) ;

//	Enable or disable the tune related menu items
	
	mTunesNew.setEnabled(IsBookSelected) ;
	mTunesDelete.setEnabled(IsTuneSelected) ;
	mTunesPlay.setEnabled(IsTuneSelected) ;
	mTunesTranspose.setEnabled(IsTuneSelected) ;
	mTunesMidiFile.setEnabled(IsTuneSelected) ;
	mTunesPrint.setEnabled(IsTuneSelected) ;
	
//  Enable or disable the list related menu items

	mListsRemove.setEnabled(IsListSelected) ;
	mListsSetTitle.setEnabled(IsListSelected) ;
	mListsPrint.setEnabled(IsListSelected) ;

//  Enable or disable the file menu items

	mFilePrintBook.setEnabled(IsBookSelected) ;
	mFilePrintTune.setEnabled(IsTuneSelected) ;
	mFilePrintList.setEnabled(IsListSelected) ;
}

}
