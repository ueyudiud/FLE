package flapi.recipe;

import java.util.HashMap;
import java.util.Map;

import flapi.gui.GuiCondition;

public class CraftingState extends GuiCondition
{
	private static final Map<Integer, CraftingState> colorMap = new HashMap();
	private static final Map<Character, CraftingState> map = new HashMap();
	public static CraftingState DEFAULT;
	public static CraftingState CRUSH;
	public static CraftingState POLISH;
	
	private final char c;
	public final int color;

	public static CraftingState getState(int colorIndex)
	{
		return colorMap.get(colorIndex) == null ? DEFAULT : colorMap.get(colorIndex);
	}
	public static CraftingState[] getStates(String aString)
	{
		return getStates(aString.toCharArray());
	}
	
	public static CraftingState[] getStates(char...aChars)
	{
		CraftingState[] ret = new CraftingState[aChars.length];
		for(int i = 0; i < ret.length; ++i)
		{
			ret[i] = getState(aChars[i]);
		}
		return ret;
	}
	
	public static CraftingState getState(char aChar)
	{
		return map.get(aChar) != null ? map.get(aChar) : DEFAULT;
	}
	
	public CraftingState(char aChar, String aName, int colorIndex) 
	{
		super(aName);
		if(map.containsKey(aChar)) throw new RuntimeException();
		c = aChar;
		map.put(new Character(aChar), this);
		colorMap.put(colorIndex, this);
		color = colorIndex;
	}
	
	public CraftingState onStateAdd(CraftingState aState)
	{
		return aState;
	}
	
	@Override
	public CraftingState setTextureName(String...aTextureName)
	{
		super.setTextureName(aTextureName);
		return this;
	}
	
	public char getCharIndex()
	{
		return c;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof CraftingState)) return false;
		CraftingState state = (CraftingState) obj;
		return state.c == c;
	}
}