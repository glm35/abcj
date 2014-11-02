/**
 * A beamer for grace notes.
 * 
 * <p>This is much more restrictive than a general beamer and must allow
 * for scaling.
 * 
 * <p>It can also be calculated immediately as the notes will not get spread out.
 * 
 * <p>It is provided as a sub-class of GlyphBeamer so we can use the relevant
 * calculation routines more easily.
 */
package  abcj.model.music ;

public class GlyphGraceBeamer extends GlyphBeamer
{
/**
 * The vertical scaling factor to apply.
 * 
 * <p>The notes being drawn are scaled but the beam isn't.   It will need
 * adjusting accordingly (e.g. stroke thicknesses and stem lengths.
 */
	private float  mScaleY ;

/**
 * The default constructor.
 */
public  GlyphGraceBeamer()
{
	super(+1)	;
	mDirection = + 1 ;		// Always up
}	   



/**
 * Calculate beams.
 */
public  void  calculate()
{
	mGlyphs.clear() ;

	prepareArrays() ;
	
	if ( mCount < 1 )   return ;
		
	calculateSlope() ;
	calculateStemLengths(mScaleY) ;
	drawStems(mScaleY) ;
	drawBeams(mScaleY) ;
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
	mHeadX        = new float[mCount] ;
	mHeadWidth    = new float[mCount] ;
	mLowestHeadY  = new float[mCount] ;
	mHighestHeadY = new float[mCount] ;
	mTailCount    = new int[mCount] ;
	
	for ( int i = 0 ; i < mCount ; i++ ) {
		GlyphGraceNote  g = (GlyphGraceNote) mBeamGlyphs.get(i) ;
		
		mScaleY          = g.mScaleFactor ;
	
		mGlyphType[i]    = 3 ;
		mNoteIndex[i]    = g.mNoteIndex ;
		mHeadWidth[i]    = mScaleY * g.mHeadWidth - .25F ;
		mHeadX[i]        = g.mX + g.mLeadingSpace 
						+ ( g.mHeadX + g.mHeadWidth ) * mScaleY	;
		mLowestHeadY[i]  = g.mY + SL2 - 2 ;
		mHighestHeadY[i] = g.mY + SL2 - 2 ;
		mTailCount[i]    = 2 ;
	}
}


}
