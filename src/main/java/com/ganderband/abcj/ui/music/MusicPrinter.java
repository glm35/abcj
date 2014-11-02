/**
 * A class to manage the printing of music.
 */

package  abcj.ui.music ;

import  java.awt.* ;
import  java.awt.geom.* ;
import  java.awt.print.* ;
import  java.util.* ;
import  abcj.model.* ;
import  abcj.model.abc.* ;
import  abcj.model.music.* ;
import  abcj.util.* ;

public class MusicPrinter implements Printable
{
/**
 * The font to use for the page number.
 */
	private static final Font  PAGE_NUMBER_FONT = 
										new Font( "SansSerif", Font.PLAIN, 10 ) ;
/**
 * The array list of tunes to print.
 */
	private ArrayList  mTunes ; 
/**
 * A private instance of an ABC Parser.
 * 
 * <p>This prevents any conflict between this and the parser thread.
 */
	private ABCParser  mParser = new ABCParser() ;
/**
 * A private instance of a music builder.
 * 
 * <p>This prevents any conflict between this and the parser thread.
 */
	private MusicBuilder  mBuilder = new MusicBuilder() ;
/**
 * The scale factor used for getting the correct width.
 */
	private float  mScaleFactor ;
/**
 * Rectangle describing the printable area of the page.
 */
	Rectangle2D  mPageBounds ;
/**
 * An array list containing an entry for each page.
 * 
 * <p>The entry consists of another array list which contains an entry
 * for each tune to print on the page.
 */
	private ArrayList  mPages ;
/**
 * A class defining the info kept for each tune.
 */
	static class PageEntry {
		public ArrayList  mGlyphs ;
		public Point2D    mOffset ;
		public Dimension  mSize ;
		
