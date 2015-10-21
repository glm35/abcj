/**
 * The outermost Pane of the ABCJ GUI as an applet.
 *
 * <p>This is a much simplified version of the main GUI for use witrhin a web page.   It is
 * also used by ABCJunior.
 * 
 * <p>In advanced mode it also provides a 'save to midi' and an 'exit' button.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  java.awt.event.* ;

import  javax.swing.* ;

import  java.util.* ;

import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.model.* ;
import  com.ganderband.abcj.model.abc.* ;
import  com.ganderband.abcj.model.music.* ;

public class AppletMainPane extends JPanel implements ABCJConstants, MainGUI, ActionListener
{
/**
 * The owning ABCJ appication instance.
 */
	private ABCJ  mABCJ ;
/**
 * The Editor top text info pane (the tabbed split pane at the top) - music or errors.
 */
	private JTabbedPane  mEditorPaneTextInfo ;
/**
 * The Editor pane text editting pane.
 */
	private JSplitPane  mEditorPaneText ;
/**
 * The Text Mode - Text editor pane.
 */
	private TextEditTextPane  mTextEditTextPane ;
/**
 * The Text Mode - Music display pane.
 */
	private TextEditMusicPane  mTextEditMusicPane ;
/**
 * The Text Mode - ABC parsing errors.
 */
	private TextEditErrorPane  mTextEditErrorPane ;
/**
 * The music zoom factor to use on all panes.
 */
	private float  mZoomFactor ;
/**
 * The Play Button.
 */
	private JButton mPlayButton ;
/**
 * The Print Button.
 */
	private JButton mPrintButton ;
/**
 * The Save to Midi Button.
 */
	private JButton mSaveToMidiButton ;
/**
 * The Exit Button.
 */
	private JButton mExitButton ;
/**
 * Are we in advanced mode or not ?
 */
	private boolean  mAdvancedMode = false ;

	
/**
 * The default constructor.
 */
public  AppletMainPane( ABCJ App, boolean AdvancedMode )
{
	mABCJ         = App ;
	mAdvancedMode = AdvancedMode ;
	
	App = mABCJ ;	// To remove a warning error !

	setAlignmentX(Component.LEFT_ALIGNMENT) ;		// So it works OK in a BoxLayout

	setLayout( new BorderLayout() ) ;

//  Build the components

	mTextEditTextPane  = new TextEditTextPane(App) ;
	mTextEditMusicPane = new TextEditMusicPane(App) ;
	mTextEditErrorPane = new TextEditErrorPane(App) ;

//	Build the text editor pane

	mEditorPaneTextInfo = new JTabbedPane() ;
	mEditorPaneTextInfo.setTabPlacement(SwingConstants.BOTTOM) ;
	
	mEditorPaneTextInfo.addTab( "Music",  mTextEditMusicPane ) ;
	mEditorPaneTextInfo.addTab( "Errors", mTextEditErrorPane ) ;

	mEditorPaneText = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
								  mEditorPaneTextInfo, mTextEditTextPane ) ;
	mEditorPaneText.setOneTouchExpandable(true) ;
	mEditorPaneText.setResizeWeight(0.8) ;
	
//  Finally add it to the parent
	
	add( BorderLayout.CENTER, mEditorPaneText ) ;
	
//  Now create and add the button panel at the top

	mPrintButton       = new JButton("Print") ;
	mPlayButton        = new JButton("Play") ;
	mSaveToMidiButton  = new JButton("Save To Midi") ;
	mExitButton        = new JButton("Exit") ;
	
	mPrintButton.addActionListener(this) ;
	mPlayButton.addActionListener(this) ;
	mSaveToMidiButton.addActionListener(this) ;
	mExitButton.addActionListener(this) ;
	
	JPanel  p = new JPanel() ;
	
	if ( mAdvancedMode ) {
		p.setLayout( new GridLayout( 1, 6 ) ) ;
	
		p.add(mPrintButton) ;
		p.add( new JLabel() ) ;
		p.add(mPlayButton) ;
		p.add(mSaveToMidiButton) ;
		p.add( new JLabel() ) ;
		p.add(mExitButton) ;
	}
	else {
		p.setLayout( new GridLayout( 1, 5 ) ) ;
		
		p.add( new JLabel() ) ;
		p.add(mPrintButton) ;
		p.add( new JLabel() ) ;
		p.add(mPlayButton) ;
		p.add( new JLabel() ) ;
	}

	add( BorderLayout.NORTH, p ) ;
	
//  Set the parameters for initial music display
	
	setZoomFactor(1.0f) ;
	
	MusicBuilder.setStaveWidth(700) ;
}


/**
 * Set the information in the editor text info pane.
 */
