package com.ganderband.abcj.ui ;

/**
 * A JTextArea field which also provides Cut/Copy/Paste and Undo/Redo.
 */
import  java.awt.event.* ;
import  javax.swing.* ;
import  javax.swing.text.* ;
import  javax.swing.undo.* ;

public class TextAreaWithPopup extends JTextArea 
			implements MouseListener, ActionListener, KeyListener
{
/**
 * The popup menu.
 */
	private JPopupMenu  mPopupMenu ;
/**
 * The Cut menu item.
 */
	private JMenuItem  mMenuCut ;
/**
 * The Copy menu item.
 */
	private JMenuItem  mMenuCopy ;
/**
 * The Paste menu item.
 */
	private JMenuItem  mMenuPaste ;
/**
 * The Select All menu item.
 */
	private JMenuItem  mMenuSelectAll ;
/**
 * The Undo menu item.
 */
	private JMenuItem  mMenuUndo ;
/**
 * The Redo menu item.
 */
	private JMenuItem  mMenuRedo ;
/**
 * The undo manager controlling undo/redo.
 */
	UndoManager  mUndoManager ;

		
/**
 * Construct a text area allowing for a cut/copy/paste popup.
 */
public  TextAreaWithPopup()
{	super() ;   init() ;   }


/**
 * Construct a text area allowing for a cut/copy/paste popup.
 */
public  TextAreaWithPopup( String Text )
{	super(Text) ;   init() ;   }


/**
 * Construct a text area allowing for a cut/copy/paste popup.
 */
public  TextAreaWithPopup( Document Doc )
{	super(Doc) ;   init() ;   }


/**
 * Construct a text area allowing for a cut/copy/paste popup.
 */
public  TextAreaWithPopup( int Rows, int Cols )
{	super( Rows, Cols ) ;   init() ;   }


/**
 * Construct a text area allowing for a cut/copy/paste popup.
 */
public  TextAreaWithPopup( String Text, int Rows, int Cols )
{	super( Text, Rows, Cols ) ;   init() ;   }


/**
 * Construct a text area allowing for a cut/copy/paste popup.
 */
public  TextAreaWithPopup( Document Doc, String Text, int Rows, int Cols )
{	super( Doc, Text, Rows, Cols ) ;   init() ;   }


/**
 * Special initialization for this class.
 */
private void  init()
{
	addPopupMenu() ;

//  Create a javax.swing.undo.UndoManager; this is an amazing class that
//  keeps a Stack of UndoableEdits and lets you invoke them;
//  by registering it as a Listener on the TextComponent.Document,
//  the Document will create the UndoableEdit objects and send them
//  to the UndoManager. Between them they do ALL the work!

    mUndoManager = new UndoManager() ;
    getDocument().addUndoableEditListener(mUndoManager) ;
    
//  We should also listen to key events so the accelerator mechanism works properly !
//  On Windows only the Cut/Copy and Paste options will work otherwise.
//  On Unix ?????
    
    addKeyListener(this) ;
}

/**
 * Add a popup menu to the text area.
 */
public void  addPopupMenu()
{

//  Create the popup menu with Cut, Copy, Paste and Select All
	
	mPopupMenu = new JPopupMenu() ;

	mMenuCut       = new JMenuItem("Cut") ;
	mMenuCopy      = new JMenuItem("Copy") ;
	mMenuPaste     = new JMenuItem("Paste") ;
	mMenuSelectAll = new JMenuItem("Select All") ;
	mMenuUndo      = new JMenuItem("Undo") ;
	mMenuRedo      = new JMenuItem("Redo") ;

	mMenuCut.setAccelerator(
			KeyStroke.getKeyStroke( KeyEvent.VK_X, InputEvent.CTRL_MASK ) ) ; 
	mMenuCopy.setAccelerator(
			KeyStroke.getKeyStroke( KeyEvent.VK_C, InputEvent.CTRL_MASK ) ) ; 
	mMenuPaste.setAccelerator(
			KeyStroke.getKeyStroke( KeyEvent.VK_V, InputEvent.CTRL_MASK ) ) ; 
	mMenuSelectAll.setAccelerator(
			KeyStroke.getKeyStroke( KeyEvent.VK_A, InputEvent.CTRL_MASK ) ) ; 
	mMenuUndo.setAccelerator(
			KeyStroke.getKeyStroke( KeyEvent.VK_Z, InputEvent.CTRL_MASK ) ) ; 
	mMenuRedo.setAccelerator(
			KeyStroke.getKeyStroke( KeyEvent.VK_Y, InputEvent.CTRL_MASK ) ) ; 
	
	mPopupMenu.add(mMenuCut) ;
	mPopupMenu.add(mMenuCopy) ;
	mPopupMenu.add(mMenuPaste) ; 
	mPopupMenu.addSeparator() ;
	mPopupMenu.add(mMenuSelectAll) ;
	mPopupMenu.addSeparator() ;
	mPopupMenu.add(mMenuUndo) ;
	mPopupMenu.add(mMenuRedo) ;

//  Finally listen to the required items
	
	addMouseListener(this) ;
	
	mMenuCut.addActionListener(this) ;
	mMenuCopy.addActionListener(this) ;
	mMenuPaste.addActionListener(this) ;
	mMenuSelectAll.addActionListener(this) ;
	mMenuUndo.addActionListener(this) ;
	mMenuRedo.addActionListener(this) ;
}


/**
 * Listen to Mouse Pressed events.
 */
public void  mousePressed( MouseEvent e )
{	checkForPopupTrigger(e) ;   }


/**
 * Listen to Mouse Released events.
 */
public void  mouseReleased( MouseEvent e )
{	checkForPopupTrigger(e) ;   }


/**
 * Listen to Mouse Clicked events.
 */
public void  mouseClicked( MouseEvent e )
{
//  Nothing needed	
}


/**
 * Listen to Mouse Entered events
 */
public void  mouseEntered( MouseEvent e )
{
//  Nothing needed
}
/**
 * Listen to Mouse Exited events.
 */
public void  mouseExited( MouseEvent e )
{
//  Nothing needed	
}


/**
 * Check a mouse event for being a popup trigger.
 */
private void  checkForPopupTrigger( MouseEvent e )
{ 
	if ( e.isPopupTrigger() ) {
		
	//  Enable undo/redo menu items as requnecessary
		
		mMenuUndo.setEnabled( mUndoManager.canUndo() ) ; 
		mMenuRedo.setEnabled( mUndoManager.canRedo() ) ; 
		
	//  Now show the popup
		
		mPopupMenu.show( e.getComponent(), e.getX(), e.getY() ) ;
	}
}


/**
 * Clear undo history for this text area.
 */
public void  clearUndoHistory()
{	mUndoManager.die();   }


/**
 * Check for a menu item selected.
 */
public void  actionPerformed( ActionEvent e )
{
	
//  Do the Cut, Copy, Paste or Select All actions on the text area
	
	if ( e.getSource() == mMenuCut       )   cut() ;
	if ( e.getSource() == mMenuCopy      )   copy() ;
	if ( e.getSource() == mMenuPaste     )   paste() ;
	if ( e.getSource() == mMenuSelectAll )   selectAll() ;
	
	if ( e.getSource() == mMenuUndo  &&  mUndoManager.canUndo() )
		mUndoManager.undo() ;
	if ( e.getSource() == mMenuRedo  &&  mUndoManager.canRedo() )
		mUndoManager.redo() ;
}


/**
 * A key has been pressed.
 */
public void keyPressed( KeyEvent e )
{
//  Nothing required
}


/**
 * A key has been released.
 */
public void keyReleased( KeyEvent e )
{
	
//  Select the relevant key codes and send send the appropriate action event
	
	ActionEvent Action = null ;
	
//  Windows already manages the Cut, Copy and Paste keys (Ctrl-X, Ctrl-C,Ctrl-V)
//  We must specifically ignore them here otherwise the action will happen twice
//	(particularly Paste !!)
	
	if ( System.getProperty("os.name").indexOf("Windows") >= 0 ) {
		if ( ( e.getKeyCode() == KeyEvent.VK_C
		   ||  e.getKeyCode() == KeyEvent.VK_V
		   ||  e.getKeyCode() == KeyEvent.VK_X )
		 &&  ( e.getModifiers() == InputEvent.CTRL_MASK ) )
			return ;
	}

//  Generate the Undo Event for the appropriate accelerator

	KeyStroke  KS        = mMenuUndo.getAccelerator() ;
	int        KeyCode   = KS.getKeyCode() ;
	int		   Modifiers = KS.getModifiers() & 0x0F ;	// Only RH 4 bits 
	
	if ( e.getKeyCode() == KeyCode  &&  ( e.getModifiers() & Modifiers ) == Modifiers )
		Action = new ActionEvent( mMenuUndo, 0, null ) ;
	
//  Generate the Redo Event for the appropriate accelerator
	
	KS        = mMenuRedo.getAccelerator() ;
	KeyCode   = KS.getKeyCode() ;
	Modifiers = KS.getModifiers() & 0x0F ;	// Only RH 4 bits 
	
	if ( e.getKeyCode() == KeyCode  &&  ( e.getModifiers() & Modifiers ) == Modifiers )
		Action = new ActionEvent( mMenuRedo, 0, null ) ;
	
//  Generate the Cut Event for the appropriate accelerator
	
	KS        = mMenuCut.getAccelerator() ;
	KeyCode   = KS.getKeyCode() ;
	Modifiers = KS.getModifiers() & 0x0F ;	// Only RH 4 bits 
	
	if ( e.getKeyCode() == KeyCode  &&  ( e.getModifiers() & Modifiers ) == Modifiers )
		Action = new ActionEvent( mMenuCut, 0, null ) ;
	
//  Generate the Copy Event for the appropriate accelerator
	
	KS        = mMenuCopy.getAccelerator() ;
	KeyCode   = KS.getKeyCode() ;
	Modifiers = KS.getModifiers() & 0x0F ;	// Only RH 4 bits 
	
	if ( e.getKeyCode() == KeyCode  &&  ( e.getModifiers() & Modifiers ) == Modifiers )
		Action = new ActionEvent( mMenuCopy, 0, null ) ;
	
//  Generate the Paste Event for the appropriate accelerator
	
	KS        = mMenuPaste.getAccelerator() ;
	KeyCode   = KS.getKeyCode() ;
	Modifiers = KS.getModifiers() & 0x0F ;	// Only RH 4 bits 
	
	if ( e.getKeyCode() == KeyCode  &&  ( e.getModifiers() & Modifiers ) == Modifiers )
		Action = new ActionEvent( mMenuPaste, 0, null ) ;
	
//  Generate the Select All Event for the appropriate accelerator
	
	KS        = mMenuSelectAll.getAccelerator() ;
	KeyCode   = KS.getKeyCode() ;
	Modifiers = KS.getModifiers() & 0x0F ;	// Only RH 4 bits 
	
	if ( e.getKeyCode() == KeyCode  &&  ( e.getModifiers() & Modifiers ) == Modifiers )
		Action = new ActionEvent( mMenuSelectAll, 0, null ) ;

//  Finally generate the ncessary action	
	
	if ( Action != null )   actionPerformed(Action) ;
}


/**
 * A key has been released.
 */
public void keyTyped( KeyEvent e )
{
//  Nothing required
}

}