/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.gui;

import nebula.client.gui.GuiContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Fluid slot type.
 * @author ueyudiud
 *
 */
public abstract class FluidSlot<T extends IFluidTank>
{
	public T tank;
	protected boolean shouldRender = true;
	public int x;
	public int y;
	public int u;
	public int v;
	public int slotNumber;
	public boolean renderHorizontal;
	
	public FluidSlot(T tank, int x, int y, int u, int v)
	{
		this.tank = tank;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
	
	public FluidSlot setRenderHorizontal()
	{
		this.renderHorizontal = true;
		return this;
	}
	
	public FluidSlot setNoRender()
	{
		this.shouldRender = false;
		return this;
	}
	
	/**
	 * Get slot capacity.
	 * @return
	 */
	public int getCapacity()
	{
		return this.tank.getCapacity();
	}
	
	/**
	 * Get fluid stack current in slot.
	 * @return
	 */
	public FluidStack getStackInSlot()
	{
		return this.tank.getFluid();
	}
	
	/**
	 * Set stack to slot.
	 * @param stack
	 */
	public abstract void putStack(FluidStack stack);
	
	/**
	 * Called when player clicked slot.
	 * @param player The player.
	 * @param currentStack The current item stack.
	 */
	public void onSlotClick(EntityPlayer player, ItemStack currentStack)
	{
		
	}
	
	/**
	 * Render fluid slot into GUI.
	 * @param gui
	 */
	@SideOnly(Side.CLIENT)
	public void renderSlot(GuiContainerBase gui)
	{
		if(this.shouldRender)
		{
			gui.drawFluid(this.x, this.y, this.tank.getInfo(), this.u, this.v, this.renderHorizontal);
		}
	}
	
	/**
	 * Should slot visible to click or do others actions.
	 * @return
	 */
	public boolean isVisible()
	{
		return true;
	}
}