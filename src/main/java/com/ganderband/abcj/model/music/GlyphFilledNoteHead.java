/**
 * The glyph class for a filled note head (crotchet downwards).
 */
package  abcj.model.music;

public class GlyphFilledNoteHead extends Glyph
{
/**
 * The static relative path used to draw this glyph. 
 * 
 * <p>As this item is always drawn the same we should share a single instance
 * of the path used to draw it.
 */
	protected static RelativePath  sPath = new RelativePath() ;	
/**
 * Create a path for drawing statically.
 */
	static {
		sPath.moveTo(   7.5F,  1.1F ) ;
		sPath.curveTo( -2.0F, -3.5F, -9.0F,  0.5F, -7.0F,  4.0F ) ;
		sPath.curveTo(  2.0F,  3.5F,  9.0F, -0.5F,  7.0F, -4.0F ) ;
	}

	
/**
 * The default constructor.
 */
public  GlyphFilledNoteHead()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphFilledNoteHead( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 4.1F ;		//  Correct for outlying space
}

}
