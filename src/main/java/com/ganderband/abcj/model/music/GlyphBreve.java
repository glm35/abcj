/**
 * The glyph class for a breve (double note).
 */
package  com.ganderband.abcj.model.music;

public class GlyphBreve extends Glyph
{
/**
 * Create a path for drawing statically.
 */
/**
 * The static relative path used to draw this glyph. 
 * 
 * <p>As this item is always drawn the same we should share a single instance
 * of the path used to draw it.
 */
	protected static RelativePath  sPath = new RelativePath() ;	
	static {
		sPath.moveTo(   4.8F,  0.6F ) ;
		sPath.curveTo(  2.8F, -1.6F,  6.0F,  3.2F,  3.2F,  4.8F ) ;
		sPath.curveTo( -2.8F,  1.6F, -6.0F, -3.2F, -3.2F, -4.8F ) ;
		sPath.moveTo(   7.2F,  2.4F ) ;
		sPath.curveTo(  0.0F, -1.8F, -2.2F, -3.2F, -5.6F, -3.2F ) ;
		sPath.curveTo( -3.4F,  0.0F, -5.6F,  1.4F, -5.6F,  3.2F ) ;
		sPath.curveTo(  0.0F,  1.8F,  2.2F,  3.2F,  5.6F,  3.2F ) ;
		sPath.curveTo(  3.4F,  0.0F,  5.6F, -1.4F,  5.6F, -3.2F ) ;
		sPath.moveTo( -11.0F,  4.0F ) ;
		sPath.lineTo(   0.0F, -9.0F ) ;
		sPath.lineTo(  -1.0F,  1.0F ) ;
		sPath.lineTo(   0.0F,  9.0F ) ;
		sPath.lineTo(   1.0F, -1.0F ) ;
		sPath.moveTo(  12.0F,  0.0F ) ;
		sPath.lineTo(   0.0F, -9.0F ) ;
		sPath.lineTo(  -1.0F,  1.0F ) ;
		sPath.lineTo(   0.0F,  9.0F ) ;
		sPath.lineTo(   1.0F, -1.0F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphBreve()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphBreve( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 1.1F ;	// Adjust for trailing space
}

}
