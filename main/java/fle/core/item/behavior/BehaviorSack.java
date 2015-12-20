package fle.core.item.behavior;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import flapi.solid.ISolidContainerItem;
import flapi.solid.Solid;
import flapi.solid.Solid.SolidState;
import flapi.solid.SolidStack;
import flapi.solid.SolidTank;
import flapi.solid.SolidTankInfo;
import fle.core.item.ItemFleSub;
import fle.tool.item.behavior.BehaviorTool;

public class BehaviorSack extends BehaviorTool implements ISolidContainerItem
{
	public static int emptySackIndex;
	
	public static final Map<Solid, String> map = new HashMap();
	
	protected final SolidState state;
	protected final int capacity;

	public BehaviorSack(SolidState aState, int aCapacity)
	{
		state = aState;
		capacity = aCapacity;
	}
	public BehaviorSack(String str, Solid aSolid, int aCapacity)
	{
		map.put(aSolid, str);
		state = aSolid.getSolidState();
		capacity = aCapacity;
	}

	private NBTTagCompound setupNBT(ItemStack aStack)
	{
		if(!aStack.hasTagCompound()) aStack.stackTagCompound = new NBTTagCompound();
		return aStack.stackTagCompound;
	}
	
	public boolean isEmpty(ItemStack aStack)
	{
		return aStack.hasTagCompound() ? getSolid(aStack) != null : true;
	}
	
	@Override
	public SolidStack getSolid(ItemStack aStack)
	{
		return SolidStack.readFromNBT(setupNBT(aStack));
	}

	@Override
	public int fill(ItemStack aStack, SolidStack resource, boolean doFill)
	{
		if(map.containsKey(resource.get()))
		{
			SolidTank st = new SolidTank(capacity, getSolid(aStack));
			int ret = st.fill(resource, doFill);
			if(doFill)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				st.writeToNBT(nbt);
				aStack.stackTagCompound = nbt;
				aStack.setItemDamage(ItemFleSub.a(map.get(st.get())).getItemDamage());
			}
			return ret;
		}
		return 0;
	}

	@Override
	public SolidStack drain(ItemStack aStack, SolidStack resource,
			boolean doDrain)
	{
		SolidStack tStack = getSolid(aStack);
		return tStack != null && tStack.isStackEqul(resource) ? drain(aStack, resource.getSize(), doDrain) : null;
	}

	@Override
	public SolidStack drain(ItemStack aStack, int maxDrain, boolean doDrain)
	{
		SolidTank st = new SolidTank(capacity, getSolid(aStack));
		SolidStack ret = st.drain(maxDrain, doDrain);
		if(doDrain)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			st.writeToNBT(nbt);
			aStack.stackTagCompound = nbt;
			if(st.get() == null)
			{
				aStack.setItemDamage(emptySackIndex);
			}
		}
		return ret;
	}

	@Override
	public boolean canFill(ItemStack aStack, Solid solid)
	{
		return solid == null ? true : solid.getSolidState().ordinal() <= state.ordinal();
	}

	@Override
	public boolean canDrain(ItemStack aStack, Solid solid)
	{
		return true;
	}

	@Override
	public SolidTankInfo getTankInfo(ItemStack aStack)
	{
		return new SolidTank(capacity, getSolid(aStack)).getInfo();
	}
}