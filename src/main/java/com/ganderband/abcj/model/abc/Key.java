/**
 * A class to represent a key.
 * 
 * <p>This class will contain all key related information together with
 * the relevant methods to determine sharps and flats etc.
 */
package  com.ganderband.abcj.model.abc ;

import  java.util.* ;

public class Key implements ABCParserConstants
{
/**
 * The default key if none provided.
 */
	public static final String  DEFAULT_KEY = "C" ;
/**
 * Valid key accidentals.
 */
	public static final String  VALID_ACCIDENTALS = "#b" ;
/**
 * Valid modes.
 */
	public static final String[]  VALID_MODES = {
			"maj", "min", "ion", "dor", "phr", "lyd", "mix", "aoe", "loc", "m"
		} ;
/**
 * The note this key is based on.
 * 
 * <p>This can be "HP", "Hp", or any valid real note (C#, Bb, ...).
 */
	private String  mNote ;
/**
 * The mode of this key.
 */
	private String  mMode ;
/**
 * The global accidentals for this key.
 * 
 * <p>This is an array of pitches.
 */
	private ArrayList  mGlobAcc ;
/**
 * An array indicating which sharps and flats on the key sig.
 */
	private int[]  mAccidentals ;
/**
 * An array indicating the special sharps and flats for global accidentals.
 * 
 * <p>+1 = 1 sharp, +2 = 2 sharps, -1 = 1 flat, -2 = 2 flats, 9 = natural, 0 = none
 */
	private int[]  mGlobalAccidentals = new int[7] ;
/**
 * The array of local accidentals.
 * 
 * <p>This is used to remember any accidentals applied during a bar.
 */
	private int[]  mLocalAccidentals = new int[7] ;
/**
 * The saved array of local accidentals.
 * 
 * <p>This is used to remember any accidentals applied during a bar.
 */
	private int[]  mSavedLocalAccidentals = new int[7] ;
/**
 * The key names corresponding to the table below.
 */
	public static String[]  VALID_KEY_NAMES = {
				"Cb", "C", "C#", 
				"Db", "D", "D#", 
				"Eb", "E", "E#", 
				"Fb", "F", "F#", 
				"Gb", "G", "G#", 
				"Ab", "A", "A#", 
				"Bb", "B", "B#"
		} ;
/**
 * The notes to sharpen or flatten for each key in the relevant major scale.
 * 
 * <p>Note that we allow each the key sig to be sharp or flat as this is valid
 * for Cbmaj and C#maj.
 * 
 * <p>0 = no accidental, +1 = sharp, -1 = flat, 9 = natural, 99 = invalid
 * 
 * <p>The valid major scales are as follows :-
 * 
 * 	C  (no sharps)	C   D   E   F   G   A   B
 * 	G  (1 sharp)	G   A   B   C   D   E   F#
 * 	D  (2 sharps)	D   E   F#  G   A   B   C#
 * 	A  (3 sharps)	A   B   C#  D   E   F#  G#
 * 	E  (4 sharps)	E   F#  G#  A   B   C#  D#
 * 	B  (5 sharps)	B   C#  D#  E   F#  G#  A#
 * 	F# (6 sharps) 	F#  G#  A#  B   C#  D#  E#
 * 	C# (7 sharps)	C#  D#  E#  F#  G#  A#  B#
 * 	F  (1 flat) 	F   G   A   Bb  C   D   E
 * 	Bb (2 flats)	Bb  C   D   Eb  F   G   A
 * 	Eb (3 flats)	Eb  F   G   Ab  Bb  C   D
 * 	Ab (4 flats)	Ab  Bb  C   Db  Eb  F   G
 * 	Db (5 flats)	Db  Eb  F   Gb  Ab  Bb  C
 * 	Gb (6 flats)	Gb  Ab  Bb  Cb  Db  Eb  F
 * 	Cb (7 flats)	Cb  Db  Eb  Fb  Gb  Ab  Bb
 * 
 * 	OR in note order
 * 
 * 	Cb (7 flats)	Cb  Db  Eb  Fb  Gb  Ab  Bb
 * 	C  (no sharps)	C   D   E   F   G   A   B
 * 	C# (7 sharps)	C#  D#  E#  F#  G#  A#  B#
 * 	Db (5 flats)	Db  Eb  F   Gb  Ab  Bb  C
 * 	D  (2 sharps)	D   E   F#  G   A   B   C#
 * 	Eb (3 flats)	Eb  F   G   Ab  Bb  C   D
 * 	E  (4 sharps)	E   F#  G#  A   B   C#  D#
 * 	F  (1 flat) 	F   G   A   Bb  C   D   E
 * 	F# (6 sharps) 	F#  G#  A#  B   C#  D#  E#
 * 	Gb (6 flats)	Gb  Ab  Bb  Cb  Db  Eb  F
 * 	G  (1 sharp)	G   A   B   C   D   E   F#
 * 	Ab (4 flats)	Ab  Bb  C   Db  Eb  F   G
 * 	A  (3 sharps)	A   B   C#  D   E   F#  G#
 * 	Bb (2 flats)	Bb  C   D   Eb  F   G   A
 * 	B  (5 sharps)	B   C#  D#  E   F#  G#  A#
 */
	private static final int[][]  MAJ_ACCIDENTALS = {
		
//         C   D   E   F   G   A   B		
		
		{ -1, -1, -1, -1, -1, -1, -1 } ,		// 0  = Cb maj		OK
		{  0,  0,  0,  0,  0,  0,  0 } ,		// 1  = C maj		OK
		{ +1, +1, +1, +1, +1, +1, +1 } ,		// 2  = C# maj		OK
		
//         C   D   E   F   G   A   B		
		
		{  0, -1, -1,  0, -1, -1, -1 } ,		// 3  = Db maj		OK
		{ +1,  0,  0, +1,  0,  0,  0 } ,		// 4  = D maj		OK
		{ 99,  0,  0,  0,  0,  0,  0 } ,		// 5  = D# maj		Invalid
		
//		   C   D   E   F   G   A   B		
		
		{  0,  0, -1,  0,  0, -1, -1 } ,		// 6  = Eb maj		OK
		{ +1, +1,  0, +1, +1,  0,  0 } ,		// 7  = E maj		OK
		{ 99,  0,  0,  0,  0,  0,  0 } ,		// 8  = E# maj		Invalid
		
//		   C   D   E   F   G   A   B		
		
		{ 99,  0,  0,  0,  0,  0,  0 } ,		// 9  = Fb maj		Invalid
		{  0,  0,  0,  0,  0,  0, -1 } ,		// 10 = F maj		OK
		{ +1, +1, +1, +1, +1, +1,  0 } ,		// 11 = F# maj		OK
		
//		   C   D   E   F   G   A   B		
		
		{ -1, -1, -1,  0, -1, -1, -1 } ,		// 12 = Gb maj		OK
		{  0,  0,  0, +1,  0,  0,  0 } ,		// 13 = G maj		OK
		{ 99,  0,  0,  0,  0,  0,  0 } ,		// 14 = G# maj		Invalid
		
//		   C   D   E   F   G   A   B		
		
		{  0, -1, -1,  0,  0, -1, -1 } ,		// 15 = Ab maj		OK
		{ +1,  0,  0, +1, +1,  0,  0 } ,		// 16 = A maj		OK
		{ 99,  0,  0,  0,  0,  0,  0 } ,		// 17 = A# maj		Invalid
		
//         C   D   E   F   G   A   B		
		
		{  0,  0, -1,  0,  0,  0, -1 } ,		// 18 = Bb maj		OK
		{ +1, +1,  0, +1, +1, +1,  0 } ,		// 19 = B maj		OK
		{ 99,  0,  0,  0,  0,  0,  0 } ,		// 20 = B# maj		Invalid
	} ;
/**
 * The notes to sharpen or flatten for each key in the "HP" pipe scale.
 */
	private static final int[]  HP_ACCIDENTALS =
		
//		   C   D   E   F   G   A   B		
		
