/**
 * The glyph class for an empty note head (minim).
 */
package  abcj.model.music;

public class GlyphEmptyNoteHead extends Glyph
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
		sPath.moveTo(   7.0F,  1.5F ) ;
		sPath.curveTo( -1.0F, -1.8F, -7.0F,  1.4F, -6.0F,  3.2F ) ;
		sPath.curveTo(  1.0F,  1.8F,  7.0F, -1.4F,  6.0F, -3.2F ) ;
		sPath.moveTo(   0.5F, -0.3F ) ;
		sPath.curveTo(  2.0F,  3.8F, -5.0F,  7.6F, -7.0F,  3.8F ) ;
		sPath.curveTo( -2.0F, -3.8F,  5.0F, -7.6F,  7.0F, -3.8F ) ;
	}


/**
 * The default constructor.
 */
public  GlyphEmptyNoteHead()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphEmptyNoteHead( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 4.0F ;		//  Correct for outlying space
}

}
