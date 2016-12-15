/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public abstract class ModelLayer
{
	public abstract int getLayer();
	
	public abstract List<BakedQuad> getQuads(ItemStack stack);
	
	public abstract int getColor(ItemStack stack);
}