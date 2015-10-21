/**
 * The popup menu handler for the list edit list pane.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  javax.swing.tree.* ;
import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.model.* ;

public class ListEditListPanePopupHandler implements MouseListener
{
/**
 * The owning ABCJ application instance.
 */
	protected ABCJ  mABCJ ;
/**
 * The tune list tree from the list pane
 */
	private ListTree  mTree ;
/**
 * The base popup menu.
 */
	private ListLibraryPopupMenu  mBasePopupMenu ;
/**
 * The tune list popup menu.
 */
	private ListPopupMenu  mListPopupMenu ;
/**
 * The tune popup menu.
 */
	private ListTunePopupMenu  mTunePopupMenu ;
/**
 * The component on which a popup menu was triggered.
 */
	private TreePath  mPopupTreePath ;

 
/**
 * Get the tree being managed.
 */
public ListTree  getTree()
{   return  mTree ;   }
 
 
/**
 * Get the path which caused the popup.
 */
public TreePath  getPopupTreePath()
{   return  mPopupTreePath ;   }
 
 
/**
 * The default constructor.
 */
public  ListEditListPanePopupHandler( ABCJ App, ListTree Tree )
{
	mABCJ = App ;
	mTree = Tree ;

	mBasePopupMenu = new ListLibraryPopupMenu(this) ;
	mListPopupMenu = new ListPopupMenu(this) ;
	mTunePopupMenu = new ListTunePopupMenu(this) ;

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
	
//	If so, then store the path for later and bring up the relevant popup

	if ( e.isPopupTrigger() ) {
		mPopupTreePath = mTree.getPathForLocation( e.getX(), e.getY() ) ;

		if ( mPopupTreePath == null )   return ;
		
		DefaultMutableTreeNode  LastNode = (DefaultMutableTreeNode)
									 mPopupTreePath.getLastPathComponent() ;

	//  Popup the required menu
	
		JPopupMenu  Menu = null ;
									 
		if ( LastNode.getUserObject() instanceof String )
			Menu = mBasePopupMenu ;
		if ( LastNode.getUserObject() instanceof TuneList )
			Menu = mListPopupMenu ;
		if ( LastNode.getUserObject() instanceof Tune )
			Menu = mTunePopupMenu ;

		Menu.show( e.getComponent(), e.getX(), e.getY() ) ;
	}
}

}
