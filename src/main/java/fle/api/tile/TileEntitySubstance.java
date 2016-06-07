package fle.api.tile;

import java.util.ArrayList;

import farcore.lib.collection.Register;
import farcore.lib.substance.ISubstance;
import farcore.lib.substance.SubstanceBlockAbstract;
import farcore.lib.tile.TileEntityAgeUpdatable;
import farcore.lib.tile.TileEntitySyncable;
import fle.api.block.BlockSubstance;
import fle.api.block.ItemSubstance;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntitySubstance<T extends SubstanceBlockAbstract> extends TileEntityAgeUpdatable
{
	public T substance = getRegister().get(0);
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		substance = getRegister().get(nbt.getInteger("i"));
		if(substance == null)
		{
			substance = getRegister().get(0);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		substance = getRegister().get(nbt.getString("substance"));
	}
	
	@Override
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		nbt.setInteger("i", getRegister().id(substance));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(substance != null)
		{
			nbt.setString("substance", getRegister().name(substance));
		}
	}
	
	@Override
	protected void updateServer2()
	{
		
	}
	
	public abstract Register<T> getRegister();

	public void getDrops(BlockSubstance<T> block, ItemSubstance item, ArrayList<ItemStack> list, boolean isSilkTouching)
	{
		list.add(item.provide(substance));
	}
}