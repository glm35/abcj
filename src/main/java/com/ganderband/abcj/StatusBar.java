/**
 * A class repesenting the status bar.
 */
package  com.ganderband.abcj ;

import  javax.swing.* ;
import  java.awt.* ;

public class StatusBar extends JPanel
{
/**
 * The status label inside this panel.
 */
private JLabel  mLabel ;


/**
 * The default constructor.
 */
public  StatusBar()
{
	setAlignmentX(Component.LEFT_ALIGNMENT) ;		// So it works OK in a BoxLayout

	setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) ) ;

	mLabel = new JLabel("") ;
//	mLabel.setSize(20,1000);
	add(mLabel) ;	
}

	
/**
 * Set the text on the component and paint it immediately.
 */
public void  setText( String Text )
{	
	String  Blanks = "                                                  " ;
	
//  Add a lot of blanks so any following immedaite paints work OK
	
	Text += Blanks + Blanks + Blanks + Blanks + Blanks + Blanks + Blanks + Blanks ;
	mLabel.setText(Text) ;
}

	
/**
 * Set the text on the component and paint it immediately.
 */
public void  setTextImmediately( String Text )
{	
	mLabel.setText(Text) ;
	paintImmediately( 0, 0, getSize().width, getSize().height ) ;
}

}
