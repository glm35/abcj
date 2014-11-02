/**
 * The tune popup menu for the index pane.
 */
package  abcj.ui ;

import  java.awt.event.* ;
import  javax.swing.* ;
import  abcj.* ;

public class TunePopupMenu extends JPopupMenu implements ActionListener
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
 * The Delete option.
 */
	private JMenuItem  mDelete ;
/**
 * The Play option.
 */
	private JMenuItem  mPlay ;
/**
 * The Transpose option.
 */
	private JMenuItem  mTranspose ;
/**
 * The Create MIDI File option.
 */
	private JMenuItem  mMidiFile ;
/**
 * The Print option.
 */
	private JMenuItem  mPrint ;
	
 
/**
 * The default constructor.
 */
public  TunePopupMenu( IndexPanePopupHandler PopupHandler )
{
	mPopupHandler = PopupHandler ;
	mABCJ         = PopupHandler.mABCJ ;

//  Build the popup menu

	mPlay = new JMenuItem("Play Tune") ;
	mPlay.addActionListener(this) ;
	add(mPlay) ;
	
	mTranspose = new JMenuItem("Transpose Tune") ;
	mTranspose.addActionListener(this) ;
	add(mTranspose) ;
	
	mPrint = new JMenuItem("Print Tune ...") ;
	mPrint.addActionListener(this) ;
	add(mPrint) ;
	
	mMidiFile = new JMenuItem("Create Midi File ...") ;
	mMidiFile.addActionListener(this) ;
	add(mMidiFile) ;
	
	addSeparator() ;
	
	mDelete = new JMenuItem("Delete Tune") ;
	mDelete.addActionListener(this) ;
	add(mDelete) ;
}


/**
 * Check for action events from popup menus.
 */
public void  actionPerformed( ActionEvent e )
{
	
//  Ensure the item is selected

	mPopupHandler.getTree().setSelectionPath( mPopupHandler.getPopupTreePath() ) ;
	
//	Check for delete tune

	if ( e.getSource() == mDelete )   mABCJ.deleteSelectedTune() ;

//	Check for play tune

	if ( e.getSource() == mPlay )   mABCJ.playSelectedTune() ;

//	Check for transpose tune

	if ( e.getSource() == mTranspose )   mABCJ.transposeSelectedTune() ;

//	Check for create Midi file from tune

	if ( e.getSource() == mMidiFile )   mABCJ.createMidiFileFromSelectedTune() ;

//	Check for print tune

	if ( e.getSource() == mPrint )   mABCJ.printSelectedTune() ;
}
 
}
