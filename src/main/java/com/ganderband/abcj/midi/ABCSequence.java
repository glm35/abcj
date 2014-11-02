/**
 * A sequence generated from the current ABC.
 * 
 * <p>This used the javax.sound.midi package.
 */
package  abcj.midi ;

import  javax.sound.midi.* ;
import  java.util.* ;
import  abcj.model.abc.* ;
import  abcj.util.* ;
import  abcj.* ;

/**
 * @author Steve Spencer-Jowett
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ABCSequence implements ABCParserConstants
{
/**
 * The PPQ (pulses/quarter-note) to use for the sequence.
 */
	private static final int  PPQ = 96 ;
/**
 * Grace note length in MIDI pulses.
 */
	private static final int GRACE_NOTE_TICKS = 5 ;
/*
 * This velocity is used for all notes.
 */
	private static final int  VELOCITY = 64 ;
/*
 * This channel is used for all notes.
 */
	private static final int  CHANNEL0 = 0 ;
/**
 * The Sequence object which contains the MIDI sequence.
 */
	private Sequence  mSequence ;
/**
 * The Midi File format to create.
 */
	private static int  sMidiFileFormat = 1 ;
/**
 * The music track.
 */
	private Track  mMusicTrack ;
/**
 * The tempo map track.
 * 
 * <p>For file format 1 this will refer to the single music track.
 */
	private Track  mMapTrack ;
/**
 * The ABC Header of the tune.
 */
	private ABCHeader  mHeader ;
/**
 * The phrases of the tune.
 */
	private ArrayList  mPhrases ;
/**
 * The current time as a fraction.
 * 
 * <p>This is the time since the start of the tune.
 */
	private Fraction  mCurrentTime = new Fraction(0) ;
/**
 * The current time as MIDI pulses.
 */
	private int  mCurrentTimeMidi = 0 ;
/**
 * Is a slur in progress.
 */
	private boolean  mSlurInProgress = false ;
/**
 * Tied note midi pitch (0 if none).
 * 
 * <p>Chords will not be tied.
 */
	private int  mTiedNoteMidiPitch = 0 ;
/**
 * Are grace notes enabled ?
 */
	private boolean  mGraceNotesEnabled = false ;
/**
 * The roll type to use (0=none).
 */
	private int  mRollType = 0 ;
/**
 * The MIDI program number to use.
 */
	private int  mMidiProgram = 0 ;
/**
 * Thetranspose amount.
 */
	private int  mTransposeAmount = 0 ;


/**
 * The standard constructor.
 */
public  ABCSequence( ABCHeader Header, ArrayList Phrases )
{
	mHeader  = Header ;
	mPhrases = Phrases ;
	
//  Extract the properties which control the sequence build.

	mMidiProgram = ABCJProperties.getPropertyInt("Player.Instrument") - 1 ;
	if ( mMidiProgram < 0 )   mMidiProgram = 0 ;

	mTransposeAmount = ABCJProperties.getPropertyInt("Player.Transpose") ;

	mRollType = ABCJProperties.getPropertyInt("Player.Roll") ;
	
	mGraceNotesEnabled = ( ABCJProperties.getPropertyInt("Player.GraceNotes") != 0 ) ;
	
//  Build a MIDI sequence object to hold the ABC sequence

	try {   mSequence = new Sequence( Sequence.PPQ, PPQ ) ;   }
	catch ( InvalidMidiDataException e ) {
		System.out.println( "Exception ignored - " + e ) ;
		e.printStackTrace() ;
	}		// Ignore as all valid

//	Create one or two tracks within the sequence depending on the file format
	
//	Track objects cannot be created by invoking their constructor
//	directly. Instead, the Sequence object does the job. So we
//	obtain the Track there. This links the Track to the Sequence
//	automatically.


	if ( sMidiFileFormat == 2 ) {
	    mMapTrack = mSequence.createTrack() ;
		mMusicTrack = mSequence.createTrack() ;
	}
	else {
		mMusicTrack = mSequence.createTrack() ;
		mMapTrack = mMusicTrack ;
	}
	
//  Process the header (setup tempo, meter and keysig).
//  Then process the music itself.

	try {
		addProgramChangeEvent( mMidiProgram, 0 ) ;
		processHeader() ;	
		processMusic() ;
	}
	
//  Catch any Midi exceptions

	catch ( Exception e ) {
		System.out.println("*** MIDI Exception ***");
		e.printStackTrace() ;
		System.exit(1) ;
	}
}


