/**
 * The glyph class for a text item.
 */
package  abcj.model.music;

import  java.awt.* ;

public class GlyphText extends Glyph
{
/**
 * The text to display.
 */
	private String  mText ;
/**
 * The font for displaying the text.
 */
	private Font  mFont ;
	
	
/**
 * The default constructor.
 */
public  GlyphText( String Text, Font f )
{	this( 0, 0, Text, f ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphText( float x, float y, String Text, Font f )
{
	super( x, y ) ;
	
	mText = Text ;
	mFont = f ;
}


/**
 * Paint this text to the 2D graphics context supplied.
 */
public void  paint( Graphics2D g )
{

//  Save the current font if necessary
	
	Font  SavedFont = null ;
	if ( mFont != null ) {
		SavedFont = g.getFont() ;
		g.setFont(mFont) ;
	}
	
//  Now draw the string

	g.drawString( mText, mX + mLeadingSpace, mY ) ;
	
//  Restore font if required

	if ( SavedFont != null )   g.setFont(SavedFont) ;
}

}
