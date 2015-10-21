/**
 * The glyph class for an nth repeat.
 */
package  com.ganderband.abcj.model.music ;

import  java.awt.* ;
import  com.ganderband.abcj.model.abc.* ;

public class GlyphNthRepeat extends GlyphArray
{
/**
 * The font to use for generating the repeat number.
 */
	private static final Font  REPEAT_FONT = new Font( "SansSerif", Font.PLAIN, 8 ) ;
/**
 * The Nth repeat element representing this ABC Nth repeat.
 */
	private NthRepeatElement  mNthRepeat ;
	
	
/**
 * The default constructor.
 */
public  GlyphNthRepeat( NthRepeatElement Element )
{	this( 0, 0, Element ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphNthRepeat( float x, float y, NthRepeatElement Element )
{
	super( x, y ) ;
	mNthRepeat = Element ;
	mLeadingSpace = mTrailingSpace = 2 ;

//  Generate the relevant accidental glyphs for each key signature

	generateGlyphs() ;
}


/**
 * Generate the relevant glyphs.
 */
public void  generateGlyphs()
{
	
//  Generate the relevant glyphs for each bar-line type

	switch ( mNthRepeat.getNthRepeatType() ) {
		
	case  NthRepeatElement.FIRST :
		generateRepeat("1") ;
		break ;		
		
	case  NthRepeatElement.SECOND :
		generateRepeat("2") ;
		break ;		
		
//	case  NthRepeatElement.FIRST_FULL :			// No longer needed
//	case  NthRepeatElement.BARLINE_FIRST :		// these have been split into
//		generateRepeat("1") ;					// separate bar-line and repeat only
//		addGlyph( new GlyphThinBarLine() ) ;	// glyphs by the MusicBuilder class
//		mWidth -= 2 ;
//		break ;		
		
//	case  NthRepeatElement.SECOND_FULL :
//	case  NthRepeatElement.BARLINE_SECOND :
//		generateRepeatDots() ;
//		mWidth += 1.9F ;
//		generateRepeat("2") ;
//		addGlyph( new GlyphThinBarLine() ) ;
//		addGlyph( new GlyphThickBarLine() ) ;
//		mWidth -= 2 ;
//		break ;		
	}
}


/**
 * Generate the relevant glyphs for repeat dots.
 */
public void  generateRepeatDots()
{
	addGlyphNoAdvance( new GlyphDot(),     STAVE_LINE_SPACING ) ;
	addGlyph         ( new GlyphDot(), 2 * STAVE_LINE_SPACING ) ;
}


/**
 * Generate the repeat identifier itself.
 */
public void  generateRepeat( String Text )
{

//  Generate the relevant lines

	addGlyphNoAdvance( new GlyphLine( 0.5F, -3,    0.5F, -15,
									  GlyphStave.STAVE_LINE_STROKE ) ) ;
	addGlyphNoAdvance( new GlyphLine( 0.5F, -15,  12.5F, -15,
									  GlyphStave.STAVE_LINE_STROKE ) ) ;

//  and add the text
	
	addGlyphNoAdvance( new GlyphText( Text, REPEAT_FONT ), 3, -7 ) ;
}

}
