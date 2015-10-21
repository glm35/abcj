/**
 * The class which shows the tune transpose dialog.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  java.awt.event.* ;
import  javax.swing.* ;
import  com.ganderband.abcj.model.abc.*;

public class TuneTransposeDialog extends JDialog implements ActionListener
{
/**
 * The owning frame.
 */
	private JFrame  mFrame ;
/**
 * The OK button.
 */ 
	private JButton  mOKButton ;
/**
 * The Cancel button.
 */ 
	private JButton  mCancelButton ;
/**
 * Transpose amount selected.
 */
	private int  mTransposeAmount ;
/**
 * The transpose combo box.
 */
	private JComboBox  mTranspose = new JComboBox() ;

	
/**
 * The standard constructor.
 */
public  TuneTransposeDialog( JFrame Frame, Key Key )
{
	super( Frame, true ) ;		// Modal
	mFrame = Frame ;
	
	buildGUI(Key) ;
}


/**
 * Build the player GUI.
 */
public void  buildGUI( Key Key )
{
	setTitle("Transpose Tune") ;
	
//	Build the Transpose panel

	JPanel  TransposePanel = new JPanel() ;
	
	TransposePanel.setLayout( new FlowLayout() ) ;
	TransposePanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	
	TransposePanel.add( BorderLayout.WEST, new JLabel("Transpose   ") ) ;
	TransposePanel.add( BorderLayout.EAST, mTranspose ) ;
	
//  Populate the transpose combo box
	
	setupComboBox(Key) ;

//  Build the buttons panel

	mOKButton     = new JButton("OK") ;
	mCancelButton = new JButton("Cancel") ;
	
	mOKButton.addActionListener(this) ;
	mCancelButton.addActionListener(this) ;

	JPanel  ButtonsPanel = new JPanel() ;
	ButtonsPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	ButtonsPanel.setLayout( new GridLayout( 1, 2 ) ) ;

	ButtonsPanel.add(mOKButton) ;
	ButtonsPanel.add(mCancelButton) ;
	

//  Now add the components in the right place
	
	getContentPane().setLayout( new BorderLayout() ) ;
	
	getContentPane().add( BorderLayout.NORTH, TransposePanel ) ;
	getContentPane().add( BorderLayout.SOUTH, ButtonsPanel ) ;

	pack() ;
	
//  Centre in the window

	Rectangle  rf = mFrame.getBounds() ;
	Rectangle  r  = getBounds() ;
	
	setLocation( rf.x + ( rf.width - r.width ) / 2, rf.y + ( rf.height - r.height ) / 2 ) ;
}


/**
 * List to the button events.
 */
public void  actionPerformed( ActionEvent e )
{
		
//  Handle the cancel button

	if ( e.getSource() == mCancelButton ) {
		mTransposeAmount = 0 ;
		dispose() ;
	}
	
//	Handle the OK button

	if ( e.getSource() == mOKButton ) {
		mTransposeAmount =  mTranspose.getSelectedIndex() - 12 ;
		dispose() ;
	}
}


/**
 * A static doDialog method to save instantiation.
 */
public static int  doDialog( JFrame Frame, Key Key )
{
	TuneTransposeDialog  Dlg = new TuneTransposeDialog(Frame, Key ) ;
	Dlg.show() ;
	return  Dlg.mTransposeAmount ;
}


/**
 * Setup the combo box.
 */
public void  setupComboBox( Key Key )
{
	for ( int i = -12 ; i <= 12 ; i++ ) {
		
	//  Format the number
		
		String  Number = Integer.toString(i) ;
		if ( i > 0 )   Number = "+" + Number ;
		
	//  Determine target key name
	
		ABCTransposer  Transposer = new ABCTransposer(i) ;
		
		Key  Work = null ;
		
		try {
			Work = new Key( Key.getBaseNote(), Key.getMode(), Key.getGlobalAccidentals() ) ;
		}
		catch ( Exception e ) {
			System.out.println("**** PROBLEM in TuneTransposeDialog !!! ****") ;
		}
		
		Transposer.transpose(Work) ;
		String  KeyName = Work.toString() ;
		
	//  Add the item to the combo box
		
		mTranspose.addItem( Number + "    " + KeyName ) ;
	}

//  Position the cursor and listen to it
	
	mTranspose.setSelectedIndex(12) ;
	
	mTranspose.addActionListener(this) ;
}

}