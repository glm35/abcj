/**
 * The base left hand index pane of the ABC GUI.
 * 
 * <p>This abstract class provides common function shared between all
 * book/tune index panes.
 */
package  abcj.ui ;

import  java.util.* ;
import  javax.swing.* ;
import  javax.swing.tree.* ;
import  abcj.* ;
import  abcj.model.* ;

public abstract class BaseIndexPane extends JScrollPane
{
/**
 * The owning ABCJ application instance.
 */
	protected ABCJ  mABCJ ;
/**
 * The tunebook tree contained in this pane.
 */
	protected TuneTree  mTree ;
 
 
/**
 * The default constructor.
 */
public  BaseIndexPane( ABCJ App )
{	mABCJ = App ;   }


/**
 * Build the initial contents of the tree.
 */
public void  buildTree( boolean MultipleSelection )
{

//  Create the tree

	mTree = new TuneTree( mABCJ, "All ABC Books", MultipleSelection ) ;
							
	setViewportView(mTree) ;
	
//  Add an entry for every tune book in the library

	ArrayList  Books = mABCJ.getLibrary().getTuneBooks() ;

	for ( int i = 0 ; i < Books.size() ; i++ )
		mTree.addTuneBook( (TuneBook) Books.get(i) ) ;	
}


/**
 * Reset any defaults in case the Look and Feel has changed.
 */

public void  resetDefaults()
{	mTree.resetDefaults() ;   }


/**
 * A new tune book has been added to the library.
 */
public void  addTuneBook( TuneBook Book )
{
	DefaultMutableTreeNode  Node = mTree.addTuneBook(Book) ;

	mTree.selectNode(Node) ;
}


/**
 * A tune book has been deleted from the library.
 */
public void  removeTuneBook( TuneBook Book )
{	mTree.removeLevel1Node(Book) ;   }


/**
 * A tune book title has been changed.
 */
public void  changeTuneBookTitle( TuneBook Book )
{	mTree.changeLevel1Node(Book) ;   }


/**
 * A tune has been added to the current tunebook.
 */
public void  addNewTune( Tune Tune )
{
	DefaultMutableTreeNode  Node = mTree.addLevel2Node( Tune.getTuneBook(), Tune ) ;
		
	mTree.selectNode(Node) ;
}


/**
 * A tune has been deleted from the library.
 */
public void  removeTune( Tune Tune )
{	mTree.removeLevel2Nodes(Tune) ;   }


/**
 * Tune ABC Text has been editted.
 */
public void  tuneEditted( Tune Tune )
{	mTree.changeLevel2Nodes(Tune) ;   }
 
}
