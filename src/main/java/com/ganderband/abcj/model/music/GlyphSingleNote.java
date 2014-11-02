/**
 * The glyph class for a note.
 * 
 * <p>This class is also used for rests by subclassing.
 */
package  abcj.model.music ;

import  abcj.model.abc.* ;
import  abcj.util.* ;

public class GlyphSingleNote extends GlyphNoteBase
{
/**
 * The Note instance representing this (from the ABC model)
 */
	private Note  mNote ;
/**
 * A flag indicating that this is a rest.
 */
	private boolean  mIsRest ;
/**
 * The index of the note onto the stave ( e = 0, f = 1, d = -1, ...)
 */
	protected int  mNoteIndex ;
/**
 * A flag to indicate that the note stem direction is forced to a certain value.
 * 
 * p>The bagpipe keys will set this. If 0 then nothing happens.
 */
	private int  mForceDirection ;
/**
 * The drawing length of the note.
 * 
 * <p>Again, this is to allow for tuplet which are drawn with a different
 * length to their absolute played length.
 */	
	private  Fraction  mDrawLength ;

	
/**
 * The default constructors.
 */
public  GlyphSingleNote( NoteElement Element, int ForceDirection,
						 GlyphBeamer Beamer )
{	this( 0, 0, Element, ForceDirection, null, Beamer ) ;   }

public  GlyphSingleNote( NoteElement Element, int ForceDirection, Fraction SpecMult,
						 GlyphBeamer Beamer )
{	this( 0, 0, Element, ForceDirection, SpecMult, Beamer ) ;   }


/**
 * The standard constructor.
 */
public  GlyphSingleNote( float x, float y, NoteElement Element, int  ForceDirection,
				   Fraction SpecMult, GlyphBeamer Beamer )
{
	super( x, y, SpecMult, Beamer ) ;
	mBaseNoteElement   = Element ;
	mNote              = Element.getNote() ; 
	mForceDirection    = ForceDirection ;
	
	mIsRest = ( Element.getType() == REST_ELEMENT ) ;

//  Generate the relevant glyphs for each note

	generateGlyphs() ;
}


/**
 * Generate the glyphs required to draw this note.
 */
public void  generateGlyphs()
{

//  Calculate the basic length range of the note as defined above
//  Allow for tuplets where an adjustment is required before drawing.

	mDrawLength = mNote.getAbsoluteLength() ;
	
	if ( mSpecialMultiplier != null )
		mDrawLength = mDrawLength.times(mSpecialMultiplier) ;

	mLengthRange = getLengthRange(mDrawLength) ;

//  Count number of tails

	mTailCount = getTailCount(mLengthRange) ;

//	Determine the note index
	
	mNoteIndex  = mIsRest ? 0 : mNote.getPitch().getNoteIndex() ;  // e -> 0

//  Determine direction of note stem

	mDirection = ( mNoteIndex <= -3 ) ? +1 : -1 ;
	
	if ( mForceDirection != 0 )   mDirection = mForceDirection ;
	
	if ( mIsRest )   mDirection = 0 ;

//	Now determine which row to show the note on as an index.
	
	if ( mIsRest )		mY = 2 * SL2 ;
	else				mY = - mNoteIndex * SL2 ;
	
//  First of all, check for any grace notes

	generateGraceNotes( mBaseNoteElement.getGraceNotes(), mForceDirection ) ;
	
//  Draw all the bits

	float  WidthWithTails = mWidth ;
	
	if ( mIsRest ) {
		generateGuitarChord() ;
		generateRestHead() ;
		generateDots() ;
	}
	else {
		generateAccidentals() ;
		generateLedgerLines() ;
		generateGracings() ;
		generateGuitarChord() ;
		
		mHeadX = mWidth ;		// Store for beaming

		generateNoteHead() ;
		if ( mBeamer == null ) {
			generateNoteStem() ;
			WidthWithTails = generateTails() ;
		}
		else
			mWidth += mHeadWidth ;
		generateDots() ;
	}
	
//  Add any outstanding space
		
	if ( WidthWithTails > mWidth )   mWidth = WidthWithTails ;
	
//  Finally determine the trailing space

	initPaddingSpace(mLengthRange) ;

//  Connect the glyph to any words against the note so we can position the
//  words correctly when we draw them later
	
	handleWords( mBaseNoteElement, this ) ;
}


/**
 * Generate the required accidental glyphs.
 */
public void  generateAccidentals()
{	generateAccidentals( mNote.getPitch().getAccidental(), 0 ) ;   }


/**
 * Generate the ledger lines.
 */
public void  generateLedgerLines()
{	generateLedgerLines( mNoteIndex, 0 ) ;   }


/**
 * Generate any gracings.
 */
public void  generateGracings()
{	generateGracings( mBaseNoteElement.getGracings(), 0 ) ;   }




/**
 * Generate any guitar chord.
 */
public void  generateGuitarChord()
{
	
//	Determine y position of the chord

	float  y = -7 ;
	if ( y + mY > - 4 * SL2 ) y = - mY - 4 * SL2 ;	// Min Y pos
	
//	Generate the necessary glyph

	generateGuitarChord( mBaseNoteElement.getGuitarChord(), y ) ;
}


/**
 * Generate the required note stem.
 */
public void  generateNoteStem()
{
	if ( mLengthRange < 3 )   return ;

	Glyph  g ;
	
//  Generate upwards stem

	if ( mDirection > 0 ) {
		mStemEndY = -6 * SL2 ;
		g = new GlyphLine( mHeadWidth - 0.5F, SL2 - 0.4F,
						   mHeadWidth - 0.5F, mStemEndY,
						   NOTE_STEM_STROKE ) ;			
	}
		
//  Generate downwards stem

	else {
		mStemEndY = 8 * SL2 ;
		g = new GlyphLine( 0.6F, SL2 + 0.8F, 0.6F, mStemEndY, NOTE_STEM_STROKE ) ;
	}
	
	addGlyphNoAdvance(g) ;
}


/**
 * Generate the required dots.
 */
public void  generateDots()
{	generateDots( mNote, mLengthRange, 0 ) ;   }

}
