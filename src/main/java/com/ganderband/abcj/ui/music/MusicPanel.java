/**
 * A class to manage the music display area when text editting.
 */

package  com.ganderband.abcj.ui.music ;

import  java.awt.* ;
import  javax.swing.* ;
import  java.util.* ;

import com.ganderband.abcj.model.music.*;

public class MusicPanel extends JPanel
{

/** 
 * The spacing to allow round the edges of the panel.
 *
 * This is used to increase the dimensions of the the parent window to there
 * is some clear space round the edge of the music.
 */
	private static final float  BORDER_SPACE = 10 ;
/**
 * The glyphs array to be shown in this panel.
 */
	private ArrayList  mGlyphs = null ;
/**
 * The current music zoom factor when displaying.
 */
	private float  mZoomFactor = 1.0F ;


/**
 * The default constructor.
 */
public  MusicPanel()
{
//	setBackground( new Color( 255, 255, 230 ) ) ;	// Light yellow
	setBackground( new Color( 255, 255, 255 ) ) ;	// White for color print ink saving !
}


/**
 * Set the preferred size.
 * 
 * <p>This is overridden so we can add the border requirements.
 */
public void  setPreferredSize( Dimension d )
{
	Dimension  d2 = new Dimension() ;
	d2.setSize( d.width + 2 * BORDER_SPACE, d.height + 2 * BORDER_SPACE ) ;
	super.setPreferredSize(d2) ;
}


/**
 * Set the zoom factor.
 * 
 * <p>This method zooms the lot for viewing purposes.
 */
public void  setZoomFactor( float Zoom )
{
	mZoomFactor = Zoom ;
	repaint() ;		// As it all needs redrawing now.
}

/**
 * Set the glyphs array to be displayed.
 */
public void  setGlyphs( ArrayList Glyphs )
{
	mGlyphs = Glyphs ;
	repaint() ;			// Force a repaint as it's changed !	
}


/**
 * The main paint method for this panel.
 * 
 * <p>Using paintComponent rather than paint gives access to a better set
 * of drawing methods as the graphics context is actually a Graphics2D
 */
public void  paintComponent( Graphics g )
{
	
//  Paint the super class

	super.paintComponent(g) ;
	
//  Get a 2D graphics context then position and scale
	
	Graphics2D  g2d = (Graphics2D) g ;
	
	g2d.translate( BORDER_SPACE, BORDER_SPACE ) ;
	g2d.scale( mZoomFactor, mZoomFactor ) ;
	
//	Force anti-aliasing and high quality

	 RenderingHints  Hints = new RenderingHints( RenderingHints.KEY_ANTIALIASING,
												  RenderingHints.VALUE_ANTIALIAS_ON ) ;
	 Hints.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY ) ;

	 g2d.setRenderingHints(Hints) ;
	
// Loop through all glyphs and display

// N.B. This should be optimised to ignore glyphs which are outside
//      of the drawing area

	 if ( mGlyphs != null )
		for ( int i = 0 ; i < mGlyphs.size() ; i++ )
			( (Glyph) mGlyphs.get(i) ).paint(g2d) ;
}

}
