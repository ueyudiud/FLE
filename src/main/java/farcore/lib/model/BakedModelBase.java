/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface BakedModelBase extends IBakedModel
{
	@Override
	default boolean isAmbientOcclusion()
	{
		return true;
	}
	
	@Override
	default boolean isGui3d()
	{
		return false;
	}
	
	@Override
	default boolean isBuiltInRenderer()
	{
		return false;
	}
	
	@Override
	default TextureAtlasSprite getParticleTexture()
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
	
	@Override
	default ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}
	
	@Override
	default ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}
}