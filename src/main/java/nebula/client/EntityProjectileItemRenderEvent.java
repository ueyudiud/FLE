/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client;

import nebula.client.ClientEvent.RenderEvent;
import nebula.common.entity.EntityProjectileItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@HasResult
@SideOnly(Side.CLIENT)
public class EntityProjectileItemRenderEvent extends RenderEvent
{
	private float					partialTicks;
	private EntityProjectileItem	projectileItem;
	
	public EntityProjectileItemRenderEvent(EntityProjectileItem item, RenderManager manager, float partialTicks)
	{
		super();
		this.partialTicks = partialTicks;
		this.projectileItem = item;
		this.manager = manager;
	}
	
	public EntityProjectileItem getProjectileItem()
	{
		return this.projectileItem;
	}
	
	public ItemStack getStack()
	{
		return this.projectileItem.currentItem;
	}
	
	public float yaw()
	{
		return this.projectileItem.prevRotationYaw + (this.projectileItem.rotationYaw - this.projectileItem.prevRotationYaw) * this.partialTicks;
	}
	
	public float pitch()
	{
		return this.projectileItem.prevRotationPitch + (this.projectileItem.rotationPitch - this.projectileItem.prevRotationPitch) * this.partialTicks;
	}
}
