/**
 * The glyph class for a sixteenth rest.
 */
package  com.ganderband.abcj.model.music;

public class GlyphSixteenthRest extends GlyphEighthRest
{
/**
 * The static relative path used to draw this glyph. 
 * 
 * <p>As this item is always drawn the same we should share a single instance
 * of the path used to draw it.
 */
	protected static RelativePath  sPath16 = new RelativePath() ;	
/**
 * Create a path for drawing statically .
 */
	static {
		sPath16.moveTo(  5.05F, 11.5F ) ;
		sPath16.moveTo(  3.3F,  -4.0F ) ;
		sPath16.lineTo( -4.0F,  15.6F ) ;
		sPath16.lineTo(  0.5F,  -0.5F ) ;
		sPath16.lineTo(  4.0F, -15.6F ) ;
		sPath16.lineTo( -0.5F,   0.5F ) ;
		addRestCurve(sPath16) ;
		sPath16.moveTo( -1.45F,  6.0F ) ;
		addRestCurve(sPath16) ;
	}

	
/**
 * The default constructor.
 */
public  GlyphSixteenthRest()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphSixteenthRest( float x, float y )
{	super( x, y ) ;   setPath(sPath16.mPath) ;   }

}
