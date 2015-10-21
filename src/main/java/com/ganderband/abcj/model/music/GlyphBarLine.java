/**
 * The glyph class for a bar line.
 */
package  com.ganderband.abcj.model.music ;

import  com.ganderband.abcj.model.abc.* ;

public class GlyphBarLine extends GlyphArray
{
/**
 * The bar line element representing this ABC bar line.
 */
	private BarLineElement  mBarLine ;
	
	
/**
 * The default constructor.
 */
public  GlyphBarLine( BarLineElement Element )
{	this( 0, 0, Element ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphBarLine( float x, float y, BarLineElement BarLine )
{
	super( x, y ) ;
	mBarLine = BarLine ;
	mLeadingSpace = mTrailingSpace = 4 ;

//  Generate the relevant accidental glyphs for each key signature

	generateGlyphs() ;
}


/**
 * Generate the relevant glyphs.
 */
public void  generateGlyphs()
{
	
//  Generate the relevant glyphs for each bar-line type

	switch ( mBarLine.getBarLineType() ) {
		
	case  BarLineElement.DOUBLE :				//  ||
		addGlyph( new GlyphThinBarLine() ) ;
		addGlyph( new GlyphThinBarLine() ) ;
		mWidth -= 2 ;
		break ;
	
	case  BarLineElement.DOUBLE_THICK_RIGHT :	//  |]
		addGlyph( new GlyphThinBarLine() ) ;
		addGlyph( new GlyphThickBarLine() ) ;
		mWidth -= 2 ;
		break ;
	
	case  BarLineElement.SINGLE_REP_RIGHT :		//	|:
		addGlyph( new GlyphThickBarLine() ) ;
		addGlyph( new GlyphThinBarLine() ) ;
		mWidth += 0.1F ;
		generateRepeatDots() ;
		break ;
	
	case  BarLineElement.DOUBLE_THICK_LEFT :	//  [|
		addGlyph( new GlyphThickBarLine() ) ;
		addGlyph( new GlyphThinBarLine() ) ;
		mWidth -= 2 ;
		break ;
	
	case  BarLineElement.DOUBLE_REP :			//  ::
		generateRepeatDots() ;
		mWidth += 1.9F ;
		addGlyph( new GlyphThinBarLine() ) ;
		addGlyph( new GlyphThickBarLine() ) ;
		addGlyph( new GlyphThinBarLine() ) ;
		mWidth += 0.1F ;
		generateRepeatDots() ;
		break ;

	case  BarLineElement.SINGLE_REP_LEFT :		//  :|
		generateRepeatDots() ;
		mWidth += 1.9F ;
		addGlyph( new GlyphThinBarLine() ) ;
		addGlyph( new GlyphThickBarLine() ) ;
		mWidth -= 2 ;
		break ;

	case  BarLineElement.SINGLE :				//  |
		addGlyph( new GlyphThinBarLine() ) ;
		mWidth -= 2 ;
		break ;
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

}
