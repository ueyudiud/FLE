/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

/**
 * The no client only chat formatting.
 * @author ueyudiud
 */
public enum EnumChatFormatting
{
	BLACK('0'),
	DARK_BLUE('1'),
	DARK_GREEN('2'),
	DARK_AQUA('3'),
	DARK_RED('4'),
	DARK_PURPLE('5'),
	GOLD('6'),
	GRAY('7'),
	DARK_GRAY('8'),
	BLUE('9'),
	GREEN('a'),
	AQUA('b'),
	RED('c'),
	LIGHT_PURPLE('d'),
	YELLOW('e'),
	WHITE('f'),
	OBFUSCATED('k', true),
	BOLD('l', true),
	STRIKETHROUGH('m', true),
	UNDERLINE('n', true),
	ITALIC('o', true),
	RESET('r');
	
	private final char controlCode;
	private final boolean isNotColor;
	private final String opcodeName;
	
	EnumChatFormatting(char controlCode)
	{
		this(controlCode, false);
	}
	
	EnumChatFormatting(char controlCode, boolean isNotColor)
	{
		this.controlCode = controlCode;
		this.isNotColor = isNotColor;
		this.opcodeName = "\u00a7" + controlCode;
	}
	
	public char getControlCode()
	{
		return this.controlCode;
	}
	
	public boolean isNotColor()
	{
		return this.isNotColor;
	}
	
	/**
	 * Checks if typo is a color.
	 */
	public boolean isColor()
	{
		return !this.isNotColor && this != RESET;
	}
	
	@Override
	public String toString()
	{
		return this.opcodeName;
	}
}
