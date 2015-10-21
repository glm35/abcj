/**
 * The outermost Pane of the ABCJ GUI.
 *
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  javax.swing.* ;
import  javax.swing.event.* ;
import  java.util.* ;

import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.model.* ;
import  com.ganderband.abcj.model.abc.* ;

public class MainPane extends JSplitPane implements ABCJConstants, ChangeListener, MainGUI
{
/**
 * The owning ABCJ appication instance.
 */
	private ABCJ  mABCJ ;
/**
 * The Selector pane (the tabbed pane on the left hand side).
 */
	private JTabbedPane  mSelectorPane ;
/**
 * The Index pane.
 */
	private IndexPane  mIndexPane ;
/**
 * The Search pane.
 */
	private SearchPane  mSearchPane ;
/**
 * The Editor pane (the tabbed split pane on the right hand side).
 */
	private JTabbedPane  mEditorPane ;
/**
 * The Editor top text info pane (the tabbed split pane at the top) - music or errors.
 */
	private JTabbedPane  mEditorPaneTextInfo ;
/**
 * The Editor pane text editting pane.
 */
	private JSplitPane  mEditorPaneText ;
/**
 * The Editor pane music editting pane.
 */
	private JSplitPane  mEditorPaneMusic ;
/**
 * The Editor pane list editting pane.
 */
	private JSplitPane  mEditorPaneLists ;
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
 * The Music Mode - Header editting pane.
 */
	private MusicEditHeaderPane  mMusicEditHeaderPane ;
/**
 * The Music Mode - Music editting pane.
 */
	private MusicEditMusicPane  mMusicEditMusicPane ;
/**
 * The Lists Mode - Music display pane.
 */
	private ListEditMusicPane  mListEditMusicPane ;
/**
 * The Lists Mode - Lists editting pane.
 */
	private ListEditListPane  mListEditListPane ;
/**
 * The music zoom factor to use on all panes.
 */
	private float  mZoomFactor ;
	
	
/**
 * The default constructor.
 */
public  MainPane( ABCJ App )
{
	mABCJ = App ;

	setAlignmentX(Component.LEFT_ALIGNMENT) ;		// So it works OK in a BoxLayout

//  Build the components
	
	mIndexPane     = new IndexPane(App) ;
	mSearchPane    = new SearchPane(App) ;
	
	mTextEditTextPane    = new TextEditTextPane(App) ;
	mTextEditMusicPane   = new TextEditMusicPane(App) ;
	mTextEditErrorPane   = new TextEditErrorPane(App) ;
	mMusicEditHeaderPane = new MusicEditHeaderPane(App) ;
	mMusicEditMusicPane  = new MusicEditMusicPane(App) ;
	mListEditMusicPane   = new ListEditMusicPane(App) ;
	mListEditListPane    = new ListEditListPane(App) ;
	
//	Build the selector pane on the left

	mSelectorPane = new JTabbedPane() ;
	mSelectorPane.addTab( "Index",  mIndexPane ) ;
	mSelectorPane.addTab( "Search", mSearchPane ) ;
 	
//	Build the text editor pane on the right

	mEditorPaneTextInfo = new JTabbedPane() ;
	mEditorPaneTextInfo.setTabPlacement(SwingConstants.BOTTOM) ;
		
	mEditorPaneTextInfo.addTab( "Music",  mTextEditMusicPane ) ;
	mEditorPaneTextInfo.addTab( "Errors", mTextEditErrorPane ) ;


	mEditorPaneText = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
								  mEditorPaneTextInfo, mTextEditTextPane ) ;
	mEditorPaneText.setOneTouchExpandable(true) ;
	mEditorPaneText.setResizeWeight(0.50) ;
 	
//	Build the music editor pane on the right

	mEditorPaneMusic = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
								  mMusicEditHeaderPane, mMusicEditMusicPane ) ;
	mEditorPaneMusic.setOneTouchExpandable(true) ;
	mEditorPaneMusic.setResizeWeight(0.50) ;
	
	mEditorPane = new JTabbedPane() ;
 	
