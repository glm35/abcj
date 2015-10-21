/**
 * The glyph class for a minim rest.
 */
package  com.ganderband.abcj.model.music;

public class GlyphMinimRest extends Glyph
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
		sPath.moveTo(   0.0F,   9.0F ) ;
		sPath.lineTo(   7.0F,   0.0F ) ;
		sPath.lineTo(   0.0F,   3.0F ) ;
		sPath.lineTo( - 7.0F,   0.0F ) ;
		sPath.lineTo(   0.0F,  -3.0F ) ;
	}
	
/**
 * The default constructor.
 */
public  GlyphMinimRest()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphMinimRest( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }

}
