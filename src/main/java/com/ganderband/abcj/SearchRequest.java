/**
 * A class representing a parsed search request.
 */
package  abcj ;

import  java.util.* ;

public class SearchRequest
{
/**
 * The array of base words.
 */
	private String[]  mBaseWords ;
/**
 * An array indicating if a prefix is allowed.
 */
	private boolean[]  mPrefixAllowed ;
/**
 * An array indicating if a suffix is allowed.
 */
	private boolean[]  mSuffixAllowed ;


/**
 * Construct an instance from a text string.
 */
public  SearchRequest( String Text )
{
	Text = Text.trim() ;

	ArrayList  v = new ArrayList() ;
	
//  Parse the text string into individual words and store in an array list

	while ( true ) {
		int  Index = Text.indexOf(' ') ;
		if ( Index < 0 )   break ;
		
		v.add( Text.substring( 0, Index ) ) ;
		Text = Text.substring( Index + 1 ).trim() ;
	}
	
	if ( Text.length() > 0 )   v.add(Text) ;
	
//  Now we know how many there are we can allocate arrays to improve performance

	mBaseWords = new String[ v.size() ] ;
	mPrefixAllowed = new boolean [ v.size() ] ;
	mSuffixAllowed = new boolean [ v.size() ] ;
	
//  Now store each word separately

	for ( int i = 0 ; i < v.size() ; i++ )
		storeWord( (String) v.get(i), i ) ;
}


/**
 * Parse and store an individual word.
 */
private void  storeWord( String Word, int Index )
{

//  Determine if a prefix is allowed

	if ( Word.charAt(0) == '*' ) {
		mPrefixAllowed[Index] = true ;
		Word = Word.substring(1) ;
	}
	
//	Determine if a suffix is allowed

	if ( Word.length() > 0  &&  Word.charAt( Word.length() - 1 ) == '*' ) {
		mSuffixAllowed[Index] = true ;
		Word = Word.substring( 0, Word.length() - 1 ) ;
	}
	
//  Finally store the base word

	mBaseWords[Index] = Word.toLowerCase() ;
}


/**
 * Get the number of items in the request.
 */
public int  getItemCount()
{	return  mBaseWords.length ;   }


/**
 * Get the base word at the given index.
 */
public String  getBaseWord( int Index )
{	return  mBaseWords[Index] ;   }


/**
 * Get the prefix allowed at the given index.
 */
public boolean  getPrefixAllowed( int Index )
{	return  mPrefixAllowed[Index] ;   }


/**
 * Get the suffix allowed at the given index.
 */
public boolean  getSuffixAllowed( int Index )
{	return  mSuffixAllowed[Index] ;   }

}
