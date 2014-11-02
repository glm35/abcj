/**
 * A class containing some general utilites.
 */
package  abcj.util ;

import  java.awt.* ;
import  java.awt.font.* ;
import  java.awt.geom.* ;
import  java.net.* ;
import  java.io.* ;

public class Utils
{

/**
 * A utility method to remove all whitespace from a string.
 */
public static String  removeAllWhitespace( String Text )
{
	String  Result = "" ;
	for ( int i = 0 ; i < Text.length() ; i++ ) {
		char  c = Text.charAt(i) ;
		if ( c != ' '  &&  c != '\t' )
			Result += c ;
	}
	
	return  Result ;
}


/**
 * Pad a string to the given length.
 */
public static String  pad( String Text, int Length )
{
	if ( Text.length() >= Length )   return  Text ;
	
	StringBuffer  s = new StringBuffer(Length) ;
	s.append(Text) ;
	
	for ( int i = Text.length() ; i < Length ; i++ )
		s.append(' ') ;
	
	return  s.toString() ;
}


/**
 * Concatenate 2 character arrays.
 */
public static char[]  concatenate( char[] a, char[] b )
{
	char[]  Result = new char[ a.length + b.length ] ;
	System.arraycopy( a, 0, Result, 0, a.length ) ;
	System.arraycopy( b, 0, Result, a.length, b.length ) ;
	return  Result ;
}


/**
 * Concatenate a string and a character arrays.
 */
public static char[]  concatenate( String a, char[] b )
{	return  concatenate( a.toCharArray(), b ) ;   }


/**
 * Wait a while.
 */
public static void  sleep( long Interval )
{
	try {   Thread.sleep(Interval) ;   }
	catch ( Exception x ) {
	//  Ignore exceptions as we may forcibly shut the delay down
	}
}


/**
 * Get the deminseions of a string using the default rendering context.
 */
public static Rectangle2D  getStringBounds( String s, Font f )
{	return   f.getStringBounds( s, new FontRenderContext( null, true, true ) ) ;   }


/**
 * Get a resource URL.
 */
public static Image  getImage( Object Source, String Name )
{
	Toolkit  Tools = Toolkit.getDefaultToolkit() ;
	Image	 Img = null ;
	
//  Try and get it as a file in case of override.
//  Note that runnig as an applet will cause a security exception here so we should
//  trap and ignore it
	
	try {
		File  f = new File(Name) ;
		
		if ( f.exists() ) {
			Img = Tools.getImage(Name) ;
			if ( Img != null )   return  Img ;
		}
	}
	catch ( Exception e ) {
	//  Ignore security exception
	}
	
//  Next try and get it from the same place as the requesting class
	
	URL  u = Source.getClass().getResource(Name) ;

	if ( u != null ) {
		Img = Tools.getImage(u) ;
		if ( Img != null )   return  Img ;
	}
	
//  Finally try the class loader
	
	u = Source.getClass().getClassLoader().getResource(Name) ;
	
	if ( u != null ) {
		Img = Tools.getImage(u) ;
		if ( Img != null )   return  Img ;
	}

	return  null ;
}
/**
 * Check if the given URL is accessible.
 *
 * @param u  the URL to validate
 *
 * @return   <code>true</code> if the URL is accessible, <code>false</code> otherwise
 */
public static boolean  validateURL( URL u )
{
	if ( u == null )   return  false ;
	
	try {
		u.openConnection().connect() ;
		return  true ;
	}
	catch ( Exception e )  {   return  false ;   }
}

/**
 * Convert an absolute file name to relative.
 * 
 * <p>For a subdirectory of the current directory, remove the pre-amble.
 * 
 * <p>If not subdirectory then remove current drive if the same so it's relative to the
 * current drive.
 */
public static String  toRelative( String FileName )
{
	
//  Get the current directory and current drive (windows only)
	
	String  CurrDir = ( new File("") ).getAbsolutePath() ;
	
	if ( CurrDir.charAt( CurrDir.length() - 1 ) != File.separatorChar )
		CurrDir += File.separatorChar ;

	int  CurrDirLength = CurrDir.length() ;
	
	String  CurrDrive = null ;
	int     CurrDriveLength = 0 ;

	if ( CurrDirLength > 1  &&  CurrDir.charAt(1) == ':' ) {
		CurrDrive = CurrDir.substring( 0, 2 ) ;
		CurrDriveLength = CurrDrive.length() ;
	}
	
//  Check for a match with the current directory
	
	if ( FileName.length() > CurrDirLength
	 &&  FileName.substring( 0, CurrDirLength ).equals(CurrDir) ) {
		return  FileName.substring(CurrDirLength) ;
	}

//  Check for a match with the current drive
	
	if ( CurrDrive != null
	 &&  FileName.length() > CurrDriveLength
	 &&  FileName.substring( 0, CurrDriveLength ).equals(CurrDrive) ) {
		return  FileName.substring(CurrDriveLength) ;
	}
	
	return  FileName ;
}

}