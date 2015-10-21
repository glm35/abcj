/**
 * The dialog representing playback options. 
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  java.awt.event.* ;
import  javax.swing.* ;

import  com.ganderband.abcj.* ;

public class PlayerOptionsDialog extends JDialog implements ActionListener
{
/**
 * The owning frame.
 */
	private JFrame  mFrame ;
/**
 * The instrument combo box.
 */
	private JComboBox  mInstrument = new JComboBox( new String[] {
				"1 Acoustic Grand Piano",
				"2 Bright Acoustic Piano",
				"3 Electric Grand Piano",
				"4 Honky-tonk Piano",
				"5 Electric Piano 1 (Rhodes Piano)",
				"6 Electric Piano 2 (Chorused Piano)",
				"7 Harpsichord",
				"8 Clavinet",
				"9 Celesta",
				"10 Glockenspiel",
				"11 Music Box",
				"12 Vibraphone",
				"13 Marimba",
				"14 Xylophone",
				"15 Tubular Bells",
				"16 Dulcimer (Santur)",
				"17 Drawbar Organ (Hammond)",
				"18 Percussive Organ",
				"19 Rock Organ",
				"20 Church Organ",
				"21 Reed Organ",
				"22 Accordion (French)",
				"23 Harmonica",
				"24 Tango Accordion (Band neon)",
				"25 Acoustic Guitar (Nylon)",
				"26 Acoustic Guitar (Steel)",
				"27 Electric Guitar (Jazz)",
				"28 Electric Guitar (Clean)",
				"29 Electric Guitar (Muted)",
				"30 Overdriven Guitar",
				"31 Distortion Guitar",
				"32 Guitar Harmonics",
				"33 Acoustic Bass",
				"34 Electric Bass (Fingered)",
				"35 Electric Bass (Picked)",
				"36 Fretless Bass",
				"37 Slap Bass 1",
				"38 Slap Bass 2",
				"39 Synth Bass 1",
				"40 Synth Bass 2",
				"41 Violin",
				"42 Viola",
				"43 Cello",
				"44 Contrabass",
				"45 Tremolo Strings",
				"46 Pizzicato Strings",
				"47 Orchestral Harp",
				"48 Timpani",
				"49 String Ensemble 1 (Strings)",
				"50 String Ensemble 2 (Low Strings)",
				"51 Synth Strings 1",
				"52 Synth Strings 2",
				"53 Choir Aahs",
				"54 Voice Oohs",
				"55 Synth Voice",
				"56 Orchestra Hit",
				"57 Trumpet",
				"58 Trombone",
				"59 Tuba",
				"60 Muted Trumpet",
				"61 French Horn",
				"62 Brass Section",
				"63 Synth Brass 1",
				"64 Synth Brass 2",
				"65 Soprano Sax",
				"66 Alto Sax",
				"67 Tenor Sax",
				"68 Baritone Sax",
				"69 Oboe",
				"70 English Horn",
				"71 Bassoon",
				"72 Clarinet",
				"73 Piccolo",
				"74 Flute",
				"75 Recorder",
				"76 Pan Flute",
				"77 Blown Bottle",
				"78 Shakuhachi",
				"79 Whistle",
				"80 Ocarina",
				"81 Lead 1 (Square Wave)",
				"82 Lead 2 (Sawtooth Wave)",
				"83 Lead 3 (Calliope)",
				"84 Lead 4 (Chiffer)",
				"85 Lead 5 (Charang)",
				"86 Lead 6 (Voice Solo)",
				"87 Lead 7 (Fifths)",
				"88 Lead 8 (Bass + Lead)",
				"89 Pad 1 (New Age Fantasia)",
				"90 Pad 2 (Warm)",
				"91 Pad 3 (Polysynth)",
				"92 Pad 4 (Choir Space Voice)",
				"93 Pad 5 (Bowed Glass)",
				"94 Pad 6 (Metallic Pro)",
				"95 Pad 7 (Halo)",
				"96 Pad 8 (Sweep)",
				"97 FX 1 (Rain)",
				"98 FX 2 (Soundtrack)",
				"99 FX 3 (Crystal)",
				"100 FX 4 (Atmosphere)",
				"101 FX 5 (Brightness)",
				"102 FX 6 (Goblins)",
				"103 FX 7 (Echoes, Drops)",
				"104 FX 8 (SciFi, Star Theme)",
				"105 Sitar",
				"106 Banjo",
				"107 Shamisen",
				"108 Koto",
				"109 Kalimba",
				"110 Bagpipe",
				"111 Fiddle",
				"112 Shanai",
				"113 Tinkle Bell",
				"114 Agogo",
				"115 Steel Drums",
				"116 Woodblock",
				"117 Taiko Drum",
				"118 Melodic Tom",
				"119 Synth Drum",
				"120 Reverse Cymbal",
				"121 Guitar Fret Noise",
				"122 Breath Noise",
				"123 Seashore",
				"124 Bird Tweet",
				"125 Telephone Ring",
				"126 Helicopter",
				"127 Applause",
				"128 Gunshot"
			} ) ;
