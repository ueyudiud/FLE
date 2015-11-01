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
import fle.core.net.FleMatterUpdatePacket;
import fle.core.te.argil.TileEntityCeramicFurnaceCrucible;

public class InventoryCeramicFurnaceCrucible extends InventoryWithFluidTank<TileEntityCeramicFurnaceCrucible>
{
	private int[] progress = new int[3];
	public Map<IAtoms, Integer> matterMap = new HashMap();
	private int buf1 = 0;
	
	public InventoryCeramicFurnaceCrucible()
	{
		super("inventory.ceramic.furnace.crucible", 3, 3000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		progress = nbt.getIntArray("Progress");
		matterMap.clear();
		NBTTagList list = nbt.getTagList("Progress", 11);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			Matter matter = Matter.getMatterFromName(nbt1.getString("CFN"));
			int size = nbt1.getInteger("C");
			if(matter != null && size > 0)
				matterMap.put(matter, size);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setIntArray("Progress", progress);
		NBTTagList list = new NBTTagList();
		for(Entry<IAtoms, Integer> entry : matterMap.entrySet())
		{
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setString("CFN", entry.getKey().getChemicalFormulaName());
			nbt1.setInteger("C", entry.getValue());
			list.appendTag(nbt1);
		}
		nbt.setTag("MatterMap", list);
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
		Stack<IAtoms>[] stacks = WeightHelper.asArray(matterMap);
		matterMap.clear();
		for(Stack<IAtoms> s : stacks)
		{
			if(s.getObj() == null || s.getSize() <= 0) continue;
			Stack<IAtoms>[] s1 = s.getObj().getAtomsOutput(tile, s);
			if(s1 != null)
			{
				WeightHelper.add(matterMap, s1);
			}
		}
	}
	
	private void meltItem(int slotID)
	{
		AtomStack stack = MatterDictionary.getMatter(stacks[slotID]).copy();
		stack.setSize(stack.getSize() / 4);
		WeightHelper.add(matterMap, stack);
		--stacks[slotID].stackSize;
		if(stacks[slotID].stackSize <= 0) stacks[slotID] = null;
		progress[slotID] = 0;
	}
	
	public void outputStack(TileEntityCeramicFurnaceCrucible tile)
	{
		WeightHelper<IAtoms> wh = new WeightHelper<IAtoms>(matterMap);
		MaterialAbstract m = MaterialAlloy.findAlloy(wh);
		if(m != null)
		{
			FluidStack stack = MatterDictionary.getFluid(new AtomStack(m.getMatter(), wh.allWeight()));
			if(fill(stack, false) != 0)
			{
				fill(stack, true);
				matterMap.clear();
			}
		}
		else
		{
			matterMap.clear();
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