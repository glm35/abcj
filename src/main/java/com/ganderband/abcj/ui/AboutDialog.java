/**
 * The class which shows a general about dialog.
 */
package  com.ganderband.abcj.ui ;

import  javax.swing.* ;
import  com.ganderband.abcj.* ;

public class AboutDialog
{
/**
 * The frame which owns this dialog.
 */
	private JFrame  mFrame ;

	
/**
 * The standard constructor.
 */
public  AboutDialog( JFrame Frame )
{	mFrame = Frame ;   }


/**
 * A static doDialog method to save instantiation.
 */
public static void  doDialog( JFrame Frame )
{
	AboutDialog  Dlg = new AboutDialog(Frame) ;
	Dlg.doDialog() ;
}

/**
 * Show the dialog and get results.
 */
public void  doDialog()
{
	
	String  Message = "ABCJ Version : " + ABCJ.VERSION + "\n\n"
					+ "Copyright : Steve Spencer-Jowett 2005 onwards" + "\n\n"
					+ "Protected by the GNU public license" ;
	
	JOptionPane.showMessageDialog( mFrame, Message, "About ABCJ",
								   JOptionPane.INFORMATION_MESSAGE ) ;
}

}