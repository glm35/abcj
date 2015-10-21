/**
 * A class representing a general musical glyph.
 * 
 * <p>These symbolds a represented as Java2D shapes.
 */
package  com.ganderband.abcj.model.music ;

import  java.awt.geom.* ;
import  java.awt.* ;
import  com.ganderband.abcj.model.abc.* ;

public abstract class Glyph implements Shape, GlyphMetrics, ABCParserConstants
{
/**
 * The absolute path for this glyph.
 */
	protected GeneralPath  mPath ;
/**
 * The width of this glyph.
 */
	protected float  mWidth = 0.0F ;
/**
 * The height of this glyph.
 * 
 * <p>Currently only used for tuplets in glyphs.  May not be needed at all in future !!
 */
	protected float  mHeight = 0.0F ;
/**
 * The x co-ordinate to draw this glyph at.
 */
	protected float  mX ;
/**
 * The y co-ordinate to draw this glyph at.
 */
	protected float  mY ;
/**
 * Leading space to pad before this glyph.
 */
	protected float  mLeadingSpace = 0.0F ;
/**
 * Trailing space to pad after this glyph.
 */
	protected float  mTrailingSpace = 0.0F ;
/**
 * The stave number of this glyph.
 */
	protected int  mStaveNumber = 0 ;


/**
 * The default constructor.
 */
public  Glyph()
{	this( 0, 0 ) ;   }


/**
 * The standard constructor.
 */
public  Glyph( float x, float y )
{	mX = x ;
	mY = y ;
	mLeadingSpace = mTrailingSpace = 0 ;
}


/**
 * Get the width of this glyph.
 */
public float  getWidth()
{	return  mWidth ;   }


/**
 * Get the leading space for this glyph.
 */
public float  getLeadingSpace()
{	return  mLeadingSpace ;   }


/**
 * Get the trailing space for this glyph.
 */
public float  getTrailingSpace()
{	return  mTrailingSpace ;   }


/**
 * Get the height of this glyph.
 */
public float  getHeight()
{	return  mHeight ;   }


/**
 * Get the Stave containing this glyph.
 */
public int  getStaveNumber()
{	return  mStaveNumber ;   }


/**
 * Set the Stave containing this glyph.
 */
public void  setStaveNumber( int StaveNumber )
{	mStaveNumber = StaveNumber ;   }


/*
 * Shape.contains - required by the Shape interface.
 */
public boolean  contains( double arg0, double arg1, double arg2, double arg3 )
{	return  mPath.contains( arg0, arg1, arg2, arg3 ) ;   }


/*
 * Shape.contains - required by the Shape interface.
 */
public boolean  contains( double arg0, double arg1 )
{	return  mPath.contains( arg0, arg1 ) ;   }


/*
 * Shape.contains - required by the Shape interface.
 */
public boolean  contains( Point2D arg0 )
{	return  mPath.contains(arg0) ;   }


/*
 * Shape.contains - required by the Shape interface.
 */
public boolean  contains( Rectangle2D arg0 )
{	return  mPath.contains(arg0) ;   }


/*
 * Shape.getBounds - required by the Shape interface.
 */
public Rectangle  getBounds()
{	return  mPath.getBounds() ;   }


/*
 * Shape.getBounds - required by the Shape interface.
 */
public Rectangle2D getBounds2D()
{	return  mPath.getBounds2D() ;   }


/*
 * Shape.getPathIterator - required by the Shape interface.
 */
public PathIterator  getPathIterator( AffineTransform arg0, double arg1 )
{	return  mPath.getPathIterator( arg0, arg1 ) ;   }


/*
 * Shape.getPathIterator - required by the Shape interface.
 */
public PathIterator  getPathIterator( AffineTransform arg0 )
{	return  mPath.getPathIterator(arg0 ) ;   }


/*
 * Shape.intersects - required by the Shape interface.
 */
public boolean  intersects( double arg0, double arg1, double arg2, double arg3 )
{	return  mPath.intersects( arg0, arg1, arg2, arg3 ) ;   }


/*
 * Shape.intersects - required by the Shape interface.
 */
public boolean  intersects( Rectangle2D arg0 )
{	return  mPath.intersects(arg0) ;   }
	
/**
 * Set the path for this glyph.
 */
public void  setPath( GeneralPath Path )
{
	mPath   = Path ;
	mWidth  = (float) Path.getBounds2D().getWidth() + 1 ;	// Allow a small gap
	mHeight = (float) Path.getBounds2D().getHeight() ;
}


/**
 * Set the current co-ordinates.
 */
public void  setCoords( float x, float y )
{	mX = x ;   mY = y ;   }


/**
 * Paint this glyph to the 2D graphics context supplied.
 * 
 * <p>The default behaviour is to just fill the path of this glyph.
 * 
 * <p>Note that the graphics context supplied my return with a transform in
 * place.   Most likely, this has just moved to the left by the specified
 * width of the component.
 */
public void  paint( Graphics2D g )
{
	
//  Save transform and position at correct place

	AffineTransform  SavedTransform = g.getTransform() ;

	g.translate( mX + mLeadingSpace, mY ) ;
	
//  Standard drawing is just to fill the glyph

	if ( mPath != null )   g.fill(this) ;

//	g.setStroke( new BasicStroke(0.3F) ) ;
//	Rectangle2D.Float r = new Rectangle2D.Float( 0, 0,
//									 mWidth, (float) getBounds().getHeight() ) ;	
//	g.draw(r) ;	

//	Restore transform and return 

	g.setTransform(SavedTransform) ;
}

}
