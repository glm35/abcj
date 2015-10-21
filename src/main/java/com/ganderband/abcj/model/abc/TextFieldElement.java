/**
 * A class representing a TextField Element.
 *
 * <p>This class represents all the fields where only a text field is needed. 
 */
package  com.ganderband.abcj.model.abc ;

public class TextFieldElement extends FieldElement
{
/**
 * The text value of this element (without type or colon).
 */
	private String  mText ;
	

/**
 * Construct an element of a given type with some text.
 */
public  TextFieldElement( char FieldType, String Text )
{	super(FieldType) ;
	setType(TEXT_FIELD_ELEMENT) ;
	mText = Text ;
}


/**
 * Get the text of the field.
 */
public String  getText()
{	return  mText ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "TextFieldElement " + getFieldType() + " >" + mText + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{	return  mText ;   }

}
