/**
 * A grace notes glyph.
 * 
 * <p>This kept separate from the normal note drawing algorithms rather
 * than sub-classing GlyphNoteBase.   This is because there are many differences
 * which are more easily maintained separately (particularly the scaling !)
 * 
 * <p>Grace notes are always drawn upwards
 */

package  abcj.model.music ;

import  abcj.model.abc.* ;
import  java.awt.* ;


public class GlyphGraceNote extends GlyphArray
{
/**
 * The stroke used to draw the note stem.
 */
	private static Stroke  GRACENOTE_STEM_STROKE = new BasicStroke(0.7F) ;
/**
 * The pitch represented by this grace note.
 */
	private Pitch  mPitch ;
/**
 * The index of the note onto the stave ( e = 0, f = 1, d = -1, ...)
 */
	protected int  mNoteIndex ;
/**
 * The width of the note head.
 */	
	protected float  mHeadWidth ;
/**
 * The x-coordinate of the note head.
 */	
	protected float  mHeadX ;
/**
 * The y-coordinate of the end of the loose end of the stem.
 * 
 * <p>This is used for drawing tails. 
 */
	private float  mStemEndY ;
/**
 * The grace note beamer used for drawing the beam.
 */
	private GlyphGraceBeamer  mBeamer ;

/**
 * The standard constructor.
 */
public  GlyphGraceNote( Pitch p, GlyphGraceBeamer Beamer )
{
	mPitch          = p ;
	mBeamer         = Beamer ;
	
	mLeadingSpace   = 0 ;
	mTrailingSpace  = 2 ;
	
	mScaleFactor    = 0.5F ;
	
	generateGlyphs() ;
}


/**
 * Generate all the relevant glyphs.
 */
public void  generateGlyphs()
{
	
//  Calculate the y-coordinate for the note head

	mNoteIndex  = mPitch.getNoteIndex() ;  // e -> 0

	mY = - mNoteIndex * SL2 + 1.4F ;


//  Now generate the relevant bits

	generateAccidentals() ;
	
	mHeadX = mWidth ;
	
	generateLedgerLines() ;
	generateNoteHead() ;
	
	if ( mBeamer == null ) {
		generateNoteStemAndStroke() ;
		generateTails() ;
	}
	else
		mWidth += mHeadWidth ;
	
//  As this thing will be scaled, we should scale the width too !

	mWidth *= mScaleFactor ;
}


/**
 * Generate the required accidental glyphs.
 */
public void  generateAccidentals()
{	GlyphNoteBase.generateAccidentals( mPitch.getAccidental(), 0, this ) ;   }
/**
 * Generate the ledger lines.
 */
public void  generateLedgerLines()
{	GlyphNoteBase.generateLedgerLines( mNoteIndex, -2.8F, this, mScaleFactor ) ;   }


/**
 * Generate a single filled note head.
 */
public void  generateNoteHead()
{
	Glyph  g = new GlyphFilledNoteHead() ;
	addGlyphNoAdvance(g) ;
	mHeadWidth = g.getWidth() ;
}


/**
 * Generate the required note stem.
 * 
 * <p>This is drawn slightly longer for a grace note as it will be scaled.
 */
public void  generateNoteStemAndStroke()
{
	
//	Always generate upwards stem

	mStemEndY = -6 * SL2 ;
	addGlyphNoAdvance( new GlyphLine( mHeadWidth - 0.5F, SL2 - 0.4F,
					  				  mHeadWidth - 0.5F, mStemEndY,
									  GRACENOTE_STEM_STROKE ) ) ;
	addGlyphNoAdvance( new GlyphLine( mHeadWidth / 3 - 0.5F, -SL2 - 0.4F,
									  5 * mHeadWidth / 3 - 0.5F,
									  mStemEndY + 2 * SL2,
									  GRACENOTE_STEM_STROKE ) ) ;
}


/**
 * Generate the required note tails.
 *
 * <p>This method only generates the single tail required by grace notes 
 */
public void  generateTails()
{
		
//	Draw tails on up stems

	float  y = mStemEndY - 0.4F ;
		
	mWidth += mHeadWidth ;
		
	Glyph  g = new GlyphFlagUp() ;
	addGlyphNoAdvance( g, -0.3F, y ) ;
		
	mWidth += g.getWidth() - 0.3F ;
}

}
