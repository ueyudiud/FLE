/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
abstract class FarCoreItemLayerUnbaked
{
	static final float Z_LEVEL_SCALE = 16F;
	
	protected float zOffset = 0.5F;
	protected int layer;
	List<String> texturePool = new ArrayList();
	Map<String, ResourceLocation> textureSets = new HashMap();
	
	public abstract FarCoreItemLayer bake(VertexFormat format,
			com.google.common.base.Function<ResourceLocation, TextureAtlasSprite> bakeTextureGetter,
			Optional<TRSRTransformation> transform);
}