package fle.core.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import fle.FLE;
import fle.api.FleValue;
import fle.api.gui.InventoryWithFluidTank;
import fle.api.material.MatterDictionary;
import fle.api.material.MatterDictionary.IFreezingRecipe;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityCastingPool;

public class InventoryCastingPool extends InventoryWithFluidTank<TileEntityCastingPool>
{
	public int buf = 0;
	
	public InventoryCastingPool()
	{
		super(12, 6000);
		maxHeat = 1800;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		buf = nbt.getInteger("Buf");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Buf", buf);
	}
	
	int syncTick = 0;
	
	@Override
	public void updateEntity(TileEntityCastingPool tile)
	{
		super.updateEntity(tile);
		if(syncTick == 20)
		{
			if(RecipeHelper.fillOrDrainInventoryTank(this, tank, 9, 10))
			{
				tile.getWorldObj().markBlockRangeForRenderUpdate(tile.xCoord, tile.yCoord, tile.zCoord, tile.xCoord, tile.yCoord, tile.zCoord);
			}
		}
		else ++syncTick;
		IFreezingRecipe recipe = MatterDictionary.getFreeze(getFluid(), this);
		if(recipe != null && !tile.getWorldObj().isRemote)
		{
			int require = recipe.getMatterRequire(getFluid(), this);
			if(getFluidAmount() >= require)
			{
				++buf;
				if(buf > (getFluid().getFluid().getTemperature(getFluid()) - FleValue.WATER_FREEZE_POINT))
				{
					ItemStack output = recipe.getOutput(getFluid(), tile);
					if(RecipeHelper.matchOutput(tile, 11, output))
					{
						buf = 0;
						drain(require, true);
						for(int i = 0; i < 9; ++i)
							RecipeHelper.onInputItemStack(this, i);
						RecipeHelper.onOutputItemStack(tile, 11, output);
						syncSlot(tile, 0, 9);
						syncSlot(tile, 11, 12);
					}
					else
					{
						
					}
				}
				else
				{
					tile.tc.reseaveHeat((getFluid().getFluid().getTemperature(getFluid()) - FLE.fle.getThermalNet().getEnvironmentTemperature(tile.getBlockPos())) * 0.2D);
					FLE.fle.getNetworkHandler().sendToNearBy(new CoderTileUpdate(tile, (byte) 2, (Double) tile.tc.getHeat()), new TargetPoint(tile.getWorldObj().provider.dimensionId, tile.xCoord + 0.5F, tile.yCoord + 0.5F, tile.zCoord + 0.5F, 16.0F));
				}
				syncTank(tile);
				FLE.fle.getNetworkHandler().sendToNearBy(new CoderTileUpdate(tile, (byte) 1, (Integer) buf), new TargetPoint(tile.getWorldObj().provider.dimensionId, tile.xCoord + 0.5F, tile.yCoord + 0.5F, tile.zCoord + 0.5F, 16.0F));
			}
		}
		else if(recipe == null && !tile.getWorldObj().isRemote)
		{
			buf = 0;
		}
	}

	@Override
	protected void onMelted(TileEntityCastingPool tile)
	{
		if(getFluid().getFluid().canBePlacedInWorld())
		{
			tile.getWorldObj().removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
			tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, getFluid().getFluid().getBlock());
		}
	}

	@Override
	public String getInventoryName()
	{
		return "inventory.casting.pool";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.UP && aSlotID == 9;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.UP && (aSlotID == 10 || aSlotID == 11);
	}
}