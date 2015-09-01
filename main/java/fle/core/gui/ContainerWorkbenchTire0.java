package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import fle.api.gui.ContainerCraftable;

public class ContainerWorkbenchTire0 extends ContainerCraftable
{
    public IInventory craftResult = new InventoryCraftResult();
    private World worldObj;
    private int posX;
    private int posY;
    private int posZ;
    
	public ContainerWorkbenchTire0(World aWorld, int x, int y, int z, InventoryPlayer player)
	{
		super(player, null, 0, 0);
        worldObj = aWorld;
        posX = x;
        posY = y;
        posZ = z;
		inv = new InventoryWorkbenchTire0(this);
		
		locateRecipeInput = new TransLocation("input", -1);
		locateRecipeOutput = new TransLocation("output", 36, 46);
        addSlotToContainer(new SlotCrafting(player.player, inv, this.craftResult, 0, 124, 35));
        int l;
        int i1;

        for (l = 0; l < 3; ++l)
        {
            for (i1 = 0; i1 < 3; ++i1)
            {
                this.addSlotToContainer(new Slot(inv, i1 + l * 3, 30 + i1 * 18, 17 + l * 18)
                {
                	@Override
                	public boolean isItemValid(ItemStack aStack)
                	{
                		return ((InventoryWorkbenchTire0) inv).isSlotAccess(getSlotIndex());
                	}
                });
            }
        }
	}

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventory)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe((InventoryCrafting) inv, worldObj));
    }
    
    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);

        if (!this.worldObj.isRemote)
        {
            for (int i = 0; i < 9; ++i)
            {
                ItemStack itemstack = this.inv.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
    
    public boolean canInteractWith(EntityPlayer player)
    {
        return player.getDistanceSq((double)posX + 0.5D, (double)posY + 0.5D, (double)posZ + 0.5D) <= 64.0D;
    }
}