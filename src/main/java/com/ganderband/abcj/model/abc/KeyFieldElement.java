/**
 * A class representing an Key Field Element.
 */
package  com.ganderband.abcj.model.abc ;

public class KeyFieldElement extends FieldElement
{
/**
 * The key represented by this field.
 */
	private Key  mKey ;
	
/**
 * Construct an instance.
 */
public  KeyFieldElement( Key Key )
{
	super('K') ;
	setType(KEY_FIELD_ELEMENT) ;
	mKey = Key ;
}


/**
 * Get the key of this field.
 */
public Key  getKey()
{	return  mKey ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "KeyFieldElement >" + formatField() + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{	return  mKey.toString() ;   }

}
