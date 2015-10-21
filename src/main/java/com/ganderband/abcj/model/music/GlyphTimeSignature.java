/**
 * The glyph class for a time signature.
 */
package  com.ganderband.abcj.model.music ;

import  java.awt.* ;
import  java.awt.geom.* ;
import  com.ganderband.abcj.model.abc.* ;
import  com.ganderband.abcj.util.* ;

public class GlyphTimeSignature extends Glyph
{
/**
 * The font used to display time signature
 */	
	private Font  TIME_SIG_FONT = new Font( "Serif", 1, 16 ) ;
/**
 * The MeterField instance representing this (from the ABC model)
 */
	private MeterFieldElement  mMeter ;
/**
 * A special glyph for representing common-time and cut-time.
 */
	private Glyph  mSpecialGlyph ;
/**
 * The top of the fraction as a string.
 */
	private String  mTop ;	
/**
 * The bottom of the fraction as a string.
 */
	private String  mBottom ;	
/**
 * The width of the top string.
 */
	private float  mTopWidth ;
/**
 * The width of the bottom string.
 */
	private float  mBottomWidth ;

	
/**
 * The default constructor.
 */
public  GlyphTimeSignature( MeterFieldElement Meter )
{	this( 0, 0, Meter ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphTimeSignature( float x, float y, MeterFieldElement Meter )
{
	super( x, y ) ;
	mMeter = Meter ;
	
	initPaddingSpace() ;
	
//  Check if this is cut-time or common-time and setup the special glyph.

	if ( mMeter.isCommonTime() ) {
		mSpecialGlyph = new GlyphCommonTime() ;
		mWidth = mSpecialGlyph.getWidth() ;
		return ;
	}
	
	if ( mMeter.isCutTime() ) {
		mSpecialGlyph = new GlyphCutTime() ;
		mWidth = mSpecialGlyph.getWidth() ;
		return ;
	}
	
//  If normal fraction then build the top and bottom strings
	
	int[]  Nums = mMeter.getNumerators() ;
	mTop = Integer.toString( Nums[0] ) ;
	for ( int i = 1 ; i < Nums.length ; i++ )
		mTop += "+" + Integer.toString( Nums[i] ) ;

	mBottom = Integer.toString( mMeter.getDenominator() ) ;
	
//  Now store the length of each string so we can calculate the width

	mTopWidth    = (float) Utils.getStringBounds( mTop, TIME_SIG_FONT ).getWidth() ;
	mBottomWidth = (float) Utils.getStringBounds( mBottom, TIME_SIG_FONT ).getWidth() ;
	
	mWidth = ( mTopWidth > mBottomWidth ) ? mTopWidth : mBottomWidth ;
}


/**
 * Paint this stave to the 2D graphics context supplied.
 */
public void  paint( Graphics2D g )
{
	
//	Save relavant info position at correct place

	AffineTransform  SavedTransform = g.getTransform() ;
	Font             SavedFont      = g.getFont() ;
	
	g.translate( mX + mLeadingSpace, mY ) ;
	
//  Draw a glyph for common-time and cut-time

	if ( mSpecialGlyph != null ) {
		mSpecialGlyph.paint(g) ;
	}

//  Draw a full time signature with the top and bottom centered

	else {
		g.setFont(TIME_SIG_FONT) ;
		
		g.drawString( mTop,    ( mWidth - mTopWidth ) / 2,
							   STAVE_LINE_SPACING * 2 - .5F ) ;
		g.drawString( mBottom, ( mWidth - mBottomWidth ) / 2,
							   STAVE_LINE_SPACING * 4 - .5F ) ;
	}

//  Restore and return 

	g.setFont(SavedFont) ;
	g.setTransform(SavedTransform) ;	
}


/**
 * Initialize the padding space for this glyph. 
 */
public void  initPaddingSpace()
{
	mLeadingSpace = mTrailingSpace = 2 ;
}

}
