/**
 * This class provides a parser for an ABC tune.
 *
 * <p>This class does not use any external tools for parsing (such as JavaCC) as
 * the ABC grammar is ill-formed.   I took a pragmatic approach here and decided
 * to parse directly instead of going through the relevant contortions to get an
 * external tool to do the necessary !
 */
 
package  com.ganderband.abcj.model.abc ;

import  java.io.* ;
import  java.util.* ;
import  com.ganderband.abcj.util.* ;

public class ABCParser implements ABCParserConstants
{
/**
 * Parser status - currently parsing.
 */
	public static final int  IS_PARSING = 0 ;
/**
 * Parser status - not parsing - ABC OK.
 */
	public static final int  ABC_OK = 1 ;
/**
 * Parser status - not parsing - ABC warnings found.
 */
	public static final int  ABC_WARNINGS = 2 ;
/**
 * Parser status - not parsing - ABC errors found.
 */
	public static final int  ABC_ERRORS = 3 ;
/**
 * Parser status - not parsing - no results (nothing to parse).
 */
	public static final int  NO_RESULTS = 4 ;
/**
 * Debug yes or no.
 */
	static private boolean  sDebug = false ;
//	static private boolean  sDebug = true ;
/**
 * Amount of indenting when debugging.
 */
	private static String  sDebugIndent = "" ;
/**
 * A flag to indicate if we are parsing the header or not.
 * 
 * <p>If not, then we are parsing music.  
 */
	private boolean  mParsingHeader ;
/**
 * The current status of the parser.
 */
	private int  mParserState = NO_RESULTS ;
/**
 * The current line of text being parsed.
 * 
 * <p>This is stored as a member variable to remove the need to pass it
 * around and hence improve the parsing speed.
 */	
	private char[]  mCurrLine ;
/**
 * The current parsing position in this line.
 */
	private int  mCurrPos ;
/**
 * The current length of this line.
 */
	private int  mCurrLen ;
/**
 * A flag to indicate we are parsing history and should be looking for
 * a new field.
 */
	private boolean  mParsingHistory ;
/**
 * The ABC Header.
 */	
	private ABCHeader  mABCHeader ;
/**
 * The ABC Tune.
 */	
	private ABCMusic  mABCMusic ;
/**
 * The current ABC line element being constructed.
 */
	private MusicLineElement  mABCLine ;
/**
 * The error flag to indicate status of ABC during parsing.
 */
	private int  mErrorFlag ;
/**
 * A flag indicating that any parsing is to stop now.
 * 
 * <p>This can be used from another thread to terminate the current parse
 * as it may be invalid - it saves some time !
 */
	private boolean  mIsStopRequested = false ;
/**
 * The last music element created.
 * 
 * <p>This is used to build a linked list of all the music items in the tune
 * and will continue across all lines parsed.   It used so that the music can be
 * accessed as a continuous list and essentially flattens the tree to make it
 * easier to manage in certain cases (except for tuplets !!).
 */
	private  ABCElement  mLastElement = null ;
/**
 * An array containing the parsing log.
 * 
 * <p>The array consists of strings. 
 */
	private ArrayList  mLog = new ArrayList() ;
/**
 * The current meter in force.
 */
	private Fraction  mCurrentMeter ;
/**
 * The current default length in force.
 */
	private Fraction  mCurrentDefLength ;
/**
 * The current key in force.
 */
	private Key  mCurrentKey ;
/**
 * The current tempo in force.
 */
	private TempoFieldElement  mCurrentTempo ;
/**
 * The last broken rhythm indication used.
 */
	private int  mPreviousBrokenRhythm ;
/**
 * The first element of a phrase.
 */
	private ABCElement  mPhraseStart ;
/**
 * A phrase has been properly started (and contains notes).
 */
	private boolean  mPhraseStarted ;
/**
 * The first repeat element of a phrase.
 */
	private NthRepeatElement  mPhraseRepeat1 ;
/**
 * The second repeat element of a phrase.
 */
	private NthRepeatElement  mPhraseRepeat2 ;
/**
 * The array list of phrases within the tune.
 */
	private ArrayList  mPhrases ;
	
	
/**
 * A private helper to convert the current line to a string.
 */
private String  lineToString()
{	return  new String( mCurrLine, mCurrPos, mCurrLen - mCurrPos ) ;   }


/**
 * A private helper to convert the current line to a string.
 */
private String  lineToString( int Pos )
{	return  new String( mCurrLine, Pos, mCurrLen - Pos ) ;   }


/**
 * A private helper to convert the current line to a string.
 */
private String  lineToString( int Pos, int Length )
{	return  new String( mCurrLine, Pos, Length ) ;   }


/**
 * A private helper to locate whitespace in the current line.
 */
private int  locateWhitespace()
{
	for ( int i = mCurrPos ; i < mCurrLen ; i++ ) {
		if ( mCurrLine[i] == ' '  ||  mCurrLine[i] == '\t' )
			return  i ;
	}
	return  -1 ;
}


/**
 * A private helper method to skip leading whitespace in the current line.
 * 
 * <p>In this case, whitespace is blank or tab
 */
private void  skipLeadingWhitespace()
{
	for ( ; mCurrPos < mCurrLen ; mCurrPos++ )
		if ( mCurrLine[mCurrPos] != ' '  &&  mCurrLine[mCurrPos] != '\t' )
			break ;
}


/**
 * A private helper method to skip trailing whitespace in the current line.
 * 
 * <p>In this case, whitespace is blank or tab
 */
private void  skipTrailingWhitespace()
{
	for ( ; mCurrLen > mCurrPos ; mCurrLen-- )
		if ( mCurrLine[mCurrLen - 1] != ' '  &&  mCurrLine[mCurrLen - 1] != '\t' )
			break ;
}


/**
 * A private helper method to trim leading and trailing whitespace
 * in the current line.
 * 
 * <p>In this case, whitespace is blank or tab
 */
private void  trim()
{
	skipLeadingWhitespace() ;
	skipTrailingWhitespace() ;
}


/**
 * A private helper to search the current line for a value.
 */
public int  indexOf( char Target )
{
	for ( int i = mCurrPos ; i < mCurrLen ; i++ )
		if ( mCurrLine[i] == Target )
			return  i ;
	
	return  -1 ;
}


/**
 * A private helper to search the current line for a value from the end.
 */
public int  lastIndexOf( char Target )
{
	for ( int i = mCurrLen - 1 ; i >= mCurrPos ; i-- )
		if ( mCurrLine[i] == Target )
			return  i ;
	
	return  -1 ;
}


/**
 * A private helper to compare the current line to a string.
 */
public boolean  compareTo( String Text )
{
	if ( mCurrLen - mCurrPos != Text.length() )   return  false ;
	for ( int i = mCurrPos ; i < mCurrLen ; i++ ) {
		if ( mCurrLine[i] != Text.charAt(i - mCurrPos) )
			return  false ;
	}
	
	return  true ;
}


/**
 * A private helper to compare the current line to a string (with a length).
 */
public boolean  compareTo( String Text, int Length )
{
	if ( mCurrLen - mCurrPos < Length )   return  false ;
	for ( int i = 0 ; i < Length ; i++ ) {
		if ( mCurrLine[mCurrPos + i] != Text.charAt(i) )
			return  false ;
	}
	
	return  true ;
}


/**
 * A private helper to compare the current line to a string (with a length)
 * ignoring case.
 * 
 */
public boolean  compareToNoCase( String Text, int Length )
{
	if ( mCurrLen - mCurrPos < Length )   return  false ;
	for ( int i = 0 ; i < Length ; i++ ) {
		if ( Character.toLowerCase( mCurrLine[mCurrPos + i] ) !=
									Character.toLowerCase( Text.charAt(i) ) )
			return  false ;
	}
	
	return  true ;
}


/**
 * A private helper method to handle linking of elements.
 */
private void  link( ABCElement To )
{
	if ( mLastElement == null )
		mABCMusic.mRootElement = To ;
	else
		mLastElement.setNextElement(To) ;
	mLastElement = To ;
}

/**
 * Debug to the console. 
 */
public static void  debug( String Text )
{
	if ( ! sDebug )   return ;
	System.out.println( sDebugIndent + Text ) ;
}


/**
 * Debug method entry to the console. 
 */
public static void  debugEntry( String Text )
{
	if ( ! sDebug )   return ;
	System.out.println( sDebugIndent + "Enter Method : " + Text ) ;
	sDebugIndent += DEBUG_INDENT ;
}


/**
 * Debug method exit to the console. 
 */
public static void  debugExit( String Text )
{
	if ( ! sDebug )   return ;
	
	sDebugIndent = sDebugIndent.substring( 0,
							 sDebugIndent.length() - DEBUG_INDENT.length() ) ;
	System.out.println( sDebugIndent + "Exit Method : " + Text ) ;
}

	
/**
 * Set the debug flag.
 */
public static void  setDebug( boolean Flag )
{	sDebug = Flag ;   }


/**
 * Get debug state.
 */
public static boolean  isDebugEnabled()
{	return  sDebug ;   }


/**
 * Get parser state.
 */
public int  getStatus()
{	return  mParserState ;   }


/**
 * Get the log of the parsing process.
 * 
 * <p>This is an array of strings for display if necessary.
 */

public  ArrayList  getLog()
{	return  mLog ;   }

/**
 * Get the ABC Header tree.
 */
public ABCHeader  getABCHeader()
{	return  mABCHeader ;   }

/**
 * Get the ABC Music tree.
 */
public ABCMusic  getABCMusic()
{	return  mABCMusic ;   }


/**
 * Get current line being parsed.
 */
public String  getCurrentLine()
{	return  new String(mCurrLine) ;   }


/**
 * Get the current list of phrases.
 */
public ArrayList  getPhrases()
{	return  mPhrases ;   }


/**
 * Wait for valid non-empty result.
 */
public void  waitForResult()
{
	while ( getStatus() == NO_RESULTS  ||  getStatus() == IS_PARSING )
		Utils.sleep(50) ;
}


/**
 * Clear any results and set to empty.
 */
public void  clear()
{
	mIsStopRequested = true ;
	
//  We must wait until the parser is idling before doing this as
//  it may come from another thread and upset the process if we reset
//  the variables whilst its running !

	while ( mParserState == IS_PARSING )   Utils.sleep(50) ;

	mABCHeader   = null ;
	mABCMusic    = null ;
	mParserState = NO_RESULTS ;
	
	mIsStopRequested = false ;
}

 
/**
 * Parse the given string.
 */
public void  parse( String Text )
{
	debugEntry("parse") ;
	
//	System.out.println("Start Parse "+Text);
	
//	long  StartTime = System.currentTimeMillis() ;
	
	mParserState    = IS_PARSING ;
	mParsingHeader  = true ;
	mParsingHistory = false ;
	mABCHeader      = new ABCHeader() ;		// Create new in case current 
	mABCMusic       = new ABCMusic() ;		// ones are in use !
	mLastElement    = null ;
	mLog.clear() ;
	
	mErrorFlag = ABC_OK ;
		
	try {

	//  Open the array as an input stream and parse each line separately

		InputStream  Stream = new ByteArrayInputStream( Text.getBytes() ) ;
		BufferedReader  Rdr = new BufferedReader( new InputStreamReader(Stream) ) ;
		
		String  Line ;
		while ( true ) {
			Line = Rdr.readLine() ;
			if ( Line == null )   break ;
			
		//  Check for stop being requested.  Terminate if so
		
			if ( mIsStopRequested ) {
				mErrorFlag = NO_RESULTS ;
				break ;
			}
			
		//  Now parse an individual line and check for errors
		
			try {
				parseLine(Line) ;
				mLog.add( "I" + Line ) ;		// I = info as line is OK
			}
			
		//  If parse error then add error to the log
		
			catch ( ParserException e ) {
				mErrorFlag = ABC_ERRORS ;
				
				String  s = "" ;
				while ( s.length() < mCurrPos )   s += " " ;
				s += "^" ;
				
				mLog.add( "E" + Line ) ;		// I = info as line is OK
				mLog.add( "E" + s ) ;
				mLog.add( "E" + "*** ERROR *** - " + e.getMessage()
							  + " in line above." ) ;
			}
		}
		
		Rdr.close() ;
	}
	
//  Check for other problems

	catch ( Exception e ) {
		calculate() ;		//  Calculat real lengths and locate repeats
		
		mParserState = ABC_ERRORS ;
		mLog.add( "E" + "*** Parser failed - Check Java console") ;
		System.out.println("*** Parser Failed ***") ;
		e.printStackTrace(System.out) ;
		debugExit("parse") ;
		return ;
	}
	
//  Check for stop being requested and exit

	if ( mIsStopRequested ) {
		mParserState = mErrorFlag = NO_RESULTS ;
		debugExit("parse") ;
		return ;
	}
	
//  Complete the calculations (real lengths, pitches and repeats)

//	long  ParseTime = System.currentTimeMillis() ;
	
	calculate() ;
			
//	long  CalcTime = System.currentTimeMillis() ;
	
//	Check for stop being requested and exit

	if ( mIsStopRequested ) {
		mParserState = mErrorFlag = NO_RESULTS ;
		debugExit("parse") ;
		return ;
	}

//  Terminate and return
		
	mParserState = mErrorFlag ;
	
//	System.out.println( "End Parse  P=" + ( ParseTime - StartTime ) + " " 
//					  + "C=" + ( CalcTime  - ParseTime ) ) ;
	
	debugExit("parse") ;
}


/**
 * Parse a number from the current line.
 * 
 * @return  a string representation of the number or null if none found
 */
public String  parseNumber()
{
	debugEntry("parseNumber") ;
	
//  Keep reading digits until non-digit detected

	String  Number = "" ;
	
	for ( ; mCurrPos < mCurrLen ; mCurrPos++ ) {
		char  c = mCurrLine[mCurrPos] ;
		if ( Character.isDigit(c) )   Number += c ;
		else						  break ;
	}
	
//  Set number to null if empty and return

	if ( Number.length() == 0 )   Number = null ;
	
	debugExit("parseNumber") ;
	return  Number ;
}


/**
 * Parse a note length of the form x/y or just x from the current line.
 * 
 * @return  in 2 lement integer array with Num and Denom or null if none found
 */
public int[]  parseNoteLength( boolean StrictFlag )
{
	debugEntry("parseNoteLength") ;

//  Extract a single number as the numerator - note that we may have no numerator
//  If this is not strict

	String  Num = parseNumber() ;
	
	if ( Num == null ) {
		if ( StrictFlag
		 ||  mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != '/' ) { 
			debugExit("parseNoteLength") ;
			return  null ;
		}
	}
	
//  If next character is a '/' then skip by and parse another number.
//  If no number then assume 2 as this is the default behaviour

	String  Denom = null ;
	
	if ( mCurrPos < mCurrLen  &&  mCurrLine[mCurrPos] == '/' ) {
		mCurrPos++ ;
		
		Denom = parseNumber() ;
		if ( Denom == null )   Denom = "2" ;
	}
	
//  If next char is not a '/' and strict is requested the return null

	else {
		if ( StrictFlag ) {
			debugExit("parseNoteLength") ;
			return  null ;
		}
	}

//	Save the values and return

	int[]  Arr = new int[2] ;
	Arr[0] = ( Num   == null ) ? 0 : Integer.parseInt(Num) ;
	Arr[1] = ( Denom == null ) ? 0 : Integer.parseInt(Denom) ;
		
	debugExit("parseNoteLength") ;
	return  Arr ;
	}


/**
 * Parse a base note from the current line.
 * 
 * <p>If this method is called then we MUST find a valid base note as there
 * will be no other option to choose.   Consequently, if we do not, then an
 * exception is returned.
 *
 * @return  a string representation of the base note or 0 if none found
 * 
 * @throws  a parser exception if no valid base note found
 **/
public char  parseBaseNote()   throws ParserException
{
	debugEntry("parseBaseNote") ;

	char  BaseNote ;

	if ( mCurrPos == mCurrLen ) {
		debugExit("parseBaseNote") ;
		throw  new ParserException("No base note found") ;
	}
	
	BaseNote = mCurrLine[mCurrPos] ;
	if ( Pitch.VALID_BASENOTES.indexOf(BaseNote) < 0 ) {
		debugExit("parseBaseNote") ;
		throw  new ParserException("Invalid base note") ;
	}
	
	mCurrPos++ ;
	
//	Return the extracted base note

	debugExit("parseBaseNote") ;
	return  BaseNote ;
}


/**
 * Parse an accidental from the current line.
 *
 * @return  an integer representation of the accidentals or 0 if none found
 */
public int  parseAccidental()
{
	debugEntry("parseAccidental") ;

//	Check against all valid accidentals

	for ( int i = 0 ; i < Pitch.VALID_ACCIDENTALS.length ; i++ ) {
		String  TestAcc = Pitch.VALID_ACCIDENTALS[i] ;
		
		if ( compareTo( TestAcc, TestAcc.length() ) ) {
			mCurrPos += TestAcc.length() ;
			debugExit("parseAccidental") ;
			
			if ( i == 0 )   return  +2 ;
			if ( i == 1 )   return  -2 ;
			if ( i == 2 )   return  +1 ;
			if ( i == 3 )   return  -1 ;
			if ( i == 4 )   return   9 ;
			return  0 ;
		}
	}
	
//	If we got this far then no valid accidental was found - return null

	debugExit("parseAccidental") ;
	return  0 ;
}


/**
 * Parse octave indications from the current line.
 *
 * @return  The octave indication as an integer or null if none found
 */
public int  parseOctave()
{
	debugEntry("parseOctave") ;
	
//	Check the first character to see if we have up or down octave

	if ( mCurrPos == mCurrLen ) {
		debugExit("parseOctave") ;
		return  0 ;
	}

	int   Octave    = 0 ;
	char  OctChar   = mCurrLine[mCurrPos] ;
	
	int   Increment = 0 ;
	if ( OctChar  == '\'' )   Increment = +1 ;
	if ( OctChar  == ','  )   Increment = -1 ;
	
//	Remove octave indications

	if ( Increment != 0 ) {
		while ( mCurrPos < mCurrLen ) {
			char c = mCurrLine[mCurrPos] ;
			if ( c != OctChar )   break ;
			Octave += Increment ;
			mCurrPos++ ;			
		}
		debugExit("parseOctave") ;
		return  Octave ;
	}
	
//	If we got this far then no indications were found - return null

	debugExit("parseOctave") ;
	return  0 ;
}


/**
 * Parse Gracings from the current line.
 *
 * @return  a string representation of the gracings or null if none found
 */
public String  parseGracings()
{
	debugEntry("parseGracings") ;

	if ( mCurrPos == mCurrLen ) {
		debugExit("parseOctave") ;
		return  null ;
	}

//	Remove and store all gracings characters

	String  Gracings = "" ;
	
	while ( VALID_GRACINGS.indexOf( mCurrLine[mCurrPos] ) >= 0 )
		Gracings += mCurrLine[mCurrPos++] ;

	if ( Gracings.length() == 0 )   Gracings = null ;
	
	debugExit("parseGracings") ;
	return  Gracings ;
}


/**
 * Parse tie indication from the current line.
 *
 * @return  a string representation of the tie or null if none found
 */
public String  parseTie()
{
	debugEntry("parseTie") ;
	
//	If next character is '-' then we have a tie

	if ( mCurrPos < mCurrLen  &&  mCurrLine[mCurrPos] == '-' ) {
		mCurrPos++ ;
		debugExit("parseTie") ;
		return  "-" ;
	}
	
//	If we got this far then no indications were found - return null

	debugExit("parseTie") ;
	return  null ;
}


/**
 * Parse beam indication from the current line.
 * 
 * <p>Note that this does NOT bump the input pointer.
 *
 * @return  " " for no beam, null for beam
 */
public String  parseBeam()
{
	debugEntry("parseBeam") ;
	
//	If next character is ' ' or end of string then no beaming

	if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] == ' ' ) {
		debugExit("parseBeam") ;
		return  " " ;
	}
	
