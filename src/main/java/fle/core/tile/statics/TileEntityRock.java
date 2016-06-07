package fle.core.tile.statics;

import java.util.ArrayList;

import farcore.energy.thermal.ThermalHelper;
import farcore.energy.thermal.ThermalNet;
import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.lib.collection.Register;
import farcore.lib.substance.SubstanceRock;
import fle.api.block.BlockSubstance;
import fle.api.block.ItemSubstance;
import fle.api.tile.TileEntitySubstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Deprecated
public class TileEntityRock extends TileEntitySubstance<SubstanceRock>
{
	private int brokenCache = 0;
	
	public TileEntityRock()
	{
		
	}
	public TileEntityRock(SubstanceRock rock)
	{
		this.substance = rock;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("bc", (short) brokenCache);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		brokenCache = nbt.getShort("bc");
	}
	
	@Override
	protected void updateServer2()
	{
		if(getBlockType() != EnumBlock.cobble.block())
		{
			int delta = (int) ThermalNet.getTempDifference(worldObj, xCoord, yCoord, zCoord);
			if(delta > 20)
			{
				brokenCache += delta / 20;
				if(brokenCache >= 100)
				{
					EnumBlock.cobble.spawn(worldObj, xCoord, yCoord, zCoord, substance);
				}
			}
			else
			{
				if(brokenCache > 0)
					brokenCache--;
			}
		}
		super.updateServer2();
	}
	
	@Override
	public Register<SubstanceRock> getRegister()
	{
		return SubstanceRock.getRocks();
	}
	
	@Override
	public void getDrops(BlockSubstance<SubstanceRock> block, ItemSubstance item, ArrayList<ItemStack> list,
			boolean isSilkTouching)
	{
		if(isSilkTouching)
		{
			list.add(item.provide(substance));
		}
		else
		{
			ItemStack stack = EnumItem.cobble_block.instance(1, substance);
			if(stack != null)
			{
				list.add(stack);
			}
		}
	}
}