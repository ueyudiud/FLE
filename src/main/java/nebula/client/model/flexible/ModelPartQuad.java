/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import nebula.client.model.flexible.INebulaBakedModelPart.BakedModelPart;
import nebula.client.util.BakedQuadBuilder;
import nebula.client.util.CoordTransformer;
import nebula.client.util.IIconCollection;
import nebula.client.util.IModelModifier;
import nebula.client.util.MultiQuadBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public class ModelPartQuad implements INebulaModelPart
{
	int tintindex;
	
	boolean renderTwoFace;
	
	float x, y, z, w, h;
	CoordTransformer transformer = new CoordTransformer();
	
	float textureMinU, textureMaxU, textureMinV, textureMaxV;
	
	float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
	
	private String icon;
	
	@Override
	public Collection<String> getResources()
	{
		return ImmutableList.of(this.icon);
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		IModelModifier modifier = new ModelModifierByCoordTransformer(transformation, this.transformer);
		MultiQuadBuilder builder = new MultiQuadBuilder(format, modifier);
		builder.setTIndex(this.tintindex);
		BakedQuadBuilder builder1 = builder.getBuilder();
		
		builder1.startQuad(modifier.rotateFacing(EnumFacing.UP));
		builder1.normal(0, 1, 0);
		builder1.color(this.red, this.green, this.blue, this.alpha);
		builder1.pos(this.x         , this.y, this.z         , this.textureMinU, this.textureMinV);
		builder1.pos(this.x         , this.y, this.z + this.h, this.textureMinU, this.textureMaxV);
		builder1.pos(this.x + this.w, this.y, this.z + this.h, this.textureMaxU, this.textureMaxV);
		builder1.pos(this.x + this.w, this.y, this.z         , this.textureMaxU, this.textureMinV);
		builder1.endQuad();
		if (this.renderTwoFace)
		{
			builder1.startQuad(modifier.rotateFacing(EnumFacing.DOWN));
			builder1.normal(0, -1, 0);
			builder1.color(this.red, this.green, this.blue, this.alpha);
			builder1.pos(this.x         , this.y, this.z         , this.textureMinU, this.textureMinV);
			builder1.pos(this.x + this.w, this.y, this.z         , this.textureMaxU, this.textureMinV);
			builder1.pos(this.x + this.w, this.y, this.z + this.h, this.textureMaxU, this.textureMaxV);
			builder1.pos(this.x         , this.y, this.z + this.h, this.textureMinU, this.textureMaxV);
			builder1.endQuad();
		}
		return new BakedModelPart(builder.bake(bakedTextureGetter::apply, iconHandlerGetter.apply(this.icon)));
	}
}