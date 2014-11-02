/**
 * A class representing an End Slur Element.
 */
package  abcj.model.abc ;

public class BeginSlurElement extends ABCElement
{
/**
 * Construct an element of given type.
 */
public  BeginSlurElement()
{	super(BEGINSLUR_ELEMENT) ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "BeginSlurElement >(<" ;   }


/**
 * Format the item as ABC text.
 * 
 * <p>Slurs should always terminate beaming.
 */
public String  format()
{	return  " (" ;   }

}
