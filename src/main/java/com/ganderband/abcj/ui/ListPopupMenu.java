/**
 * The list popup menu for the list pane.
 */
package  abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  abcj.* ;

public class ListPopupMenu extends JPopupMenu implements ActionListener
{
/**
 * The popup handler for the index pane.
 */
	private ListEditListPanePopupHandler  mPopupHandler ;
/**
 * The main ABCJ instance.
 */
	private ABCJ  mABCJ ;
/**
 * The Remove option.
 */
	private JMenuItem  mRemove ;
/**
 * The Set Title option.
 */
	private JMenuItem  mSetTitle ;
/**
 * The Print option.
 */
	private JMenuItem  mPrint ;
	
 
/**
 * The default constructor.
 */
public  ListPopupMenu( ListEditListPanePopupHandler PopupHandler )
{
	mPopupHandler = PopupHandler ;
	mABCJ         = PopupHandler.mABCJ ;

//  Build the popup menu
	
	mPrint = new JMenuItem("Print List ...") ;
	mPrint.addActionListener(this) ;
	add(mPrint) ;
	
	mSetTitle = new JMenuItem("Set List Title ...") ;
	mSetTitle.addActionListener(this) ;
	add(mSetTitle) ;

	addSeparator() ;
	
	mRemove = new JMenuItem("Remove List") ;
	mRemove.addActionListener(this) ;
	add(mRemove) ;
}


/**
 * Check for action events from popup menus.
 */
public void  actionPerformed( ActionEvent e )
{
	
//  Ensure the item is selected

	mPopupHandler.getTree().setSelectionPath( mPopupHandler.getPopupTreePath() ) ;
	
//	Check for tune list delete

	if ( e.getSource() == mRemove )   mABCJ.removeSelectedTuneList() ;		
	
//	Check for tune list set title

	if ( e.getSource() == mSetTitle )   mABCJ.setSelectedTuneListTitle() ;		

//	Check for tune list print

	if ( e.getSource() == mPrint )   mABCJ.printSelectedList() ;

}
 
}
