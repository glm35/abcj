/**
 * The list edditing lower list pane of the ABC GUI.
 */
package  abcj.ui ;

import  java.util.* ;
import  javax.swing.* ;
import  javax.swing.tree.* ;
import  abcj.* ;
import  abcj.model.* ;

public class ListEditListPane extends JScrollPane
{
/**
 * The owning ABCJ application instance.
 */
	protected ABCJ  mABCJ ;
/**
 * The tune list tree contained in this pane.
 */
	protected ListTree  mTree ;
 
 
/**
 * The default constructor.
 */
public  ListEditListPane( ABCJ App )
{
	mABCJ = App ;

//	Create the relevant tree

	buildTree() ;
	
//	Create a popup handler to handle popup menus

	new ListEditListPanePopupHandler( App, mTree ) ;
}


/**
 * Build the initial contents of the tree.
 */
public void  buildTree()
{

//  Create the tree

	mTree = new ListTree( mABCJ, "All ABC Tune Lists", false ) ;
	
	setViewportView(mTree) ;
	
//  Add an entry for every tune list in the library

	ArrayList  Lists = mABCJ.getLibrary().getTuneLists() ;

	for ( int i = 0 ; i < Lists.size() ; i++ )
		mTree.addTuneList( (TuneList) Lists.get(i) ) ;	
}


/**
 * Reset any defaults in case the Look and Feel has changed.
 */

public void  resetDefaults()
{	mTree.resetDefaults() ;   }


/**
 * A new tune list has been added to the library.
 */
public void  addTuneList( TuneList List )
{
	DefaultMutableTreeNode  Node = mTree.addTuneList(List) ;
									 
//	Ensure it is visible and select it

	mTree.selectNode(Node) ;
}


/**
 * A tune list has been deleted from the library.
 */
public void  removeTuneList( TuneList List )
{	mTree.removeLevel1Node(List) ;   }


/**
 * A tune list title has been changed.
 */
public void  changeTuneListTitle( TuneList List )
{	mTree.changeLevel1Node(List) ;   }


/**
 * Refresh a node completely after tunes were added.
 */
public void  refreshTuneList( TuneList List )
{	mTree.refreshTuneList(List) ;   }


/**
 * A tune has been deleted from the library.
 */
public void  removeTune( Tune Tune )
{	mTree.removeLevel2Nodes(Tune) ;   }


/**
 * A tune book has been deleted from the library.
 */
public void  removeTuneBook( TuneBook Book )
{	mTree.removeTuneBook(Book) ;   }


/**
 * Tune ABC Text has been editted.
 */
public void  tuneEditted( Tune Tune )
{	mTree.changeLevel2Nodes(Tune) ;   }

}
