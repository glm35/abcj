/**
 * The glyph class for a line item.
 */
package  com.ganderband.abcj.model.music ;

import  java.awt.* ;
import  java.awt.geom.* ;

public class GlyphLine extends Glyph
{
/**
 * The start x co-ordinate.
 */
	private float  mX1 ;
/**
 * The start y co-ordinate.
 */
	private float  mY1 ;
/**
 * The end x co-ordinate.
 */
	private float  mX2 ;
/**
 * The end y co-ordinate.
 */
	private float  mY2 ;
/**
 * The stroke used to draw the line (null implies use default).
 */
	private Stroke  mStroke ;
	
	
/**
 * The default constructor.
 */
public  GlyphLine( float x1, float y1, float x2, float y2, Stroke Stroke )
{	this( 0, 0, x1, y1, x2, y2, Stroke ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphLine( float x, float y, float x1, float y1, float x2, float y2,
				   Stroke Stroke )
{
	super( x, y ) ;
	mX1 = x1 ;
	mY1 = y1 ;
	mX2 = x2 ;
	mY2 = y2 ;
	mStroke = Stroke ;
}


/**
 * Paint this text to the 2D graphics context supplied.
 */
public void  paint( Graphics2D g )
{
	
//  Save current stroke if necessary

	Stroke  SavedStroke = null ;
	if ( mStroke != null ) {
		SavedStroke = g.getStroke() ;
		g.setStroke(mStroke) ;
	}
	
//  Construct and draw the required line

	g.draw( new Line2D.Float( mX1, mY1, mX2, mY2 ) ) ;
	
//  Restore the stroke if required

	if ( SavedStroke != null )   g.setStroke(SavedStroke) ; 
}


/**
 * Set the current co-ordinates.
 * 
 * <p>Note that for a line we must also adjust the end position of the line.
 */
public void  setCoords( float x, float y )
{
	mX1 = mX1 - mX + x ;
	mY1 = mY1 - mY + y ;
	mX2 = mX2 - mX + x ;
	mY2 = mY2 - mY + y ;
	mX = x ;   mY = y ;
}

}
