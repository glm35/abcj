/**
 * The class which shows the tune book title dialog.
 */
package  abcj.ui ;

import  javax.swing.* ;

public class TuneBookTitleDialog
{
/**
 * The frame which owns this dialog.
 */
	private JFrame  mFrame ;

	
/**
 * The standard constructor.
 */
public  TuneBookTitleDialog( JFrame Frame )
{	mFrame = Frame ;   }


/**
 * A static doDialog method to save instantiation.
 */
public static String  doDialog( JFrame Frame, String FileName, String Title )
{
	TuneBookTitleDialog  Dlg = new TuneBookTitleDialog(Frame) ;
	return  Dlg.doDialog( FileName, Title ) ;
}

/**
 * Show the dialog and get results.
 */
public String  doDialog( String FileName, String Title )
{
	return  (String) JOptionPane.showInputDialog(
								 mFrame,
								 "Changing title for - " + FileName +"\n\n"
								   + "Enter new title below\n\n",
								 "Set Book Title",
								 JOptionPane.QUESTION_MESSAGE, null, null,
								 Title ) ;
}

}