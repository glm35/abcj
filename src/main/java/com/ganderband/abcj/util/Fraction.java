/**
 * A class to manage fractions.
 */

package  abcj.util;

import  java.io.* ;

public class Fraction implements Cloneable, Comparable, Serializable
{
/**
 * The numerator of the fraction.
 */	
	private int  mNum ;
/**
 * The denominator of the fraction.
 */	
	private int  mDenom ;


/**
 * Create a Fraction equal in value to num / den.
 */
public  Fraction( int Num, int Denom )
{

//  Normalize while constructing

	if ( Denom < 0 ) {
		Num   = -Num ;
		Denom = -Denom ;
	}
	
	int  a = Num ;
	if ( a < 0 )   a = -a ;
    
    int  g = gcd( a, Denom ) ;
    
    mNum   = Num   / g ;
    mDenom = Denom / g ;
}	


/**
 * Create a Fraction from an integer.
 */
public  Fraction( int Value )
{	this( Value, 1 ) ;   }

/**
 * Create a fraction from another one.
 */
public  Fraction( Fraction f )
{   mNum = f.mNum ;   mDenom = f.mDenom ;   }
	

/**
 *  Get the numerator.
 */
public int  getNumerator()
{	return  mNum ;   }

/**
 * Get the denominator.
 */
public int  getDenominator()
{   return  mDenom ;   }


/**
 * Return a Fraction representing the negated value of this instance.
 */
public Fraction  negative()
{	return  new Fraction( -mNum, mDenom ) ;   }


/**
 * Return a Fraction representing 1 / this Fraction.
 */
public Fraction  inverse()
{   return new Fraction( mDenom, mNum ) ;   }


/**
 * Return a Fraction representing this Fraction plus f.
 */
public Fraction  plus( Fraction f )
{	return  new Fraction( mNum * f.mDenom + f.mNum * mDenom, mDenom * f.mDenom ) ;   }


/**
 * Return a Fraction representing this Fraction plus n
 */
public Fraction  plus( int n )
{	return  new Fraction( mNum + n * mDenom, mDenom ) ;   }


/**
 * Return a Fraction representing this Fraction minus f.
 */
public Fraction  minus( Fraction f )
{   return  new Fraction( mNum * f.mDenom - f.mNum * mDenom, mDenom * f.mDenom ) ;   }


/**
 * Return a Fraction representing this Fraction minus n.
 **/
public Fraction  minus( int n )
{	return  new Fraction( mNum - n * mDenom, mDenom ) ;   }


/**
 * Return a Fraction representing this Fraction times f.
 */
public Fraction  times( Fraction f )
{	return  new Fraction( mNum * f.mNum, mDenom * f.mDenom ) ;   }


/**
 * Return a Fraction representing this Fraction times n.
 */
public Fraction  times( int n )
{   return  new Fraction( mNum * n, mDenom ) ;   }


/**
 * Return a Fraction representing this Fraction divided by f.
 */
public Fraction  dividedBy( Fraction f ) 
{	return  new Fraction( mNum * f.mDenom, mDenom * f.mDenom ) ;   }


/**
 * Return a Fraction representing this Fraction divided by n.
 */
public Fraction  dividedBy( int n )
{	return  new Fraction( mNum, mDenom * n ) ;   }


/**
 * Return a number less, equal, or greater than zero
 * reflecting whether this Fraction is less, equal or greater than 
 * the value of Fraction Obj.
 */
public int  compareTo( Object Obj )
{
    Fraction  f = (Fraction) Obj ;
    
    int  a = mNum * f.mDenom ;
    int  b = f.mNum * mDenom ;
    
    if ( a < b )	     return  -1 ;
    else if ( a > b )	 return  1 ;
    else				 return  0 ;
}


/**
 * Return a number less, equal, or greater than zero
 * reflecting whether this Fraction is less, equal or greater than n.
 */
public int  compareTo( int n )
{
    long a = mNum ;
    long b = n * mDenom ;
    
    if ( a < b )	     return  -1 ;
    else if ( a > b )	 return  1 ;
    else				 return  0 ;
  }


/**
 * Return a number less, equal, or greater than zero
 * reflecting whether this Fraction is less, equal or greater than n.
 */
public int  compareTo( int n, int d )
{
	long a = mNum * d ;
	long b = n * mDenom ;
    
	if ( a < b )	     return  -1 ;
	else if ( a > b )	 return  1 ;
	else				 return  0 ;
}


/**
 * Check for basic equality to another fraction.
 */
public boolean  equals( Object Obj )
{	return  ( compareTo(Obj) == 0 ) ;   }



/**
 * Check for basic equality to an integer.
 */
public boolean  equals( int n )
{   return  ( compareTo(n) == 0 ) ;   }


/**
 * Generate a sensible hashcode for hashtable entries.
 */
public int  hashCode()
{	return  mNum ^ mDenom ;   }


/** 
 * Compute the nonnegative greatest common divisor of a and b.
 * (This is needed for normalizing Fractions, but can be
 * useful on its own.)
 */
public static int  gcd( int a, int b )
{ 
	if ( a < 0 )   a = -a ;
	if ( b < 0 )   b = -b ;

	int  x ;
	int  y ;

	if ( a >= b )   {   x = a ;   y = b ;   }
    else			{   x = b ;   y = a ;   }

	while ( y != 0 ) {
		int  t = x % y ;
		x = y ;
		y = t ;
	}
	
	return  x ;
}


/**
 * Represent the fraction as a string.
 */
public String  toString()
 { 
	if ( mDenom == 1 ) 
		return  "" + mNum ;
	return  mNum + "/" + mDenom ; 
}


/**
 * Clone the object.
 * 
 * <p>As required by Cloneable.
 */
public Object  clone()
{   return  new Fraction(this) ;   }


/**
 * Return the value of the Fraction as a double.
 */
public double  asDouble()
{	return ( (double) mNum ) / ( (double) mDenom ) ;   }


}