/**
 * Get the current Midi File format.
 */
public int  getMidiFileFormat()
{	return  sMidiFileFormat ;   }


/**
 * Get then enclosed MIDI Sequence object.
 */
public Sequence  getMidiSequence()
{	return  mSequence ;   }


/**
 * Setup the initial values from the ABC Header.
 */
private void  processHeader()   throws InvalidMidiDataException
{
	
//  Setup the initial events

	generateMidiTempo( mHeader.getTempo() ) ;
	generateMidiMeter( mHeader.getMeter() ) ;
	generateMidiKey( mHeader.getKeyElement() ) ;
}


/**
 * Process the ABC Music.
 */
private void  processMusic()   throws InvalidMidiDataException
{
	
//  Loop through all the phrases in the music

	for ( int i = 0 ; i < mPhrases.size() ; i++ )
		processPhrase( (Phrase) mPhrases.get(i) ) ;
		
	clearTiedNotes() ;
}


/**
 * Process a single phrase of music.
 */
private void  processPhrase( Phrase p )   throws InvalidMidiDataException
{
	
//  Generate the required number of repeats of the whole thing
	
	for ( int i = 0 ; i < p.getRepeatCount() ; i++ ) {
		
		ABCElement  Start = p.getStartElement() ;
		ABCElement  End   = p.getEndElement() ;
		ABCElement  Rep1  = p.getRepeat1Element() ;
		ABCElement  Rep2  = p.getRepeat2Element() ;
		
	//  Now generate the relevant MIDI for the phrase
		
		if ( Rep1 == null  ||  Rep2 == null )
			generateMidi( Start, End ) ;
		else {
			generateMidi( Start, Rep2 ) ;
			generateMidi( Start, Rep1 ) ;
			generateMidi( Rep2 , End  ) ;
		}
	}
}


/**
 * Generate a continous Midi stream for the given range of elements.
 */
private void  generateMidi( ABCElement From, ABCElement To )
	throws InvalidMidiDataException
{
	
//  Loop through all the elements in the range

	ABCElement  Element = From ;
	
	while ( Element != To ) {
		
		switch ( Element.getType() ) {
			
		//  Process begin and end slur elements
		
		case  BEGINSLUR_ELEMENT :
			mSlurInProgress = true ;
			break ;
			
		case  ENDSLUR_ELEMENT :
			mSlurInProgress = false ;
			break ;
			
		//  Process inline fields
				
		case  METER_FIELD_ELEMENT :
			generateMidiMeter( (MeterFieldElement) Element ) ;
			break ;
					
		case  TEMPO_FIELD_ELEMENT :
			generateMidiTempo( (TempoFieldElement) Element ) ;
			break ;
					
		case  KEY_FIELD_ELEMENT :
			generateMidiKey( (KeyFieldElement) Element ) ;
			break ;

		//  Process note related events
		
		case  REST_ELEMENT :
			generateMidiRest( (RestElement) Element ) ;
			break ;
								
		case  NOTE_ELEMENT :
			generateMidiNote( (NoteElement) Element ) ;
			break ;					
								
		case  TUPLET_ELEMENT :
			generateMidiTuplet( (TupletElement) Element ) ;
			break ;					
								
		case  MULTINOTE_ELEMENT :
			generateMidiMultiNote( (MultiNoteElement) Element ) ;
			break ;					
					
		}

		Element = Element.getNextElement() ;
	}
}


/**
 * Generate a midi event for a meter change.
 */
private void  generateMidiMeter( MeterFieldElement Element )
	throws InvalidMidiDataException 
{
	
//	Build and add the meter meta event

	Fraction  Meter = Element.getMeterAsFraction() ;

	MetaMessage  TimeSigMsg = new MetaMessage() ;
		
	byte[]  TimeSigData = new byte[] { (byte) Meter.getNumerator(),
									   (byte) Meter.getDenominator(), 
									   (byte) PPQ, (byte) 8 } ;		// ???
							
	TimeSigMsg.setMessage( 0x58, TimeSigData, TimeSigData.length ) ;
		
	mMapTrack.add( new MidiEvent( TimeSigMsg, mCurrentTimeMidi ) ) ;
}


/**
 * Generate a midi event for a tempo change.
 */
