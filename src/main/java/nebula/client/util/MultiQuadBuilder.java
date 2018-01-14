/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.util;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import nebula.common.util.L;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class MultiQuadBuilder implements Consumer<BakedQuad>
{
	private final BakedQuadBuilder				builder;
	private int									tindex	= -1;
	private ImmutableList.Builder<BakedQuad>	quadBuilder;
	
	public MultiQuadBuilder(VertexFormat format, IModelModifier modifier)
	{
		this(format, modifier, false);
	}
	
	public MultiQuadBuilder(VertexFormat format, IModelModifier modifier, final boolean flag)
	{
		final TextureAtlasSprite defIcon = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		this.builder = new BakedQuadBuilder(format, modifier, this)
		{
			@Override
			public void startQuad(EnumFacing facing)
			{
				startQuad(facing, MultiQuadBuilder.this.tindex, defIcon);
				if (flag)
				{
					normal(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ());
				}
			}
		};
		this.quadBuilder = ImmutableList.builder();
	}
	
	public void setTIndex(int idx)
	{
		this.tindex = idx;
	}
	
	public BakedQuadBuilder getBuilder()
	{
		return this.builder;
	}
	
	@Override
	public void accept(BakedQuad quad)
	{
		this.quadBuilder.add(quad);
	}
	
	public Map<String, List<BakedQuad>> bake(com.google.common.base.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, IIconCollection handler)
	{
		Map<String, ResourceLocation> map = handler.build();
		List<BakedQuad> bakedQuads = this.quadBuilder.build();
		Map<String, List<BakedQuad>> map2 = Maps.<String, ResourceLocation, List<BakedQuad>> transformValues(map, (com.google.common.base.Function<ResourceLocation, List<BakedQuad>>) loc -> {
			TextureAtlasSprite icon = bakedTextureGetter.apply(loc);
			return ImmutableList.copyOf(Lists.transform(bakedQuads, L.toFunction(BakedQuadRetex::new, icon)));
		});
		return ImmutableMap.copyOf(map2);
	}
}
