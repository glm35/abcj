/**
 * The class which shows the zoom factor dialog.
 */
package  abcj.ui ;

import  javax.swing.* ;

public class ZoomDialog
{
/**
 * The frame which owns this dialog.
 */
	private JFrame  mFrame ;
/**
 * A static array of the zooming possibilities.
 */
	private static final Object[]  CHOICES = {
						    "50%",  "60%",  "70%",  "80%",  "90%",  
						   "100%", "110%", "120%", "130%", "140%",
						   "150%", "160%", "170%", "180%", "190%",
						   "200%", "250%", "300%"
						} ;
	

/**
 * The standard constructor.
 */
public  ZoomDialog( JFrame Frame )
{	mFrame = Frame ;   }


/**
 * A static doDialog method to save instantiation.
 */
public static float  doDialog( JFrame Frame, float ZoomFactor )
{
	ZoomDialog  Dlg = new ZoomDialog(Frame) ;
	return  Dlg.doDialog(ZoomFactor) ;
}


/**
 * Show the dialog and get results.
 */
public float  doDialog( float ZoomFactor )
{

//  Convert the current zoom factor to a string.

	String  StartValue = Integer.toString( (int) ( ZoomFactor * 100 ) ) + "%" ;
	
	String  Result = (String) JOptionPane.showInputDialog(
									mFrame,
									"Select the required zoom factor\n",
									"Select Zoom Factor",
									JOptionPane.QUESTION_MESSAGE,
									null, CHOICES, StartValue ) ;

//  Exit if cancelled or convert back to a float and return

	if ( Result == null )   return  0 ;
	
	Result = Result.substring( 0, Result.length() - 1 ) ;
	
	return  ( (float) Integer.parseInt(Result) ) / 100 ;
}

}