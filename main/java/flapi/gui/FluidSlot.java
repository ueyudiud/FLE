package flapi.gui;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import flapi.te.interfaces.IFluidTanks;

public class FluidSlot
{
	protected final int index;
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
    
    public void renderFluidInSlot(GuiContainerBase gui)
    {
    	if(getHasStack())
    		gui.drawFluid(x, y, tank.getTank(index), w, h, drawLay());
    }
    
    protected boolean drawLay()
    {
		return false;
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
}