//	If we got this far then no indications were found - return null to indicate beam

	debugExit("parseBeam") ;
	return  null ;
}


/**
 * Parse broken rhythm indication from the current line.
 *
 * @return  The broken rhythm count ( ">>" = 2, "<<<" = -3, etc. )
 */
public int  parseBrokenRhythm()
{
	debugEntry("parseBrokenRhythm") ;
	
//  Check which type of broken rhythm follows 

	char  BRType = '?' ;
	if ( mCurrPos < mCurrLen )   BRType = mCurrLine[mCurrPos] ;
	
	if ( BRType != '<'  &&  BRType != '>' ) {
		debugExit("parseBrokenRhythm") ;
		return  0 ;
	}
	
//  Read and count broken rhythm

	int  Count = 0 ;

	for ( ; mCurrPos < mCurrLen ; mCurrPos++ ) {
		if ( mCurrLine[mCurrPos] != BRType )   break ;
		Count++ ;
	}
	
	if ( BRType == '<' )   Count = -Count ;

//	Return the parsed value

	debugExit("parseBrokenRhythm") ;
	return  Count ;
}


/**
 * Parse Begin Slur from the current line.
 * 
 * <p>If successful then an element will have been added to the music line.
 *
 * @return  a string representation of the begin slur or null if none found
 */
