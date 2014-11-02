/**
 * An element representing a multi-note.
 * 
 * <p>This is based on <code>NoteElement</code> as there is much in common.
 */
package  abcj.model.abc ;

import  java.util.* ;
import  abcj.util.* ;

public class MultiNoteElement extends NoteElement
{
/**
 * The notes info of this multi note.
 */
	protected ArrayList  mNotes ;		


/**
 * The standard constructor.
 */
public  MultiNoteElement( String GuitarChord, ArrayList GraceNotes, String Gracings,
					 	  ArrayList Notes, int BrokenRhythm, boolean Beam )
{
	super( GuitarChord, GraceNotes, Gracings, null, BrokenRhythm, Beam ) ;

	setType(MULTINOTE_ELEMENT) ;
	mNotes = Notes ;
	
//  Create a dummy note element as if it were a single note so we can store
//  the first of the note lengths for overall length calculation

	mNote = new Note( new Pitch( '?', 0, 0 ), 0, 0, null ) ;
}


/**
 * Get the notes in this multi-note element.
 */
public ArrayList  getNotes()
{	return  mNotes ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "MultiNoteElement >" + format() +"<" ;   }


/**
 * Format the item as ABC text.
 */
public String  format()
{
	String s = "" ;
	if ( mGuitarChord != null )   s += "\"" + mGuitarChord + "\"" ;
	
	if ( mGraceNotes != null ) {
		s += "{" ;
		for ( int i = 0 ; i < mGraceNotes.size() ; i++ )
			s += mGraceNotes.get(i).toString() ;
		s += "}" ;
	}

	if ( mGracings != null )   s += mGracings ;
	
	s = s += "[" ;
	if ( mNotes != null )
		for ( int i = 0 ; i < mNotes.size() ; i++ )
			s += mNotes.get(i).toString() ;
	s = s += "]" ;
	
	s += formatBrokenRhythm() ;
	
	if ( ! mBeam )   s += " " ;
	
	return  s ;
}


/**
 * Calculate the absolute length of the note.
 */
public void  calculateAbsoluteLength( Fraction DefLength, Fraction Multiplier )
{
	if ( mNotes.size() == 0 )   return ;
	
//  Calculate the length for all notes in the multinote element

	for ( int i = 0 ; i < mNotes.size() ; i++ )
		( (Note) mNotes.get(i) ).calculateAbsoluteLength( DefLength, Multiplier ) ;
		
	mNote.mAbsoluteLength = ( (Note) mNotes.get(0) ).getAbsoluteLength() ;
}


/**
 * Calculate the absolute pitch of the notes in this multi-note.
 */
public void  calculateAbsolutePitch( Key Key )
{
	for ( int i = 0 ; i < mNotes.size() ; i++ )
		Key.calculateAbsolutePitch( ( (Note) mNotes.get(i) ).mPitch ) ;
	
//	Also do it for any grace notes

	if ( mGraceNotes != null )
	for ( int i = 0 ; i < mGraceNotes.size() ; i++ )
		Key.calculateAbsolutePitch( (Pitch) mGraceNotes.get(i) ) ;
}

}
