/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.model;

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
	/**
	 * Should model enable ambient occlusion.
	 */
	@Override
	default boolean isAmbientOcclusion()
	{
		return true;
	}
	
	/**
	 * For a model this property is to determine should switch light on during
	 * rendering model in GUI.
	 */
	@Override
	default boolean isGui3d()
	{
		return false;
	}
	
	/**
	 * Default value is false, for most of model this is not true.
	 */
	@Override
	default boolean isBuiltInRenderer()
	{
		return false;
	}
	
	/**
	 * Get particle texture of model (Seems useless).
	 */
	@Override
	default TextureAtlasSprite getParticleTexture()
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
	
	/**
	 * Get camera transforms.
	 */
	@Override
	default ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}
	
	/**
	 * Get item overrides? (But what is item override?)
	 */
	@Override
	default ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}
}