public String  parseBeginSlur()
{
	debugEntry("parseBeginSlur") ;
	
//	Return null if not a slur

	if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != '(' ) {
		debugExit("parseBeginSlur") ;
		return  null ;	
	}

//	Construct and add the required element
	
	mCurrPos++ ;
	ABCElement  Element = new BeginSlurElement() ;		
	mABCLine.add(Element) ;
	link(Element) ;
	
	debugExit("parseBeginSlur") ;
	return  "(" ;
}


/**
 * Parse End Slur from the current line.
 * 
 * <p>If successful then an element will have been added to the music line.
 *
 * @return  a string representation of the end slur or null if none found
 */
public String  parseEndSlur()
{
	debugEntry("parseEndSlur") ;
	
//	Return null if not a slur

	if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != ')' ) {
		debugExit("parseEndSlur") ;
		return  null ;	
	}

//	Construct and add the required element
	
	mCurrPos++ ;

	ABCElement  Element = new EndSlurElement() ;
	mABCLine.add(Element) ;
	link(Element) ;

	debugExit("parseEndSlur") ;
	return  ")" ;
}


/**
 * Parse Guitar Chord from the current line.
 *
 * @return  a string representation of the chord (no quotes) or null if none found
 * 
 * @throws  a parser exception if chord started but invalid
 */
public String  parseGuitarChord()   throws ParserException
{
	debugEntry("parseGuitarChord") ;
	
	
//  Check first char for a "

	if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != '"' ) {
		debugExit("parseGuitarChord") ;
		return  null ;
	}

//	Locate the ending " and extract the chord or return exception for error
		
	mCurrPos++ ;
	
	int  Pos = indexOf('\"') ;
	if ( Pos < 0 ) {
		debugExit("parseGuitarChord") ;
		throw  new ParserException("Guitar chord invalid") ;
	}

	String  Chord = lineToString( mCurrPos, Pos - mCurrPos ) ;
	mCurrPos = ++Pos ;
	debugExit("parseGuitarChord") ;
	return  Chord ;
}


/**
 * Parse Nth repeats from current line.
 * 
 * <p>If successful then an element will have been added to the music line.
 *
 * @return  a string representation of the repeat or null if none found
 */
public String  parseNthRepeat()
{
	debugEntry("parseNthRepeat") ;
	
	for ( int i = 0 ; i < NthRepeatElement.VALID_NTHREPEATS.length ; i++ ) {
		String  TestRep = NthRepeatElement.VALID_NTHREPEATS[i] ;
		
		if ( compareTo( TestRep, TestRep.length() ) ) {

		//  Construct and add the required element
			
			ABCElement  Element = new NthRepeatElement(i) ;
			mABCLine.add(Element) ;
			link(Element) ;
			
			mCurrPos += TestRep.length() ;
			
			debugExit("parseNthRepeat") ;
			return  TestRep ;
		}
	}
		
	debugExit("parseNthRepeat") ;
	return  null ;	
}


/**
 * Parse Bar lines from the current line.
 * 
 * <p>If successful then an element will have been added to the music line.
 *
 * @return  a string representation of the bar line or null if none found
 */
public String  parseBarLine()
{
	debugEntry("parseBarLine") ;
	
	for ( int i = 0 ; i < BarLineElement.VALID_BARLINES.length ; i++ ) {
		String  TestBar = BarLineElement.VALID_BARLINES[i] ;
		
		if ( compareTo( TestBar, TestBar.length() ) ) {
			
		//  Construct and add the required element

			ABCElement  Element = new BarLineElement(i) ;			
			mABCLine.add(Element) ;
			link(Element) ;
			
			mCurrPos += TestBar.length() ;
			
			debugExit("parseBarLine") ;
			return  TestBar ;
		}
	}
		
	debugExit("parseBarLine") ;
	return  null ;	
}


/**
 * Parse Grace Notes from the current line.
 *
 * @return  an array containing the pitches of the grace notes
 * 
 * @throws  a parser exception if grace notes started but invalid
 */
public ArrayList  parseGraceNotes()   throws ParserException
{
	debugEntry("parseGraceNotes") ;
	
//  Check for leading {

	if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != '{' ) {
		debugExit("parseGraceNotes") ;
		return  null ;
	}

	mCurrPos++ ;	
	
//	Build an array list containing the parsed pitches

	ArrayList  GraceNotes = new ArrayList() ;

//	Keep parsing until closing brace or error

	while ( true ) {
		if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] == 'z' ) {
			debugExit("parseGraceNotes") ;
			throw  new ParserException("Grace Notes invalid") ;
		}
		
		if ( mCurrLine[mCurrPos] == '}' )   break ;
		
		Pitch  p = parsePitchOrRest() ;		// May throw exception
		
	//  Store the results in the array and remove the text
	
		GraceNotes.add(p) ;
	}

	if ( GraceNotes.size() == 0 )   GraceNotes = null ;
	
//	Return what's been removed	

	mCurrPos++ ;
	debugExit("parseGraceNotes") ;
	return  GraceNotes ;
}


/**
 * Parse a pitch (e.g. ^G''') from the current line.
 * 
 * <p>This also catches rests.   It a fundamental routine used as part of the
 * catch-all parsing.   If nothing else is detected then what we have must be a
 * note and is in error if one is not found.   Consequently, when this method is
 * called we MUST be able to locate a pitch.   Therefore, unlike the previous
 * routines, we should return an exception rather than null if it is not found).
 *
 * <p>The member variable <code>mParsePitchResults</code> contains
 * the results as a <code>Pitch</code> for later use so there is no need to
 * pass the results back (hence improving performance at the cost of purity)
 *
 * @return  the pitch object created
 * 
 * @throws  a parser exception if pitch is invalid
 **/
public Pitch  parsePitchOrRest()   throws ParserException
{
	debugEntry("parsePitchOrRest") ;
	
	char  BaseNote   = 0 ;
	int   Accidental = 0 ;
	int   Octave     = 0 ;
	
	if ( mCurrPos == mCurrLen ) {
		debugExit("parsePitchOrRest") ;
		throw  new ParserException("Invalid end of line - base note missing") ;
	}

//	Check if this is a rest

	if ( mCurrLine[mCurrPos] == 'z' ) {
		BaseNote = 'z' ;
		mCurrPos++ ;
	}
	
//	Handle a real note

	else {
		
	//  Check for leading accidentals

		Accidental = parseAccidental() ;

	//  Check the base note (this will throw an exception if not valid)

		BaseNote = parseBaseNote() ;

	//  Check for octave indications

		Octave = parseOctave() ;
	}

//	Create a new pitch and return it

	Pitch  p = new Pitch( BaseNote, Accidental, Octave ) ;

	debugExit("parsePitchOrRest") ;
	return  p ;
}


