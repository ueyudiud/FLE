package farcore.lib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FSlot
{
	public FluidTank tank;
	protected boolean shouldRender = true;
	public int x;
	public int y;
	public int u;
	public int v;
	public int slotNumber;

	public FSlot(FluidTank tank, int x, int y, int u, int v)
	{
		this.tank = tank;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
	
	public FSlot setNoRender()
	{
		shouldRender = false;
		return this;
	}

	public int getCapacity()
	{
		return tank.getCapacity();
	}

	public FluidStack getStackInSlot()
	{
		return tank.getFluid();
	}
	
	public void putStack(FluidStack stack)
	{
		tank.setFluid(stack);
	}

	public void onSlotClick(EntityPlayer player, ItemStack currentStack)
	{

	}

	@SideOnly(Side.CLIENT)
	public void renderSlot(GuiContainerBase gui)
	{
		if(shouldRender)
		{
			gui.drawFluid(x, y, tank.getInfo(), u, v);
		}
	}
	
	public boolean isVisible()
	{
		return true;
	}
}