/**
 * A simple applet providing some ABCJ function to embed in a web page.
 */

package  com.ganderband.abcj ;

import  javax.swing.* ;
import  com.ganderband.abcj.model.* ;
import  com.ganderband.abcj.ui.* ;

public class ABCJApplet extends JApplet
{
/**
 * The initial tune to show.
 */
	public static final String  START_TUNE =
				  "X:1\n"
				+ "T:Piece of Cake\n"
				+ "R:March/Hornpipe/Scottish\n"
				+ "C:Steve Spencer-Jowett\n"
				+ "M:4/4\n"
				+ "L:1/8\n"
				+ "Q:1/4=200\n"
				+ "K:Am\n"
				+ "|: A2 ed cBAG | .c2 c2 d2 cd | .e2 e2 fedc | B2 AB E4 |\n"
				+ "|  A2 ed cBAG | .c2 c2 d2 cd | .e2 e2 fedc | B2 G2 A4 :|\n"
				+ "|: .e2 e2 agfe | f2 ed e4 | A2 ed cBAc | B2 AB E4 |\n"
				+ "|  .e2 e2 agfe | f2 ed e4 | A2 ed cBAc | B2 G2 A4 :|\n"
				+ "\n" ;
	
/**
 * Initialize the applet.
 * 
 * <p>Create the panel layout of the applet at this point.
 * 
 * <p>This will noyl be called when embedded in a web browser.
 */
public void init()
{
	
//  Create an applet version of the ABCJ object
	
	ABCJ  a = new ABCJ() ;
	
	a.createWorkerThreads() ;
	
//  Now create the GUI for the applet
	
	AppletMainPane  p = new AppletMainPane( a, false ) ;
	
	getContentPane().add(p) ;
	
	a.setGUIInterface(p) ;
	
//  Create a single tune for startup and set it as selected.
//  In the applet version it will not be a problem as it is impossible to select another tune anyway !
	
	Tune  t = new Tune( null, null, "Temp Tune" ) ;
	t.setABCText(START_TUNE) ;
	a.selectTune(t) ;
}

}
