/**
 * A general pane for holding a music panel.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  javax.swing.* ;
import  java.util.* ;
import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.model.* ;
import  com.ganderband.abcj.ui.music.* ;


public abstract class MusicPane extends JScrollPane
{
/**
 * The owning ABCJ appication instance.
 */
	private ABCJ  mABCJ ;
/**
 * The music panel contained in this pane.
 */
	private MusicPanel  mMusicPanel ;
/**
 * The current dimensions of the music panel.
 */
	private Dimension  mMusicSize ;
/**
 * The current zoom factor.
 */
	private float  mZoomFactor = 2.0F ;
/**
 * The tune currently being shown.
 * 
 * <p>This is used to control whether the display is repositioned on update or not.
 */
	private Tune  mCurrentTune ;
/**
 * The default constructor.
 */
public  MusicPane( ABCJ App )
{
	mABCJ = App ;
	
	mMusicPanel = new MusicPanel() ;
	
	setViewportView(mMusicPanel) ;
//	setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS) ;
//	setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS) ;
}


/**
 * Indicate that the music notation has been changed and should be repainted.
 */
public void  showMusic( ArrayList Glyphs, Dimension d )
{
	mMusicSize = new Dimension(d) ;

//  For scrolling to work correctly we must set preferred size and revalidate 

	if ( d != null ) {
		
		Dimension  d2 = new Dimension() ;	// Clone it
		d2.setSize( mZoomFactor * mMusicSize.getWidth(),
					mZoomFactor * mMusicSize.getHeight() ) ;

		mMusicPanel.setPreferredSize(d2) ;		// Now update
		mMusicPanel.revalidate() ;

	//  If selected tune has changed then reposition
	
		if ( mABCJ.getSelectedTune() != mCurrentTune )
			getViewport().setViewPosition( new Point( 0, 0 ) ) ;
			
		mCurrentTune = mABCJ.getSelectedTune() ;
	}
	
	mMusicPanel.setGlyphs(Glyphs) ;
	mMusicPanel.setZoomFactor(mZoomFactor) ;
}


/**
 * Set the zoom factor.
 * 
 * <p>This method zooms the lot for viewing purposes.
 */
public void  setZoomFactor( float Zoom )
{
	
//  Before zooming determine the new co-ordinate position

	Point  OldPos = getViewport().getViewPosition() ;
							
	Point  NewPos = new Point() ;
	NewPos.setLocation( OldPos.getX() * Zoom / mZoomFactor,
						OldPos.getY() * Zoom / mZoomFactor ) ;	
							   
//  Apply the new zoom factor and force a repaint

	mZoomFactor = Zoom ;

	if ( mMusicSize == null )   return ;
	
	Dimension  d = new Dimension() ;
	d.setSize( mZoomFactor * mMusicSize.getWidth(),
			   mZoomFactor * mMusicSize.getHeight() ) ;
	
	mMusicPanel.setZoomFactor(Zoom) ;
	mMusicPanel.setPreferredSize(d) ;		// Now update
	mMusicPanel.revalidate() ;
	
//  Finally locate the cursor at the equivalent zoomed position

	getViewport().setViewPosition(NewPos) ;
}

}
