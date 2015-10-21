/**
 * The tune popup menu for the list pane.
 */
package  com.ganderband.abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  javax.swing.tree.* ;
import  com.ganderband.abcj.* ;
import  com.ganderband.abcj.model.* ;

public class ListTunePopupMenu extends JPopupMenu implements ActionListener
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
 * The Delete option.
 */
	private JMenuItem  mDelete ;
/**
 * The Play option.
 */
	private JMenuItem  mPlay ;
/**
 * The Create Midi File option.
 */
	private JMenuItem  mMidiFile ;
/**
 * The tune popup menu Print option.
 */
	private JMenuItem  mPrint ;
	
 
/**
 * The default constructor.
 */
public  ListTunePopupMenu( ListEditListPanePopupHandler PopupHandler )
{
	mPopupHandler = PopupHandler ;
	mABCJ         = PopupHandler.mABCJ ;

//  Build the popup menu
	
	mPlay = new JMenuItem("Play Tune") ;
	mPlay.addActionListener(this) ;
	add(mPlay) ;
	
	mPrint = new JMenuItem("Print Tune ...") ;
	mPrint.addActionListener(this) ;
	add(mPrint) ;
	
	mMidiFile = new JMenuItem("Create Midi File ...") ;
	mMidiFile.addActionListener(this) ;
	add(mMidiFile) ;
	
	addSeparator() ;

	mDelete = new JMenuItem("Delete Tune from List") ;
	mDelete.addActionListener(this) ;
	add(mDelete) ;
}


/**
 * Check for action events from popup menus.
 */
public void  actionPerformed( ActionEvent e )
{
	
//  Ensure the item is selected

	TreePath  PopupTreePath = mPopupHandler.getPopupTreePath() ;

	mPopupHandler.getTree().setSelectionPath( mPopupHandler.getPopupTreePath() ) ;
	
//	Check for delete tune

	if ( e.getSource() == mDelete ) {
		
		DefaultMutableTreeNode  ListNode, TuneNode ;
		ListNode = (DefaultMutableTreeNode) PopupTreePath.getPathComponent(1) ;
		TuneNode = (DefaultMutableTreeNode) PopupTreePath.getPathComponent(2) ;
		
		mABCJ.deleteTuneFromList( (TuneList) ListNode.getUserObject(),
								  (Tune) TuneNode.getUserObject() ) ;
	}		
	
//	Check for play tune

	if ( e.getSource() == mPlay )   mABCJ.playSelectedTune() ;

//	Check for create Midi file from tune

	if ( e.getSource() == mMidiFile )   mABCJ.createMidiFileFromSelectedTune() ;

//	Check for print tune

	if ( e.getSource() == mPrint )   mABCJ.printSelectedTune() ;
}
 
}
