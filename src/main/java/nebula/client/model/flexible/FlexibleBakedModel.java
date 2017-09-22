/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import nebula.client.model.BakedModelBase;
import nebula.client.model.ICustomItemRenderModel;
import nebula.common.util.A;
import nebula.common.util.L;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public class FlexibleBakedModel implements BakedModelBase, ICustomItemRenderModel, IPerspectiveAwareModel
{
	private final boolean gui3d;
	private final boolean builtIn;
	private final INebulaBakedModelPart[] parts;
	private final TextureAtlasSprite particle;
	private final Function<ItemStack, String>[] itemDataGen;
	private final Function<IBlockState, String>[] blockDataGen;
	private final int[] itemLoadingData;
	private final int[] blockLoadingData;
	private final ImmutableMap<TransformType, TRSRTransformation> transforms;
	/**
	 * The model state marked, if model has crashed during
	 * loading quad, the model will be marked as problem model.
	 * To prevent crashing exception filled logs, the model will
	 * stop to bake quad until model is be reloaded.
	 */
	private boolean errored = false;
	
	public FlexibleBakedModel(ImmutableMap<TransformType, TRSRTransformation> transforms, ImmutableList<INebulaBakedModelPart> parts,
			TextureAtlasSprite particle, boolean gui3d, boolean builtIn,
			Function<ItemStack, String>[] itemDataGen, Function<IBlockState, String>[] blockDataGen,
			int[] itemLoadingData, int[] blockLoadingData)
	{
		this.transforms = transforms;
		this.gui3d = gui3d;
		this.builtIn = builtIn;
		this.parts = L.cast(parts, INebulaBakedModelPart.class);
		this.particle = particle;
		this.itemDataGen = itemDataGen;
		this.blockDataGen = blockDataGen;
		this.itemLoadingData = itemLoadingData;
		this.blockLoadingData = blockLoadingData;
	}
	
	@Override
	public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
	{
		if (this.errored) return ImmutableList.of();
		if (this.itemDataGen == null) return getQuads((IBlockState) null, facing, rand);
		try
		{
			Object[] datas = A.transform(this.itemDataGen, f->f.apply(stack));
			List<BakedQuad> quads = new ArrayList<>();
			for (int i = 0; i < this.parts.length; ++i)
			{
				quads.addAll(this.parts[i].getQuads(facing, this.itemLoadingData[i] == -1 ? NebulaModelLoader.NORMAL : (String) datas[this.itemLoadingData[i]], rand));
			}
			return quads;
		}
		catch (Exception exception)
		{
			FarCore.catching(exception);
			this.errored = true;
			return ImmutableList.of();
		}
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if (this.errored) return ImmutableList.of();
		try
		{
			List<BakedQuad> quads = new ArrayList<>();
			if (this.blockDataGen == null)
			{
				A.executeAll(this.parts, part->quads.addAll(part.getQuads(side, NebulaModelLoader.NORMAL, rand)));
			}
			else
			{
				Object[] datas = A.transform(this.blockDataGen, f->f.apply(state));
				for (int i = 0; i < this.parts.length; ++i)
				{
					quads.addAll(this.parts[i].getQuads(side, this.blockLoadingData[i] == -1 ? NebulaModelLoader.NORMAL : (String) datas[this.blockLoadingData[i]], rand));
				}
			}
			return quads;
		}
		catch (Exception exception)
		{
			FarCore.catching(exception);
			this.errored = true;
			return ImmutableList.of();
		}
	}
	
	@Override
	public boolean isGui3d()
	{
		return this.gui3d;
	}
	
	@Override
	public boolean isBuiltInRenderer()
	{
		return this.builtIn;
	}
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return this.particle;
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, this.transforms, cameraTransformType);
	}
}