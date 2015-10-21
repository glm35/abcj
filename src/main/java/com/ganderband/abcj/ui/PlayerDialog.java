/**
 * The class which shows the tune player dialog.
 * 
 * <p>Check whether a midi player can be created as there are some conflicts with later
 * JVMs if JMF has been installed.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  java.awt.event.* ;
import  javax.swing.* ;
import  javax.sound.midi.* ;
import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.util.* ;
import  com.ganderband.abcj.midi.* ;

public class PlayerDialog extends JDialog implements ActionListener, Runnable
{
/**
 * The owning frame.
 */
	private JFrame  mFrame ;
/**
 * The owning main GUI.
 */
	private MainGUI  mMainGUI ;
/**
 * The relevant MIDI sequence.
 */
	ABCSequence  mSequence ;
/**
 * The sequencer object used to play the sequence.
 * 
 * <p>This is package scope to prevent 'synthetic accessor' warnings in inner classes.
 */
	Sequencer  mSequencer ;
/**
 * The Play/Pause button.
 */
	private JButton  mPlayPauseButton ;
/**
 * The Stop button.
 */
	private JButton  mStopButton ;	
/**
 * The Close button.
 */
	private JButton  mCloseButton ;
/**
 * The progress bar showing the music playing.
 */	
	private  JProgressBar  mProgressBar = new JProgressBar() ;
/**
 * The checkbox indicating looped playback.
 */
	private  JCheckBox  mLoopCheck = new JCheckBox( "Loop", true ) ;
/**
 * The microsecond position of the sequencer.
 */
	private long  mPosition ;
/**
 * The microsecond position of the cue start position.
 */
	private long  mCueStartPos ;
/**
 * The microsecond position of the cue end position.
 */
	private long  mCueEndPos ;
/**
 * A flag to indicate we are currently playing.
 */
	private boolean  mIsPlaying ;
/**
 * The status thread.
 */
	private Thread  mStatusThread ;
/**
 * A flag to indicate the status thread is running.
 */
	private boolean  mIsRunning ;
/**
 * The start position label.
 */
	private JLabel  mStartPos = new JLabel("00:00.00") ;
/**
 * The end position label.
 */
	private JLabel  mEndPos = new JLabel("99:99.99") ;
/**
 * The current position label.
 */
	private JLabel  mCurrPos = new JLabel("55:55.55") ;
/**
 * The Cue Start button.
 */
	private JButton  mCueStartButton = new JButton("Cue Start") ;
/**
 * The Cue Start label.
 */
	private JLabel  mCueStart = new JLabel("00:00:00") ;
/**
 * The Cue End button.
 */
	private JButton  mCueEndButton = new JButton("Cue End") ;
/**
 * The Cue End label.
 */
	private JLabel  mCueEnd = new JLabel("99:99:99") ;
/**
 * The Cue Reset button.
 */
	private JButton  mCueResetButton = new JButton("Cue Reset") ;
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
 * The active transpose amount.
 */
	private int  mTransposeAmount ;
/**
 * A flag to indicate dialog and player created OK.
 * 
 * <p>There are some problems with JMF conflicting with the java sound API which can
 * cause the player to lockup with "55:55.55" shown in the display on older versions
 * of ABCJ.   This flag will allow these to trapped on the later version.
 */
	private boolean  mIsOK = false ;
	
	
/**
 * The standard constructor.
 */
