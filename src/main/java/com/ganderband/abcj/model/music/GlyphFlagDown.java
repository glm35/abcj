/**
 * The glyph class for a Flag Down (for quaver downwards).
 */
package  com.ganderband.abcj.model.music;

public class GlyphFlagDown extends Glyph
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
		sPath.moveTo(  0.0F, 12.4F ) ;
		sPath.curveTo( 0.9F, -3.7F,  9.1F, -6.4F,  6.0F, -12.4F ) ;
		sPath.curveTo( 1.0F,  5.4F, -4.2F,  8.4F, -6.0F,   8.4F ) ;
	}

	
/**
 * The default constructor.
 */
public  GlyphFlagDown()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphFlagDown( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 3.5F ;		//  Correct for outlying space
}

}