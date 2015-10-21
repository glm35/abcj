/**
 * The glyph class for a single beam.
 */
package  com.ganderband.abcj.model.music ;

import  java.awt.geom.* ;


public class GlyphBeam extends Glyph
{
/**
 * The thickness of beam to use.
 */
	private static final float  BEAM_THICKNESS = 2.0F ;


/**
 * The standard constructor.
 */
public  GlyphBeam( float x1, float y1, float x2, float y2, int Direction,
					float Scale )
{
	
//  The y coordinates need adjusting for neatness

	y1 -= Direction * 0.5F ;
	y2 -= Direction * 0.5F ;
	
//  Construct the necessary path to draw this beam

	mPath = new GeneralPath() ;
	mPath.moveTo( x1, y1 ) ;
	mPath.lineTo( x2, y2 ) ;
	mPath.lineTo( x2, y2 + Direction * BEAM_THICKNESS * Scale ) ;
	mPath.lineTo( x1, y1 + Direction * BEAM_THICKNESS * Scale ) ;
	mPath.lineTo( x1, y1 ) ;
}

}