		{  0,  0,  0,  0,  0,  0,  0 } ;
/**
 * The notes to sharpen or flatten for each key in the "HP" pipe scale.
 */
	private static final int[]  Hp_ACCIDENTALS =
		
//		   C   D   E   F   G   A   B		
		
		{ +1,  0,  0, +1,  9,  0,  0 } ;		// C#, F#, Gnatural
/**
 * A table to map modes to the equivalent major key signature.
 *
 * <p>Key sig equivalents
 *
 * <p>Cmaj = Amin = Cion = Ddor = Ephr = Flyd = Gmix = Aaeo = Bloc
 *
 *  <p>Tone relationships in each mode
 *
 *  maj = T T t T T T t
 *  min = T t T T t T T
 *  ion = T T t T T T t	(same as maj)
 *  dor = T t T T T t T
 *  phr = t T T T t T T
 *  lyd = T T T t T T t
 *  mix = T T t T T t T
 *  aeo = T t T T t T T (same as min)
 *  loc = t T T t T T T
 */
	private static final int[][]  MODE_MAP = {
		
// 	C# (7 sharps)	C#  D#  E#  F#  G#  A#  B#		2

//		  maj min ion dor phr lyd mix aeo loc
		
		{  0, 99,  0, 99, 99, 12, 99, 99, 99  } ,		// 0  = Cb
		{  1,  6,  1, 18, 15, 13, 10,  6,  3  } ,		// 1  = C
		{  2,  7,  2, 19, 16, 99, 11,  7,  4  } ,		// 2  = C#

//		  maj min ion dor phr lyd mix aeo loc
		
		{  3, 99,  3,  0, 99, 15, 12, 99, 99  } ,		// 3  = Db
		{  4, 10,  4,  1, 18, 16, 13, 10,  6  } ,		// 4  = D
		{ 99, 11, 99,  2, 19, 99, 99, 11,  7  } ,		// 5  = D#
			
//		  maj min ion dor phr lyd mix aeo loc
		
		{  6, 12,  6,  3,  0, 18, 15, 12, 99  } ,		// 6  = Eb
		{  7, 13,  7,  4,  1, 19, 16, 13, 10  } ,		// 7  = E
		{ 99, 99, 99, 99,  2, 99, 99, 99, 11  } ,		// 8  = E#
		
//		  maj min ion dor phr lyd mix aeo loc
		
		{ 99, 99, 99, 99, 99,  0, 99, 99, 99  } ,		// 9  = Fb
		{ 10, 15, 10,  6,  3,  1, 18, 15, 12  } ,		// 10 = F
		{ 11, 16, 11,  7,  4,  2, 19, 16, 13  } ,		// 11 = F#

//		  maj min ion dor phr lyd mix aeo loc
		
		{ 12, 99, 12, 99, 99,  3,  0, 99, 99  } ,		// 12 = Gb
		{ 13, 18, 13, 10,  6,  4,  1, 18, 15  } ,		// 13 = G
		{ 99, 19, 99, 11,  7, 99,  2, 19, 16  } ,		// 14 = G#

//		  maj min ion dor phr lyd mix aeo loc
		
		{ 15,  0, 15, 12, 99,  6,  3,  0, 99  } ,		// 15 = Ab
		{ 16,  1, 16, 13, 10,  7,  4,  1, 18  } ,		// 16 = A
		{ 99,  2, 99, 99, 11, 99, 99,  2, 19  } ,		// 17 = A#

//		  maj min ion dor phr lyd mix aeo loc
		
		{ 18,  3, 18, 15, 12, 10,  6,  3,  0  } ,		// 18 = Bb
		{ 19,  4, 19, 16, 13, 11,  7,  4,  1  } ,		// 19 = B
		{ 99, 99, 99, 99, 99, 99, 99, 99,  2  } ,		// 20 = B#

	} ;
/**
 * A table which defines the number of semi-tones to the next note.
 * 
 * <p>This table assumes no accidental are in force.
 */	
	private static final int[]  SEMITONES_UP = { 2, 2, 1, 2, 2, 2, 1 } ;

	
/**
 * Construct an instance.
 * 
 * <p>This will throw an exception if the key is not valid.
 */
public  Key( String Note, String Mode, ArrayList GlobAcc )   throws ParserException
{
	mNote    = Note ;
	mMode    = Mode ;
	mGlobAcc = GlobAcc ;
	parseKey() ;
	parseGlobalAccidentals() ;
}


/**
 * Get the base note.
 */
public String  getBaseNote()
{	return  mNote ;   }


/**
 * Get the mode.
 */
public String  getMode()
{	return  mMode ;   }


/**
 * Get the global accidentals.
 */
public ArrayList  getGlobalAccidentals()
{	return  mGlobAcc ;   }


/**
 * Get the sharp/flat count.
 */
public int  getAccidentalCount()
	{
	int  Count = 0 ;
	for ( int i = 0 ; i < 7 ; i++ ) {
		if ( mAccidentals[i] == 9 )   return  0 ;
		Count += mAccidentals[i] ;
	}
	return  Count ;		
}


/**
 * Get the sharps and flats.
 */
public int[]  getAccidentals()
{	return  mAccidentals ;   }


/**
 * Get the forced stem direction (if any).
 * 
 * <p>The bagpipe keys HP and Hp both force stems downwards.
 */
public int  getStemDirection()
{	return  ( mNote.equals("HP")  ||  mNote.equals("Hp") ) ? -1 : 0 ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{
	String  s = mNote ;
	if ( mMode != null )   s += mMode ;
	
	if ( mGlobAcc != null )
		for ( int i = 0 ; i < mGlobAcc.size() ; i++ )
			s += " " + mGlobAcc.get(i).toString() ;
	
	return  s ;
}


/**
 * Parse the key into a more sensible form.
 * 
 * <p>This will throw an exception of the key is invalid.
 */
private void  parseKey()  throws ParserException
{
	
//  First of all, get the HP and Hp keys out of the way

	if  ( mNote.equals("HP" ) )
		mAccidentals = HP_ACCIDENTALS ;
	else if  ( mNote.equals("Hp" ) )
		mAccidentals = Hp_ACCIDENTALS ;
		
//  Parse a normal key sig spec

	else {
		
	//  Determine the index of the key (note that # and b are allowed on ALL keys)
	//  and the mode
	
		int  KeyIndex = getKeyIndex(mNote) ;
		int  ModeIndex = getModeIndex(mMode) ;

	//  Use the Key index and the Mode Index to locate the correct
	//  sharps and flats element.
	//  Return an exception if the combination is invalid (99 found)
			
		int  Index = MODE_MAP[KeyIndex][ModeIndex] ;
		if ( Index != 99 ) {
			mAccidentals = MAJ_ACCIDENTALS[Index] ;
			Index = mAccidentals[0] ;
		}

		if ( Index == 99 )
			throw  new ParserException("Invalid key signature") ;
	}
}


/**
 * Parse the global accidentals into a more sensible form.
 * 
 * <p>This will throw an exception of the key is invalid.
 */
private void  parseGlobalAccidentals()
{
	for ( int i = 0 ; i < 7 ; i++ )   mGlobalAccidentals[i] = 0 ;
	
	if ( mGlobAcc == null )   return ;
	
//  Format each global accidental into a more useful form

	for ( int i = 0 ; i < mGlobAcc.size() ; i++ ) {
		Pitch  p = ( (Pitch) mGlobAcc.get(i) ) ;
		
	//  Determine index of the base note( C=0, D=1, ... B=6 )
	//  and store the correct value
	
		int  Index = Pitch.VALID_BASENOTES.indexOf(p.mBaseNote) ;
		if ( Index > 6 )   Index -= 7 ;
	
		mGlobalAccidentals[Index] = p.mAccidental ;
	}
}


/**
 * Calculate the absolute pitch of a note.
 * 
 * <p>This is done here as most of what we need to do this is local to this class.
 * 
 * <p>Note that we must allow for local accidentals as a different accidental
 * may have been set by a previous note in the bar.
 */
public void  calculateAbsolutePitch( Pitch p )
{
	
//  Determine Index into the base note array and get the base MIDI pitch
	
	int  Index = Pitch.VALID_BASENOTES.indexOf(p.mBaseNote) ;
	
	if ( Index < 0 )   return ;		// Must be a rest

	int  MidiPitch = Pitch.MIDI_PITCH[Index] ;

	if ( Index > 6 )   Index -= 7 ;	
	
//  Adjust for required number of octaves

	int  w = p.mOctave ;
	
	if ( w > 0 ) {
		for ( int i = 0 ; i < w ; i++ )   MidiPitch += 12 ;
	}
	else if ( w < 0 ) {
		for ( int i = 0 ; i < -w ; i++ )   MidiPitch -= 12 ;
	}
	
//  If any accidentals specified then adjust as required and return.
//  We must also store the change as a local accidental so that following
//  notes in the same bar will take notice if no accidentals are specified.

	w = p.mAccidental ;

	if ( w != 0 ) {
		
		if ( w != 9 )   MidiPitch += w ;
		p.mMidiPitch = MidiPitch ;
		
		mLocalAccidentals[Index] = w ;
		
		return ;
	}
	
//  No accidentals check for any local accidentals and return 
	
	w = mLocalAccidentals[Index] ;  
	if ( w != 0 ) {
		if ( w != 9 )   MidiPitch += w ;
		p.mMidiPitch = MidiPitch ;
		return ;
	}
	
//  Still no accidentals check for any global accidentals and return 
	
	w = mGlobalAccidentals[Index] ;  
	if ( w != 0 ) {
		if ( w != 9 )   MidiPitch += w ;
		p.mMidiPitch = MidiPitch ;
		return ;
	}
		
//  Finally check for key signature accidentals

	w = mAccidentals[Index] ;  
	if ( w != 0 ) {
		if ( w != 9 )   MidiPitch += w ;
		p.mMidiPitch = MidiPitch ;
		return ;
	}
	
//  Nothing special, just return the base value

	p.mMidiPitch = MidiPitch ;
}


/**
 * Determine number of semitones to next note up.
 */
public int  nextUpInSemitones( Note Note )
{

//  First of all determine the index (0-7) of the base note so we can look up
//  the sharps and flats array

	int  Index = Note.mPitch.getBaseNoteIndex() ;
	if ( Index > 6 )   Index -= 7 ;

//  Now determine the index of the next note up
	
	int  UpIndex = Index + 1 ; 
	if ( UpIndex > 6 )   UpIndex -= 7 ;
	
//  Now calculate the relevant shift

	int  Shift = SEMITONES_UP[Index] ;
	if ( mAccidentals[Index]   != 9 )   Shift -= mAccidentals[Index] ;
	if ( mAccidentals[UpIndex] != 9 )   Shift += mAccidentals[UpIndex] ;
	
	return  Shift ;
}


/**
 * Determine number of semitones to next note down.
 */
public int  nextDownInSemitones( Note Note )
{

//	First of all determine the index (0-7) of the base note so we can look up
//	the sharps and flats array

	int  Index = Note.mPitch.getBaseNoteIndex() ;
	if ( Index > 6 )   Index -= 7 ;

//	Now determine the index of the next note down
	
	int  DownIndex = Index - 1 ; 
	if ( DownIndex < 0 )   DownIndex += 7 ;
	
	int  Shift = SEMITONES_UP[DownIndex] ;
	if ( mAccidentals[DownIndex] != 9 )   Shift -= mAccidentals[DownIndex] ;
	if ( mAccidentals[Index]     != 9 )   Shift += mAccidentals[Index] ;

	
	return  -Shift ;
}


/**
 * Clear the local accidentals array.
 * 
 * <p>This array is reset at the start of every bar.
 */
public void  resetLocalAccidentals()
{	for ( int i = 0 ; i < 7 ; i++ )   mLocalAccidentals[i] = 0 ;   }


/**
 * Compare one key to another.
 */
public boolean equals( Key k ) 
{	return  toString().equals( k.toString() ) ;   }


/**
 * Set the base note.
 */
public void  setBaseNote( String Note )   throws ParserException
{	mNote = Note ;   parseKey() ;   }


/**
 * Set the mode.
 */
public void  setMode( String Mode )   throws ParserException
{	mMode = Mode ;   parseKey() ;   }


/**
 * Set the global accidentals.
 */
public void  setGlobalAccidentals( ArrayList GlobAcc )
{	mGlobAcc = GlobAcc ;   parseGlobalAccidentals() ;   }


/**
 * Determine the index into the key sig arrays.
 * 
 * <p>Note that this helper method allows for both sharp and flat on each note.
 */
public static int  getKeyIndex( String BaseNote )
{
//  Determine index of the base note( C=0, D=1, ... B=6 )
	
	int  Index = Pitch.VALID_BASENOTES.indexOf( BaseNote.charAt(0) ) ;
	if ( Index > 6 )   Index -= 7 ;
	
//  Determine the adjustment for any accidental

	int Adjust = 0 ;
	if ( BaseNote.length() > 1 ) {
		if ( BaseNote.charAt(1) == '#' )   Adjust = +1 ;
		if ( BaseNote.charAt(1) == 'b' )   Adjust = -1 ;
	}

//  Now calculate the index into the info arrays for this key
//  Reminder - # and b are valid for ALL notes

	return  3 * Index + Adjust + 1 ;
}


/**
 * Get the mode index of this key.
 */
public int  getModeIndex()
{	return  getModeIndex(mMode) ;   }


/**
 * Determine the mode index.
 */
public static int  getModeIndex( String Mode )
{
	int  ModeIndex = 0 ;
	
	if ( Mode == null )   return  0 ;

	for ( ModeIndex = 0 ; ModeIndex < VALID_MODES.length ; ModeIndex++ ) {
		String  TestMode = Mode ;
		if ( TestMode.length() > 3 )
		   TestMode = TestMode.substring( 0, 3 ) ;	
		if ( TestMode.equals( VALID_MODES[ModeIndex] ) )
			break ;	
	}

	if ( ModeIndex == 9 )   ModeIndex = 1 ;		// "m" to "min"

	return  ModeIndex ;
}


/**
 * Save state of this key internally.
 * 
 * <p>This particularly stores the state of local accidentals so it can be restored.
 */
public void  saveState()
{	System.arraycopy( mLocalAccidentals, 0, mSavedLocalAccidentals, 0, 7 ) ;   }


/**
 * Restore the state of this key internally.
 * 
 * <p>This particularly restores the state of local accidentals.
 */
public void  restoreState()
{	System.arraycopy( mSavedLocalAccidentals, 0, mLocalAccidentals, 0, 7 ) ;   }


/**
 * Is this a sharp or flat key sig.
 */
public int  getSharpFlat()
{
	for ( int i = 0 ; i < 8 ; i++ ) {
		if ( mAccidentals[i] > 0 )   return  +1 ;
		if ( mAccidentals[i] < 0 )   return  -1 ;
	}
	return  0 ;
}

}
