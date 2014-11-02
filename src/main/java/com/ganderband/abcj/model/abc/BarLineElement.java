/**
 * A class representing a Bar Line Element.
 */
package  abcj.model.abc ;

public class BarLineElement extends ABCElement
{
/**
 * Valid bar-lines.
 * 
 * <p>Note that the order or these is critical to correct parsing.
 */
	public static final String[]  VALID_BARLINES = {
			":||:",	// not strictly valid - will be converted to :: 
			":|:",	// not strictly valid - will be converted to :: 
			"||:",	// not strictly valid - will be converted to |: 
			":||",	// not strictly valid - will be converted to :| 
			"||", "|]", "|:", "[|", "::", ":|", "|"
		} ;
/**
 * The names of each barline type.
 * 
 * <p>These are the indices of the elements in the array. 
 */
	public static final int  INV1 = 0 ;
	public static final int  INV2 = 1 ;
	public static final int  INV3 = 2 ;
	public static final int  INV4 = 3 ;
	public static final int  DOUBLE = 4 ;
	public static final int  DOUBLE_THICK_RIGHT = 5 ;
	public static final int  SINGLE_REP_RIGHT = 6 ;
	public static final int  DOUBLE_THICK_LEFT = 7 ;
	public static final int  DOUBLE_REP = 8 ;
	public static final int  SINGLE_REP_LEFT = 9 ;
	public static final int  SINGLE = 10 ;
/**
 * The bar line type.
 * 
 * <p>See ABCParserConstants for the values.
 */
	int  mBarLine ;


/**
 * Construct an element of given type.
 */
public  BarLineElement( int BarLine )
{
	super(BARLINE_ELEMENT) ;
			
//  Convert any special ones which are commonly used but invalid
		
	if ( BarLine == INV1 )   BarLine = DOUBLE_REP ;
	if ( BarLine == INV2 )   BarLine = DOUBLE_REP ;
	if ( BarLine == INV3 )   BarLine = SINGLE_REP_RIGHT ;
	if ( BarLine == INV4 )   BarLine = SINGLE_REP_LEFT ;
	
	mBarLine = BarLine ;
}


/**
 * Return the bar line type.
 */
public int  getBarLineType()
{	return  mBarLine ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "BarLineElement >" + VALID_BARLINES[mBarLine] + "<" ;   }


/**
 * Format the item as ABC text.
 * 
 * <p>Bar lines look better when preceded and followed by blanks.
 */
public String  format()
{	return  " " + VALID_BARLINES[mBarLine] + " " ;   }

}