/**
 * Parse a note (e.g. ^G2/3-) or a rest from the current line.
 *
 * <p>This also catches rests.   It a fundamental routine used as part of the
 * catch-all parsing.   If nothing else is detected then what we have must be a
 * note and is in error if one is not found.   Consequently, when this method is
 * called we MUST be able to locate a note.   Therefore, unlike the previous
 * routines, we should return an exception rather than null if it is not found).
 *
 * @return  the note object constructed (null is not an option here)
 * 
 * @throws  a parser exception if note started but invalid
 **/
public Note  parseNoteOrRest()   throws ParserException
{
	debugEntry("parseNoteOrRest") ;
	
//	Check for a note or a rest (This will throw an exception if not found)
//  Results in mParsePitchResults

	Pitch  Pitch = parsePitchOrRest() ;
	
//	Check for note length (results in mParseNoteLengthResults)

	int[]  NoteLength = parseNoteLength(false) ;	// Non-strict

//	Check for tie
	
	String  Tie = parseTie() ;

//	Store the values in the relevant member variables of mParseNoteResults
//	Note that the note length info has already been save in mParseNoteLengthResults
//	and the pitch info in mParsePitchResults

	Note  Note ;
	
	if ( NoteLength == null )
		Note = new Note( Pitch, 0, 0, Tie ) ;
	else
		Note = new Note( Pitch, NoteLength[0], NoteLength[1], Tie ) ;

//	Now return what has been removed

	debugExit("parseNoteOrRest") ;
	return  Note ;
}


/**
 * Parse Note Element from current line.
 * 
 * <p>A note element consists of chord + grace notes + gracings +note/rest.
 *
 * <p>This also catches rests.   It a fundamental routine used as part of the
 * catch-all parsing.   If nothing else is detected then what we have must be a
 * note element and is in error if one is not found.   Consequently, when this method is
 * called we MUST be able to locate a note element.   Therefore, unlike the previous
 * routines, we should return an exception rather than null if it is not found).
 * 
 * @param Tuplet  the tuplet being parsed or null if ordinary note
 *
 * @return  a string representation of the note element (null is not an option here)
 * 
 * @throws  a parser exception if note started but invalid
 */
public String  parseNoteElement( TupletElement Tuplet )
   throws ParserException
{
	debugEntry("parseNoteElement") ;
	
	int  OldPos = mCurrPos ;
	
	NoteElement  Element ;

//  Check if there is a guitar chord here, throws exception if invalid

	String  GuitarChord = parseGuitarChord() ;
	
//  Space is not strictly OK after a guitar chord but many use it

	skipLeadingWhitespace() ;

//  Check if there are grace notes here
//  This will return an exception if there is an error
	
	ArrayList  GraceNotes = parseGraceNotes() ;

//  Parse gracings

	String  Gracings = parseGracings() ;

//  Check for multi note

	if ( mCurrPos < mCurrLen  &&  mCurrLine[mCurrPos] == '[' ) {
		
		ArrayList  MultiNote = parseMultiNote() ;

	//  Check for broken rhythm
	
		int  BrokenRhythm = parseBrokenRhythm() ;

	//  Check for beaming
	
		boolean  Beam = ( parseBeam() == null ) ;

	//  Construct the required multinote element
		
		Element = new MultiNoteElement( GuitarChord, GraceNotes, Gracings,
										MultiNote, BrokenRhythm, Beam ) ;
	}
	
//  Check for single note and all its associated bits
//  This will return an exception if the note is invalid

	else {
		Note  Note = parseNoteOrRest() ;

	//  Check for broken rhythm
	
		int  BrokenRhythm = parseBrokenRhythm() ;

	//  Check for beaming
	
		boolean  Beam = ( parseBeam() == null ) ;

//  Add a rest or a note element as required
	
		if ( Note.mPitch.mBaseNote == 'z' )
			Element = new RestElement( GuitarChord, GraceNotes, Gracings,
									   Note, BrokenRhythm ) ;
		else
			Element = new NoteElement( GuitarChord, GraceNotes, Gracings,
									   Note, BrokenRhythm, Beam ) ;
	}
	
//  If chords have been used then record this fact against the ABCMusic instance
	
	if ( GuitarChord != null )   mABCMusic.mHasChords = true ;
	
//  Finally add the element to the correct place and return what has been removed
	
	if ( Tuplet != null )
		Tuplet.add(Element) ;
	else {
		mABCLine.add(Element) ;
		link(Element) ;
	}
	
	debugExit("parseNoteElement") ;
	return  lineToString( OldPos, mCurrPos - OldPos ) ;
}

public String  parseNoteElement()   throws ParserException
{	return  parseNoteElement(null) ;
}


/**
 * Parse a Music line from the current line.
 *
 * @throws  a parser exception if line invalid
 */
public void  parseMusicLine()   throws ParserException
{
	debugEntry("parseMusicLine") ;
	
//  Check if this is the very first identified music line.
//  If so then we should validate the header info stored so far.

	if ( mParsingHeader ) {
		validateHeader() ;
		mParsingHeader = false ;
	}

//	Separate the text and comment

	String  Comment = null ;
	
	int  CommentPos = lastIndexOf('%') ;		// Comment
	if ( CommentPos >= 0 ) {
		Comment = lineToString( CommentPos + 1 ) ;
		mCurrLen = CommentPos ;		
	}
	
	trim() ;		// Remove leading and trailing whitespace	

//	Check for line break or no-break character at the end

	String  Break = null ;

	char  c = mCurrLine[mCurrLen - 1] ;
	if ( c == '!'  ||  c == '\\'  ||  c == '*' ) {
		mCurrLen-- ;
		skipTrailingWhitespace() ;
		Break = "" + c ;
	}
	
//	Construct a line element to store the results
	
	mABCLine = new MusicLineElement(Break) ;
	
	mABCLine.setComment(Comment) ;
	mABCLine.setCommentPos(CommentPos) ;
	   
//	Remainder is music - loop round getting each respective element
	
	while ( mCurrPos < mCurrLen ) {
		
		if ( sDebug ) debug( "Parsing >" + lineToString() + "<" ) ;
		
	//  Trim off leading and trailing space.   We should not trim off embedded
	//  space as this may be relevant for beaming
		
		trim() ;

	//  Check the first few characters to determine what is happening.
	//  Check in the order given to make sure we get the right result when
	//  there are common characters

	//  "[1" "[2" "|1" ":|2"				Nth repeat
	//	"||" "|]" "|:" "[|" "::" ":|" "|"	Bar line
	//	"(" + Digit							Tuplet
	//  "("									Begin slur
	//	")"									End slur
	//	Assume the rest are -				Note Elements
	
	//  Check for nth repeat.
	//	If this returns non null then it was OK,
	//  If null then not found so continue to next test
		
		if ( parseNthRepeat() != null )   continue ;	
	
	//  Check for bar line.
	//	If this returns non null then it was OK,
	//  If null then not found so continue to next test
	
		if ( parseBarLine() != null )   continue ;	
	
	//  Check for tuplet
	//  Note that we must be careful it isn't a begin slur by looking ahead
	
		if ( mCurrPos < mCurrLen - 1
		 &&  mCurrLine[mCurrPos] == '('
		 &&  Character.isDigit( mCurrLine[mCurrPos + 1] ) ) {
			parseTuplet() ;
			continue ;		//  Go back round loop
		 }
	
	//  Check for begin slur.
	//	If this returns non null then it was OK,
	//  If null then not found so continue to next test
	
		if ( parseBeginSlur() != null )   continue ;	
	
	//  Check for end slur.
	//	If this returns non null then it was OK,
	//  If null then not found so continue to next test
	
		if ( parseEndSlur() != null )   continue ;
		
	//  Check for in-line field
	
		if ( parseInLineField() != null )   continue ;	
	
	//  If we reach here then it is a note element
	//	If this returns non null then it was OK,
	//  If null then invalid so return error
	
		if ( parseNoteElement() == null ) {
			debugExit("parseMusicLine") ;
			throw  new ParserException( "Problem with note element") ;
		}
		 	
		continue ;		//  Go back round loop
	}	   

//  Finally add the music line element and return
//  Note that we first complete the element to generate the correct text 
	
	mABCLine.complete() ;
	mABCMusic.add(mABCLine) ;
	link(mABCLine) ;

	debugExit("parseMusicLine") ;
}


/**
 * Parse a key note from the current line.
 *
 * @throws  a parser exception if no key note to extract
 */
public String  parseKeyNote()   throws ParserException
{
	debugEntry("parseKeyNote") ;

//	Check for a valid base note (will throw exception if not valid)

	String  KeyNote = "" + parseBaseNote() ;
	
//	Check for a following key accidental and extract of possible

	if ( mCurrPos < mCurrLen ) {
		char  Accidental = mCurrLine[mCurrPos] ;
		if ( Key.VALID_ACCIDENTALS.indexOf(Accidental) >= 0 ) {
			KeyNote += Accidental ;
			mCurrPos++ ;
		}
	}

//	Now return the extracted note

	debugExit("parseKeyNote") ;
	return  KeyNote ;
}


/**
 * Parse current line of ABC.
 * 
 * <p>This may be comment, tex, field or music
 * 
 * @throws  a parser exception if line is invalid in any way
 */
