/**
 * The ABCJ Splash screen.
 * 
 * This can be clicked to remove it or it will disappear after 10 seconds.
 */
package  com.ganderband.abcj ;

import  java.awt.* ;
import  java.awt.event.* ;
import  javax.swing.* ;

import com.ganderband.abcj.util.Utils;

public class SplashWindow extends JWindow
{
	
/**
 * The main constructor.
 */
public  SplashWindow( String FileName, JFrame Frame, int WaitTime )
{
	super(Frame) ;
	
//  Build the splash screen itself

	JLabel  Label = new JLabel( new ImageIcon( Utils.getImage( this, FileName ) ) ) ;
	getContentPane().add( Label, BorderLayout.CENTER) ;
	pack() ;
	
//  Centre in the screen

	Dimension  ScreenSize = Toolkit.getDefaultToolkit().getScreenSize() ;
	Dimension  LabelSize = Label.getPreferredSize() ;
	setLocation( ScreenSize.width  / 2 - ( LabelSize.width  / 2 ),
				 ScreenSize.height / 2 - ( LabelSize.height / 2 ) ) ;
				 
//  Add a mouse listener to allow it to be closed by clicking

	addMouseListener ( new MouseAdapter() {
				public void  mousePressed( MouseEvent e )
				{
					setVisible(false) ;
					dispose() ;
				}
			} ) ;
			
			
//  Create a closer item (for running on a separate thread) to close
//  the splash screen
			
	final int  Pause = WaitTime ;
	final Runnable  CloserRunner = new Runnable() {
				public void  run()
				{
					setVisible(false) ;
					dispose() ;
				}
			} ;
			
//  Now add a delaying thread to cause the splash screen to be closed
//  at the requested time
 
	Runnable  WaitRunner = new Runnable() {
				public void  run()
				{
					try	{
						Thread.sleep(Pause) ;
						SwingUtilities.invokeAndWait(CloserRunner) ;
					}
					catch ( Exception e ) {
							e.printStackTrace(System.out) ;
					}
				}
			} ;
			
			
//  Set the window visible and start the waiting thread.
//  The wait thread is separate to the normal swing event thread but will submit
//  the close request at the appropriate time on the normal swing thread
 
	setVisible(true) ;
		
	( new Thread( WaitRunner, "SplashThread" ) ).start() ;
}
}

