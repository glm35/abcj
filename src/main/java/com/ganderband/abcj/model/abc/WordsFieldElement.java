/**
 * A class representing a Words Field Element.
 * 
 * <p>This is the "w:" field and not the "W:" field.
 */
package  com.ganderband.abcj.model.abc ;

import  java.util.* ;
import  com.ganderband.abcj.model.music.* ;

public class WordsFieldElement extends FieldElement
{
/**
 * The words.
 */
	private String  mWords ;
/**
 * The associated line of music.
 * 
 * <p>This enalbles the words and notes to be connected up at this time.
 */
	private MusicLineElement  mMusicLine ;
/**
 * The array of word tokens for this element.
 */
	private ArrayList  mTokens = new ArrayList() ;
/**
 * The current note selected in the music line.
 * 
 * <p>This is usually  a note, reset or multi-note (chord).   If may, however,
 * be a tuplet element and, in this case, the mTupletIndex field is used to
 * identify the relevant note in the tuplet.
 */
	private ABCElement  mCurrentNote ;
/**
 * The tuplet index as documented against the mCurrentNote field.
 */
	private int  mTupletIndex ;
/**
 * A public inner class to define information for each token in the words string.
 */
	public static class WordToken {
	/**
	 * The word for this note or null.
	 */
		public String  mWord ;
	/**
	 * A Control character (-,_,~,*,|).
	 * 
	 * <p>Note that _,* and | apply to notes whereas - and ~ do not.
	 * - will be attached to the previous word.
	 * ~ will not appear as it causes more than one word to be associated with a note.
	 * _ may also be attached to a word to indicate that more underscores follow.
	 */
		public char  mControl ;
	/**
	 * The linked note element.
	 */
		public ABCElement  mLinkedElement ;
	/**
	 * The linked index for a tuplet element.
	 */
		public int  mLinkedTupletIndex ;
	/**
	 * The link to the glyph used to draw the corresponding note.
	 * 
	 * <p>This is added during music building.
	 * 
	 * <p>This isn't quite as pure as it should be as the ABC model really ought not
	 * to know about the music model !
	 */
		public Glyph  mLinkedGlyph ;
	/**
	 * The text glyph for the word itself.
	 * 
	 * <p>This is added during music building.
	 * 
	 * <p>This isn't quite as pure as it should be as the ABC model really ought not
	 * to know about the music model !
	 */
		public GlyphText  mTextGlyph ;
	/**
	 * The main constructor.
	 */
	public  WordToken( String Word, char Control )
	{	mWord = Word ;   mControl = Control ;   mLinkedElement = null ;   }
	}

	
/**
 * Construct from a string.
 */
public  WordsFieldElement( String Words, MusicLineElement MusicLine )
{
	super('P') ;
	setType(WORDS_FIELD_ELEMENT) ;
	mWords = Words ;
	mMusicLine = MusicLine ;
	
//  Parse the string into tokens
	
	parse() ;
	
//  And now connect each word token to its relevant note.
//  This will enable the coordinates to be easily accessed later and also allow
//  the note to be made wider if the words are too long !
	 
	connectToNotes() ;
}


/**
 * Get the array of word tokens.
 */
public ArrayList  getWordTokens()
{	return  mTokens ;   }


/**
 * Represent object as a string.
 */
public String  toString()
{	return  "WordsFieldElement >" + mWords + "<" ;   }


/**
 * Format the field item as ABC text (but no comments or type + colon).
 */
public String  formatField()
{	return  mWords ;   }


/**
 * Parse the element into an array to identify each syllable or other note related element.
 * 
 * <p>Note that ther are no leading or trailing blanks by this time.
 */
private void  parse()
{
	
//	The control characters are as follows :-
//
//	~ (tilde) - This is turned into a space, but doesn't count as a "word". 
//		It lets you put two words on a single note. In this example, it also separates the 1. and 2.
//		from the first word of the text.   The abc2ps formatter has a special kludge to spot this and
//		outdent the number, so the second "word" lines up with the note. 
//	- (hyphen) -  This is used as a syllable separator, and each syllable is lined up with one note. 
//	* (asterisk) - This means that the syllable takes up an extra note. Use as many as you need to cover
//		all the the notes. You can also use * at the start of the line, to skip over notes. 
//	_ (underscore) - This is like *, but draws a long horizontal after the syllable. Some music publishers
//		like to show long words this way; others don't. It is appropriate in this example, for the long
//		melismas on the word "Ah!" 
//	| (bar line) - If you use it, each | lines up with one bar line in the music. This can be used
//		if you want the words to start several bars in, and don't want to bother counting notes. 
	
	String  Syllable = "" ;
	char    LastControl = ' ' ;

//  Process each character separately
	
	for ( int i = 0 ; i < mWords.length() ; i++ ) {
		char  c = mWords.charAt(i) ;
		
//  Check the character and process accordingly
		
	switch ( c ) {
		
	//  Handle Word break to new note
		
		case  ' ' :
			if ( " -~".indexOf(LastControl) >= 0 )   break ;
			
		//  Store any saved syllable
			
			if ( Syllable.length() > 0 ) {
				addToken( Syllable, c ) ;
				Syllable = "" ;
			}
			
			break ;
			
	//  Handle Word break staying on same note
			
		case  '~' :
			if ( " -~".indexOf(LastControl) >= 0 )   break ;
			
		//  Add a space to the current word and carry on (no new note)
			
			Syllable += " " ;
			break ;
			
	//  Handle syllable break to new note
			
		case  '-' :
			if ( " -~".indexOf(LastControl) >= 0 )   break ;
			
		//  Store any saved syllable
				
			if ( Syllable.length() > 0 ) {
				addToken( Syllable, c ) ;
				Syllable = "" ;
			}
				
			break ;
			
	//  Handle note with no word
			
		case  '*' :
			
		//  Store any saved syllable
					
				if ( Syllable.length() > 0 ) {
					addToken( Syllable, ' ' ) ;
					Syllable = "" ;
				}

			addToken( null, c ) ;
				
			break ;
			
	//  Handle melismas (long underscores)
			
		case  '_' :
			
		//  Store any saved syllable
			
			if ( Syllable.length() > 0 ) {
				addToken( Syllable, c ) ;
				Syllable = "" ;
			}

			addToken( null, c ) ;
			
			break ;
			
	//  Handle align to bar
			
		case  '|' :
			
		//  Store any saved syllable
				
			if ( Syllable.length() > 0 ) {
				addToken( Syllable, ' ' ) ;
				Syllable = "" ;
			}

			addToken( null, c ) ;
			
			break ;
			
		//  Treat all other characters as text
			
		default :
			Syllable += c ;
		break ;
		}
		
		LastControl = c ;
	}
	
//  Store any outstanding word/syllable at the end of the line

	if ( Syllable.length() > 0 )   addToken( Syllable, ' ' ) ;
}


/**
 * Create and add a new token to the tokens array.
 */
public void  addToken( String Word, char Control )
{	mTokens.add( new WordToken( Word, Control ) ) ;   }



/**
 * Connect each word token to its corresponding note/chord element.
 * 
 * <p>This will enable the coordinates to be easily accessed later and also allow
 * the note to be made wider if the words are too long !
 */
public void  connectToNotes()
{

//System.out.println(mMusicLine.format()) ;
//System.out.println(mWords);
	
//  Locate the first useable note of the music line
	
	mCurrentNote = (ABCElement) mMusicLine.getElements().get(0) ;
	locateNextNote() ;

//System.out.println(mCurrentNote+" "+mTupletIndex);

//  Process each token in the token list
	
	for ( int i = 0 ; i < mTokens.size() ; i++ ) {
		WordToken  t = (WordToken) mTokens.get(i) ;
		
	//  Handle an '*' token - skip to next note
		
		if ( t.mControl == '*' ) {
			locateNextNote() ;
		}
		
	//  Handle an '|' token - skip to first note in next bar
		
		else if ( t.mControl == '|' ) {
			locateNextBarLine() ;
			locateNextNote() ;
		}

	//  Handle melismas (long underscores)
		
		else if ( t.mControl == '_' ) {
			
			//  ************* this may or may nor have a note associated with it
			linkToCurrentNote(t) ;
			locateNextNote() ;
		}
		
	//  It's a normal word so just build the appropriate links

		else {
			linkToCurrentNote(t) ;
			locateNextNote() ;
		}
	}
}


/**
 * Locate next note element from a given starting point.
 */
public void  locateNextNote()
{
	if ( mCurrentNote == null )   return ;
	
//  Do an initial check to see if we are in a tuplet.
//  If so, then return the next note in the tuplet or carry in if there are no more.
	
	if ( mCurrentNote.getType() == TUPLET_ELEMENT ) {
		if ( mTupletIndex < ( (TupletElement) mCurrentNote ).getNoteElements().size() - 1 ) {
			mTupletIndex++ ;
			return ;
		}
	}
	
//  Search further along for the next note
	
	while ( true ) {
		mCurrentNote = mCurrentNote.mNextElement ;
		if ( mCurrentNote == null )   return ;
		
		int  Type = mCurrentNote.getType() ;
		
	//  If note, rest or multi-note (chord) then return
		
		if ( Type == NOTE_ELEMENT  ||  Type == REST_ELEMENT  ||  Type == MULTINOTE_ELEMENT )   return ;
		
	//  If a tuplet then set the tuplet index to the first note of the tuplet and return
		
		if ( Type == TUPLET_ELEMENT ) {
			mTupletIndex = 0 ;
			return ;
		}
	}
}


/**
 * Locate next note bar line from a given starting point.
 */
public void  locateNextBarLine()
{
	if ( mCurrentNote == null )   return ;
	
//  Search further along for the next bar line
	
	while ( true ) {
		mCurrentNote = mCurrentNote.getNextElement() ;
		if ( mCurrentNote == null )   return ;
		
		if ( mCurrentNote.getType() == BARLINE_ELEMENT )   return ;
	}
}


/**
 * Link a word token to the current note.
 */
public void  linkToCurrentNote( WordToken t )
{

//  First of all, store the current note against the word token
	
	t.mLinkedElement     = mCurrentNote ;
	t.mLinkedTupletIndex = mTupletIndex ;

//  Locate the real note element (allowing for tuplets)

	NoteElement  e ;
	
	if ( mCurrentNote.getType() != TUPLET_ELEMENT )
		e = (NoteElement) mCurrentNote ;
	else {
		ArrayList a = ( (TupletElement) mCurrentNote ).getNoteElements() ;
		e = (NoteElement) a.get(mTupletIndex) ;
	}
	
//  Now do the reverse and tell the cureent note about the word token associated with it.
	
	e.linkWord(t) ;
}

}
