/**
 * The glyph class for common time.
 */
package  abcj.model.music;

public class GlyphCommonTime extends Glyph
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
		sPath.moveTo(   6.4F,   6.7F ) ;
		sPath.curveTo(  0.9F,   0.0F,  2.3F,  0.7F,  2.4F,  2.2F ) ;
		sPath.curveTo( -1.2F,  -2.0F, -3.6F,  0.1F, -1.6F,  1.7F ) ;
		sPath.curveTo(  2.0F,   1.0F,  3.8F, -3.5F, -0.8F, -4.7F ) ;
		sPath.curveTo( -2.0F,  -0.4F, -6.4F,  1.3F, -5.8F,  7.0F ) ;
		sPath.curveTo(  0.4F,   6.4F,  7.9F,  6.8F,  9.1F,  0.7F ) ;
		sPath.curveTo( -2.3F,   5.6F, -6.7F,  5.1F, -6.8F, -0.0F ) ;
		sPath.curveTo( -0.5F,  -4.4F,  0.7F, -7.5F,  3.5F, -6.9F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphCommonTime()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphCommonTime( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}
