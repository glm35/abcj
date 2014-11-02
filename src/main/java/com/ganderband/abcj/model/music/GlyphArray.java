/**
 * A class representing a glyph comprised of an array of glyphs.
 * 
 * <p>These symbolds a represented as Java2D shapes.
 */
package  abcj.model.music ;

import  java.util.* ;
import  java.awt.* ;
import  java.awt.geom.* ;


public abstract class GlyphArray extends Glyph
{
/**
 * The array list of the relevant glyphs to draw.
 */
	protected ArrayList  mGlyphs = new ArrayList() ;
/**
 * Any scaling necessary before drawing.
 */
	protected float  mScaleFactor = 1.0F ;

/**
 * The default constructor.
 */
public  GlyphArray()
{	super( 0, 0 ) ;   }


/**
 * The standard constructor.
 */
public  GlyphArray( float x, float y )
{	super( x, y ) ;   }


/**
 * Set the Stave containing this glyph.
 */
public void  setStaveNumber( int StaveNumber )
{
	super.setStaveNumber(StaveNumber) ;
	for ( int i = 0 ; i < mGlyphs.size() ; i++ )
		( (Glyph) mGlyphs.get(i) ).setStaveNumber(StaveNumber) ;
}


/**
 * Paint this glyph to the 2D graphics context supplied.
 */
public void  paint( Graphics2D g )
{
	
//	Save relavant and info position at correct place then scale

	AffineTransform  SavedTransform = g.getTransform() ;
	
	g.translate( mX + mLeadingSpace, mY ) ;
	
	g.scale( mScaleFactor, mScaleFactor ) ;
	
//  Draw the contained path if possible

	if ( mPath != null )   g.fill(this) ;

//	Draw all the relevant glyphs 
	
	for ( int i = 0 ; i < mGlyphs.size() ; i++ )
		( (Glyph) mGlyphs.get(i) ).paint(g) ;

//	Restore and return 

	g.setTransform(SavedTransform) ;	
}


/**
 * A private helper method for adding glyphs to the array.
 * 
 * <p>Only the y coordinate is provided as the x coordinate is the current width.
 * If no y is given then the existing y co-ordinate is preserved.
 */
protected void  addGlyph( Glyph g, float x, float y )
{
	mGlyphs.add(g) ;
	g.setCoords( mWidth + x, y ) ;
	mWidth += g.mLeadingSpace + g.mWidth + g.mTrailingSpace ;  // Maybe +x too
															   // not used yet
}

protected void  addGlyph( Glyph g, float y )
{	addGlyph( g, 0, y ) ;   }

protected void  addGlyph( Glyph g )
{	addGlyph( g, 0, g.mY ) ;   }


/**
 * A private helper method for adding glyphs to the array.
 * 
 * <p>Only the y coordinate is provided as the x coordinate is the current width.
 * If no y is given then the existing y co-ordinate is preserved.
 */
protected void  addGlyphNoAdvance( Glyph g, float x, float y )
{
	mGlyphs.add(g) ;
	g.setCoords( mWidth + x, y ) ;
}

protected void  addGlyphNoAdvance( Glyph g, float y )
{	addGlyphNoAdvance( g, 0, y ) ;   }

protected void  addGlyphNoAdvance( Glyph g )
{	addGlyphNoAdvance( g, 0, g.mY ) ;   }

}
