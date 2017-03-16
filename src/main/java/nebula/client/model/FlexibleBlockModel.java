/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class FlexibleBlockModel implements IPerspectiveAwareModel
{
	boolean ao;
	private TextureAtlasSprite particle;
	List<INebulaBakedModelPart> parts;
	
	public FlexibleBlockModel(boolean ao, List<INebulaBakedModelPart> parts, TextureAtlasSprite particle)
	{
		this.ao = ao;
		this.parts = ImmutableList.copyOf(parts);
		this.particle = particle == null ? Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite() : particle;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, @Nullable EnumFacing side, long rand)
	{
		List<BakedQuad> list = new ArrayList<>();
		this.parts.forEach(part->INebulaBakedModelPart.putQuads(list, part, state, side, rand));
		return list;
	}
	
	public boolean isAmbientOcclusion() { return this.ao; }
	public boolean isGui3d() { return true; }
	public boolean isBuiltInRenderer() { return false; }
	public TextureAtlasSprite getParticleTexture() { return this.particle; }
	public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
	public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		return MapWrapper.handlePerspective(this, ModelHelper.BLOCK_STANDARD_TRANSFORMS, cameraTransformType);
	}
}