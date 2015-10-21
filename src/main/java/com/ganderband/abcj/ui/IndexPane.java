/**
 * The left hand index pane of the ABC GUI.
 */
package  com.ganderband.abcj.ui ;

import  com.ganderband.abcj.* ;

public class IndexPane extends BaseIndexPane
{
 
/**
 * The default constructor.
 */
public  IndexPane( ABCJ App )
{
	super(App) ;
	
//  Create the relevant tree (with multiple selection)

	buildTree(true) ;
	
//  Create a popup handler to handle popup menus

	new IndexPanePopupHandler( App, mTree ) ;
}
 
}
