package fle.core.te;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.api.net.INetEventListener;
import fle.api.recipe.CraftingState;
import fle.api.te.TEInventory;
import fle.core.inventory.InventoryColdForgingPlatform;
import fle.core.recipe.ColdForgingRecipe;

public class TileEntityColdForgingPlatform extends TEInventory<InventoryColdForgingPlatform> implements INetEventListener
{
	public TileEntityColdForgingPlatform()
	{
		super(new InventoryColdForgingPlatform());
	}

	@Override
	public void updateInventory()
	{
		inv.updateEntity(this);
	}
	
	public void onOutput()
	{
		inv.onOutput();
	}
	
	public boolean canTakeStack()
	{
		return inv.canTakeStack();
	}

	public void onSlotChange()
	{
		inv.onSlotChanged();
	}

	public void onToolClick(EntityPlayer aPlayer, int activeSlot)
	{
		int[] array = new int[2];
		switch(inv.dir)
		{
		case 0 : array[0] = inv.hard;
		break;
		case 1 : array[1] = inv.hard;
		break;
		case 2 : array[0] = -inv.hard;
		break;
		case 3 : array[1] = -inv.hard;
		}
		inv.onToolClick(aPlayer, activeSlot, ColdForgingRecipe.getState(array));
		inv.onSlotChanged();
		inv.syncSlot(this, 0, 4);
		sendToNearBy(new CoderTileUpdate(this, (byte) 2, new String(inv.array)), 16.0F);
	}
	
	public void setup()
	{
		inv.dir = 0;
		inv.hard = 1;
	}
	
	public void switchType(int type)
	{
		if(type == 0)
		{
			if(++inv.dir == 4) 
				inv.dir = 0;
		}
		else if(type == 1)
		{
			if(++inv.hard == 4) 
				inv.hard = 1;
		}
		else if(type == 2)
		{
			for(int i = 0; i < 4; ++i)
				inv.decrStackSize(i, 1);
			inv.array = "         ".toCharArray();
			sendToNearBy(new CoderTileUpdate(this, (byte) 2, new String(inv.array)), 16.0F);
		}
		sendToNearBy(new CoderTileUpdate(this, (byte) 3, inv.hard), 16.0F);
		sendToNearBy(new CoderTileUpdate(this, (byte) 4, inv.dir), 16.0F);
	}
	
	public int getDir()
	{
		return inv.dir;
	}
	
	public int getHarness()
	{
		return inv.hard;
	}

	@SideOnly(Side.CLIENT)
	public CraftingState getState(int i)
	{
		return CraftingState.getState(inv.array[i]);
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		switch(type)
		{
		case 2 : inv.array = ((String) contain).toCharArray();
		break;
		case 3 : inv.hard = (Integer) contain;
		break;
		case 4 : inv.dir = (Integer) contain;
		}
	}
}