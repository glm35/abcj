/**
 * An element representing a single line of music.
 */
package  abcj.model.abc ;

import  java.util.* ;
import  abcj.util.* ;

public class MusicLineElement extends ABCElement
{
/**
 * An array list containing all elements in the line of music.
 */
	private ArrayList  mElements = new ArrayList() ;
/**
 * The text position of the comment.
 */
	private int  mCommentPos ;
/**
 * The field comment.
 */
	private String  mComment ;
/**
 * The break character for this line.
 */
	private String  mBreak ;

/**
 * A flag to indicate if this instance is completed.
 * 
 * <p>This indicates that the item has changed but has not been updated.
 */
	private boolean  mIsCompleted = false ;

	
/**
 * The standard constructor.
 */
public  MusicLineElement( String Break )
{
	super(MUSIC_LINE_ELEMENT) ;
	mBreak = Break ;
	mIsCompleted = false ;
}


/**
 * Get the line break for this line.
 */
public String  getBreak()
{	return  mBreak ;   }


/**
 * Get the completion status of the element.
 */
public boolean  isCompleted()
{	return  mIsCompleted ;   }


/**
 * Complete the element.
 *
 *<p>This sets the text and flags as complete.
 */
public void  complete()
{
	
//  ??????????? Not currently necessary

	mIsCompleted = true ;
}


/**
 * Add an element to the line.
 */
public void  add( ABCElement e )
{
	if ( ABCParser.isDebugEnabled() )		// Saves time !
		ABCParser.debug( "Line add - " + e ) ;
	
	mElements.add(e) ;
	mIsCompleted = false ;
}
	
	
/**
 * Get the elements in the line.
 */
public ArrayList  getElements()
{	return  mElements ;   }


/**
 * Get the field comment.
 */
public String  getComment()
{	return  mComment ;   }
	
	
/**
 * Set the field comment.
 */
public void  setComment( String Comment )
{	mComment = Comment ;   }
	
	
/**
 * Get the field commment position.
 */
public int  getCommentPos()
{	return  mCommentPos ;   }
	
	
/**
 * Set the field comment position.
 */
public void  setCommentPos( int Pos )
{	mCommentPos = Pos ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "MusicLineElement >" + format() +"<" ;   }


/**
 * Format the item as ABC text.
 */
public String  format()
{
	
//  Do the music part
	
	String  s = formatMusic() ;
	
//  Check for a break character
	
	if ( mBreak != null )   s += " " + mBreak ;
	
//  Add any comments
	
	if ( mComment != null ) {
		if ( mCommentPos < s.length() )
			mCommentPos = s.length() + 2 ;	// Ensure some space
			
		s = Utils.pad( s, mCommentPos ) + "%" + mComment ;
	}
	
	return  s ;
}


/**
 * Format the music part as ABC text (no comments).
 */
protected String  formatMusic()
{
	StringBuffer  s = new StringBuffer(200) ;
	
//  Process each element in there and remove duplicate blanks

	char  LastChar = '?' ;
	ABCElement  LastElement = null ;
	for ( int i = 0 ; i < mElements.size() ; i++ ) {
		
		ABCElement  e = ( (ABCElement) mElements.get(i) ) ;
		String      w = e.format() ;
		
	//  Check for inline field element and add wrapping brackets
		
		if ( e instanceof FieldElement )   w = "[" + w + "] " ;
		
	//  Prevent duplicate blanks
	//  and blanks between bar lines and repeats
	
		if ( w.charAt(0) == ' ' ) {

			if ( LastChar == ' ' )   s.deleteCharAt( s.length() - 1 ) ;
			
			if ( i > 0
			 &&  LastElement.getType() == BARLINE_ELEMENT
		 	 &&  e.getType() == NTHREPEAT_ELEMENT )
		 		w = w.substring(1) ; 
		}
		
	//  Append to the string
		
		s.append(w) ;

		LastChar = s.charAt( s.length() - 1 ) ;
		LastElement = e ; 
	}

//  Remove leading and trailing blanks on return

	return  s.toString().trim() ;
}

}
