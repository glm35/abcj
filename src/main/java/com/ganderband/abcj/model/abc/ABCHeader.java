/**
 * A class representing all the ABC Header information.
 */
package  com.ganderband.abcj.model.abc ;

import  java.util.* ;
import  com.ganderband.abcj.util.* ;

public class ABCHeader implements ABCParserConstants
{
/**
 * An array list containing all elements in the header.
 */
	private ArrayList  mElements = new ArrayList() ;
/**
 * The default length field element. 
 */
	private DefaultLengthFieldElement  mDefaultLength ;
/**
 * The meter field element. 
 */
	private MeterFieldElement  mMeter ;
/**
 * The tempo field element. 
 */
	private TempoFieldElement  mTempo ;
/**
 * The key field element. 
 */
	private KeyFieldElement  mKeyElement ;
/**
 * The title of the tune.
 */
	private String  mTitle = null ;
/**
 * The composer of the tune.
 */
	private String  mComposer = null ;

	
/**
 * Add an element to the header.
 */
public void  add( ABCElement e )
{
	mElements.add(e) ;
	
//  Select out certain elements for easy access

	switch ( e.getType() ) {
	case  DEFAULT_LENGTH_FIELD_ELEMENT :
		mDefaultLength = (DefaultLengthFieldElement) e ;   break ;
	case  METER_FIELD_ELEMENT :
		mMeter = (MeterFieldElement) e ; 				   break ;
	case  TEMPO_FIELD_ELEMENT :
		mTempo = (TempoFieldElement) e ;				   break ;
	case  KEY_FIELD_ELEMENT :
		mKeyElement = (KeyFieldElement) e ;				   break ;
	default :
		if (  e.getType() != TEXT_FIELD_ELEMENT )   break ;
		
		TextFieldElement  t = (TextFieldElement) e ;

		if ( t.getFieldType() == 'T'  &&  mTitle == null )
			mTitle = t.getText() ;
		if ( t.getFieldType() == 'C'  &&  mComposer == null )
			mComposer = t.getText() ;
	}

	if ( ABCParser.isDebugEnabled() )		// Saves time !
		ABCParser.debug( "Header add - " + e ) ;
}


/**
 * Get the tune title.
 * 
 * <p>This is obtained from the first T: field element.
 */
public String  getTitle()
{	return  mTitle ;   }


/**
 * Get the tune composer.
 * 
 * <p>This is obtained from the first T: field element.
 */
public String  getComposer()
{	return  mComposer ;   }


/**
 * Get the elements array in the header.
 */
public ArrayList  getElements()
{	return  mElements ;   }


/**
 * Get the key field element.
 */
public KeyFieldElement  getKeyElement()
{
	if ( mKeyElement != null )   return  mKeyElement ;
	
// Construct a default

	try {   mKeyElement = new KeyFieldElement( new Key( Key.DEFAULT_KEY, null, null ) ) ;   }
	catch ( Exception e )   {
		System.out.println( "Exception ignored - " + e ) ;
		e.printStackTrace() ;
	}		// Ignore as OK
	
	return  mKeyElement ;
}


/**
 * Get the meter field element.
 */
public MeterFieldElement  getMeter()
{	
	if ( mMeter == null )	
		mMeter = new MeterFieldElement(MeterFieldElement.DEFAULT_METER) ;
	return  mMeter ;
}

/**
 * Get the tempo field element.
 */
public TempoFieldElement  getTempo()
{
	if ( mTempo == null )
		mTempo = TempoFieldElement.DEFAULT_TEMPO ;
	return  mTempo ;
 }


/**
 * Get the default length field element.
 */
public DefaultLengthFieldElement  getDefaultLength()
{
	if ( mDefaultLength != null )   return  mDefaultLength ;
	
//  Determine the default length based on the current meter

	getMeter() ;		// Forces it to be non-blank
	
	Fraction  f = mMeter.getMeterAsFraction() ;
	
	if ( f.compareTo( 3 / 4 ) < 0 )
		f = DefaultLengthFieldElement.DEFAULT_DEFAULT_LENGTH2 ;
	else
		f = DefaultLengthFieldElement.DEFAULT_DEFAULT_LENGTH1 ;

	mDefaultLength = new DefaultLengthFieldElement( 
										f.getNumerator(), f.getDenominator() ) ;
	
	return  mDefaultLength ;
}


/**
 * Format the header back to ABC text.
 */
public String  format()
{
	
//  Format each element on its own as a separate line
	
	String  Result = "" ;
	for ( int i = 0 ; i < mElements.size() ; i++ )
		Result += ( (ABCElement) mElements.get(i) ).format() + "\n" ;
	
//  Return the result
	
	return  Result ;
}

}
