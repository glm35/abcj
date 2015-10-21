/**
 * The glyph class for a dot.
 */
package  com.ganderband.abcj.model.music;

public class GlyphDot extends Glyph
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
		sPath.moveTo(  0.0F,  3.0F ) ;
		sPath.curveTo( 0.0F, -1.5F,  2.0F, -1.5F,  2.0F, 0.0F ) ;
		sPath.curveTo( 0.0F,  1.5F, -2.0F,  1.5F, -2.0F, 0.0F ) ;
	}
 
	
/**
 * The default constructor.
 */
public  GlyphDot()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphDot( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 1.0F ;	// Adjust for trailing space
}

}
