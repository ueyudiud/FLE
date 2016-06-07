package farcore.lib.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import farcore.interfaces.tile.IFluidTanks;
import farcore.lib.gui.GuiBase;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;

public class FluidSlot
{
	private static final List<String> EMPTY = Arrays.asList("Empty");
	
	protected final int index;
	protected boolean clickable = true;
	protected boolean drawLay = false;
	public final IFluidTanks tank;
	public int x;
	public int y;
	public int w;
	public int h;
	public int slotNumber;
    
	public FluidSlot(IFluidTanks tank, int id, int x, int y)
	{
		this(tank, id, x, y, 16, 16);
	}
	public FluidSlot(IFluidTanks tank, int id, int x, int y, int w, int h)
	{
		index = id;
		this.tank = tank;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public FluidSlot setNoClick()
	{
		clickable = false;
		return this;
	}
	
	public FluidSlot setLay()
	{
		drawLay = true;
		return this;
	}
	
	public boolean slotClick(EntityPlayer player, ItemStack stack)
	{
		if(isClickable())
		{
			IFluidTank tank = getTank();
			if(FluidContainerRegistry.isContainer(stack))
			{
				FluidStack stack1 = FluidContainerRegistry.getFluidForFilledItem(stack);
				if(stack1 != null && canInsertFluid(stack, stack1))
				{
					if(tank.fill(stack1, false) == stack1.amount)
					{
						stack.stackSize--;
						if(stack.stackSize == 0)
						{
							player.inventory.setItemStack(null);
						}
						U.Inventorys.givePlayerToContainer(player, FluidContainerRegistry.drainFluidContainer(stack));
						return true;
					}
					return false;
				}
				else if(stack1 == null && canExtractFluid(stack, tank.getFluid()))
				{
					ItemStack stack2 = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), stack);
					if(stack2 != null)
					{
						tank.drain(FluidContainerRegistry.getContainerCapacity(stack), true);
						stack.stackSize--;
						if(stack.stackSize == 0)
						{
							player.inventory.setItemStack(null);
						}
						U.Inventorys.givePlayerToContainer(player, stack2);
						return true;
					}
					return false;
				}
			}
			else if(stack.getItem() instanceof IFluidContainerItem)
			{
				IFluidContainerItem container = (IFluidContainerItem) stack.getItem();
				FluidStack stack1 = container.getFluid(stack);
				if(stack1 != null && canInsertFluid(stack, stack1))
				{
					FluidStack stack2;
					if((stack2 = container.drain(stack, tank.getCapacity() - tank.getFluidAmount(), false)) != null)
					{
						container.drain(stack, tank.fill(stack2, true), true);
						return true;
					}
				}
				if(canExtractFluid(stack, tank.getFluid()))
				{
					int amount;
					if((amount = container.fill(stack, tank.getFluid(), false)) > 0)
					{
						container.fill(stack, tank.drain(amount, true), true);
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	
	public boolean isClickable()
	{
		return clickable;
	}
	
	protected boolean canInsertFluid(ItemStack stack, FluidStack fluid)
	{
		return true;
	}
	
	protected boolean canExtractFluid(ItemStack stack, FluidStack fluid)
	{
		return true;
	}

    /**
     * if par2 has more items than par1, onCrafting(fluid,countIncrease) is called
     */
    public void onSlotChange(FluidStack fluid, FluidStack change)
    {
        if (fluid != null && change != null)
        {
            if (fluid.getFluid() == change.getFluid())
            {
                int i = change.amount - fluid.amount;

                if (i > 0)
                {
                    this.onCrafting(fluid, i);
                }
            }
        }
    }
    
	public void onCrafting(FluidStack fluid, int i) {	}

	public void setStack(FluidStack stack)
	{
		tank.setFluidStackInTank(index, stack);
	}

    /**
     * Helper fnct to get the stack in the slot.
     */
    public FluidStack getStack()
    {
        return getTank().getFluid();
    }
    
    /**
     * Helper fnct to get the stack in the slot.
     */
    public IFluidTank getTank()
    {
        return tank.getTank(index);
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack()
    {
        return getStack() != null;
    }
    
    public void renderFluidInSlot(GuiBase gui)
    {
    	if(getHasStack())
    	{
    		gui.drawFluid(x, y, tank.getTank(index), w, h, drawLay);
    	}
    }
    
    public List<String> getInfo()
    {
    	IFluidTank tank = getTank();
    	if(tank.getFluid() == null)
    	{
    		return EMPTY;
    	}
    	else
    	{
    		List<String> list = new ArrayList();
    		list.add(tank.getFluid().getLocalizedName());
    		list.add("Amount : " + tank.getFluidAmount() + " L");
    		return list;
    	}
    }

    /**
     * Retrieves the index in the inventory for this slot, this value should typically not
     * be used, but can be useful for some occasions.
     *
     * @return Index in associated inventory for this slot.
     */
    public int getSlotIndex()
    {
        return index;
    }
    
	public boolean isRenderInGui()
	{
		return true;
	}
}