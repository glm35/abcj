/**
 * The class which shows a general error dialog.
 */
package  com.ganderband.abcj.ui ;

import  javax.swing.* ;

public class ErrorDialog
{
/**
 * The frame which owns this dialog.
 */
	private JFrame  mFrame ;

	
/**
 * The standard constructor.
 */
public  ErrorDialog( JFrame Frame )
{	mFrame = Frame ;   }


/**
 * A static doDialog method to save instantiation.
 */
public static void  doDialog( JFrame Frame, String Message )
{
	ErrorDialog  Dlg = new ErrorDialog(Frame) ;
	Dlg.doDialog(Message) ;
}

/**
 * Show the dialog and get results.
 */
public void  doDialog( String Message )
{
	JOptionPane.showMessageDialog( mFrame, Message, "Error",
								   JOptionPane.ERROR_MESSAGE ) ;
}

}