/**
 * A class representing a single tune. 
 */
package  abcj.model ;

import  java.io.* ;
import  java.text.* ;
import  abcj.* ;

public class Tune
{
/**
 * Skeleton ABC text for a single tune.
 */
	public static final String  ABC_TUNE_SKELETON = "X:{0}\n"
												  + "T:{1}\n"
												  + "K:C\n"
												  + "abc |]\n" ;
/**
 * The tune book this tune belongs to.
 */
	private TuneBook  mBook ;
/**
 * The index of this tune extracted from the ABC for easy access.
 */
	private String  mIndex ;
/**
 * The title of this tune extracted from the ABC for easy access.
 */
	private String  mTitle ;
/**
 * The starting position of the tune in the ABC file.
 * 
 * <p>This value will be -1 of the tune was not read from a file.
 */
	private long  mStartPos ;
/**
 * The ending position of the tune in the ABC file.
 * 
 * <p>This value will be -1 of the tune was not read from a file.
 */
	private long  mEndPos ;
/**
 * The loaded ABC text.
 */
	private String  mABCText ;
/**
 * A flag to indicate if this tune has been fully parsed yet.
 */
	private boolean  mIsParsed = false ;
/**
 * A flag to indicate if this tune has been changed.
 */
	private boolean  mIsModified = false ;
/**
 * A flag to indicate if this tune has any errors.
 */
	private boolean  mIsInError = false ;


/**
 * Construct and instance for a tune already in an ABC File.
 */
public  Tune( TuneBook Book, String Index, String Title, long StartPos, long EndPos )
{
	mBook     = Book ;
	mIndex    = Index ;
	mTitle    = Title ;
	mStartPos = StartPos ;
	mEndPos   = EndPos ;
}


/**
 * Construct an instance for a brand new tune (not yet saved).
 */
public  Tune( TuneBook Book, String Index, String Title )
{
	mBook     = Book ;
	mIndex    = Index ;
	mTitle    = Title ;
	mStartPos = mEndPos = -1 ;
	
//  Setup the initial text as the ABC tune skeleton.
//  Note that we plug in the required index

	String  s = MessageFormat.format( ABC_TUNE_SKELETON,
									  new Object[] { Index, Title } ) ;
	setABCText(s) ;
}


/**
 * Get the tune book of the tune.
 */
public TuneBook  getTuneBook()
{	return  mBook ;   }


/**
 * Get the index of the tune.
 */
public String  getIndex()
{	if ( mIndex == null )   return  "" ;
	return  mIndex.trim() ;
}


/**
 * Get the title of the tune.
 */
public String  getTitle()
{	return  mTitle ;   }


/**
 * Set the modified flag.
 */
public void  setModified( boolean Flag )
{
	mIsModified = Flag ;
	
//  If marked as modified then carry it through to the owning book also.
//  Note that this is not necessarily valid in reverse !

	if ( Flag  &&  mBook != null )   mBook.setModified(true) ;
}


/**
 * Get the error status of the tune.
 */
public boolean  isInError()
{	return  mIsInError ;   }


/**
 * Get the parsed status of the tune.
 */
public boolean  isParsed()
{	return  mIsParsed ;   }


/**
 * Get the error status of the tune.
 */
public boolean  isModified()
{	return  mIsModified ;   }


/**
 * Get the ABC Text for this tune.
 * 
 * <p>If not yet loaded then it will be retrieved from the file.
 */
public String  getABCText()
{
	if ( mABCText == null )   loadABCText() ;
	return  mABCText ;
}


/**
 * Set the ABC Text for this tune.
 */
public void  setABCText( String Text )
{
	mABCText = Text ;
	setModified(true) ;
	
//  Extract and store the index and title in case they've been changed

	mIndex = extractField( "X:", Text ) ;
	mTitle = extractField( "T:", Text ) ;
}


/**
 * Load the ABC text for this tune from file.
 * 
 * <p>In this case the input file is expected to be open.
 */

private void  loadABCText()
{
	try {
		
	//  Open the input file and load the text from there

		RandomAccessFile  InFile = new RandomAccessFile( mBook.getFileName(), "r" ) ;
		loadABCText(InFile) ;
		InFile.close() ;	
	}
	
//  Catch any exceptions and log as error

	catch ( IOException e ) {
		System.out.println("*ERROR* - Cannot load tune text") ;
		e.printStackTrace(System.out) ;
	}
}


/**
 * Load the ABC text for this tune from file.
 * 
 * <p>In this case the input file is expected to be open.
 */

private void  loadABCText( RandomAccessFile InFile )
{
	try {
		
	//	Position at correct point and load

		InFile.seek(mStartPos) ;
	
	//  Read until we are past the end position or at EOF
		
		mABCText = "" ;
		while ( InFile.getFilePointer() < mEndPos )
			mABCText += InFile.readLine() + "\n" ;
	}
	
//	Catch any exceptions and log as error

	catch ( IOException e ) {
		System.out.println("*ERROR* - Cannot load tune text") ;
		e.printStackTrace(System.out) ;
	}
}


/**
 *  Save this tune to an output file.
 * 
 * <p>Note that the input file has been pre-opened to save time
 * when many tunes are being written.
 */
public void  save( RandomAccessFile InFile, RandomAccessFile OutFile )
{
	
//  Ensure we have the data

	if ( mABCText == null )   loadABCText(InFile) ;
	String  s = mABCText ;
	
//  Initialise

	long    NewStartPos = 0 ;
	long    NewEndPos   = 0 ;
	String  EndOfLine   = System.getProperty("line.separator") ;
	
	try {
		NewStartPos = OutFile.getFilePointer() ;

	//  Break into lines and write so the correct EOL character gets written

		while ( true ) {
			int  Pos = s.indexOf('\n') ;
			if ( Pos < 0 )   break ;
		
			OutFile.writeBytes( s.substring( 0, Pos ) + EndOfLine ) ;
			s = s.substring(Pos + 1) ;
		}

		if ( ! s.equals("") )
			OutFile.writeBytes( s + EndOfLine ) ;

		NewEndPos = OutFile.getFilePointer() ;
			
		OutFile.writeBytes(EndOfLine) ;
	}
	
//  Catch and report any exceptions

	catch ( Exception e ) {
		System.out.println("*ERROR* - Failed to save tune") ;
		e.printStackTrace(System.out) ;
	}

//  Store the new start and end positions and clear the text field

	mStartPos = NewStartPos ;
	mEndPos   = NewEndPos ;
	mABCText  = null ;
	
	setModified(false) ;
}


/**
 * Search the tune.
 */
public boolean  search( SearchRequest Request, TuneSearchListener Target,
						RandomAccessFile InFile )
{
	
//  If the ABC text already exists then use it, if not, then load from file

	String  Text = mABCText ;
	if ( Text == null ) {
		loadABCText(InFile) ;
		Text = mABCText ;
		mABCText = null ;
	}

	Text = Text.toLowerCase() ;
	
//  Search this text for each keyword

	for ( int WordIndex = 0 ; WordIndex < Request.getItemCount() ; WordIndex++ ) {
		
		String  SearchWord = Request.getBaseWord(WordIndex) ;
		int     Length     = SearchWord.length() ; 
		
	//  Check each occurrence of the word
	
		boolean  Found = false ;
	
		int Index = 0 ;
		while ( true ) {
			Index = Text.indexOf( SearchWord, Index ) ;
			if ( Index < 0 )   break ;
			
		//  Check prefix and move on to next occurrence if not valid

			if ( ! Request.getPrefixAllowed(WordIndex)  &&  Index > 0 ) {
				if ( Character.isLetterOrDigit( Text.charAt(Index - 1) ) ) {
					Index += Length ;
					continue ;		//  Invalid - Move on to next occurrence
				}
			}

		//  Check suffix and move on to next occurrence if not valid

			if ( ! Request.getSuffixAllowed(WordIndex)
			   &&  Index + Length < Text.length() ) {
				if ( Character.isLetterOrDigit( Text.charAt(Index + Length) ) ) {
					Index += Length ;
					continue ;		//  Invalid - Move on to next occurrence
				}
			}

		//  Word match found - flag and terminate the loop		

			Found = true ;
			break ;
		}
		
	//  Word was not matched - return false
	//  If it was found then we continue on to the next request word
	
		if ( ! Found )   return  false ;
	}
	
//  Complete match - call listener and return

	Target.tuneFound(this) ;
	
	return  true ;	
}


/**
 * Extract a field from the ABC Text.
 */
public String  extractField( String Type, String Text )
{
	int  StartPos = Text.indexOf(Type) ;
	if ( StartPos < 0 )   return  null ;

	int  EndPos = Text.indexOf( "\n", StartPos ) ;
	if ( EndPos < 0 )   EndPos = Text.length() ;
	
	return  Text.substring( StartPos + Type.length(), EndPos ) ;
}

}
