package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.gui.ContainerCraftable;
import flapi.gui.FluidSlot;
import flapi.gui.SlotTool;
import flapi.net.INetEventListener;
import flapi.te.interfaces.IContainerNetworkUpdate;
import flapi.world.BlockPos;
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
			addSlotToContainer(new SlotTool(tileCFF, 3, 22, 58));
			for(i = 0; i < 3; ++i)
				addSlotToContainer(new Slot(tileCFF, i, 41 + 18 * i, 58));
		}
		if(isCrucibleNearBy(aPos))
		{
			addSlotToContainer(new FluidSlot(tileCFC, 0, 120, 26, 8, 30));
		}
		if(isInletNearBy(aPos) && tileCFC != null)
		{
			addSlotToContainer(new Slot(tileCFC, 0, 39, 26));
			addSlotToContainer(new Slot(tileCFC, 1, 59, 11));
			addSlotToContainer(new Slot(tileCFC, 2, 79, 26));
		}
		if(isOutletNearBy(aPos))
		{
			addSlotToContainer(new Slot(tileCFO, 0, 138, 26));
			addSlotToContainer(new Slot(tileCFO, 1, 138, 58));
		}
		lastNumbers = new int[(tileCFF != null ? tileCFF.getProgressSize() : 0) + (tileCFC != null ? tileCFC.getProgressSize() : 0) + (tileCFO != null ? tileCFO.getProgressSize() : 0)];
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
	public void addCraftingToCrafters(ICrafting crafter)
	{
		super.addCraftingToCrafters(crafter);
		for(int i = 0; i < lastNumbers.length; ++i)
		{
			crafter.sendProgressBarUpdate(this, i, getProgress(i));
		}
	}

	@Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(Object obj : crafters)
        {
        	ICrafting crafter = (ICrafting) obj;
    		for(int i = 0; i < lastNumbers.length; ++i)
    		{
    			int value = getProgress(i);
    			if(lastNumbers[i] != value)
    				crafter.sendProgressBarUpdate(this, i, value);
    		}
        }
		for(int i = 0; i < lastNumbers.length; ++i)
			lastNumbers[i] = getProgress(i);
    }
	
	@Override
	public void updateProgressBar(int index, int amount)
	{
		super.updateProgressBar(index, amount);
		setProgress(index, amount);
	}

	protected int getProgress(int id)
	{
		int i = 0;
    	if (tileCFF instanceof IContainerNetworkUpdate)
    	{
    		i = id;
    		id -= ((IContainerNetworkUpdate) tileCFF).getProgressSize();
    		if(id < 0) return ((IContainerNetworkUpdate) tileCFF).getProgress(i);
    	}
    	if (tileCFC instanceof IContainerNetworkUpdate)
    	{
    		i = id;
    		id -= ((IContainerNetworkUpdate) tileCFC).getProgressSize();
    		if(id <= 0) return  ((IContainerNetworkUpdate) tileCFC).getProgress(i);
    	}
    	if (tileCFO instanceof IContainerNetworkUpdate)
    	{
    		i = id;
    		id -= ((IContainerNetworkUpdate) tileCFO).getProgressSize();
    		if(id <= 0) return  ((IContainerNetworkUpdate) tileCFO).getProgress(i);
    	}
    	return 0;
	}
	protected void setProgress(int id, int value)
	{
		int i = 0;
    	if (tileCFF instanceof IContainerNetworkUpdate)
    	{
    		i = id;
    		id -= ((IContainerNetworkUpdate) tileCFF).getProgressSize();
    		if(id < 0) 
    		{
    			((IContainerNetworkUpdate) tileCFF).setProgress(i, value);
    			return;
    		}
    	}
    	if (tileCFC instanceof IContainerNetworkUpdate)
    	{
    		i = id;
    		id -= ((IContainerNetworkUpdate) tileCFC).getProgressSize();
    		if(id <= 0)
    		{
    			((IContainerNetworkUpdate) tileCFC).setProgress(i, value);
    			return;
    		}
    	}
    	if (tileCFO instanceof IContainerNetworkUpdate)
    	{
    		i = id;
    		id -= ((IContainerNetworkUpdate) tileCFO).getProgressSize();
    		if(id <= 0)
    		{
    			((IContainerNetworkUpdate) tileCFO).setProgress(i, value);
    			return;
    		}
    	}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq((double) pos.x + 0.5D, (double) pos.y + 0.5D, (double) pos.z + 0.5D) <= 64.0D;
	}
	
	@Override
	public void onReceive(byte type, Object contain)
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