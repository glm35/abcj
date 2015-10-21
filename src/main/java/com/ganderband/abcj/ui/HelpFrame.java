/**
 * A simple help frame class for ABCJ. 
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import  javax.swing.* ;
import  javax.swing.text.html.* ;

public class HelpFrame extends JFrame
{
	JEditorPane  mHtmlPane ;
	
/**
 * The main constructor.
 *
 */
public  HelpFrame()
{
	setTitle("ABCJ Help") ;
	setSize( 400, 300 ) ;
	
	getContentPane().setLayout( new BorderLayout() ) ;

//  Create the main panel	
	
	JPanel  TopPanel = new JPanel() ;
	TopPanel.setLayout( new BorderLayout() ) ;
	
	getContentPane().add( TopPanel, BorderLayout.CENTER ) ;

   	try {
   		
    //  Create an HTML editor pane
	    
		mHtmlPane = new JEditorPane() ;
	    mHtmlPane.setEditable(false) ;
	    mHtmlPane.setEditorKit( new HTMLEditorKit() ) ;
	
	//  Add it to the scroll panel
	    
	    JScrollPane  ScrollPane = new JScrollPane() ;
	    ScrollPane.getViewport().add( mHtmlPane, BorderLayout.CENTER ) ;

	    TopPanel.add( ScrollPane, BorderLayout.CENTER ) ;
	    
//	    html.addHyperlinkListener( this );
	    
   	//  Load the file we want to display
		   
	    Cursor  OriginalCursor = mHtmlPane.getCursor() ;
		Cursor  WaitCursor = Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) ;
		mHtmlPane.setCursor(WaitCursor) ;
		    
	    SwingUtilities.invokeLater( new PageLoader( mHtmlPane, "ABCJHelp.htm", OriginalCursor, ScrollPane ) ) ;
   	}
		catch( Exception e ) {
		    System.out.println( "HelpFrame Exception : " + e ) ;
		}
	}


}
