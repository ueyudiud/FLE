package farcore.block.plant.tree;

import farcore.lib.substance.SubstanceWood;
import farcore.lib.tile.TileEntityAgeUpdatable;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySpaling extends TileEntityAgeUpdatable
{
	SubstanceWood wood = SubstanceWood.WOOD_VOID;
	private int age;
	
	@Override
	protected boolean init()
	{
		if(wood == null) return false;
		return super.init();
	}
	
	public void setWood(SubstanceWood wood)
	{
		this.wood = wood;
	}
	
	@Override
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		super.writeDescriptionsToNBT1(nbt);
		nbt.setString("w", wood.getName());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("wood", wood.getName());
		nbt.setShort("age", (short) age);
	}
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		super.readDescriptionsFromNBT1(nbt);
		wood = SubstanceWood.getSubstance(nbt.getString("w"));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		wood = SubstanceWood.getSubstance(nbt.getString("wood"));
		age = nbt.getShort("age");
	}
	
	@Override
	protected void updateServer2()
	{
		if(!getBlockType().canBlockStay(worldObj, xCoord, yCoord, zCoord))
		{
			removeBlock(0, 0, 0);
			return;
		}
		if(wood == null) return;
		++age;
		if(age >= 50)
		{
			grow(wood);
		}
	}

	public boolean grow(SubstanceWood wood)
	{
		if(wood.generator.generate(worldObj, rand, xCoord, yCoord, zCoord, false))
		{
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}
}