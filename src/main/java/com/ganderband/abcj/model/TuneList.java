/**
 * This class represents a single tune list (in memory).
 */
package  abcj.model ;

import  java.io.* ;
import  java.util.* ;

public class TuneList
{
/**
 * The file name holding all lists.
 */
	private static final String  LIST_FILENAME = "ABCJ.LST" ;
/**
 * The starter file name holding all lists.
 */
	private static final String  LIST_FILENAME_STARTER = "ABCJ.LST.starter" ;
/**
 * The owning library.
 */
	private Library  mLibrary ;
/**
 * The tune list title.
 */
	private String  mTitle ;
/**
 * A flag to indicate if this tune list has been modified.
 */	
	private boolean   mIsModified = false ;
/**
 * The array of tunes in this list.
 */
	private ArrayList  mTunes = new ArrayList() ;
	
/**
 * The main constructor.
 */
public  TuneList( Library Lib, String Title )
{	mLibrary = Lib ;   mTitle = Title ;   }


/**
 * Get the title of the list.
 */
public String  getTitle()
{	return  mTitle ;   }


/**
 * Set the title of the list.
 */
public void  setTitle( String Title )
{	mTitle = Title ;   }


/**
 * Get the modified status of the tune list.
 */
public boolean  isModified()
{	return  mIsModified ;   }


/**
 * Get the tunes in the list.
 * 
 * <p>This returns an array of <code>Tune</code>s.
 */
public ArrayList  getTunes()
{	return  mTunes ;   }


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
 * Add a new tune to the tune list.
 * 
 * <p>This is only used when initially building the ists
 */
public void  addTune( Tune Tune )
{	mTunes.add(Tune) ;   }


/**
 * Remove a book from this tune list.
 */
public void  removeTuneBook( TuneBook Book )
{
	boolean  Changed = false ;
	ArrayList  NewArray = new ArrayList() ;
	
//  Build a new array containing without the relevant book

	for ( int i = 0 ; i < mTunes.size() ; i++ ) {
		Tune  t = (Tune) mTunes.get(i) ;
		if ( t.getTuneBook() == Book )   Changed = true ;
		else							 NewArray.add(t) ;
	}
	
//  If changed then adjust accordingly

	if ( Changed ) {
		mTunes = NewArray ;
		setModified(true) ;
	}
}


/**
 * Remove a tune from this tune list.
 */
public void  removeTune( Tune Tune )
{
	boolean  Changed = false ;
	ArrayList  NewArray = new ArrayList() ;
	
//	Build a new array containing without the relevant book

	for ( int i = 0 ; i < mTunes.size() ; i++ ) {
		Tune  t = (Tune) mTunes.get(i) ;
		if ( t == Tune )   Changed = true ;
		else			   NewArray.add(t) ;
	}
	
//	If changed then adjust accordingly

	if ( Changed ) {
		mTunes = NewArray ;
		setModified(true) ;
	}
}


/**
 * Add a set of tunes into the tune list.
 * 
 * <p>This is used when dragging and dropping.
 */
public void  addTunes( ArrayList NewTunes, Tune AfterTune, boolean AddAtEnd )
{

//  Locate the position where insertion will start

	int  InsertPos = 0 ;
	if ( AddAtEnd )   InsertPos = mTunes.size() ;
	
	if ( AfterTune != null ) {
		for ( int i = 0 ; i < mTunes.size() ; i++ ) {
			if ( mTunes.get(i) == AfterTune ) {
				InsertPos = i + 1 ;
				break ;
			}
		}
	}
	
//  Handle all items in the list and insert tunes at the correct place

	for ( int i = 0 ; i < NewTunes.size() ; i++ )
		if ( NewTunes.get(i) instanceof Tune )
			mTunes.add( InsertPos++, NewTunes.get(i) ) ;
			
//  Finally indicate the tree has been modified and return
	
	setModified(true) ;
}

/**
 * A static method to initialize all lists.
 */
public static ArrayList  initializeLists( Library Lib )
{
	ArrayList  Lists = new ArrayList() ;
	
	TuneList  CurrList = null ;
	
	try {

	//  Determine which file name to load.
	//  If ABCJ.PRO does not exist then load from a starter file ABCJ.PRO.starter
		
		String  FileName = LIST_FILENAME ;
		
		if ( ! ( new File(LIST_FILENAME) ).exists() )
			FileName = LIST_FILENAME_STARTER ;

	//  Open the file and check for existence

		BufferedReader  Rdr = new BufferedReader( new FileReader(FileName) ) ;

	//  Read lines from the file until no more

		while ( true ) {
			String  Line = Rdr.readLine() ;
			if ( Line == null )   break ;

		//  Handle an individual line (First char L = List, First char T = tune

			switch ( Line.charAt(0) ) {

			//  Handle a list name line.
			//  The remainder of the line is the list title and should be
			//  used to create a new list.

			case  'L' :
				CurrList = new TuneList( Lib, Line.substring(1) ) ;
				Lists.add(CurrList) ;
				break ;
				
			//  Handle a tune list name item.
			//  The remainder of the line is the book file name and the tune
			//  index (separated by a single blank)   These should be used to 
			//  add a new tune to a tune list.
				
			case  'T' :
				int  Split = Line.indexOf(" ") ;
				String  BookFile = Line.substring( 1, Split ) ;
				String  Index    = Line.substring( Split + 1 ) ;
				
				TuneBook  Book = Lib.locateBook(BookFile) ;
				if ( Book != null ) {
					Tune  Tune = Book.locateTune(Index) ;
					if ( Tune != null )
						CurrList.addTune(Tune) ;
					else
						System.out.println( "List - Tune Not Found - "
										  + BookFile +" " + Index );
				}
				
				break ;
			}
		}

	//  End of file, close and return
		
		Rdr.close() ;
	}
	
//  Ignore file not found as this is OK
	
	catch ( FileNotFoundException e ) {
	//  Ignore this exception
	}
	
//  If any other errors, return list so far built (particularly not found).

	catch ( Exception e ) {
		System.out.println( "TuneList.initializeLists failed with "+ e ) ;
		e.printStackTrace(System.out) ;
	}
	
	return  Lists ;
}


/**
 * A static method to save all lists.
 */
public static void  saveLists( ArrayList Lists )
{
	try {
	
	//  Open the file and check for existence

		BufferedWriter  Wtr = new BufferedWriter( new FileWriter(LIST_FILENAME) ) ;
	
	//  Loop through and write all lists
	
		for ( int i = 0 ; i < Lists.size() ; i++ ) {
			TuneList  List = (TuneList) Lists.get(i) ;
			
			Wtr.write( "L" + List.mTitle ) ;
			Wtr.newLine() ;
			
			for ( int j = 0 ; j < List.mTunes.size() ; j++ ) {
				Tune  t = (Tune) List.mTunes.get(j) ;
				Wtr.write( "T" + t.getTuneBook().getFileName() + " " + t.getIndex() ) ;
				Wtr.newLine() ;
			}
		}
		
		Wtr.close() ;
	}
	
//	Catch any errors

	catch ( Exception e ) {
		System.out.println("Error saving lists");	
	}
}

}