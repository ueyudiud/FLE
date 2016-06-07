package farcore.block.plant.tree;

import java.util.List;

import farcore.interfaces.tile.IDebugableTile;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.tile.TileEntityAgeUpdatable;
import farcore.util.U;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class TileEntitySpaling extends TileEntityAgeUpdatable implements IDebugableTile
{
	SubstanceWood wood = SubstanceWood.WOOD_VOID;
	private float age;
	
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
		nbt.setFloat("age", age);
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
		age = nbt.getFloat("age");
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
		age += add();
		if(age >= getMaxAge())
		{
			grow(wood);
		}
	}
	
	public float add()
	{
		float temp = U.Worlds.getEnviormentTemp(worldObj, xCoord, yCoord, zCoord);
		if(temp < 270F || temp > 340) return 0;
		float value = - (temp - 270F) * (temp - 340F) / 1000F;
		float rainfall;
		if((rainfall = worldObj.getBiomeGenForCoords(xCoord, zCoord).getFloatRainfall()) < 0.2F)
		{
			value *= rainfall * 5F;
		}
		if(U.Worlds.isCatchingRain(worldObj, xCoord, yCoord, zCoord))
		{
			value += 0.1F;
		}
		int airCount = 0;
		for(int i = -5; i <= 5; ++i)
			for(int j = -5; j <= 5; ++j)
				for(int k = -5; k <= 5; ++k)
				{
					if(worldObj.isAirBlock(xCoord + i, yCoord + j, zCoord + k))
					{
						++airCount;
					}
				}
		value += airCount * 0.001F;
		return value;
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

	public int getMaxAge()
	{
		return 50;
	}
	
	@Override
	public void addDebugInformation(List<String> list)
	{
		list.add("Grow progress : " + EnumChatFormatting.GREEN + U.Lang.progress((double) age / (double) getMaxAge()));
	}
}