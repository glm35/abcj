/**
 * The glyph class for a flat.
 */
package  com.ganderband.abcj.model.music;

import  java.awt.* ;
import  java.awt.geom.* ;

public class GlyphFlat extends Glyph
{
/**
 * The stroke used to draw the vertical lines.
 */
	private static Stroke  VERTICAL_LINE_STROKE = new BasicStroke(0.7F) ;
/**
 * The stroke used to draw the horizontal lines.
 */
	private static Stroke  HORIZONTAL_LINE_STROKE = new BasicStroke(0.9F) ;

	
/**
 * The default constructor.
 */
public  GlyphFlat()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphFlat( float x, float y )
{
	super( x, y ) ;
	
	mWidth = 3 * STAVE_LINE_SPACING / 4 + 1 ;
}


/**
 * Paint this stave to the 2D graphics context supplied.
 */
public void  paint( Graphics2D g )
{
	
//  Save relevant info and position

	AffineTransform  SavedTransform = g.getTransform(); 
	Stroke           SavedStroke    = g.getStroke() ;
	
	g.translate( mX + mLeadingSpace, mY ) ;	

	g.setStroke(VERTICAL_LINE_STROKE) ;
	g.draw( new Line2D.Float(
			0, - 3 * STAVE_LINE_SPACING / 2 + 1.7F, 0, STAVE_LINE_SPACING + 0.7F ) ) ;
	
	g.setStroke(HORIZONTAL_LINE_STROKE) ;
	g.draw( new QuadCurve2D.Float(
			0, STAVE_LINE_SPACING + 0.7F, STAVE_LINE_SPACING, -0.3F, 0, - 0.3F ) ) ;
	
//  Restore and return

	g.setStroke(SavedStroke) ;
	g.setTransform(SavedTransform) ;
}

}
