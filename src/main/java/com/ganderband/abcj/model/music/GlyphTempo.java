/**
 * The glyph class for a tempo.
 */
package  abcj.model.music ;

import  abcj.model.abc.* ;

public class GlyphTempo extends GlyphArray
{
/**
 * The tempo element representing this tempo (from the ABC model)
 */
	private TempoFieldElement  mTempo ;

	
/**
 * The default constructor.
 */
public  GlyphTempo( TempoFieldElement Element )
{	this( 0, 0, Element ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphTempo( float x, float y, TempoFieldElement Element )
{
	super( x, y ) ;
	mTempo = Element ;

//  Generate the relevant glyphs to indicate the tempo

	generateGlyphs() ;
}


/**
 * Generate the relevant glyphs to indicate this tempo.
 */
public void  generateGlyphs()
{
	
//  Build and add a note element which can be used to indicate the tempo

	Note  n = new Note( new Pitch( 'e', 0, 0 ), 1, 1, null ) ;
	n.setAbsoluteLength( mTempo.getMeterFraction() ) ;
	NoteElement  ne = new NoteElement( null, null, null, n, 0, false )	 ;
	
	addGlyph( new GlyphSingleNote( ne, +1, null, null ), -25 ) ;

//  Now add the text '=nnn'

	addGlyph( new GlyphText( "=" + mTempo.getTempo(), null ), -25 ) ;
	
//  Indicate thate we should draw this a little smaller

	mScaleFactor = 0.7F ;
	
//  We should also force the width to 0 as this glyph is drawn in parallel
//  and should not affect any resizing

	mWidth = 0 ;
}

}