private void  generateMidiTempo( TempoFieldElement Element )
	throws InvalidMidiDataException 
{
	
//	Build and add the tempo meta event (as microseconds/quarternote)

	int  Mpq = 60000000 / Element.getAbsoluteTempo() ;

	MetaMessage  TempoMsg = new MetaMessage() ;

	byte[]  TempoData = new byte[] { 0x03, (byte) 0xD0, (byte) 0x90 } ;
	TempoData[0] = (byte)   ( Mpq >> 16 ) ;
	TempoData[1] = (byte) ( ( Mpq >>  8 ) & 0xFF ) ;
	TempoData[2] = (byte) (   Mpq         & 0xFF ) ;
		
	TempoMsg.setMessage( 0x51, TempoData, TempoData.length ) ;
		
	mMapTrack.add( new MidiEvent( TempoMsg, mCurrentTimeMidi ) ) ;
}


/**
 * Generate a midi event for a key change change.
 */
private void  generateMidiKey( KeyFieldElement Element )
	throws InvalidMidiDataException 
{
	 Key  Key = Element.getKey() ;
		
	 MetaMessage  KeySigMsg = new MetaMessage() ;

	 byte[]  KeySigData  = new byte[] { 0x00, 0x00 } ;
	 KeySigData[0] = (byte) Key.getAccidentalCount() ;
	 if ( Key.getModeIndex() == 1 )   KeySigData[1] = 1 ;	// minor
		 
	 KeySigMsg.setMessage( 0x59, KeySigData, KeySigData.length ) ;
		
	 mMapTrack.add( new MidiEvent( KeySigMsg, mCurrentTimeMidi ) ) ;
}


/**
 * Generate a midi event for a rest.
 */
private void  generateMidiRest( RestElement Element )
	throws InvalidMidiDataException 
{
	clearTiedNotes() ;		// Terminate any ties
	
//  All we need to do here is update the current time with the length of the rest

	incrementTime( Element.getNote().getAbsoluteLength() ) ;
}


/**
 * Generate a midi event for a note.
 */
private void  generateMidiNote( NoteElement Element )
	throws InvalidMidiDataException 
{
	Note  Note = Element.getNote() ;
	generateMidiGraceNoteEvents( Element.getGraceNotes() ) ;
	generateMidiNoteEvents( Note, Element.getGracings() ) ;
	incrementTime( Note.getAbsoluteLength() ) ;
}


/**
 * Generate midi events for a tuplet.
 */
private void  generateMidiTuplet( TupletElement Element )
	throws InvalidMidiDataException 
{
	ArrayList  Notes = Element.getNoteElements() ;
	
//  Handle each note of the tuplet as an ordinary note element

	for ( int i = 0 ; i < Notes.size() ; i++ ) {
		NoteElement  e = (NoteElement) Notes.get(i) ;
		if ( e.getType() == MULTINOTE_ELEMENT )
			generateMidiMultiNote( (MultiNoteElement) e ) ;
		else
			generateMidiNote(e) ;
	}
}


/**
 * Generate midi events for a multi note.
 */
private void  generateMidiMultiNote( MultiNoteElement Element )
	throws InvalidMidiDataException 
{
	clearTiedNotes() ;
	
//  Generate any gracings first of all

	generateMidiGraceNoteEvents( Element.getGraceNotes() ) ;

//  Now generate events for each note in the chord
	
	ArrayList  Notes = Element.getNotes() ;
	
//	Generate Midi events for each not but do not update the clock

	for ( int i = 0 ; i < Notes.size() ; i++ )
		generateMidiNoteEvents( (Note) Notes.get(i), Element.getGracings() ) ;
		
//  Update the clock with the length of the first note

	incrementTime( ( (Note) Notes.get(0) ).getAbsoluteLength() ) ;		
}


/**
 * Generate all midi events for a note (no clock updating).
 */
