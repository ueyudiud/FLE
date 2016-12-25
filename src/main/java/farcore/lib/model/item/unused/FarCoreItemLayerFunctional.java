/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import farcore.util.L;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FarCoreItemLayerFunctional extends FarCoreItemLayer
{
	private Function<ItemStack, List<BakedQuad>> function;
	
	public FarCoreItemLayerFunctional(int layer, Function<ItemStack, String> variantFunction, Map<String, List<BakedQuad>> quads, Function<ItemStack, Integer> colorMultiplier)
	{
		super(layer);
		this.colorMultiplier = colorMultiplier;
		this.function = variantFunction.andThen(L.toFunction(quads, ImmutableList.of()));
	}
	public FarCoreItemLayerFunctional(int layer, Function<ItemStack, String> variantFunction, Map<String, TextureAtlasSprite> icons, List<BakedQuad> quads, Function<ItemStack, Integer> colorMultiplier)
	{
		super(layer);
		this.colorMultiplier = colorMultiplier;
		this.function = variantFunction
				.andThen(L.toFunction(icons, Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()))
				.andThen(icon -> Lists.transform(quads, quad -> new BakedQuadRetextured(quad, icon)));
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack)
	{
		return this.function.apply(stack);
	}
}