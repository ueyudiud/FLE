package flapi.gui;

import flapi.solid.ISolidTanks;
import flapi.solid.SolidStack;

public class SolidSlot
{
	protected final int index;
	public final ISolidTanks tank;
	public int x;
	public int y;
	public int w;
	public int h;
	public int slotNumber;
    
	public SolidSlot(ISolidTanks tank, int id, int x, int y)
	{
		this(tank, id, x, y, 16, 16);
	}
	public SolidSlot(ISolidTanks tank, int id, int x, int y, int w, int h)
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
    public void onSlotChange(SolidStack solid, SolidStack change)
    {
        if (solid != null && change != null)
        {
            if (solid.getSolid() == change.getSolid())
            {
                int i = change.size - solid.size;

                if (i > 0)
                {
                    this.onCrafting(solid, i);
                }
            }
        }
    }
    
	public void onCrafting(SolidStack fluid, int i) {	}

	public void setStack(SolidStack stack)
	{
		tank.setSolidStackInTank(index, stack);
	}
	
    /**
     * Helper fnct to get the stack in the slot.
     */
    public SolidStack getStack()
    {
        return tank.getSolidStackInTank(index);
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack()
    {
        return getStack() != null;
    }
    
    public void renderSolidInSlot(GuiContainerBase gui)
    {
    	if(getHasStack())
    		gui.drawSolid(x, y, tank.getSolidTank(index), w, h, drawLay());
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