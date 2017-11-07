/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableMap;

import nebula.client.model.flexible.FlexibleModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelHelper
{
	/**
	 * The standard item transformation map, use to transform model.
	 * 
	 * @see net.minecraftforge.client.model.IPerspectiveAwareModel.MapWrapper#handlePerspective(net.minecraft.client.renderer.block.model.IBakedModel,
	 *      ImmutableMap, TransformType)
	 */
	public static final ImmutableMap<TransformType, TRSRTransformation>	ITEM_STANDARD_TRANSFORMS;
	/**
	 * The standard block-like-item transformation map, use to transform model.
	 * 
	 * @see net.minecraftforge.client.model.IPerspectiveAwareModel.MapWrapper#handlePerspective(net.minecraft.client.renderer.block.model.IBakedModel,
	 *      ImmutableMap, TransformType)
	 */
	public static final ImmutableMap<TransformType, TRSRTransformation>	BLOCK_STANDARD_TRANSFORMS;
	
	/**
	 * Create a item model with single layer.
	 * 
	 * @param resource
	 * @return
	 */
	public static IModel makeItemModel(ResourceLocation resource)
	{
		return new FlexibleModel(resource.toString());
	}
	
	/**
	 * Create a TRSRTransformation with data.
	 * 
	 * @return
	 */
	public static TRSRTransformation transformation(float translationX, float translationY, float translationZ, float leftRotX, float leftRotY, float leftRotZ, float leftRotW, float scaleX, float scaleY, float scaleZ, float rightRotX, float rightRotY, float rightRotZ, float rightRotW)
	{
		return new TRSRTransformation(new Vector3f(translationX, translationY, translationZ), new Quat4f(leftRotX, leftRotY, leftRotZ, leftRotW), new Vector3f(scaleX, scaleY, scaleZ), new Quat4f(rightRotX, rightRotY, rightRotZ, rightRotW));
	}
	
	static
	{
		ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
		builder.put(TransformType.NONE, TRSRTransformation.identity());
		builder.put(TransformType.THIRD_PERSON_LEFT_HAND, transformation(0.225F, 0.4125F, 0.2875F, 0.0F, 0.0F, 0.0F, 1.0F, 0.55F, 0.55F, 0.55F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, transformation(0.225F, 0.4125F, 0.2875F, 0.0F, 0.0F, 0.0F, 1.0F, 0.55F, 0.55F, 0.55F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_LEFT_HAND, transformation(0.910625F, 0.24816513F, 0.40617055F, -0.15304594F, -0.6903456F, 0.15304594F, 0.6903456F, 0.68F, 0.68F, 0.68F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, transformation(0.910625F, 0.24816513F, 0.40617055F, -0.15304594F, -0.6903456F, 0.15304594F, 0.6903456F, 0.68F, 0.68F, 0.68F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.HEAD, transformation(1.0F, 0.8125F, 1.4375F, 0.0F, 1.0F, 0.0F, 4.4E-8F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GUI, transformation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GROUND, transformation(0.25F, 0.375F, 0.25F, 0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIXED, TRSRTransformation.identity());
		ITEM_STANDARD_TRANSFORMS = builder.build();
		builder = ImmutableMap.builder();
		builder.put(TransformType.NONE, TRSRTransformation.identity());
		builder.put(TransformType.THIRD_PERSON_LEFT_HAND, transformation(0.23483497F, 0.60772145F, 0.31888893F, 0.5624222F, 0.3036032F, 0.23296294F, 0.7329629F, 0.375F, 0.375F, 0.375F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, transformation(0.23483497F, 0.60772145F, 0.31888893F, 0.5624222F, 0.3036032F, 0.23296294F, 0.7329629F, 0.375F, 0.375F, 0.375F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_LEFT_HAND, transformation(0.78284276F, 0.3F, 0.5F, 0.0F, 0.9238796F, 0.0F, -0.3826834F, 0.4F, 0.4F, 0.4F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, transformation(0.78284276F, 0.3F, 0.5F, 0.0F, 0.9238796F, 0.0F, -0.3826834F, 0.4F, 0.4F, 0.4F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.HEAD, transformation(1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.25F, 0.25F, 0.25F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GUI, transformation(0.94194174F, 0.22936705F, 0.34375006F, -0.1F, 0.9F, 0.23911762F, -0.37F, 0.625F, 0.625F, 0.625F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GROUND, transformation(0.375F, 0.5625F, 0.375F, 0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 0.25F, 0.25F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIXED, transformation(0.25F, 0.25F, 0.25F, 0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 1.0F));
		BLOCK_STANDARD_TRANSFORMS = builder.build();
	}
}
