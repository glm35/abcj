/**
 * A class representing a note in ABC.
 */
package  com.ganderband.abcj.model.abc ;

import  com.ganderband.abcj.util.* ;

public class Note
{
/**
 * The pitch of the note.
 */
	protected Pitch  mPitch ;
/**
 * The numerator of the length.
 */
	protected int  mNum ;
/**
 * The denominator of the length.
 */
	protected int  mDenom ;
/**
 * The tie for the note.
 */
	protected String  mTie ;
/**
 * The absolute length of the note as a fraction.
 */
	protected Fraction  mAbsoluteLength ; 
/**
 * The current key in force.
 */
	protected Key  mKey ;


/**
 * The standard constructor.
 */
public  Note( Pitch Pitch, int Num, int Denom, String Tie )
{	mPitch = Pitch ;   mNum = Num ;   mDenom = Denom ;   mTie = Tie ;   }


/**
 * Get the pitch of the note.
 */
public Pitch  getPitch()
{	return  mPitch ;   }


/**
 * Get the tie.
 */
public String  getTie()
{	return  mTie ;   }


/**
 * Get the current key.
 */
public Key  getKey()
{	return  mKey ;   }


/**
 * Return the absolute length.
 */
public Fraction  getAbsoluteLength()
{	return  mAbsoluteLength ;   }


/**
 * Set the absolute length.
 */
public void  setAbsoluteLength( Fraction AbsLength )
{	mAbsoluteLength = AbsLength ;   }


/**
 * Convert to a string.
 */
public String  toString()
{
	String  s = mPitch.toString() ;
	if ( mNum > 0 )       s += Integer.toString(mNum) ;
	if ( mDenom > 0 )     s += "/" + Integer.toString(mDenom) ;
	if ( mTie != null )   s += mTie.toString() ;
	
	return  s ;
}


/**
 * Calculate the absolute length of the note.
 * 
 * <p>This will use the current note length and combine it with the default length.
 * Any broken rhythm adjustor will then be applied (this has may not be from the
 * current note as the second part of broken rhythm can be inherited from the
 * previous note.
 */
public void  calculateAbsoluteLength( Fraction DefLength, Fraction Multiplier )
{

//	Convert specified ABC length to its equivalent fraction

	Fraction  f = new Fraction( ( mNum   == 0 ) ? 1 : mNum,
								( mDenom == 0 )	? 1 : mDenom ) ;
								
	mAbsoluteLength = f.times(DefLength) ;
	
//  Now apply any special multiplier

	if ( Multiplier != null )
		mAbsoluteLength = mAbsoluteLength.times(Multiplier) ;
}

}
