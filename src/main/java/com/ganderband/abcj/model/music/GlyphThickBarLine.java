/**
 * The glyph class for a thick bar line.
 */
package  com.ganderband.abcj.model.music ;

import  java.awt.geom.* ;
import  java.awt.* ;


public class GlyphThickBarLine extends Glyph
{
/**
 * The stroke for a thin bar line.
 */
	private static final Stroke  THICK_BARLINE_STROKE =
			new BasicStroke( 3.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND ) ;


/**
 * The default constructor.
 */
public  GlyphThickBarLine()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphThickBarLine( float x, float y )
{
	super( x, y ) ;
	mWidth = 5 ;
}


/**
 * Paint this glyph to the 2D graphics context supplied.
 */
public void  paint( Graphics2D g )
{
	
//	Save transform and position at correct place

	AffineTransform  SavedTransform = g.getTransform() ;
	Stroke 			 SavedStroke    = g.getStroke() ;
	g.translate( mX + mLeadingSpace, mY ) ;
	
//	Standard drawing is just to fill the glyph

	g.setStroke(THICK_BARLINE_STROKE) ;
	g.draw( new Line2D.Float( 1.5F, 0, 1.5F, STAVE_LINE_SPACING * 4 ) ) ;

//	Restore transform and return 

	g.setStroke(SavedStroke) ;
	g.setTransform(SavedTransform) ;
}



}
