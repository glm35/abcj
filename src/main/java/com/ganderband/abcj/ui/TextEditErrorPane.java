/**
 * The top text editor pane of the ABC GUI - For showing ABC errors.
 */
package  abcj.ui ;

import  java.awt.* ;
import  javax.swing.* ;
import  javax.swing.text.* ;
import  abcj.* ;

public class TextEditErrorPane extends JScrollPane
{
/**
 * The owning ABCJ appication instance.
 */
//	private ABCJ  mABCJ ;		// Not yet used
/**
 * The text area used to edit the ABC text.
 */
	JTextPane  mTextPane = new JTextPane() ;
/**
 * The attribute set for info lines.
 */
	private  SimpleAttributeSet  mInfoStyle = new SimpleAttributeSet() ;
/**
 * The attribute set for warning lines.
 */
	private  SimpleAttributeSet  mWarningStyle = new SimpleAttributeSet() ;
/**
 * The attribute set for error lines.
 */
	private  SimpleAttributeSet  mErrorStyle = new SimpleAttributeSet() ;

/**
 * The default constructor.
 */
public  TextEditErrorPane( ABCJ App )
{
//	mABCJ = App ;

//  Define styles for displaying the types of text
	
	StyleConstants.setForeground( mInfoStyle, Color.gray ) ;
	StyleConstants.setForeground( mWarningStyle, Color.blue.darker() ) ;
	StyleConstants.setForeground( mErrorStyle, Color.red.darker() ) ;

//  Now build the pane

	mTextPane.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) ) ;
	mTextPane.setEditable(false) ;
	
	mTextPane.setForeground(Color.black) ;
	mTextPane.setBackground(Color.lightGray) ;

	setViewportView(mTextPane) ;
}


/**
 * Set the text in the text area.
 */
public void  setText( String[] Text )
{
	try {
		int  CursorPos = 0 ;
	
	//  Clear the document to start with

		StyledDocument Doc = mTextPane.getStyledDocument() ;
		Doc.remove( 0, Doc.getLength() ) ;
		
	//  Display all the lines

		for ( int i = 0 ; i < Text.length ; i++ ) {
			char  Type = Text[i].charAt(0) ;
			String  s = Text[i].substring(1) + "\n" ;
			
		//  Determine attributes for a single line and display it
			
			AttributeSet         Style = mInfoStyle ;
			if ( Type == 'W' )   Style = mWarningStyle ;
			if ( Type == 'E' )   Style = mErrorStyle ;

			if ( Type == 'E'  &&  CursorPos == 0 )   CursorPos = Doc.getLength() ;
						
			Doc.insertString( Doc.getLength(), s, Style ) ;
			
			if ( Type == 'E'  &&  CursorPos == 0 )   CursorPos = i ;
			
			mTextPane.setCaretPosition(CursorPos) ;
		}
	}
	catch ( Exception e ) {
		System.out.println("Problem in TextEditorErrorPane.setText");
		e.printStackTrace(System.out) ;
	}
}

}