//	Build the list editor pane on the right

	mEditorPaneLists = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
								  mListEditMusicPane, mListEditListPane ) ;
	mEditorPaneLists.setOneTouchExpandable(true) ;
	mEditorPaneLists.setResizeWeight(0.50) ;
	
//  Add the three previous panes to the owning right hand pane
	
	mEditorPane = new JTabbedPane() ;
	
	mEditorPane.addTab( "Text Edit",  mEditorPaneText ) ;
//	mEditorPane.addTab( "Music Edit", mEditorPaneMusic ) ;
	mEditorPane.addTab( "Tune Lists Edit", mEditorPaneLists ) ;

//  Finally setup this main pane containing the above two

	setOrientation(JSplitPane.HORIZONTAL_SPLIT) ;
	setOneTouchExpandable(true) ;
	setResizeWeight(0.00) ;		// Give all new space to editor
	
	setLeftComponent(mSelectorPane) ;
	setRightComponent(mEditorPane) ;
	
//  Listen to the tabbed editor pane so we can store it's state as a property

	mEditorPane.addChangeListener(this) ;
	mSelectorPane.addChangeListener(this) ;
}


/**
 * Set the current edit mode.
 */
public void  setEditMode( int EditMode )
{
	
//  Select editor panes accordingly

	switch ( EditMode ) {
	
	case  TEXT_EDIT_MODE :
		mEditorPane.setSelectedComponent(mEditorPaneText) ;
		break ;
		
	case  MUSIC_EDIT_MODE :
		mEditorPane.setSelectedComponent(mEditorPaneMusic) ;
		break ;
	}
}


/**
 * Select a specified tune.
 */
public void  selectTune( Tune Tune )
{
	switch ( ABCJProperties.getEditMode() ) {
	
	case  TEXT_EDIT_MODE :	
		mTextEditTextPane.setInitialText(
							( Tune != null ) ? Tune.getABCText() : "" ) ;
		// ?????????????
		break ;
	
	case  MUSIC_EDIT_MODE :
		// ?????????????
		break ;	
	}
	
//  Clear any music panes if no tune selected

	if ( Tune == null )   clearMusic() ;
}

/**
 * Listens to changes on the tabbed editor pane.
 *
 * <p>This is so we can remember the state of the GUI when next loaded.
 */
public void  stateChanged( ChangeEvent Evt )
{
	
//  Remember text or music edit mode

	if ( Evt.getSource() == mEditorPane ) {
		if ( mEditorPane.getSelectedComponent() == mEditorPaneText )
			ABCJProperties.setEditMode(TEXT_EDIT_MODE) ;
		
		if ( mEditorPane.getSelectedComponent() == mEditorPaneMusic )
			ABCJProperties.setEditMode(MUSIC_EDIT_MODE) ;
	}
 
//  Update the menu bar accordingly

	mABCJ.refreshMenu() ;
}


/**
 * A new tune book has been added to the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  addTuneBook( TuneBook Book )
{	mIndexPane.addTuneBook(Book) ;   }


/**
 * A tune book has been deleted from the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  removeTuneBook( TuneBook Book )
{	
	mIndexPane.removeTuneBook(Book) ;
	mSearchPane.removeTuneBook(Book) ;
	mListEditListPane.removeTuneBook(Book) ;
}


/**
 * A book title has been changed.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  changeTuneBookTitle( TuneBook Book )
{	mIndexPane.changeTuneBookTitle(Book) ;   }


/**
 * A tune has been added to the current tunebook.
 */
public void  addNewTune( Tune Tune )
{	mIndexPane.addNewTune(Tune) ;   }


/**
 * A tune has been removed from the current tunebook.
 */
public void  removeTune( Tune Tune )
{
	mIndexPane.removeTune(Tune) ;
	mSearchPane.removeTune(Tune) ;
	mListEditListPane.removeTune(Tune) ;
	setEditorInfo( new String[] { "INothing to parse" }, ABCParser.NO_RESULTS ) ; 
}