/**
 * The transpose combo box.
 */
	private JComboBox  mTranspose = new JComboBox( new String[] {
				"-12", "-11", "-10", "-9", "-8", "-7", "-6",
				"-5",  "-4",  "-3",  "-2", "-1", "0",
				"+1",  "+2", "+3", "+4", "+5", "+6", 
				"+7", "+8", "+9", "+10", "+11", "+12"  
			} ) ;	
/**
 * The roll type combo box.
 */
	private JComboBox  mRollType = new JComboBox( new String[] {
				"0 No Rolls",
				"1 Simple Roll"
			} ) ;	
/**
 * The grace note check box.
 */
	private JCheckBox  mGraceNotes = new JCheckBox("Play Grace Notes") ;
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
public  PlayerOptionsDialog( JFrame Frame )
{
	super( Frame, true ) ;		// Modal
	mFrame = Frame ;
	
//  Prepare the initial values

	int  w = ABCJProperties.getPropertyInt("Player.Instrument") ;
	if ( w == 0 )   w = 1 ;
	mInstrument.setSelectedIndex(w - 1) ;

	w = ABCJProperties.getPropertyInt("Player.Transpose") ;
	mTranspose.setSelectedIndex(w + 12) ;

	w = ABCJProperties.getPropertyInt("Player.Roll") ;
	mRollType.setSelectedIndex(w) ;

	w = ABCJProperties.getPropertyInt("Player.GraceNotes") ;
	mGraceNotes.setSelected( w != 0 ) ;

//  Now build the GUI
	
	buildGUI() ;
}


/**
 * Build the player GUI.
 */
public void  buildGUI()
{
	setTitle("Player Options") ;
	
//  Create the main panel

	JPanel  MainPanel = new JPanel() ;
	 
	MainPanel.setLayout( new GridLayout( 5, 1 ) ) ;
	MainPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	
	getContentPane().add(MainPanel) ;
	
//  Create the Instrument combo box

	JPanel  p = new JPanel() ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	p.setLayout( new BorderLayout() ) ;
	
	p.add( BorderLayout.WEST, new JLabel("Instrument   ") ) ;
	p.add( BorderLayout.EAST, mInstrument ) ;
	
	MainPanel.add(p) ;
	
//	Create the Transpose combo box

	p = new JPanel() ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	p.setLayout( new BorderLayout() ) ;
	
	p.add( BorderLayout.WEST, new JLabel("Transpose   ") ) ;
	p.add( BorderLayout.EAST, mTranspose ) ;
	
	MainPanel.add(p) ;
	
//	Create the Rolls combo box

	p = new JPanel() ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	p.setLayout( new BorderLayout() ) ;
	
	p.add( BorderLayout.WEST, new JLabel("Rolls   ") ) ;
	p.add( BorderLayout.EAST, mRollType ) ;
	
	MainPanel.add(p) ;
	
//	Create the Grace note check box

	p = new JPanel() ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;

	p.add(mGraceNotes) ;

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
	int  w = mInstrument.getSelectedIndex() ;
	ABCJProperties.setProp( "Player.Instrument", w + 1 ) ;
	
	w = mTranspose.getSelectedIndex() ;
	ABCJProperties.setProp( "Player.Transpose", w - 12 ) ;
	
	w = mRollType.getSelectedIndex() ;
	ABCJProperties.setProp( "Player.Roll", w ) ;
	
	ABCJProperties.setProp( "Player.GraceNotes", mGraceNotes.isSelected() ? 1 : 0  ) ;
}


}
