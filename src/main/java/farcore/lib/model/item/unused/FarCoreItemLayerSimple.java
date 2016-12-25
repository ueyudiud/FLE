/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FarCoreItemLayerSimple extends FarCoreItemLayer
{
	TextureAtlasSprite icon;
	List<BakedQuad> quads;
	
	public FarCoreItemLayerSimple(int layer, TextureAtlasSprite icon, List<BakedQuad> quads)
	{
		super(layer);
		this.icon = icon;
		this.quads = quads;
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack)
	{
		return this.quads;
	}
	
	@Override
	public int getColor(ItemStack stack)
	{
		return 0xFFFFFFFF;
	}
}