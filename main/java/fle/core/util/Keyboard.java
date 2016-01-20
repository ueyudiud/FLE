package fle.core.util;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import farcore.util.IKey;
import farcore.util.IKeyBoard;

public class Keyboard implements IKeyBoard
{
	public static enum Key implements IKey
	{
		Forward('F'),
		Place('P'),
		Jump('J'),
		Sneak('S'),
		Tech('T');
		
		char chr;
		int flag;
		
		Key(char c)
		{
			chr = c;
			flag = 1 << ordinal();
		}
		
		public static int toInt(Iterable keySet)
		{
			int ret = 0;
			for (Iterator i$ = keySet.iterator(); i$.hasNext();)
			{
				Key key = (Key)i$.next();
				ret |= 1 << key.ordinal();
			}

			return ret;
		}

		public static Set fromInt(long keyState)
		{
			Set ret = EnumSet.noneOf(Key.class);
			int i = 0;
			for (; keyState != 0; keyState >>= 1)
			{
				if ((keyState & 1) != 0)
					ret.add(values()[i]);
				i++;
			}

			return ret;
		}

		@Override
		public char getControlChar()
		{
			return chr;
		}
	}
	
	private final Map<EntityPlayer, Set<IKey>> playerKeys = new WeakHashMap();

	public Keyboard()
	{
	}

	public boolean isPlaceKeyDown(EntityPlayer player)
	{
		return isKeyDown(player, Key.Place);
	}

	public boolean isForwardKeyDown(EntityPlayer player)
	{
		return isKeyDown(player, Key.Forward);
	}

	public boolean isJumpKeyDown(EntityPlayer player)
	{
		return isKeyDown(player, Key.Jump);
	}
	
	public boolean isTechKeyDown(EntityPlayer player)
	{
		return isKeyDown(player, Key.Tech);
	}

	public boolean isSneakKeyDown(EntityPlayer player)
	{
		return player.isSneaking();
	}

	public void sendKeyUpdate()
	{
	}

	public void processKeyUpdate(EntityPlayer player, long keyState)
	{
		playerKeys.put(player, Key.fromInt(keyState));
	}

	public void removePlayerReferences(EntityPlayer player)
	{
		playerKeys.remove(player);
	}

	@Override
	public boolean isKeyDown(EntityPlayer player, IKey key)
	{
		Set keys = (Set) playerKeys.get(player);
		return keys == null ? false : keys.contains(key);
	}
}