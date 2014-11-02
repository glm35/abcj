/**
 * The glyph class for a sharp.
 */
package  abcj.model.music;

import  java.awt.* ;
import  java.awt.geom.* ;

public class GlyphSharp extends Glyph
{
/**
 * The stroke used to draw the vertical lines.
 */
	private static Stroke  VERTICAL_LINE_STROKE = new BasicStroke(0.7F) ;
/**
 * The stroke used to draw the horizontal lines.
 */
	private static Stroke  HORIZONTAL_LINE_STROKE = new BasicStroke(1.5F) ;

	
/**
 * The default constructor.
 */
public  GlyphSharp()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphSharp( float x, float y )
{
	super( x, y ) ;
	
	mWidth = STAVE_LINE_SPACING + 2.3F ;	// Allow for line width as well !
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

	final float SL4 = STAVE_LINE_SPACING / 4 ;
	
	g.setStroke(VERTICAL_LINE_STROKE) ;
	g.draw( new Line2D.Float(
			SL4, - STAVE_LINE_SPACING + 1, SL4, 2 * STAVE_LINE_SPACING  ) ) ;
	g.draw(	new Line2D.Float(
			3 * SL4, - STAVE_LINE_SPACING, 3 * SL4, 2 * STAVE_LINE_SPACING - 1 ) ) ;
	
	g.setStroke(HORIZONTAL_LINE_STROKE) ;
	g.draw( new Line2D.Float(
			0, SL4, STAVE_LINE_SPACING + .3F, SL4 - 2 ) ) ;
	g.draw( new Line2D.Float( 
			0, 3 * SL4 + 2, STAVE_LINE_SPACING + .3F, 3 * SL4 ) ) ;
	
//  Restore and return

	g.setStroke(SavedStroke) ;
	g.setTransform(SavedTransform) ;
}

}
