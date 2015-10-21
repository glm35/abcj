/**
 * An element representing a note.
 */
package  com.ganderband.abcj.model.abc ;

import  java.util.* ;
import  com.ganderband.abcj.util.* ;

public class NoteElement extends ABCElement
{
/**
 * The guitar chord.
 */
	protected String  mGuitarChord ;
/**
 * The grace notes.
 */
	protected ArrayList  mGraceNotes ;
/**
 * The gracings.
 */
	protected String  mGracings ;
/**
 * The note info of this note (pitch, num, denom, tie).
 */
	protected Note  mNote ;		
/**
 * The broken rhythm indication.
 */
	protected int  mBrokenRhythm ;
/**
 * Beam to next note or not.
 */
	protected boolean  mBeam = false ;
/**
 * An array list of linked word tokens.
 */
	protected ArrayList  mWordTokens ;
	
	
/**
 * The standard constructor.
 */
public  NoteElement( String GuitarChord, ArrayList GraceNotes, String Gracings,
					 Note Note, int BrokenRhythm, boolean Beam )
{
	super(NOTE_ELEMENT) ;
	mGuitarChord  = GuitarChord ;
	mGraceNotes   = GraceNotes ;
	mGracings     = Gracings ;
	mNote         = Note ;
	mBrokenRhythm = BrokenRhythm ;
	mBeam         = Beam ;
}


/**
 * Set beam state.
 */
public void  setBeam( boolean Beam )
{	mBeam = Beam ;   }


/**
 * Get the contained note.
 */
public Note  getNote()
{	return  mNote ;   }


/**
 * Get the gracings.
 */
public String  getGracings()
{	return  mGracings ;   }


/**
 * Get the grace notes.
 */
public ArrayList  getGraceNotes()
{	return  mGraceNotes ;   }


/**
 * Get the guitar chord.
 */
public String  getGuitarChord()
{	return  mGuitarChord ;   }


/**
 * Set the guitar chord.
 */
public void  setGuitarChord( String Chord )
{	mGuitarChord = Chord ;   }


/**
 * Get the broken rhythm indicator.
 */
public int  getBrokenRhythm()
{	return  mBrokenRhythm ;   }


/**
 * Get any words associated with this note.
 */
public ArrayList  getWords()
{	return  mWordTokens ;   }


/**
 * Get the beaming status.
 */
public boolean  isBeam()
{	return  mBeam ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "NoteElement >" + format() +"<" ;   }


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

	if ( mGracings != null )       s += mGracings ;
	if ( mNote != null )           s += mNote.toString() ;
	
	s += formatBrokenRhythm() ;
	
	if ( ! mBeam )				   s += " " ;
		
	return  s ;
}


/**
 * Format the broken rhythm indication as a string.
 */
protected String  formatBrokenRhythm()
{
	String  s = "" ;
	
	if ( mBrokenRhythm < 0 ) {
		for ( int i = 0 ; i < -mBrokenRhythm ; i++ )
			s+= "<" ;	
	}
	else {
		for ( int i = 0 ; i < mBrokenRhythm ; i++ )
			s += ">" ;	
	}
		
	return  s ;
}


/**
 * Calculate the absolute length of the note.
 */
public void  calculateAbsoluteLength( Fraction DefLength, Fraction Multiplier )
{
	mNote.calculateAbsoluteLength( DefLength, Multiplier ) ;
}


/**
 * Calculate the absolute pitch of the note.
 */
public void  calculateAbsolutePitch( Key Key )
{
	mNote.mKey = Key ;
	Key.calculateAbsolutePitch(mNote.mPitch) ;
	
//  Also do it for any grace notes

	if ( mGraceNotes != null )
	for ( int i = 0 ; i < mGraceNotes.size() ; i++ )
		Key.calculateAbsolutePitch( (Pitch) mGraceNotes.get(i) ) ;
}


/**
 * Link a word to this note.
 */
public void  linkWord( WordsFieldElement.WordToken t )
{
	if ( mWordTokens == null )   mWordTokens = new ArrayList() ;
	mWordTokens.add(t) ;
}

}
