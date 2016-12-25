/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.List;
import java.util.function.Function;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public abstract class FarCoreItemLayer
{
	public final int layer;
	protected Function<ItemStack, Integer> colorMultiplier;
	
	public FarCoreItemLayer(int layer)
	{
		this.layer = layer;
	}
	
	public abstract List<BakedQuad> getQuads(ItemStack stack);
	
	public int getColor(ItemStack stack)
	{
		return this.colorMultiplier == null ? 0xFFFFFFFF : this.colorMultiplier.apply(stack);
	}
}