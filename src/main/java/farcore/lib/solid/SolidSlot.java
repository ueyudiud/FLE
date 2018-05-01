/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.solid;

import org.lwjgl.opengl.GL11;

import farcore.lib.inventory.ISolidContainer;
import nebula.client.gui.GuiContainer01Slots;
import nebula.common.gui.ISlot;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class SolidSlot implements ISlot<SolidStack>
{
	public ISolidContainer	container;
	protected boolean		shouldRender	= true;
	public int				x;
	public int				y;
	public int				u;
	public int				v;
	public int				slotNumber;
	public boolean			renderHorizontal;
	
	public SolidSlot(ISolidContainer container, int x, int y, int u, int v)
	{
		this.container = container;
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
		return this.container.getRemainAmountInContainer() + this.container.getStackAmountInContainer();
	}
	
	/**
	 * Get fluid stack current in slot.
	 * 
	 * @return
	 */
	public SolidStack getStack()
	{
		return this.container.getStackInContainer();
	}
	
	/**
	 * Set stack to slot.
	 * 
	 * @param stack
	 */
	public void putStack(SolidStack stack)
	{
		this.container.setStackInContainer(stack);
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
	public void renderSlot(GuiContainer01Slots gui, int x, int y)
	{
		if (this.shouldRender)
		{
			if (this.container.hasStackInContainer())
			{
				SolidStack stack = this.container.getStackInContainer();
				if (stack.getSolid() == null)
				{
					return;//Data not synch.
				}
				TextureAtlasSprite solidIcon = stack.getSolid().getIcon(stack);
				if (solidIcon != null)
				{
					gui.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					int color = stack.getSolid().getColor(stack);
					final float zLevel = 100.0F;
					if (this.renderHorizontal)
					{
						gui.drawRepeated(solidIcon, x + this.x, y + this.y, (double) (stack.amount * this.u) / (double) getCapacity(), this.v, zLevel, color);
					}
					else
					{
						gui.drawRepeated(solidIcon, x + this.x, y + this.y + this.v - (double) (stack.amount * this.v) / (double) getCapacity(), this.u, (double) (stack.amount * this.v) / (double) getCapacity(), zLevel, color);
					}
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
