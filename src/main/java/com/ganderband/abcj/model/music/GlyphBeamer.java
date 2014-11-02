/**
 * The glyph class for a beamer.
 *
 * <p>This class manages all the lines used to beam notes together. 
 */
package  abcj.model.music ;

import  java.awt.* ;
import  java.util.* ;

public class GlyphBeamer extends GlyphArray
{
/**
 * The separation of the beams.
 */
	private static final float  TAIL_SEP = 3.0F ;
/**
 * The width of a single tail (if not beamed).
 */
	private static final float  SINGLE_TAIL_WIDTH = 4.0F ;
/**
 * The minimum value of a slope before it goes to zero.
 */
	protected static final float  MIN_SLOPE = 0.05F ;
/**
 * The maximum value of a slope (45 degrees).
 */
	protected static final float  MAX_SLOPE = 1.0F ;
/**
 * The minimum stem length.
 */
	protected static final float  MIN_STEM_LENGTH = 5.0F * SL2 ;
/**
 * The average stem length.
 */
	protected static final float  AVERAGE_STEM_LENGTH = SL2 * 6 ;
/**
 * The array of glyphs to beam.
 */
	protected ArrayList  mBeamGlyphs = new ArrayList() ;
/**
 * The size of the array.
 * 
 * <p>This is stored to ease calculation.
 */
	protected int  mCount ;
/**
 * The forced stem direction (or 0).
 */
	private int  mForceDirection = 0 ;
/**
 * The direction for drawing the stems.
 */
	protected int  mDirection = 0 ;
/**
 * The slope to draw the beam at.
 */	
	protected float  mSlope ;
/**
 * An array to make calculation a lot easier.
 * 
 * <p>This array holds 1 for a note and 2 for a multi-note.
 */	
	protected int[]  mGlyphType ;
/**
 * An array to make calculation a lot easier.
 * 
 * <p>This array holds the note index of the note (first for multi).
 */	
	protected int[]  mNoteIndex ;
/**
 * An array to make calculation a lot easier.
 * 
 * <p>This array holds the tail count for each note (first for multi).
 */	
	protected int[]  mTailCount ;
/**
 * An array to make calculation a lot easier.
 * 
 * <p>This array holds the x co-ordinate of each note head.
 */
	protected float[]  mHeadX ;	
/**
 * An array to make calculation a lot easier.
 * 
 * <p>This array holds the x co-ordinate of each note head.
 */
	protected float[]  mHeadWidth ;	
/**
 * An array to make calculation a lot easier.
 * 
 * <p>This array holds the lowest y co-ordinate of each note head.
 */	
	protected float[]  mLowestHeadY ;	
/**
 * An array to make calculation a lot easier.
 * 
 * <p>This array holds the highest y co-ordinate of each note head.
 */	
	protected float[]  mHighestHeadY ;
/**
 * The initial Y coordinate of the main beam.
 */
	protected float  mBeamY ;	
/**
 * The maximum number of tails in this beam
 */
	protected int  mMaxTails ;

	
/**
 * The default constructor.
 */
public  GlyphBeamer( int ForceDirection )
{
	super( 0, 0 ) ;
	mForceDirection = ForceDirection ;
}	   


/**
 * Add a glyph to this beamer.
 */
public void  addBeamGlyph( Glyph g )
{	mBeamGlyphs.add(g) ;   }


/**
 * Calculate beams.
 */
public  void  calculate()
{
	mGlyphs.clear() ;

	prepareArrays() ;	
	calculateStemDirection() ;
	calculateSlope() ;
	calculateStemLengths() ;
	adjustMultiNoteHeads() ;
	drawStems() ;
	drawBeams() ;
}


/**
 * Prepare some working arrays to ease calculation.
 *
 */
public void  prepareArrays()
{
	mCount        = mBeamGlyphs.size() ;
	mGlyphType    = new int[mCount] ;
	mNoteIndex    = new int[mCount] ;
	mTailCount    = new int[mCount] ;
	mHeadX        = new float[mCount] ;
	mHeadWidth    = new float[mCount] ;
	mLowestHeadY  = new float[mCount] ;
	mHighestHeadY = new float[mCount] ;
	mMaxTails     = 0 ;
	
	for ( int i = 0 ; i < mCount ; i++ ) {
		Object  g = mBeamGlyphs.get(i) ;

	//  Store single note values
	
		if ( g instanceof GlyphSingleNote ) {
			GlyphSingleNote  s = (GlyphSingleNote) g ;
			
			mGlyphType[i]    = 1 ;
			mNoteIndex[i]    = s.mNoteIndex ;
			mTailCount[i]	 = s.mTailCount ;
			mHeadX[i]        = s.mX + s.mLeadingSpace + s.mHeadX ;
			mHeadWidth[i]    = s.mHeadWidth ;
			mLowestHeadY[i]  = s.mY + SL2 ;
			mHighestHeadY[i] = s.mY + SL2 ;
		}
		
	//  Store multi note values
	
		else {
			GlyphMultiNote  m = (GlyphMultiNote) g ;
			
			mGlyphType[i]    = 2 ;
			mNoteIndex[i]    = m.mFirstNote.getPitch().getNoteIndex() ;
			mHeadX[i]        = m.mX + m.mLeadingSpace + m.mHeadX ;
			mHeadWidth[i]    = m.mHeadWidth ;
			mTailCount[i]	 = m.mTailCount ;
			mLowestHeadY[i]  = m.mY + m.mLowestHeadY + SL2 ;
			mHighestHeadY[i] = m.mY + m.mHighestHeadY + SL2 ;
		}
		
	//  Determine maximum number of tails
	
		if ( mTailCount[i] > mMaxTails )   mMaxTails = mTailCount[i] ;
	}
}


/**
 * Calculate the stem direction for this beam.
 */
public  void  calculateStemDirection()
{
	if ( mForceDirection != 0 ) {
	   mDirection = mForceDirection ;
	   return ;
	}
	
//  Add up all the indices of the note heads and count them
//  Note that multinotes have more than one head (use the first).

	float  Average = 0 ;
	for ( int i = 0 ; i < mCount ; i++ )
		Average += mNoteIndex[i] ;

//  Calculate the average and determine stem direction

	mDirection = ( Average / mCount <= -3.0F ) ? +1 : -1 ;
	
//  Now we know the stem direction we can adjust the current x co-ordinates
//	of the note heads (temporarily of course !) to reflect the x co-ordinate
//  of the stems instead.   This will make drawing somewhat easier later on.

	for ( int i = 0 ; i < mCount ; i++ ) {
		if ( mDirection > 0 )
			mHeadX[i] += mHeadWidth[i] - 0.5F ;
		else
			mHeadX[i] += 0.6F ;
	}		
}


/**
 * Calculate the slope for this beam.
 * 
 * <p>This is determined by using the line of best fit algorithm.
 */
public  void  calculateSlope()
{
	
//  Pass through all points and detemine the relevant values for the 
//  least squares slope calculation

	float  SumX, SumY, SumX2, SumXY ;
	SumX = SumY = SumX2 = SumXY = 0 ;
	
	for ( int i = 0 ; i < mCount ; i++ ) {

		float  x = mHeadX[i] ;
		float  y = ( mDirection < 0 ) ? mLowestHeadY[i] : mHighestHeadY[i] ;
		
		SumX  += x ;
		SumY  += y ;
		SumX2 += x * x ;
		SumXY += x * y ;
	}
	
//  Now do the calculation to get the slope
	
	mSlope = ( mCount * SumXY - SumX * SumY ) / ( mCount * SumX2 - SumX * SumX ) ;

//  Check for shallow slope becoming zero.

	if ( mSlope > 0  &&  mSlope <  MIN_SLOPE	
	 ||  mSlope < 0  &&  mSlope > -MIN_SLOPE )
		mSlope = 0 ;

//	Check for maximum slope.

	if ( mSlope > 0  &&  mSlope >  MAX_SLOPE )
		mSlope = MAX_SLOPE ;	
	if ( mSlope < 0  &&  mSlope < -MAX_SLOPE )
		mSlope = -MAX_SLOPE ;
}


/**
 * Calculate the stem lengths for each note.
 * 
 * <p>This method will determine the initial y co-ordinate of the beam.
 * It tries to keep the average stem length at the value provided with this class.
 * However, there is a minimum stem length required (particularly depending in tails)
 * and the y-coordinate will take this into account.
 * 
 * <p>Note also that stems must ALL go in the same direction. 
 */
public  void  calculateStemLengths()
{	calculateStemLengths(1.0F) ;   }
	
public  void  calculateStemLengths( float Scale )
{

//  Assume the initial y co-ordinate of the main beam is zero and
//  work out the total stem length
//  Note that we can also determine the minimum/maximum offset to ensure that
//  all stems go the same way and are not too short.

	float TotalLength = 0 ;
	float  MinBeamY = -99999 ;
	float  MaxBeamY = +99999 ;
	
	for ( int i = 0 ; i < mCount ; i++ ) {
		
		float  y1 = mSlope * ( mHeadX[i] - mHeadX[0] ) ;
		float  y2, LimitY2 ;
		
		if ( mDirection < 0 ) {
			y2      = mLowestHeadY[i] ;
			LimitY2 = y2 + Scale * MIN_STEM_LENGTH - y1 ;
			if( MinBeamY < LimitY2 )   MinBeamY = LimitY2 ;
		}
		else {
			y2      = mHighestHeadY[i] ;
			LimitY2 = y2 - Scale * MIN_STEM_LENGTH - y1 ;
			if( MaxBeamY > LimitY2 )   MaxBeamY = LimitY2 ;
			
		}
		
		TotalLength += y1 - y2 ; 
	}

//  Now determine the required total length and determine the necessary
//  initial y co-ordinate for the main beam to generate this.

	mBeamY = - Scale * AVERAGE_STEM_LENGTH * mDirection - TotalLength / mCount ;
	
//  Ensure it stays within range

	if ( mBeamY < MinBeamY )   mBeamY = MinBeamY ; 
	if ( mBeamY > MaxBeamY )   mBeamY = MaxBeamY ; 
}


/**
 * Draw stems.
 * 
 * <p>Draw the required stems for each note head.
 */
public void  drawStems()
{	drawStems(1.0F) ;   }

public void  drawStems( float Scale )
{
	
	Stroke  Stroke = new BasicStroke( Scale * GlyphNoteBase.NOTE_STEM_STROKE_WIDTH ) ;

	for ( int i = 0 ; i < mCount ; i++ ) {
		
		float  x  = mHeadX[i] ;
		float  y1 = mBeamY + mSlope * ( mHeadX[i] - mHeadX[0] ) ;
		float  y2 = ( mDirection < 0 ) ? mHighestHeadY[i] : mLowestHeadY[i] ;
									    
		mGlyphs.add( new GlyphLine( x, y1, x, y2, Stroke ) ) ;
		
	//  Also update the stem end co-ordinate in case we need it later
	//  and the direction

		if ( mGlyphType[i] < 3 ) {
			GlyphNoteBase  n = (GlyphNoteBase) mBeamGlyphs.get(i) ;
			n.mStemEndY = y1 - n.mY ;
			n.mDirection = mDirection ;
		}
	}
}


/**
 * Draw beams.
 * 
 * <p>Draw the required beams to indicate note length.
 */
public void  drawBeams()
{	drawBeams(1.0F) ;   }

public void  drawBeams( float Scale )
{
	
//  Draw the single main beam.   Note that this will automatically generate
//  the other beams by using recursion.

	drawSingleBeam( 0, 0, mCount - 1, false, Scale ) ;
}


/**
 * Draw a single beam.
 * 
 * <p>This method will draw a single beam and check the level below it for
 * further beaming (using recursion).
 */
public void  drawSingleBeam( int TailLevel, int StartIndex, int EndIndex,
							 boolean LeftTail, float Scale )
{
	
//  Calculate the start and end points of the beam

	float  BaseY = mBeamY + mDirection * TailLevel * TAIL_SEP * Scale ;
	
	if ( Scale < .7F )   BaseY += TailLevel * mDirection ;	// A fudge for readability

	float  x1 = mHeadX[StartIndex] ;
	float  y1 = BaseY + mSlope * ( mHeadX[StartIndex] - mHeadX[0] ) ;
	
	float  x2 = mHeadX[EndIndex] ;
	float  y2 = BaseY + mSlope * ( mHeadX[EndIndex] - mHeadX[0] ) ;
	
//  If a single note rather than a beam then we should apply the appropriate
//  short tail to the left or right as determined by the LeftTail flag
	
	if ( StartIndex == EndIndex ) {
		if ( LeftTail )   x2 = x1 - SINGLE_TAIL_WIDTH ;
		else			  x2 = x1 + SINGLE_TAIL_WIDTH ;

		y2 = BaseY + mSlope * ( x2 - mHeadX[0] ) ;
	}
	
//  Now add the required glyph
	
	mGlyphs.add( new GlyphBeam( x1, y1, x2, y2, mDirection, Scale ) ) ;
	
//  Bump the tail level to the next level

	TailLevel++ ; 
	
//  Return if now at the beaming limit

	if ( TailLevel == mMaxTails )   return ;

//  Do a special check first to determine which horizontal direction short beams
//  should be drawn (i.e. those on a single note such as in hornpipe, strathspey or mazurka).
//  Short beams can be identified when the tail count on both sides of the note is less
//  than the tail count on the note itself.   To determine the overall direction we need only
//  check for a short beam at the start or end.  If both or neither then do nothing special.
	
	boolean  Hornpipe = false ;
	if ( EndIndex > StartIndex
	 &&  mTailCount[EndIndex] > mTailCount[EndIndex - 1] )
			Hornpipe = true ;
	
	boolean  Strathspey = false ;
	if ( EndIndex > StartIndex
	 &&  mTailCount[StartIndex] > mTailCount[StartIndex + 1] )
			Strathspey = true ;

	int  ShortBeamHorzDir = 0 ;	// do nothing
	if ( Hornpipe &&  ! Strathspey )   ShortBeamHorzDir = -1 ;	// Left
	if ( Strathspey &&  ! Hornpipe )   ShortBeamHorzDir = +1 ;
	
//  We should now pass through all notes contained in this beam and check for
//  further beaming.
	
	int  Start = -1 ;
	int  End = -1 ;

	for ( int i = 0 ; i < mCount ; i++ ) {
			
	//  If beaming in progress then check for continue current beam
		
		if ( Start >= 0 ) {
				
			if ( mTailCount[i] > TailLevel )
				End = i ;
				
		//  If not continued then terminate the current beam
			
			else {
				boolean  Flag = LeftTail ;
				if ( StartIndex != EndIndex )   Flag = ( End == EndIndex ) ;
				
				if ( Start == End ) {
					if ( ShortBeamHorzDir == -1 ) Flag = true ;		// Hornpipe
					if ( ShortBeamHorzDir == +1 ) Flag = false ;	// Strathspey
				}
				
				drawSingleBeam( TailLevel, Start, End, Flag, Scale ) ;
				Start = End = -1 ;
			}
		}
			
	//  No beam in progress - check for start new one
		
		else if ( mTailCount[i] > TailLevel )
			Start = End = i ;
	}
		
//  Draw any outstanding beam

	if ( Start >= 0 ) {
		boolean  Flag = LeftTail ;
		if ( StartIndex != EndIndex )   Flag = ( End == EndIndex ) ;
		
		if ( Start == End ) {
			if ( ShortBeamHorzDir == -1 ) Flag = true ;		// Hornpipe
			if ( ShortBeamHorzDir == +1 ) Flag = false ;	// Strathspey
		}

		drawSingleBeam( TailLevel, Start, End, Flag, Scale ) ;
	}
}


/**
 * Adjust the head coordinates to reflect the correct stem direction.
 * 
 * <p>This is only necessary for multi-notes which have note heads in the alternate
 * position on the other side of the stave to prevent conflict.   It is also
 * only necessary if the beam direction is different to the originally calculated
 * stem direction for the multi-note.
 */
public void  adjustMultiNoteHeads()
{

//  Process all multi-note glyphs
	
	for ( int i = 0 ; i < mCount ; i++ ) {
		Object  g = mBeamGlyphs.get(i) ;
	
		if ( g instanceof GlyphMultiNote ) {
			GlyphMultiNote  m = (GlyphMultiNote) g ;
			
		//  Only process if stem direction is different
		
			if ( mDirection != m.mDirection ) {
				
			//  Adjust all note heads in the alternate position correctly
			
				for ( int j = 0 ; j < m.mAlternatePosHead.size() ; j++ ) {
					Glyph  n = (Glyph) m.mAlternatePosHead.get(j) ;
					n.mX += 2 * mDirection * ( mHeadWidth[i] - 1 ) ; 
				}
			}

		}
	}	
}

}