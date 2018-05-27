/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.client.render;

import java.util.HashMap;
import java.util.Map;

import fle.api.client.TileEntityItemCustomRender.IRender;
import nebula.client.render.IIconRegister;
import nebula.common.stack.AbstractStack;
import nebula.common.util.L;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
abstract class TileEntityItemRenderRegisteration implements IRender
{
	protected final Map<AbstractStack, ResourceLocation[]> map = new HashMap<>();
	
	public void registerRender(AbstractStack stack, ResourceLocation...locations)
	{
		this.map.put(stack, locations);
	}
	
	protected ResourceLocation[] getResource(ItemStack stack)
	{
		return L.get(this.map, L.toPredicate(AbstractStack::similar, stack));
	}
	
	protected TextureAtlasSprite icon(ResourceLocation location)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
	}
	
	@Override
	public boolean access(ItemStack stack)
	{
		return L.contain(this.map.keySet(), L.toPredicate(AbstractStack::similar, stack));
	}
	
	@Override
	public void registerIcon(IIconRegister register)
	{
		for (ResourceLocation[] locations : this.map.values())
		{
			for (ResourceLocation location : locations)
			{
				register.registerIcon(location);
			}
		}
	}
}
