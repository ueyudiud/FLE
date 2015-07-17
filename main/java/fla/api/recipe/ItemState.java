package fla.api.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import fla.api.util.FlaValue;

public enum ItemState
{
	Default(' ', 0, 0)
	{
		@Override
		public ItemState getStateOn(ItemState state) 
		{
			return state == Crush ? Hit : super.getStateOn(state);
		}
	},
	Select(' ', 1, 0, false),
	Hit('h', 0, 1)
	{
		@Override
		public ItemState getStateOn(ItemState state) 
		{
			return state == Crush ? Crush : super.getStateOn(state);
		}
	},
	Crush('c', 1, 1),
	Polish('p', 0, 2),
	Smooth('s', 1, 2);
	
	public static ResourceLocation locate = new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/state.png");
	
	final char c;
	int u, v;

	ItemState(char id, int x, int y, boolean register)
	{
		c = id;
		u = x * 16;
		v = y * 16;
		if(register) StateManager.register(id, this);
	}
	ItemState(char id, int x, int y)
	{
		c = id;
		u = x * 16;
		v = y * 16;
		StateManager.register(id, this);
	}
	
	public final char getIndex()
	{
		return c;
	}
	
	public ItemState getStateOn(ItemState state)
	{
		if(this == Default) return state;
		return this;
	}
	
	public static class StateManager
	{
		private static Map<Character, ItemState> stateMap = new HashMap();
		private static Map<IItemChecker, ItemState> toolCraftingMap = new HashMap();
		
		public static void register(IItemChecker checker, ItemState state)
		{
			toolCraftingMap.put(checker, state);
		}
		private static void register(char c, ItemState state)
		{
			stateMap.put(new Character(c), state);
		}
		
		public static boolean isItemEffective(ItemStack i)
		{
			for(IItemChecker checker : toolCraftingMap.keySet())
			{
				if(checker.match(i))
				{
					return true;
				}
			}
			return false;
		}
		
		public static char getCraftedChar(char ic, ItemStack ii)
		{
			for(IItemChecker checker : toolCraftingMap.keySet())
			{
				if(checker.match(ii))
				{
					ItemState state = toolCraftingMap.get(checker);
					ItemState state1 = stateMap.get(ic);
					return state1.getStateOn(state).getIndex();
				}
			}
			return ic;
		}

		public static ItemState[] getStates(String c)
		{
			return getStates(c.toCharArray());
		}
		
		public static ItemState[] getStates(char...c)
		{
			ItemState[] ret = new ItemState[c.length];
			for(int i = 0; i < ret.length; ++i)
			{
				ret[i] = getState(c[i]);
			}
			return ret;
		}
		
		public static ItemState getState(char c)
		{
			ItemState state = stateMap.get(c);
			return state != null ? state : Default;
		}
	}

	public int getU() 
	{
		return u;
	}

	public int getV() 
	{
		return v;
	}
}