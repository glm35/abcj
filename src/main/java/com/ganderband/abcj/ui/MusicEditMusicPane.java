/**
 * The bottom music editor pane of the ABC GUI - For editting the music.
 */
package  com.ganderband.abcj.ui ;

import  javax.swing.* ;
import  com.ganderband.abcj.* ;

public class MusicEditMusicPane extends JScrollPane
{
/**
 * The owning ABCJ appication instance.
 */
//	private ABCJ  mABCJ ;		// Not yet used
	
/**
 * The default constructor.
 */
public  MusicEditMusicPane( ABCJ App )
{
//	mABCJ = App ;
	
	setViewportView( new JLabel("MusicEditMusicPane") ) ;
}

}
