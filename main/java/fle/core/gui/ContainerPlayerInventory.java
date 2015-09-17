package fle.core.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.SlotHolographic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

@Deprecated
public class ContainerPlayerInventory extends ContainerPlayer
{
	int craftBuf;
	int craftingTick;
	boolean flag = false;
	
	public ContainerPlayerInventory(final InventoryPlayer inventory,
			boolean flag, final EntityPlayer player)
	{
		super(inventory, flag, player);
		flag = true;
        addSlotToContainer(new SlotCrafting(inventory.player, craftMatrix, craftResult, 0, 144, 36));
        int i;
        int j;

        for (i = 0; i < 2; ++i)
        {
            for (j = 0; j < 2; ++j)
            {
                addSlotToContainer(new Slot(craftMatrix, j + i * 2, 88 + j * 18, 26 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i)
        {
            final int k = i;
            addSlotToContainer(new Slot(inventory, inventory.getSizeInventory() - 1 - i, 8, 8 + i * 18)
            {
                public int getSlotStackLimit()
                {
                    return 1;
                }
                public boolean isItemValid(ItemStack aStack)
                {
                    if (aStack == null) return false;
                    return aStack.getItem().isValidArmor(aStack, k, player);
                }
                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex()
                {
                    return ItemArmor.func_94602_b(k);
                }
            });
        }

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
            	if(inventory.player.experienceLevel > j + i * 9)
            		addSlotToContainer(new Slot(inventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }

        onCraftMatrixChanged(craftMatrix);
        flag = false;
	}

	@Override
	protected Slot addSlotToContainer(Slot slot)
	{
		if(flag)
		{
			return super.addSlotToContainer(slot);
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack slotClick(int aSlotID, int aMouseclick, int aShifthold, EntityPlayer aPlayer)
	{
		while(craftBuf > 40)
		{
			craftBuf -= 40;
			++craftingTick;
		}
		Slot slot = getSlot(aSlotID);
		if(slot.isSlotInInventory(craftResult, 0))
		{
		    if (slot.getHasStack())
		    {
		    	++craftingTick;
		    	if(craftingTick > 10)
		    	{
		    		if(!aPlayer.inventory.addItemStackToInventory(slot.decrStackSize(127)))
		    		{
		    			craftingTick = 0;
		    			craftBuf = 0;
		    			return super.slotClick(aSlotID, aMouseclick, aShifthold, aPlayer);
		    		}
		    	}
		    }
		    else
		    {
    			craftingTick = 0;
    			craftBuf = 0;
		    }
		    return null;
		}
		return super.slotClick(aSlotID, aMouseclick, aShifthold, aPlayer);
	}
}