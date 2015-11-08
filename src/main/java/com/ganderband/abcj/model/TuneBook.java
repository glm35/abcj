/**
 * This class represents a single tunebook (in memory).
 */
package  com.ganderband.abcj.model ;

import  java.io.* ;
import  java.util.* ;
import  com.ganderband.abcj.* ;

public class TuneBook
{
/**
 * A temporary file name used when writing out updates.
 */
	private static final String  TEMP_FILE = "$$ABCJ$$.ABC" ;
/**
 * The owning library.
 */
	private Library  mLibrary ;
/**
 * The file name associated with this tunebook.
 */
	private String  mFileName ;
/**
 * The title associated with this tunebook.
 */
	private String  mTitle ;
/**
 * An ArrayList of the tunes in this book.
 */
	private ArrayList  mTunes = new ArrayList() ;
/**
 * A flag to indicate if this tunebook has yet been parsed.
 * 
 * <p>This is used to control memory usage and performance.
 */
	private boolean  mIsParsed = false ;
/**
 * A flag to indicate if this tunebook has been modified.
 */	
	private boolean   mIsModified = false ;
/**
 * A flag to indicate if the file was found or not.
 */
	private boolean  mIsFileNotFound = true ;
/**
 * The maximum index in this tune book.
 */
	private int  mMaxIndex = 0 ;
		
		
/**
 * Create a tunebook for a file and title.
 */
public  TuneBook( Library Lib, String FileName, String Title, boolean CreateFlag )
{
	mLibrary = Lib ;
	mFileName = FileName ;   mTitle = Title ;
	
//  If the file does not exists then create a new one

	if ( CreateFlag  &&  ! ( new File(FileName) ).exists() )   create() ;
	
//  Now load the tunebook

	load() ;
}


/**
 * Get the tunebook file name.
 */
public String  getFileName()
{	return  mFileName ;   }


/**
 * Get the tunebook title.
 */
public String  getTitle()
{	return  mTitle ;   }


/**
 * Set the tunebook title.
 */
public void  setTitle( String NewTitle )
{
	if ( NewTitle.trim().equals("") )   NewTitle = null ;
	mTitle = NewTitle ;
}


/**
 * Get the parsed status of the tune book..
 */
public boolean  isParsed()
{	return  mIsParsed ;   }


