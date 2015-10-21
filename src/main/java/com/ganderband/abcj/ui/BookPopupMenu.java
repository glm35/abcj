/**
 * The book popup menu for the index pane.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  com.ganderband.abcj.* ;

public class BookPopupMenu extends JPopupMenu implements ActionListener
{
/**
 * The popup handler for the index pane.
 */
	private IndexPanePopupHandler  mPopupHandler ;
/**
 * The main ABCJ instance.
 */
	private ABCJ  mABCJ ;
/**
 * The New Tune option.
 */
	private JMenuItem  mNewTune ;
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
public  BookPopupMenu( IndexPanePopupHandler PopupHandler )
{
	mPopupHandler = PopupHandler ;
	mABCJ         = PopupHandler.mABCJ ;

//  Build the popup menu

	mNewTune = new JMenuItem("New Tune") ;
	mNewTune.addActionListener(this) ;
	add(mNewTune) ;
	
	mPrint = new JMenuItem("Print Book ...") ;
	mPrint.addActionListener(this) ;
	add(mPrint) ;
	
	mSetTitle = new JMenuItem("Set Title ...") ;
	mSetTitle.addActionListener(this) ;
	add(mSetTitle) ;
	
	addSeparator() ;
	
	mRemove = new JMenuItem("Remove Book") ;
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
	
//  Check for add new tune

	if ( e.getSource() == mNewTune )   mABCJ.addNewTune() ;
	
//	Check for tune book delete

	if ( e.getSource() == mRemove )   mABCJ.removeSelectedTuneBook() ;		
	
//	Check for tune book set title

	if ( e.getSource() == mSetTitle )   mABCJ.setSelectedTuneBookTitle() ;		

//	Check for print book

	if ( e.getSource() == mPrint )   mABCJ.printSelectedBook() ;
}
 
}
