/**
 * A class representing a Meter Field Element.
 * 
 * <p>Meters are of the form x/y, x+y/z or x+y+.../z
 */
package  com.ganderband.abcj.model.abc ;

import  com.ganderband.abcj.util.* ;

public class MeterFieldElement extends FieldElement
{
/**
 * The default meter if none provided.
 */
	public static final String  DEFAULT_METER = "C" ;	// Common time
/**
 * The meter numerators.
 */
	private int[]  mNums ;
/**
 * The meter numerator.
 */
	private int  mDenom ;
/**
 * A flag to indicate common time (C=4/4). 
 */
	private boolean  mIsCommonTime = false ;
/**
 * A flag to indicate cut time (C| = 2/2). 
 */
	private boolean  mIsCutTime = false ;


/**
 * Construct from a string (which should be "C" or "C|").
 */
public  MeterFieldElement( String Meter )
{
	super('M') ;
	setType(METER_FIELD_ELEMENT) ;
	
	if ( Meter.equals("C") ) {
		mIsCommonTime = true ;
		mNums = new int[] { 4 } ;
		mDenom = 4 ;
	}
	
	if ( Meter.equals("C|") ) {
		mIsCutTime = true ;
		mNums = new int[] { 2 } ;
		mDenom = 2 ;
	}
}


/**
 * Construct from array of numerators and denominator.
 */
public  MeterFieldElement( int[] NumsDenom )
{
	super('M') ;
	setType(METER_FIELD_ELEMENT) ;
	
	mNums = new int[NumsDenom.length - 1] ;
	for ( int i = 0 ; i < mNums.length ; i++ )   mNums[i] = NumsDenom[i] ;

	mDenom = NumsDenom[ NumsDenom.length - 1 ] ;
}

	
/**
 * Construct from array of numerators and denominator.
 */
public  MeterFieldElement( int[] Nums, int Denom )
{
	super('M') ;
	mNums  = Nums ;
	mDenom = Denom ;		
	setType(METER_FIELD_ELEMENT) ;
}


/**
 * Get the numerators.
 */
public int[]   getNumerators()
{	return  mNums ;   }


/**
 * Get the denominator.
 */
public int   getDenominator()
{	return  mDenom ;   }


/**
 * Is this common time.
 */
public boolean  isCommonTime()
{	return  mIsCommonTime ;   }


/**
 * Is this cut time.
 */
public boolean  isCutTime()
{	return  mIsCutTime ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "MeterFieldElement >" + formatField() + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{
	String  s = "" ;
	for ( int i = 0 ; i < mNums.length ; i++ ) {
		if ( i > 0 )   s += "+" ;
		s += Integer.toString( mNums[i] ) ;
	}
	return  s + "/" + Integer.toString(mDenom) ;
}


/**
 * Return meter as a fraction.
 * 
 * <p>This will add up all the nums.
 */

public Fraction  getMeterAsFraction()
{
	int  Sum = 0 ;
	for ( int i = 0 ; i < mNums.length ; i++ )   Sum += mNums[i] ;
	
	return  new Fraction( Sum, mDenom ) ;
}

}
