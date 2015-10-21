/**
 * The glyph class for a slide.
 */
package  com.ganderband.abcj.model.music;

public class GlyphSlide extends Glyph
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
		sPath.moveTo(   0.4F, 2.8F ) ;
		sPath.curveTo(  1.8F, 0.7F,  4.5F, -0.2F,  7.2F, -4.8F ) ;
		sPath.curveTo( -2.1F, 5.0F, -5.4F,  6.8F, -7.6F, 6.0F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphSlide()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphSlide( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}
