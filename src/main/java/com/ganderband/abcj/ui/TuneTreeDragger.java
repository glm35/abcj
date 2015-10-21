/**
 * A dragger for handling drag from a tune tree.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.dnd.* ;
import  java.awt.datatransfer.* ;
import  com.ganderband.abcj.util.* ;

public class TuneTreeDragger implements DragGestureListener, DragSourceListener
{
/**
 * The owning tune tree.
 */
	private TuneTree  mTree ;
/**
 * The drag source object for drag and drop.
 */
	private DragSource  mDragSource ;


/**
 * The standard constructors.
 */
public  TuneTreeDragger( TuneTree Tree )
{
	mTree = Tree ;
	
//	Enable dragging from this component
	
	mDragSource = DragSource.getDefaultDragSource() ;
	mDragSource.createDefaultDragGestureRecognizer( 
								Tree, DnDConstants.ACTION_COPY_OR_MOVE, this ) ;
}


/**
 * A drag and drop has been started.
 */
public void  dragGestureRecognized( DragGestureEvent e )
{
	
//  Construct a transferable object from the currently selected items

	Transferable  t = new TransferableObject( mTree.getSelectedNodeUserObjects() ) ;

//  Now start the drag

	mDragSource.startDrag( e, DragSource.DefaultCopyNoDrop, t, this ) ;
}


/**
 * The user is dragging and has entered a drop target.
 */
public void  dragEnter( DragSourceDragEvent e )
{
	
//  If this method fires then the target will accept the drag so we should
//  change the cursor to indicate this

	e.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop) ;   
}

/**
 * The user is dragging and is moving over a drop target.
 */
public void  dragOver( DragSourceDragEvent e )
{
//  Nothing required
}


/**
 * The user is dragging and has left a drop target.
 */
public void  dragExit( DragSourceEvent e )
{
//	If this method fires then we have no drop target so we should
//	change the cursor to indicate this

	e.getDragSourceContext().setCursor(DragSource.DefaultCopyNoDrop) ;    
}


/**
 * The user has changed the drag action between copy and move.
 */
public void  dropActionChanged( DragSourceDragEvent e )
{
//  Nothing required
}


/**
 * The user has finished or cancelled the drag operation.
 */
public void  dragDropEnd( DragSourceDropEvent e )
{
//  Nothing required
}

}