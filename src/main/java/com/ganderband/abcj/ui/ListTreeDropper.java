/**
 * A dropper instance to handle drop onto a list tree.
 */
package  abcj.ui ;

import  java.util.* ;
import  java.awt.* ;
import  java.awt.dnd.* ;
import  java.awt.datatransfer.* ;
import  javax.swing.tree.* ;
import  abcj.util.* ;
import  abcj.model.* ;

public class ListTreeDropper implements DropTargetListener
{
/**
 * The owning list tree.
 */
	private ListTree  mTree ;
/**
 * The drop target path.
 */
	private TreePath  mDropTargetPath = null ;


/**
 * The standard constructors.
 */
public  ListTreeDropper( ListTree Tree )
{
	mTree = Tree ;
	
//  Enable this as a target for drag and drop dropping
//  Note that we need the map to enable the user object to be accepted

	new DropTarget( Tree, this ) ;
}


/**
 * The user is dragging and has entered this drop target.
 */
public void  dragEnter( DropTargetDragEvent e )
{
	if ( locateDragTarget( e.getLocation() ) )
		e.acceptDrag(DnDConstants.ACTION_COPY) ;
	else
		e.rejectDrag() ;
}


/**
 * The user is dragging and is moving over this drop target.
 */
public void  dragOver( DropTargetDragEvent e )
{
	if ( locateDragTarget( e.getLocation() ) )
		e.acceptDrag(DnDConstants.ACTION_COPY) ;
	else
		e.rejectDrag() ;
}


/**
 * The user is dragging and has left this drop target.
 */
public void  dragExit( DropTargetEvent e )
{   setDropTargetPath(null) ;   }

/**
 * The user has changed the drag action between copy and move.
 */
public void  dropActionChanged( DropTargetDragEvent e )
{
//  Nothing required
}


/**
 * The user has finished or cancelled the drag and drop operation.
 * 
 * <p>This will perform the requested drag and drop.
 */
public void  drop( DropTargetDropEvent e )
{
	if ( ! locateDragTarget( e.getLocation() ) )   e.rejectDrop() ;
	
	try {   
		Transferable  t = e.getTransferable() ;
   
   	//  If valid data then accept the drop and add all new tunes
    
		if ( t.isDataFlavorSupported(TransferableObject.LOCAL_OBJECT) ) {
		
			e.acceptDrop(DnDConstants.ACTION_COPY) ;

			processDrop( (ArrayList) t.getTransferData(
											TransferableObject.LOCAL_OBJECT) ) ;
		
			e.getDropTargetContext().dropComplete(true) ;
			setDropTargetPath(null) ;
			
			return ;
		}
		
	//  Reject drop in all other cases
	
	}
	catch ( Exception x )   {

System.out.println("***EX*** ");
x.printStackTrace(System.out) ;	
	}

System.out.println("Reject drop");	
	e.rejectDrop() ;
	setDropTargetPath(null) ;
}


/**
 * Locate drag target.
 * 
 * <p>This method locates the current component under the cursor and then
 * determines if there is a suitable drag target and highlights it.
 */
public boolean  locateDragTarget( Point p )
{

//  Get the current point under the cursor and locate it's path
//  If not available then return without accepting the drag
//	If no level 1 node covered then, again reject as we are on the header.

	TreePath  Path = mTree.getPathForLocation( p.x, p.y ) ;

	if ( Path == null  ||  Path.getPathCount() < 2 )   return false ;

//  Highlight this node as a drag target (green line round the node
	
	setDropTargetPath(Path) ;	
	
	return  true ;
}


/**
 * Set the drop target path.
 */
public void  setDropTargetPath( TreePath Path )
{
	if ( Path == null ) {
		if ( mDropTargetPath == null )
			return ;
	}
	else if ( Path.equals(mDropTargetPath) )
		return ;

//  Update the renderer
	
	mTree.mRenderer.setDropTargetPath(Path) ;
	
//  Ensure old and new nodes are both refreshed

	TreeNode  OldNode = null ;
	if ( mDropTargetPath != null )
		OldNode = (TreeNode) mDropTargetPath.getLastPathComponent() ;
		
	TreeNode  NewNode = null ;
	if ( Path != null )
		NewNode = (TreeNode) Path.getLastPathComponent() ;

	if ( OldNode != null )   mTree.mTreeModel.nodeChanged(OldNode) ;
	if ( NewNode != null )   mTree.mTreeModel.nodeChanged(NewNode) ;

//  Save the new value and return (only save non-null)
	
	mDropTargetPath = Path ;
}


/**
 * Process a valid drop.
 */
public void  processDrop( ArrayList Tunes )
{
	
//  Extract the tune list from the path

	DefaultMutableTreeNode  Node = (DefaultMutableTreeNode)
											 mDropTargetPath.getPathComponent(1) ;
	TuneList  List = (TuneList) Node.getUserObject() ; 												 

//  Extract the tune after which the new stuff will be added.
//  This may be null (add at the end if expanded, at start if not)

	Tune  AfterTune = null ;
	if ( mDropTargetPath.getPathCount() > 2 ) {
		Node = (DefaultMutableTreeNode) mDropTargetPath.getPathComponent(2) ;
		AfterTune = (Tune) Node.getUserObject() ; 												 
	}
	
//  Determine the add at end flag.
//  If the the tune list node is not expanded then add at then end. 

	boolean AddAtEnd = false ;
	if ( AfterTune == null  &&  ! mTree.isExpanded(mDropTargetPath) )
		AddAtEnd = true ;
	
//  Now pass to the main ABC class to do the real work

	mTree.mABCJ.addTunesToList( Tunes, List, AfterTune, AddAtEnd ) ;
}

}