package farcore.lib.model.item.unused;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The
 * @author ueyudiud
 *
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class ModelLayerV1 extends ModelLayer
{
	public static final List<BakedQuad> MISSING_TEXTURE = ImmutableList.of();
	
	private final int layer;
	private final Map<String, List<BakedQuad>> quads;
	private final Function<ItemStack, String> textureKeyFunction;
	private final Function<ItemStack, Integer> colorMultiplier;
	
	public ModelLayerV1(int layer, Map<String, List<BakedQuad>> quads, Function<ItemStack, String> textureKeyFunction, Function<ItemStack, Integer> colorMultiplier)
	{
		this.layer = layer;
		this.quads = quads;
		this.textureKeyFunction = textureKeyFunction;
		this.colorMultiplier = colorMultiplier;
	}
	
	@Override
	public int getLayer()
	{
		return this.layer;
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack)
	{
		return this.quads.getOrDefault(this.textureKeyFunction == null ? FarCoreItemModelLoader.NORMAL : this.textureKeyFunction.apply(stack), MISSING_TEXTURE);
	}
	
	@Override
	public int getColor(ItemStack stack)
	{
		return this.colorMultiplier == null ? 0xFFFFFFFF : this.colorMultiplier.apply(stack);
	}
}