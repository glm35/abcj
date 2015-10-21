/**
 * The constants used by ABCParser.
 */
package  com.ganderband.abcj.model.abc ;

public interface ABCParserConstants
{
/**
 * Comment element identifier.
 */
	public static final int  COMMENT_ELEMENT = 1 ;
/**
 * Tex Command element identifier.
 */
	public static final int  TEX_COMMAND_ELEMENT = 2 ;
/**
 * Field element identifier.
 * 
 * <p>Used for general fields. Some will have specific ids (e.g. tempo).
 */
	public static final int  FIELD_ELEMENT = 3 ;
/**
 * Index field element identifier.
 */
	public static final int  INDEX_FIELD_ELEMENT = 4 ;
/**
 * Default Length field element identifier.
 */
	public static final int  DEFAULT_LENGTH_FIELD_ELEMENT = 5 ;
/**
 * Meter field element identifier.
 */
	public static final int  METER_FIELD_ELEMENT = 6 ;
/**
 * Part field element identifier.
 */
	public static final int  PART_FIELD_ELEMENT = 7 ;
/**
 * Parts field element identifier.
 */
	public static final int  PARTS_FIELD_ELEMENT = 8 ;
/**
 * Tempo field element identifier.
 */
	public static final int  TEMPO_FIELD_ELEMENT = 9 ;
/**
 * Key field element identifier.
 */
	public static final int  KEY_FIELD_ELEMENT = 10 ;
/**
 * Music line element identifier.
 */
	public static final int  MUSIC_LINE_ELEMENT = 11 ;
/**
 * Bar Line element identifier.
 */
	public static final int  BARLINE_ELEMENT = 12 ;
/**
 * Nth Repeat element identifier.
 */
	public static final int  NTHREPEAT_ELEMENT = 13 ;
/**
 * Begin Slur element identifier.
 */
	public static final int  BEGINSLUR_ELEMENT = 14 ;
/**
 * End Slur element identifier.
 */
	public static final int  ENDSLUR_ELEMENT = 15 ;
/**
 * Rest element identifier.
 */
	public static final int  REST_ELEMENT = 16 ;
/**
 * Note element identifier.
 */
	public static final int  NOTE_ELEMENT = 17 ;
/**
 * Tuplet element identifier.
 */
	public static final int  TUPLET_ELEMENT = 18 ;
/**
 * Multinote element identifier.
 */
	public static final int  MULTINOTE_ELEMENT = 19 ;
/**
 * Text field element identifier.
 */
	public static final int  TEXT_FIELD_ELEMENT = 20 ;
/**
 * Words field element identifier.
 */
	public static final int  WORDS_FIELD_ELEMENT = 21 ;
/**
 * Valid standard header fields.
 */
	public static final String  VALID_HEADER_FIELDS = "XTABCDEFGHIKLMNOPQRSWZ" ;
/**
 * Valid standard music fields.
 */
	public static final String  VALID_MUSIC_FIELDS = "EKLMPQTw" ;
/**
 * Valid gracings.
 */
	public static final String  VALID_GRACINGS = "~.vu" ;
	
/**
 * The indenting amount to use when debugging.
 */
	public static final String  DEBUG_INDENT = "    " ;
}
