/**
 * A thread to manage parsing of ABC strings.
 * 
 * <p>Parsing is done asynchronously on a separate thread so the GUI remains 
 * responsive.   Even though the parser is efficient (approx 15ms to parse a
 * standard tune), the GUI may be impacted otherwise.
 */
package  abcj ;

import  java.util.* ;
import  javax.swing.* ;
import  abcj.model.abc.* ;
import  abcj.model.music.* ;

public class ParserThread extends Thread implements Runnable
{
/**
 * The owning application.
 * 
 * <p>This is package scope to prevent 'synthetic accessor' warnings in inner classes.
 */
	ABCJ  mABCJ ;
/**
 * The timer tick to use when idling (in milliseconds).
 */
	private static final int  TIMER_TICK = 200 ;	// Milliseconds
/**
 * The parser instance used to do the parsing.
 */
	private ABCParser  mParser ;
/**
 * A flag indicating that parsing is needed. 
 */
	private boolean  mParseNeeded = false ;
/**
 * The text to wating to be parsed.
 */
	private String  mWaitingABCText ;
/**
 * A flag indicating that the current tune has changed.
 * 
 * <p>If parsing is in progress then the results will no longer be valid. 
 */
	private boolean  mTuneChanged = false ;
/**
 * A flag to indicate if the parser thread is paused.
 */
	private boolean  mIsPaused = false ;
/**
 * A flag used to request a pause.
 */
	private boolean  mRequestPause = false ;
/**
 * The music builder instance to use for constructing the musical glyphs.
 */
	private MusicBuilder  mMusicBuilder ;


/**
 * The standard constructor.
 */
public  ParserThread( ABCJ App, ABCParser Parser, MusicBuilder Builder )
{	mABCJ = App ;   mParser = Parser ;   mMusicBuilder = Builder ;   }


/**
 * Set thread paused status.
 * 
 * <p>This method will wait for the pause to actually complete before continuing.
 */
public void  setPaused( boolean Flag )
{
	if ( Flag == mIsPaused )   return ;

//  If pausing then we must wait for the pause to happen

	if ( Flag ) {
		mRequestPause = true ;
		while ( ! mIsPaused ) {
			try { sleep(30) ; }
			catch ( Exception e ) {
				System.out.println( "Exception ignored - " + e ) ;
				e.printStackTrace() ;
			}
		}
	}
	
//  Finally set the flag and return

	mIsPaused = Flag ;
}

/**
 * Indicate that parsing is needed.
 */
public synchronized void  queueParse( String ABCText )
{	mParseNeeded = true ;    mWaitingABCText = ABCText ;   }


/**
 * Indicate the the current tune has changed.
 * 
 * <p>Any current results are no longer valid.
 */
public synchronized void  queueTuneChanged( String ABCText )
{
	clear() ;		// Wait for parser to clear before continuing
	
	mTuneChanged = true ;
	if ( ABCText != null )   queueParse(ABCText) ;
}


/**
 * Clear any results and set to empty.
 */
public void  clear()
{
	
//  Tell the parser to stop and wait for it to do this

	mParser.clear() ;
}


/**
 * Get the status of the parser.
 */
public int  getStatus()
{	return  mParser.getStatus() ;   }


/**
 * The run method for this thread.
 */
public void  run()
{
	try {
		
	//  Loop until exit requested
	
		while ( true ) {
			
		//  Check for a requested pause
		
			if ( mRequestPause ) {
				mRequestPause = false ;
				mIsPaused = true ;
			}
			
		//  If paused, then just wait a while and go round the loop
		
			if ( mIsPaused ) {
				sleep(TIMER_TICK) ;
				continue ;
			}
		
		//  Check if anything needs parsing.
		//  Note that the checks and sets need doing atomically in case
		//  anything changes whilst it happens

			boolean  DoParse ;
			String   ParseText = null ;
					
			synchronized ( this ) {
				DoParse = mParseNeeded ;
				if ( DoParse ) {
					mParseNeeded = mTuneChanged = false ;
					ParseText = new String(mWaitingABCText) ;
				}
			}
			
		//  Now do the parsing
		
			if ( DoParse ) {

			   	mParser.parse(ParseText) ;
			   	mMusicBuilder.build( mParser.getABCHeader(), mParser.getABCMusic() ) ;

			//  Check if a different tune has been selected in the meantime
			
				if ( mTuneChanged ) {
					mTuneChanged = false ;
					mParser.clear() ;
					// .... Tell the GUI the current results are invalid
					sendParserInfo("IParsing in Progress - Please Wait ...") ;
					continue ;		// Retry - don't wait
				}
				
			// Tell GUI of the new valid results
			
				sendParserLog() ;
				sendBuilderInfo() ;
			}
			
		//  Nothing to parse but tune has changed so we must clear any results 
			
			else if ( mTuneChanged ) {
				mTuneChanged = false ;
				mParser.clear() ;
				// .... Tell the GUI results are invalid (empty)
				sendParserInfo("INothing to Parse") ;
			}
	
	
		//  If nothing to parse then wait a short while before trying again
		
			else
				sleep(TIMER_TICK) ;
		}
	}
	
//  Ignore an interrupted exception as this may have been caused when running as an applet
	
	catch ( InterruptedException e ) {
//		System.out.println("*** Parser thread has been interrupted ***") ;
	}
}


/**
 * Send the parser log back to the GUI. 
 */
private void  sendParserLog()
{
	ArrayList  Arr = mParser.getLog() ;
	String[]  s = new String[ Arr.size() ] ;
	
	for ( int i = 0 ; i < Arr.size() ; i++ )
		s[i] = Arr.get(i).toString() ;
	
	sendParserInfo(s) ;
}


/**
 * Send a string array back to the GUI.
 * 
 * <p>This indicates the results of the parse. 
 */
private void  sendParserInfo( String[] Text )
{
	
//  Clone the string array as final so we can send it.

	final String[]  LocalText = Text ;
	final int       Status    = mParser.getStatus() ;
	
//  Use invokeLater so it gets done on the main GUI thread

	SwingUtilities.invokeLater( 
				new Runnable() {
					public void run()
					{   mABCJ.receiveParserInfo( LocalText, Status ) ;   }
				} ) ;
}

private void  sendParserInfo( String Text )
{	sendParserInfo( new String[] { Text } ) ;   }


/**
 * Send a string array back to the GUI.
 * 
 * <p>This indicates the results of the build. 
 */
private void  sendBuilderInfo()
{
	
//	Clone the status as final so we can send it.

	final int  Status = mMusicBuilder.getStatus() ;
	
//	Use invokeLater so it gets done on the main GUI thread

	SwingUtilities.invokeLater( 
				new Runnable() {
					public void run()
					{   mABCJ.receiveBuilderInfo(Status) ;   }
				} ) ;
}

}
