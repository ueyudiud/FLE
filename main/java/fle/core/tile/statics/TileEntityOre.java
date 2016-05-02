package fle.core.tile.statics;

import farcore.lib.substance.SubstanceMineral;
import farcore.lib.substance.SubstanceRock;
import farcore.lib.tile.TileEntityAgeUpdatable;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityOre extends TileEntityAgeUpdatable
{
	public boolean setup;
	public SubstanceRock rock = SubstanceRock.getSubstance("stone");
	public SubstanceMineral mineral = SubstanceMineral.VOID_MINERAL;
	
	public TileEntityOre(SubstanceMineral mineral, SubstanceRock rock)
	{
		this.mineral = mineral;
		this.rock = rock;
		this.setup = true;
	}
	
	public TileEntityOre()
	{
		setup = false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		mineral = SubstanceMineral.getSubstance(nbt.getString("ore"));
		rock = SubstanceRock.getSubstance(nbt.getString("rock"));
	}
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		super.readDescriptionsFromNBT1(nbt);
		mineral = SubstanceMineral.getSubstance(nbt.getString("o"));
		rock = SubstanceRock.getSubstance(nbt.getString("r"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("ore", mineral.getName());
		nbt.setString("rock", rock.getName());
	}
	
	@Override
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		super.writeDescriptionsToNBT1(nbt);
		nbt.setString("o", mineral.getName());
		nbt.setString("r", rock.getName());
	}
	
	@Override
	protected boolean init()
	{
		if(!setup) return false;
		return super.init();
	}
	
	@Override
	protected void updateServer2()
	{
		;
	}
}