public void  setEditorInfo( String[] Text, int Status )
{
	
//  Indicate the status through the tab text color
	
	Color  c = null ;
	
	switch ( Status ) {
	case  ABCParser.ABC_OK :         c = Color.green ;     break ; 
	case  ABCParser.ABC_WARNINGS :   c = Color.blue ;	   break ; 
	case  ABCParser.ABC_ERRORS :     c = Color.red ;	   break ; 
	case  ABCParser.NO_RESULTS :     c = Color.black ;	   break ; 
	}

	mEditorPaneTextInfo.setForegroundAt( 1, c ) ;
	
//	Pass the info through to the text edit error pane

	mTextEditErrorPane.setText(Text) ; 
}


/**
 * Indicate that the music notation has been changed and should be repainted.
 */
public void  showMusic( ArrayList Glyphs, Dimension d )
{
	mTextEditMusicPane.showMusic( Glyphs, d ) ;
}


/**
 * Indicate that the music notation panes should be cleared.
 */
public void  clearMusic()
{
	mTextEditMusicPane.showMusic( null, new Dimension( 20, 20 ) ) ;
}


/**
 * Get the current zoom factor.
 */
public float  getZoomFactor()
{	return  mZoomFactor ;   }


/**
 * Set the current zoom factor.
 */
public void  setZoomFactor( float ZoomFactor )
{
	
//  Save and pass it through to the required panes

	mZoomFactor = ZoomFactor ;
	
	mTextEditMusicPane.setZoomFactor(ZoomFactor) ;
}


/**
 * Handle a button press.
 */
public void  actionPerformed( ActionEvent e )
{

//	Handle Print Tune option

	if ( e.getSource() == mPrintButton )
		mABCJ.printSelectedTune() ;
	
//	Handle Play Tune option

	if ( e.getSource() == mPlayButton )
		mABCJ.playSelectedTune() ;
	
//	Handle Save To Midi option

	if ( e.getSource() == mSaveToMidiButton )
		mABCJ.createMidiFileFromSelectedTune() ;
	
//	Handle Exit option

	if ( e.getSource() == mExitButton )
		mABCJ.windowClosing(null) ;
}


/**
 * Set the current edit mode.
 */
public void  setEditMode( int EditMode )
{
	//  Do nothing for an applet - text mode only
}


/**
 * Reset any defaults in case the Look and Feel has changed.
 */
public void  resetDefaults()
{
	//  Do nothing for an applet - text mode only
}


/**
 * Select a specified tune.
 */
public void  selectTune( Tune Tune )
{
	mTextEditTextPane.setInitialText( ( Tune != null ) ? Tune.getABCText() : "" ) ;
	
//  Clear any music panes if no tune selected

	if ( Tune == null )   clearMusic() ;
}


/**
 * Tune ABC Text has been editted.
 */
public void  tuneEditted( Tune Tune )
{
//  Nothing required here - it's all been handled by now !
}


/**
 * A new tune book has been added to the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  addTuneBook( TuneBook Book )
{
	//  Do nothing for an applet - tune books not used
}


/**
 * A tune book has been deleted from the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  removeTuneBook( TuneBook Book )
{
	//  Do nothing for an applet - tune books not used
}


/**
 * A book title has been changed.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  changeTuneBookTitle( TuneBook Book )
{
	//  Do nothing for an applet - tune books not used
}


/**
 * A new tune list has been added to the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  addTuneList( TuneList List )
{
	//  Do nothing for an applet - tune lists not used
}


/**
 * A tune list has been deleted from the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  removeTuneList( TuneList List )
{
	//  Do nothing for an applet - tune lists not used
}


/**
 * Refresh a tune list after tunes have been added.
 */
public void  refreshTuneList( TuneList List )
{
	//  Do nothing for an applet - tune lists not used
}


/**
 * A list title has been changed.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  changeTuneListTitle( TuneList List )
{
	//  Do nothing for an applet - tune lists not used
}


/**
 * A tune has been added to the current tunebook.
 */
public void  addNewTune( Tune Tune )
{
	//  Do nothing for an applet - only one tune used
}


/**
 * A tune has been removed from the current tunebook.
 */
public void  removeTune( Tune Tune )
{
	//  Do nothing for an applet - only one tune used
}


/**
 * Set the initial GUI state.
 */
public void  setInitialGUIState()
{
	
//  Set the divider location for the text editor pane

	int DivLoc = ABCJProperties.getPropertyInt("ui.TextEditDividerLocation") ;
	if ( DivLoc > 0 )
	    mEditorPaneText.setDividerLocation(DivLoc) ;
	else {
		mEditorPaneText.setDividerLocation(0.5) ;
		mEditorPaneText.setDividerLocation( mEditorPaneText.getDividerLocation() ) ;
	}
	
//  Get and set the zoom factor

	float  ZoomFactor = ABCJProperties.getPropertyFloat("ui.ZoomFactor") ;
	if ( ZoomFactor == 0 )   ZoomFactor = 1.0F ;
	
	setZoomFactor(ZoomFactor) ;
}


/**
 * Save the GUI state to the properties file.
 */
public void  saveGUIState()
{
	
//  Save all three divider locations

	ABCJProperties.setProp( "ui.TextEditDividerLocation",
							mEditorPaneText.getDividerLocation() ) ;
}

}
