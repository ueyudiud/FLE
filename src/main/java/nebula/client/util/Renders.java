/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.util;

import javax.annotation.Nullable;

import nebula.Nebula;
import nebula.client.ClientProxy;
import nebula.client.model.ICustomItemModelSelector;
import nebula.client.model.StateMapperExt;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class Renders
{
	public static void registerClientRegister(Object object)
	{
		Nebula.proxy.registerRender(object);
	}
	
	public static void registerCompactModel(Block block, boolean splitFile, String modid, String path, IProperty<?> property, IProperty<?>...properties)
	{
		Nebula.proxy.registerCompactModel(splitFile, block, modid, path, property, properties);
	}
	
	public static void registerColorMultiplier(IBlockColor color, Block...block)
	{
		((ClientProxy) Nebula.proxy).registerColorMultiplier(color, block);
	}
	
	public static void registerColorMultiplier(IItemColor color, Item...item)
	{
		((ClientProxy) Nebula.proxy).registerColorMultiplier(color, item);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerCustomItemModelSelector(Block item, ICustomItemModelSelector selector)
	{
		registerCustomItemModelSelector(Item.getItemFromBlock(item), selector);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerCustomItemModelSelector(Item item, ICustomItemModelSelector selector)
	{
		ModelLoader.setCustomMeshDefinition(item, selector);
		ModelBakery.registerItemVariants(item, selector.getAllowedResourceLocations(item).toArray(new ResourceLocation[0]));
	}
	
	public static void registerBuildInModel(Block block)
	{
		nebula.client.ClientProxy.registerBuildInModel(block);
	}
	
	/**
	 * Register sub model by a mapper and property to split.<p>
	 * This method will register both item model and block model,
	 * they use same block model location.<p>
	 * @param <T> the type of property.
	 * @param mapper the model mapper.
	 * @param block the block for mapper.
	 * @param property the split property, or <tt>null</tt> for only one variant exist.
	 */
	public static <T extends Comparable<T>> void registerCompactModel(StateMapperExt mapper, Block block, @Nullable IProperty<T> property)
	{
		Item item = Item.getItemFromBlock(block);
		IBlockState state = block.getDefaultState();
		if(property != null)
		{
			for (T value : property.getAllowedValues())
			{
				IBlockState state2 = state.withProperty(property, value);
				ModelLoader.setCustomModelResourceLocation(item, block.getMetaFromState(state2), mapper.getLocationFromState(state2));
			}
		}
		else
		{
			ModelLoader.setCustomModelResourceLocation(item, 0, mapper.getLocationFromState(state));
		}
		ModelLoader.setCustomStateMapper(block, mapper);
	}
	
	/**
	 * Register model mapping with each meta of block.
	 * @param mapper the model mapper.
	 * @param block the block to register.
	 * @param metaCount the meta count.
	 */
	public static void registerCompactModel(StateMapperExt mapper, Block block, int metaCount)
	{
		Item item = Item.getItemFromBlock(block);
		for (int i = 0; i < metaCount; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(item, i, mapper.getLocationFromState(block.getStateFromMeta(i)));
		}
		ModelLoader.setCustomStateMapper(block, mapper);
	}
	
	private static final float TEX_PIX_SCALE = 0.00390625F;
	
	public static void drawTexturedModalRect(int x, int y, int u, int v, int w, int h, float zLevel)
	{
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(x + 0, y + h, zLevel).tex((u + 0) * TEX_PIX_SCALE, (v + h) * TEX_PIX_SCALE).endVertex();
		vertexbuffer.pos(x + w, y + h, zLevel).tex((u + w) * TEX_PIX_SCALE, (v + h) * TEX_PIX_SCALE).endVertex();
		vertexbuffer.pos(x + w, y + 0, zLevel).tex((u + w) * TEX_PIX_SCALE, (v + 0) * TEX_PIX_SCALE).endVertex();
		vertexbuffer.pos(x + 0, y + 0, zLevel).tex((u + 0) * TEX_PIX_SCALE, (v + 0) * TEX_PIX_SCALE).endVertex();
		tessellator.draw();
	}
}