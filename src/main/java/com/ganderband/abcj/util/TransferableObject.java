/**
 * An object transferable with drag and drop.
 * 
 * <p>The object must be marked as Serializable for this to work OK even if it
 * is not necessary for local objects !
 * 
 * <p>This is only usable within a single JVM.
 */
package  abcj.util ;

import  java.awt.datatransfer.* ;

public class TransferableObject implements Transferable
{
/**
 * A reference to the object being transferred.
 */
	private Object  mObject ;
/**
 * A static data flavor for local JVM objects.
 */
	public static DataFlavor  LOCAL_OBJECT ;

/**
 * A static initialize to create the static flavor.
 */
	static {
		try {
			LOCAL_OBJECT = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType ) ;
		}
		catch ( Exception e ) {
			System.out.println( "Exception ignored - " + e ) ;
			e.printStackTrace() ;
		}
	}
	

/**
 * The standard constructor.
 */
public  TransferableObject( Object Array )
{	mObject = Array ;   }


/**
 * Get the transfer data.
 */
public Object  getTransferData( DataFlavor Flavor )
{	return  ( Flavor == LOCAL_OBJECT ) ? mObject : null ;   }


/**
 * Get an array indicating valid flavors for this instance.
 */
public DataFlavor[]  getTransferDataFlavors()
{   return  new DataFlavor[] { LOCAL_OBJECT } ;   }


/**
 * Check whether flavor is supported.
 */
public boolean  isDataFlavorSupported( DataFlavor Flavor )
{
	return  ( Flavor == LOCAL_OBJECT ) ;
}

}
