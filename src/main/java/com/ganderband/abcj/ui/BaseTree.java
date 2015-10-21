/**
 * A class for collecting some basic tree management stuff.
 */
package  com.ganderband.abcj.ui ;

import  java.util.* ;
import  javax.swing.* ;
import  javax.swing.tree.* ;
import  javax.swing.event.* ;
import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.model.* ;

public abstract class BaseTree extends JTree implements TreeSelectionListener
{
/**
 * The owing ABCJ Application.
 */
	protected ABCJ  mABCJ ;
/**
 * The explicit tree model to use.
 * 
 * <p>This will enable us to update the tree dynamically.
 */
	protected DefaultTreeModel  mTreeModel ;
/**
 * The explicit tree selection model to use.
 * 
 * <p>This will enable us to update the tree dynamically.
 */
	protected DefaultTreeSelectionModel  mSelectionModel ;
/**
 * The root node of the tree.
 */
	protected DefaultMutableTreeNode  mRootNode ;
/**
 * The cell render for drawing each cell.
 */
	protected IndexTreeCellRenderer  mRenderer ;
/**
 * A flag to indicate if duplicate level 2 nodes are allowed.
 * 
 * <p>This is used by tune lists where the same tune may be in multiple lists.
 */
	private boolean  mDuplicatesAllowed = false ;
/**
 * A static array of all tune trees.
 * 
 * <p>This is used to make sure all the trees are synchronized.
 */ 
	private static ArrayList  sAllTrees = new ArrayList() ;
/**
 * Indicate synchronization in progress.
 * 
 * <p>This flag is used to control the reselection of items whilst
 * synchronization is in progress.   It has particular effect where a tune
 * is selected from the lists panel and the tune is in more than one list.
 * If syncing is not controlled then the first occurrence of the tune will
 * be selected rather than the one being clicked on.
 */	
	private static boolean  sSyncFlag = false ;
	
	
/**
 * The standard constructors.
 */
public  BaseTree( ABCJ App, String Title, boolean MultipleSelection,
				  boolean DuplicatesAllowed )
{
	mABCJ = App ;
	mDuplicatesAllowed = DuplicatesAllowed ;

//  Create the root node, tree model and base the tree on it

	mRootNode = new DefaultMutableTreeNode(Title) ;
	mTreeModel = new DefaultTreeModel(mRootNode) ;
	setModel(mTreeModel) ;

	mRenderer = new IndexTreeCellRenderer() ;
	setCellRenderer(mRenderer) ;
	getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION) ;

//  Setup specially for multiple selection

	if ( MultipleSelection ) {								  
		mSelectionModel = new IndexTreeSelectionModel() ;
		mSelectionModel.setSelectionMode(
								TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION) ;
		setSelectionModel(mSelectionModel) ;
	}
								  
// Add listeners and return
								  
	addTreeSelectionListener(this) ;

	sAllTrees.add(this) ;
}


/**
 * Reset any defaults in case the Look and Feel has changed.
 */

public void  resetDefaults()
{
	
//  Recreate a new renderer as it seems the only way to get a new Look and Feel
//  to take

	mRenderer = new IndexTreeCellRenderer() ;
	setCellRenderer(mRenderer) ;
}


/**
 * Clear all bar the root node.
 */
public void  clear()
{
	mRootNode.removeAllChildren() ;
	mTreeModel.reload() ;
}


/**
 * Locate a level 1 node in the tree.
 * 
 * <p>This will either be tune book or a tune list depending on the sub-class.
 */
public  DefaultMutableTreeNode  locateLevel1Node( Object Obj )
{

//	Search the tree for the relevant list node

	for ( Enumeration  e = mRootNode.children() ; e.hasMoreElements() ; ) {
		DefaultMutableTreeNode  Node = (DefaultMutableTreeNode) e.nextElement() ;
		if ( Node.getUserObject() == Obj )
			return  Node ;
	}
	
	return  null ;
}


/**
 * Remove a level 1 node from the tree.
 * 
 * <p>This will either be tune book or a tune list depending on the sub-class.
 */
public void  removeLevel1Node( Object Obj )
{
	DefaultMutableTreeNode  Node = locateLevel1Node(Obj) ;
	if ( Node != null )   mTreeModel.removeNodeFromParent(Node) ;
}


/**
 * Change a level 1 node in the tree.
 * 
 * <p>This is used to indicate the the node has been changed.
 * 
 * <p>This will either be tune book or a tune list depending on the sub-class.
 */
public void  changeLevel1Node( Object Obj )
{	
	DefaultMutableTreeNode  Node = locateLevel1Node(Obj) ;
	if ( Node != null )   mTreeModel.nodeChanged(Node) ;
}



/**
 * Change a level 2 node in the tree.
 * 
 * <p>This is used to indicate that a node has been changed.
 * Note that the object may be present in multiple nodes (e.g. Tune lists)
 */
public void  changeLevel2Nodes( Object Obj )
{
	ArrayList  Nodes = locateLevel2Nodes(Obj) ;
	for ( int i = 0 ; i < Nodes.size() ; i++ )
		mTreeModel.nodeChanged( (DefaultMutableTreeNode) Nodes.get(i) ) ;
}


/**
 * Add a level 1 node to the end of the tree (without children).
 * 
 * <p>This will either be tune book or a tune list depending on the sub-class.
 */
public DefaultMutableTreeNode  addLevel1Node( Object Obj )
{
	DefaultMutableTreeNode  Node = new DefaultMutableTreeNode(Obj) ;
	mTreeModel.insertNodeInto( Node, mRootNode, mRootNode.getChildCount() ) ;
	return  Node ;
}