public  PlayerDialog( JFrame Frame, MainGUI GUI, ABCSequence Seq )
{
	super( Frame, true ) ;		// Modal
	mFrame   = Frame ;
	mMainGUI = GUI ;
	
	mSequence = Seq ;
	
	buildGUI() ;
	
//  Terminate the sequencer when this dialog is closed
	
	addWindowListener( new WindowAdapter() {
					public void windowClosing( WindowEvent e ) {
						stopStatusThread() ;
						mSequencer.stop() ;
						mSequencer.close() ;
					}
				} ) ;

//  Now build the sequencer and start it running

	try {
		mSequencer = MidiSystem.getSequencer() ;
		
		mSequencer.open() ;
		mSequencer.setSequence( mSequence.getMidiSequence() ) ;

		mPosition = 0 ;		
		mCueStartPos = 0 ;
		mCueEndPos = mSequencer.getMicrosecondLength() ;
		
		
		mStartPos.setText( formatMicroseconds(0) ) ;
		mCurrPos.setText( formatMicroseconds(0) ) ;
		mEndPos.setText( formatMicroseconds(mCueEndPos) ) ;

		mCueStart.setText( formatMicroseconds(0) ) ;
		mCueEnd.setText( formatMicroseconds(mCueEndPos) ) ;
		
		mProgressBar.setMinimum(0) ;
		mProgressBar.setMaximum( (int) mCueEndPos ) ;

		mSequencer.start() ;
		setPlaying(true) ;
	}
	catch ( Exception e ) {
		System.out.println("PlayerDialog failed") ;
		e.printStackTrace(System.out)  ;
		return  ;	// Return immediately with not OK
	}
	
//  Finally start the status thread and indicate OK

	mStatusThread = new Thread(this) ;
	mStatusThread.start() ;
	
	mIsOK = true ;
}


/**
 * Is the dialog OK ?
 */
public boolean  isOK()
{	return  mIsOK ;   }


/**
 * Build the player GUI.
 */
public void  buildGUI()
{
	setTitle("Play Tune") ;

//  Build the transport buttons panel

	mPlayPauseButton  = new JButton( "Pause", new ImageIcon( Utils.getImage( this, "images/pauseblue.gif") ) ) ;
	mStopButton  = new JButton( "Stop", new ImageIcon( Utils.getImage( this, "images/stopblue.gif") ) ) ;
	mCloseButton = new JButton( "Close" ) ;
	
	mPlayPauseButton.setVerticalTextPosition(SwingConstants.BOTTOM) ;
	mPlayPauseButton.setHorizontalTextPosition(SwingConstants.CENTER) ;
	mStopButton.setVerticalTextPosition(SwingConstants.BOTTOM) ;
	mStopButton.setHorizontalTextPosition(SwingConstants.CENTER) ;
	
	mPlayPauseButton.addActionListener(this) ;
	mStopButton.addActionListener(this) ;
	mCloseButton.addActionListener(this) ;
	
	mLoopCheck.setHorizontalTextPosition(SwingConstants.LEFT) ;

	JPanel  p = new JPanel() ;
	p.setLayout( new GridLayout( 1, 3 ) ) ;

	p.add(mPlayPauseButton) ;
	p.add(mStopButton) ;
	p.add(mCloseButton) ;
	
	JPanel  TransportPanel = new JPanel() ;
	
	TransportPanel.setLayout( new BorderLayout() ) ;
	TransportPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	
	TransportPanel.add( BorderLayout.NORTH, p ) ;
//	TransportPanel.add( BorderLayout.SOUTH, mLoopCheck ) ;	// Looping cannot be set !!
	
//  Build the progress bar for the status panel

	JPanel  StatusPanel = new JPanel() ;
	
	StatusPanel.setLayout( new BorderLayout() ) ;
	StatusPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	
	StatusPanel.add( BorderLayout.NORTH, mProgressBar ) ;
	
	p = new JPanel() ;
	p.setLayout( new GridLayout( 1, 3 ) ) ;
	p.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) ;
	p.setBackground(Color.black) ;
	
	mStartPos.setHorizontalAlignment(SwingConstants.LEFT) ;
	mCurrPos.setHorizontalAlignment(SwingConstants.CENTER) ;
	mEndPos.setHorizontalAlignment(SwingConstants.RIGHT) ;
	
	mCurrPos.setForeground(Color.green) ;
	
	p.add(mStartPos) ;
	p.add(mCurrPos) ;
	p.add(mEndPos) ;
	
	StatusPanel.add( BorderLayout.SOUTH, p ) ;

