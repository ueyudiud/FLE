package fle.core.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import fle.api.inventory.InventoryWithFluidTank;
import fle.api.material.IAtoms;
import fle.api.material.MaterialAbstract;
import fle.api.material.MaterialAlloy;
import fle.api.material.Matter;
import fle.api.material.Matter.AtomStack;
import fle.api.material.MatterDictionary;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;
import fle.core.init.Lang;
import fle.core.net.FleMatterUpdatePacket;
import fle.core.te.argil.TileEntityCeramicFurnaceCrucible;
import fle.core.util.MatterContainer;

public class InventoryCeramicFurnaceCrucible extends InventoryWithFluidTank<TileEntityCeramicFurnaceCrucible>
{
	public int[] progress = new int[3];
	public MatterContainer container = new MatterContainer();
	private int buf1 = 0;
	
	public InventoryCeramicFurnaceCrucible()
	{
		super(Lang.inventory_ceramicFurnace + ".crucible", 3, 3000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		progress = nbt.getIntArray("Progress");
		container.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setIntArray("Progress", progress);
		container.writeToNBT(nbt);
	}
	
	@Override
	public void updateEntity(TileEntityCeramicFurnaceCrucible tile)
	{
		int tem = tile.getTemperature();
		for(int i = 0; i < stacks.length; ++i)
		{
			int[] is = MatterDictionary.getMeltRequires(stacks[i]);
			if(is[0] <= tem)
			{
				int tick = (tem - is[0]) * 2;
				tile.onHeatEmit(ForgeDirection.UNKNOWN, tick);
				progress[i] += tick;
				if(progress[i] >= is[1])
				{
					meltItem(i);
				}
			}
			else if(stacks[i] == null)
			{
				progress[i] = 0;
			}
			else if(progress[i] > 0)
			{
				--progress[i];
			}
		}
		if(buf1 < 20) ++buf1;
		else
		{
			updateMatter(tile);
			buf1 = 0;
			tile.sendToNearBy(new FleMatterUpdatePacket(tile, tile), 16.0F);
			syncTank(tile);
		}
	}
	
	private void updateMatter(TileEntityCeramicFurnaceCrucible tile)
	{
		if(tile.getWorldObj().isRemote) return;
		container.update(tile.getBlockPos(), tile);
	}
	
	private static final float lostSize = 0.25F;
	
	private void meltItem(int slotID)
	{
		AtomStack stack = MatterDictionary.getMatter(stacks[slotID]).copy();
		stack.setSize((int) (stack.getSize() * lostSize));
		container.add(stack);
		--stacks[slotID].stackSize;
		if(stacks[slotID].stackSize <= 0) stacks[slotID] = null;
		progress[slotID] = 0;
	}
	
	public void outputStack(TileEntityCeramicFurnaceCrucible tile)
	{
		MaterialAbstract m = MaterialAlloy.findAlloy(container.getHelper());
		if(m != null)
		{
			FluidStack stack = MatterDictionary.getFluid(new AtomStack(m.getMatter(), container.size()));
			if(fill(stack, false) != 0)
			{
				fill(stack, true);
				container.clear();
			}
			syncSlot(tile);
		}
	}
	
	@Override
	protected void onMelted(TileEntityCeramicFurnaceCrucible tile)
	{
		if(getFluid() != null)
			if(getFluid().getFluid().canBePlacedInWorld() && getFluidAmount() >= 1000)
			{
				tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, getFluid().getFluid().getBlock());
				tile.getWorldObj().removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
				tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
			}
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.UP ? MatterDictionary.getMatter(aResource) != null : false;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
	}
}