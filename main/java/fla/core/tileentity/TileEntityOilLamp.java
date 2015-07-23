package fla.core.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.recipe.Fuel;
import fla.api.recipe.FuelOilLamp;
import fla.api.recipe.FuelStack;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.tileentity.base.TileEntityBaseWithFacing;

public class TileEntityOilLamp extends TileEntityBaseWithFacing
{
	private float fluidBuffer;
	public FuelStack<FuelOilLamp> fluid;
	public boolean hasWick;
	public boolean isBurning;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		fluid = FuelStack.getFuelStackFromNBT(nbt);
		fluidBuffer = nbt.getFloat("Buffer");
		hasWick = nbt.getBoolean("Wick");
		isBurning = nbt.getBoolean("Burning");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		if(fluid != null) fluid.writeToNBT(nbt);
		nbt.setFloat("Buffer", fluidBuffer);
		nbt.setBoolean("Wick", hasWick);
		nbt.setBoolean("Burning", isBurning);
	}
	
	@Override
	public void updateEntity() 
	{
		if(fluid == null)
		{
			isBurning = false;
		}
		if(worldObj.isRaining())
		{
			boolean flag = true;
			for(int i = yCoord; i < 256; ++i)
			{
				if(worldObj.getBlock(xCoord, i, zCoord).isSideSolid(worldObj, xCoord, i, zCoord, ForgeDirection.UP))
				{
					flag = false;
				}
			}
			if(flag) isBurning = false;
		}
		if(isBurning)
		{
			if(!hasWick) isBurning = false;
			if(fluidBuffer <= 0)
			{
				if(fluid.decrStack(1))
				{
					isBurning = false;
					fluid = null;
					worldObj.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, 0);
				}
				else
				{
					fluidBuffer = fluid.getFuel().getFuelBuffer();
				}
			}
			else
			{
				--fluidBuffer;
			}
		}
	}

	public void setContainFluid(FuelOilLamp fuel)
	{
		setContainFluid(fuel, FlaValue.CAPACITY_OIL_LAMP);
	}
	public void setContainFluid(FuelOilLamp fuel, int amont)
	{
		if(fuel != null)
		{
			this.fluid = new FuelStack(fuel, amont);
		}
		else
		{
			this.fluid = null;
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
		return isBurning() ? ((FuelOilLamp) this.getFuelType()).hasSmoke() : false;
	}

	@Override
	public boolean canSetDirection(World world, BlockPos pos) 
	{
		return true;
	}

	@Override
	public boolean canSetDirectionWith(World world, BlockPos pos, double xPos,
			double yPos, double zPos, ItemStack itemstack) 
	{
		return true;
	}

	@Override
	public ForgeDirection setDirectionWith(World world, BlockPos pos,
			double xPos, double yPos, double zPos, ItemStack itemstack) 
	{
		return ForgeDirection.NORTH;
	}

	
	public double getContainFluid() 
	{
		return fluid == null ? 0 : fluid.getContain();
	}

	public Fuel getFuelType() 
	{
		return fluid == null ? null : fluid.getFuel();
	}
}