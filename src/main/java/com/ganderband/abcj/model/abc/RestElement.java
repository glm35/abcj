/**
 * An element representing a rest.
 */
package  com.ganderband.abcj.model.abc ;

import  java.util.* ;

public class RestElement extends NoteElement
{
	
/**
 * The standard constructor.
 */
public  RestElement( String GuitarChord, ArrayList GraceNotes, String Gracings,
					 Note Note, int BrokenRhythm )
{
	super( GuitarChord, GraceNotes, Gracings, Note, BrokenRhythm, false ) ;
	setType(REST_ELEMENT) ;		 	   
}


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "RestElement >" + format() +"<" ;   }

}
