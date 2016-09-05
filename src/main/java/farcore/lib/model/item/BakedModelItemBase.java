package farcore.lib.model.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedModelItemBase implements ICustomItemRenderModel
{
	/**
	 * Model layer provider.
	 */
	private IModelLayerProvider provider;
	/**
	 * Icon model applier.
	 */
	private Map<String, IModelIconApplier> appliers;
	/**
	 * Texture map.
	 */
	private Map<String, List<BakedQuad>> quads;

	private TextureAtlasSprite particle;
	private List<BakedQuad> particleQuad;
	private ItemOverrideList overrideList;

	BakedModelItemBase(IModelLayerProvider provider, Map<String, IModelIconApplier> appliers,
			Map<String, List<BakedQuad>> quads, TextureAtlasSprite particle, List<BakedQuad> particleQuad,
			ItemOverrideList overrideList)
	{
		this.provider = provider;
		this.appliers = appliers;
		this.quads = quads;
		this.particle = particle;
		this.particleQuad = particleQuad;
		this.overrideList = overrideList;
	}

	@Override
	public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
	{
		List<BakedQuad> result = new ArrayList();
		int length = provider.getRenderPasses();
		for(int i = 0; i < length; ++i)
		{
			String phase = provider.getModelLayer(i, stack);
			if(appliers.containsKey(phase))
			{
				phase = appliers.get(phase).applyModelPhase(stack);
			}
			List<BakedQuad> re1 = quads.get(phase);
			if(re1 != null)
			{
				result.addAll(re1);
			}
		}
		return result;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {return side == null ? particleQuad : ImmutableList.of();}
	@Override
	public boolean isAmbientOcclusion() {return true;}
	@Override
	public boolean isGui3d() {return false;}
	@Override
	public boolean isBuiltInRenderer() {return false;}
	@Override
	public TextureAtlasSprite getParticleTexture() {return particle;}
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {return ItemCameraTransforms.DEFAULT;}
	@Override
	public ItemOverrideList getOverrides() {return overrideList;}
}