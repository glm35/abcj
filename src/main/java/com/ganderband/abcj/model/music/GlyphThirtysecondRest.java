/**
 * The glyph class for a thirtysecond rest.
 */
package  com.ganderband.abcj.model.music;

public class GlyphThirtysecondRest extends GlyphEighthRest
{
/**
 * The static relative path used to draw this glyph. 
 * 
 * <p>As this item is always drawn the same we should share a single instance
 * of the path used to draw it.
 */
	protected static RelativePath  sPath32 = new RelativePath() ;	
/**
 * Create a path for drawing statically.
 */
	static {
		sPath32.moveTo(  5.5F,  12.0F ) ;
		sPath32.moveTo(  4.8F, -10.0F ) ;
		sPath32.lineTo( -5.5F,  21.6F ) ;
		sPath32.lineTo(  0.5F,  -0.5F ) ;
		sPath32.lineTo(  5.5F, -21.6F ) ;
		sPath32.lineTo( -0.5F,   0.5F ) ;
		addRestCurve(sPath32) ;
		sPath32.moveTo( -1.45F,  6.0F ) ;
		addRestCurve(sPath32) ;
		sPath32.moveTo( -1.45F,  6.0F ) ;
		addRestCurve(sPath32) ;
	}

	
/**
 * The default constructor.
 */
public  GlyphThirtysecondRest()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphThirtysecondRest( float x, float y )
{	super( x, y ) ;   setPath(sPath32.mPath) ;   }

}
