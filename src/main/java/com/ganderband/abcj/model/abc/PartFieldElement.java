/**
 * A class representing an Part Field Element.
 * 
 * This should currently be a single character A-Z.
 */
package  abcj.model.abc ;

public class PartFieldElement extends FieldElement
{
/**
 * The part.
 */
	private String  mPart ;
	
	
/**
 * Construct from a string.
 */
public  PartFieldElement( String Part )
{	super('P') ;   setType(PART_FIELD_ELEMENT) ;   mPart = Part ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "PartFieldElement >" + mPart + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{	return  mPart ;   }

}
