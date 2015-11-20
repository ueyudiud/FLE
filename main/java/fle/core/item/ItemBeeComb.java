package fle.core.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import fle.api.item.IBeeComb;
import fle.api.item.IItemBehaviour;
import fle.api.item.ItemFleMetaBase;
import fle.api.util.FleLog;
import fle.api.world.BlockPos;

public class ItemBeeComb extends ItemFleMetaBase implements IBeeComb
{
	private static Map<Integer, BeeCombInfo> map = new HashMap();
	private static final BeeCombInfo defaultInfo = new BeeCombInfo(0, 0);
	
	public ItemBeeComb(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public BeeCombInfo info(ItemStack aStack)
	{
		return map.containsKey(getDamage(aStack)) ? map.get(getDamage(aStack)) : defaultInfo;
	}

	@Override
	public int getMaxBeeCap(ItemStack aStack)
	{
		return info(aStack).a;
	}

	@Override
	public int getMaxLarvaCap(ItemStack stack)
	{
		return info(stack).b;
	}

	@Override
	public int getLarvaCount(ItemStack aStack)
	{
		return setupNBT(aStack).getInteger("Larva");
	}

	@Override
	public int getQueenCount(ItemStack aStack)
	{
		return info(aStack).c;
	}
	
	@Override
	public int getHoneyAmount(ItemStack aStack)
	{
		return setupNBT(aStack).getInteger("Honey");
	}

	@Override
	public void saveToNBT(ItemStack aStack, int age, int honey)
	{
		NBTTagCompound nbt = setupNBT(aStack);
		nbt.setInteger("Age", age);
		nbt.setInteger("Honey", honey);
	}

	@Override
	public int getMaturationTick(ItemStack aStack)
	{
		return info(aStack).d;
	}

	@Override
	public ItemStack onMaturation(ItemStack aStack, int larva, int honey)
	{
		setDamage(aStack, info(aStack).m);
		return aStack;
	}

	@Override
	public ItemStack deQueen(ItemStack aStack)
	{
		setDamage(aStack, info(aStack).m);
		return aStack;
	}

	@Override
	public ItemStack putNewComb(ItemStack aStack, int queenCount,
			short honeyCount, short beesCount)
	{
		BeeCombInfo info = info(aStack);
		if(info.m >= 0 && info.e <= beesCount && info.g >= beesCount && info.h == queenCount && honeyCount >= info.f)
		{
			return new ItemStack(this, 1, info.n);
		}
		return null;
	}

	@Override
	public int getGrowSpeed(World world, BlockPos pos, ItemStack aStack, int beesCount)
	{
		IItemBehaviour behavior = itemBehaviors.get(getDamage(aStack));
		if(behavior instanceof IBCGS)
		{
			try
			{
				return ((IBCGS) behavior).getGrowSpeed(world, pos, beesCount);
			}
			catch(Throwable e)
			{
				FleLog.getLogger().catching(e);
			}
		}
		return 0;
	}
	
	public static interface IBCGS{int getGrowSpeed(World world, BlockPos pos, int beesCount);}
	
	private static class BeeCombInfo
	{
		int a;
		int b;
		int c;
		int d;
		int m;
		int n;
		
		int e;
		int f;
		int g;
		int h;

		public BeeCombInfo(int beeCap, int larvaCap)
		{
			this(beeCap, larvaCap, false, -1, -1, -1, -1, -1, false, -1);
		}
		public BeeCombInfo(int beeCap, int larvaCap, boolean hasQueen, int tick, int outputID, int minHoney, int minBee, int maxBee, boolean withQueen, int growID)
		{
			a = beeCap;
			b = larvaCap;
			c = hasQueen ? 1 : 0;
			d = tick;
			m = outputID;
			e = minBee;
			f = minHoney;
			g = maxBee;
			h = withQueen ? 1 : 0;
			n = growID;
		}
	}
}