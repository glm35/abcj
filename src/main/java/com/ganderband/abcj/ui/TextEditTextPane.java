/**
 * The bottom text editor pane of the ABC GUI - For editting the ABC Text.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  javax.swing.* ;
import  javax.swing.event.* ;
import  com.ganderband.abcj.* ;

public class TextEditTextPane extends JScrollPane implements DocumentListener
{
/**
 * The owning ABCJ appication instance.
 */
	private ABCJ  mABCJ ;
/**
 * The text area used to edit the ABC text.
 */
	TextAreaWithPopup  mTextArea = new TextAreaWithPopup() ;


/**
 * The default constructor.
 */
public  TextEditTextPane( ABCJ App )
{
	mABCJ = App ;
	
//	setMinimumSize( new Dimension( 400, 200 ) ) ;
	
	mTextArea.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) ) ;
	mTextArea.getDocument().addDocumentListener(this) ;
	
	mTextArea.setForeground(Color.black) ;
	mTextArea.setBackground(Color.white) ;
	
	setViewportView(mTextArea) ;
}


/**
 * Set the text in the text area.
 */
public void  setText( String Text )
{	mTextArea.setText(Text) ;   }


/**
 * Set the initial text in the text area.
 * 
 * <p>This is used to setup the initial starting point for the text area.
 * It will not fire changed events.
 */
public void  setInitialText( String Text )
{	
	mTextArea.getDocument().removeDocumentListener(this) ;
	setText(Text) ;
	mTextArea.clearUndoHistory() ;
	mTextArea.getDocument().addDocumentListener(this) ;
	mTextArea.setCaretPosition(0) ;
}


/**
 * Catch text changed events.
 * 
 * <p>This is used to trap whenever the content of the text area is changed.
 */
public void  changedUpdate( DocumentEvent Event )
{	mABCJ.tuneEditted( mTextArea.getText() ) ;   }


/**
 * Catch text changed events.
 * 
 * <p>This is used to trap whenever the content of the text area is changed.
 */
public void  insertUpdate( DocumentEvent Event )
{	changedUpdate(Event) ;   }


/**
 * Catch text removed events.
 * 
 * <p>This is used to trap whenever the content of the text area is changed.
 */
public void  removeUpdate( DocumentEvent Event )
{	changedUpdate(Event) ;   }

}