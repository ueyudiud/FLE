/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import farcore.FarCore;
import farcore.lib.model.ModelHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The baked model.
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FarCoreItemModel implements IPerspectiveAwareModel, ICustomItemRenderModel
{
	private Function<ItemStack, List<BakedQuad>>[] layers;
	private TextureAtlasSprite particle;
	
	public FarCoreItemModel(Function<ItemStack, List<BakedQuad>>[] layers, TextureAtlasSprite particle)
	{
		this.layers = layers;
		this.particle = particle;
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
	{
		if(facing != null) return ImmutableList.of();
		List<BakedQuad> list = new ArrayList();
		try
		{
			for(Function<ItemStack, List<BakedQuad>> layer : this.layers)
			{
				list.addAll(layer.apply(stack));
			}
		}
		catch(Exception exception)
		{
			FarCore.catching(exception);
		}
		return list;
	}
	
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) { return ImmutableList.of(); }
	public boolean isAmbientOcclusion() { return false; }
	public boolean isGui3d() { return false; }
	public boolean isBuiltInRenderer() { return false; }
	public TextureAtlasSprite getParticleTexture() { return this.particle; }
	public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
	public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		return MapWrapper.handlePerspective(this, ModelHelper.ITEM_STANDARD_TRANSFORMS, cameraTransformType);
	}
}