/**
 * Locate a level 2 node in the tree.
 * 
 * <p>This will always be a tune but may have a parent node of either
 * a tune book or a tune list.
 * 
 * <p>Note that this returns an array as there may be duplicate nodes in the tree.
 */
public  ArrayList  locateLevel2Nodes( Object Obj )
{
	ArrayList  Nodes = new ArrayList() ;

//	Try all level 1 nodes

	for ( Enumeration  e = mRootNode.children() ; e.hasMoreElements() ; ) {
		DefaultMutableTreeNode  L1Node = (DefaultMutableTreeNode) e.nextElement() ;

	//  Now try level 2 nodes in the level 1 node		
		
		for ( Enumeration  f = L1Node.children() ; f.hasMoreElements() ; ) {
			DefaultMutableTreeNode  L2Node = (DefaultMutableTreeNode) f.nextElement() ;
			if ( L2Node.getUserObject() == Obj ) {
				Nodes.add(L2Node) ;
				
			//	If no duplicates allowed then return now else continue
			
				if ( ! mDuplicatesAllowed )   return  Nodes ;
			}
		}
	}
	
	return  Nodes ;
}


/**
 * Remove a level 2 node from the tree.
 * 
 * <p>This will always be a tune.
 * Note that the object may be present in multiple nodes (e.g. Tune lists)
 */
public void  removeLevel2Nodes( Object Obj )
{	removeLevel2Nodes( locateLevel2Nodes(Obj) ) ;   }

public void  removeLevel2Nodes( ArrayList Nodes )
{
	for ( int i = 0 ; i < Nodes.size() ; i++ )
		mTreeModel.removeNodeFromParent( (DefaultMutableTreeNode) Nodes.get(i) ) ;
}


/**
 * Add a level 2 node to the given parent.
 */
public DefaultMutableTreeNode  addLevel2Node( Object Parent, Object Obj )
{
	DefaultMutableTreeNode  ParentNode = locateLevel1Node(Parent) ;

	DefaultMutableTreeNode  NewNode = new DefaultMutableTreeNode(Obj) ;
	mTreeModel.insertNodeInto( NewNode, ParentNode, ParentNode.getChildCount() ) ;
	
	return  NewNode ;
}


/**
 * Select a node and make it visible.
 */
public void  selectNode( DefaultMutableTreeNode Node )
{
	TreePath  Path = null ;
	if ( Node != null )   Path = new TreePath( Node.getPath() ) ;
	setSelectionPath(Path) ;
	scrollPathToVisible(Path) ;
}


/**
 * A node in the tree has been selected.
 */

public void  valueChanged( TreeSelectionEvent e )
{
	if ( sSyncFlag )   return ;		// Prevent nesting

//	Determine the selected node and return if none

	DefaultMutableTreeNode  Node = (DefaultMutableTreeNode)
										getLastSelectedPathComponent() ;
	if ( Node == null )   return ;

//	Now get the use object (which will be a String, TuneBook or Tune)

	Object  InfoObj = Node.getUserObject() ;
	
//	If this is not a tune then indicate that no tune has been selected
//	otherwise select the tune itself.

	if ( InfoObj instanceof Tune )
		mABCJ.selectTune( (Tune) InfoObj ) ;
	else if ( InfoObj instanceof TuneBook )
		mABCJ.selectTuneBook( (TuneBook) InfoObj ) ; 
	else if ( InfoObj instanceof TuneList ) {
		mABCJ.selectTuneList( (TuneList) InfoObj ) ;
	} 
	else {
		mABCJ.selectTuneList(null) ;
		mABCJ.selectTuneBook(null) ;
		mABCJ.selectTune(null) ;
	}
	
//	Finally ensure all trees are in sync

	synchronizeTrees() ;
}


/**
 * Synchronize all the trees.
 */
public void  synchronizeTrees()
{
	if ( sSyncFlag )   return ;		// Prevent nesting
	
	sSyncFlag = true ;

//	Determine the currently selected item (book or tune) in this tree

	DefaultMutableTreeNode  Node = (DefaultMutableTreeNode)
										getLastSelectedPathComponent() ;
	Object  SelObj = null ;
	if ( Node != null )   SelObj = Node.getUserObject() ;
										
//	Pass through all trees and ensure the same object is selected if possible

	for ( int i = 0 ; i < sAllTrees.size() ; i++ ) {
		
		BaseTree  Tree = (BaseTree) sAllTrees.get(i) ;
		
	//  Only update the other trees
		
		if ( Tree == this )   continue ;

	//  Locate the required node with the same user object
		
		DefaultMutableTreeNode  NewNode = null ;
		
		if ( SelObj instanceof String )
			NewNode = mRootNode ;
		else if ( SelObj instanceof TuneBook )
			NewNode = Tree.locateLevel1Node(SelObj) ;
		else if ( SelObj instanceof TuneList )
			NewNode = Tree.locateLevel1Node(SelObj) ;
		else if ( SelObj instanceof Tune ) {
			ArrayList  Nodes = Tree.locateLevel2Nodes(SelObj) ;
			if ( Nodes.size() > 0 )
				NewNode = (DefaultMutableTreeNode) Nodes.get(0) ;
		}
	
	//  Now make it visible and select it
	
		Tree.selectNode(NewNode) ;
	}
	
	sSyncFlag = false ;
}


/**
 * Get an array of the selected node user objects.
 * 
 * <p>This is used by drag and drop
 */
public ArrayList  getSelectedNodeUserObjects()
{
	ArrayList  List = new ArrayList() ;
	
	TreePath[]  Paths = getSelectionPaths() ;
	if ( Paths == null )   return  List ;

	for ( int i = 0 ; i < Paths.length ; i++ )
		List.add( ( (DefaultMutableTreeNode) Paths[i].getLastPathComponent() )
																.getUserObject() ) ;
	
	return  List ;
}

}