void  parseLine( String Text )   throws ParserException
{
	debugEntry("parseLine") ;
	
	debug( ">" + Text + "<" ) ;
	
//  Remove leading and trailing white space and check for empty (end of tune)
	
	Text.trim() ;
	if ( Text.length() == 0 ) {
		debugExit("parseLine") ;
		return ;
	} 
			
//  Convert to a character array before parsing to improve parsing speed
			
	mCurrLine = Text.toCharArray() ;
	mCurrPos  = 0 ;
	mCurrLen  = mCurrLine.length ;
	
//  If we are still parsing history then we should check for continuation.
//  If this line is not a field then just add a new history line to the tune

	char  Type = mCurrLine[0] ;

	if ( mParsingHistory ) {
		if ( Type != '%'  &&  Type != '\\'
		
		 &&  ( mCurrLen < 2  ||  mCurrLine[1] != ':' ) ) {
			
		// Parse as a new history line and return
		
			mCurrLine = Utils.concatenate( "H:", mCurrLine ) ;
			mCurrLen  = mCurrLine.length ; 
			parseFieldLine() ;
			
			debugExit("parseLine") ;
			return ;
		}
	}

	mParsingHistory = false ;
	
//	Identify what sort of line this is
//	(Field, In-tune Field, Comment, Music, Tex command)
//  Beware of a bar-line type |: being mistaken for a field !

	if      ( Type == '\\' )					 parseTexCommandLine() ;
	else if ( Type == '%'  )  					 parseCommentLine() ;
	else if ( mCurrLine.length > 1
	      &&  mCurrLine[1] == ':'
		  &&  Type != '|' )						 parseFieldLine() ;
	else										 parseMusicLine() ;
	
	debugExit("parseLine") ;
}


/**
 * Parse current line as comment line.
 */
public void  parseCommentLine()
{
	debugEntry("parseCommentLine") ;
	
//	Build a comment element and add it to the header

	String  Comment = lineToString(1) ; 
	mABCHeader.add( new CommentElement(Comment) ) ;
		
	debugExit("parseCommentLine") ;
}


/**
 * Parse current line as a TEX Command line.
 *
 * @throws  a parser exception if found inside header
 */
public void  parseTexCommandLine()   throws ParserException
{
	debugEntry("parseTexCommandLine") ;
	
//	Tex command are not valid inside the header

	if ( mParsingHeader ) {
		debugExit("parseTexCommandLine") ;
		throw new ParserException("Tex commands not valid inside header") ;
	}
		
//	Build a Tex command element and add it to the header

	String  Command = lineToString(1) ; 
	mABCHeader.add( new TexCommandElement(Command) ) ;	

	debugExit("parseTexCommandLine") ;
}


/**
 * Parse current line as a Field line.
 *
 * @throws  a parser exception if field is invalid
 */
public void  parseFieldLine( boolean InLineField )   throws ParserException
{
	debugEntry("parseFieldLine") ;
	
//	Get the field type and validate it in context

	char  Type = mCurrLine[mCurrPos] ;
	
	if ( mParsingHeader ) {
		if ( VALID_HEADER_FIELDS.indexOf(Type) < 0 ) {
			debugExit("parseFieldLine") ;
			throw new ParserException("Field not valid in header") ;
		}
	}
	else {
		if ( VALID_MUSIC_FIELDS.indexOf(Type) < 0 ) {
			debugExit("parseFieldLine") ;
			throw new ParserException("Field not valid in music") ;
		}
	}
	
//	Extract the text and comment

	mCurrPos += 2 ;		// Point after X:
	
	String  Comment = null ;
	
	int  CommentPos = lastIndexOf('%') ;		// Comment
	if ( CommentPos >= 0 ) {
		Comment = lineToString( CommentPos + 1 ) ;
		mCurrLen = CommentPos ;		
	}
	
	trim() ;		// Remove whitespace from the ends
	
//	Check and parse specific field types

	FieldElement  Element ;

	if ( Type == 'X' )
		Element = parseFieldIndex() ;
	else if ( Type == 'L' )
		Element = parseFieldDefaultLength() ;
	else if ( Type == 'M' )
		Element = parseFieldMeter() ;
	else if ( Type == 'P' ) {
		if ( mParsingHeader )
			Element = parseFieldParts() ;
		else
			Element = parseFieldPart() ;
	}
	else if ( Type == 'Q' )
		Element = parseFieldTempo() ;
	else if ( Type == 'K' )
		Element = parseFieldKey() ;
	else if ( Type == 'w' )
		Element = parseWords() ;
	
//	Build a general text field element and store.
//  Notew that we preserve any embedded eading spaces here	


	else {
		mCurrPos = 2 ;
		Element = new TextFieldElement( Type, lineToString() ) ;
	}
	
//  Indicate that this is an inline field
	
	Element.setInline(true) ;
	
//	Add the comment then add the field to the header or music as required

	Element.setComment(Comment) ;
	Element.setCommentPos(CommentPos) ;

	if ( mParsingHeader )
	   mABCHeader.add(Element) ;	
	else if ( InLineField ) {
		mABCLine.add(Element) ;
		link(Element) ;
	} 
	else {
	   	mABCMusic.add(Element) ;
		link(Element) ;
	}	
	
//  If we have just parsed a history field the turn on the flag for continuation

	if ( Type == 'H' )   mParsingHistory = true ;

	debugExit("parseFieldLine") ;
}

/**
 * Parse a non-inline field.
 */
public void  parseFieldLine()   throws ParserException
{	parseFieldLine(false) ;   }


/**
 * Parse Index field from current line.
 *
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseFieldIndex()   throws ParserException
{
	debugEntry("parseFieldIndex") ;
	
//	Ensure the Text value is a number

	String  Index = parseNumber() ;
	if ( Index == null ) {
		debugExit("parseFieldIndex") ;
		throw new ParserException("Index field must be numeric") ;
	}
	
//  Remainder of line should be empty

	if ( mCurrPos < mCurrLen ) {
		debugExit("parseFieldIndex") ;
		throw new ParserException("Invalid characters") ;
	}

//	Construct and return an index field
		
	FieldElement  Element = new IndexFieldElement(Index) ;	
	debugExit("parseFieldIndex") ;
	return  Element ;	
}


/**
 * Parse Default Length field from current line.
 *
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseFieldDefaultLength()   throws ParserException
{
	debugEntry("parseFieldDefaultLength") ;
	
//	Parse a strict note length from the text
//	If null is returned then there was not a valid value and we should generate
//	an exception to indicate that this line is invalid

	int[]  NoteLength = parseNoteLength( true ) ;
	
	if ( NoteLength == null ) {
		debugExit("parseFieldDefaultLength") ;
		throw  new ParserException(
					"Default Length field must have strict note length") ;
	}
	
//	Remainder of line should be empty

	if ( mCurrPos < mCurrLen ) {
		debugExit("parseFieldDefaultLength") ;
		throw new ParserException("Invalid characters") ;
	}
	
//	Construct and return a default length field
	
	FieldElement  Element = new DefaultLengthFieldElement(
					NoteLength[0], NoteLength[1] ) ;
	debugExit("parseFieldDefaultLength") ;
	return  Element ;
}


/**
 * Parse Meter field from given text.
 *
 * @param Text  the string to check
 *
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseFieldMeter()   throws ParserException
{
	debugEntry("parseFieldMeter") ;
	
//	First of all we should check for cut time or common time

	FieldElement  Element ;

	if ( compareTo("C") ) {
	    Element = new MeterFieldElement("C") ;
	    mCurrPos++ ;
	}
	else if ( compareTo("C|") ) {
	   Element = new MeterFieldElement("C|") ;
	   mCurrPos += 2 ;
	}
	
//	Parse a meter fraction from the text
//	The result array contains all the numerators followed by a single denominator
//  An exception is thrown if invalid

	else {
		int[]  Result = parseMeterFraction() ;
		Element = new MeterFieldElement(Result) ;
	}
	
//	Remainder of line should be empty

	if ( mCurrPos < mCurrLen ) {
		debugExit("parseFieldDefaultLength") ;
		throw new ParserException("Invalid characters") ;
	}
	
//	Return the meter field
	
	debugExit("parseFieldMeter") ;
	return  Element ;
}


/**
 * Parse a meter fraction of the form x+y+.../z from current line.
 * 
 * <p>Last element of the int array is the denominator
 *
 * @return  an array containing the numerators and the denominator
 * 
 * @throws  a parser exception if the field is invalid
 */
public int[]  parseMeterFraction()   throws ParserException
{
	debugEntry("parseMeterFraction") ;
	
//  Parse the numerators

	ArrayList  Arr = new ArrayList() ;
	
	while ( true ) {
		
		String  w = parseNumber() ;
		if ( w == null ) {
			debugExit("parseMeterFraction") ;
			throw  new ParserException("Meter fraction invalid") ;
		}
		
		Arr.add(w) ;
		
	//  Check for another numerator
		
		if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != '+' )   break ;
		mCurrPos++ ;
	}
	
//  Check for '/ next

	if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != '/' ) {
		debugExit("parseMeterFraction") ;
		throw  new ParserException("Meter fraction invalid") ;
	}
	
	mCurrPos++ ;
	
//  Now extract the denominator	
	
	String  w = parseNumber() ;
	if ( w == null ) {
		debugExit("parseMeterFraction") ;
		throw  new ParserException("Meter fraction invalid") ;
	}
		
	Arr.add(w) ;

//  Finally return the results

	int[]  Result = new int[ Arr.size() ] ;

	for ( int i = 0 ; i < Arr.size() ; i++ )
		Result[i] = Integer.parseInt( ( (String) Arr.get(i) ) ) ;
		
	debugExit("parseMeterFraction") ;
	return  Result ; 
}


