/**
 * The glyph class for a multi-note.
 *
 * <p>This is by no means 100 percent perfect as the musical notation in
 * this area is also imperfect !
 * 
 * <p>I have taken the following decisions re. note lengths :-
 * 
 * <ol>
 * <li>Each note head will be drawn correctly (this may cause some problems
 * 		if we use different note lengths (with and without stems).
 * <li>Tails will only be drawn for the FIRST note in the group.  This means
 * 		that the result may be ambiguous.
 * <li>The first note in the group will determine if a stem is required.
 * <li>Dots will be drawn for each note.
 * <li>The note furthest away from the centre will be used to determine the 
 * 		stem direction.
 * <li>Chords are not allowed in tuplets.
 * </ol> 
 */
package  abcj.model.music ;

import  java.util.* ;
import  abcj.model.abc.* ;
import  abcj.util.* ;

public class GlyphMultiNote extends GlyphNoteBase
{
/**
 * The array of notes contained in the multi-note element.
 */	
	protected ArrayList  mNotes ;
/**
 * The alternate position of the note head (normal or opposite).
 */
	protected boolean[]  mAlternatePos ;
/**
 * An array list of the note head glyphs with an alternate head position.
 * 
 * <p>This is kept to enable the beamer to adjust the position as required.
 */
	protected ArrayList  mAlternatePosHead ;
/**
 * The first note in the list.
 */
	protected Note  mFirstNote ;
/**
 * The highest note in the list.
 */
	protected Note  mHighestNote ;
/**
 * The lowest note in the list.
 */
	protected Note  mLowestNote ;
/**
 * A flag to indicate that the note stem direction is forced to a certain value.
 * 
 * p>The bagpipe keys will set this. If 0 then nothing happens.
 */
	private int  mForceDirection ;
/**
 * The width required for accidentals.
 * 
 * <p>This is necessary for correct positioning when there are multiple
 * and different accidentals.
 */
	private float  mAccidentalWidth ;
/**
 * The y-coordinate the the highest note.
 */
	protected float  mHighestHeadY ;
/**
 * The y-coordinate the the lowest note.
 */
	protected float  mLowestHeadY ;
/**
 * Store the width of a sharp as static.
 */
	private static final float  sWidthSharp = ( new GlyphSharp() ).getWidth() ;
/**
 * Store the width of a flat as static.
 */
	private static final float  sWidthFlat = ( new GlyphFlat() ).getWidth() ;
/**
 * Store the width of a natural as static.
 */
	private static final float  sWidthNatural = ( new GlyphNatural() ).getWidth() ;
			
/**
 * The default constructors.
 */
public  GlyphMultiNote( MultiNoteElement Element, int ForceDirection,
						Fraction SpecMult, GlyphBeamer Beamer )
{	this( 0, 0, Element, ForceDirection, SpecMult, Beamer ) ;   }

public  GlyphMultiNote( MultiNoteElement Element, int ForceDirection,
						GlyphBeamer Beamer )
{	this( 0, 0, Element, ForceDirection, null, Beamer ) ;   }


/**
 * The standard constructor.
 */
public  GlyphMultiNote( float x, float y, MultiNoteElement Element,
						int  ForceDirection, Fraction SpecMult, GlyphBeamer Beamer )
{
	super( x, y, SpecMult,Beamer ) ;
	mBaseNoteElement = Element ;
	mNotes           = Element.getNotes() ;

//  Generate the relevant glyphs for each note

	generateGlyphs() ;
}


/**
 * Generate the glyphs required to draw this note.
 */
public void  generateGlyphs()
{
	if ( mNotes.size() == 0 )   return ;
	
	mAlternatePosHead = new ArrayList() ;
	 
	locateExtremities() ;
	determineDirection() ;
	determineHeadPositions() ;
	
//  First of all, check for any grace notes

	generateGraceNotes( mBaseNoteElement.getGraceNotes(), mForceDirection ) ;
	
	float  mBasePos = mWidth ;

//  Generate note specific items

	float  FinalWidth = 0 ;
	mY = 0 ;

	for ( int i = 0 ; i < mNotes.size() ; i++ ) {
		Note  n = (Note) mNotes.get(i) ;

	//  First of all, determine the length range of the note
	
		Fraction  NoteLength  = n.getAbsoluteLength() ;
		
		if ( mSpecialMultiplier != null )
			NoteLength = NoteLength.times(mSpecialMultiplier) ;

		int  LengthRange = getLengthRange(NoteLength) ;
		
	//  Now determine  the y co-ordinate of the note
	
		int   NoteIndex  = n.getPitch().getNoteIndex() ;  // e -> 0
		mY = - NoteIndex * SL2 ;
		
//		Store next few values for beaming
	
		if ( n == mHighestNote )   mHighestHeadY = mY ;
		if ( n == mLowestNote  )   mLowestHeadY  = mY ;
		mHeadX = mAccidentalWidth + mBasePos ;

	//  Generate the required glyphs
	
		generateAccidentals( n.getPitch().getAccidental() ) ;
		generateLedgerLines(NoteIndex) ;
		generateNoteHead( LengthRange, mAlternatePos[i] ) ;
		generateDots( n, LengthRange ) ;
		
		if ( mWidth > FinalWidth )   FinalWidth = mWidth ;
		
		mWidth = mBasePos ;		//  Force back to start after each note
	 	mY = 0 ;
	}
	
//  Finally complete the drawing by adding gracings, guitar chord, stem and tails

	mWidth = mAccidentalWidth ;
	
	float  WidthWithTails = 0 ;

	Fraction  NoteLength = mFirstNote.getAbsoluteLength() ;
	if ( mSpecialMultiplier != null )
		NoteLength = NoteLength.times(mSpecialMultiplier) ;
	mLengthRange = getLengthRange(NoteLength) ;

	mTailCount   = getTailCount(mLengthRange) ;

	generateGracings() ;
	generateGuitarChord() ;
	if ( mBeamer == null ) {
		generateNoteStem() ;
		WidthWithTails = generateTails() ;
	}
	else
		mWidth += mHeadWidth ;

//  Allow for any extra length

	mWidth = ( WidthWithTails > FinalWidth ) ? WidthWithTails : FinalWidth ;
	
//  Finally setup the trailing space

	initPaddingSpace(mLengthRange) ;
	
//  Handle any words associated with this chord
	
	handleWords( mBaseNoteElement, this ) ;
}


/**
 * Generate the required accidental glyphs.
 */
public void  generateAccidentals( int Accidental )
{

//  Move to the right a bit before drawing so the notes line up

	mWidth += mAccidentalWidth - getAccidentalWidth(Accidental) ; 		
	
	generateAccidentals( Accidental, mY ) ;
}


/**
 * Generate the ledger lines.
 */
public void  generateLedgerLines( int NoteIndex )
{	generateLedgerLines( NoteIndex, mY ) ;   }


/**
 * Generate any gracings.
 */
public void  generateGracings()
{
	String  Gracings = mBaseNoteElement.getGracings() ;
	
	if ( Gracings == null )   return ;
	
	float  y = - mHighestNote.getPitch().getNoteIndex() * SL2 ;

	if ( Gracings.indexOf('.') >= 0  &&  mDirection >= 0 )
		y  = - mLowestNote.getPitch().getNoteIndex()  * SL2 ;
		
	generateGracings( Gracings, y ) ;
}


/**
 * Generate any guitar chord.
 */
public void  generateGuitarChord()
{
	
//	Determine y position of the chord

	float  y = - mHighestNote.getPitch().getNoteIndex() * SL2 - 7 ;
	if ( y > - 4 * SL2 ) y = - 4 * SL2 ;	// Min Y pos
	
//	Generate the necessary glyph

	generateGuitarChord( mBaseNoteElement.getGuitarChord(), y ) ;
}


/**
 * Generate the required note head glyph.
 */
public void  generateNoteHead( int LengthRange, boolean AlternatePos )
{
	Glyph  g = getNoteHead(LengthRange) ;
	addGlyph( g, mY ) ;
	
	if ( mHeadWidth == 0 )   mHeadWidth = g.getWidth() ;
	
//  If the note should be on the other side of the stem in its alternate position
//  to prevent conflict with another note within a semi-tone then move it accordingly.
//  N.B. This is by no means complete as accidentals, dots and the overall
//  starting position should be handled as well.   I suspect this is unlikely to
//  become necessary !
//  Note also that that the beamer may require these to be drawn the other way !!

	if ( AlternatePos ) {
		mAlternatePosHead.add(g) ;
		g.mX += mDirection * ( mHeadWidth - 1 ) ; 
	}
}


/**
 * Generate the required dots.
 */
public void  generateDots( Note Note, int LengthRange )
{	generateDots( Note, LengthRange, mY ) ;   }


/**
 * Generate the required note stem.
 * 
 * <p>This is only called for the very first note.
 */
public void  generateNoteStem()
{
	if ( mLengthRange < 3 )   return ;

	float  HighY = - mHighestNote.getPitch().getNoteIndex() * SL2 ;
	float  LowY  = - mLowestNote.getPitch().getNoteIndex()  * SL2 ;

//	Generate upwards stem

	Glyph  g ;

	if ( mDirection > 0 ) {
		mStemEndY = HighY - 5 * SL2 ;
		g = new GlyphLine( mHeadWidth - 0.5F, LowY + SL2 - 0.4F,
						   mHeadWidth - 0.5F, mStemEndY,
						   NOTE_STEM_STROKE ) ;			
	}
	
//	Generate downwards stem

	else {
		mStemEndY = LowY + 7 * SL2 ;
		g = new GlyphLine( 0.6F, HighY + SL2 + 0.8F, 0.6F, mStemEndY,
						   NOTE_STEM_STROKE ) ;
	}
	
	addGlyphNoAdvance(g) ;
}


/**
 * Locate the extremities (highest and lowest notes).
 */
public void  locateExtremities()
{
	mFirstNote = (Note) mNotes.get(0) ;

	int   LowestNoteIndex = 999 ;
	int   HighestNoteIndex = -999 ;
	
	mAccidentalWidth = 0 ;
	
	for ( int i = 0 ; i < mNotes.size() ; i++ ) {
		Note  n = (Note) mNotes.get(i) ;
	
	//	Locate the highest and lowest notes
		
		int  NoteIndex = n.getPitch().getNoteIndex() ;
		
		if ( NoteIndex < LowestNoteIndex ) {
			LowestNoteIndex = NoteIndex ;
			mLowestNote = n ;
		}
		if ( NoteIndex > HighestNoteIndex ) {
			HighestNoteIndex = NoteIndex ;
			mHighestNote = n ;
		}
	
	//  Determine the maximum accidentals width for later positioning
	
		float  Width = getAccidentalWidth( n.getPitch().getAccidental() ) ;
		
		if ( Width > mAccidentalWidth )   mAccidentalWidth = Width ;
	}
}


/**
 * Determine the note stem direction.
 */
public void  determineDirection()
{
	if ( mForceDirection != 0 ) {
		mDirection = mForceDirection ;
		return ;
	}
	
//  Determine the note furthest away from the centre and set the direction
//  according to that

	int  HighOffset = mHighestNote.getPitch().getNoteIndex() + 3 ;
	int  LowOffset  = mLowestNote.getPitch().getNoteIndex() + 3 ;
	
	mDirection = ( Math.abs(HighOffset) > Math.abs(LowOffset) ) ? -1 : +1 ;
}


/**
 * A helper method to get the required width for an accidental.
 * 
 * <p>This is done in advance of drawing so we can line up note heads which
 * have different accidentals correctly.
 */	
public static float  getAccidentalWidth( int Accidental )
{	
	switch ( Accidental ) {

	case  +2 :   return  sWidthSharp * 2 ;
	case  +1 :	 return  sWidthSharp ;
	case  -2 :	 return  sWidthFlat * 2 - 1 ;
	case  -1 :	 return  sWidthFlat - 1 ;
	case   9 :	 return  sWidthNatural - 0.5F ;
	default  :   return  0 ;
	}
}


/**
 * Determine whether the note head is in normal or alternate position.
 * 
 * <p>For a down stem a normal head is on the left and the alternate on the right.
 * <p>For an up stem a normal head is on the right and the alternate on the left.
 * 
 * <p>Most note should be in the normal position.
 */
public void  determineHeadPositions()
{
	
//  First of all, collect all of the note indices into an array.

	int[]  NoteIndex = new int[ mNotes.size() ] ;
	int[]  Pos       = new int[ mNotes.size() ] ;
	
	for ( int i = 0 ; i < mNotes.size() ; i++ ) {
		Note  n = (Note) mNotes.get(i) ;
		NoteIndex[i] = n.getPitch().getNoteIndex() ;
		Pos[i] = i ;
	}
	
//  Now sort the array into ascending order

	for ( int i = 0 ; i < NoteIndex.length - 1 ; i++ ) {
		int  MinIndex = i ;
		int  MinVal   = NoteIndex[i] ;
		
		for ( int j = i + 1 ; j < NoteIndex.length ; j++ ) { 
			if ( NoteIndex[j] < MinVal ) {
				MinIndex = j ;
				MinVal = NoteIndex[j] ;
			}
		}
				
		if ( MinIndex != i ) {
			int  t = NoteIndex[i] ;
			NoteIndex[i] = NoteIndex[MinIndex] ;
			NoteIndex[MinIndex] = t ;
			t = Pos[i] ;
			Pos[i] = Pos[MinIndex] ;
			Pos[MinIndex] = t ;
		}
	}

//  Now try and determine the appropriate alignment
//  We start at the lowest note in normal position.   If the next note is
//  within a semi-tone then it goes on the other side.
	
	boolean[]  AlternatePos = new boolean[NoteIndex.length] ;
	
	AlternatePos[0] = false ;
	for ( int i = 1 ; i < NoteIndex.length ; i++ ) {
		if ( Math.abs( NoteIndex[i] - NoteIndex[i - 1] ) <= 1.1
		 && ! AlternatePos[i - 1] )
			AlternatePos[i] = true ;
		else
			AlternatePos[i] = false ;
	}
	
//  Now store the results in the correct order

	mAlternatePos = new boolean[AlternatePos.length] ;
	for ( int i = 0 ; i < AlternatePos.length ; i++ )
		mAlternatePos[ Pos[i] ]	 = AlternatePos[i] ;
}

}
