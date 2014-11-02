/**
 * A class representing an Index Field Element.
 */
package  abcj.model.abc ;

public class IndexFieldElement extends FieldElement
{
/**
 * The index.
 */
	private String  mIndex ;


/**
 * Construct from a string.
 */
public  IndexFieldElement( String Index )
{	super('X') ;   setType(INDEX_FIELD_ELEMENT) ;   mIndex = Index ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "IndexFieldElement >" + formatField() + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{	return  mIndex ;   }

}
