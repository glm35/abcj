/**
 * The tree selection model used to handle the index trees.
 * 
 * <p>This class provides a special tree selection model which is
 * useful for selecting mutiple entries for drag and drop.
 * 
 * <p>Cicking on an entry which is already selected will be ignored
 * instead of resrtting all the other entries.
 */
package  abcj.ui ;

import  javax.swing.tree.* ;

public class IndexTreeSelectionModel extends DefaultTreeSelectionModel
{
	
public void  setSelectionPath( TreePath path )
{
	if (  ! isPathSelected(path) )   super.setSelectionPath(path) ;
}

}
