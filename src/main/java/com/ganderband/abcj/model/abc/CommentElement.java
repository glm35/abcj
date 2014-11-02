/**
 * A class representing a Comment Element.
 */
package  abcj.model.abc ;

public class CommentElement extends ABCElement
{
/**
 * The comment text (without leading %)
 */
	private String  mComment ;


/**
 * Construct an element of a given type.
 */
public  CommentElement( String Comment )
{	super(COMMENT_ELEMENT) ;   mComment = Comment ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "CommentElement >" + mComment + "<" ;   }


/**
 * Format the item as ABC text.
 */
public String  format()
{	return  "%" + mComment ;   }

}