		public  PageEntry( ArrayList Glyphs, Point2D Offset, Dimension Size )
		{	mGlyphs = Glyphs ;   mOffset = Offset ;   mSize = Size ;   }
		
	}


/**
 * The standard constructors.
 */
public  MusicPrinter( ArrayList Tunes )
{
	mTunes = Tunes ;
}

public  MusicPrinter( Tune Tune )
{
	mTunes = new ArrayList() ;
	mTunes.add(Tune) ;
}


/**
 * Print the list of tunes in this instance.
 */
public void  print()
{
	mPages = null ;

//	Construct a print job

	PrinterJob  Job = PrinterJob.getPrinterJob() ;
	Job.setPrintable(this) ;

//	Now do the print dialog and print it

	if ( Job.printDialog() ) {

		try {
			Job.print() ;
		}
		catch ( Exception e ) {
			System.out.println("*** Print Error ***") ;
			System.out.println(e) ;
			e.printStackTrace(System.out) ;
		}
	}
}


/**
 * Used for printing this music.
 */
public int  print( Graphics g, PageFormat f, int Index )
{
	
//  Prepare all pages if not yet done

	if ( mPages == null ) {
		determineScaleFactor(f) ;
		prepare() ;
	} 

//  Return if this page is empty

	if ( Index >= mPages.size() )   return  NO_SUCH_PAGE ;
	
//  Add "Page xxx of xxx" at the bottom right

	Graphics2D  g2d = (Graphics2D) g ;
	
	String  s = "Page " + ( Index + 1 ) + " of " + mPages.size() ;
	
	Rectangle2D  r = Utils.getStringBounds( s, PAGE_NUMBER_FONT ) ;

	float  x = (float) ( mPageBounds.getX() + mPageBounds.getWidth()
													- r.getWidth() ) ;
	float  y = (float) ( mPageBounds.getY() + mPageBounds.getHeight()
													- r.getHeight() - r.getX() ) ;
													
	Font  SavedFont = g2d.getFont() ;
	g2d.setFont(PAGE_NUMBER_FONT) ;
	g2d.drawString( s, x, y ) ;
	g2d.setFont(SavedFont) ;
	
//  Do the print by printing all tunes on the page

	ArrayList  Page = (ArrayList) mPages.get(Index) ;

	for ( int i = 0 ; i < Page.size() ; i++ ) {
		PageEntry  Entry = (PageEntry) Page.get(i) ;
		paintMusic( g2d, Entry ) ;
	}
	
	return  PAGE_EXISTS ;
}


/**
 * Used for printing this music.
 *
 * <p>This will parse and build all tunes in the list and allocate each to a
 * page.   The results are stored in the mPage array. 
 */
public void  prepare()
{
	mPages = new ArrayList() ;
	ArrayList  CurrPage = new ArrayList() ;

	float  TotalHeight = 0 ;
	
//  Loop through all tunes

	for (int i = 0 ; i < mTunes.size() ; i++ ) {
		Tune  Tune = (Tune) mTunes.get(i) ;
		
	//  Parse the tune now (not on the parser thread)

		mParser.parse( Tune.getABCText() ) ;
		ABCHeader  Header = mParser.getABCHeader() ;
		ABCMusic   Music  = mParser.getABCMusic() ;
		
	//  Build the tune now (not on the parser thread)
	
		mBuilder.build( Header, Music ) ;
	
		ArrayList  Glyphs = mBuilder.getGlyphs() ;
		Dimension  Size   = mBuilder.getDimension() ;
		
		Size.width  *= mScaleFactor ;
		Size.height *= mScaleFactor ;
	
	//  Adjust the size to correspond to the scale factor
	//  Check if enough space on current page to print it.   If not then
	//  add the current page to the pages array and start a new page 

		if ( TotalHeight + Size.height > mPageBounds.getHeight() ) {
			mPages.add(CurrPage) ;
			CurrPage = new ArrayList() ;
			TotalHeight = 0 ;
		}
	
	//  Add relevant info to the current page
	
		Point2D  Offset = new Point.Double( mPageBounds.getX(),
								   			mPageBounds.getY() + TotalHeight ) ;
		CurrPage.add( new PageEntry( Glyphs, Offset, Size ) ) ;
		
		TotalHeight += Size.height ;
	}
	
//  Add the remaining outstanding bits

	mPages.add(CurrPage) ;
}


/**
 * Determine the correct scale factor for the page format.
 */
public void  determineScaleFactor( PageFormat f )
{
	
//	Determine the printable area of the page (width only)

	double  PrintX  = f.getImageableX() ;
	double  PrintY  = f.getImageableY() ;
	double  PrintW  = f.getImageableWidth() ;
	double  PrintH  = f.getImageableHeight() ;
    
	double  ActualW = f.getWidth() ;
	double  ActualH = f.getHeight() ;

//	Ensure we have at least .5" border on each side
//  Also allow a couple of pels so anti-aliasing will work without truncating

	if ( PrintX < 36 )   PrintX = 36 ;
	if ( PrintY < 36 )   PrintY = 36 ;

	PrintX += 2 ;
	PrintY += 2 ;
	
	PrintW = ActualW - 2 * PrintX ;
	PrintH = ActualH - 2 * PrintY ;
	
//	Determine the scaling factor so it fits.
//  The music should be based on a width + Stave Width + margins

	mScaleFactor = (float) PrintW / MusicBuilder.getStaveWidth() ;
					 
//  Setup the relevant offset point

	mPageBounds = new Rectangle2D.Double( PrintX, PrintY, PrintW, PrintH ) ;
}


/**
 * Paint a single tune.
 * 
 */
public void  paintMusic( Graphics2D g, PageEntry Entry )
{
	AffineTransform  SavedTransform = g.getTransform() ;

//  translate and scale accordingly
	
	g.translate( Entry.mOffset.getX(), Entry.mOffset.getY() ) ;
	g.scale( mScaleFactor, mScaleFactor ) ;

	
// Force anti-aliasing and high quality

	RenderingHints  Hints = new RenderingHints( RenderingHints.KEY_ANTIALIASING,
												 RenderingHints.VALUE_ANTIALIAS_ON ) ;
	Hints.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY ) ;

	g.setRenderingHints(Hints) ;
	
// Loop through all glyphs and display

	 if ( Entry.mGlyphs != null )
		for ( int i = 0 ; i < Entry.mGlyphs.size() ; i++ )
			( (Glyph) Entry.mGlyphs.get(i) ).paint(g) ;
			
//  Restore and return

	g.setTransform(SavedTransform) ;
}

}
