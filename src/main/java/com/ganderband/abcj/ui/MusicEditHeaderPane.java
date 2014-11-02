/**
 * The top music editor pane of the ABC GUI - For editting the ABC Header.
 */
package  abcj.ui ;

import  javax.swing.* ;
import  abcj.* ;

public class MusicEditHeaderPane extends JScrollPane
{
/**
 * The owning ABCJ appication instance.
 */
//	private ABCJ  mABCJ ;		// Not yet used

	
/**
 * The default constructor.
 */
public  MusicEditHeaderPane( ABCJ App )
{
//	mABCJ = App ;
	
//	setMinimumSize( new Dimension( 400, 150 ) ) ;
	
	setViewportView( new JLabel("MusicEditHeaderPane") ) ;
}

}