private void  generateMidiNoteEvents( Note Note, String Gracings )
	throws InvalidMidiDataException 
{
	Fraction  NoteLength = Note.getAbsoluteLength() ;
	int       MidiPitch  = Note.getPitch().getMidiPitch() ;
	
//  Set flags to indicate any decoration

	boolean  IsStaccato = false ;
	boolean  IsTied     = false ;
	boolean  IsRoll     = false ;
	
	if ( Gracings != null  &&  Gracings.indexOf('.') >= 0 )   IsStaccato = true ;
	if ( Gracings != null  &&  Gracings.indexOf('~') >= 0 )   IsRoll     = true ;
		
	if ( Note.getTie()  != null  &&  Note.getTie().equals("-") )   IsTied = true ;
	
//  If rolls disabled (type 0) then turn off any roll indication
//	If there is a roll on this note then stacatto and tied should be turned
//	off

	if ( mRollType == 0 )   IsRoll = false ;
	if ( IsRoll )           IsStaccato = IsTied = false ;

//	Check if a tied note is in progress.
//	If it not the same as this note then clear the tied note and continue.
//  We should also clear any tied notes if this is a roll.

	if ( mTiedNoteMidiPitch > 0  &&  mTiedNoteMidiPitch != MidiPitch  ||  IsRoll )
		clearTiedNotes() ;
	
//  If a tied note is in progress then it's for the same note.
//  What now that matters is if it ties on any further or not !
//  If the tie continues then just exit.   If not, we need not generate
//  the note on event as it has already been done.

	if ( mTiedNoteMidiPitch > 0  &&  IsTied )   return ;
	
//	Determine the duration

	int  Duration = PPQ * 4 * NoteLength.getNumerator()
				  / NoteLength.getDenominator() ;
	
	if ( mSlurInProgress  ||  IsTied  || IsRoll ) {
		// Leave duration at full length of the note
	}
	else if ( IsStaccato )
		Duration = Duration * 1 / 4 ; 		// .25 of duration
	else
		Duration = Duration * 3 / 4 ; 		// .75 of duration
		
//  If this is a roll then call the generateRoll method and return

	if ( IsRoll ) {
		generateRoll( Note, Duration ) ;
		return ;
	}
	
//	Create the requisite note on event
//  Note that, if a tied not is still in progress at this point then it
//  is not necessary to generate the note on	
	
	if ( mTiedNoteMidiPitch == 0 )
		addNoteOnEvent( MidiPitch, VELOCITY, mCurrentTimeMidi ) ;

//	Create the requisite note off event
//  If this note is tied then do not generate the note off.   Instead,
//  remember the pitch so it can be completed later.

	if ( IsTied ) {
		mTiedNoteMidiPitch = MidiPitch ;		
	}
	else {
		mTiedNoteMidiPitch = 0 ;
		addNoteOffEvent( MidiPitch, mCurrentTimeMidi + Duration ) ;
	}
}


/**
 * Generate all midi events for a grace notes.
 */
private void  generateMidiGraceNoteEvents( ArrayList GraceNotes )
	throws InvalidMidiDataException 
{
	if ( GraceNotes == null  ||  ! mGraceNotesEnabled )   return ;
	
//  Process each grace note in turn.
//  Grace notes are played prior to the following note and should therefore
//  generate earlier time positions

	int  StartTime = mCurrentTimeMidi - GraceNotes.size() * GRACE_NOTE_TICKS ;

	for ( int i = 0 ; i < GraceNotes.size() ; i++ ) {
		int  p = ( (Pitch) GraceNotes.get(i) ).getMidiPitch() ;
		
	//  Only add grace notes within the tune

		if ( StartTime >= 0 ) {
			addNoteOnEvent( p, VELOCITY, StartTime ) ;
			StartTime += GRACE_NOTE_TICKS ;
			addNoteOffEvent( p, StartTime + GRACE_NOTE_TICKS ) ;
		}
	}
}


/**
 * Generate relevant events for a roll.
 */
public void  generateRoll( Note Note, int Duration )   throws InvalidMidiDataException
{
	if ( mRollType == 1 )   generateRollType1( Note, Duration ) ;
}



/**
 * Generate relevant events for a roll type 1.
 *
 * <p>This will split the note into 5 sub-notes of equal length where the pitches
 * of the notes are as follows :-
 *
 *		Note 1  -  this note pitch 
 *		Note 2  -  next up in scale 
 *		Note 3  -  this note pitch 
 *		Note 4  -  next down in scale 
 *		Note 5  -  this note pitch 
 */
