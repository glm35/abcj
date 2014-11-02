/**
 * The base glyph class for notes (single and multinote).
 * 
 * <p>This class provides the base info and drawing mothods for drawing
 * single notes, multi-notes and rests.
 */
package  abcj.model.music ;

import  java.awt.* ;
import  java.util.* ;

import  abcj.model.abc.* ;
import  abcj.util.* ;

public abstract class GlyphNoteBase extends GlyphArray
{
/**
 * The vertical separation between tails (non-beamed.
 */
	protected static final float  TAIL_SEP = SL2 + 1.0F ;
/**
 * The width of the stroke used to draw the note stem.
 */
	protected static float  NOTE_STEM_STROKE_WIDTH = 0.7F ;
/**
 * The stroke used to draw the note stem.
 */
	protected static Stroke  NOTE_STEM_STROKE =
											new BasicStroke(NOTE_STEM_STROKE_WIDTH) ;
/**
 * The font for drawing guitar chords.
 */
	protected static Font  GUITAR_CHORD_FONT = new Font( "SansSerif", Font.BOLD, 12 ) ;
/**
 * The base NoteElement instance representing this (from the ABC model).
 * 
 * <p>This may actually be a multinote element or a rest as well as an ordinary note.
 * tuplets are handled specially in the GlyphTuplet class.
 */
	protected NoteElement  mBaseNoteElement ;
/**
 * The direction of the tail.
 * 
 * <p>+1 for up and -1 for down.
 */
	protected int  mDirection ;
/**
 * The width of the note head.
 */	
	protected float  mHeadWidth ;
/**
 * The y-coordinate of the end of the loose end of the stem.
 * 
 * <p>This is used for drawing tails. 
 */
	protected float  mStemEndY ;
/**
 * A value to easily track which length range we are in.
 * 
 * 0 = longa (4)
 * 1 = breve (2)
 * 2 = semi breve (1)
 * 3 = minim (1/2)
 * 4 = crotchet (1/4)
 * 5 = quaver (1/8)
 * 6 = semi quaver (1/16)
 * 7 = demi-semi quaver (1/32)
 */
	protected int  mLengthRange ;
/**
 * If this note is part of a tuplet then remember it here.
 */
	protected GlyphTuplet  mTuplet = null ;
/**
 * Is this note beamed ?
 * 
 * <p>If it is then note stems and tails should not be drawn.   In addition, it
 * will have been added to this beamer object during the build process.   We cannot
 * do it adequately here as the following notes may actually change the status from
 * beamed to unbeamed.   e.g. it's followed by a bar-line or a tuplet.
 */
	protected GlyphBeamer  mBeamer = null ;
/**
 * The x co-ordinate the the note head.
 */
	protected float  mHeadX ;
/**
 * The number of tails for this note.
 */
	protected int  mTailCount = 0 ;	
/**
 * The special multiplier used to adjust the note length.
 * 
 * <p>This will only appear where the note is part of a tuplet and is used to
 * specify what we should draw rather than the actual length of the note.
 */
	protected Fraction  mSpecialMultiplier ;
	

/**
 * The standard constructor.
 */
public  GlyphNoteBase( float x, float y, Fraction SpecMult, GlyphBeamer Beamer )
{
	super( x, y ) ;
	mSpecialMultiplier = SpecMult ;
	mBeamer = Beamer ;
	initPaddingSpace() ;
}

/**
 * Generate any grace notes.
 */
public void  generateGraceNotes( ArrayList GraceNotes, int ForceDirection )
{
	if ( GraceNotes == null  ||  GraceNotes.size() <= 0 )   return ;
	
//  If more than one grace note then construct a beamer

	GlyphGraceBeamer  Beamer = null ;
	if ( GraceNotes.size() > 1 )   Beamer = new GlyphGraceBeamer() ;

//  Process all the grace notes

	for ( int i = 0 ; i < GraceNotes.size() ; i++ ) {
		Pitch  p = (Pitch) GraceNotes.get(i) ;

		Glyph  g = new GlyphGraceNote( p, Beamer ) ;
	
		addGlyph( g, g.mY - mY ) ;	// Adjust position so it's in the right place
		
		if ( Beamer != null )   Beamer.addBeamGlyph(g) ;
	}
	
//  Calculate the beamer

	if ( Beamer != null ) {
		Beamer.calculate() ;
		addGlyphNoAdvance(Beamer) ;
		Beamer.mX = 0 ;
	}
}


/**
 * Get the note direction.
 */
public int  getDirection()
{	return  mDirection ;   }


/**
 * Generate the required accidental glyphs.
 */
public void  generateAccidentals( int Accidental, float y )
{	generateAccidentals(Accidental, y, this ) ;   }

public static void  generateAccidentals( int Accidental, float y, GlyphArray Array )
{
	switch ( Accidental ) {

	case  +2 :
		Array.addGlyph( new GlyphSharp(), y ) ;
		Array.addGlyph( new GlyphSharp(), y ) ;
		Array.mWidth -= 0.0F ;
		break ;
	case  +1 :
		Array.addGlyph( new GlyphSharp(), y ) ;
		Array.mWidth -= 0.0F ;
		break ;
	case  -2 :
		Array.addGlyph( new GlyphFlat(), y ) ;
		Array.addGlyph( new GlyphFlat(), y ) ;
		Array.mWidth -= 1.0F ;
		break ;
	case  -1 :
		Array.addGlyph( new GlyphFlat(), y ) ;
		Array.mWidth -= 1.0F ;
		break ;
	case  9 :
		Array.addGlyph( new GlyphNatural(), y ) ;
		Array.mWidth -= 0.5F ;
	}
}


/**
 * Generate the ledger lines.
 * 
 * <p>The scale factor is only applied vertically and is specifically for
 * getting the ledger lines in the right place when this is called by
 * the GlyphGraceNote class.   Scaling will be applied vertically so it must be
 * undone 
 */
public void  generateLedgerLines( int NoteIndex, float y )
{	generateLedgerLines( NoteIndex, y, this, 1.0F ) ;   }

public static void  generateLedgerLines( int NoteIndex, float y, GlyphArray Array,
										 float Scale )
{
	Stroke  Stroke = new BasicStroke( GlyphStave.STAVE_LINE_STROKE_WIDTH / Scale ) ;

//  Generate ledger lines at the bottom

	if ( NoteIndex < -8 ) {
		if ( NoteIndex % 2 != 0 ) {
			NoteIndex-- ;
			y += SL2 / Scale ;
		}
			
		while ( NoteIndex < -8 ) {
			Array.addGlyphNoAdvance( new GlyphLine( -1.0F, 0, 9.3F, 0,
													Stroke ), y ) ;
			y -= 2* SL2 / Scale ;	
			NoteIndex += 2 ; 
		}
	}

//  Generate ledger lines at the top

	else if ( NoteIndex > 2 ) {
		if ( NoteIndex % 2 != 0 ) {
			NoteIndex++ ;
			y -= SL2 / Scale ;
		}
		
		while ( NoteIndex > 2 ) {
			y += 2 * SL2 / Scale ;	
			Array.addGlyphNoAdvance( new GlyphLine( -1.5F, 0, 9.3F, 0,
													Stroke ), y ) ;
			NoteIndex -= 2 ; 
		}
	}
}


/**
 * Generate any gracings.
 */
public void  generateGracings( String Gracings, float y )
{
	if ( Gracings == null )   return ;

	if ( mDirection == 0 )   mDirection = +1 ;

//	Do a stacatto gracing

	if ( Gracings.indexOf('.') >= 0 ) {
		addGlyphNoAdvance( new GlyphDot(),
								  3.0F, y + mDirection * ( 2 * SL2 + 2 ) ) ;
	}
	
//	Determine y position of all following gracings

	y -= 7 ;
	if ( y + mY > - 3 * SL2 ) y = - mY - 3 * SL2 ;	// Min Y pos
	
//		Do a bow up gracing

	if ( Gracings.indexOf('u') >= 0 ) {
		addGlyphNoAdvance( new GlyphLine( 1.3F, 0, 4.3F, 4,
										  GlyphStave.STAVE_LINE_STROKE ), y ) ;
		addGlyphNoAdvance( new GlyphLine( 7.3F, 0, 4.3F, 4,
										  GlyphStave.STAVE_LINE_STROKE ), y ) ;
	}
	
//	Do a bow down gracing

	if ( Gracings.indexOf('v') >= 0 ) {
		addGlyphNoAdvance( new GlyphLine( 1, 0, 1, 4,
										  GlyphStave.STAVE_LINE_STROKE ), y ) ;
		addGlyphNoAdvance( new GlyphLine( 7, 0, 7, 4,
										  GlyphStave.STAVE_LINE_STROKE ), y ) ;
		addGlyphNoAdvance( new GlyphLine( 1, 0, 7, 0,
										  GlyphStave.STAVE_LINE_STROKE ), y ) ;
	}
	
//	Do a twiddle gracing

	if ( Gracings.indexOf('~') >= 0 ) {
		addGlyphNoAdvance( new GlyphText( "~", null ), 0.5F, y + 6 ) ;
	}
}


/**
 * Generate any guitar chord.
 */
public void  generateGuitarChord( String Chord, float y )
{
	if ( Chord == null )   return ;
	
//  Generate the necessary glyph

	Glyph  g = new GlyphText( Chord, GUITAR_CHORD_FONT ) ;
	addGlyphNoAdvance( g, 0.5F, y - 2 ) ;
}


/**
 * Generate the required note head glyph.
 */
public void  generateNoteHead()
{
	Glyph  g = getNoteHead(mLengthRange) ;
	addGlyphNoAdvance(g) ;
	mHeadWidth = g.getWidth() ;
}


/**
 * Generate the required rest head glyph.
 */
public void  generateRestHead()
{
	Glyph  g ;

	switch ( mLengthRange ) {
	case  2 :   g = new GlyphSemiBreveRest() ;	    break ;
	case  3 :   g = new GlyphMinimRest() ;	        break ;
	case  4 :   g = new GlyphQuarterRest() ;        break ;
	case  5 :   g = new GlyphEighthRest() ;	        break ;
	case  6 :   g = new GlyphSixteenthRest() ;	    break ;
	case  7 :   g = new GlyphThirtysecondRest() ;   break ;
	default :   g = new GlyphBreveRest() ;	        break ;
	}
	
//	Now determine which row to show the note on (y-coordinate) and add the glyph

	addGlyph( g, - 2 * SL2 ) ;
	
	mHeadWidth = g.getWidth() ;
	
//  Store the height against this glyph as it will be useful for tuplets
//  May not be needed later though !!

	mHeight = g.getHeight() ;
}


/**
 * Generate the required note tails.
 * 
 * <p>This returns the width value of the right hand side of the tail.
 * This is used to allow for any overlap of dots and tails.
 */
public float  generateTails()
{
		if ( mTailCount == 0 ) {
		mWidth += mHeadWidth ;
		return  mWidth ;		
	}
		
//	Draw tails on up stems

	Glyph   g = null  ;
	float  y = mStemEndY - 0.4F - TAIL_SEP ;
		
	if	( mDirection > 0 ) {
		mWidth += mHeadWidth ;
			
		for ( int i = 0 ; i < mTailCount ; i++ ) {
			y += TAIL_SEP ;
			
			g = new GlyphFlagUp() ;
			addGlyphNoAdvance( g, -0.3F, y ) ;
		}
		
		return  mWidth + g.getWidth() - 0.3F ;
	}
		
//	Draw tails on down stems
		
	y -= SL2 ;
		
	for ( int i = 0 ; i < mTailCount ; i++ ) {
		y -= TAIL_SEP ;
		
		g = new GlyphFlagDown() ;
		addGlyphNoAdvance( g, 0.7F, y ) ;
	}
		
	mWidth += mHeadWidth ;
	return  mWidth ;
}


/**
 * Generate the required dots.
 */
public void  generateDots( Note Note, int LengthRange, float y )
{
	Fraction  NoteLength = Note.getAbsoluteLength() ;
	int  NoteIndex = Note.getPitch().getNoteIndex() ;
	
//	Determine base length of the note and remove it

	Fraction  f = new Fraction(4) ;
	for ( int i = 0 ; i < LengthRange ; i++ )   f = f.dividedBy(2) ;

	Fraction  Remaining = NoteLength.minus(f) ;
	
//	Now count the number of dots required

	int  DotCount = 0 ;
	
	f = f.dividedBy(2) ;
	
	while ( Remaining.compareTo(f) >= 0 ) {
		DotCount++ ;
		Remaining = Remaining.minus(f) ;
		f = f.dividedBy(2) ;
	}
	
	if ( DotCount == 0 )   return ;

//	Now add the required number of dot glyphs

	y += ( NoteIndex % 2 != 0 ) ? -SL2 : 0 ;	// Put dots in spaces

	for ( int i = 0 ; i < DotCount ; i++ ) {
		mWidth += 1.0F ;
		addGlyph( new GlyphDot(), y ) ;
	}
}


/**
 * A helper method to determine the length range of a note.
 * 
 * <p>This is a static method and is also used by the GlyphMultiNote class.
 */
public static int  getLengthRange( Fraction Length )
{	
	if ( Length.compareTo( 1, 16 ) < 0 )   return  7 ;
	if ( Length.compareTo( 1, 8 ) < 0 )	   return  6 ;
	if ( Length.compareTo( 1, 4 ) < 0 )	   return  5 ;
	if ( Length.compareTo( 1, 2 ) < 0 )	   return  4 ;
	if ( Length.compareTo( 1 ) < 0 )	   return  3 ;
	if ( Length.compareTo( 2 ) < 0 )	   return  2 ;
	if ( Length.compareTo( 4 ) < 0 )	   return  1 ;
	return  0 ;
}


/**
 * Get the required note head glyph for a specified length range.
 * 
 * <p>This is a static method and is also used by the GlyphMultiNote class.
 */
public static Glyph  getNoteHead( int LengthRange )
{

//	Determine the correct node head
//			< 1/2   filled note head
//	1/2 <=  < 1     empty note head
//	1 <=    < 2     semi-breve
//	2 <=    < 4     breve
//	4 <= 			longa  	

	switch ( LengthRange ) {
	case  0 :   return  new GlyphLonga() ;
	case  1 :   return  new GlyphBreve() ;
	case  2 :   return  new GlyphSemiBreve() ;
	case  3 :   return  new GlyphEmptyNoteHead() ;
	default :   return  new GlyphFilledNoteHead() ;
	}
}


/**
 * Get the tail count for the given length range.
 */
public static int  getTailCount( int LengthRange )
{
	switch ( LengthRange ) {
	case  5 :   return  1 ;
	case  6 :   return  2 ;
	case  7 :   return  3 ;
	default :	return  0 ;
	}
}


/**
 * Initialize the padding space for this glyph. 
 */
public void  initPaddingSpace()
{
	mLeadingSpace = mTrailingSpace = 1 ;
}


/**
 * Initialize the padding space for this glyph based on its length range. 
 */
public void  initPaddingSpace( int LengthRange )
{
	switch ( LengthRange ) {
	case  0 :   mLeadingSpace = 14 ;   break ;		// 4	- Longa
	case  1 :   mLeadingSpace = 12 ;   break ;		// 2	- Breve
	case  2 :   mLeadingSpace = 10 ;   break ;		// 1	- Semi-breve
	case  3 :   mLeadingSpace = 8 ;   break ;		// 1/2	- Minim
	case  4 :   mLeadingSpace = 6 ;   break ;		// 1/4	- Crochet
	case  5 :   mLeadingSpace = 3 ;   break ;		// 1/8	- Quaver
	case  6 :   mLeadingSpace = 3 ;   break ;		// 1/16 - Semi-quaver
	case  7 :   mLeadingSpace = 3 ;   break ;		// 1/32 - Demi-semi quaver
	default :	mLeadingSpace = 0 ;   break ;
	}
	mTrailingSpace = mLeadingSpace ;
}


/**
 * Handle any words associated with this note.
 * 
 * <p>This method can be used by all the relevant sub-classes.
 */
public static void  handleWords( NoteElement e, Glyph g )
{
	if ( ! MusicBuilder.getShowLyrics() )   return ;
	
	ArrayList  a = e.getWords() ;
	if ( a == null )   return ;
	
	for ( int i = 0 ; i < a.size() ; i++ ) {
		WordsFieldElement.WordToken  t = (WordsFieldElement.WordToken) a.get(i) ;
		
	//  Keep a link in the word token to this glyph for drawing later
		
		t.mLinkedGlyph = g ;
		
	//  We should also ensure there is enough width (including leading and trailing space)
	//	to hold the word itself and allow a gap of 6 pixels
		
		if ( t.mWord != null ) {
			float  TextWidth = (float) Utils.getStringBounds( t.mWord, MusicBuilder.WORDS_FONT ).getWidth() ;
			
			float  AllocWidth = g.mLeadingSpace + g.mWidth + g.mTrailingSpace ;

			float  Increment = ( TextWidth + 6 - AllocWidth ) / 2 ;
			if ( Increment > 0 ) {
				g.mLeadingSpace += Increment ; 
				g.mTrailingSpace += Increment ; 
			}
		}
	}
}

}
