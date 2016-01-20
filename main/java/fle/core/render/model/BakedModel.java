package fle.core.render.model;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;

public class BakedModel implements IFlexibleBakedModel
{
	
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getTexture()
	{
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return null;
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing side)
	{
		return null;
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		return null;
	}

	@Override
	public VertexFormat getFormat()
	{
		return null;
	}
}