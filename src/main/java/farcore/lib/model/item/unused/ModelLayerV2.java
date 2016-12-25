/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * What an inconvenient model system created by Mojang...
 * @author ueyudiud
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class ModelLayerV2 extends ModelLayer
{
	private final int layer;
	/**
	 * Covert table.
	 */
	private final Table<String, String, List<BakedQuad>> quads;
	private final Function<ItemStack, String> textureRowFunction;
	private final Function<ItemStack, String> textureColumnFunction;
	private final Function<ItemStack, Integer> colorMultiplier;
	
	public ModelLayerV2(int layer, Table<String, String, List<BakedQuad>> quads, Function<ItemStack, String> textureRowFunction, Function<ItemStack, String> textureColumnFunction, Function<ItemStack, Integer> color)
	{
		this.layer = layer;
		this.textureColumnFunction = textureColumnFunction;
		this.textureRowFunction = textureRowFunction;
		this.quads = quads;
		this.colorMultiplier = color;
	}
	
	@Override
	public int getLayer()
	{
		return this.layer;
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack)
	{
		List<BakedQuad> list = this.quads.get(this.textureRowFunction.apply(stack), this.textureColumnFunction.apply(stack));
		return list != null ? list : ImmutableList.of();
	}
	
	@Override
	public int getColor(ItemStack stack)
	{
		return this.colorMultiplier.apply(stack);
	}
}