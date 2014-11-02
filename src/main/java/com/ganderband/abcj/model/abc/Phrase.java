/**
 * This class represents a phrase of the tune.
 *
 * <p>It will be used for generating MIDI files and playing the tune.
 */
package  abcj.model.abc ;

public class Phrase {
/**
 * The start element of the phrase.
 */
	ABCElement  mStart ;
/**
 * The first repeat element of the phrase (or null).
 */
	ABCElement  mRepeat1 ;
/**
 * The second repeat element of the phrase (or null).
 */
	ABCElement  mRepeat2 ;
/**
 * The end element of the phrase.
 */
	ABCElement  mEnd ;
/**
 * The number of times to play the phrase.
 */
	int  mCount ;


/**
 * The standard constructor.
 */
public  Phrase( ABCElement Start, ABCElement Repeat1, ABCElement Repeat2,
				ABCElement End, int Count )
{
	mStart   = Start ;
	mRepeat1 = Repeat1 ;
	mRepeat2 = Repeat2 ;
	mEnd     = End ;
	mCount   = Count ;
}


/**
 * Get the first element of the phrase.
 */
public ABCElement  getStartElement()
{	return  mStart ;   }


/**
 * Get the last element of the phrase.
 */
public ABCElement  getEndElement()
{	return  mEnd ;   }


/**
 * Get the first repeat element of the phrase.
 */
public ABCElement  getRepeat1Element()
{	return  mRepeat1 ;   }


/**
 * Get the second repeat element of the phrase.
 */
public ABCElement  getRepeat2Element()
{	return  mRepeat2 ;   }


/**
 * Get the phrase repeat count.
 */
public int  getRepeatCount()
{	return  mCount ;   }


}
