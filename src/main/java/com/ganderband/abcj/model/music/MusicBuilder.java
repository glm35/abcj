/**
 * 
 * <p>This class builds the music notation model from the ABC model.
 * It is called from the ABCParser.parse method as part of the parsing process.
 */
package  abcj.model.music ;

import  java.awt.* ;
import  java.awt.geom.* ;
import  java.util.* ;
import  abcj.model.abc.* ;
import  abcj.util.* ;

public class MusicBuilder implements ABCParserConstants
{
/**
 * Builder status - currently building.
 */
	public static final int  IS_BUILDING = 0 ;
/**
 * Builder status - not building - OK.
 */
	public static final int  BUILT_OK = 1 ;
/**
 * Builder status - not building - no results (nothing to build).
 */
	public static final int  NO_RESULTS = 2 ;
/**
 * The basic number of pixels vertically for a new stave.
 */
	protected static final float  STAVE_SEP_MUSIC = 60 ;
/**
 * The number of extra pixels to add vertically for a stave with chords.
 */
	protected static final float  STAVE_SEP_CHORDS = 15 ;
/**
 * The number of extra pixels to add vertically for a stave with lyrics.
 */
	protected static final float  STAVE_SEP_LYRICS = 12 ;
/**
 * The number of pixels to add at the top so the music doesn't get overwritten.
 */
	protected static final float  STAVE_SPACE_TOP = 40 ;
/**
 * The number of pixels to add at the bottom so the music doesn't get overwritten.
 */
	protected static final float  STAVE_SPACE_BOTTOM = 60 ;
/**
 * The maximum stretch amount when spacing items to fit a stave.
 */
	private static final float  MAX_STRETCH = 1.7F ;
/**
 * The font to use for the title.
 */
	public static final Font  TITLE_FONT = new Font( "SansSerif", Font.BOLD, 14 ) ;
/**
 * The font to use for the composer.
 */
	public static final Font  COMPOSER_FONT = new Font( "SansSerif", Font.ITALIC, 14 ) ;
/**
 * The font to use for the info fields.
 */
	public static final Font  INFO_FONT = new Font( "SansSerif", Font.PLAIN, 12 ) ;
/**
 * The font to use for words.
 */
	public static final Font  WORDS_FONT = new Font( "Monospaced", Font.PLAIN, 12 ) ;
/**
 * The current status of the builder.
 */
	private int  mBuilderState = NO_RESULTS ;
/**
 * The array of all music glyphs to show. 
 */
	private ArrayList  mGlyphs ;
/**
 * The array of all music words tokens to show.
 * 
 *  <p>These are kept separate so as not to interfere with the normal calculations when
 *  making music lines.
 */
	private ArrayList  mWordTokens ;
/**
 * The current x co-ordinate.
 */
	private float  mCurrentX ;
/**
 * The current y co-ordinate.
 */
	private float  mCurrentY ;
/**
 * The required stave width (in pixels).
 * 
 * <p>This is shared between all instances. 
 */
	private static float  sStaveWidth = 400 ;
/**
 * The required stave height (in pixels).
 * 
 * <p>This is shared between all instances and allocates the number of pixels vertically
 * for each stave.   It has a basic value for the music but may be adjust depending on the presence
 * of chords and lyrics. 
 */
	private static float  sStaveHeight = 0 ;
/**
 * Show tempo on the stave or not.
 * 
 * <p>This is shared between all instances. 
 */
	private static boolean  sShowTempo = false ;
/**
 * Ignore ABC Line Breaks.
 * 
 * <p>This is shared between all instances. 
 */
	private static boolean  sIgnoreBreaks = false ;
/**
 * Show Clef on each line.
 * 
 * <p>This is shared between all instances. 
 */
	private static boolean  sShowClef = false ;
/**
 * Show info fields from the header or not.
 * 
 * <p>This is shared between all instances. 
 */
	private static boolean  sShowInfoFields = false ;
/**
 * Show lyrics or not.
 * 
 * <p>This is shared between all instances. 
 */
	private static boolean  sShowLyrics = false ;
/**
 * The index of the first glyph on the current stave.
 */ 
	private int  mFirstGlyphIndex ;
/**
 * The index of the last glyph on the current stave.
 */ 
	private int  mLastGlyphIndex ;
/**
 * The index of the last bar-line or nth repeat on the current stave.
 */ 
	private int  mLastBarLineIndex ;
/**
 * The current stave being processed. 
 */
	private GlyphStave  mCurrentStave ;
/**
 * The number of staves so far.
 */
	private int  mStaveCount = 0 ;
/**
 * The current stem direction to use (if forced).
 */
	private int  mForceDirection = 0 ;
/**
 * The current key signature.
 */
	private Key  mCurrentKey ;
/**
 * The dimensions required to show the music.
 */	
	private Dimension  mDimensions = new Dimension() ;
/**
 * The current beamer being built.
 */
	private GlyphBeamer  mCurrentBeamer = null ;
/**
 * An array of all the beamers.
 * 
 * <p>This is maintained so we can recalculate at the end without having
 * to search all the glyphs.
 */
	private ArrayList  mBeamers ;
/**
 * The current slur being built.
 */
	private GlyphSlur  mCurrentSlur = null ;
/**
 * An array of all the slurs.
 * 
 * <p>This is maintained so we can recalculate at the end without having
 * to search all the glyphs.
 */
	private ArrayList  mSlurs ;
/**
 * The current level of slur.
 * 
 * <p>Whilst can ve nested, only the outer slur is drawn.
 */
	private int  mSlurLevel ;
/**
 * The current tie being built.
 * 
 * <p>This will be added to the slur array when done as drawing is the same.
 */
	private GlyphSlur  mCurrentTie = null ;
/**
 * A flag to indicate if a new line is required.
 * 
 * <p>This flag will be set at then end of an ABC music line without
 * continuation.   The Ignore Breaks flag can be used to override this.
 */
	private boolean  mStartNewStave = false ;
/**
 * The most recent line break character.
 */
	private String  mLastLineBreak ;
/**
 * A lookup table for the header info field elements.
 * 
 * <p>This table is populated by a static initializer.
 */
	private static Hashtable  sInfoFields = new Hashtable() ;
/**
 * A static initializer to populate the header info fields lookup table.
 * 
 * <p>Note that words are handled separately
 */
	static {
		sInfoFields.put( "A", "Area" ) ;
		sInfoFields.put( "B", "Book" ) ;
		sInfoFields.put( "D", "Discography" ) ;
		sInfoFields.put( "F", "File Name" ) ;
		sInfoFields.put( "G", "Group" ) ;
		sInfoFields.put( "H", "History" ) ;
		sInfoFields.put( "I", "Information" ) ;
		sInfoFields.put( "N", "Notes" ) ;
		sInfoFields.put( "O", "Origin" ) ;
		sInfoFields.put( "P", "Parts" ) ;
		sInfoFields.put( "R", "Rhythm" ) ;
		sInfoFields.put( "S", "Source" ) ;
		sInfoFields.put( "Z", "Transcription Note" ) ;
	}
	
	
/**
 * Get the current builder status.
 */
public int  getStatus()
{	return  mBuilderState ;   }


