/**
 * An element representing a tuplet.
 */
package  com.ganderband.abcj.model.abc ;

import  java.util.* ;
import  com.ganderband.abcj.util.* ;

public class TupletElement extends ABCElement
{
/**
 * Tuplet Spec array.
 */
	private int[]  mTupletSpec = new int[3] ;
/**
 * An array list of the note elements.
 */
	private ArrayList  mNoteElements = new ArrayList() ;
/**
 * A flag to indicate if this instance is completed.
 * 
 * <p>This indicates that the item has changed but has not been updated.
 */
	private boolean  mIsCompleted = false ;
/**
 * Beam to follow notes or not.
 */
	private boolean  mBeam = false ;
/**
 * The special multiplier used to calculate the real note width.
 */
	private Fraction  mSpecialMultiplier ;


/**
 * The standard constructor.
 */
public  TupletElement( int[] TupletSpec )
{
	super(TUPLET_ELEMENT) ;
	for ( int i = 0 ; i < 3 ; i++ )   mTupletSpec[i] = TupletSpec[i] ;
	
	complete() ;
}


/**
 * Get the completion status of this element.
 */
public boolean  isCompleted()
{	return  mIsCompleted ;   }


/**
 * Set beam state.
 */
public void  setBeam( boolean Beam )
{	mBeam = Beam ;   }


/**
 * Get the note elements in the tuplet.
 */
public ArrayList  getNoteElements()
{	return  mNoteElements ;   }


/**
 * Get the special multiplier applicable to this tuplet.
 */
public Fraction  getSpecialMultiplier()
{	return  mSpecialMultiplier ;   }


/**
 * Get the tuplet spec.
 */
public int[]  getTupletSpec()
{	return  mTupletSpec ;   }


/**
 * Complete the element.
 *
 * <p>This sets the text and flags as complete.
 */
public void  complete()
{

//  ??????????? Not currently necessary

	mIsCompleted = true ;
}


/**
 * Add a note or rest element to the list.
 */
public void  add( NoteElement Element )
{
	Element.setBeam(true) ;		// Force beaming on
	mNoteElements.add(Element) ;
	mIsCompleted = false ;
}


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "TupletElement >" + format() +"<" ;   }


/**
 * Format the item as ABC text.
 */
public String  format()
{
	String  s = "(" + Integer.toString( mTupletSpec[0] ) ;
	if ( mTupletSpec[1] > 0 ) {
		s += ":" + Integer.toString( mTupletSpec[1] ) ;
		if ( mTupletSpec[2] > 0 )
			s += ":" + Integer.toString( mTupletSpec[2] ) ;
	}
	else {
		if ( mTupletSpec[2] > 0 )
			s += "::" + Integer.toString( mTupletSpec[2] ) ;
	}
	
	for ( int i = 0 ; i < mNoteElements.size() ; i++ )
		s += ( (NoteElement) mNoteElements.get(i) ).format() ;
	
	if ( ! mBeam )   s += " " ;
		
	return  s ;
}


/**
 * Calculate the absolute length of the notes.
 * 
 * <p>This will use the current note length and combine it with the default length.
 */
public void  calculateAbsoluteLength( Fraction DefLength, Fraction Meter )
{
	
//	Convert to the more general p:q:r specification for calculation purposes
//  See the ABC specification for how this works !

	int  p = mTupletSpec[0] ;
	int  q = mTupletSpec[1] ;
	int  r = mTupletSpec[2] ;

	if ( q == 0 ) {
		if      ( p == 2  ||  p == 4  ||  p == 8 )
			q = 3 ;	
		else if ( p == 3  ||  p == 6 )
			q = 2 ;
		else if ( Meter.getNumerator() % 3 == 0 )
			q = 3 ;
		else
			q = 2 ;
	}
	
	if ( r == 0 )   r = p ;

//  Convert this to a special multipler for adjusting the defined note lengths

	mSpecialMultiplier = new Fraction( q, p ) ;
	
//  Now adjust all notes in the tuplet.
//  Particularly, note that any broken rhythms will be ignored for tuplets 

	for ( int i = 0 ; i < mNoteElements.size() ; i++ )
		( (NoteElement) mNoteElements.get(i) ).
							calculateAbsoluteLength( DefLength, mSpecialMultiplier ) ;
}


/**
 * Calculate the absolute pitch of the notes in this tuplet.
 */
public void  calculateAbsolutePitch( Key Key )
{
	for ( int i = 0 ; i < mNoteElements.size() ; i++ )
		( (NoteElement) mNoteElements.get(i) ).calculateAbsolutePitch(Key) ;
}

}
