/**
 * The glyph class for a bass clef.
 */
package  abcj.model.music;

public class GlyphBassClef extends Glyph
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
		sPath.moveTo(   0.6F, 20.6F ) ;
		sPath.curveTo(  6.3F, -1.9F, 10.2F, -5.6F,  10.5F, -10.8F ) ;
		sPath.curveTo(  0.3F, -4.9F, -0.5F, -8.1F,  -2.6F,  -8.8F ) ;
		sPath.curveTo( -2.5F, -1.2F, -5.8F,  0.7F,  -5.9F,   4.1F ) ;
		sPath.curveTo(  1.8F, -3.1F,  6.1F,  0.6F,   3.1F,   3.0F ) ;
		sPath.curveTo( -3.0F,  1.4F, -5.7F, -2.3F,  -1.9F,  -7.0F ) ;
		sPath.curveTo(  2.6F, -2.3F, 11.4F, -0.6F,  10.1F,   8.0F ) ;
		sPath.curveTo( -0.1F,  4.6F, -5.0F, 10.2F, -13.3F,  11.5F ) ;
		sPath.moveTo(  15.5F, -17.0F ) ;
		sPath.curveTo(  0.0F, -1.5F,  2.0F, -1.5F,   2.0F,   0.0F ) ;
		sPath.curveTo(  0.0F,  1.5F, -2.0F,  1.5F,  -2.0F,   0.0F ) ;
		sPath.moveTo(   0.0F,  5.5F ) ;
		sPath.curveTo(  0.0F, -1.5F,  2.0F, -1.5F,   2.0F,   0.0F ) ;
		sPath.curveTo(  0.0F,  1.5F, -2.0F,  1.5F,  -2.0F,   0.0F ) ;
	}


/**
 * The default constructor.
 */
public  GlyphBassClef()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphBassClef( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}
