/**
 * The glyph class for a rest.
 */
package  com.ganderband.abcj.model.music ;

import  com.ganderband.abcj.model.abc.* ;
import  com.ganderband.abcj.util.* ;

public class GlyphRest extends GlyphSingleNote
{
	
	
/**
 * The default constructors.
 */
public  GlyphRest( RestElement Element )
{	this( 0, 0, Element, null ) ;   }

public  GlyphRest( RestElement Element, Fraction SpecMult )
{	this( 0, 0, Element, SpecMult ) ;   }

	
/**
 * The standard constructor.
 */
public  GlyphRest( float x, float y, RestElement Element, Fraction SpecMult )
{	super( x, y, Element, 0, SpecMult, null ) ;   }

}
