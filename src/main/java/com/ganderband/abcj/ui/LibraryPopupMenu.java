/**
 * The library popup menu for the index pane.
 */
package  abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  abcj.* ;

public class LibraryPopupMenu extends JPopupMenu implements ActionListener
{
/**
 * The main ABCJ instance.
 */
	private ABCJ  mABCJ ;
/**
 * The Add Book option.
 */
	private JMenuItem  mAddBook ;
/**
 * The Create Book option.
 */
	private JMenuItem  mCreateBook ;
	
 
/**
 * The default constructor.
 */
public  LibraryPopupMenu( IndexPanePopupHandler PopupHandler )
{
	mABCJ = PopupHandler.mABCJ ;

//  Build the popup menu

	mAddBook = new JMenuItem("Add Book (from ABC File) ...") ;
	mAddBook.addActionListener(this) ;
	add(mAddBook) ;
	
	mCreateBook = new JMenuItem("Create Book (new ABC File) ...") ;
	mCreateBook.addActionListener(this) ;
	add(mCreateBook) ;
}


/**
 * Check for action events from popup menus.
 */
public void  actionPerformed( ActionEvent e )
{
	
//  Check for tune book open

	if ( e.getSource() == mAddBook )   mABCJ.openABCFile() ;
	
//	Check for tune book create

	if ( e.getSource() == mCreateBook )   mABCJ.createABCFile() ;
}
 
}