//  Build the cueing panel

	mCueStart.setHorizontalAlignment(SwingConstants.CENTER) ;
	mCueEnd.setHorizontalAlignment(SwingConstants.CENTER) ;

	mCueStartButton.addActionListener(this) ;
	mCueEndButton.addActionListener(this) ;
	mCueResetButton.addActionListener(this) ;

	JPanel  CueingPanel = new JPanel() ;
	
	CueingPanel.setLayout( new FlowLayout() ) ;
	CueingPanel.setBorder( BorderFactory.createTitledBorder("Cueing") ) ;
	
	CueingPanel.setLayout( new GridLayout( 2, 3 ) ) ;
	
	CueingPanel.add(mCueStart) ;
	CueingPanel.add( new JLabel() ) ;
	CueingPanel.add(mCueEnd) ;
	CueingPanel.add(mCueStartButton) ;
	CueingPanel.add(mCueResetButton) ;
	CueingPanel.add(mCueEndButton) ;
	
//	Create the Transpose panel

	JPanel  TransposePanel = new JPanel() ;
	
	TransposePanel.setLayout( new FlowLayout() ) ;
	TransposePanel.setBorder( BorderFactory.createTitledBorder("Transpose") ) ;
	
	TransposePanel.add( BorderLayout.WEST, new JLabel("Transpose   ") ) ;
	TransposePanel.add( BorderLayout.EAST, mTranspose ) ;
	
	mTransposeAmount = ABCJProperties.getPropertyInt("Player.Transpose") ;
	mTranspose.setSelectedIndex( mTransposeAmount + 12 ) ;
	
	mTranspose.addActionListener(this) ;

//  Now add the components in the right place
	
	getContentPane().setLayout( new BorderLayout() ) ;
	
	getContentPane().add( BorderLayout.NORTH,  TransportPanel ) ;
	getContentPane().add( BorderLayout.CENTER, StatusPanel ) ;
	
	p = new JPanel() ;
	p.setLayout( new BorderLayout() ) ;
	
	p.add( BorderLayout.NORTH, CueingPanel ) ;
	p.add( BorderLayout.SOUTH, TransposePanel ) ;

	getContentPane().add( BorderLayout.SOUTH, p ) ;
	
	pack() ;
	
//  Locate the owning window bounds.    Note that we may be running as an applet in which
//  case there is no owning frame and we should use the applet bounds instead
	
	Rectangle  rf ;
	
	if ( mFrame != null )
		rf = mFrame.getBounds() ;
	else
		rf = ( (AppletMainPane) mMainGUI ).getBounds() ;
	
//  Centre in the window

	Rectangle  r  = getBounds() ;
	
	setLocation( rf.x + ( rf.width - r.width ) / 2, rf.y + ( rf.height - r.height ) / 2 ) ;
}

/**
 * List to the button events.
 */
public void  actionPerformed( ActionEvent e )
{
	
//  Handle the play/pause button

	if ( e.getSource() == mPlayPauseButton ) {
		
		if ( mIsPlaying ) {		// Pause
			mPosition = mSequencer.getMicrosecondPosition() ;
			mSequencer.stop() ;
			setPlaying(false) ;
		}
		else {					// Play
			mSequencer.setMicrosecondPosition(mPosition) ;
			mSequencer.start() ;
			setPlaying(true) ;
		}
	}
	
//	Handle the stop button

	if ( e.getSource() == mStopButton ) {
		mSequencer.stop() ;
		mPosition = mCueStartPos ;
		mSequencer.setMicrosecondPosition(mCueStartPos) ;
		setPlaying(false) ;
	}
	
//	Handle the close button

	if ( e.getSource() == mCloseButton ) {
		stopStatusThread() ;
		mSequencer.stop() ;
		mSequencer.close() ;
		dispose() ;
	}
	
//	Handle the cue start button

	if ( e.getSource() == mCueStartButton ) {
		mCueStartPos = mSequencer.getMicrosecondPosition() ;
		mCueStart.setText( formatMicroseconds(mCueStartPos) ) ;
	}
	
//	Handle the cue end button

	if ( e.getSource() == mCueEndButton ) {
		mCueEndPos = mSequencer.getMicrosecondPosition() ;
		mCueEnd.setText( formatMicroseconds(mCueEndPos) ) ;
	}
	
//	Handle the cue reset button

	if ( e.getSource() == mCueResetButton ) {
		mCueStartPos = 0 ;
		if ( ! mIsPlaying )   mPosition = 0 ;
		mCueStart.setText( formatMicroseconds(0) ) ;
		mCueEndPos = mSequencer.getMicrosecondLength() ;
		mCueEnd.setText( formatMicroseconds(mCueEndPos) ) ;
	}
	
//	Handle the loop checkbox

	if ( e.getSource() == mLoopCheck ) {
	// Do nothing - the sequencer can't handle it !!
	}
	
//	Handle the transpose combo box

	if ( e.getSource() == mTranspose ) {
	
		int  w = mTranspose.getSelectedIndex() - 12 ;
		int  RelativeTranspose = w - mTransposeAmount ;
		mTransposeAmount = w ;
	
		try {
			mSequence.transpose(RelativeTranspose) ;
			mSequencer.setSequence( mSequence.getMidiSequence() ) ;
		}
		catch ( Exception x ) {
			System.out.println("PlayerDialog  transpose failed") ;
			x.printStackTrace(System.out)  ;
		}
	}
}


