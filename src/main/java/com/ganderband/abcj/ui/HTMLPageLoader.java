/**
 * A class to control the loading of HTML into an editor pane.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.* ;
import java.io.FileInputStream;

import  javax.swing.* ;
import  javax.swing.text.* ;

class PageLoader implements Runnable
{
/**
 * The editor pane being loaded.
 */
    private JEditorPane  mHtmlPane ;
/**
 * The editor pane scroller.
 */
    private JScrollPane  mScroller ;
/**
 * The file name being loaded.
 */
    private String  mFileName ;
/**
 * The original cursor shown.
 */
    private Cursor  mOriginalCursor ;
    

/**
 * Construct a pageloader instance for a particular page.
 */
public  PageLoader( JEditorPane Pane, String FileName, Cursor OriginalCursor, JScrollPane Scroller ) 
{	mHtmlPane = Pane ;   mFileName = FileName ;   mOriginalCursor = OriginalCursor ;   mScroller = Scroller ;   }


/**
 * The run method to manage the loading itself.
 */
public void  run() 
{

//  If no file name given then paint has completed.
//  Restore the cursor and position at the top
	
	if( mFileName == null ) { 
		mHtmlPane.setCursor(mOriginalCursor) ;
   		mHtmlPane.getParent().repaint() ;		// Force repaint
   		
   		if ( mScroller != null )
   			mScroller.getViewport().setViewPosition( new Point ( 0, 0 ) ) ;
	}
	
//  Get the requested page to load
	
	else { 
		Document  Doc = mHtmlPane.getDocument() ;
    		
		try {
		    mHtmlPane.read( new FileInputStream(mFileName), mHtmlPane.getEditorKit().createDefaultDocument() ) ;
		}
		catch( Exception e ) {
			mHtmlPane.setDocument(Doc) ;	// Put original doc back if error
		}
    		
	// Schedule the cursor to revert after the paint has happended.
    		
    	finally {
   	        mFileName = null ;
   		    SwingUtilities.invokeLater(this) ;
    	}
	}
}

}
