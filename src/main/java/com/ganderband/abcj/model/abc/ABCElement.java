/**
 * A class representing a general ABC Element.
 */
package  com.ganderband.abcj.model.abc ;

public abstract class ABCElement implements ABCParserConstants
{
/**
 * The element type as described in ABCParserConstants.
 */
	private int  mType ;
/**
 * A link to the following element.
 * 
 * <p>This is used to maintain a continuous list of elements across
 * music lines.
 */
	ABCElement  mNextElement = null ;
/**
 * A flag to say if this element was added by the parser.
 * 
 * <p>At the moment, only a final appropriate bar-line will be added
 * if there isn't already one there.
 * 
 * <p>These elements should not appear on the music.
 */
	boolean  mParserAdded = false ;

/**
 * Construct an element of a given type.
 */
protected  ABCElement( int Type )
{	mType = Type ;   }

	
/**
 * Get the element type.
 */
public int  getType()
{	return  mType ;   }

	
/**
 * Set the element type.
 */
public void  setType( int Type )
{	mType = Type ;   }

	
/**
 * Did the parser add this element.
 */
public boolean  isParserAdded()
{	return  mParserAdded ;   }


/**
 * Link this element to the one passed.
 * 
 * <p>The element passed will be the next element after this one.
 */
public void  setNextElement( ABCElement Element )
{	mNextElement = Element ;   }


/**
 * Return the next element in the chain.
 */
public ABCElement  getNextElement()
{	return  mNextElement ;   }


/**
 * Format the item as ABC text.
 */
public abstract String  format() ;

}
