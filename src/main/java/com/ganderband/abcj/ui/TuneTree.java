/**
 * A class for managing trees of tunes.
 * 
 * This is used for the index pane, search pane and list index pane.
 * 
 * <p>Note that the list index pane allows multiple selection and also
 * drag and drop.
 */
package  com.ganderband.abcj.ui ;

import  java.util.* ;
import  javax.swing.tree.* ;
import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.model.* ;

public class TuneTree extends BaseTree
{


/**
 * The standard constructors.
 */
public  TuneTree( ABCJ App, String Title, boolean MultipleSelection )
{
	super( App, Title, MultipleSelection, false ) ;
	
//	Create a dragger for this component

	if ( MultipleSelection )   new TuneTreeDragger(this) ;
}


/**
 * Add a tune book node to the end of the tree (including children).
 */
public DefaultMutableTreeNode  addTuneBook( TuneBook Book )
{
	DefaultMutableTreeNode  BookNode = new DefaultMutableTreeNode(Book) ;
	
	mTreeModel.insertNodeInto( BookNode, mRootNode, mRootNode.getChildCount() ) ;
		
//  Now add an entry for each tune in the tunebook

	ArrayList  Tunes = Book.getTunes() ;
	
	for ( int j = 0 ; j < Tunes.size() ; j++ )
		mTreeModel.insertNodeInto( new DefaultMutableTreeNode( Tunes.get(j) ),
								   BookNode, BookNode.getChildCount() ) ;
								   
	return  BookNode ;
}

}