/**
 * A class representing all the ABC Music information.
 */
package  com.ganderband.abcj.model.abc ;

import  java.util.* ;

public class ABCMusic implements ABCParserConstants
{
/**
 * An array list containing all elements in the music.
 */
	ArrayList  mElements = new ArrayList() ;
/**
 * The root element of the linked list.
 */
	ABCElement  mRootElement = null ;
/**
 * A flag indicating if the music has chords.
 */
	boolean  mHasChords = false ;
/**
 * A flag indicating if the music has lyrics.
 */
	boolean  mHasLyrics = false ;
	

/**
 * Get whether the music uses chords or not.
 */
public boolean  hasChords()
{	return  mHasChords ;   }


/**
 * Get whether the music uses lyrics or not.
 */
public boolean  hasLyrics()
{	return  mHasLyrics ;   }


/**
 * Add an element to the music.
 */
public void  add( ABCElement e )
{
	mElements.add(e) ;
	if ( ABCParser.isDebugEnabled() )		// Saves time !
		ABCParser.debug( "Music add - " + e ) ;
}


/**
 * Get the elements array in the music.
 */
public ArrayList  getElements()
{	return  mElements ;   }


/**
 * Get the first element in the linked music list.
 */
public ABCElement  getRootElement()
{	return  mRootElement ;   }


/**
 * Format the music back to ABC text.
 */
public String  format()
{
	
//  Format each element on its own as a separate line
	
	String  Result = "" ;
	for ( int i = 0 ; i < mElements.size() ; i++ )
		Result += ( (ABCElement) mElements.get(i) ).format() + "\n" ;
	
//  Return the result
	
	return  Result ;
}

}
