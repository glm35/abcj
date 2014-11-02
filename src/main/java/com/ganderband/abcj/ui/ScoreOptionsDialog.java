/**
 * The dialog representing scoring options. 
 */
package  abcj.ui ;

import  java.awt.* ;
import  java.awt.event.* ;
import  javax.swing.* ;
import  abcj.* ;

public class ScoreOptionsDialog extends JDialog implements ActionListener
{
/**
 * The owning frame.
 */
	private JFrame  mFrame ;
/**
 * The stave width combo box.
 */
	private JComboBox  mStaveWidth = new JComboBox( new String[] {
				"400",  "450",  "500",  "550",  "600",  "650",
				"700",  "750",  "800",  "850",  "900",  "950",
			   "1000", "1050", "1100", "1150", "1200"
			} ) ;
/**
 * The Show Tempo check box.
 */
	private JCheckBox  mShowTempo = new JCheckBox("Show Tempo") ;
/**
 * The Ignore ABC Line Breaks check box.
 */
	private JCheckBox  mIgnoreBreaks = new JCheckBox("Ignore ABC Line Breaks") ;
/**
 * The Show Clef check box.
 */
	private JCheckBox  mShowClef = new JCheckBox("Show Clef on each line") ;
/**
 * The Show Header Info fields check box.
 */
	private JCheckBox  mShowInfoFields = new JCheckBox("Show Info Fields") ;
/**
 * The Show Lyrics check box.
 */
	private JCheckBox  mShowLyrics = new JCheckBox("Show Lyrics") ;
/**
 * The OK button.
 */	
	private JButton  mOKButton = new JButton("OK") ;
/**
 * The Cancel button.
 */	
	private JButton  mCancelButton = new JButton("Cancel") ;


/**
 * The standard constructor.
 */
public  ScoreOptionsDialog( JFrame Frame )
{
	super( Frame, true ) ;		// Modal
	mFrame = Frame ;
	
//  Prepare the initial values

	String  s = Integer.toString( ABCJProperties.getPropertyInt("Score.StaveWidth") ) ;
	if ( s == null  ||  s.length() == 0 )   s = "600" ;
	mStaveWidth.setSelectedItem(s) ;

	int  w = ABCJProperties.getPropertyInt("Score.ShowTempo") ;
	mShowTempo.setSelected( w != 0 ) ;

	w = ABCJProperties.getPropertyInt("Score.IgnoreBreaks") ;
	mIgnoreBreaks.setSelected( w != 0 ) ;

	w = ABCJProperties.getPropertyInt("Score.ShowClef") ;
	mShowClef.setSelected( w != 0 ) ;

	w = ABCJProperties.getPropertyInt("Score.ShowInfoFields") ;
	mShowInfoFields.setSelected( w != 0 ) ;

	w = ABCJProperties.getPropertyInt("Score.ShowLyrics") ;
	mShowLyrics.setSelected( w != 0 ) ;

//  Now build the GUI
	
	buildGUI() ;
}


/**
 * Build the player GUI.
 */
public void  buildGUI()
{
	setTitle("Score Options") ;
	
//  Create the main panel

	JPanel  MainPanel = new JPanel() ;
	 
	MainPanel.setLayout( new BoxLayout( MainPanel, BoxLayout.Y_AXIS ) ) ;	
	MainPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	
	getContentPane().add(MainPanel) ;
	
//  Create the Stave Width combo box

	JPanel  p = new JPanel() ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	p.setLayout( new BorderLayout() ) ;
	
	p.add( BorderLayout.WEST, new JLabel("Stave Width   ") ) ;
	p.add( BorderLayout.EAST, mStaveWidth ) ;
	
	MainPanel.add(p) ;
	
//  Add the various option selections

	p = new JPanel() ;
	p.setLayout( new GridLayout( 5, 1 ) ) ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;

	p.add(mShowTempo) ;
	p.add(mIgnoreBreaks) ;
	p.add(mShowClef) ;
	p.add(mShowInfoFields) ;
	p.add(mShowLyrics) ;
	
	MainPanel.add(p) ;
	
//  Add the OK and cancel buttons

	p = new JPanel() ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	p.setLayout( new GridLayout( 1, 3 ) ) ;
	
	p.add(mOKButton) ;
	p.add( new JLabel() ) ;
	p.add(mCancelButton) ;
	
	MainPanel.add(p) ;
	
	pack() ;
	
//  Centre in the window

	Rectangle  rf = mFrame.getBounds() ;
	Rectangle  r  = getBounds() ;
	
	setLocation( rf.x + ( rf.width - r.width ) / 2, rf.y + ( rf.height - r.height ) / 2 ) ;

//  Listen to relevant buttons
	
	mOKButton.addActionListener(this) ;
	mCancelButton.addActionListener(this) ;
}


/**
 * Called when a button is pressed. 
 */
public void  actionPerformed( ActionEvent e ) {
	
//  Handle the cancel button

	if ( e.getSource() == mCancelButton )
		dispose() ;

//  Handle the OK button

	if ( e.getSource() == mOKButton ) {
		saveValues() ;
		dispose() ;
	}
}
	

/**
 * Save the relevant values to the ABCJ properties file.
 */
public void  saveValues()
{
	String  s = (String) mStaveWidth.getSelectedItem() ;
	ABCJProperties.setProp( "Score.StaveWidth", Integer.parseInt(s) ) ;

	ABCJProperties.setProp( "Score.ShowTempo", mShowTempo.isSelected() ? 1 : 0  ) ;
	ABCJProperties.setProp( "Score.IgnoreBreaks", mIgnoreBreaks.isSelected() ? 1 : 0  ) ;
	ABCJProperties.setProp( "Score.ShowInfoFields", mShowInfoFields.isSelected() ? 1 : 0  ) ;
	ABCJProperties.setProp( "Score.ShowLyrics", mShowLyrics.isSelected() ? 1 : 0  ) ;
	ABCJProperties.setProp( "Score.ShowClef", mShowClef.isSelected() ? 1 : 0  ) ;
}


}
