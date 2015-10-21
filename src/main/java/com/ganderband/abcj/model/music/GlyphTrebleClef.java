/**
 * The glyph class for a treble clef.
 */
package  com.ganderband.abcj.model.music;

public class GlyphTrebleClef extends Glyph
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
		sPath.moveTo(   7.5F,  20.4F ) ;
		sPath.curveTo( -3.3F,  -1.9F,  -3.1F,  -6.8F,   2.4F,  -8.6F ) ;
		sPath.curveTo(  7.0F,  -0.0F,   9.8F,   8.0F,   4.1F,  11.7F ) ;
		sPath.curveTo( -5.2F,   2.4F, -12.5F,   0.0F, -13.3F,  -6.2F ) ;
		sPath.curveTo( -0.7F,  -6.4F,   4.15F,-10.5F,  10.0F, -15.3F ) ;
		sPath.curveTo(  4.0F,  -4.0F,   3.6F,  -6.1F,   2.8F,  -9.6F ) ;
		sPath.curveTo( -2.3F,   1.5F,  -4.7F,   4.8F,  -4.5F,   8.5F ) ;
		sPath.curveTo(  0.8F,  12.2F,   3.4F,  17.3F,   3.5F,  26.3F ) ;
		sPath.curveTo(  0.3F,   4.4F,  -1.2F,   6.2F,  -3.8F,   6.2F ) ;
		sPath.curveTo( -3.7F,   0.1F,  -5.8F,  -4.3F,  -2.8F,  -6.1F ) ;
		sPath.curveTo(  3.9F,  -1.9F,   6.1F,   4.6F,   1.4F,   4.8F ) ;
		sPath.curveTo(  0.7F,   1.2F,   4.6F,   0.8F,   4.2F,  -4.2F ) ;
		sPath.curveTo( -0.2F, -10.3F,  -3.0F, -15.7F,  -3.5F, -28.3F ) ;
		sPath.curveTo(  0.0F,  -4.1F,   0.6F,  -7.4F,   5.0F, -10.6F ) ;
		sPath.curveTo(  2.3F,   3.2F,   2.9F,  10.0F,   1.0F,  12.7F ) ;
		sPath.curveTo( -2.4F,   4.3F, -11.5F,  10.3F, -11.8F,  15.0F ) ;
		sPath.curveTo(  0.4F,   7.0F,   6.9F,   8.5F,  11.7F,   6.1F ) ;
		sPath.curveTo(  3.9F,  -3.0F,   1.3F,  -8.8F,  -3.7F,  -8.1F ) ;
		sPath.curveTo( -4.0F,   0.2F,  -4.8F,   3.1F,  -2.7F,   5.7F ) ;
	} 
	
/**
 * The default constructor.
 */
public  GlyphTrebleClef()
{	this( 0, 0 ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphTrebleClef( float x, float y )
{
	super( x, y ) ;
	setPath(sPath.mPath) ;
	mWidth -= 3 ;
}

}
