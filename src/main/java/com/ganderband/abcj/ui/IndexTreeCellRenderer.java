/**
 * This class is used to render cells in the index tree.
 */
package  com.ganderband.abcj.ui ;

import  javax.swing.* ;
import  javax.swing.tree.* ;
import  java.awt.* ;
import  com.ganderband.abcj.model.* ;
import  com.ganderband.abcj.util.* ;

public class IndexTreeCellRenderer extends DefaultTreeCellRenderer
{
/**
 * The background non-selection color to use for an OK node.
 */
	private Color  mOKBackgroundNonSelectionColor ;
/**
 * The background selection color to use for an OK node.
 */
	private Color  mOKBackgroundSelectionColor ;
/**
 * The text non-selection color to use for an OK node.
 */
	private Color  mOKTextNonSelectionColor ;
/**
 * The text selection color to use for an OK node.
 */
	private Color  mOKTextSelectionColor ;
/**
 * The background non-selection color to use for an error node.
 */
	private Color  mErrorBackgroundNonSelectionColor = Color.red ;
/**
 * The background selection color to use for an error node.
 */
	private Color  mErrorBackgroundSelectionColor = Color.red ;
/**
 * The text non-selection color to use for an error node.
 */
	private Color  mErrorTextNonSelectionColor = Color.black ;
/**
 * The text selection color to use for an error node.
 */
	private Color  mErrorTextSelectionColor = Color.white ;
/**
 * The background non-selection color to use for an error node.
 */
//	private Color  mTargetBackgroundNonSelectionColor = Color.green ;	// Not used !
/**
 * The background color to use for a drop target.
 */
	private Color  mDropTargetBackgroundSelectionColor = Color.green ;
/**
 * The icon for displaying tunes.
 */
	private ImageIcon  mTuneIcon ;
/**
 * The icon for displaying books.
 */
	private ImageIcon  mBookIcon ;
/**
 * The icon for displaying libraries.
 */
	private ImageIcon  mLibIcon ;
/**
 * The drop target path.
 * 
 * <p>One node in the tree may be a drop trget.
 */	
	private TreePath  mDropTargetPath = null ;
/**
 * A flag to indicate that the node should be underlined.
 */
	private boolean mUnderline = false ;
	

/**
 * The default constructor.
 */
public  IndexTreeCellRenderer()
{
	mTuneIcon = new ImageIcon( Utils.getImage( this, "images/note.gif" ) ) ;
	mBookIcon = new ImageIcon( Utils.getImage( this, "images/book.gif" ) ) ;
	mLibIcon  = new ImageIcon( Utils.getImage( this, "images/library.gif" ) ) ;
}


/**
 * Set the drop target path.
 */
public void  setDropTargetPath( TreePath Path )
{	mDropTargetPath = Path ;   }


/**
 * Render a single cell in the tree.
 */
public Component  getTreeCellRendererComponent( JTree Tree, Object Value,
							boolean Selected, boolean Expanded, boolean Leaf,
							int Row, boolean HasFocus )
{
	
//  Store the original color scheme

	if ( mOKBackgroundNonSelectionColor == null )
		storeColorScheme() ;

//  Construct the displayable string based on the node type.
//  We also extract a flag to indicate of the node is in error

	DefaultMutableTreeNode  Node = (DefaultMutableTreeNode) Value ;
	Object  InfoObj = Node.getUserObject() ;

	String     Title     = "Unknown" ;
	boolean    ErrorFlag = false ;
	ImageIcon  Icon      = null ;

//  Set title for basic string node (library)
	
	if ( InfoObj instanceof String ) {
		Title = (String) InfoObj ;
		Icon = mLibIcon ;
	}

//  Set title for a tunebook entry

	if ( InfoObj instanceof TuneBook ) {
		TuneBook  Book = (TuneBook) InfoObj ;
	
		Title = Book.getTitle() ;
		if ( Title == null )   Title = Book.getFileName() ;
		else				   Title += " (" + Book.getFileName() + ")" ;
		
		ErrorFlag = Book.isFileNotFound() ;
		
		Icon = mBookIcon ;
	}

//	Set title for a tunelist entry

	if ( InfoObj instanceof TuneList ) {
		TuneList  List = (TuneList) InfoObj ;
	
		Title = List.getTitle() ;
		
		Icon = mBookIcon ;
	}

//	Set title for a tune entry

	if ( InfoObj instanceof Tune ) {
		Tune  Tune = (Tune) InfoObj ;
	
		Title = Tune.getTitle() ;
		if ( Title == null )   Title = "" ;
		else			  	   Title += "   " ;
		
		if ( Tree instanceof ListTree ) {
			Title += "(" + Tune.getTuneBook().getFileName() ;
			if ( Tune.getIndex() != null )
				Title += " " + Tune.getIndex() ;
			Title += ")" ;
		}
		else {
			if ( Tune.getIndex() != null )
				Title += "(" + Tune.getIndex() + ")" ;
		}
		
		ErrorFlag = Tune.isInError() ;
		
		Icon = mTuneIcon ;
	}

//  Set the necessary colors for error or non-error
	
	if ( ErrorFlag ) {								
		setBackgroundNonSelectionColor(mErrorBackgroundNonSelectionColor) ;
		setBackgroundSelectionColor(mErrorBackgroundSelectionColor) ;
		setTextNonSelectionColor(mErrorTextNonSelectionColor) ;
		setTextSelectionColor(mErrorTextSelectionColor) ;
	}
	else {
		setBackgroundNonSelectionColor(mOKBackgroundNonSelectionColor) ;
		setBackgroundSelectionColor(mOKBackgroundSelectionColor) ;
		setTextNonSelectionColor(mOKTextNonSelectionColor) ;
		setTextSelectionColor(mOKTextSelectionColor) ;
	}
	
//  If drop target node then set colors and underline

	checkDropTarget( Tree, Node ) ;

//  Display the node and set it's icon

	super.getTreeCellRendererComponent( Tree, Title, Selected, Expanded,
										Leaf, Row, HasFocus ) ;					

	if ( Icon != null )   setIcon(Icon) ;
		
	return  this ;						
}
	
	
/**
 * Check for drop target nodes.
 * 
 * <p>This method will perform the relevant chnages for certain nodes in the
 * drop target path as follows :-
 * 
 * 1.   If a level 2 node (tune) is in the drop target path then it should be
 * 		underlined to indicate the drop position.
 * 2.   If a level 1 node (tune list) is in the path then it should be highlighted
 * 		to indicate this tune list will be used.
 * 3.   In addition, if the level 1 node is expanded then it should also be
 * 		underlined to indicate that the tunes will be dropped at the beginning.
 * 		If not expanded then they will be added at the end.
 */
public void  checkDropTarget( JTree Tree, DefaultMutableTreeNode Node )
{
	mUnderline = false ;
	
	if ( mDropTargetPath == null  ||  mDropTargetPath.getPathCount() < 2 )
		return ;
	
//  If level 1 node (tune list) then highlight and possibly underline it

	if ( mDropTargetPath.getPathComponent(1) == Node ) {
		
		setBackgroundNonSelectionColor(mDropTargetBackgroundSelectionColor) ;
		setBackgroundSelectionColor(mDropTargetBackgroundSelectionColor) ;
		
		if ( Tree.isExpanded(mDropTargetPath) )   mUnderline = true ;
	}

	
//	If level 2 node (tune list) then underline it
 
	if ( mDropTargetPath.getPathCount() > 2
	 &&  mDropTargetPath.getPathComponent(2) == Node )
		mUnderline = true ;
}
	
	
/**
 * Store the original color scheme.
 */

public void  storeColorScheme()
{
	mOKBackgroundNonSelectionColor = getBackgroundNonSelectionColor() ;
	mOKBackgroundSelectionColor    = getBackgroundSelectionColor() ;
	mOKTextNonSelectionColor       = getTextNonSelectionColor() ;
	mOKTextSelectionColor          = getTextSelectionColor() ;
}

/**
 * The component paint method.
 * 
 * <p>This is overridden so an underline can be painted whilst dragging.
 */
public void paint( Graphics g )
{
	super.paint(g) ;
	
//  Draw underline if requested to indicate drop position

	if ( mUnderline ) {
		Dimension d = getSize() ;
		Color  SavedColor = g.getColor() ;
		g.setColor(Color.red) ;
		g.drawLine( 0, d.height - 1, d.width, d.height - 1 ) ;
		g.setColor(SavedColor) ;
	}
}
 
}
