/**
 * A class representing a Default Length Field Element.
 */
package  com.ganderband.abcj.model.abc ;

import  com.ganderband.abcj.util.* ;


public class DefaultLengthFieldElement extends FieldElement
{
/**
 * The default default length if none provided.
 */
	public static final Fraction  DEFAULT_DEFAULT_LENGTH1 = new Fraction( 1, 8 ) ;
	public static final Fraction  DEFAULT_DEFAULT_LENGTH2 = new Fraction( 1, 16 ) ;
/**
 * The note length Fraction.
 */
	private Fraction  mLength ;
	
/**
 * Construct from the numerator and denominator.
 */
public  DefaultLengthFieldElement( int Num, int Denom )
{
	super('L') ;
	setType(DEFAULT_LENGTH_FIELD_ELEMENT) ;
	mLength = new Fraction( Num, Denom ) ; 
}


/**
 * Get the length as a fraction.
 */
public Fraction  getDefaultLength()
{	return  mLength ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "DefaultLengthFieldElement >" + formatField() + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon.
 */
public String  formatField()
{	return  mLength.toString() ;   }

}
