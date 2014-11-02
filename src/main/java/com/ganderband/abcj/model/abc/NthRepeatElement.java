/**
 * A class representing an Nth Repeat Element.
 */
package  abcj.model.abc ;

public class NthRepeatElement extends ABCElement
{
/**
 * Valid Nth repeats.
 * 
 * <p>Note that the order or these is critical to correct parsing.
 */
	public static final String[]  VALID_NTHREPEATS = {
			"|[1",		// This is two elements strictly
			":|[2",		// This is two elements strictly
			"[1", "[2", "|1", ":|2",
		} ;
/**
 * The names of each repeat type.
 * 
 * <p>These are the indices of the elements in the array. 
 */
	public static final int  FIRST_FULL = 0 ;
	public static final int  SECOND_FULL = 1 ;
	public static final int  FIRST = 2 ;
	public static final int  SECOND = 3 ;
	public static final int  BARLINE_FIRST = 4 ;
	public static final int  BARLINE_SECOND = 5 ;

/**
 * The Nth Repeat type.
 */
	int  mNthRepeat ;


/**
 * Construct an element of given type.
 */
public  NthRepeatElement( int NthRepeat )
{	super(NTHREPEAT_ELEMENT) ;   mNthRepeat = NthRepeat ;   }


/**
 * Return the Nth repeat type.
 */
public int  getNthRepeatType()
{	return  mNthRepeat ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "NthRepeatElement >" + VALID_NTHREPEATS[mNthRepeat] + "<" ;   }


/**
 * Format the item as ABC text.
 * 
 * <p>Nth repeats look better when preceded and followed by blanks.
 */
public String  format()
{	return  " " + VALID_NTHREPEATS[mNthRepeat] + " " ;   }

}
