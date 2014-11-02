/**
 * This class represents the complete tune library (in memory).
 */
package  abcj.model ;

import  java.util.* ;
import  abcj.* ;

public class Library
{
/**
 * An ArrayList of the tunebooks.
 */
	private ArrayList  mTuneBooks ;
/**
 * An ArrayList of tune lists.
 */
	private ArrayList  mTuneLists ;
/**
 * A flag to indicate if this library has been modified.
 */	
	private boolean   mIsModified = false ;


/**
 * Initialize the library from the properties library entries.
 */
public void  initialize( ArrayList PropsLib )
{
	initializeTuneBooks(PropsLib) ;
	initializeTuneLists() ;
}


/**
 * Initialize the library tunebooks from the properties library entries.
 */
public void  initializeTuneBooks( ArrayList PropsLib )
{
	mTuneBooks = new ArrayList() ;
	
	ArrayList  x = ABCJProperties.getLibrary() ;
	for ( int i = 0 ; i < x.size() ; i++ ) {
		ABCJProperties.LibraryEntry  e = (ABCJProperties.LibraryEntry)
					x.get(i) ;
		mTuneBooks.add( new TuneBook( this, e.mFileName, e.mTitle, false ) ) ;
	}
}


/**
 * Initialize the library tune lists.
 */
public void  initializeTuneLists()
{
	
//  All tune lists are stored in a single file.   Use a static method in
//  the TuneList class to build the array appropriately. 
	
	mTuneLists = TuneList.initializeLists(this) ;
}


/**
 * Get the tunebooks array.
 */
public ArrayList  getTuneBooks()
{	return  mTuneBooks ;   }


/**
 * Get the tune lists array.
 */
public ArrayList  getTuneLists()
{	return  mTuneLists ;   }


/**
 * Set the modified flag.
 */
public void  setModified( boolean Flag )
{	mIsModified = Flag ;   }


/**
 * Get the modified flag.
 */
public boolean  isModified()
{	return  mIsModified ;   }


/**
 * Save the whole library back to file.
 */
public void  save()
{
	
	TuneList.saveLists(mTuneLists) ;
	
	if ( ! mIsModified )   return ;
	
//  Save all tune books

	for ( int i = 0 ; i < mTuneBooks.size() ; i++ )
		( (TuneBook) mTuneBooks.get(i) ).save() ;
		
//  Save all lists

	TuneList.saveLists(mTuneLists) ;
		
//  Reset the modified flag when done

	setModified(false) ; 
}


/**
 * Add a new tune book to the library.
 */
public TuneBook  addTuneBook( String FileName, boolean CreateFlag )
{

//  First of all we should check that the tune book does not yet exist

	for ( int i = 0 ; i < mTuneBooks.size() ; i++ ) {
		String  f = ( (TuneBook) mTuneBooks.get(i) ).getFileName() ;
		if ( FileName.equalsIgnoreCase(f) )   return  null ;
	}

//  Create a new tune book instance and add it to the library and properties file

	TuneBook  Book = new TuneBook( this, FileName, null, CreateFlag ) ;
	mTuneBooks.add(Book) ;
	ABCJProperties.addLibraryEntry( FileName, null ) ;
	
	return  Book ;
}


/**
 * Create a new tune book and ABC file and add it to the library.
 */
public TuneBook  createTuneBook( String FileName )
{	return  addTuneBook( FileName, true ) ;   }


/**
 * Delete a tune book from the library.
 */
public void  removeTuneBook( TuneBook Book )
{
	String  FileName = Book.getFileName() ;

//  File located, delete it and update the properties file
		
	mTuneBooks.remove(Book) ;
	ABCJProperties.removeLibraryEntry(FileName) ;
	
//  Also remove it from any lists

	for ( int i = 0 ; i < mTuneLists.size() ; i++ )
		( (TuneList) mTuneLists.get(i) ).removeTuneBook(Book) ;
}


/**
 * Delete a tune from the library.
 */
public void  removeTune( Tune Tune )
{
	
//  First of all, remove the tune from the book

	Tune.getTuneBook().removeTune(Tune) ;

//  Now remove the tune from any lists

	for ( int i = 0 ; i < mTuneLists.size() ; i++ )
		( (TuneList) mTuneLists.get(i) ).removeTune(Tune) ;
}

/**
 * A book title has been changed.
 * 
 * <p>This will cause the properties to be updated accordingly.
 */
public void  changeTuneBookTitle( TuneBook Book )
{
	ABCJProperties.changeLibraryEntry( Book.getFileName(), Book.getTitle() ) ;
}


/**
 * Search the library for tunes.
 */
public void  search( SearchRequest Request, TuneSearchListener Target )
{

//  Search all books in the library

	for ( int i = 0 ; i < mTuneBooks.size() ; i++ )
		( (TuneBook) mTuneBooks.get(i) ).search( Request, Target ) ;
}  


/**
 * Locate a tune book by file name.
 */
public TuneBook  locateBook( String FileName )
{

//  Search through all tune books until found

	for ( int i = 0 ; i < mTuneBooks.size() ; i++ ) {
		TuneBook  Book = ( (TuneBook) mTuneBooks.get(i) ) ;
		if ( Book.getFileName().equals(FileName) )
			return  Book ; 
	}
	
	return  null ;		// Not found
}


/**
 * Create a new tune list.
 */
public TuneList  createTuneList()
{
	TuneList  List = new TuneList( this, "Tune List" ) ;
	mTuneLists.add(List) ;
	setModified(true) ;
	return  List ;
}


/**
 * Remove a tune list.
 */
public void  removeTuneList( TuneList List )
{
	mTuneLists.remove(List) ;
	setModified(true) ;
}

}