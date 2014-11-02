/**
 * The class which shows a the file overwrite dialog.
 */
package  abcj.ui ;

import  javax.swing.* ;

public class FileOverwriteDialog
{
/**
 * The frame which owns this dialog.
 */
	private JFrame  mFrame ;
/**
 * A static object indicating the options available.
 */
	private static final Object[]  OPTIONS = { "Overwrite", "Cancel" } ;

	
/**
 * The standard constructor.
 */
public  FileOverwriteDialog( JFrame Frame )
{	mFrame = Frame ;   }



/**
 * A static doDialog method to save instantiation.
 */
public static int  doDialog( JFrame Frame, String FileName )
{
	FileOverwriteDialog  Dlg = new FileOverwriteDialog(Frame) ;
	return  Dlg.doDialog(FileName) ;
}

/**
 * Show the dialog and get results.
 */
public int  doDialog( String FileName )
{
	return  JOptionPane.showOptionDialog( mFrame,
						"File " + FileName + " already exists."
								+ "   Select 'Overwrite' or 'Cancel'",
						"Warning",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null, OPTIONS, OPTIONS[1] ) ;
}

}