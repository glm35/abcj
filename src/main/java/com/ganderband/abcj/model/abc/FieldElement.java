/**
 * An abstract class representing a Field Element.
 * 
 * Certain special fields (e.g. tempo) will subclass this to provide
 * extra function.
 */
package  abcj.model.abc ;

import  abcj.util.* ;

public abstract class FieldElement extends ABCElement
{
/**
 * The field type.
 */
	private char  mFieldType ;
/**
 * The text position of the comment.
 */
	private int  mCommentPos ;
/**
 * The field comment.
 */
	private String  mComment ;
/**
 * Is this an inline field or not.
 */
	protected boolean  mInline = false ;
	
	
/**
 * Construct an element of a given type.
 */
public  FieldElement( char FieldType )
{
	super(FIELD_ELEMENT) ;
	mFieldType = FieldType ;
	mCommentPos = 0 ;
	mComment = null ;
}
	
	
/**
 * Get the field type.
 */
public char  getFieldType()
{	return  mFieldType ;   }
	
	
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
 * Set the inline field status.
 */
public void  setInline( boolean b )
{	mInline = b ;   }


/**
 * Get the inline field status.
 */
public boolean  isInline()
{	return  mInline ;   }


/**
 * Format the item as ABC text.
 */
public String  format()
{
	String  s = mFieldType + ":" + formatField() ;
	
	if ( mComment != null ) {
		if ( mCommentPos < s.length() )
			mCommentPos = s.length() + 2 ;	// Ensure some space
			
		s = Utils.pad( s, mCommentPos ) + "%" + mComment ;
	}
	
	return  s ;
}


/**
 * Format the field item as ABC text (no comments or type + colol).
 */
public abstract String  formatField() ;

}
