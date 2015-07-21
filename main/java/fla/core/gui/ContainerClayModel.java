package fla.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fla.api.network.IListenerContainer;
import fla.api.recipe.ErrorType;
import fla.core.gui.base.ContainerCraftable;
import fla.core.gui.base.SlotOutput;
import fla.core.recipe.machine.ClayModelRecipe;

public class ContainerClayModel extends ContainerCraftable implements IListenerContainer
{
	World world;
	int x;
	int y;
	int z;
	float[] fs;
	ErrorType type = null;
	
	public ContainerClayModel(World world, int x, int y, int z, InventoryPlayer player) 
	{
		super(player, null, 0, 0);
		this.inv = new InventoryClayModel(this);
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		fs = new float[10];
		for(int i = 0; i < fs.length; ++i) fs[i] = 1.0F;
		this.addSlotToContainer(new SlotOutput(inv, 0, 130, 35));
		
		this.locateRecipeInput = new TransLocation("inventory_input", -1);
		this.locateRecipeOutput = new TransLocation("inventory_output", 36);
	}
	
	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack, ItemStack itemstack, int locate)
	{
		if(this.locateRecipeOutput.conrrect(locate))
		{
			if(!this.locatePlayer.mergeItemStack(itemstack, true))
			{
				return true;
			}
		}
		else if(this.locatePlayerBag.conrrect(locate))
		{
			if(!this.locatePlayerHand.mergeItemStack(itemstack, false))
			{
				return true;
			}
		}
		else if(this.locatePlayerHand.conrrect(locate))
		{
			if(!this.locatePlayerBag.mergeItemStack(itemstack, false))
			{
				return true;
			}
		}
		return false;
	}

	private void onCrafting(int id) 
	{
		if(id < 5)
		{
			fs[id] = Math.max(fs[id] - 0.046875F, 0.0F);
			fs[id + 5] = Math.min(fs[id + 5] + 0.015625F, 1.0F);
		}
		else
		{
			fs[id] = Math.max(fs[id] - 0.046875F, 0.0F);
			fs[id - 5] = Math.min(fs[id - 5] + 0.015625F, 1.0F);
		}
		inv.setInventorySlotContents(0, ClayModelRecipe.getRecipeResult(fs));
	}
	
	@Override
	public void onPacketData(int x, int y, int z, byte type, short contain) 
	{
		if(type == (byte) 1)
		{
			onCrafting(contain);
		}
		else if(type == (byte) 2)
		{
			this.type = ErrorType.values()[contain];
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) 
	{
		super.onContainerClosed(player);
		dropInventoryItem(inv, player);
	}
}