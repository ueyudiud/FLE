package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.SlotHolographic;
import fle.api.net.INetEventListener;
import fle.api.world.BlockPos;
import fle.core.te.argil.TileEntityCeramicFurnaceCrucible;
import fle.core.te.argil.TileEntityCeramicFurnaceFirebox;
import fle.core.te.argil.TileEntityCeramicFurnaceInlet;
import fle.core.te.argil.TileEntityCeramicFurnaceOutlet;

public class ContainerCeramicFurnace extends ContainerCraftable implements INetEventListener
{
	public BlockPos pos;
	protected TileEntityCeramicFurnaceInlet tileCFI = null;
	protected TileEntityCeramicFurnaceCrucible tileCFC = null;
	protected TileEntityCeramicFurnaceOutlet tileCFO = null;
	protected TileEntityCeramicFurnaceFirebox tileCFF = null;
	
	public ContainerCeramicFurnace(BlockPos aPos, InventoryPlayer player)
	{
		super(player, null, 0, 0);
		pos = aPos;
		int i;
		if(isFireboxNearBy(aPos))
		{
			addSlotToContainer(new SlotHolographic(tileCFF, 3, 22, 58, false, false));
			for(i = 0; i < 3; ++i)
				addSlotToContainer(new Slot(tileCFF, i, 41 + 18 * i, 58));
		}
		if(isCrucibleNearBy(aPos))
		{
			
		}
		if(isInletNearBy(aPos) && tileCFC != null)
		{
			addSlotToContainer(new Slot(tileCFC, 0, 39, 26));
			addSlotToContainer(new Slot(tileCFC, 1, 59, 11));
			addSlotToContainer(new Slot(tileCFC, 2, 79, 26));
		}
		if(isOutletNearBy(aPos))
		{
			addSlotToContainer(new Slot((IInventory) tileCFO, 0, 138, 26));
			addSlotToContainer(new Slot((IInventory) tileCFO, 1, 138, 58));
		}
		locateRecipeInput = new TransLocation("input", -1);
		locateRecipeOutput = new TransLocation("output", -1);
	}
	
	boolean isFireboxNearBy(BlockPos aPos)
	{
		if(aPos.getBlockTile() instanceof TileEntityCeramicFurnaceFirebox)
		{
			tileCFF = (TileEntityCeramicFurnaceFirebox) aPos.getBlockTile();
			return true;
		}
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(aPos.toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceFirebox)
			{
				tileCFF = (TileEntityCeramicFurnaceFirebox) aPos.toPos(dir).getBlockTile();
				return true;
			}
		}
		return tileCFF != null;
	}
	
	boolean isCrucibleNearBy(BlockPos aPos)
	{
		if(tileCFF != null)
		{
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				if(dir == ForgeDirection.DOWN) continue;
				if(tileCFF.getBlockPos().toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
				{
					tileCFC = (TileEntityCeramicFurnaceCrucible) tileCFF.getBlockPos().toPos(dir).getBlockTile();
					return true;
				}
			}
			return false;
		}
		if(aPos.getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			tileCFC = (TileEntityCeramicFurnaceCrucible) aPos.getBlockTile();
			return true;
		}
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(aPos.toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
			{
				tileCFC = (TileEntityCeramicFurnaceCrucible) aPos.toPos(dir).getBlockTile();
				return true;
			}
		}
		return tileCFC != null;
	}
	
	boolean isInletNearBy(BlockPos aPos)
	{
		if(tileCFC != null)
		{
			if(tileCFC.getBlockPos().toPos(ForgeDirection.UP).getBlockTile() instanceof TileEntityCeramicFurnaceInlet)
			{
				tileCFI = (TileEntityCeramicFurnaceInlet) tileCFC.getBlockPos().toPos(ForgeDirection.UP).getBlockTile();
				return true;
			}
			else
			{
				return false;
			}
		}
		if(aPos.getBlockTile() instanceof TileEntityCeramicFurnaceInlet)
		{
			tileCFI = (TileEntityCeramicFurnaceInlet) aPos.getBlockTile();
			return true;
		}
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(aPos.toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceInlet)
			{
				tileCFI = (TileEntityCeramicFurnaceInlet) aPos.toPos(dir).getBlockTile();
				return true;
			}
		}
		return tileCFI != null;
	}
	
	private static ForgeDirection dirs[] = {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
	
	boolean isOutletNearBy(BlockPos aPos)
	{
		if(tileCFC != null)
		{
			for(ForgeDirection dir : dirs)
			{
				if(tileCFC.getBlockPos().toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceOutlet)
				{
					tileCFO = (TileEntityCeramicFurnaceOutlet) tileCFC.getBlockPos().toPos(dir).getBlockTile();
					return true;
				}
			}
			return false;
		}
		if(aPos.getBlockTile() instanceof TileEntityCeramicFurnaceOutlet)
		{
			tileCFO = (TileEntityCeramicFurnaceOutlet) aPos.getBlockTile();
			return true;
		}
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(aPos.toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceOutlet)
			{
				tileCFO = (TileEntityCeramicFurnaceOutlet) aPos.toPos(dir).getBlockTile();
				return true;
			}
		}
		return tileCFO != null;
	}
	
	@Override
	public ItemStack slotClick(int aSlotID, int aMouseclick, int aShifthold, EntityPlayer aPlayer)
	{
		if(tileCFF != null && aSlotID > 0)
		{
			if(getSlot(aSlotID) == getSlotFromInventory(tileCFF, 3))
			{
				ItemStack tStack = aPlayer.inventory.getItemStack();
			    if (tStack != null)
			    {
			    	tileCFF.onToolClick(tStack, aPlayer);
			    	if (tStack.stackSize <= 0)
			    	{
			    		aPlayer.inventory.setItemStack(null);
			    	}
			    }
			    return null;
			}
		}
		return super.slotClick(aSlotID, aMouseclick, aShifthold, aPlayer);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq((double) pos.x + 0.5D, (double) pos.y + 0.5D, (double) pos.z + 0.5D) <= 64.0D;
	}
	
	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 0)
		{
			switch((Integer) contain)
			{
			case 0 : tileCFC.onOutput();
			break;
			case 1 : tileCFC.drain();
			}
		}
	}
}