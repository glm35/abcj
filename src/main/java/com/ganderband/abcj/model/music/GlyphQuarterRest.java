/**
 * The glyph class for a quarter rest.
 */
package  com.ganderband.abcj.model.music;

public class GlyphQuarterRest extends Glyph
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
		sPath.moveTo(   3.4F,  3.0F ) ;
		sPath.lineTo(   1.3F,  3.4F ) ;
		sPath.lineTo(  -2.0F,  4.5F ) ;
		sPath.lineTo(   3.1F,  4.8F ) ;
		sPath.curveTo( -3.2F, -3.5F, -5.8F,  1.4F, -1.4F,  3.8F ) ;
		sPath.curveTo( -1.9F, -2.0F, -0.8F, -5.0F,  2.4F, -2.6F ) ;
		sPath.lineTo(  -2.2F, -4.2F ) ;
		sPath.curveTo(  0.0F,  0.0F,  2.0F, -4.7F,  2.1F, -4.7F ) ;
		sPath.lineTo(  -3.3F, -5.0F ) ;
	}
	
/**
 * The default constructor.
 */
public  GlyphQuarterRest()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphQuarterRest( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}
