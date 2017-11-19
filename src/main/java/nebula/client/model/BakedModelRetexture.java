/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterators;

import nebula.Log;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.blockstate.BlockStateWrapper;
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
		this.parent = basemodel;
	}
	
	protected IBlockState unwrapState(IBlockState state)
	{
		return state instanceof BlockStateTileEntityWapper ? ((BlockStateWrapper) state).getRealState() : state;
	}
	
	protected static <K, V> void apply(Iterable<K> iterable, Builder<V> builder, Function<? super K, ? extends V> function)
	{
		builder.addAll(Iterators.transform(iterable.iterator(), function));
	}
	
	protected abstract void replaceQuads(IBlockState state, ImmutableList.Builder<BakedQuad> newList, List<BakedQuad> oldList);
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		List<BakedQuad> quads = this.parent.getQuads(unwrapState(state), side, rand);
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		try
		{
			replaceQuads(state, builder, quads);
		}
		catch (Exception exception)
		{
			Log.catchingIfDebug(exception);
			return ImmutableList.of();
		}
		return builder.build();
	}
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return this.parent.isAmbientOcclusion();
	}
	
	@Override
	public boolean isGui3d()
	{
		return this.parent.isGui3d();
	}
	
	@Override
	public boolean isBuiltInRenderer()
	{
		return this.parent.isBuiltInRenderer();
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return this.parent.getParticleTexture();
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return this.parent.getItemCameraTransforms();
	}
	
	@Override
	public ItemOverrideList getOverrides()
	{
		return this.parent.getOverrides();
	}
}
