/**
 * The glyph class for a key signature.
 */
package  abcj.model.music ;

import  abcj.model.abc.* ;

public class GlyphKeySignature extends GlyphArray
{
/**
 * The Key instance representing this (from the ABC model)
 */
	private Key  mKey ;
/**
 * A static array containing the relevant y coordinate for each accidental.
 * 
 * <p>This is the y coordinate where it should be placed when in a key sig
 * with all flats.
 */
	private static final float[]  YCOORD_FLATS = {
		STAVE_LINE_SPACING,				// C
		STAVE_LINE_SPACING / 2,			// D
		0,								// E
		3 * STAVE_LINE_SPACING,			// F
		5 * STAVE_LINE_SPACING / 2,		// G
		2 * STAVE_LINE_SPACING,			// A
		3 * STAVE_LINE_SPACING / 2,		// B
	} ;
/**
 * The order to check for flats to ensure the key sig comes out right.
 * 
 * <p>I.E. the accidentals are in the right order. 
 */
	private static final int[]  FLAT_ORDER = { 6, 2, 5, 1, 4, 0, 3 } ;
/**
 * A static array containing the relevant y coordinate for each accidental.
 * 
 * <p>This is the y coordinate where it should be placed when in a key sig
 * with all flats.
 */
	private static final float[]  YCOORD_SHARPS = {
		STAVE_LINE_SPACING,				// C
		STAVE_LINE_SPACING / 2,			// D
		0,								// E
		- STAVE_LINE_SPACING / 2,		// F
		- STAVE_LINE_SPACING,			// G
		2 * STAVE_LINE_SPACING,			// A
		3 * STAVE_LINE_SPACING / 2,		// B
	} ;
/**
 * The order to check for sharps to ensure the key sig comes out right.
 * 
 * <p>I.E. the accidentals are in the right order. 
 */
	private static final int[]  SHARP_ORDER = { 3, 0, 4, 1, 5, 2, 6 } ;

	
/**
 * The default constructor.
 */
public  GlyphKeySignature( Key Key )
{	this( 0, 0, Key ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphKeySignature( float x, float y, Key Key )
{
	super( x, y ) ;
	mKey = Key ;
	initPaddingSpace() ;

//  Generate the relevant accidental glyphs for each key signature

	generateGlyphs() ;
}


/**
 * Generate the relevant accidental glyphs for each key signature.
 */
public void  generateGlyphs()
{
	
//  Initialize

	float   y ;
	String  Note = mKey.getBaseNote() ;
	int[]   Accs = mKey.getAccidentals() ;
	
	mWidth = 0 ;
	
//  Check for "HP" (no sig) 

	if ( Note.equals("HP") )   return ;
	
//	Check for "Hp" (F#, C#, Gnatural) 

	if ( Note.equals("Hp") ) {
		
		y = - STAVE_LINE_SPACING / 2 ;		// F
		addGlyph ( new GlyphSharp(), y ) ;
		
		y = STAVE_LINE_SPACING ; 		 	// C
		addGlyph ( new GlyphSharp(), y ) ;

		y = - STAVE_LINE_SPACING ;		  	// G
		addGlyph ( new GlyphNatural(), y ) ;
		
		return ;
	}
	
//  All other keys are either sharps or flats only - Add the sharps

	if ( mKey.getAccidentalCount() > 0 ) {
		
		for ( int i = 0 ; i < 7 ; i++ ) {
			if ( Accs[ SHARP_ORDER[i] ] == +1  ) {
				y = YCOORD_SHARPS[ SHARP_ORDER[i] ] ;
				addGlyph ( new GlyphSharp(), y ) ;
			}
		}
	}
	
//  Add the flats

	else {
	
		for ( int i = 0 ; i < 7 ; i++ ) {
			if ( Accs[ FLAT_ORDER[i] ] == -1  ) {
				y = YCOORD_FLATS[ FLAT_ORDER[i] ] ;
				addGlyph ( new GlyphFlat(), y ) ;
			}
		}
	}
}


/**
 * Initialize the trailing space for this glyph. 
 */
public void  initPaddingSpace()
{
	mLeadingSpace  = 2 ;
	mTrailingSpace = 1 ;
}

}