/**
 * Parse Parts field from the current line.
 *
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseFieldParts()   throws ParserException
{
	debugEntry("parseFieldParts") ;
	
//	 Validate a part spec allowing for nesting

	boolean  NumAllowed = false ;
	int		 Nesting    = 0 ;
	boolean  Valid      = true ;
	
	int  OldPos = mCurrPos ;

	for ( ; mCurrPos < mCurrLen ; mCurrPos++ ) {
		char  c = mCurrLine[mCurrPos] ;
		
		if ( Character.isUpperCase(c) ) {
			NumAllowed = true ;
		}
		else if ( Character.isDigit(c) ) {
			if ( ! NumAllowed )   Valid = false ;
		}
		else if ( c == '(' ) {
			Nesting++ ;
			NumAllowed = false ;
		}
		else if ( c == ')' ) {
			Nesting-- ;
			NumAllowed = true ;			
		}
		else
			Valid = false ;

		if ( ! Valid  ) {
			debugExit("parseFieldParts") ;
			throw new ParserException("Invalid parts spec") ;
		}
	}

//	Now check for nesting errors
	
	if ( Nesting != 0 ) {
		debugExit("parseFieldParts") ;
		throw new ParserException("Unmatched brackets in parts spec") ;
	}
	
//	Remainder of line should be empty

	if ( mCurrPos < mCurrLen ) {
		debugExit("parseFieldDefaultLength") ;
		throw new ParserException("Invalid characters") ;
	}
	
//	Construct and return a parts field
		
	FieldElement  Element = new PartsFieldElement( lineToString(OldPos) ) ;
	debugExit("parseFieldParts") ;
	return  Element ;
}


/**
 * Parse Part field from the current line.
 *
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseFieldPart()   throws ParserException
{
	debugEntry("parseFieldPart") ;

//  N.B. It seems that the part name can be a string ... such as 'Coda'
//  I have therefore suppressed the following edit checks	
	
//  Something must be specified
	
	if ( mCurrLen == mCurrPos ) {
		debugExit("parseFieldPart") ;
		throw new ParserException("No Part specified") ;
	}
	
//	Part must be a single uppercase letter

//	if ( mCurrLen - mCurrPos != 1 ) {
//		debugExit("parseFieldPart") ;
//		throw new ParserException("Part must be a single character A-Z") ;
//	}

//	if ( ! Character.isUpperCase( mCurrLine[mCurrPos] ) ) {
//		debugExit("parseFieldPart") ;
//		throw new ParserException("Part must be A to Z") ;
//	}
	
//	Remainder of line should be empty

//	if ( mCurrPos < mCurrLen ) {
//		debugExit("parseFieldDefaultLength") ;
//		throw new ParserException("Invalid characters") ;
//	}
	
//	Construct and return a part field

	FieldElement  Element = new PartFieldElement( lineToString(mCurrPos) ) ;

	debugExit("parseFieldPart") ;
	return  Element ;
}


/**
 * Parse Tempo field from the current line.
 *
 * <p>Optional forms are :-
 * <ul>
 * <li>		nnn		(equivalent to "C=nnn")
 * <li>		C=nnn
 * <li>		Cnnn=nnn
 * <li>	 	Cnnn/nnn=nnn
 * <li>	 	nnn/nnn=nnn
 * </ul>
 *
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseFieldTempo()   throws ParserException
{
	debugEntry("parseFieldTempo") ;

//	Initialize the values to collect

	boolean  Absolute   = false ;
	int	     Tempo      = 0 ;
	int[]    NoteLength = null ;
	
//	Catch any formatting errors

	try {

	//	Check for an initial leading "C" - relative tempo

		if ( mCurrLine[mCurrPos] == 'C' ) {
			mCurrPos++ ;
			int  Pos = indexOf('=') ;
			
			if ( Pos < 0 )   throw  new Exception() ;		// Dummy to force error
			
			if ( Pos == 0 ) {			// Relative "C=nnn"
				mCurrPos++ ;
			}
			else {		// "Cnnn=nnn"  or  "Cnnn/nnn=nnn"
				
				NoteLength = parseNoteLength(false) ;	// Non-strict
				if ( NoteLength == null )   throw  new Exception() ;
				
				mCurrPos = ++Pos ;	// skip past =
			}
		}
	
	//  No leading "C", may be absolute or relative

		else {
			int  Pos = indexOf('=') ;
			if ( Pos >= 0 ) {				// Absolute ("nnn/nnn=nnn")
				Absolute = true ;
				
				NoteLength = parseNoteLength(true) ;	// Strict
				if ( NoteLength == null )   throw  new Exception() ;
			
				if ( mCurrLine[mCurrPos] != '=' )   throw  new Exception() ;
				mCurrPos++ ;			//  remove the '=' sign
			}
		}
		
	//  Finally parse out the tempo value
	
		String  w = parseNumber() ;
		if ( w == null )   throw new Exception() ;	// Must have a tempo value

		Tempo = Integer.parseInt(w) ;
	
//		Remainder of line should be empty

		if ( mCurrPos < mCurrLen ) {
			debugExit("parseFieldDefaultLength") ;
			throw new ParserException("Invalid characters") ;
		}
	}
	
//	Catch errors and return

	catch ( Exception e ) {
		debugExit("parseFieldTempo") ;
		throw new ParserException("Invalid tempo spec.") ;
	}

//	Construct a Tempo field element and return

	FieldElement  Element ;
	if ( NoteLength == null )
		Element = new TempoFieldElement( 0, 0, Tempo, Absolute ) ;
	else
		Element = new TempoFieldElement( NoteLength[0], NoteLength[1],
										 Tempo, Absolute ) ;
		
	debugExit("parseFieldTempo") ;
	return  Element ;	
}


/**
 * Parse Key field from the current line.
 *
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseFieldKey()   throws ParserException
{
	debugEntry("parseFieldKey") ;

//	Check for special case of "HP" or "Hp"

	FieldElement  Element ;
	
	if ( compareTo("HP") ) {
	  Element = new KeyFieldElement( new Key( "HP", null, null ) ) ;
	  mCurrPos += 2 ;
	}
	else if ( compareTo("Hp") ) {
	   Element = new KeyFieldElement( new Key( "Hp", null, null ) ) ;
	   mCurrPos += 2 ;
	}
		
//	Parse full key spec

	else {
		try {
		
		//  Parse the note and remove it (may throw exception)
		
			String  KeyNote = parseKeyNote() ;
			
		//  Parse the mode (may throw exception)
		
			String Mode = parseMode() ;
			
		//  Locate next whitespace and find global accidentals if so
		//  Parse the global accidentals (may throw exception)

			int  Pos = locateWhitespace() ;
			if ( Pos >=0 )   mCurrPos = Pos ;
			else			 mCurrPos = mCurrLen ;		
			
			ArrayList  GlobAcc = null ;
			
			if ( Pos >= 0 ) {
				mCurrPos = Pos ;
				skipLeadingWhitespace() ;
			
				GlobAcc = parseGlobalAccidentals() ;
			}
	
		//	Remainder of line should be empty

			if ( mCurrPos < mCurrLen ) {
				debugExit("parseFieldDefaultLength") ;
				throw new ParserException("Invalid characters") ;
			}

		//  Build the required key element		
		
			Element = new KeyFieldElement( new Key( KeyNote, Mode, GlobAcc ) ) ;
		}

	//  Return any errors as an exception
		
		catch ( Exception e ) {
			debugExit("parseFieldKey") ;
			throw new ParserException("Invalid key spec.") ;
		}
	}

//	Finally create and return the result

	debugExit("parseFieldKey") ;
	return  Element ;	
}


/**
 * Parse a mode from the current line.
 *
 * @return  the mode string or null if not found
 * 
 * @throws  a parser exception if the mode is invalid
 */
public String  parseMode()   throws ParserException
{
	debugEntry("parseMode") ;
	
	if ( mCurrPos == mCurrLen
	 ||  mCurrLine[mCurrPos] == ' '  ||  mCurrLine[mCurrPos] == '\t' ) {
		debugExit("parseMode") ;
		return  null ;
	}
	
//	Check if any of the given required modes are given

	for ( int i = 0 ; i < Key.VALID_MODES.length ; i++ ) {
		String  TestMode = Key.VALID_MODES[i] ;
		
		if ( compareToNoCase( TestMode, TestMode.length() ) ) {
			mCurrPos += TestMode.length() ;
			debugExit("parseMode") ;
			return  TestMode ;
		}
	}

//	If we got this far then no valid mode was found - return an exception

	debugExit("parseMode") ;
	throw  new ParserException("Invalid Key mode") ;
}


/**
 * Parse global accidentals from the current line.
 *
 * @return  the array of pitches in the global accidentals or null
 * 
 * @throws  a parser exception if invalid
 */
