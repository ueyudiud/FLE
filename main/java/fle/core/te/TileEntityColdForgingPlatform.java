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
		getTileInventory().updateEntity(this);
	}
	
	public void onOutput()
	{
		getTileInventory().onOutput();
	}
	
	public boolean canTakeStack()
	{
		return getTileInventory().canTakeStack();
	}

	public void onSlotChange()
	{
		getTileInventory().onSlotChanged();
	}

	public void onToolClick(EntityPlayer aPlayer, int activeSlot)
	{
		int[] array = new int[2];
		switch(getTileInventory().dir)
		{
		case 0 : array[0] = getTileInventory().hard;
		break;
		case 1 : array[1] = getTileInventory().hard;
		break;
		case 2 : array[0] = -getTileInventory().hard;
		break;
		case 3 : array[1] = -getTileInventory().hard;
		}
		getTileInventory().onToolClick(aPlayer, activeSlot, ColdForgingRecipe.getState(array));
		getTileInventory().onSlotChanged();
		getTileInventory().syncSlot(this, 0, 4);
		sendToNearBy(new CoderTileUpdate(this, (byte) 2, new String(getTileInventory().array)), 16.0F);
	}
	
	public void setup()
	{
		getTileInventory().dir = 0;
		getTileInventory().hard = 1;
	}
	
	public void switchType(int type)
	{
		if(type == 0)
		{
			if(++getTileInventory().dir == 4) 
				getTileInventory().dir = 0;
		}
		else if(type == 1)
		{
			if(++getTileInventory().hard == 4) 
				getTileInventory().hard = 1;
		}
		else if(type == 2)
		{
			for(int i = 0; i < 4; ++i)
				getTileInventory().decrStackSize(i, 1);
			getTileInventory().array = "         ".toCharArray();
			sendToNearBy(new CoderTileUpdate(this, (byte) 2, new String(getTileInventory().array)), 16.0F);
		}
		sendToNearBy(new CoderTileUpdate(this, (byte) 3, getTileInventory().hard), 16.0F);
		sendToNearBy(new CoderTileUpdate(this, (byte) 4, getTileInventory().dir), 16.0F);
	}
	
	public int getDir()
	{
		return getTileInventory().dir;
	}
	
	public int getHarness()
	{
		return getTileInventory().hard;
	}

	@SideOnly(Side.CLIENT)
	public CraftingState getState(int i)
	{
		return CraftingState.getState(getTileInventory().array[i]);
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		switch(type)
		{
		case 2 : getTileInventory().array = ((String) contain).toCharArray();
		break;
		case 3 : getTileInventory().hard = (Integer) contain;
		break;
		case 4 : getTileInventory().dir = (Integer) contain;
		}
	}
}