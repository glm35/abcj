/**
 * A class representing a Tempo Field element.
 *
 * <p>Each tempo form is stored as follows :-
 * 
 * 						Num		Denom	Tempo	Absolute
 * 
 * "nnn"				0		0		nnn		false
 * "C=nnn"				0		0		nnn		false
 * "Cnnn=nnn"			nnn		0		nnn		false
 * "Cnnn/nnn=nnn"		nnn		nnn		nnn		false
 * "nnn/nnn=nnn" 		nnn		nnn		nnn		true
 */
package  com.ganderband.abcj.model.abc ;

import  com.ganderband.abcj.util.* ;

public class TempoFieldElement extends FieldElement
{
/**
 * The default tempo if none provided.
 */
	public static final TempoFieldElement  DEFAULT_TEMPO = 
									new TempoFieldElement( 1, 4, 120, true ) ;
/**
 * The numerator.
 */
	private int  mNum ;
/**
 * The meter numerator.
 */
	private int  mDenom ;
/**
 * The Tempo.
 */
	private int  mTempo ;
/**
 * A flag to indicate absolute or relative. 
 */
	private boolean  mIsAbsolute ;
/**
 * The absolute tempo value.
 * 
 * <p>This is effectively 1/4=nnn.
 */
	private int  mAbsoluteTempo ;
/**
 * The default length currently applicable.
 */
	private Fraction  mDefLength ;
	
	
/**
 * Construct an instance
 */
public  TempoFieldElement( int Num, int Denom, int Tempo, boolean Absolute )
{
	super('Q') ;
	setType(TEMPO_FIELD_ELEMENT) ;
	
	mNum        = Num ;
	mDenom      = Denom ;
	mTempo      = Tempo ;
	mIsAbsolute = Absolute ;	
}


/**
 * Get the absolute tempo.
 * 
 * <p>This is equivalent to 1/4=nnn
 */
public int  getAbsoluteTempo()
{	return  mAbsoluteTempo ;   }


/**
 * Get the default length.
 * 
 * <p>This is the default length that was applicable to this tempo indication.
 */
public Fraction  getDefaultLength()
{	return  mDefLength ;   }


/**
 * Get the meter fraction for this tempo.
 */
public Fraction  getMeterFraction()
{
	
//  Determine the note length
	
	int  Num   = ( mNum   == 0 ) ? 1 : mNum ;
	int  Denom = ( mDenom == 0 ) ? 1 : mDenom ;
	
	Fraction  f = new Fraction( Num, Denom ) ; 
	
	if ( ! mIsAbsolute )   f = f.times(mDefLength) ;
	
	return  f ;
}


/**
 * Get the tempo value for this tempo.
 */
public int  getTempo()
{	return  mTempo ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "TempoFieldElement >" + formatField() + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{
	String s = "" ;
	if ( ! mIsAbsolute )   s = "C" ;
	if ( mNum > 0 )   s += Integer.toString(mNum) ;
	if ( mDenom > 0 )   s += "/" + Integer.toString(mDenom) ;
	return  s + "=" + Integer.toString(mTempo) ;
}


/**
 * Calculate the absolute tempo (1/4=nnn)
 */

public void  calculateAbsoluteTempo( Fraction DefLength )
{
	mDefLength     = DefLength ;
	mAbsoluteTempo = mTempo ;
	
//  Determine the note length
	
	Fraction  f = getMeterFraction() ;
	
//  Now calculate the tempo for 1/4=nnn

	mAbsoluteTempo = mTempo * f.getNumerator() * 4 / f.getDenominator() ;
}

}
