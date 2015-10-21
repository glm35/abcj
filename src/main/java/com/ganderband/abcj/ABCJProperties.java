/**
 * The properties used by ABCJ.
 */
package  com.ganderband.abcj ;

import  java.awt.* ;
import  java.util.* ;
import  java.io.* ;

public class ABCJProperties extends Properties
{
/**
 * The ABCJ Properties file name.
 */
	private static final String  PROPS_FILE = "ABCJ.PRO" ;
/**
 * The Starter ABCJ Properties file name.
 */
	private static final String  PROPS_FILE_STARTER = "ABCJ.PRO.starter" ;
/**
 * A static properties instance which holds the properties.
 */
	private static Properties  sProps = new Properties() ;
/**
 * An array list containing the library files.
 * 
 * <p>This makes it easier to access these.
 */
	private static ArrayList  sLibrary = new ArrayList() ;
/**
 * The last remembered size of the library entries in the props file.
 * 
 * <p>This is used to remove all previous entries.
 */
	private static int  sLastPropsLibraryCount ;
/**
 * A local structure to define a library entry.
 */
	public static class LibraryEntry {
			
		public String  mFileName ;
		public String  mTitle ;
			
		LibraryEntry( String FileName, String Title )
		{   mFileName = FileName ;   mTitle = Title ;   }
	}


/**
 * Load up the properties.
 * 
 * <p>If the normal properties file does not exist then the properties
 * will be loader from a start file
 */
public static  void  load()
{
	
//  Initialize the defaults

	sProps.setProperty( "EditMode", 
						Integer.toString(ABCJConstants.TEXT_EDIT_MODE) ) ;
	sProps.setProperty( "LookAndFeel", 
						Integer.toString(ABCJConstants.JAVA_LAF) ) ;

//  Determine which file name to load.
//  If ABCJ.PRO does not exist then load from a starter file ABCJ.PRO.starter
	
	String  FileName = PROPS_FILE ;
	
	if ( ! ( new File(PROPS_FILE) ).exists() )
		FileName = PROPS_FILE_STARTER ;
	
//  Now load from a file

	try {
		sProps.load( new FileInputStream(FileName) ) ;
	}
	
//  If not found then ignore and allow defaults to be kept

	catch ( FileNotFoundException e ) {
		System.out.println("Properties file not found - using defaults") ;
	}

//  Log any other error

	catch ( Exception e ) {
		System.out.println("*ERROR* - Failed to load properties file") ;
		System.out.println(e) ;
	}
	
//  Build the Library array 

	loadLibraryArray() ;
}
 

/**
 * Save the properties.
 */
public static  void  save()
{
	
//  Try and store back to the file
	
	try {
		sProps.store( new FileOutputStream(PROPS_FILE), "ABCJ Properties File" ) ;
	}
	
//  Log any exceptions

	catch ( Exception e ) {
		System.out.println("*ERROR* - Failed to store properties file") ;
		System.out.println(e) ;
	}
}


/**
 * Get a property as a string.
 */
public static String  getPropertyString( String Key )
{	return  sProps.getProperty(Key) ;   }


/**
 * Get a property as an integer.
 */
public static int  getPropertyInt( String Key )
{
	try {   return  Integer.parseInt( getPropertyString(Key) ) ;   }
	catch ( Exception e ) {   return  0 ;   	}
}


/**
 * Get the property as a float.
 */
public static float  getPropertyFloat( String Key )
{
	try {   return  Float.parseFloat( getPropertyString(Key) ) ;   }
	catch ( Exception e ) {   return  0 ;   	}
}


/**
 * Get a property as a rectangle.
 * 
 * <p>This is actually stored as 4 properties.
 */
public static Rectangle  getPropertyRect( String Key )
{
	return  new Rectangle( getPropertyInt( Key + ".x" ),
						   getPropertyInt( Key + ".y" ),
						   getPropertyInt( Key + ".width" ),
						   getPropertyInt( Key + ".height" ) ) ;
}


/**
 * Set a property from a string.
 * 
 * <p>Can't use setProperty as name as it would hide a method !
 */
public static void  setProp( String Key, String Value )
{	sProps.setProperty( Key, Value ) ;   }


/**
 * Set a property from an integer.
 * 
 * <p>Can't use setProperty as name as it would hide a method !
 */
public static void  setProp( String Key, int Value )
{	setProp( Key, Integer.toString(Value) ) ;   }


/**
 * Set a property from a float.
 * 
 * <p>Can't use setProperty as name as it would hide a method !
 */
public static void  setProp( String Key, float Value )
{	setProp( Key, Float.toString(Value) ) ;   }


/**
 * Set a property from a rectangle.
 * 
 * <p>This is actually stored as 4 properties.
 */
public static void  setProp( String Key, Rectangle Rect )
{
	setProp( Key + ".x",      Rect.x      ) ;
	setProp( Key + ".y",      Rect.y      ) ;
	setProp( Key + ".width",  Rect.width  ) ;
	setProp( Key + ".height", Rect.height ) ;
}


/**
 * Get the edit mode.
 */
public static int  getEditMode()
{	return  getPropertyInt("EditMode") ;   }


/**
 * Set the edit mode.
 */
public static void  setEditMode( int EditMode )
{	setProp( "EditMode", EditMode ) ;   }


/**
 * Get the look and feel.
 */
public static int  getLookAndFeel()
{	return  getPropertyInt("LookAndFeel") ;   }


/**
 * Set the look and feel.
 */
public static void  setLookAndFeel( int LookAndFeel )
{	setProp( "LookAndFeel", LookAndFeel ) ;   }


/**
 * Get the File Chooser path name.
 */
public static String  getChooserPathName()
{	return  getPropertyString("ChooserPathName") ;   }


/**
 * Set the File Chooser path name.
 */
public static void  setChooserPathName( String PathName )
{	setProp( "ChooserPathName", PathName ) ;   }


/**
 * Load the library array from the properties file.
 * 
 * <p>This method extracts the array from the properties
 */
private static void  loadLibraryArray()
{
	sLibrary.clear() ;
	
//  Read file entries until no more
	
	for ( int Index = 1 ; ; Index++ ) {
		
	//  Get a single file and exit if not there
	
		String  FileName = getPropertyString(
								 "LibraryFile" + Integer.toString(Index) ) ;
		
		if ( FileName == null )   break ;
		
	//  Read the title entry for the file

		String  Title = getPropertyString( 
								"LibraryTitle" + Integer.toString(Index) ) ;
	
	//  Store the entry in the array
	
		sLibrary.add( new LibraryEntry( FileName, Title ) ) ;
		
		sLastPropsLibraryCount = Index ;
	}
}


/**
 * Store the library array to the properties file.
 * 
 * <p>This method puts the array back into the properties
 */
private static void  storeLibraryArray()
{

//  Clear all old entries

	for ( int i = 1 ; i <= sLastPropsLibraryCount ; i++ ) {
		sProps.remove( "LibraryFile" + Integer.toString(i) ) ;
		sProps.remove( "LibraryTitle" + Integer.toString(i) ) ;
	}

//  Now re-add the current entries

	sLastPropsLibraryCount = sLibrary.size() ;
	
	for ( int i = 1 ; i <= sLastPropsLibraryCount ; i++ ) {
		LibraryEntry  Entry = (LibraryEntry) sLibrary.get(i - 1) ;
		
		setProp( "LibraryFile" + Integer.toString(i), Entry.mFileName ) ;
	
		if ( Entry.mTitle != null )
			setProp( "LibraryTitle" + Integer.toString(i), Entry.mTitle ) ;
	}
}


/**
 * Get the library array.
 */
public static ArrayList  getLibrary()
{	return  sLibrary ;   }


/**
 * Add a new library entry.
 */
public static void  addLibraryEntry( String FileName, String Title )
{
	sLibrary.add( new LibraryEntry( FileName, Title ) ) ;
	storeLibraryArray() ;
}


/**
 * Remove a library entry.
 */
public static void  removeLibraryEntry( String FileName )
{

//  Locate the correct library entry for this file

	int  Index = locateLibraryEntry(FileName) ;

//  Delete it if found

	if ( Index >= 0 ) {
	   sLibrary.remove(Index) ;
	   storeLibraryArray() ;
	} 
}


/**
 * Change a library entry.
 */
public static void  changeLibraryEntry( String FileName, String Title )
{

//	Locate the correct library entry for this file

	int  Index = locateLibraryEntry(FileName) ;

//	Change it if found

	if ( Index >= 0 ) {
		sLibrary.set( Index, new LibraryEntry( FileName, Title ) ) ;
		storeLibraryArray() ;
	} 
}


/**
 * Locate the library entry with the given name.
 */
private static int  locateLibraryEntry( String FileName )
{
	for ( int i = 0 ; i < sLibrary.size() ; i++ ) {
		LibraryEntry  Entry = (LibraryEntry) sLibrary.get(i) ;
		if ( Entry.mFileName.equals(FileName) )
			return  i ;
	}
	
	return  -1 ;
}
 
}
