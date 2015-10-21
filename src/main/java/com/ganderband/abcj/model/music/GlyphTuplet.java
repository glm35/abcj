/**
 * The glyph class for a tuplet.
 */
package  com.ganderband.abcj.model.music ;

import  java.awt.* ;
import  java.util.* ;

import  com.ganderband.abcj.model.abc.* ;
import  com.ganderband.abcj.util.* ;

public class GlyphTuplet extends GlyphArray
{
/**
 * The font to use for generating the tuplet number.
 */
	protected static final Font  TUPLET_FONT = new Font( "SansSerif", Font.PLAIN, 10 ) ;
/**
 * The TupletElement instance representing this (from the ABC model)
 */
	protected TupletElement  mTupletElement ;
/**
 * A flag to indicate that the note stem direction is forced to a certain value.
 * 
 * p>The bagpipe keys will set this. If 0 then nothing happens.
 */
	private int  mForceDirection ;
/**
 * A beamer used to beam the notes of this tuplet.
 */	
	protected GlyphBeamer  mBeamer ;	
/**
 * A slur used to mark the notes of this tuplet if no beamer.
 */	
	protected GlyphSlur  mSlur ;	
/**
 * The special multipler for this tuplet.
 * 
 * <p>This is inverted from the one supplied to the tuplet element.
 */
	private  Fraction  mSpecMult ;	
	
/**
 * The default constructor.
 */
public  GlyphTuplet( TupletElement Element, int ForceDirection )
{	this( 0, 0, Element, ForceDirection ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphTuplet( float x, float y, TupletElement Element, int  ForceDirection )
{
	super( x, y ) ;
	mTupletElement  = Element ;
	mForceDirection = ForceDirection ;
	
//	Get the special multiplier for the tuplet and invert it.
//	Doing this will enable us to tell the GlyphNote class what to
//	draw for the note as it will be different to its actual length because
//	of the tuplet spec.

	mSpecMult = mTupletElement.getSpecialMultiplier().inverse() ;

//  Generate the beamer and the relevant glyphs for each note

	generateBeamer() ;
	generateGlyphs() ;
}


/**
 * Generate the glyphs required to draw this note.
 */
public void  generateGlyphs()
{
	mSlur = new GlyphSlur(null) ;	// Won't go across staves, don't need builder

	ArrayList  Notes = mTupletElement.getNoteElements() ;
	
	for ( int i = 0 ; i < Notes.size() ; i++ ) {
		NoteElement  e = (NoteElement) Notes.get(i) ;
		
		GlyphNoteBase  g ;
		if ( e.getType() == REST_ELEMENT )
			g = new GlyphRest( (RestElement) e, mSpecMult ) ;
		else if ( e.getType() == NOTE_ELEMENT )
			g = new GlyphSingleNote( e, mForceDirection, mSpecMult, mBeamer ) ;
		else 
			g = new GlyphMultiNote( (MultiNoteElement) e, mForceDirection,
									mSpecMult, mBeamer ) ;
			
	//  Now tell the glyph its part of a tuplet and add it to the tuplet
	//  and the beamer if it is beamed.
	//  It should always be added to the slur.

		g.mTuplet = this ;
		
		addGlyph(g) ;
	
		if ( mBeamer != null )   mBeamer.addBeamGlyph(g) ;
		mSlur.addSlurGlyph(g) ;
		
	//  We should also allocate any words associated with this note
		
		GlyphNoteBase.handleWords( e, g ) ;
	}
}


/**
 * Generate any beamer required to beam this tuplet.
 */
public void  generateBeamer()
{
	mBeamer = null ;
	
//  For a tuplet, all notes must be < 1/4 and all bar the last must be beamed

	ArrayList  Notes = mTupletElement.getNoteElements() ;
	
	for ( int i = 0 ; i < Notes.size() ; i++ ) {
		NoteElement  e = (NoteElement) Notes.get(i) ;
		
	//  If first note then check for a real note rather than a rest
	
		if ( i == 0  &&  e.getType() == REST_ELEMENT )   return ;
		
	//  Check for too long.  Allow for an error where a multi-note is empty
	//  and therefore will not have an absolute length
	
		Fraction  f = e.getNote().getAbsoluteLength() ;
		
		if ( f == null  ||  f.times(mSpecMult).compareTo( 1, 4 ) >= 0  )
			return ;
		
	//  Check all bar last for beaming
	
		if ( i < Notes.size() - 1  &&  ! e.isBeam() )
			return ;
		
	//  If first note then check for a real note rather than a rest
	
		if ( i == Notes.size() - 1  &&  e.getType() == REST_ELEMENT )   return ;
	}
	
//  All valid for beaming, create a beamer

	mBeamer = new GlyphBeamer(mForceDirection) ;
}

}
