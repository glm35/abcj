/**
 * The main class to instantiate ABCJunior.
 */
package  com.ganderband.abcj ;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import  javax.swing.* ;

import com.ganderband.abcj.model.Tune;
import com.ganderband.abcj.ui.AppletMainPane;

public class ABCJunior extends ABCJ
{
/**
 * The main applet pane.
 */
	private AppletMainPane  mAppletMainPane ;
	
	
/**
 * The default constructor.
 */
public  ABCJunior()
{
//  Nothing needed at present.
}


/**
 * Initialize as an application.
 */
public void  initApplication()
{

//  Load the properties

	ABCJProperties.load() ;
	
//	Create and set up the junior GUI
	
	createGUI() ;

//  Create the worker threads
	
	createWorkerThreads() ;
	
//  Create a single tune for startup and set it as selected.
//  In the applet version it will not be a problem as it is impossible to select another tune anyway !
	
	Tune  t = new Tune( null, null, "Temp Tune" ) ;
	t.setABCText(ABCJApplet.START_TUNE) ;
	selectTune(t) ;
	
//	Finally display the frame full screen to activate the application.

//	mFrame.pack() ;
	mFrame.setVisible(true) ;
}


/**
 * Create the standard GUI.
 * 
 * <p>The standard GUI for ABCJunior
 */
public void  createGUI()
{
	
//	Create and set up the Swing Frame.
		
	mFrame = new JFrame("ABCJunior") ;

	mFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE) ;
	mFrame.addWindowListener(this) ;

//  Build the applet version of ABCJ for ABCJunior

	mAppletMainPane = new AppletMainPane( this, true ) ;
	
	mFrame.getContentPane().add(mAppletMainPane) ;
	
	setGUIInterface(mAppletMainPane) ;
	
//  Set the initial GUI state from the properties file

	setInitialGUIState() ;
}



/**
 * Set the initial GUI state.
 */
public void  setInitialGUIState()
{
	setEditMode( ABCJProperties.getEditMode() ) ;
	setLookAndFeel( ABCJProperties.getLookAndFeel() ) ;

//  Set the state of the main pane (e.g. divider locations)

	mAppletMainPane.setInitialGUIState() ;

//  Set the initial frame rectangle from the properties file
//  This defaults to filling the screen

		Rectangle  r = ABCJProperties.getPropertyRect("FrameRect") ;

	if ( r.isEmpty() ) {
		Dimension  d = Toolkit.getDefaultToolkit().getScreenSize() ;
		r = new Rectangle( 0, 0, d.width, d.height ) ;
	}

	mFrame.setBounds(r) ;
}


/**
 * Save the GUI state to the properties file.
 */
public void  saveGUIState()
{
	mAppletMainPane.saveGUIState() ;

	ABCJProperties.setProp( "FrameRect", mFrame.getBounds() ) ;
}


}
