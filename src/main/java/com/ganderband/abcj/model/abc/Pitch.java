/**
 * A class representing a pitch in ABC.
 */
package abcj.model.abc;

public class Pitch
{
/**
 * Valid base notes.
 */
	public static final String  VALID_BASENOTES = "CDEFGABcdefgab" ;
/**
 * The MIDI pitch value for each valid base note.
 * 
 * <p>Middle C = 60 = C6
 */
	public static final int[]  MIDI_PITCH = {
			60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79, 81, 83
		} ;
/**
 * Valid accidentals.
 */
	public static final String[]  VALID_ACCIDENTALS = {
			"^^", "__", "^", "_", "="
		} ;
/**
 * The pitch of the note.
 */
	protected char  mBaseNote ;
/**
 * The accidentals associated with it.
 * 
 * <p>+1, +2, -1, -2 or 9 (natural) or 0 (none)
 */
	protected int  mAccidental ;
/**
 * The octave adjusters associated with it.
 * 
 * <p>+ is octaves up, - is octaves down.
 */
	protected int  mOctave ;
/**
 * The MIDI note value of the pitch to play.
 * 
 * <p>This is absolute and will be set by the Key.calculateAbsolutePitch method.
 */
	protected int  mMidiPitch = 0 ;


/**
 * The standard constructor.
 */
public  Pitch( char BaseNote, int Accidental, int Octave )
{	mBaseNote = BaseNote ;   mAccidental = Accidental ;   mOctave = Octave ;   }


/**
 * Get the Base Note of the pitch.
 */
public char  getBaseNote()
{	return  mBaseNote ;   }


/**
 * Get the Octave adjustment of the pitch.
 */
public int  getOctave()
{	return  mOctave ;   }


/**
 * Set the Base Note of the pitch.
 */
public void  setBaseNote( char BaseNote )
{	mBaseNote = BaseNote ;   }


/**
 * Set the Octave adjustment of the pitch.
 */
public void  setOctave( int Octave )
{	mOctave = Octave ;   }


/**
 * Set the Accidentals of the pitch.
 */
public void  setAccidental( int Accidental )
{	mAccidental = Accidental ;   }


/**
 * Get the Midi Pitch of the pitch.
 */
public int  getMidiPitch()
{	return  mMidiPitch ;   }


/**
 * Get the Accidental of the pitch.
 */
public int  getAccidental()
{	return  mAccidental ;   }


/**
 * Get the note index of the pitch (allowing for octaves).
 */
public int  getNoteIndex()
{
	return  VALID_BASENOTES.indexOf(mBaseNote) - 9 + 7 * mOctave ;  // e -> 0
}

/**
 * Get the base note index of the pitch (don't allow for octaves).
 */
public int  getBaseNoteIndex()
{	return  VALID_BASENOTES.indexOf(mBaseNote) ;   }  // C -> 0, c -> 7


/**
 * Convert to a string.
 */
public String  toString()
{
	String s = "" ;
	
	switch ( mAccidental ) {
	case  +1 :   s += "^" ;    break ;
	case  +2 :   s += "^^" ;   break ;   
	case  -1 :   s += "_" ;    break ;
	case  -2 :   s += "__" ;   break ;
	case   9 :   s += "=" ;    break ;
	}
	
	s += mBaseNote ;
	
	if ( mOctave > 0 ) {
		for ( int i = 0 ; i < mOctave ; i++ )    s += "'" ;
	}
	else if ( mOctave < 0 ) {
		for ( int i = 0 ; i < -mOctave ; i++ )   s += "," ;
	}

	return  s ;
}

}
