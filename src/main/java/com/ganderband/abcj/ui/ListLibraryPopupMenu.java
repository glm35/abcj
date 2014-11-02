/**
 * The library popup menu for the lists pane.
 */
package  abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  abcj.* ;

public class ListLibraryPopupMenu extends JPopupMenu implements ActionListener
{
/**
 * The main ABCJ instance.
 */
	private ABCJ  mABCJ ;
/**
 * The base popup menu Create List option.
 */
	private JMenuItem  mCreateList ;
	
 
/**
 * The default constructor.
 */
public  ListLibraryPopupMenu( ListEditListPanePopupHandler PopupHandler )
{
	mABCJ = PopupHandler.mABCJ ;

//  Build the popup menu

	mCreateList = new JMenuItem("Create New List") ;
	mCreateList.addActionListener(this) ;
	add(mCreateList) ;
}


/**
 * Check for action events from popup menus.
 */
public void  actionPerformed( ActionEvent e )
{
	
//	Check for tune list create

	if ( e.getSource() == mCreateList )   mABCJ.createTuneList() ;
}
 
}
