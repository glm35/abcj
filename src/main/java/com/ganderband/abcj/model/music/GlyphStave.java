/**
 * The glyph class for a stave.
 */
package  abcj.model.music;

import  java.awt.* ;

public class GlyphStave extends GlyphArray
{
/**
 * The thickness of the stroke used to draw each stave line.
 */
	protected static float  STAVE_LINE_STROKE_WIDTH = 0.5F ;
/**
 * The stroke used to draw each stave line.
 */
	protected static Stroke  STAVE_LINE_STROKE =
										new BasicStroke(STAVE_LINE_STROKE_WIDTH) ;

	
/**
 * The default constructor.
 */
public  GlyphStave( float Width )
{	this( 0, 0, Width ) ;   }

/**
 * Set the stave width.
 * 
 * <p>This will force a recalculate of the drawing path.
 */
public void  setStaveWidth( float Width )
{
	mWidth = Width ;
	
//	Construct a path representing the stave of the given width

	mGlyphs.clear() ;
	
	for ( int i = 0 ; i < 5 ; i++ ) {
		mGlyphs.add( new GlyphLine(	0,     STAVE_LINE_SPACING * i,
									Width, STAVE_LINE_SPACING * i,
									STAVE_LINE_STROKE ) ) ;
	}
}


/**
 * The standard constructor.
 */
public  GlyphStave( float x, float y, float Width )
{
	super( x, y ) ;
	setStaveWidth(Width) ;
}

}
