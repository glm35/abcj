/**
 * The glyph class for an upper mordent.
 */
package  abcj.model.music;

public class GlyphUpperMordent extends Glyph
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
		sPath.moveTo(  5.0F, -17.8F ) ;
		sPath.lineTo(  2.2F,  -2.2F ) ;
		sPath.lineTo(  2.1F,   2.9F ) ;
		sPath.lineTo(  0.7F,  -0.7F ) ;
		sPath.lineTo( -2.2F,   2.2F ) ;
		sPath.lineTo( -2.1F,  -2.9F ) ;
		sPath.lineTo( -0.7F,   0.7F ) ;
		sPath.lineTo( -2.2F,   2.2F ) ;
		sPath.lineTo( -2.1F,  -2.9F ) ;
		sPath.lineTo( -0.7F,   0.7F ) ;
		sPath.lineTo(  2.2F,  -2.2F ) ;
		sPath.lineTo(  2.1F,   2.9F ) ;
		sPath.lineTo(  0.7F,  -0.7F ) ;
	}

	
/**
 * The default constructor.
 */
public  GlyphUpperMordent()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphUpperMordent( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}