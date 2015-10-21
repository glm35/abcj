/**
 * A class representing an Part Field Element.
 * 
 * <p>See ABC spec for format of text.
 */
package  com.ganderband.abcj.model.abc ;

public class PartsFieldElement extends FieldElement
{
/**
 * The parts.
 */
	private String  mParts ;
	
	
/**
 * Construct from a string.
 */
public  PartsFieldElement( String Parts )
{	super('P') ;   setType(PARTS_FIELD_ELEMENT) ;   mParts = Parts ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "PartsFieldElement >" + mParts + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{	return  mParts ;   }

}
