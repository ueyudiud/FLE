/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.function.Function;

import nebula.client.model.flexible.INebulaBakedModelPart.BakedModelPart;
import nebula.client.util.BakedQuadBuilder;
import nebula.client.util.IIconCollection;
import nebula.client.util.MultiQuadBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelPartItemLayerFlat extends ModelPartItemLayer
{
	ModelPartItemLayerFlat(ModelPartItemLayer layer)
	{
		super.icon = layer.icon;
		super.index = layer.index;
		super.zOffset = layer.zOffset;
	}
	
	private static final float	POS	= 8.5F / 16F;
	private static final float	NEG	= 7.5F / 16F;
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		MultiQuadBuilder builder = new MultiQuadBuilder(format, null);
		BakedQuadBuilder builder1 = builder.getBuilder();
		builder.setTIndex(this.index);
		builder1.startQuad(EnumFacing.NORTH);
		builder1.pos(0, 0, NEG - 1E-3F * this.index, 0, 16);
		builder1.pos(0, 1, NEG - 1E-3F * this.index, 0, 0);
		builder1.pos(1, 1, NEG - 1E-3F * this.index, 16, 0);
		builder1.pos(1, 0, NEG - 1E-3F * this.index, 16, 16);
		builder1.endQuad();
		builder1.startQuad(EnumFacing.SOUTH);
		builder1.pos(0, 0, POS + 1E-3F * this.index, 0, 16);
		builder1.pos(1, 0, POS + 1E-3F * this.index, 16, 16);
		builder1.pos(1, 1, POS + 1E-3F * this.index, 16, 0);
		builder1.pos(0, 1, POS + 1E-3F * this.index, 0, 0);
		builder1.endQuad();
		return new BakedModelPart(builder.bake(bakedTextureGetter::apply, iconHandlerGetter.apply(this.icon)));
	}
}
