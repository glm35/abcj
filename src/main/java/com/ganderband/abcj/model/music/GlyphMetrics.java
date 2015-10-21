/**
 * Common metrics used by musical drawing.
 */
package com.ganderband.abcj.model.music;

public interface GlyphMetrics
{
/**
 * The distance between each stave line vertically.
 */
	public static float  STAVE_LINE_SPACING = 6.0F ;
/**
 * A constant to ease calculation.
 */
	public static final float  SL2 = STAVE_LINE_SPACING / 2 ;
/**
 * A constant to ease calculation.
 */
	public static final float  SL23 = 2 * STAVE_LINE_SPACING / 3 ;
}