public ArrayList  parseGlobalAccidentals()   throws ParserException
{
	debugEntry("parseGlobalAccidentals") ;

//  Return if nothing there to parse

	if ( mCurrPos == mCurrLen ) {
		debugExit("parseGlobalAccidentals") ;
		return  null ;
	}
		
//  Now extract all global accidentals

	ArrayList  GlobAcc = new ArrayList() ;
	
	try {
		
	//  Loop until end
	
		while ( mCurrPos < mCurrLen ) {
			
			skipLeadingWhitespace() ;
	
		//  Check for a leading accidental
	
			int  Accidental = parseAccidental() ;
			if ( Accidental == 0 )   throw  new Exception() ;	// Force error
		
		//  Check next char for a base note (may throw exception)

			char  BaseNote = parseBaseNote() ;
	
		//  Ok so far, add to the accumulating string
	
			GlobAcc.add( new Pitch( BaseNote, Accidental, 0 ) ) ;
		}
	}
	
//	Return any exceptions as an error

	catch ( Exception e ) {
		debugExit("parseGlobalAccidentals") ;
		throw  new ParserException("Invalid global accidental") ;
	}
	
	if ( GlobAcc.size() == 0 )   GlobAcc = null ;

	debugExit("parseGlobalAccidentals") ;
	return  GlobAcc ;
}


/**
 * Parse Tuplet from the current line.
 *
 * @return  the string representation of the tuplet
 * 
 * @throws  a parser exception if invalid
 */
public String  parseTuplet()   throws ParserException
{
	debugEntry("parseTuplet") ;
	
	int  OldPos = mCurrPos ;
	
//	Ensure we have a ( at the start

	if ( mCurrPos >= mCurrLen - 1  ||  mCurrLine[mCurrPos] != '(' ) {
		debugExit("parseTuplet") ;
		throw  new ParserException("Invalid tuplet") ;
	}

	mCurrPos++ ;	

//	Extract the tuplet spec - this may generate an exception
//	The results are in mParseTupletSpecResults.

	int[]  TupletSpec = parseTupletSpec() ;
	
//	Create a tuplet element to hold the notes

	TupletElement  Tuplet = new TupletElement(TupletSpec) ; 

//	Tuplet spec OK - now extract the required number of notes

	int  Count = TupletSpec[2] ;
	if  ( Count == 0 )   Count = TupletSpec[0] ;

	for ( int i = 0 ; i < Count ; i++ ) {
		
	//  Parse a single note.  This will throw an exception on error
	//  It will also add it to the tuplet if OK

		parseNoteElement(Tuplet) ;
	}

//  Check for beaming
	
	Tuplet.setBeam( parseBeam() == null ) ;

//	Results calculated - add element to line and return string removed

	Tuplet.complete() ;		//  Ensure the tuplet has been fully setup
	
	mABCLine.add(Tuplet) ; 
	link(Tuplet) ;

	debugExit("parseTuplet") ;
	return  lineToString( OldPos, mCurrPos - OldPos ) ;
}


/**
 * Parse Tuplet Spec from the current line ( (x:y:z ).
 *
 * @return  an integer array containing the 3 tuplet spec values
 * 
 * @throws  a parser exception if invalid
 */
public int[]  parseTupletSpec()   throws ParserException
{
	debugEntry("parseTupletSpec") ;
	
	int[]  TupletSpec = new int[3] ;
	for ( int i = 0 ; i < 3 ; i++ )   TupletSpec[i] = 0 ;

//	Extract the first number and validate

	String  w = parseNumber() ;
	if ( w == null ) {
		debugExit("parseTupletSpec") ;
		throw  new ParserException("Invalid tuplet spec") ;
	}
	
	TupletSpec[0] = Integer.parseInt(w) ;

//	If next char is a ':' then get the second part

	if ( mCurrPos < mCurrLen  &&  mCurrLine[mCurrPos] == ':' ) {
		
		mCurrPos++ ;
		
		w = parseNumber() ;
		if ( w == null ) {
			debugExit("parseTupletSpec") ;
			throw  new ParserException("Invalid tuplet spec") ;
		}
	
		TupletSpec[1] = Integer.parseInt(w) ;
	}
	
//	If next char is a ':' then get the third part

	if ( mCurrPos < mCurrLen  &&  mCurrLine[mCurrPos] == ':' ) {

		mCurrPos++ ;
		
		w = parseNumber() ;
		if ( w == null ) {
			debugExit("parseTupletSpec") ;
			throw  new ParserException("Invalid tuplet spec") ;
		}
	
		TupletSpec[2] = Integer.parseInt(w) ;
	}

//	Results calculated - return string removed

	debugExit("parseTupletSpec") ;
	return  TupletSpec ;
}


/**
 * Parse multi note from the current line.
 *
 * @return  an array of the notes in the multi-note (or null if not found)
 * 
 * @throws  a parser exception if invalid
 */
public ArrayList  parseMultiNote()   throws ParserException
{
	debugEntry("parseMultiNote") ;

//	Check first character is '['

	if ( mCurrPos == mCurrLen  ||  mCurrLine[mCurrPos] != '[' ) {
		debugExit("parseMultiNote") ;
		return  null ;
	}
	
	mCurrPos++ ;
	
//	Keep parsing notes until empty string or closing ']'

	ArrayList  MultiNote = new ArrayList() ;

	while ( true ) {
		
	//  Check for end of text (if so then error as no closing ']'
	
		if ( mCurrPos == mCurrLen ) {
			debugExit("parseMultiNote") ;
			throw  new ParserException("Unclosed multi-note") ;
		}
		
	//  Now check for a closing ']' and exit loop
	
		if ( mCurrLine[mCurrPos] == ']' )   break ;
		
	//  Get a single note (this may return an exception)
	
		Note  Note = parseNoteOrRest() ;
		
	//  Store the note in the array, results are in mParseNoteResults
	
		MultiNote.add(Note) ;
	}
	
	mCurrPos++ ;

//	All OK - return string rep of multinote element.

	debugExit("parseMultiNote") ;
	return  MultiNote ;
}


/**
 * Parse in-line field from current line.
 *
 * @return  the string representation of the field (or null if not found)
 * 
 * @throws  a parser exception if invalid
 */
public String  parseInLineField()   throws ParserException
{
	debugEntry("parseInLineField") ;
	
	int  OldPos = mCurrPos ;
	
//  Check first few characters to see if theis is an inline field

	if ( mCurrPos >= mCurrLen - 3
	 ||  mCurrLine[mCurrPos] != '['
	 ||  mCurrLine[mCurrPos + 2] != ':' ) {
		debugExit("parseInLineField") ;
		return  null ;
	 }

	 mCurrPos++ ;

//  Check for the end of the in-line field

	int  Pos = indexOf(']') ;
	if ( Pos < 0 ) {
		debugExit("parseInLineField") ;
		throw new ParserException("Unterminated in-line field") ;
	}

//  It looks valid so far.
//  Temporarily remove the end of the line and parse to the field parsing routine

	int  TempLen = mCurrLen ;
	mCurrLen = Pos ;

	parseFieldLine(true) ;		// true indicates inline field	
	
//  Field stored OK - restore current position and length before continuing
	
	mCurrPos = Pos + 1 ;
	mCurrLen = TempLen ;
	
	debugExit("parseInLineField") ;
	return  lineToString( OldPos, mCurrPos - OldPos ) ;
}


/**
 * Validate the header once it has all been loaded.
 * 
 * <p>This method provides various checks such as no key specified, ...
 */
public void  validateHeader()
{
	
//  Check for missing key field - error

	if ( mABCHeader.getKeyElement() == null ) {
		mLog.add( "EMissing Key Field in header") ;
		mErrorFlag = ABC_ERRORS ;
	}
		
//  Check for warnings - ??? Are these the correct defaults ???

//	if ( mABCHeader.getDefaultLength() == null ) {
//		mLog.add( "WMissing Default Length  L:1/8  assumed") ;
//		if ( mErrorFlag < ABC_WARNINGS )   mErrorFlag = ABC_WARNINGS ;
//	}
	
//	if ( mABCHeader.getMeter() == null ) {
//		mLog.add( "WMissing Meter  M:4/4  assumed") ;
//		if ( mErrorFlag < ABC_WARNINGS )   mErrorFlag = ABC_WARNINGS ;
//	}
	
//	if ( mABCHeader.getTempo() == null ) {
//		mLog.add( "WMissing Tempo  Q:120  assumed") ;
//		if ( mErrorFlag < ABC_WARNINGS )   mErrorFlag = ABC_WARNINGS ;
//	}
}


/**
 * Pass through all elements and calculate extra data.
 * 
 * <p>Whilst this could have been done during parsing, it's easier to
 * follow as a separate unit !
 * 
 * <p>This will determine the absolute note lengths, absolute pitch
 * and the repeats information.
 */
