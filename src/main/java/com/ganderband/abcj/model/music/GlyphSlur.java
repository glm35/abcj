/**
 * The glyph class for a slur.
 * 
 * <p>This is also used for handling ties and triplets as well as slurs.
 * It essentially just draws the relevant symbol.   Like the beamers, it
 * is calculated when all else has been completed and positioned.
 */
package  abcj.model.music ;

import  java.util.* ;
import  java.awt.geom.* ;


public class GlyphSlur extends GlyphArray
{
/**
 * A private constant for adjusting tightly angled beams.
 */
	private static final float  TIGHT_ADJUST = 2.0F ;
/**
 * A private constant for defining the top bezier curve.
 */
	private static final float  BEZIER1 = 5.0F ;
/**
 * A private constant for defining the bottom bezier curve.
 */
	private static final float  BEZIER2 = 7.0F ;
/**
 * The array of glyphs in the slur.
 */
	private ArrayList  mSlurGlyphs = new ArrayList() ;
/**
 * The direction of the slur.
 * 
 * <p>-1 means below, +1 means above.
 */
	private int  mDirection ;
/**
 * The size of the array.
 * 
 * <p>This is stored to ease calculation.
 */
	private int  mCount ;
/**
 * The music builder producing this glyph.
 * 
 * <p>We need to know this for glyphs split across staves.
 */
	private MusicBuilder  mBuilder ;


/**
 * The default constructor.
 */
public  GlyphSlur( MusicBuilder Builder )
{	super( 0, 0 ) ;   mBuilder = Builder ;   }


/**
 * Add a glyph to this slur.
 */
public void  addSlurGlyph( Glyph g )
{	mSlurGlyphs.add(g) ;   }


/**
 * Calculate the slur based on its contents.
 */
public  void  calculate()
{
	mCount = mSlurGlyphs.size() ;

	if ( mCount < 1 )   return ;
	
	calculateDirection() ;
	drawSlur() ;
}


/**
 * Determine the direction of the slur.
 *
 * <p>This is done by looking range of the note indexes (allowing for stems) 
 */
public void  calculateDirection()
{
	float  AverageTop    = 0 ;
	float  AverageBottom = 0 ;
	
//  Process all items in the slur

	for ( int i = 0 ; i < mCount ; i++ ) {
		GlyphNoteBase  g = (GlyphNoteBase) mSlurGlyphs.get(i) ;
		
	//  Determine y range of a note and total up the top and bottom
	
		int[]  Range = determineNoteIndexRange(g) ;
		
	//  Determine the extremities
		
		AverageTop += Range[0] ;
		AverageBottom += Range[1] ;
	}
	
	AverageTop    /= mCount ;
	AverageBottom /= mCount ;
	
//  Now choose the direction based on which protrudes most

	mDirection = ( ( AverageTop + 3 ) > ( -AverageBottom - 3 ) ) ? -1 : +1 ;
}


/**
 * Determine the note index range of a glyph.
 */
public int[]  determineNoteIndexRange( GlyphNoteBase g )
{
	int  Top    = -3 ;
	int  Bottom = -3 ;
	
//	Handle a rest

	if ( g instanceof GlyphRest ) {
		Top    = -3 ;
		Bottom = -3 ;
	}
	
//	Handle a single note

	else if ( g instanceof GlyphSingleNote ) {
		GlyphSingleNote  n = (GlyphSingleNote) g ;
		if ( g.mDirection < 0 ) {
			Top    = n.mNoteIndex ;
			Bottom = Top - 6 ;
		}
		else {
			Bottom = n.mNoteIndex ;
			Top    = Bottom + 6 ;
		}
	}
	
//	Handle a multi note

	else if ( g instanceof GlyphMultiNote ) {
		GlyphMultiNote  m = (GlyphMultiNote) g ;
		if ( m.mDirection < 0 ) {
			Top    = m.mHighestNote.getPitch().getNoteIndex() ;
			Bottom = m.mLowestNote.getPitch().getNoteIndex() - 6 ;
		}
		else {
			Top    = m.mHighestNote.getPitch().getNoteIndex() + 6 ;
			Bottom = m.mLowestNote.getPitch().getNoteIndex() ;
		}
	}

	return  new int[] { Top, Bottom } ;   
}


/**
 * Draw the slur.
 */
public void  drawSlur()
{
	mPath = new GeneralPath() ;
	
//	Locate the first and last items of the glyph

	GlyphNoteBase  First = (GlyphNoteBase) mSlurGlyphs.get(0) ;
	GlyphNoteBase  Last  = (GlyphNoteBase) mSlurGlyphs.get(mCount - 1 ) ;

//	Determine the left x co-ordinate
//	This is comparatively easy as the glyph type does not matter.

	float  x1 = First.mX + First.mLeadingSpace +First.mHeadX ;
	
	if ( mDirection > 0  &&  First.mDirection > 0 )
		x1 += First.mHeadWidth - 1.5F ; 
	else if ( mDirection <= 0  &&  First.mDirection <= 0 )
		x1 -= 0.5F ;

//	Determine the right x co-ordinate
//	This is comparatively easy as the glyph type does not matter.

	float  x2 = Last.mX + Last.mLeadingSpace +Last.mHeadX + Last.mHeadWidth ;
	
	if ( mDirection > 0  &&  Last.mDirection > 0 )
		x2 += 0.5F ;
	else if ( mDirection <= 0  &&   Last.mDirection <= 0 )
		x2 -= Last.mHeadWidth - 1.5F ;

//	Determine the left and right y-coordinates

	float  y1 = adjustY(First) ;
	float  y2 = adjustY(Last) ;

//  If start and end are on the same stave then it's easy.
//  For a tuplet, the builder will be null so we can't do it anyway

	if ( mBuilder == null
	 ||  First.getStaveNumber() == Last.getStaveNumber() ) {
		drawSlur( x1, y1, x2, y2, First.mTuplet ) ;
		return ;	
	}
	
//  Slur split across staves.   Draw two slurs, one for each stave

//  Determine the difference in y-coordinates between the two staves

	float  DiffY = ( Last.getStaveNumber() - First.getStaveNumber() )
				 * MusicBuilder.getStaveHeight() ; 

//  Draw slur for top stave

	drawSlur( x1, y1, x2 + MusicBuilder.getStaveWidth(), y2 - DiffY, First.mTuplet ) ;

//	Draw slur for bottom stave

	drawSlur( x1 - MusicBuilder.getStaveWidth(), y1 + DiffY, x2, y2, First.mTuplet ) ;
}
	

/**
 * Draw a a slur from (x1,y1) to (x2,y2).
 */
public void  drawSlur( float x1, float y1, float x2, float y2, GlyphTuplet Tuplet )
{
	
//	If this is a steep curve then add some y to the tight end so it looks better

	float  Slope = ( y2 - y1 ) / ( x2 - x1 ) ;  // +ve down, -ve up
	float  Adjustment = Slope * TIGHT_ADJUST ;

	if ( mDirection < 0 ) {	
		if ( Slope > 0 )   y2 += Adjustment ;
		if ( Slope < 0 )   y1 -= Adjustment ;
	}
	else {	
		if ( Slope > 0 )   y1 -= Adjustment ;
		if ( Slope < 0 )   y2 += Adjustment ;
	}
	
//  Draw right half of slur

	float LeftOffset  = 0 ;
	float RightOffset = 0 ;

	if ( mBuilder != null  &&  x1 < 0 ) {
		x1 = 0 ;
		y1 = ( y1 + y2 ) * .5F - mDirection * BEZIER1 * 1.5F ;
		LeftOffset = -mDirection * ( BEZIER2 - BEZIER1 ) ; 
	}
	
//	Draw left half of slur

	else if ( mBuilder != null
  		  &&  x2 > MusicBuilder.getStaveWidth() ) {
		x2 = MusicBuilder.getStaveWidth() ;
		y2 = ( y1 + y2 ) * .5F - mDirection * BEZIER1 * 1.5F ;
		RightOffset = -mDirection * ( BEZIER2 - BEZIER1 ) ; 
	}

//	Generate the required bezier curves as a path so it will get painted.
//	The two bezier points are generate at 33% and 66% of the total width

	float  x3 = ( 2 * x1 + x2 ) / 3 ; 
	float  y3 = ( 2 * y1 + y2 ) / 3 ; 
	float  x4 = ( x1 + 2 * x2 ) / 3 ; 
	float  y4 = ( y1 + 2 * y2 ) / 3 ;

//  Draw complete slur as normal

	mPath.moveTo( x1, y1 ) ;
	mPath.curveTo( x3, y3 - mDirection * BEZIER1,
				   x4, y4 - mDirection * BEZIER1,
				   x2, y2 ) ;
	mPath.lineTo( x2, y2 + RightOffset ) ;
	mPath.curveTo( x4, y4 - mDirection * BEZIER2,
				   x3, y3 - mDirection * BEZIER2,
				   x1, y1 + LeftOffset ) ;
	mPath.lineTo( x1, y1 ) ;

//	If this is a tuplet then we should add the tuplet number

	if ( Tuplet == null )   return ;
	
//	Determine the number to draw and add it

	String  Digit = Integer.toString( Tuplet.mTupletElement.getTupletSpec()[0] ) ;
	
//	Determine the mid point of the line

	float  x = ( x1 + x2 ) / 2 ;
	float  y = ( y1 + y2 ) / 2 ;

//	Construct a unit vector at right angles so we can move the right direction

	float  Length = (float) Math.sqrt( 1 + Slope * Slope ) ;
	float  xr = -Slope / Length ;
	float  yr = 1 / Length ;

	if ( mDirection < 0 ) {
		x += xr * 2 * BEZIER2 - 2 ;
		y += yr * 2 * BEZIER2 ;
	}
	else {
		x -= xr * BEZIER2 + 2 ;
		y -= yr * BEZIER2 ;
	}

	addGlyphNoAdvance( new GlyphText( Digit, GlyphTuplet.TUPLET_FONT ), x, y ) ;
}


/**
 * This routine will return the appropriate y co-ordinate for the passed glyph.
 * 
 * <p>This depends on the glyph type, glyph direction and slur direction.
 */
public float  adjustY( GlyphNoteBase g )
{
	float  y = g.mY ;
	
//	Calculate y co-ordinate for a single note

	if ( g instanceof GlyphSingleNote ) {
		
		if ( mDirection > 0 ) {
			if ( g.mDirection > 0 )   y += g.mStemEndY - SL2 - 2 ; 
			else					  y -= SL2 ;
		}
		else {
			if ( g.mDirection > 0 )   y += 4 * SL2 ; 
			else					  y += g.mStemEndY + SL2 + 2 ;
		}
	}
	
//	Calculate y co-ordinate for a multi note

	if ( g instanceof GlyphMultiNote ) {
		GlyphMultiNote  m = (GlyphMultiNote) g ;
		
		if ( mDirection > 0 ) {
			if ( m.mDirection > 0 )   y += g.mStemEndY - SL2 - 2 ; 
			else					  y -= m.mHighestHeadY + SL2 ;
		}
		else {
			if ( m.mDirection > 0 )   y += m.mLowestHeadY + 4 * SL2 ; 
			else					  y += m.mStemEndY + SL2 + 2 ;
		}
	}

//  Calculate y co-ordinate for a rest

	else if ( g instanceof GlyphRest ) {
		if ( mDirection > 0 )   y -= g.getHeight() / 2 - 2 ;		
		else				    y += g.getHeight() / 2 + 2 ;
	}
	
	return  y ;
}

}
