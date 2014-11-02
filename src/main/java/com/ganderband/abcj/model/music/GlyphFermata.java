/**
 * The glyph class for Fermata.
 */
package  abcj.model.music;

public class GlyphFermata extends Glyph
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
		sPath.moveTo(   7.5F,  -13.5F ) ;
		sPath.curveTo(  0.00F,  -1.5F,   2.00F,  -1.5F,   2.0F, 0.0F ) ;
		sPath.curveTo(  0.00F,   1.5F,  -2.00F,   1.5F,  -2.0F, 0.0F ) ;
		sPath.moveTo(  -7.50F,   0.0F ) ;
		sPath.curveTo(  0.00F, -11.5F,  15.00F, -11.5F,  15.0F, 0.0F ) ;
		sPath.lineTo(  -0.25F,   0.0F ) ;
		sPath.curveTo( -1.25F,  -9.0F, -13.25F,  -9.0F, -14.5F, 0.0F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphFermata()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphFermata( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}