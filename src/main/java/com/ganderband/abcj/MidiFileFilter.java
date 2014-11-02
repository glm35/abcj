/**
 * A file filter for accepting Midi files.
 */
package  abcj ;

import  java.io.File ;
import  javax.swing.filechooser.* ;

public class MidiFileFilter extends FileFilter
{

/**
 * The accept method for a file filter. 
 */
public boolean  accept( File f )
{
	if ( f.isDirectory() )   return  true ;		// Always show directories

//  Extract the file extension

	String  Ext = null ;
	String  s = f.getName() ;
	int  i = s.lastIndexOf('.') ;
	if ( i > 0  &&  i < s.length() - 1 )
		Ext = s.substring(i + 1).toLowerCase() ;

//  Check if acceptable
	
	return  ( Ext != null  &&  Ext.equals("mid") ) ;
}


/**
 * Get the filter description.
 */
public String  getDescription()
{   return  "Midi Files" ;   }

}
