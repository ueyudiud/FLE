package fle.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.inventory.InventoryTileBase;
import fle.core.init.Lang;
import fle.core.te.argil.TileEntityCeramicFurnaceCrucible;
import fle.core.te.argil.TileEntityCeramicFurnaceFirebox;

public class InventoryCeramicFurnaceFirebox extends InventoryTileBase<TileEntityCeramicFurnaceFirebox>
{
	int burnTime;
	int currectBurnTime;
	boolean isBurning;
	
	public InventoryCeramicFurnaceFirebox()
	{
		super(4);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("BurnTime", burnTime);
		nbt.setBoolean("IsBurning", isBurning);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		currectBurnTime = burnTime = nbt.getInteger("BurnTime");
		isBurning = nbt.getBoolean("IsBurning");
	}
	
	private ForgeDirection dirs[] = {ForgeDirection.UP, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.WEST, ForgeDirection.SOUTH};

	@Override
	public void updateEntity(TileEntityCeramicFurnaceFirebox tile)
	{
		if(burnTime > 0)
		{
			burnTime -= 1200;
			tile.onHeatReceive(ForgeDirection.UNKNOWN, 1200.0F);
			for(ForgeDirection dir : dirs)
			{
				if(tile.getBlockPos().toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
				{
					for(int i = 0; i < 10; ++i)
						FLE.fle.getThermalNet().emmitHeatTo(tile.getBlockPos(), dir);
					break;
				}
			}
		}
		if(burnTime <= 0 && isBurning)
		{
			burnTime = 0;
			for(int i = 0; i < 3; ++i)
			{
				int buf = FleAPI.getFulBuf(stacks[i], FLE.fle.getAirConditionProvider().getAirLevel(tile.getBlockPos()));
				if(buf > 0)
				{
					currectBurnTime = burnTime = buf;
					decrStackSize(i, 1);
					break;
				}
			}
		}
		if(burnTime <= 0)
		{
			isBurning = false;
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public String getInventoryName()
	{
		return Lang.inventory_ceramicFireBox;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return false;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return FleAPI.getFulBuf(aResource) > 0;
	}

	public boolean isBurning()
	{
		return isBurning;
	}
	
	public double getBurnProgress()
	{
		return (double) burnTime / (double) currectBurnTime;
	}

	public void setBurning()
	{
		isBurning = true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return new int[]{0, 1, 2};
	}
}