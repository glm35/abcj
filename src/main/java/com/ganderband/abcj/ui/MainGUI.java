package  abcj.ui ;

import java.awt.Dimension;
import java.util.ArrayList;

import  abcj.model.* ;

/**
 * This defines the interface to the main GUI.
 *
 * <p>It is provided in this form so both an applet and an application can be written.
 */
public interface MainGUI
{

	
/**
 * Set the current edit mode.
 */
public void  setEditMode( int EditMode ) ;


/**
 * Reset any defaults in case the Look and Feel has changed.
 */
public void  resetDefaults() ;


/**
 * Select a specified tune.
 */
public void  selectTune( Tune Tune ) ;


/**
 * Tune ABC Text has been editted.
 */
public void  tuneEditted( Tune Tune ) ;


/**
 * A new tune book has been added to the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  addTuneBook( TuneBook Book ) ;


/**
 * A tune book has been deleted from the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  removeTuneBook( TuneBook Book ) ;


/**
 * A book title has been changed.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  changeTuneBookTitle( TuneBook Book ) ;


/**
 * A new tune list has been added to the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  addTuneList( TuneList List ) ;


/**
 * A tune list has been deleted from the library.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  removeTuneList( TuneList List ) ;


/**
 * Refresh a tune list after tunes have been added.
 */
public void  refreshTuneList( TuneList List ) ;


/**
 * A list title has been changed.
 * 
 * <p>We should update the GUI accordingly.
 */
public void  changeTuneListTitle( TuneList List ) ;


/**
 * A tune has been added to the current tunebook.
 */
public void  addNewTune( Tune Tune ) ;


/**
 * A tune has been removed from the current tunebook.
 */
public void  removeTune( Tune Tune ) ;


/**
 * Set the information in the editor text info pane.
 */
public void  setEditorInfo( String[] Text, int Status ) ;


/**
 * Indicate that the music notation panes should be cleared.
 */
public void  clearMusic() ;


/**
 * Indicate that the music notation has been changed and should be repainted.
 */
public void  showMusic( ArrayList Glyphs, Dimension d ) ;


/**
 * Get the current zoom factor.
 */
public float  getZoomFactor() ;


/**
 * Set the current zoom factor.
 */
public void  setZoomFactor( float ZoomFactor ) ;

}
