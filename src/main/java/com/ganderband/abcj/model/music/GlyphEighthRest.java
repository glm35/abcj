/**
 * The glyph class for an eighth rest.
 */
package  com.ganderband.abcj.model.music;

public class GlyphEighthRest extends Glyph
{
/**
 * The static relative path used to draw this glyph. 
 * 
 * <p>As this item is always drawn the same we should share a single instance
 * of the path used to draw it.
 */
	protected static RelativePath  sPath = new RelativePath() ;	
/**
 * Create a path for drawing statically .
 */
	static {
		sPath.moveTo(  3.1F,  11.5F ) ;
		sPath.moveTo(  3.3F,  -4.0F ) ;
		sPath.lineTo( -3.4F,   9.6F ) ;
		sPath.lineTo(  0.5F,  -0.5F ) ;
		sPath.lineTo(  3.4F,  -9.6F ) ;
		sPath.lineTo( -0.5F,   0.5F ) ;
		addRestCurve(sPath) ;
	}
 

/**
 * The default constructor.
 */
public  GlyphEighthRest()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphEighthRest( float x, float y )
{	super( x, y ) ;   setPath(sPath.mPath) ;   }


/**
 * A helper method used by sub-classes to ease drawing.
 */	
protected static void  addRestCurve( RelativePath p )
{
	p.curveTo( -1.5F,  1.5F, -2.4F,  2.0F, -3.6F,  2.0F ) ;
	p.curveTo(  2.4F, -2.8F, -2.8F, -4.0F, -2.8F, -1.2F ) ;
	p.curveTo(  0.0F,  2.7F,  4.3F,  2.4F,  5.9F,  0.6F ) ;
}

}
