/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid;

import org.lwjgl.opengl.GL11;

import farcore.lib.solid.container.SolidTank;
import nebula.client.gui.GuiContainerBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class SolidSlot
{
	public SolidTank	tank;
	protected boolean	shouldRender	= true;
	public int			x;
	public int			y;
	public int			u;
	public int			v;
	public int			slotNumber;
	public boolean		renderHorizontal;
	
	public SolidSlot(SolidTank tank, int x, int y, int u, int v)
	{
		this.tank = tank;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
	
	public SolidSlot setRenderHorizontal()
	{
		this.renderHorizontal = true;
		return this;
	}
	
	public SolidSlot setNoRender()
	{
		this.shouldRender = false;
		return this;
	}
	
	/**
	 * Get slot capacity.
	 * 
	 * @return
	 */
	public int getCapacity()
	{
		return this.tank.getCapacity();
	}
	
	/**
	 * Get fluid stack current in slot.
	 * 
	 * @return
	 */
	public SolidStack getStackInSlot()
	{
		return this.tank.getStack();
	}
	
	/**
	 * Set stack to slot.
	 * 
	 * @param stack
	 */
	public void putStack(SolidStack stack)
	{
		this.tank.setStack(stack);
	}
	
	/**
	 * Called when player clicked slot.
	 * 
	 * @param player The player.
	 * @param currentStack The current item stack.
	 */
	public void onSlotClick(EntityPlayer player, ItemStack currentStack)
	{
		
	}
	
	/**
	 * Render solid slot into GUI.
	 * 
	 * @param gui
	 */
	@SideOnly(Side.CLIENT)
	public void renderSlot(GuiContainerBase gui)
	{
		if (this.shouldRender)
		{
			if (this.tank.getSolidAmount() > 0)
			{
				SolidStack stack = this.tank.getStack();
				if (stack.getSolid() == null)
					return;//Data not synch.
				TextureAtlasSprite solidIcon = stack.getSolid().getIcon(stack);
				if (solidIcon != null)
				{
					boolean mark = gui.startTranslate();
					gui.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					int color = stack.getSolid().getColor(stack);
					if (this.renderHorizontal)
					{
						gui.drawRepeated(solidIcon, this.x, this.y, (double) (stack.amount * this.u) / (double) this.tank.getCapacity(), this.v, gui.getZLevel(), color);
					}
					else
					{
						gui.drawRepeated(solidIcon, this.x, this.y + this.v - (double) (stack.amount * this.v) / (double) this.tank.getCapacity(), this.u, (double) (stack.amount * this.v) / (double) this.tank.getCapacity(), gui.getZLevel(), color);
					}
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					gui.endTranslate(mark);
				}
			}
		}
	}
	
	/**
	 * Should slot visible to click or do others actions.
	 * 
	 * @return
	 */
	public boolean isVisible()
	{
		return true;
	}
}
