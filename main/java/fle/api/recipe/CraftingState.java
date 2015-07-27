package fle.api.recipe;

import java.util.HashMap;
import java.util.Map;

import fle.api.gui.GuiCondition;

public class CraftingState extends GuiCondition
{
	private static Map<Character, CraftingState> map = new HashMap();
	
	private final char c;

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
		CraftingState state = map.get(aChar);
		return state != null ? state : null;
	}
	
	public CraftingState(char aChar, String aName) 
	{
		super(aName);
		if(map.containsKey(new Character(aChar))) throw new RuntimeException();
		c = aChar;
		map.put(new Character(aChar), this);
	}
	
	public CraftingState onStateAdd(CraftingState aState)
	{
		return aState;
	}
}