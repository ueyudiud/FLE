package fle.core.gui;

import java.util.List;

import javax.lang.model.type.ErrorType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.GuiCondition;
import fle.api.gui.SlotOutput;
import fle.api.net.INetEventListener;
import fle.core.recipe.CeramicsRecipe;

public class ContainerCeramics extends ContainerCraftable implements INetEventListener
{
	boolean outputed = false;
	World world;
	int x;
	int y;
	int z;
	float[] fs;
	GuiCondition type = null;
	
	public ContainerCeramics(World world, int x, int y, int z, InventoryPlayer player) 
	{
		super(player, null, 0, 0);
		this.inv = new InventoryCeramics(this);
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
		if(outputed) return;
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
		inv.setInventorySlotContents(0, CeramicsRecipe.getRecipeResult(fs));
	}
	
	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == (byte) 1)
		{
			onCrafting((Integer) contain);
		}
		else if(type == (byte) 2)
		{
			this.type = GuiCondition.register.get((String) contain);
		}
	}
	
	@Override
	public void onOutputChanged(IInventory inv, int slotId)
	{
		if(CeramicsRecipe.getRecipeResult(fs) != null && inv.getStackInSlot(0) == null)
		{
			for(int i = 0; i < fs.length; ++i) fs[i] = 0.0F;
			outputed = true;
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) 
	{
		super.onContainerClosed(player);
	}
}