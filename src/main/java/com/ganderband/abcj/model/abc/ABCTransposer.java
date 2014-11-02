/**
 * A class representing an ABC Transposer object.
 * 
 * <p>This class is responsible for transposing for transposing ABC from one key to another.
 */
package  abcj.model.abc ;

import  java.util.* ;

public class ABCTransposer implements ABCParserConstants
{
/**
 * The amount to transpose.
 */
	private int  mTransposeAmount ;
/**
 * The adjustment amount for ledger lines.
 */
	private int  mLedgerAdjust ;
/**
 * The current key in force.
 */
	private Key  mCurrentKey ;
/**
 * The following table defined how to map key signatures on transposition.
 * 
 * <p>It is based on the major scale only.   Other modes are managed by applying
 * the appropriate offset.
 * 
 * <p>The first 2 digits of the number are the index of the new major key sig.
 * <p>The last two digits are the number of lines to move up the stave.
 */	
	private static final int[][] SIG_TRANSPOSE_TABLE1 = {
		
 	//        0     +1     +2     +3     +4     +5     +6     +7     +8     +9    +10    +11
		
//		{ Cb+00,  C+00, Db+01,  D+01, Eb+02,  E+02,  F+03, F#+03,  G+04, Ab+05,  A+05, Bb+06 } ,	// 0  = Cb maj		OK
		{     0,   100,   301,   401,   602,   702,  1003,  1103,  1304,  1505,  1605,  1806 } ,
//		{  C+00, Db+01,  D+01, Eb+02,  E+02,  F+03, F#+03,  G+04, Ab+05,  A+05, Bb+06,  B+06 } ,	// 1  = C maj		OK
		{   100,   301,   401,   602,   702,  1003,  1103,  1304,  1505,  1605,  1806,  1906 } ,
//		{ C#+00,  D+01, Eb+02,  E+02,  F+03, F#+03,  G+04, Ab+05,  A+05, Bb+06,  B+06,  C+07 } ,	// 2  = C# maj		OK
		{   200,   401,   602,   702,  1003,  1103,  1304,  1505,  1605,  1806,  1906,   107 } ,
		
//		{ Db+00,  D+00, Eb+01,  E+01,  F+02, F#+02,  G+03, Ab+04,  A+04, Bb+05,  B+05,  C+06 } ,	// 3  = Db maj		OK
		{   300,   400,   601,   701,  1002,  1102,  1303,  1504,  1604,  1805,  1905,   106 } ,
//		{  D+00, Eb+01,  E+01,  F+02, F#+02,  G+03, Ab+04,  A+04, Bb+05,  B+05,  C+06, Db+07 } ,	// 4  = D maj		OK
		{   400,   601,   701,  1002,  1102,  1303,  1504,  1604,  1805,  1905,   106,   307 } ,
//		{ D#+00,  E+01,  F+02, F#+02,  G+03, Ab+04,  A+04, Bb+05,  B+05,  C+06, Dd+07,  D+07 } ,	// 6  = D# maj		OK
		{   500,   701,  1002,  1102,  1303,  1504,  1604,  1805,  1905,   106,   307,   407 } ,
		
//		{ Eb+00,  E+00,  F+01, F#+01,  G+02, Ab+03,  A+03, Bb+04,  B+04,  C+05, Dd+06,  D+06 } ,	// 6  = Eb maj		OK
		{   600,   700,  1001,  1101,  1302,  1503,  1603,  1804,  1904,   105,   306,   406 } ,
//		{  E+00,  F+01, F#+01,  G+02, Ab+03,  A+03, Bb+04,  B+04,  C+05, Dd+06,  D+06, Eb+07 } ,	// 7  = E maj		OK
		{   700,  1001,  1101,  1302,  1503,  1603,  1804,  1904,   105,   306,   406,   607 } ,
//		{                                      INVALID                                       } ,	// 8  = E# maj   invalid
		{  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999 } ,

//		{                                      INVALID                                       } ,	// 9  = Fb maj   invalid
		{  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999 } ,
//		{  F+00, F#+00,  G+01, Ab+02,  A+02, Bb+03,  B+03,  C+04, Dd+05,  D+05, Eb+06,  E+06 } ,	// 10 = F maj		OK
		{  1000,  1100,  1301,  1502,  1602,  1803,  1903,   104,   305,   405,   606,   706 } ,
//		{  F#+00, G+01, Ab+02,  A+02, Bb+03,  B+03,  C+04, Dd+05,  D+05, Eb+06,  E+06,  F+07 } ,	// 11 = F# maj		OK
		{  1100,  1301,  1502,  1602,  1803,  1903,   104,   305,   405,   606,   706,  1007 } ,

//		{ Gb+00,  G+00, Ab+01,  A+01, Bb+02,  B+02,  C+03, Dd+04,  D+04, Eb+05,  E+05,  F+06 } ,	// 12 = Gb maj		OK
		{  1200,  1300,  1501,  1601,  1802,  1902,   103,   304,   405,   605,   705,  1006 } ,
//		{  G+00, Ab+01,  A+01, Bb+02,  B+02,  C+03, Dd+04,  D+04, Eb+05,  E+05,  F+06, F#+06 } ,	// 13 = G maj		OK
		{  1300,  1501,  1601,  1802,  1902,   103,   304,   404,   605 ,  705,  1006,  1106 } ,
//		{ G#+00,  A+01, Bb+02,  B+02,  C+03, Dd+04,  D+04, Eb+05,  E+05,  F+06, F#+06,  G+07 } ,	// 14 = G# maj		OK
		{  1400,  1601,  1802,  1902,   103,   304,   404,   605,   705,  1006,  1106,  1307 } ,
		
//		{ Ab+00,  A+00, Bb+01,  B+01,  C+02, Dd+03,  D+03, Eb+04,  E+04,  F+05, F#+05,  G+06 } ,	// 15 = Ab maj		OK
		{  1500,  1600,  1801,  1901,   102,   303,   403,   604,   704,  1005,  1105,  1306 } ,
//		{  A+00, Bb+01,  B+01,  C+02, Dd+03,  D+03, Eb+04,  E+04,  F+05, F#+05,  G+06, Ab+07 } ,	// 16 = A maj		OK
		{  1600,  1801,  1901,   102,   303,   403,   604,   704,  1005,  1105,  1306,  1507 } ,
//		{                                      INVALID                                       } ,	// 17 = A# maj   invalid
		{  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999 } ,
	
//		{ Bb+00,  B+00,  C+01, Dd+02,  D+02, Eb+03,  E+03,  F+04, F#+04,  G+05, Ab+06,  A+06 } ,	// 18 = Bb maj		OK
		{  1800,  1900,   101,   302,   402,   603,   703,  1004,  1104,  1305,  1506,  1606 } ,
//		{  B+00,  C+01, Dd+02,  D+02, Eb+03,  E+03,  F+04, F#+04,  G+05, Ab+06,  A+06, Bb+07 } ,	// 19 = B maj		OK
		{  1900,   101,   302,   402,   603 ,  703,  1004,  1104,  1305,  1506,  1606,  1807 } ,
//		{                                     INVALID                                        } ,	// 20 = B# maj   invalid
		{  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999,  9999 } ,
	} ;
/**
 * The hashtable used to convert invalid key sigs to valid key sigs.
 * 
 * <p>This essentially provides a map which switches sharps and flats.
 */
	private static Hashtable  sKeyMapTable = new Hashtable() ;
/**
 * Populate the hashtable statically.
 */	
	static {
		sKeyMapTable.put( "Db", "C#") ;		// min, phr, aoe, loc
		sKeyMapTable.put( "Ab", "G#") ;		// phr, loc
		sKeyMapTable.put( "Eb", "D#") ;		// loc
	}

/**
 * Construct a transposer for a specific transpose amount.
 */
public  ABCTransposer( int Transpose )
{	mTransposeAmount = Transpose ;   }


/**
 * Transpose an ABC header.
 */
public boolean  transpose( ABCHeader Hdr )
{	return  transpose( Hdr.getKeyElement() ) ;   }


/**
 * Transpose a key field element.
 */
public boolean  transpose( KeyFieldElement Element )
{
	
//  Locate the key signature element, key and base note
	
	Key  Key = Element.getKey() ;
	
//  Ignore the piping keys
	
	if ( Key.getBaseNote().equals("HP")  ||  Key.getBaseNote().equals("Hp") )
		return  false ;
	
	return  transpose(Key) ;
}


/**
 * Transpose ABC music.
 */
public boolean  transpose( ABCMusic Music )
{

//  Loop through all elements in the music
	
	ABCElement  e = Music.getRootElement() ;
	
	while ( e != null ) {
		
		int  Type = e.getType() ;
		
	//  Clear key local accidentals at bar-lines
		
		if ( Type == BARLINE_ELEMENT )
			mCurrentKey.resetLocalAccidentals() ;
		
	//  Select out elements to transpose
		
		else if ( Type == MULTINOTE_ELEMENT )					// chords of notes
			transpose( (MultiNoteElement) e ) ;
		else if ( Type == TUPLET_ELEMENT )						// tuplets
			transpose( (TupletElement) e ) ;
		else if ( Type == NOTE_ELEMENT )						// ordinary note
			transpose( (NoteElement) e ) ;
		else if ( Type == KEY_FIELD_ELEMENT )					// in-line key field
			transpose( (KeyFieldElement) e ) ;
		
	//  Get next element in the music	
		
		e = e.getNextElement() ;
	}
	
	return  true ;
}


/**
 * Transpose a multi-note element.
 */
public boolean  transpose( MultiNoteElement Element )
{
	transposeGuitarChord(Element) ;
	transposeNotes( Element.getGraceNotes() ) ;
	transposeNotes( Element.getNotes() ) ;
	
	return  true ;
}


/**
 * Transpose a tuplet element.
 */
public boolean  transpose( TupletElement Element )
{
	ArrayList  Elements = Element.getNoteElements() ;
	
	if ( Elements == null )   return  true ;
	
	for ( int i = 0 ; i < Elements.size() ; i++ )
		transpose( (NoteElement) Elements.get(i) ) ;

	return  true ;
}


/**
 * Transpose an ordinary note element.
 */
public boolean  transpose( NoteElement Element )
{
	transposeGuitarChord(Element) ;
	transposeNotes( Element.getGraceNotes() ) ;
	transposeNote( Element.getNote() ) ;
	return  true ;
}


/**
 * Transpose notes.
 */
public void  transposeNotes( ArrayList Notes )
{
	if ( Notes == null )   return ;

	System.out.println("    notes "+Notes) ;
	
	for ( int i = 0 ; i < Notes.size() ; i++ )
		transposeNote( (Note) Notes.get(i) ) ;
}


/**
 * Transpose an ABC key.
 */
public boolean  transpose( Key KeyInfo )
{
	mCurrentKey = KeyInfo ;
	
//  For a key sig transpose, convert to an upwards < 1 octave transpose
	
	int  w = mTransposeAmount ;
	mLedgerAdjust = 0 ;
	while ( w < 0 ) {
		w += 12 ;
		mLedgerAdjust -= 7 ;
	}
	while ( w >= 12 ) {
		w -= 12 ;
		mLedgerAdjust += 7 ;
	}
	
// If octaves then leave it alone and return
	
	if ( w == 0 )   return  true ;
	
//  Locate the base note and its key index
	
	String	BaseNote = KeyInfo.getBaseNote() ;
	int		KeyIndex = Key.getKeyIndex(BaseNote) ;
	
//  Use the transpose table to get the index of the target key
	
	int  TargetIndex = SIG_TRANSPOSE_TABLE1[KeyIndex][w] / 100 ;
	
//  And get the target key base note

	String  TargetBase = Key.VALID_KEY_NAMES[TargetIndex] ;
	
//  Apply the base note.
//  Note that we may have generated an invalid key signature of we are using any
//  mode other than maj(ion).   Rather than having separate transpose tables for each mode
//  it is easier just to convert the invalid key sig e.g. Dbmin to a valid one e.f. C#min
//  by switching the sharps and flats.

	boolean  Failed = false ;

	try {   KeyInfo.setBaseNote(TargetBase) ;   }
	catch ( Exception x ) {   Failed = true ;   }
	
	if ( Failed ) {
		TargetBase = (String) sKeyMapTable.get(TargetBase) ;
		
		try {  
			KeyInfo.setBaseNote(TargetBase) ;
			mLedgerAdjust-- ;		// As we have converted ~ to b
		}
		catch ( Exception x ) {   
			System.out.println("Transpose Map Table error !!! "+x) ;
			return  false ;
		}
	}
	
//  Determine the number of ledger lines to adjust by
	
	mLedgerAdjust += SIG_TRANSPOSE_TABLE1[KeyIndex][w] % 100 ;

	return  true ;
}


/**
 * Transpose a guitar chord.
 */
public void  transposeGuitarChord( NoteElement Element )
{
	String  Chord = Element.getGuitarChord() ;
	if ( Chord == null )   return ;

// Parse out the basic note

	int  Index = Pitch.VALID_BASENOTES.indexOf( Chord.charAt(0) ) ;
	if ( Index < 0 )   return ;
	
	int  Pos = 1 ;
	if ( Chord.length() > 1  &&  ( Chord.charAt(1) == '#'  ||  Chord.charAt(1) == 'b' ) )   Pos = 2 ;
	
	String  BaseNote = Chord.substring( 0, Pos ) ;
	String  Suffix   = Chord.substring(Pos) ;

//  Determine the new key by using the transpose table

	int  w = mTransposeAmount ;
	while ( w < 0 )   w += 12 ;
	while ( w >= 12 )   w -= 12 ;
	
	int		KeyIndex = Key.getKeyIndex(BaseNote) ;
	
	int  TargetIndex = SIG_TRANSPOSE_TABLE1[KeyIndex][w] / 100 ;
	
//  Check for sharps or flats and convert the chord so it uses the appropriate accidental
	
	if ( ( TargetIndex % 3 == 0 )  &&  mCurrentKey.getSharpFlat() > 0 ) {
		TargetIndex-- ;
		if ( TargetIndex < 0 )   TargetIndex += 21 ;
	}
	
	if ( ( TargetIndex % 3 == 2 )  &&  mCurrentKey.getSharpFlat() < 0 ) {
		TargetIndex++ ;
		if ( TargetIndex > 20 )   TargetIndex -= 21 ;
	}
	
//  Finally store the new chord
	
	String  TargetBase = Key.VALID_KEY_NAMES[TargetIndex] ;
	
	Element.setGuitarChord( TargetBase + Suffix ) ;
}


/**
 * Transpose note.
 */
public void  transposeNote( Note Note )
{
	
//  Transpose the base note up or down the relevant number of ledger lines (allowing for octave indications)
	
	Pitch  p = Note.getPitch() ;
	
	if ( p.getBaseNote() == 'z' )   return ;		// Ignore rests
	
	int  Index  = p.getBaseNoteIndex() + mLedgerAdjust ;
	int  Octave = p.getOctave() ;
	
	while ( Index < 0 ) {
		Index += 7 ;
		Octave-- ;
	}
	
	while ( Index > 13 ) {
		Index -= 7 ;
		Octave++ ;
	}
	
//  Make sure we do not use any unnecessary octave indications and store the new values
	
	if ( Octave > 0  &&  Index < 7 ) {
		Octave-- ;
		Index += 7 ;
	}

	if ( Octave < 0  &&  Index > 6 ) {
		Octave++ ;
		Index -= 7 ;
	}

	p.setBaseNote( Pitch.VALID_BASENOTES.charAt(Index) ) ;
	p.setOctave(Octave) ;
	
//  If no accidentals then just recalculate the correct MIDI pitch and we are done
	
	if ( p.getAccidental() == 0 ) {
		mCurrentKey.calculateAbsolutePitch(p) ;
		return ;
	}
	
//  Now adjust any accidentals by determining the exact note and trying all until it matches !
	
	int  RequiredMidiPitch = p.getMidiPitch() + mTransposeAmount ;
	
	mCurrentKey.saveState() ;	// So we use the correct local accidentals
	
//	mCurrentKey.restoreState() ;
	p.setAccidental(+1) ;						//  Try #
	mCurrentKey.calculateAbsolutePitch(p) ;
	if ( p.getMidiPitch() == RequiredMidiPitch )   return ;
	
	mCurrentKey.restoreState() ;
	p.setAccidental(-1) ;						//  Try b
	mCurrentKey.calculateAbsolutePitch(p) ;
	if ( p.getMidiPitch() == RequiredMidiPitch )   return ;
	
	mCurrentKey.restoreState() ;
	p.setAccidental(9) ;						//  Try natural
	mCurrentKey.calculateAbsolutePitch(p) ;
	if ( p.getMidiPitch() == RequiredMidiPitch )   return ;
	
	mCurrentKey.restoreState() ;
	p.setAccidental(+2) ;						//  Try ##
	mCurrentKey.calculateAbsolutePitch(p) ;
	if ( p.getMidiPitch() == RequiredMidiPitch )   return ;
	
	mCurrentKey.restoreState() ;
	p.setAccidental(-2) ;						//  Try bb
	mCurrentKey.calculateAbsolutePitch(p) ;
	if ( p.getMidiPitch() == RequiredMidiPitch )   return ;
	
	System.out.println("******** HELP - FAILED TO TRANSPOSE A NOTE ********") ;	// Shouldn't get here
}

}
