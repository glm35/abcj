/**
 * The glyph class for segno.
 */
package  abcj.model.music;

public class GlyphSegno extends Glyph
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
		sPath.moveTo(   7.4F,  -3.5F ) ;
		sPath.moveTo(   0.0F,  -3.0F ) ;
		sPath.curveTo(  1.5F,   1.7F,  6.4F,  -0.3F,  3.0F,  -3.7F ) ;
		sPath.curveTo(-10.4F,  -7.8F, -8.0F, -10.6F, -6.5F, -11.9F ) ;
		sPath.curveTo(  4.0F,  -1.9F,  5.9F,   1.7F,  4.2F,   2.6F ) ;
		sPath.curveTo( -1.3F,   0.7F, -2.9F,  -1.3F, -0.7F,  -2.0F ) ;
		sPath.curveTo( -1.5F,  -1.7F, -6.4F,   0.3F, -3.0F,   3.7F ) ;
		sPath.curveTo( 10.4F,   7.8F,  8.0F,  10.6F,  6.5F,  11.9F ) ;
		sPath.curveTo( -4.0F,   1.9F, -5.9F,  -1.7F, -4.2F,  -2.6F ) ;
		sPath.curveTo(  1.3F,  -0.7F,  2.9F,   1.3F,  0.7F,   2.0F ) ;
		sPath.moveTo(  -6.0F,  -1.6F ) ;
		sPath.lineTo(  12.6F, -12.6F ) ;
		sPath.lineTo(   0.6F,   0.6F ) ;
		sPath.lineTo( -12.6F,  12.6F ) ;
		sPath.lineTo(  -0.6F,   0.6F ) ;
		sPath.moveTo(   0.0F,  -6.5F ) ;
		sPath.curveTo(  0.0F,  -1.5F,  2.0F, -1.5F,   2.0F,   0.0F ) ;
		sPath.curveTo(  0.0F,   1.5F, -2.0F,  1.5F,  -2.0F,   0.0F ) ;
		sPath.moveTo(  12.0F,   0.0F ) ;
		sPath.curveTo(  0.0F,  -1.5F,  2.0F, -1.5F,   2.0F,   0.0F ) ;
		sPath.curveTo(  0.0F,   1.5F, -2.0F,  1.5F,  -2.0F,   0.0F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphSegno()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphSegno( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}