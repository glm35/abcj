/**
 * The popup menu handler for the index pane.
 */
package  abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  javax.swing.tree.* ;
import  abcj.* ;
import  abcj.model.* ;

public class IndexPanePopupHandler implements MouseListener
{
/**
 * The main ABCJ instance.
 */
	protected ABCJ  mABCJ ;
/**
 * The tree from the index pane.
 */
	private TuneTree  mTree ;
/**
 * The library popup menu.
 */
	private LibraryPopupMenu  mLibraryPopupMenu ;
/**
 * The tune book popup menu.
 */
	private BookPopupMenu  mBookPopupMenu ;
/**
 * The tune popup menu.
 */
	private TunePopupMenu  mTunePopupMenu ;
/**
 * The component on which a popup menu was triggered.
 */
	private TreePath  mPopupTreePath ;

 
/**
 * Get the tree being managed.
 */
public TuneTree  getTree()
{   return  mTree ;   }
 
 
/**
 * Get the path which caused the popup.
 */
public TreePath  getPopupTreePath()
{   return  mPopupTreePath ;   }


/**
 * The default constructor.
 */
public  IndexPanePopupHandler( ABCJ App, TuneTree Tree )
{
	mABCJ = App ;
	mTree = Tree ;
	
	mLibraryPopupMenu = new LibraryPopupMenu(this) ;
	mBookPopupMenu    = new BookPopupMenu(this) ;
	mTunePopupMenu    = new TunePopupMenu(this) ;

	mTree.addMouseListener(this) ;
}


/**
 * Required for the mouse listener interface.
 * 
 * <p>Null implementation as no function requred.
 */
public void  mouseClicked( MouseEvent e )
{
//  Nothing required
}


/**
 * Required for the mouse listener interface.
 * 
 * <p>Null implementation as no function requred.
 */
public void  mouseEntered( MouseEvent e )
{
//  Nothing required
}


/**
 * Required for the mouse listener interface.
 * 
 * <p>Null implementation as no function requred.
 */
public void  mouseExited( MouseEvent e )
{
//  Nothing required
}


/**
 * Check for popup required.
 */
public void  mousePressed( MouseEvent e )
{	checkForPopup(e) ;   }


/**
 * Check for popup required.
 */
public void  mouseReleased( MouseEvent e )
{	checkForPopup(e) ;   }


/**
 * Check for popup required.
 */
public void  checkForPopup( MouseEvent e )
{
	
//  If so, then store the path for later and bring up the relevant popup

	if ( e.isPopupTrigger() ) {
		mPopupTreePath = mTree.getPathForLocation(e.getX(), e.getY() ) ;
		
		if ( mPopupTreePath == null )   return ;
		
		DefaultMutableTreeNode  LastNode = (DefaultMutableTreeNode)
									 mPopupTreePath.getLastPathComponent() ;

	//  Popup the required menu
	
		JPopupMenu  Menu = null ;
									 
		if ( LastNode.getUserObject() instanceof String )
			Menu = mLibraryPopupMenu ;
		if ( LastNode.getUserObject() instanceof TuneBook )
			Menu = mBookPopupMenu ;
		if ( LastNode.getUserObject() instanceof Tune )
			Menu = mTunePopupMenu ;
	
		Menu.show( e.getComponent(), e.getX(), e.getY() ) ;
	}
}
 
}
