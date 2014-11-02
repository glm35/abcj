/**
 * The glyph class for longa.
 */
package  abcj.model.music;

public class GlyphLonga extends Glyph
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
		sPath.moveTo(   2.5F,   6.5F ) ;
		sPath.lineTo(  10.0F,   0.0F ) ;
		sPath.lineTo(   0.0F,  -1.5F ) ;
		sPath.lineTo( -10.0F,   0.0F ) ;
		sPath.lineTo(   0.0F,   1.5F ) ;
		sPath.moveTo(   0.0F,  -5.4F ) ;
		sPath.lineTo(  10.0F,   0.0F ) ;
		sPath.lineTo(   0.0F,  -1.5F ) ;
		sPath.lineTo( -10.0F,   0.0F ) ;
		sPath.lineTo(   0.0F,   1.5F ) ;
		sPath.moveTo(  -2.5F,   8.0F ) ;
		sPath.lineTo(   0.0F, -10.0F ) ;
		sPath.lineTo(   2.5F,  -1.5F ) ;
		sPath.lineTo(   0.0F,  10.0F ) ;
		sPath.lineTo(  -2.5F,   1.5F ) ;
		sPath.moveTo(  12.5F,   5.0F ) ;
		sPath.lineTo(   0.0F, -15.0F ) ;
		sPath.lineTo(   2.5F,  -1.5F ) ;
		sPath.lineTo(   0.0F,  15.0F ) ;
		sPath.lineTo(  -2.5F,   1.5F ) ;
 	}


/**
 * The default constructor.
 */
public  GlyphLonga()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphLonga( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 1.1F ;	// Adjust for trailing space
}

}
