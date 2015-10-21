/**
 * A class representing a Begin Slur Element.
 */
package  com.ganderband.abcj.model.abc ;

public class EndSlurElement extends ABCElement
{
/**
 * Construct an element of given type.
 */
public  EndSlurElement()
{	super(ENDSLUR_ELEMENT) ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "EndSlurElement >)<" ;   }


/**
 * Format the item as ABC text.
 * 
 * <p>Slurs should always terminated previous beaming.
 */
public String  format()
{	return  ") " ;   }

}