 /**
 * Get the array glyphs to show.
 */
public ArrayList  getGlyphs()
{	return  mGlyphs ;   }


/**
 * Get the required drawing dimensions.
 */
public Dimension  getDimension()
{	return  mDimensions ;   }


/**
 * Get the stave width currently in operation.
 */
public static float  getStaveWidth()
{	return  sStaveWidth ;   }


/**
 * Set show tempo state.
 */
public static void  setShowTempo( boolean ShowTempo )
{
//	while ( mBuilderState == IS_BUILDING )   Utils.sleep(20) ;
	sShowTempo = ShowTempo ;
}


/**
 * Set ignore line breaks state.
 */
public static void  setIgnoreBreaks( boolean IgnoreBreaks )
{
//	while ( mBuilderState == IS_BUILDING )   Utils.sleep(20) ;
	sIgnoreBreaks = IgnoreBreaks ;
}


/**
 * Set show info fields state.
 */
public static void  setShowInfoFields( boolean ShowInfo )
{
//	while ( mBuilderState == IS_BUILDING )   Utils.sleep(20) ;
	sShowInfoFields = ShowInfo ;
}


/**
 * Set show lyrics state.
 */
public static void  setShowLyrics( boolean ShowLyrics )
{
//	while ( mBuilderState == IS_BUILDING )   Utils.sleep(20) ;
	sShowLyrics = ShowLyrics ;
}


/**
 * Set show clef on each line state.
 */
public static void  setShowClef( boolean ShowClef )
{
//	while ( mBuilderState == IS_BUILDING )   Utils.sleep(20) ;
	sShowClef = ShowClef ;
}


/**
 * Set the stave width currently in operation.
 */
public static void  setStaveWidth( float StaveWidth )
{
	
//  Wait until the builder has stopped before changing

//	while ( mBuilderState == IS_BUILDING )   Utils.sleep(20) ;
	sStaveWidth = StaveWidth ;
}


/**
 * Get the current stave height.
 */
public static float  getStaveHeight()
{	return  sStaveHeight ;   }


/**
 * Get the show lyrics flag.
 */
public static boolean  getShowLyrics()
{	return  sShowLyrics ;   }

	
/**
 * The build routine which constructs the array of glyphs. 
 */
public void  build( ABCHeader Hdr, ABCMusic Music )
{
	
//  Initialize

	mBuilderState = IS_BUILDING ;
	mGlyphs     = new ArrayList() ;		// Don't overwrite previous
	mBeamers    = new ArrayList() ;
	mSlurs      = new ArrayList() ;
	mWordTokens = new ArrayList() ;
	
	mCurrentX = 0 ;
	mCurrentY = 0 ;
	
//	System.out.println("Start Build") ;
	
//	long  StartTime = System.currentTimeMillis() ;
	
//  Create the header consisting of title, composer and optional fields

	createMusicHeader(Hdr) ;	
	
//  Determine the actual stave height to use.  Depends on chords and lyrics.
	
	sStaveHeight = STAVE_SEP_MUSIC ;
	
	if ( Music.hasChords() )
		sStaveHeight += STAVE_SEP_CHORDS ;
	if ( Music.hasLyrics()  &&  sShowLyrics )
		sStaveHeight += STAVE_SEP_LYRICS ;
	
//  Create an initial stave object allowing for tempo indication or not

	mCurrentY += STAVE_SPACE_TOP ;
	if ( ! sShowTempo )   mCurrentY -= 5 ;

	mStaveCount = 1 ;
	mCurrentStave = new GlyphStave(sStaveWidth) ;
	
	addNoAdvance(mCurrentStave) ;

//  Add tempo if requested

	if ( sShowTempo )
		addNoAdvance( new GlyphTempo( Hdr.getTempo() ) ) ;
	
//  Add a treble clef at the start

	add( new GlyphTrebleClef() ) ;
	
	mFirstGlyphIndex = mLastGlyphIndex = mLastBarLineIndex = mGlyphs.size() - 1 ; 

//  Get the key signature from the header (may be default) amd create
//  the required key signature glyph

	mCurrentKey = Hdr.getKeyElement().getKey() ;

	add( new GlyphKeySignature(mCurrentKey) ) ;
	
	mForceDirection = mCurrentKey.getStemDirection() ;

//	Get the time signature from the header (may be default) amd create
//	the required time signature glyph

	add( new GlyphTimeSignature( Hdr.getMeter() ) ) ;

//  Now read through all the elements in the music

	if ( Music.getElements().size() == 0 )   return ;
	
//  Indicate no beaming or slurring in progress

	mCurrentBeamer = null ;
	mCurrentSlur   = null ;
	mSlurLevel     = 0 ;
	mCurrentTie    = null ;
	mStartNewStave = false ;
	mLastLineBreak = null ;

//	Now process all music elements by following the chain

	ABCElement  Element = Music.getRootElement() ;
	
	while ( Element != null ) {
		
	//  If element added by parser then it should not show on the music
	//  so just skip on to the next element
	
		if ( Element.isParserAdded() ) {
			Element = Element.getNextElement() ;
			continue ;
		}
		
	//  Switch on the element type as it should be a little quicker then
	//  using instanceof
	
		switch ( Element.getType() ) {
			
		case  METER_FIELD_ELEMENT :	
			handleMeter( (MeterFieldElement) Element ) ;	    break ;
		case  KEY_FIELD_ELEMENT :	
			handleKey( (KeyFieldElement) Element ) ;		    break ;
		case  TEMPO_FIELD_ELEMENT :
			handleTempo( (TempoFieldElement) Element ) ;	    break ;
		case  NOTE_ELEMENT :
			handleNote( (NoteElement) Element ) ;			    break ;
		case  REST_ELEMENT :
			handleRest( (RestElement) Element ) ;			    break ;
		case  MULTINOTE_ELEMENT :
			handleMultiNote( (MultiNoteElement) Element ) ;     break ;
		case  TUPLET_ELEMENT :
			handleTuplet( (TupletElement) Element ) ;		    break ;
		case  BARLINE_ELEMENT :
			handleBarLine( (BarLineElement) Element ) ;		    break ;		
		case  NTHREPEAT_ELEMENT :
			handleNthRepeat( (NthRepeatElement) Element ) ;	    break ;		
		case  BEGINSLUR_ELEMENT :
			handleBeginSlur( (BeginSlurElement) Element ) ;	    break ;		
		case  ENDSLUR_ELEMENT :
			handleEndSlur( (EndSlurElement) Element ) ;		    break ;
		case  MUSIC_LINE_ELEMENT :
			handleMusicLine( (MusicLineElement) Element ) ;	    break ;
		case  WORDS_FIELD_ELEMENT :
			handleWordsField( (WordsFieldElement) Element ) ;   break ;
		}
		
	//  Move on to the next element
				
		Element = Element.getNextElement() ;
	}
	
//  If slur still running then close it down

	if ( mCurrentSlur != null )   mSlurs.add(mCurrentSlur) ;
	
//	If tie still running then close it down

	if ( mCurrentTie != null )   mSlurs.add(mCurrentTie) ;

//  Spread out the very last stave

	spreadCurrentStave( mFirstGlyphIndex, mLastGlyphIndex, true ) ;
	
//   Calculate and add all beaming

	for ( int i = 0 ; i < mBeamers.size() ; i++ ) {
		GlyphBeamer  b = (GlyphBeamer) mBeamers.get(i) ;
		b.calculate() ;
		mGlyphs.add(b) ;
	}
	
//	Calculate and add all slurs

   for ( int i = 0 ; i < mSlurs.size() ; i++ ) {
	   GlyphSlur  s = (GlyphSlur) mSlurs.get(i) ;
	   s.calculate() ;
	   mGlyphs.add(s) ;
   }
	
//  Add some space to prevent overlap of anything following

    mCurrentX = 0 ;	
    mCurrentY += STAVE_SPACE_BOTTOM ;
	
//  Now add saved words once music has been completely built.
//  Doing this whilst the music lines is being built causes a lot of
//  interference and it's much easier to do it separately here then
//  fix the interference !!!

    if ( sShowLyrics )   finalizeWords() ;

//  Create the trailer consisting of any words

	createMusicTrailer(Hdr) ;	

//  Finally set the appropriate page size

	mDimensions.setSize( sStaveWidth, mCurrentY ) ;

//  Built OK - tidy up and return	

//	long  BuildTime = System.currentTimeMillis() ;

	mBuilderState = BUILT_OK ;

//	System.out.println( "B=" + ( BuildTime - StartTime ) ) ; 
//	System.out.println("End Build") ;
}


/**
 * Create the header for the tune.
 * 
 * <p>The header shows the title and composer.
 */
public void  createMusicHeader( ABCHeader Hdr )
{
	float  MaxHeight = 0 ;	// The maximum height of the header
	float  SaveY = mCurrentY ;
	
//  Add the title if present (composer goes on the same line)

	if ( Hdr.getTitle() != null ) {
		Rectangle2D  r = Utils.getStringBounds( Hdr.getTitle(), TITLE_FONT ) ;

		MaxHeight = (float) r.getHeight() ;
		
		mCurrentY  -= r.getY() ;   // A little further down
		addNoAdvance( new GlyphText( Hdr.getTitle(), TITLE_FONT ) ) ;
	}
	
//	Add the composer if present (on the same line as the title)

	if ( Hdr.getComposer() != null ) {

		mCurrentY = SaveY ;
		
		Rectangle2D  r = Utils.getStringBounds( Hdr.getComposer(), COMPOSER_FONT ) ;
		
		mCurrentX = (float) ( sStaveWidth - r.getWidth() ) ;
		
		if ( r.getHeight() > MaxHeight )   MaxHeight = (float) r.getHeight() ;
		
		mCurrentY  -= r.getY() ;   // A little further down
		addNoAdvance( new GlyphText( Hdr.getComposer(), COMPOSER_FONT ) ) ;
	}

//  Reposition a little further down

	mCurrentY += MaxHeight ;
	mCurrentX = 0 ;
	
//  Do no more if header info fields no required
	
	if ( ! sShowInfoFields )   return ;
	
//  Show all the info fields
	
	String  LastType = "" ;

	ArrayList  Elements = Hdr.getElements() ;
	
	for ( int i = 0 ; i < Elements.size() ; i++ ) {
		if ( ! ( Elements.get(i) instanceof FieldElement ) )   continue ;
		
		FieldElement  e = (FieldElement) Elements.get(i) ;
		
	//  Process a single field element and determine if it needs showing by attempting
	//  to locate its description in a lookup table
	
		String  Desc = (String) sInfoFields.get( "" + e.getFieldType() ) ;
		
		if ( Desc == null )   continue ;

		Rectangle2D  r = Utils.getStringBounds( Desc, INFO_FONT ) ;
		
	//  If this is the first occurrence of this item then add some space before it
		
		if ( LastType != Desc ) {
			mCurrentY += r.getHeight() ;
			LastType = Desc ;
		}
		
	//  Add the field itself as a text glyph
		
		addNoAdvance( new GlyphText( Desc + " : " + e.formatField(), INFO_FONT ) ) ;

	//  Adjust coordinates appropriately
		
		mCurrentX = 0 ;
		mCurrentY += r.getHeight() ;
	}
}


/**
 * Create the trailer for the tune.
 * 
 * <p>The trailer only shows the words from W: if enabled.
 */
public void  createMusicTrailer( ABCHeader Hdr )
{
	
//  Ignore if words not enabled
	
	if ( ! sShowLyrics )   return ;
	
//  Locate and show all the lyrics fields
	
	String  LastType = "" ;

	ArrayList  Elements = Hdr.getElements() ;
	
	for ( int i = 0 ; i < Elements.size() ; i++ ) {
		if ( ! ( Elements.get(i) instanceof FieldElement ) ) continue ; 
		
		FieldElement  e = (FieldElement) Elements.get(i) ;
		
	//  Ignore none word elements

		if ( e.getFieldType() != 'W' )   continue ;
		
		String  Text = e.formatField() ;
		Rectangle2D  r = Utils.getStringBounds( Text, WORDS_FONT ) ;
		
	//  If this is the first occurrence of this item then add some space before it
		
		if ( LastType != "W" ) {
			mCurrentY += r.getHeight() ;
			LastType = "W" ;
		}
		
	//  Add the line itself as a text glyph
		
		addNoAdvance( new GlyphText( Text, WORDS_FONT ) ) ;

	//  Adjust coordinates appropriately
		
		mCurrentX = 0 ;
		mCurrentY += r.getHeight() ;
	}
}


/**
 * Handle a meter field element.
 */
public void  handleMeter( MeterFieldElement Element ) 
{	addGlyph( new GlyphTimeSignature(Element) ) ;   }


/**
 * Handle a key field element.
 */
public void  handleKey( KeyFieldElement Element ) 
{
	
//  If the same key then just ignore it

	if ( mCurrentKey.equals( Element.getKey() ) )   return ;
	
	mCurrentKey = Element.getKey() ;
	mForceDirection = mCurrentKey.getStemDirection() ;
	
//  Only add the glyph if we are not starting a new stave.
//  If we are going to start a new stave then the following item will
//  cause the key sig to be added
 
 	if ( ! mStartNewStave )
		addGlyph( new GlyphKeySignature(mCurrentKey) ) ;
}


/**
 * Handle a tempo field element.
 */
public void  handleTempo( TempoFieldElement Element ) 
{
	if ( sShowTempo )   addNoAdvance( new GlyphTempo(Element) ) ;
}


/**
 * Handle a note element.
 */
public void  handleNote( NoteElement Element ) 
{
	
//  Check if a beamer needs starting so we know this before building the glyph

	checkStartBeamer(Element) ;
	
//  Create and add the glyph

	Glyph  g = new GlyphSingleNote( Element, mForceDirection, mCurrentBeamer ) ;
	addGlyph(g) ;
	
//  If beamer in progress then this note is beamed so we must add it to
//  the current beamer
										   
	if ( mCurrentBeamer != null )   mCurrentBeamer.addBeamGlyph(g) ;
	
//  Finally check if the current beamer should be closed (and added to the list)

	checkStopBeamer(Element) ;
	
//  If slur in progress then we should add this item

	if ( mCurrentSlur != null )   mCurrentSlur.addSlurGlyph(g) ;
	
//  If this note is tied and a tie is not progress then create a new tie

	boolean  isTied = ( Element.getNote().getTie() != null ) ;

	if ( isTied  &&  mCurrentTie == null )   mCurrentTie = new GlyphSlur(this) ;

//  If a tie is in progress then add the current note.
//  If current is not tied then terminate the current tie afterwards
 
	if ( mCurrentTie != null ) {
		mCurrentTie.addSlurGlyph(g) ;
		
		if ( ! isTied ) {
			mSlurs.add(mCurrentTie) ;
			mCurrentTie = null ;			
		}
	}
}


/**
 * Handle a rest element.
 */
public void  handleRest( RestElement Element ) 
{
	Glyph  g = new GlyphRest(Element) ;
	addGlyph(g) ;
	
//	If slur in progress then we should add this item

	if ( mCurrentSlur != null )   mCurrentSlur.addSlurGlyph(g) ;
}


/**
 * Handle a multi-note element.
 */
public void  handleMultiNote( MultiNoteElement Element ) 
{
	
//	Check if a beamer needs starting so we know this before building the glyph

	checkStartBeamer(Element) ;
	
//	Create and add the glyph

	Glyph  g = new GlyphMultiNote( Element, mForceDirection, mCurrentBeamer ) ;
	addGlyph(g) ;
	
//	If beamer in progress then this note is beamed so we must add it to
//	the current beamer
										   
	if ( mCurrentBeamer != null )   mCurrentBeamer.addBeamGlyph(g) ;
	
//	Finally check if the current beamer should be closed (and added to the list)

	checkStopBeamer(Element) ;
	
//	If slur in progress then we should add this item

	if ( mCurrentSlur != null )   mCurrentSlur.addSlurGlyph(g) ;
}


/**
 * Handle a tuplet element.
 */
public void  handleTuplet( TupletElement Element ) 
{
	
//  Build the tuplet glyph itself 

	GlyphTuplet  g = new GlyphTuplet( Element, mForceDirection ) ;

//  Note that we don't draw the tuplet glyph itself, instead, we should add
//  all its individual notes 
	
	for ( int i = 0 ; i < g.mGlyphs.size() ; i++ ) {
		Glyph  gi = (Glyph) g.mGlyphs.get(i) ;
		addGlyph(gi) ;
		if ( mCurrentSlur != null )   mCurrentSlur.addSlurGlyph(gi) ;
	}

//  If the glyph has its own beamer then this should also be stored.
//  Similarly for a slur instead of a beamer.
				
	if ( g.mBeamer != null )    mBeamers.add(g.mBeamer) ;		
	if ( g.mSlur   != null )    mSlurs.add(g.mSlur) ;		
}


/**
 * Handle a bar-line element.
 */
public void  handleBarLine( BarLineElement Element ) 
{	addGlyph( new GlyphBarLine(Element) ) ;   }


/**
 * Handle an Nth repeat element.
 */
public void  handleNthRepeat( NthRepeatElement Element ) 
{
	
//  We should separate out combined bar-line + repeat into separate glyphs

	
//	Generate the relevant glyphs for each bar-line type

	switch ( Element.getNthRepeatType() ) {
		
	case  NthRepeatElement.FIRST :
	case  NthRepeatElement.SECOND :
		addGlyph( new GlyphNthRepeat(Element) ) ;
		break ;		
		
	case  NthRepeatElement.FIRST_FULL :
	case  NthRepeatElement.BARLINE_FIRST :
		addGlyph( new GlyphBarLine(
						new BarLineElement(BarLineElement.SINGLE) ) ) ;
		addGlyph( new GlyphNthRepeat(
						new NthRepeatElement(NthRepeatElement.FIRST) ) ) ;
		break ;		
		
	case  NthRepeatElement.SECOND_FULL :
	case  NthRepeatElement.BARLINE_SECOND :
		addGlyph( new GlyphBarLine(
						new BarLineElement(BarLineElement.SINGLE_REP_LEFT) ) ) ;
		addGlyph( new GlyphNthRepeat(
						new NthRepeatElement(NthRepeatElement.SECOND) ) ) ;
		break ;		
	}
}


/**
 * Handle a Begin Slur element.
 */
public void  handleBeginSlur( BeginSlurElement Element ) 
{
	if ( mSlurLevel == 0 )   mCurrentSlur = new GlyphSlur(this) ;
	mSlurLevel++ ;
}


/**
 * Handle an End Slur element.
 */
public void  handleEndSlur( EndSlurElement Element ) 
{
	mSlurLevel-- ;
	if ( mSlurLevel == 0 ) {
		mSlurs.add(mCurrentSlur) ;
		mCurrentSlur = null ;
	}
}


/**
 * Handle a Music Line element.
 */
public void  handleMusicLine( MusicLineElement Element ) 
{
	
//  If ignoring line breaks then return

	if ( sIgnoreBreaks )   return ;
	
//  Indicate if a new line is required and remember the line break character for
//  use at the next stave split.

	mLastLineBreak = Element.getBreak() ;
	
	if ( mLastLineBreak == null  ||  ! mLastLineBreak.equals("\\") )
		mStartNewStave = true ;
}


/**
 * Handle a Words Field element.
 */
public void  handleWordsField( WordsFieldElement Element ) 
{
	if ( ! sShowLyrics )   return ;
	
//  Determine the correct vertical position
	
	mCurrentY += (float) Utils.getStringBounds( "X", WORDS_FONT ).getHeight() ; 
	
	float  Offset = 0.45F * sStaveHeight ;
	
//  Handle all the words in the words field
	
	ArrayList  a = Element.getWordTokens() ;
	
	for ( int i = 0 ; i < a.size() ; i++ ) {
		WordsFieldElement.WordToken  t = (WordsFieldElement.WordToken) a.get(i) ;
	
	//  Add a text glyph for each word based on the x coordinate of the corresponding note
		
		if ( t.mWord != null ) {
			
			GlyphText  g = new GlyphText( t.mWord, WORDS_FONT ) ;
			t.mTextGlyph = g ;
		
			g.setCoords( 0, mCurrentY + Offset ) ;	// NB x coord is added later when it is accurate !
		}
		
		mWordTokens.add(t) ;
	}
}


/**
 * Finalize the stored words.
 * 
 * <p>This method adds the words to the score once the music has been completely built.
 */
public void  finalizeWords()
{
	float  MelismaStartX = -1 ;
	float  MelismaStartY = -1 ;
	
//  Loop through all word tokens
	
	for ( int i = 0 ; i < mWordTokens.size() ; i++ ) {
		WordsFieldElement.WordToken  ThisToken = (WordsFieldElement.WordToken) mWordTokens.get(i) ;
		
		WordsFieldElement.WordToken  NextToken = null ;
		if ( i < mWordTokens.size() - 1 )
			NextToken = (WordsFieldElement.WordToken) mWordTokens.get(i + 1) ;
		
	//  The music is now OK so setup the proper x coordinate

		float  TextWidth = 0 ;
		if ( ThisToken.mWord != null ) {
			TextWidth = (float) Utils.getStringBounds( ThisToken.mWord, WORDS_FONT ).getWidth() ; 

			ThisToken.mTextGlyph.mX = ThisToken.mLinkedGlyph.mX + ( ThisToken.mLinkedGlyph.mLeadingSpace
										+ ThisToken.mLinkedGlyph.mWidth	+ ThisToken.mLinkedGlyph.mTrailingSpace
										- TextWidth ) / 2 ;
		}

	//  Add a text glyph for the word if a word is present
		
		if ( ThisToken.mWord != null )   mGlyphs.add(ThisToken.mTextGlyph) ;
		
	//  Add a syllable separator (-) if one is specified
		
		if ( ThisToken.mControl == '-'  &&  NextToken != null ) {
			
			float  x = ( ThisToken.mLinkedGlyph.mX + ( ThisToken.mLinkedGlyph.mLeadingSpace
								+ ThisToken.mLinkedGlyph.mWidth	+ ThisToken.mLinkedGlyph.mTrailingSpace ) / 2 
					   + NextToken.mLinkedGlyph.mX + ( NextToken.mLinkedGlyph.mLeadingSpace
							    + NextToken.mLinkedGlyph.mWidth + NextToken.mLinkedGlyph.mTrailingSpace ) / 2 )
					 / 2 ;

			TextWidth = (float) Utils.getStringBounds( "-", WORDS_FONT ).getWidth() ; 
			
			GlyphText  g = new GlyphText( "-", WORDS_FONT ) ;
			g.setCoords( x - TextWidth / 2, ThisToken.mTextGlyph.mY ) ;
						
			mGlyphs.add(g) ;
		}
		
	//  Handle melismas
		
		if ( ThisToken.mControl == '_' ) {
			
		//  Store starting position
			
			if ( ThisToken.mWord != null ) {
				MelismaStartX = ThisToken.mTextGlyph.mX + TextWidth ;
				MelismaStartY = ThisToken.mTextGlyph.mY ;
			}
			
		//  If this is the last item of the melisma then draw it and reset
			
			if ( NextToken == null  ||  NextToken.mWord != null  ||  NextToken.mControl != '_' ) {

				float  EndX = ThisToken.mLinkedGlyph.mX + ThisToken.mLinkedGlyph.mLeadingSpace
							+ ThisToken.mLinkedGlyph.mWidth ;
				
				GlyphLine  g = new GlyphLine( MelismaStartX + 1, MelismaStartY, EndX, MelismaStartY,
											  new BasicStroke(GlyphStave.STAVE_LINE_STROKE_WIDTH) ) ;
				mGlyphs.add(g) ;
				
				MelismaStartX = MelismaStartY = -1 ;
			}
		}	
	}
}


/**
 * Add a glyph to the music.
 */
public void  addGlyph( Glyph g )
{
	
//  Check for a forced start of stave (by the end of a music line

//  Check if this component will take us beyond the end of the stave
//  If so, then create a new stave anyway

	if ( mStartNewStave )   startNewStaveManual() ;
		
	if ( mCurrentX + g.mWidth + g.mLeadingSpace + g.mTrailingSpace > sStaveWidth )
		startNewStaveOverflow() ;
	
//  Now add the glyph
		
	add(g) ;
			
	mLastGlyphIndex = mGlyphs.size() - 1 ;
			
	if ( g instanceof GlyphBarLine )
//	  ||  g instanceof GlyphNthRepeat )			// nth repeats at bar lines have
//												// been split into separate glyphs
		mLastBarLineIndex = mLastGlyphIndex ;
}


/**
 * Start a new stave immediately.
 * 
 * <p>This method is called when an ABC line ends with no continuation.
 */
public void  startNewStaveManual()
{
	mStartNewStave = false ;

//	Spread out the glyphs on current stave	

	spreadCurrentStave( mFirstGlyphIndex, mLastGlyphIndex, false ) ;
		
//	Determine the new x and y coordinates for the new stave

	mCurrentX = 0 ;
	mCurrentY += sStaveHeight ;

//	Insert a new stave at the required point and add the key sig

	mCurrentStave = new GlyphStave(sStaveWidth) ;

	addNoAdvance(mCurrentStave) ;

	mStaveCount++ ;

	if ( sShowClef )   add( new GlyphTrebleClef() ) ;

	add( new GlyphKeySignature(mCurrentKey) ) ;
	
	if ( sShowClef )
		mFirstGlyphIndex = mLastGlyphIndex + 4 ;  // Added three, point to next
	else
		mFirstGlyphIndex = mLastGlyphIndex + 3 ;  // Added two, point to next
}


/**
 * Start a new stave on overflow.
 */
public void  startNewStaveOverflow()
{
	
//  First of all, determine where to start the new stave.   We should split at
//  a bar-line if possible and move the following glyphs to the new stave.

	int  SplitIndex ;
	if ( mLastBarLineIndex == mFirstGlyphIndex )
		SplitIndex = mLastGlyphIndex + 1 ;
	else
		SplitIndex = mLastBarLineIndex + 1 ;

//	Spread out the glyphs on current stave (FirstGlyphIndex<=x<SplitIndex)	

	mLastLineBreak = null ;		// Limited expansion
	spreadCurrentStave( mFirstGlyphIndex, SplitIndex - 1, false ) ;
		
//  Determine the new x and y coordinates for the new stave

	mCurrentX = 0 ;
	mCurrentY += sStaveHeight ;

//  Insert a new stave at the required point and add the key sig

	mCurrentStave = new GlyphStave(sStaveWidth) ;

	insertNoAdvance( mCurrentStave, SplitIndex ) ;

	mStaveCount++ ;
	
	if ( sShowClef ) {
		insert( new GlyphTrebleClef(), SplitIndex + 1 ) ;
		insert( new GlyphKeySignature(mCurrentKey), SplitIndex + 2 ) ;
	}
	else
		insert( new GlyphKeySignature(mCurrentKey), SplitIndex + 1 ) ;

//  Adjust the coordinates of the glyphs on the new stave so they appear in the
//  correct place on the new stave.  Move left and down.
//  We should also set the new stave number

	float  Offset = 0 ;

	if ( sShowClef )
		mFirstGlyphIndex = SplitIndex + 3 ;
	else
		mFirstGlyphIndex = SplitIndex + 2 ;

	for ( int i = mFirstGlyphIndex ; i < mGlyphs.size() ; i++ ) {
		Glyph  e = (Glyph) mGlyphs.get(i) ;
		
		e.setStaveNumber(mStaveCount) ;
		
		if ( Offset == 0 )   Offset = e.mX - mCurrentX ;
			
		e.mX -= Offset ;
		e.mY += sStaveHeight ;
		
		mCurrentX += e.mWidth + e.mLeadingSpace + e.mTrailingSpace ;
	}
}


/**
 * Spread out the glyphs on the current stave.
 *
 * <p>Lines can be indefinitely spread across the stave expect for the
 * very last line which may look silly and could not be corrected !
 * The LimitedSpan flag will cause length limiting to be invoked.  
 */
public void  spreadCurrentStave( int StartIndex, int EndIndex, boolean LimitedSpan )
{
	int  Index = StartIndex ;

//  Ignore any leading clef, key sig and time sig

	Glyph  g = (Glyph) mGlyphs.get(Index) ;
	if ( g instanceof GlyphTrebleClef )   Index++ ;
	
	if ( Index > EndIndex )   return ;
	
	g = (Glyph) mGlyphs.get(Index) ;
	if ( g instanceof GlyphKeySignature )   Index++ ;

	if ( Index > EndIndex )   return ;

	g = (Glyph) mGlyphs.get(Index) ;
	if ( g instanceof GlyphTimeSignature )   Index++ ;
	
	if ( Index > EndIndex )   return ;

	StartIndex = Index ;
	
//  Determine total length which requires scaling
//  Note that if the last element is a barline then remove the right padding

	Glyph  First = (Glyph) mGlyphs.get(StartIndex) ;
	Glyph  Last  = (Glyph) mGlyphs.get(EndIndex) ;

	boolean  LastIsBarLine = ( Last instanceof GlyphBarLine
 						   ||  Last instanceof GlyphNthRepeat ) ;

	if ( LastIsBarLine )   Last.mTrailingSpace = 0 ;
 		  
	float  StartX = First.mX ;

	float  FromLength =  Last.mX + Last.mWidth + Last.mLeadingSpace
					  + Last.mTrailingSpace - StartX ;
					  
//  Determine if we are allowed to expand to the end of the line or not.
//  There is a maximum expansion allowed unless it is overridden.   This is
//  done by honouring ABC line breaks and using the trailing '*' break character
//  to force a right-adjust of the last item.

	if ( mLastLineBreak != null  &&  mLastLineBreak.equals("*") )
		LimitedSpan = false ;

	mLastLineBreak = null ;
	
//  Determine pad to length and hence the scale factor
//  There is a maximum limit here so.  IF it is applied then we should shorten
//  the relevant stave accordingly

	float  ToLength    = sStaveWidth - First.mX ;
	float  ScaleFactor = ToLength / FromLength ;

	if ( LimitedSpan  &&  ScaleFactor > MAX_STRETCH ) {
	   ScaleFactor = MAX_STRETCH ;
	   mCurrentStave.setStaveWidth( StartX + FromLength * ScaleFactor ) ;
	}

//  Now adjust x-coordinate and left padding of all components for the
//  desired effect

	for ( int i = StartIndex ; i <= EndIndex ; i++ ) {
		g = (Glyph) mGlyphs.get(i) ;
		
	//  Ignore beamers as they will calculate automatically
	
		if ( g instanceof GlyphBeamer )   continue ;
		
		float  OldX = g.mX - StartX ;
		
	//  OldWidth = OldLeftPad + GlyphWidth + OldRightPad
	//  NewWidth = OldWidth * ScaleFactor
	//
	//  BUT GlyphWidth does not change so we must adjust the left and right pads by
	//  a different scale factor (PadScale) which can be worked out as follows :- 
	//
	//  NewLeftPad  = PadScale * OldLeftPad
	//  NewRightPad = PadScale * OldRightPad
	//  NewWidth = NewLeftPad + GlyphWidth + NewRightPad
	//           = PadScale * OldLeftPad + GlyphWidth + PadScale * OldRightPad
	//  OldWidth * ScaleFactor =    ... above ...
	//  OldWidth * ScaleFactor - GlyphWidth = PadScale * ( OldLeftPad + OldRightPad )
	//
	//  PadScale = ( OldWidth * ScaleFactor - GlyphWidth )
	//			   ---------------------------------------
	//			        ( OldLeftPad + OldRightPad ) 
	
		float  OldWidth = g.mLeadingSpace + g.mWidth + g.mTrailingSpace ;
		float  PadScale = ( OldWidth * ScaleFactor - g.mWidth ) /
						  ( g.mLeadingSpace + g.mTrailingSpace ) ;
		
	//  Now set the new padding accordingly
	
		if ( g.mLeadingSpace > 0 )    g.mLeadingSpace  *= PadScale ;	
		if ( g.mTrailingSpace > 0 )   g.mTrailingSpace *= PadScale ;

	//  Finally determine the new x coordinate

		g.mX = StartX + OldX * ScaleFactor ;

	//  Fudge the last glyph by adding a bit so the barline lines up !!

		if ( i == EndIndex )   g.mX += 0.5F ;
	}
}


/**
 * Check the current element for start/continue beaming requirements.
 *
 * <p> This check should be done before the glyph is created.
 * 
 * <p>The supplied element will be a NoteElement or MultiNoteEelement.
 * 
 * <p>This method will determine if a new beamer is required.
 */
public  void  checkStartBeamer( ABCElement Element )
{
	NoteElement  e = (NoteElement) Element ;
	
//  If a beamer is already in progress then this note has already been checked
//  for valid beaming.   Therefore leave the current beamer alone and return so
//  the next glyph will be added to this beamer.

	if (  mCurrentBeamer != null )   return  ;
	
//  Beaming is not in progress.
//  If this note is not beamed or it is too long for
//  beaming even if it has been specified (length >= 1/4) the we should just
//  return without starting a new beamer (the current beamer is already null).

	if ( ! e.isBeam() )   return ;

	Fraction  AbsLength = e.getNote().getAbsoluteLength() ;
		
	if ( AbsLength == null  ||  AbsLength.compareTo ( 1, 4 ) >= 0 )
		return ;
 
//  Current note is beamed to the next and is valid for beaming (< 1/4).
//  We must check by looking ahead to see if there is anything to beam to !
//  Lookahead and get next significant beaming item (note, multi-note,
//  tuplet, bar-line, nth repeat, line break ).
//  If it is not then we should not beam the current note and just return without
//  starting a new beamer (the current beamer is already null).

	if ( ! checkValidBeaming(e) )   return ;

//  This note is valid for beaming and there is valid note for it to beam too.
//  No beamer is in progress so we must start a new one

	mCurrentBeamer = new GlyphBeamer(mForceDirection) ;
}


/**
 * Check the current element for stop beaming requirements.
 *
 * <p> This check should be done after the glyph is created.
 * 
 * <p>The supplied element will be a NoteElement or MultiNoteElement.
 * 
 * <p>This method will determine if the current beamer needs closing.
 */
public  void  checkStopBeamer( ABCElement Element )
{
	NoteElement  e = (NoteElement) Element ;

//  If no beamer is currently in progress then return as there is nothing to close

	if ( mCurrentBeamer == null )   return ;	

//	Beaming in progress (and it contains at least one note).
//  If this note is not beamed or it is too long for beaming even if it has
//  been specified (length >= 1/4) the we should close the current beamer,
//  add it to the glyph array and then reset the current beamer to null.

	if ( ! e.isBeam()
	 ||  e.getNote().getAbsoluteLength().compareTo ( 1, 4 ) >= 0 ) {
		mBeamers.add(mCurrentBeamer) ;		
		mCurrentBeamer = null ;
		return ;
	 }

//	Current note is beamed to the next and is valid for beaming (< 1/4).
//	We must check by looking ahead to see if there is anything to beam to !
//	Lookahead and get next significant beaming item (note, multi-note,
//	tuplet, bar-line, nth repeat, line break ).
//	If it is not then we should not beam the current note and close the 
//  current beamer as ablove.

	if ( ! checkValidBeaming(e) ) {
		mBeamers.add(mCurrentBeamer) ;		
		mCurrentBeamer = null ;
		return ;
	}
}


/**
 * Check for valid beaming.
 * 
 * <p>The provided element is valid for beaming but we must check if there
 * is anything for it to beam too !
 * 
 * <p>This is done by looking ahead until we find a valid beam to element or
 * something which indicates we must stop beaming (e.g. Barline).
 */
public boolean  checkValidBeaming( ABCElement Element )
{
	Element = Element.getNextElement() ;
	
//  Loop through all glyphs from the current until we reach the end or determine
//  valid termination condition.
//  Doing it this way allows us to skip through

	while ( Element != null ) {
		int  Type = Element.getType() ;
		
		switch ( Type ) {
		case  REST_ELEMENT :	//  intermediate rest does not affect beaming
			break ;				//  keep looking onwards
		
		case  NOTE_ELEMENT :	// Terminate beaming if too long (>=1/4)
			NoteElement  e = (NoteElement) Element ;
			if ( e.getNote().getAbsoluteLength().compareTo ( 1, 4 ) >= 0 )
				return  false ;
			return  true ;		//  Valid beaming target
		
		case  MULTINOTE_ELEMENT :	// Terminate beaming if too long (>=1/4)
			MultiNoteElement  m = (MultiNoteElement) Element ;
			if ( m.getNote().getAbsoluteLength() == null
			 ||  m.getNote().getAbsoluteLength().compareTo ( 1, 4 ) >= 0 )
				return  false ;
			return  true ;		//  Valid beaming target
		
		default :				//  Anything else terminates beaming  
			return  false ;
		}
		
		Element = Element.getNextElement() ;
	}
	
	return  false ;		// Nothing located to beam to
}

/**
 * Add a glyph at the current coordinates.
 */
public void  add( Glyph g )
{
	g.setCoords( mCurrentX, mCurrentY + g.mY ) ;
	g.setStaveNumber(mStaveCount) ;
	mGlyphs.add(g) ;
	mCurrentX += g.mWidth + g.mLeadingSpace + g.mTrailingSpace ;
}


/**
 * Add a glyph at the current coordinates with no cursor advance.
 */
public void  addNoAdvance( Glyph g )
{
	g.setCoords( mCurrentX, mCurrentY ) ;
	g.setStaveNumber(mStaveCount) ;
	mGlyphs.add(g) ;
}


/**
 * Insert a glyph at the current coordinates.
 */
public void  insert( Glyph g, int Index )
{
	g.setCoords( mCurrentX, mCurrentY + g.mY ) ;
	g.setStaveNumber(mStaveCount) ;
	mGlyphs.add( Index, g ) ;
	mCurrentX += g.mWidth + g.mLeadingSpace + g.mTrailingSpace ;
}


/**
 * Insert a glyph at the current coordinates with no cursor advance.
 */
public void  insertNoAdvance( Glyph g, int Index )
{
	g.setCoords( mCurrentX, mCurrentY ) ;
	g.setStaveNumber(mStaveCount) ;
	mGlyphs.add( Index, g ) ;
}

}