public void  generateRollType1( Note Note, int Duration )
	throws InvalidMidiDataException
{
	
//  Determine the base pitch and the adjustments up or down from the key.
//  Accidentals are ignored when doing this calculation.

	int  MidiPitch  = Note.getPitch().getMidiPitch() ;
	Key  Key        = Note.getKey() ;
	
	int  UpPitch   = MidiPitch + Key.nextUpInSemitones(Note) ;
	int  DownPitch = MidiPitch + Key.nextDownInSemitones(Note) ;

//  Note 1
		
	addNoteOnEvent(  MidiPitch, VELOCITY, mCurrentTimeMidi ) ;
	addNoteOffEvent( MidiPitch,           mCurrentTimeMidi + Duration * 1 / 5 ) ;
		
//  Note 2
		
	addNoteOnEvent(  UpPitch, VELOCITY, mCurrentTimeMidi + Duration * 1 / 5 ) ;
	addNoteOffEvent( UpPitch,           mCurrentTimeMidi + Duration * 2 / 5 ) ;
		
//  Note 3
		
	addNoteOnEvent(  MidiPitch, VELOCITY, mCurrentTimeMidi + Duration * 2 / 5 ) ;
	addNoteOffEvent( MidiPitch,           mCurrentTimeMidi + Duration * 3 / 5 ) ;
		
//  Note 4
		
	addNoteOnEvent(  DownPitch, VELOCITY, mCurrentTimeMidi + Duration * 3 / 5 ) ;
	addNoteOffEvent( DownPitch,           mCurrentTimeMidi + Duration * 4 / 5 ) ;
		
//  Note 5
		
	addNoteOnEvent(  MidiPitch, VELOCITY, mCurrentTimeMidi + Duration * 4 / 5 ) ;
	addNoteOffEvent( MidiPitch,           mCurrentTimeMidi + Duration ) ;
}

/**
 * Clear any tied notes.
 */
private void  clearTiedNotes()   throws InvalidMidiDataException 
{
	
//	If any tie in progress then clear it by generating the right note off

	if ( mTiedNoteMidiPitch > 0 )
		addNoteOffEvent( mTiedNoteMidiPitch, mCurrentTimeMidi ) ;
	mTiedNoteMidiPitch = 0 ;
}


/**
 * A helper method to increment the time with the given length.
 */
private void  incrementTime( Fraction f )
{
	mCurrentTime = mCurrentTime.plus(f) ;
	mCurrentTimeMidi = PPQ * 4 * mCurrentTime.getNumerator()
							   / mCurrentTime.getDenominator() ; 
}


/**
 * Create a general MIDI note event at the given time.
 */

private void  addNoteEvent( int Command, int Note, int Velocity, long Time )
	throws InvalidMidiDataException 
{
	
//	Create the MIDI message for the event

	Note += mTransposeAmount ;

	ShortMessage  Msg = new ShortMessage() ;
	Msg.setMessage( Command, CHANNEL0, Note, Velocity ) ;
	
//	Now build the event by adding the message and the timer tick 

	mMusicTrack.add( new MidiEvent( Msg, Time ) ) ;
}


/**
 * Create a note on event at the given time.
 */
private void  addNoteOnEvent( int Note, int Velocity, long Time )
	throws InvalidMidiDataException 
{	addNoteEvent( ShortMessage.NOTE_ON, Note, Velocity, Time ) ;   }


/**
 * Create a note off event at the given time.
 */
private void  addNoteOffEvent( int Note, long Time )
	throws InvalidMidiDataException 
{	addNoteEvent( ShortMessage.NOTE_OFF, Note, 0, Time ) ;   }


/**
 * Create a program change event at the given time.
 */
private void  addProgramChangeEvent( int  Program, long Time )
	throws InvalidMidiDataException 
{

//	Create the MIDI message for the event

	ShortMessage  Msg = new ShortMessage() ;
	Msg.setMessage( ShortMessage.PROGRAM_CHANGE, CHANNEL0, Program, 0 ) ;
	
//	Now build the event by adding the message and the timer tick 

	mMusicTrack.add( new MidiEvent( Msg, Time ) ) ;
}


/**
 * Transpose the sequence.
 */
public void  transpose( int Transpose )   throws InvalidMidiDataException
{
	
//  Process all events in the music track
	
	for ( int i = 0 ; i < mMusicTrack.size() ; i++ ) {
		MidiMessage  m = mMusicTrack.get(i).getMessage() ;
		
	//   Only process Note On and Note off messages
		
		if ( ! ( m instanceof ShortMessage ) )   continue ;
		
		ShortMessage  sm = (ShortMessage) m ;
		int  Status = sm.getStatus() ;
		int  Data1  = sm.getData1() ;
		int  Data2  = sm.getData2() ;
		
		if ( Status == ShortMessage.NOTE_ON  ||  Status == ShortMessage.NOTE_OFF ) {
			
		//  Tranpose the note event

			sm.setMessage( Status, Data1 + Transpose, Data2 ) ;
		}
	}
	
	
}

}