	/**
	 * Set the modified flag.
	 */
	public void  setModified( boolean Flag )
	{
		mIsModified = Flag ;
	
//	If marked as modified then carry it through to the owning book also.
//	Note that this is not necessarily valid in reverse !

	if ( Flag )   mLibrary.setModified(true) ;
}


/**
 * Get the tunes in the tunebook.
 */
public ArrayList  getTunes()
{	return  mTunes ;   }


/**
 * Get the file not found status. 
 */
public boolean  isFileNotFound() 
{	return  mIsFileNotFound ;   }


/**
 * Create a new ABC file.
 * 
 * <p>This method creates a basic ABC File with a single tune.
 * It is only used. temprarily and the tunebook instance should not
 * remain persistent.
 */
public void  create()
{
	try {
		RandomAccessFile  OutFile = new RandomAccessFile( mFileName, "rw" ) ;
		
	//  Create a dummy tune in the ABC file and save it

		Tune  t = new Tune( null, "1", "New Tune" ) ;
		t.save( null, OutFile ) ;		
		
		OutFile.close() ;
	}
	
//  Catch any exceptions

	catch ( Exception e ) {
		System.out.println("* ERROR* - Unable to create new ABC File") ;
		e.printStackTrace(System.out) ;
	}
}


/**
 * Load tunebook from file.
 * 
 * <p>This does not parse the tunes.   It only extracts the index, title and
 * the location of the tune in the file.
 */
public void  load()
{

//  Open the file and check whether it exists

	RandomAccessFile  InFile ;
	try {
		InFile = new RandomAccessFile( mFileName, "r" ) ;
	}
	catch ( FileNotFoundException e ) {
		mIsFileNotFound = true ;
		return ;
	}
	
	mIsFileNotFound = false ;
	
//  Now read through the file and extract tunes
//  Note that no parsing is done at this point to improve performance

	boolean  LoadingTune = false ;
	long     StartPos    = 0 ;
	long     EndPos      = 0 ;
	String   Index       = null ;
	String   Title       = null ;

	try {
	
		while ( true ) {
			long   FilePos = InFile.getFilePointer() ; 
			String  Line = InFile.readLine() ;
			
			if ( Line == null ) {		//  Check for EOF
				EndPos = FilePos ;
				break ;
			}

			Line = Line.trim() ;

			if ( Line.isEmpty() )
				continue;

		//  Look for start of new tune
	
			if ( ! LoadingTune ) {
				if ( ! Line.equals("") ) {
					LoadingTune = true ;
					StartPos = EndPos = FilePos ;
					Title = Index = null ;
				}
			}
		
		//  Look for end of existing tune
	
			if ( LoadingTune ) {
				
				if ( Line.equals("") ) {
					LoadingTune = false ;
					EndPos = FilePos ;
					
				//  Store the tune
				
					mTunes.add( new Tune( this, Index, Title, StartPos, EndPos ) ) ;
				}
				
			//  Check for tune title and index then store
				
				else {
					if ( Index == null   &&  Line.substring( 0, 2 ).equals("X:") ) {
					 	Index = Line.substring(2).trim() ;
		
						try {		// Keep maximum index value
							int  i = Integer.parseInt(Index) ;
							if ( i > mMaxIndex )   mMaxIndex = i ;
						}
						catch ( Exception e )   {
							System.out.println( "Exception ignored - " + e ) ;
							e.printStackTrace() ;
						}
					 	
					}
				
					if ( Title == null   &&  Line.substring( 0, 2 ).equals("T:") )
						Title = Line.substring(2).trim() ;
				}				
			}
		}	
	}
	catch ( IOException e ) {
		System.out.println("*ERROR* - Cannot load tunebook") ;
		e.printStackTrace(System.out) ;
	}

//  Check for any remaining tune at EOF

	if ( LoadingTune ) {
		
	//  Store the tune and update the maximum index

		mTunes.add( new Tune( this, Index, Title, StartPos, EndPos ) ) ;
	}
	
//  Finally close and exit

	try {   InFile.close() ;   }
	catch ( IOException e )   {
		System.out.println( "Exception ignored - " + e ) ;
		e.printStackTrace() ;
	}
}


/**
 * Save the whole tunebook back to file.
 */
public void  save()
{
	if ( ! mIsModified )   return ;

//  Construct a temporary file name and make sure it does not exist

	File  ThisFile = new File(mFileName) ;
	File  TempFile = new File( ThisFile.getParentFile(), TEMP_FILE ) ;

	TempFile.delete() ;

	try {
		
	//  Open the input and output files
	
		RandomAccessFile  InFile  = new RandomAccessFile( ThisFile, "r" ) ;
		RandomAccessFile  OutFile = new RandomAccessFile( TempFile, "rw" ) ;

	//  Process each tune one at a time and write back to file
	
		for ( int i = 0 ; i < mTunes.size() ; i++ )
			( (Tune) mTunes.get(i) ).save( InFile, OutFile ) ;		

		OutFile.close() ;		
		InFile.close() ;
		
	//  Finally rename the files accordingly
	
		ThisFile.delete() ;
		TempFile.renameTo(ThisFile) ;		
	}
	
//  Catch any exceptions	

	catch ( Exception e ) {
		System.out.println("*ERROR* - File save failed") ;
		e.printStackTrace(System.out) ;
	}
		
//  Reset the modified flag when done

	setModified(false) ; 
}


/**
 * Create and add a new tune to this tune book.
 */
public Tune  createNewTune()
{
		
//  Create a dummy tune in the ABC file with an unused index and
//  set the ABC Text to the correct skeleton.

	Tune  t = new Tune( this, Integer.toString(++mMaxIndex), "New Tune" ) ;
	mTunes.add(t) ;
	
	setModified(true) ;
	
	return  t ;
}


/**
 * Remove the given tune from the tune book.
 */
public void  removeTune( Tune Tune )
{
	mTunes.remove(Tune) ;
	setModified(true) ;
}


/**
 * Search the tunebook for tunes.
 */
public void  search( SearchRequest Request, TuneSearchListener Target )
{
	try {
		
		if ( ! mIsFileNotFound ) {
		
			//  Open the tunebook file in advance to improve performance
		 
			RandomAccessFile  InFile  = new RandomAccessFile( mFileName, "r" ) ;

			//	Search all tunes in the library

			for ( int i = 0 ; i < mTunes.size() ; i++ )
				( (Tune) mTunes.get(i) ).search( Request, Target, InFile ) ;

			InFile.close() ;
		}
	}
	
//  Catch any exceptions

	catch ( Exception e ) {
		System.out.println("*ERROR* - Search failed") ;
		e.printStackTrace(System.out) ;
	}
}  


/**
 * Locate a tune by index.
 */
public Tune  locateTune( String Index )
{
	Index = Index.trim() ;
	
//	Search through all tunes until found

	for ( int i = 0 ; i < mTunes.size() ; i++ ) {
		Tune  Tune = ( (Tune) mTunes.get(i) ) ;
		if ( Tune.getIndex().equals(Index) )
			return  Tune ; 
	}
	
	return  null ;		// Not found
}

}
