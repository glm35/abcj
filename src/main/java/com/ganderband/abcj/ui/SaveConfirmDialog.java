/**
 * The class which shows a the save confirmation dialog.
 */
package  com.ganderband.abcj.ui ;

import  javax.swing.* ;

public class SaveConfirmDialog
{
/**
 * The frame which owns this dialog.
 */
	private JFrame  mFrame ;
/**
 * A static object indicating the options available.
 */
	private static final Object[]  OPTIONS =
					{ "Save and Close", "Close without Saving", "Cancel" } ;

	
/**
 * The standard constructor.
 */
public  SaveConfirmDialog( JFrame Frame )
{	mFrame = Frame ;   }



/**
 * A static doDialog method to save instantiation.
 */
public static int  doDialog( JFrame Frame )
{
	SaveConfirmDialog  Dlg = new SaveConfirmDialog(Frame) ;
	return  Dlg.doDialog() ;
}

/**
 * Show the dialog and get results.
 */
public int  doDialog()
{
	return  JOptionPane.showOptionDialog( mFrame,
					"You have unsaved changes."
				  + "   Please select an appropriate action with the buttons below",
					"Confirm Save Files at Close",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
					null, OPTIONS, OPTIONS[2] ) ;
}

}