/**
 * Setup playing or paused.
 * 
 * <p>This method sets the button states accordingly.
 */
public void  setPlaying( boolean Flag )
{
	if ( mIsPlaying == Flag )   return ;
	mIsPlaying = Flag ;
	
//  Now set the play pause button accordingly

	if ( Flag ) {
		mPlayPauseButton.setText("Pause") ;
		mPlayPauseButton.setIcon( new ImageIcon( Utils.getImage( this, "images/pauseblue.gif") ) ) ;
		mCueStartButton.setEnabled(true) ;
		mCueEndButton.setEnabled(true) ;
		mTranspose.setEnabled(false) ;
	}
	else {
		mPlayPauseButton.setText("Play") ;
		mPlayPauseButton.setIcon( new ImageIcon( Utils.getImage( this, "images/playblue.gif") ) ) ;
		mCueStartButton.setEnabled(false) ;
		mCueEndButton.setEnabled(false) ;
		mTranspose.setEnabled(true) ;
	}
}


/**
 * Stop the status thread.
 * 
 * <p>This method requests the thread to stop and waits for it to complete.
 */
public void  stopStatusThread()
{
	mStatusThread.interrupt() ;
	while ( mIsRunning )   Utils.sleep(10) ;	
}


/**
 * The run method showing the progress bar and timer.
 */
public void  run()
{
	mIsRunning = true ;
	
//  Infinite loop till interrupted

	try {
		while ( true ) {
			Thread.sleep(100) ;
			
		//  Check if the sequencer has stopped even though the play flag is set
		
			if ( mIsPlaying  &&  ! mSequencer.isRunning() )
				setPlaying(false) ;
				
		//  Also stop if we're past the end cue mark
		
			long  Pos = mSequencer.getMicrosecondPosition() ;
		
			if ( mIsPlaying  &&  Pos > mCueEndPos ) {
				mPosition = mCueStartPos ;
				mSequencer.stop() ;
				setPlaying(false) ;
			}
				
		//  Now update the relevant status
			
			if ( ! mIsPlaying )   Pos = mPosition ;
			
			mCurrPos.setText( formatMicroseconds(Pos) ) ;
			mProgressBar.setValue( (int) Pos ) ;
		}
	}
	catch ( InterruptedException e ) {
//		System.out.println( "Exception ignored - " + e ) ;
//		e.printStackTrace() ;
	}
	
	mIsRunning = false ;
}


/**
 * A helper method to format microseconds into MM:SS.ss.
 */
public String  formatMicroseconds( long ms )
{
	ms = ms / 10000 ;		// Conver to 100ths of a second
	
	String  s = "" ;
	
//  Format minutes

	long  x = ms / 100 / 60 ;	
	String  w = Long.toString( x + 100 ) ;		// Add 100 so > 2 digits
	s += w.substring( w.length() - 2 ) + ":" ; 
	
	ms -= x * 100 * 60 ;
	
//	Format seconds

	x = ms / 100 ;	
	w = Long.toString( x + 100 ) ;		// Add 100 so > 2 digits
	s += w.substring( w.length() - 2 ) + "." ;		// Keep last 2 digits only
	
	ms -= x * 100 ; 
	
//	Format hundredths

	w = Long.toString( ms + 100 ) ;		// Add 100 so > 2 digits
	s += w.substring( w.length() - 2 ) ;		// Keep last 2 digits only 

	return  s ;
}

}