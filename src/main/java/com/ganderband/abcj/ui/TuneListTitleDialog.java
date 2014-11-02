/**
 * The class which shows the tune list title dialog.
 */
package  abcj.ui ;

import  javax.swing.* ;

public class TuneListTitleDialog
{
/**
 * The frame which owns this dialog.
 */
	private JFrame  mFrame ;

	
/**
 * The standard constructor.
 */
public  TuneListTitleDialog( JFrame Frame )
{	mFrame = Frame ;   }



/**
 * A static doDialog method to save instantiation.
 */
public static String  doDialog( JFrame Frame, String Title )
{
	TuneListTitleDialog  Dlg = new TuneListTitleDialog(Frame) ;
	return  Dlg.doDialog(Title) ;
}

/**
 * Show the dialog and get results.
 */
public String  doDialog( String Title )
{
	return  (String) JOptionPane.showInputDialog(
								 mFrame,
								 "Changing title for tune list\n\n"
								   + "Enter new title below\n\n",
								 "Set Tune List Title",
								 JOptionPane.QUESTION_MESSAGE, null, null,
								 Title ) ;
}

}