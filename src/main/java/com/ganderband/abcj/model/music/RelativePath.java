/**
 * A class representing a relative path rather than an absolute path.
 */
package com.ganderband.abcj.model.music ;

import  java.awt.geom.* ;

public class RelativePath
{
/**
 * The underlying general path object represented by this relative path.
 */
	protected GeneralPath  mPath = new GeneralPath() ;
/**
 * The working current x coordinate.
 */
	private float  mX = 0 ;
/**
 * The working current y coordinate.
 */
	private float  mY = 0 ;


//public  RelativePath( double x, double y, GeneralPath Path )
//{	this( (float) x, (float) y, Path ) ;   }
/**
 * Set starting position.
 */
public void  startAt( float x, float y )
{
	mX = x ;
	mY = y ;
	mPath.moveTo( x, y ) ;
}

public void  startAt( double x, double y )
{	startAt( (float) x, (float) y ) ;   }


/**
 * Draw relative curveTo.
 */
public void  curveTo( float x0, float y0, float x1, float y1,
					  float x2, float y2 )
{
	mPath.curveTo( mX + x0, mY + y0, mX + x1, mY + y1, mX + x2, mY + y2 ) ;
	mX += x2 ;
	mY += y2 ;
}

//public void curveTo( double x0, double y0, double x1, double y1,
//					   double x2, double y2 )
//{
//	curveTo( (float) x0, (float) y0, (float) x1, (float) y1,
//			 (float) x2, (float) y2 ) ;
//}


/**
 * Draw relative moveTo.
 */
public void  moveTo( float x0, float y0 )
{
	mPath.moveTo( mX + x0, mY + y0 ) ;
	mX += x0 ;
	mY += y0 ;
}
    
//public void  moveTo( double x0, double y0 )
//{	moveTo( (float) x0, (float) y0 ) ;   }

/**
 * Draw relative moveTo.
 */
public void  lineTo( float x0, float y0 )
{
	mPath.lineTo( mX + x0, mY + y0 ) ;
	mX += x0 ;
	mY += y0 ;
}

//public void  lineTo( double x0, double y0 )
//{	lineTo( (float) x0, (float) y0 ) ;   }

}