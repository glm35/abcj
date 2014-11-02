/**
 * The glyph class for a Flag Up (for quaver downwards).
 */
package  abcj.model.music;

public class GlyphFlagUp extends Glyph
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
		sPath.moveTo(  0.0F,  0.0F ) ;
		sPath.curveTo( 0.9F,  3.7F,  9.1F,  6.4F,  6.0F, 12.4F ) ;
		sPath.curveTo( 1.0F, -5.4F, -4.2F, -8.4F, -6.0F, -8.4F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphFlagUp()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphFlagUp( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 3.5F ;		//  Correct for outlying space
}

}