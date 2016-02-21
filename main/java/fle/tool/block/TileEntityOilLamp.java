package fle.tool.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.TEBase;
import flapi.FleAPI;
import flapi.util.FleValue;
import fle.FLE;

public class TileEntityOilLamp extends TEBase
{
	private float fluidBuffer;
	public final FluidTank fluid = new FluidTank(FleValue.CAPACITY[4]);
	public boolean hasWick;
	public boolean isBurning;
	private Block rockBlock;
	private short rockMeta;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		fluid.readFromNBT(nbt);
		fluidBuffer = nbt.getFloat("Buffer");
		hasWick = nbt.getBoolean("Wick");
		isBurning = nbt.getBoolean("Burning");
		rockBlock = GameData.getBlockRegistry().getObject(nbt.getString("RockBlock"));
		rockMeta= nbt.getShort("RockMeta");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		fluid.writeToNBT(nbt);
		nbt.setFloat("Buffer", fluidBuffer);
		nbt.setBoolean("Wick", hasWick);
		nbt.setBoolean("Burning", isBurning);
		if(rockBlock != null)
			nbt.setString("RockBlock", GameData.getBlockRegistry().getNameForObject(rockBlock));
		nbt.setShort("RockMeta", rockMeta);
	}
	
	@Override
	public void updateEntity() 
	{
		if(!worldObj.isRemote)
		{
			markNBTUpdate();
			if(!getBlockType().canBlockStay(worldObj, xCoord, yCoord, zCoord))
			{
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
				worldObj.removeTileEntity(xCoord, yCoord, zCoord);
				getBlockType().breakBlock(worldObj, xCoord, yCoord, zCoord, blockType, blockMetadata);
			}
		}
		if(fluid.getFluidAmount() == 0 || isCatchRain())
		{
			isBurning = false;
		}
		if(isBurning)
		{
			if(!hasWick) setDisburning();
			if(fluidBuffer <= 0)
			{
				if(true)//XXX
				{
					setDisburning();
				}
				else
				{
					fluidBuffer = (float) FleAPI.getFuelBuf(fluid.drain(1, true));
				}
			}
			else
			{
				--fluidBuffer;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getRockIcon()
	{
		return rockBlock == null ? Blocks.stone.getIcon(0, 0) : rockBlock.getIcon(0, rockMeta);
	}

	public void setContainFluid(FluidStack fuel)
	{
		setContainFluid(fuel, FleValue.CAPACITY[4]);
	}
	public void setContainFluid(FluidStack fuel, int amont)
	{
		if(fuel != null)
		{
			fluid.setFluid(new FluidStack(fuel, amont));
		}
		else
		{
			fluid.setFluid(null);
		}
	}
	
	public void setWick()
	{
		this.hasWick = true;
	}
	
	public void setBurning()
	{
		this.isBurning = true;
	}
	
	public boolean isBurning()
	{
		return isBurning && hasWick;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasSmoke()
	{
		return isBurning() ? FleAPI.hasSmoke(fluid.drain(1, false)) : false;
	}

	public Block getRock()
	{
		return rockBlock == null ? Blocks.stone : rockBlock;
	}
	
	public short getRockMeta()
	{
		return rockMeta;
	}
	
	public void setRockBlock(Block aRockBlock, int aMeta)
	{
		rockBlock = aRockBlock;
		rockMeta = (short) aMeta;
	}

	public void setDisburning() 
	{
		isBurning = false;
		worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, 0);
	}
}