public void  calculate()
{

//  Get relevant values from the header

	mCurrentMeter     = mABCHeader.getMeter().getMeterAsFraction() ;
	mCurrentDefLength = mABCHeader.getDefaultLength().getDefaultLength() ;
	mCurrentKey       = mABCHeader.getKeyElement().getKey() ;
	mCurrentTempo     = mABCHeader.getTempo() ;
	
	mCurrentTempo.calculateAbsoluteTempo(mCurrentDefLength) ;
	
	mCurrentKey.resetLocalAccidentals() ;
	
//  Return if no elements

	if ( mABCMusic.getElements().size() == 0 )   return ;

//  Now process all music elements by following the chain

	ABCElement  Element     = mABCMusic.getRootElement() ;
	ABCElement  LastElement = Element ;
	
	mPhraseStarted = false ;
	mPhraseStart   = Element ;
	mPhraseRepeat1 = null ;
	mPhraseRepeat2 = null ;
	mPhrases       = new ArrayList() ;
	
	mPreviousBrokenRhythm = 0 ;
	
	while ( Element != null ) {
		
	//  Switch on the element type as it should be a little quicker then
	//  using instanceof
		
		switch ( Element.getType() ) {
		
	//  Check for in-line default length field and store
	
		case  DEFAULT_LENGTH_FIELD_ELEMENT :
			mCurrentDefLength = ( (DefaultLengthFieldElement) Element).
															getDefaultLength() ;
			break ;
				
	//  Check for in-line meter field and store

		case  METER_FIELD_ELEMENT :	
			mCurrentMeter = ( (MeterFieldElement) Element).getMeterAsFraction() ;
			break ;
	
	//  Check for in-line key field and store

		case  KEY_FIELD_ELEMENT :	
			mCurrentKey = ( (KeyFieldElement) Element).getKey() ;
			break ;
	
	//  Check for in-line tempo field and store

		case  TEMPO_FIELD_ELEMENT :	
			mCurrentTempo = ( (TempoFieldElement) Element) ;
			mCurrentTempo.calculateAbsoluteTempo(mCurrentDefLength) ;
			break ;
		
	//  Calculate correct default length for note elements (note,rest,multi)
	
	//  Handle a note element (note, rest or multi-note)
	
		case  NOTE_ELEMENT :	
		case  REST_ELEMENT :	
		case  MULTINOTE_ELEMENT :
			mPhraseStarted = true ;	
			calculateNote( (NoteElement) Element ) ;
			break ;

	//  Calculate correct default length tuplet elements
	//  ignore broken rhythms here
				
		case  TUPLET_ELEMENT :	
			mPhraseStarted = true ;	
			calculateTuplet( (TupletElement) Element ) ;
			break ;
		
	//  Handle bar-lines so we can determine the phrases to play
	
		case  BARLINE_ELEMENT :
			mCurrentKey.resetLocalAccidentals() ;
			calculateBarLine( (BarLineElement) Element ) ;
			break ;		
		
	//  Handle nth repeats so we can determine the phrases to play
	
		case  NTHREPEAT_ELEMENT :
			calculateNthRepeat( (NthRepeatElement) Element ) ;
			break ;		
		}
		
	// Store the most useful last element and move on

		if ( Element.getType() != MUSIC_LINE_ELEMENT )
			LastElement = Element ;
				
		Element = Element.mNextElement ;
	}
	
//  If a phrase is in progress but unterminated then generate a warning
//  and compensate

	if ( mPhraseStarted ) {

		if ( mErrorFlag < ABC_WARNINGS )   mErrorFlag = ABC_WARNINGS ;
		mLog.add( "W" + "No suitable bar line at end of tune") ;

	//  Insert a || bar-line at the end and force a phrase calculation
	
		BarLineElement  b = new BarLineElement(BarLineElement.DOUBLE) ;
		
		b.mParserAdded = true ;		// Indicate to not show on music
		b.mNextElement = LastElement.mNextElement ;
		LastElement.mNextElement = b ; 
		
		calculateBarLine(b) ;
	}
}


/**
 * A helper method to calculate the special multiplier for broken rhythms.
 */
private static Fraction  calculateBrokenRhythmMultiplier( int BrokenRhythm )
{
	final Fraction  HALF = new Fraction( 1, 2 ) ;
	
//  Return null if nothing

	if ( BrokenRhythm == 0 )   return  null ;

//  Calculate the special multiplier accordingly
	
	Fraction  Result = new Fraction( 1, 1 ) ;
	
	if ( BrokenRhythm < 0 ) {
		for ( int i = 0 ; i < -BrokenRhythm ; i++ )
			Result = Result.times(HALF) ;
	}
	else {
		Fraction  f = Result ;
		for ( int i = 0 ; i < BrokenRhythm ; i++ ) {
			f = f.times(HALF) ;
			Result = Result.plus(f) ;
		}
	}
	
	return  Result ;
}


/**
 * Calculate extra values for a note.
 * 
 * <p>This will determine the absolute length and absolute pitch of the note.
 */
private void  calculateNote( NoteElement Element )
{
	
//  Determine the correct broken rhythm adjuster and then age it

	int  BrokenRhythm = Element.getBrokenRhythm() ;

	int  CalcBroken = ( BrokenRhythm == 0 ) ?
						   -mPreviousBrokenRhythm : BrokenRhythm ;
						    
	mPreviousBrokenRhythm = BrokenRhythm ;
	
//  Now do the calculation
			
	Element.calculateAbsoluteLength( mCurrentDefLength,
					calculateBrokenRhythmMultiplier(CalcBroken) ) ;
	Element.calculateAbsolutePitch(mCurrentKey) ;
}


/**
 * Calculate extra values for a tuplet.
 * 
 * <p>This will determine the absolute length and absolute pitch of the note.
 */
private void  calculateTuplet( TupletElement Element )
{
	
//  ignore broken rhythms here
				
	Element.calculateAbsoluteLength( mCurrentDefLength, mCurrentMeter ) ;		
	Element.calculateAbsolutePitch(mCurrentKey) ;
}


/**
 * Calculate extra values for a bar-line.
 * 
 * <p>This is used to determine the phrases for playing
 */
private void  calculateBarLine( BarLineElement Element )
{
	
//	If no phrase started yet then just reset the start and return

	if ( ! mPhraseStarted ) {
		mPhraseStart = Element ;
		return ;
	}
	
//  Ignore simple bar lines as they are only mid-phrase

	int  BarLine = Element.mBarLine ;

	if ( BarLine == BarLineElement.SINGLE )   return ;
	
//  If not a mid-phrase bar-line then check if it is followed by a second
//  repeat.   If so, then treat as a simple bar-line as the following repeat
//  will cause the correct processing.  This will either be the next element
//  or the one after with an interposing music line element.

	ABCElement  Next = Element.getNextElement() ;
	while ( Next != null  &&  Next instanceof MusicLineElement )
		Next = Next.getNextElement() ;

	if ( Next instanceof NthRepeatElement
	 &&  ( (NthRepeatElement) Next ).mNthRepeat == NthRepeatElement.SECOND )
		return ;
		
//  All other bar line types terminate the current phrase and start a
//  new one.   Also, DOUBLE_REP and SINGLE_REP_LEFT will cause the whole
//  phrase structure to be repeated including first and second repeats.

//  There is an exception to this if the bar line is followed by a second
//  repeat element.   If this is the

	int  Count = 1 ;

	if ( BarLine == BarLineElement.DOUBLE_REP
	 ||  BarLine == BarLineElement.SINGLE_REP_LEFT )	
		Count = 2 ;
		
//  Validate the repeat structure if it is there

	if ( mPhraseRepeat1 != null  &&  mPhraseRepeat2 == null ) {
		mErrorFlag = ABC_ERRORS ;
		mLog.add( "E" + "Missing second repeat in tune") ;
	}

//  Now store this information as a phrase.
//  In this context, a phrase is a collection of elements which will be played
//  end to end and not the normal definition of a musical phrase.

	mPhrases.add( new Phrase( mPhraseStart, mPhraseRepeat1, mPhraseRepeat2,
							  Element, Count ) ) ;		

//  We should also allow for short lead-in bars and short repeat-blocks
		
//  Finally, reset the phrase indications to start a new block

	mPhraseStarted = false ;	
	mPhraseStart   = Element ;
	mPhraseRepeat1 = null ;
	mPhraseRepeat2 = null ;
}


/**
 * Calculate extra values for an nth repeat.
 * 
 * <p>This is used to determine the phrases for playing
 */
private void  calculateNthRepeat( NthRepeatElement Element )
{
	switch ( Element.mNthRepeat ) {
		
	//  Handle first repeat and store or error
	
		case  NthRepeatElement.FIRST_FULL :
		case  NthRepeatElement.FIRST :
		case  NthRepeatElement.BARLINE_FIRST :
			if ( mPhraseRepeat1 != null ) {
				mErrorFlag = ABC_ERRORS ;
				mLog.add( "E" + "Tune contains duplicate first repeat") ;
			}
			else
				mPhraseRepeat1 = Element ;
			break ;
		
	//  Handle second repeat and store or error
	
		case  NthRepeatElement.SECOND_FULL :
		case  NthRepeatElement.SECOND :
		case  NthRepeatElement.BARLINE_SECOND :
			if ( mPhraseRepeat2 != null ) {
				mErrorFlag = ABC_ERRORS ;
				mLog.add( "E" + "Tune contains duplicate second repeat") ;
			}
			else if ( mPhraseRepeat1 == null ) {
				mErrorFlag = ABC_ERRORS ;
				mLog.add( "E" + "Missing first repeat in tune") ;
			}
			else
				mPhraseRepeat2 = Element ;
			break ;
	}
}


/**
 * Parse Words field from the current line.
 *
 * <p>This is the "w:" field with the music itself and not the "W:" field.
 * 
 * @return  the relevant field element created
 * 
 * @throws  a parser exception if the field is invalid
 */
public FieldElement  parseWords()   throws ParserException
{
	debugEntry("parseWords") ;

	trim() ;
	
//  Something must be specified
	
	if ( mCurrLen == mCurrPos ) {
		debugExit("parseWords") ;
		throw new ParserException("No Words specified") ;
	}
	
//	Construct and return a words field.
//  Note that this is associated with the immediately preceding music line

	FieldElement  Element = new WordsFieldElement( lineToString(mCurrPos), mABCLine ) ;

	mABCMusic.mHasLyrics = true ;
	
	debugExit("parseFieldWords") ;
	return  Element ;
}

}