/**
 * Set the initial GUI state.
 */
public void  setInitialGUIState()
{
	
//  Get and set the divider location for this pane

	int  DivLoc = ABCJProperties.getPropertyInt("ui.MainDividerLocation") ;
	if ( DivLoc > 0 )   setDividerLocation(DivLoc) ;
	else				setDividerLocation(200) ;
	
//  Set the divider location for the text editor pane

	DivLoc = ABCJProperties.getPropertyInt("ui.TextEditDividerLocation") ;
	if ( DivLoc > 0 )
	    mEditorPaneText.setDividerLocation(DivLoc) ;
	else {
		mEditorPaneText.setDividerLocation(0.5) ;
		mEditorPaneText.setDividerLocation( mEditorPaneText.getDividerLocation() ) ;
	}
	
//	Set the divider location for the music editor pane

	DivLoc = ABCJProperties.getPropertyInt("ui.MusicEditDividerLocation") ;
	if ( DivLoc > 0 )
		mEditorPaneMusic.setDividerLocation(DivLoc) ;
	else {
		mEditorPaneMusic.setDividerLocation(0.5) ;
		mEditorPaneMusic.setDividerLocation( mEditorPaneMusic.getDividerLocation() ) ;
	}
	
//	Set the divider location for the list editor pane

	DivLoc = ABCJProperties.getPropertyInt("ui.ListEditDividerLocation") ;
	if ( DivLoc > 0 )
		mEditorPaneLists.setDividerLocation(DivLoc) ;
	else {
		mEditorPaneLists.setDividerLocation(0.5) ;
		mEditorPaneLists.setDividerLocation( mEditorPaneMusic.getDividerLocation() ) ;
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

	ABCJProperties.setProp( "ui.MainDividerLocation",
							getDividerLocation() ) ;
	ABCJProperties.setProp( "ui.TextEditDividerLocation",
							mEditorPaneText.getDividerLocation() ) ;
	ABCJProperties.setProp( "ui.MusicEditDividerLocation",
							mEditorPaneMusic.getDividerLocation() ) ;
	ABCJProperties.setProp( "ui.ListEditDividerLocation",
							mEditorPaneLists.getDividerLocation() ) ;
	ABCJProperties.setProp( "ui.ZoomFactor", mZoomFactor ) ;
}


/**
 * Reset any defaults in case the Look and Feel has changed.
 */

public void  resetDefaults()
{
	mIndexPane.resetDefaults() ;
	mSearchPane.resetDefaults() ;
	mListEditListPane.resetDefaults() ;
}


/**
 * Tune ABC Text has been editted.
 */
public void  tuneEditted( Tune Tune )
{
	mIndexPane.tuneEditted(Tune) ;
	mSearchPane.tuneEditted(Tune) ;
	mListEditListPane.tuneEditted(Tune) ;
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
	mListEditMusicPane.showMusic( Glyphs, d ) ;
}


/**
 * Indicate that the music notation panes should be cleared.
 */
public void  clearMusic()
{
	mTextEditMusicPane.showMusic( null, new Dimension( 20, 20 ) ) ;
	mListEditMusicPane.showMusic( null, new Dimension( 20, 20 ) ) ;
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
	mListEditMusicPane.setZoomFactor(ZoomFactor) ;
}


/**
 * A new tune list has been added to the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  addTuneList( TuneList List )
{	mListEditListPane.addTuneList(List) ;   }


/**
 * A tune list has been deleted from the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  removeTuneList( TuneList List )
{	mListEditListPane.removeTuneList(List) ;   }


/**
 * A list title has been changed.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  changeTuneListTitle( TuneList List )
{	mListEditListPane.changeTuneListTitle(List) ;   }


/**
 * Refresh a tune list after tunes have been added.
 */
public void  refreshTuneList( TuneList List )
{	mListEditListPane.refreshTuneList(List) ;   }

}
