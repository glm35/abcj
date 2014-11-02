/**
 * The glyph class for a coda.
 */
package  abcj.model.music;

public class GlyphCoda extends Glyph
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
		sPath.moveTo(  2.0F, -12.5F ) ;
		sPath.curveTo( 0.0F, -11.5F,  15.0F, -11.5F,  15.0F, 0.0F ) ;
		sPath.lineTo( -2.0F,   0.0F ) ;
		sPath.curveTo( 0.0F,  -9.5F, -11.0F,  -9.5F, -11.0F, 0.0F ) ;
		sPath.lineTo( -2.0F,   0.0F ) ;
		sPath.curveTo( 0.0F,  11.5F,  15.0F,  11.5F,  15.0F, 0.0F ) ;
		sPath.lineTo( -2.0F,   0.0F ) ;
		sPath.curveTo( 0.0F,   9.5F, -11.0F,   9.5F, -11.0F, 0.0F ) ;
		sPath.lineTo( -2.0F,   0.0F ) ;
		sPath.moveTo(  7.0F,   9.0F ) ;
		sPath.lineTo(  0.0F,  -9.0F ) ;
		sPath.lineTo( -9.0F,   0.0F ) ;
		sPath.lineTo(  0.0F,  -1.0F ) ;
		sPath.lineTo(  9.0F,   0.0F ) ;
		sPath.lineTo(  0.0F,  -9.0F ) ;
		sPath.lineTo(  1.0F,   0.0F ) ;
		sPath.lineTo(  0.0F,   9.0F ) ;
		sPath.lineTo(  9.0F,   0.0F ) ;
		sPath.lineTo(  0.0F,   1.0F ) ;
		sPath.lineTo( -9.0F,   0.0F ) ;
		sPath.lineTo(  0.0F,   9.0F ) ;
		sPath.lineTo( -1.0F,   0.0F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphCoda()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphCoda( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}
