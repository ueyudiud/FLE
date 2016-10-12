package farcore.lib.model.block;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.util.BlockStateWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public abstract class BakedModelRetexture implements IBakedModel
{
	protected final IBakedModel parent;

	public BakedModelRetexture(IBakedModel basemodel)
	{
		parent = basemodel;
	}
	
	protected IBlockState unwrapState(IBlockState state)
	{
		return state instanceof BlockStateTileEntityWapper ?
				((BlockStateWrapper) state).getRealState() :
					state;
	}
	
	protected abstract void replaceQuads(IBlockState state, ImmutableList.Builder<BakedQuad> newList, List<BakedQuad> oldList);
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		List<BakedQuad> quads = parent.getQuads(unwrapState(state), side, rand);
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		replaceQuads(state, builder, quads);
		return builder.build();
	}
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return parent.isAmbientOcclusion();
	}
	
	@Override
	public boolean isGui3d()
	{
		return parent.isGui3d();
	}
	
	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return parent.getParticleTexture();
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return parent.getItemCameraTransforms();
	}
	
	@Override
	public ItemOverrideList getOverrides()
	{
		return parent.getOverrides();
	}
}