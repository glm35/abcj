/**
 * The left hand search pane of the ABC GUI.
 */
package  abcj.ui ;

import  java.awt.* ;
import  java.awt.event.* ;
import  javax.swing.* ;
import  javax.swing.tree.* ;
import  abcj.* ;
import  abcj.model.* ;

public class SearchPane extends JPanel implements ActionListener, TuneSearchListener
{
/**
 * The owning ABCJ application instance.
 */
	private ABCJ  mABCJ ;
/**
 * The input field containing the search text.
 */
	private JTextField  mSearchText ;
/**
 * The start search button.
 */
	private JButton  mStartSearch ;
/**
 * The tree itself.
 */
	private TuneTree  mTree ;	
/**
 * The last book added to the tree.
 */
	private TuneBook  mLastBook ;	


/**
 * The default constructor.
 */
public  SearchPane( ABCJ App )
{
	mABCJ = App ;
	
//  Build and add all the components

	setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) ) ;
	
	JLabel  Lab = new JLabel("Enter Search Options :-") ;
	Lab.setAlignmentX(Component.CENTER_ALIGNMENT) ;
	add(Lab) ;

	add( Box.createRigidArea( new Dimension( 0, 2 ) ) ) ;
	
	mSearchText = new JTextField() ;
	mSearchText.setMaximumSize( new Dimension( Short.MAX_VALUE, 24 ) ) ;
	add(mSearchText) ;

	add( Box.createRigidArea( new Dimension( 0, 2 ) ) ) ;
	
	Lab = new JLabel("e.g. jig scot* *ation") ;
	Lab.setAlignmentX(Component.CENTER_ALIGNMENT) ;
	add(Lab) ;

	add( Box.createRigidArea( new Dimension( 0, 2 ) ) ) ;
	
	mStartSearch = new JButton("Start Search") ;
	mStartSearch.setAlignmentX(Component.CENTER_ALIGNMENT) ;
	mStartSearch.addActionListener(this) ;
	add(mStartSearch) ;

	add( Box.createRigidArea( new Dimension( 0, 2 ) ) ) ;

//  Now initialize the tree

	buildTree() ;
	
//	Create a popup handler to handle popup menus

	new IndexPanePopupHandler( App, mTree ) ;
}


/**
 * Reset any defaults in case the Look and Feel has changed.
 */

public void  resetDefaults()
{	mTree.resetDefaults() ;   }


/**
 * An action has been performed (such as pressing a button).
 */
public void  actionPerformed( ActionEvent e )
{
	if ( e.getSource() == mStartSearch ) {
		
	//  Tidy up the current tree before continuing the search

		mTree.clear() ;
	
		mLastBook = null ;
		
		mABCJ.searchLibrary( mSearchText.getText(), this ) ;
	}
}


/**
 * A tune has been found in a search.
 */
public void  tuneFound( Tune Tune )
{

//  Add the book node for the given tune if not yet done

	if ( Tune.getTuneBook() != mLastBook ) {
		mLastBook = Tune.getTuneBook() ;
		mTree.addLevel1Node(mLastBook) ;
	}

//  Now add the tune itself and ensure it is visible

	DefaultMutableTreeNode  Node = mTree.addLevel2Node( Tune.getTuneBook(), Tune ) ;
	mTree.makeVisible( new TreePath( Node.getPath() ) ) ;
}


/**
 * Build the initial contents of the tree.
 */
public void  buildTree()
{
	mTree = new TuneTree( mABCJ, "Selected tunes", true ) ;
							
	JScrollPane  TreePane = new JScrollPane() ;
	add(TreePane) ;
	
	TreePane.setViewportView(mTree) ;
}


/**
 * A tune book has been deleted from the library.
 */
public void  removeTuneBook( TuneBook Book )
{	mTree.removeLevel1Node(Book) ;   }


/**
 * A tune has been deleted from the library.
 */
public void  removeTune( Tune Tune )
{	mTree.removeLevel2Nodes(Tune) ;   }


/**
 * Tune ABC Text has been editted.
 */
public void  tuneEditted( Tune Tune )
{	mTree.changeLevel2Nodes(Tune) ;   }


}
