/**
 * A class representing a Tex Command Element.
 */
package  com.ganderband.abcj.model.abc ;

public class TexCommandElement extends ABCElement
{
/**
 * The TEX command itself (without leading backslash).
 */
	private String  mCommand ;


/**
 * Construct an element of given type.
 */
public  TexCommandElement( String Command )
{	super(TEX_COMMAND_ELEMENT) ;   mCommand = Command ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "TexCommandElement >" + mCommand + "<" ;   }


/**
 * Format the item as ABC text.
 */
public String  format()
{	return  "\\" + mCommand ;   }

}
