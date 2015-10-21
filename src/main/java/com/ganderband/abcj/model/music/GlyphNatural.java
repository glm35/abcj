/**
 * The glyph class for a natural.
 */
package  com.ganderband.abcj.model.music;

import  java.awt.* ;
import  java.awt.geom.* ;

public class GlyphNatural extends Glyph
{
/**
 * The stroke used to draw the vertical lines.
 */
	private static Stroke  VERTICAL_LINE_STROKE = new BasicStroke(0.7F) ;
/**
 * The stroke used to draw the horizontal lines.
 */
	private static Stroke  HORIZONTAL_LINE_STROKE =
			new BasicStroke( 1.5F , BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND ) ;

	
/**
 * The default constructor.
 */
public  GlyphNatural()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphNatural( float x, float y )
{
	super( x, y ) ;
	
	mWidth = SL23 + 2 ;	// Allow for line width
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
					0, - STAVE_LINE_SPACING + 2, 0, STAVE_LINE_SPACING + .5F ) ) ;
	g.draw( new Line2D.Float( SL23, -0.3F, SL23, 2 * STAVE_LINE_SPACING ) ) ;
	
	g.setStroke(HORIZONTAL_LINE_STROKE) ;
	g.draw( new Line2D.Float( 0, 2, SL23, 0 ) ) ;
	g.draw( new Line2D.Float( 
					0, STAVE_LINE_SPACING, SL23, STAVE_LINE_SPACING - 2 ) ) ;
	
//  Restore and return

	g.setStroke(SavedStroke) ;
	g.setTransform(SavedTransform) ;
}

}
