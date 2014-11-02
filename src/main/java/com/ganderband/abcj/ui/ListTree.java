/**
 * A class for managing the tune list tree.
 */
package  abcj.ui ;

import  java.util.* ;
import  javax.swing.tree.* ;
import  abcj.* ;
import  abcj.model.* ;

public class ListTree extends BaseTree
{

/**
 * The standard constructors.
 */
public  ListTree( ABCJ App, String Title, boolean MultipleSelection )
{
	super( App, Title, MultipleSelection, true ) ;
	
//  Make this a target for drag and drop

	new ListTreeDropper(this) ;
}


/**
 * Add a tune list node to the end of the tree (including children).
 */
public DefaultMutableTreeNode  addTuneList( TuneList List )
{
	DefaultMutableTreeNode  ListNode = new DefaultMutableTreeNode(List) ;
	
	mTreeModel.insertNodeInto( ListNode, mRootNode, mRootNode.getChildCount() ) ;
		
//  Now add an entry for each tune in the tune list

	ArrayList  Tunes = List.getTunes() ;
	
	for ( int j = 0 ; j < Tunes.size() ; j++ )
		mTreeModel.insertNodeInto( new DefaultMutableTreeNode( Tunes.get(j) ),
								   ListNode, ListNode.getChildCount() ) ;
								   
	return  ListNode ;
}


/**
 * Refresh an existing tune list as tunes have been added.
 */
public void  refreshTuneList( TuneList List )
{
	
//  Clear all children from the node and re-add

	DefaultMutableTreeNode  ListNode = locateLevel1Node(List) ;

	ListNode.removeAllChildren() ;
		
//	Now add an entry for each tune in the tune list

	ArrayList  Tunes = List.getTunes() ;
	
	for ( int j = 0 ; j < Tunes.size() ; j++ )
		mTreeModel.insertNodeInto( new DefaultMutableTreeNode( Tunes.get(j) ),
								   ListNode, ListNode.getChildCount() ) ;

//  Ensure it is expanded to show the results

	expandPath( new TreePath( ListNode.getPath() ) ) ;	

//	Force a complete rebuild of the GUI tree representation

	mTreeModel.nodeStructureChanged(ListNode) ;
}


/**
 * Remove tune book.
 */
public void  removeTuneBook( TuneBook Book )
{

//  Look through all tunes in all tune lists and build an array of the nodes
//  which belong to the book being deleted.

	ArrayList  Nodes = new ArrayList() ;

//	Try all level 1 nodes

	for ( Enumeration  e = mRootNode.children() ; e.hasMoreElements() ; ) {
		DefaultMutableTreeNode  L1Node = (DefaultMutableTreeNode) e.nextElement() ;

	//  Now try level 2 nodes in the level 1 node		
		
		for ( Enumeration  f = L1Node.children() ; f.hasMoreElements() ; ) {
			DefaultMutableTreeNode  L2Node = (DefaultMutableTreeNode) f.nextElement() ;

		//  Check the tune book for this tune and remember if it matches
		
			if ( ( (Tune) L2Node.getUserObject() ).getTuneBook() == Book )			
				Nodes.add(L2Node) ;
		}
	}
	
//  Now we have a list of all nodes to delete we can delete them

	removeLevel2Nodes(Nodes) ;
}

}