package farcore.lib.model;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.lib.model.item.FarCoreItemModelLoader;
import farcore.lib.model.item.ModelItemBase;
import farcore.lib.model.item.ModelLayer;
import farcore.lib.model.item.ModelLayerV1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHelper
{
	public static final ImmutableMap<TransformType, TRSRTransformation> ITEM_STANDARD_TRANSFORMS;
	public static final ImmutableMap<TransformType, TRSRTransformation> BLOCK_STANDARD_TRANSFORMS;
	
	public static TextureAtlasSprite getIcon(ResourceLocation location)
	{
		return location == null ?
				Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite() :
					Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
	}
	
	public static void addCuboid(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			int u1, int v1, int u2, int v2,
			EnumFacing facing, TextureAtlasSprite icon,
			int color, IntBuffer buffer, List<BakedQuad>[] facequads, List<BakedQuad> generalquads, VertexFormat format)
	{
		float w = icon.getMaxU() - icon.getMinU();
		float h = icon.getMaxV() - icon.getMinV();
		float ua = icon.getMinU() + w * u1 / 16F;
		float va = icon.getMinU() + h * v1 / 16F;
		float ub = icon.getMinU() + w * u2 / 16F;
		float vb = icon.getMinU() + h * v2 / 16F;
		addCuboid(x1, y1, z1, x2, y2, z2, ua, va, ub, vb, facing, color, buffer, facequads, generalquads, format);
	}
	public static void addCuboid(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float u1, float v1, float u2, float v2,
			EnumFacing facing,
			int color, IntBuffer buffer, List<BakedQuad>[] facequads, List<BakedQuad> generalquads, VertexFormat format)
	{
		BakedQuad quad;
		if(x1 != x2 && z1 != z2)
		{
			buffer.rewind();
			addFace(x1, y1, z2, x2, y1, z2, x2, y1, z1, x1, y1, z1, u1, v1, u2, v2, EnumFacing.DOWN, color, buffer);
			quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.DOWN, null, true, format);
			facequads[0].add(quad);
			buffer.rewind();
			addFace(x1, y2, z1, x2, y2, z1, x2, y2, z2, x1, y2, z2, u1, v1, u2, v2, EnumFacing.UP, color, buffer);
			quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.UP, null, true, format);
			facequads[1].add(quad);
		}
		if(x1 != x2 && y1 != y2)
		{
			buffer.rewind();
			addFace(x2, y2, z1, x1, y2, z1, x1, y1, z1, x2, y1, z1, u1, v1, u2, v2, EnumFacing.NORTH, color, buffer);
			quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.NORTH, null, true, format);
			facequads[2].add(quad);
			buffer.rewind();
			addFace(x1, y2, z2, x2, y2, z2, x2, y1, z2, x1, y1, z1, u1, v1, u2, v2, EnumFacing.SOUTH, color, buffer);
			quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.SOUTH, null, true, format);
			facequads[3].add(quad);
		}
		if(z1 != z2 && y1 != y2)
		{
			buffer.rewind();
			addFace(x1, y2, z1, x2, y2, z1, x2, y1, z2, x1, y1, z1, u1, v1, u2, v2, EnumFacing.WEST, color, buffer);
			quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.WEST, null, true, format);
			facequads[4].add(quad);
			buffer.rewind();
			addFace(x2, y2, z2, x2, y2, z1, x2, y1, z1, x2, y1, z2, u1, v1, u2, v2, EnumFacing.EAST, color, buffer);
			quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.EAST, null, true, format);
			facequads[5].add(quad);
		}
	}
	public static void addCuboid(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float xOffset, float yOffset, float zOffset,
			float xRot, float yRot, float zRot,
			float xTranslate, float yTranslate, float zTranslate,
			int u1, int v1, int u2, int v2,
			EnumFacing facing, TextureAtlasSprite icon,
			int color, IntBuffer buffer, List<BakedQuad>[] facequads, List<BakedQuad> generalquads, VertexFormat format)
	{
		float w = icon.getMaxU() - icon.getMinU();
		float h = icon.getMaxV() - icon.getMinV();
		float ua = icon.getMinU() + w * u1 / 16F;
		float va = icon.getMinU() + h * v1 / 16F;
		float ub = icon.getMinU() + w * u2 / 16F;
		float vb = icon.getMinU() + h * v2 / 16F;
		addCuboid(x1, y1, z1, x2, y2, z2,
				xOffset, yOffset, zOffset, xRot, yRot, zRot, xTranslate, yTranslate, zTranslate,
				ua, va, ub, vb, facing, color, buffer, facequads, generalquads, format);
	}
	public static void addCuboid(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float xOffset, float yOffset, float zOffset,
			float xRot, float yRot, float zRot,
			float xTranslate, float yTranslate, float zTranslate,
			float u1, float v1, float u2, float v2,
			EnumFacing facing,
			int color, IntBuffer buffer, List<BakedQuad>[] facequads, List<BakedQuad> generalquads, VertexFormat format)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(xOffset, yOffset, zOffset));
		matrix.rotate((float) Math.PI, new Vector3f(xRot, yRot, zRot));
		matrix.translate(new Vector3f(xTranslate-xOffset, yTranslate-yOffset, zTranslate-zOffset));
		Vector4f vector4f1;
		Vector4f vector4f2;
		Matrix4f.transform(matrix, vector4f1 = new Vector4f(x1, y1, z1, 0), vector4f1);
		Matrix4f.transform(matrix, vector4f2 = new Vector4f(x1, y1, z1, 0), vector4f2);
		addCuboid(
				vector4f1.x, vector4f1.y, vector4f1.z,
				vector4f2.x, vector4f1.y, vector4f1.z,
				vector4f2.x, vector4f1.y, vector4f2.z,
				vector4f1.x, vector4f1.y, vector4f2.z,
				vector4f1.x, vector4f2.y, vector4f2.z,
				vector4f2.x, vector4f2.y, vector4f2.z,
				vector4f2.x, vector4f2.y, vector4f1.z,
				vector4f1.x, vector4f2.y, vector4f1.z,
				u1, v1, u2, v2, color, buffer, facequads, generalquads, format);
	}
	public static void addCuboid(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3,
			float x4, float y4, float z4,
			float x5, float y5, float z5,
			float x6, float y6, float z6,
			float x7, float y7, float z7,
			float x8, float y8, float z8,
			float u1, float v1, float u2, float v2,
			int color, IntBuffer buffer,
			List<BakedQuad>[] facequads, List<BakedQuad> generalquads, VertexFormat format)
	{
		buffer.rewind();
		addFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, EnumFacing.DOWN, color, buffer);
		BakedQuad quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.DOWN, null, true, format);
		generalquads.add(quad);
		buffer.rewind();
		addFace(x5, y5, z5, x6, y6, z6, x7, y7, z7, x8, y8, z8, u1, v1, u2, v2, EnumFacing.UP, color, buffer);
		quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.UP, null, true, format);
		generalquads.add(quad);
		buffer.rewind();
		addFace(x8, y8, z8, x5, y5, z5, x1, y1, z1, x2, y2, z2, u1, v1, u2, v2, EnumFacing.NORTH, color, buffer);
		quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.NORTH, null, true, format);
		generalquads.add(quad);
		buffer.rewind();
		addFace(x6, y6, z6, x7, y7, z7, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, EnumFacing.SOUTH, color, buffer);
		quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.SOUTH, null, true, format);
		generalquads.add(quad);
		buffer.rewind();
		addFace(x5, y5, z5, x6, y6, z6, x5, y5, z5, x1, y1, z1, u1, v1, u2, v2, EnumFacing.WEST, color, buffer);
		quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.WEST, null, true, format);
		generalquads.add(quad);
		buffer.rewind();
		addFace(x7, y7, z7, x8, y8, z8, x2, y2, z2, x3, y3, z3, u1, v1, u2, v2, EnumFacing.EAST, color, buffer);
		quad = new BakedQuad(Arrays.copyOf(buffer.array(), buffer.position()), -1, EnumFacing.EAST, null, true, format);
		generalquads.add(quad);
	}
	public static void addFace(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3,
			float x4, float y4, float z4,
			int u1, int v1, int u2, int v2,
			EnumFacing facing, TextureAtlasSprite icon,
			int color, IntBuffer buffer)
	{
		addFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, facing, icon, color, color, color, color, buffer);
	}
	public static void addFace(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3,
			float x4, float y4, float z4,
			float u1, float v1, float u2, float v2,
			EnumFacing facing,
			int color, IntBuffer buffer)
	{
		addFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, u1, v1, u2, v2, facing, color, color, color, color, buffer);
	}
	public static void addFace(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3,
			float x4, float y4, float z4,
			int u1, int v1, int u2, int v2,
			EnumFacing facing, TextureAtlasSprite icon,
			int color1, int color2, int color3, int color4, IntBuffer buffer)
	{
		float w = icon.getMaxU() - icon.getMinU();
		float h = icon.getMaxV() - icon.getMinV();
		float ua = icon.getMinU() + w * u1 / 16F;
		float va = icon.getMinU() + h * v1 / 16F;
		float ub = icon.getMinU() + w * u2 / 16F;
		float vb = icon.getMinU() + h * v2 / 16F;
		addFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4,
				ua, va, ub, vb, facing, color1, color2, color3, color4, buffer);
	}
	public static void addFace(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3,
			float x4, float y4, float z4,
			float u1, float v1, float u2, float v2,
			EnumFacing facing,
			int color1, int color2, int color3, int color4, IntBuffer buffer)
	{
		addVertex(x1, y1, z1, u1, v1, facing, color1, buffer);
		addVertex(x2, y2, z2, u2, v1, facing, color2, buffer);
		addVertex(x3, y3, z3, u2, v2, facing, color3, buffer);
		addVertex(x4, y4, z4, u1, v2, facing, color4, buffer);
	}
	public static void addVertex(float x, float y, float z, float u, float v, EnumFacing facing, int color, IntBuffer buffer)
	{
		buffer.put(Float.floatToIntBits(x));
		buffer.put(Float.floatToIntBits(y));
		buffer.put(Float.floatToIntBits(z));
		buffer.put(facing.ordinal());
		buffer.put(Float.floatToIntBits(u));
		buffer.put(Float.floatToIntBits(v));
		buffer.put(color);
	}
	
	public static IModel makeItemModel(String textureName)
	{
		return new ModelSurface(new ResourceLocation(textureName));
	}
	
	public static IModel makeItemModel(ResourceLocation location)
	{
		return new ModelSurface(location);
	}
	
	private static class ModelSurface implements IModel
	{
		private ResourceLocation location;
		public ModelSurface(ResourceLocation location) { this.location = location; }
		@Override
		public Collection<ResourceLocation> getTextures() { return ImmutableList.of(this.location); }
		@Override
		public Collection<ResourceLocation> getDependencies() { return ImmutableList.of(); }
		@Override
		public IModelState getDefaultState() { return TRSRTransformation.identity(); }
		@Override
		public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			TextureAtlasSprite icon = bakedTextureGetter.apply(this.location);
			return new ModelItemBase.BakedModelItemBase(new ModelLayer[]{new ModelLayerV1(0, ImmutableMap.of(FarCoreItemModelLoader.NORMAL, ItemLayerModel.getQuadsForSprite(0, icon, format, Optional.of(TRSRTransformation.identity()))), FarCoreItemModelLoader.NORMAL_FUNCTION, FarCoreItemModelLoader.NORMAL_MULTIPLIER)}, icon);
		}
	}
	
	public static TRSRTransformation transformation(
			float translationX, float translationY, float translationZ,
			float leftRotX, float leftRotY, float leftRotZ, float leftRotW,
			float scaleX, float scaleY, float scaleZ,
			float rightRotX, float rightRotY, float rightRotZ, float rightRotW)
	{
		return new TRSRTransformation(new javax.vecmath.Vector3f(translationX, translationY, translationZ), new Quat4f(leftRotX, leftRotY, leftRotZ, leftRotW), new javax.vecmath.Vector3f(scaleX, scaleY, scaleZ), new Quat4f(rightRotX, rightRotY, rightRotZ, rightRotW));
	}
	
	static
	{
		ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
		builder.put(TransformType.NONE, TRSRTransformation.identity());
		builder.put(TransformType.THIRD_PERSON_LEFT_HAND,  transformation(0.225F,    0.4125F,     0.2875F,     0.0F,         0.0F,        0.0F,        1.0F,       0.55F, 0.55F, 0.55F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, transformation(0.225F,    0.4125F,     0.2875F,     0.0F,         0.0F,        0.0F,        1.0F,       0.55F, 0.55F, 0.55F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_LEFT_HAND,  transformation(0.910625F, 0.24816513F, 0.40617055F, -0.15304594F, -0.6903456F, 0.15304594F, 0.6903456F, 0.68F, 0.68F, 0.68F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, transformation(0.910625F, 0.24816513F, 0.40617055F, -0.15304594F, -0.6903456F, 0.15304594F, 0.6903456F, 0.68F, 0.68F, 0.68F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.HEAD,                    transformation(1.0F,      0.8125F,     1.4375F,     0.0F,         1.0F,        0.0F,        4.4E-8F,    1.0F,  1.0F,  1.0F,  0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GUI,                     transformation(0.0F,      0.0F,        0.0F,        0.0F,         0.0F,        0.0F,        1.0F,       1.0F,  1.0F,  1.0F,  0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GROUND,                  transformation(0.25F,     0.375F,      0.25F,       0.0F,         0.0F,        0.0F,        1.0F,       0.5F,  0.5F,  0.5F,  0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIXED, TRSRTransformation.identity());
		ITEM_STANDARD_TRANSFORMS = builder.build();
		builder = ImmutableMap.builder();
		builder.put(TransformType.NONE, TRSRTransformation.identity());
		builder.put(TransformType.THIRD_PERSON_LEFT_HAND,  transformation(0.23483497F, 0.60772145F, 0.31888893F, 0.5624222F, 0.3036032F, 0.23296294F, 0.7329629F, 0.375F, 0.375F, 0.375F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, transformation(0.23483497F, 0.60772145F, 0.31888893F, 0.5624222F, 0.3036032F, 0.23296294F, 0.7329629F, 0.375F, 0.375F, 0.375F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_LEFT_HAND,  transformation(0.78284276F, 0.3F,        0.5F,        0.0F,       0.9238796F, 0.0F,       -0.3826834F, 0.4F,   0.4F,   0.4F,   0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, transformation(0.78284276F, 0.3F,        0.5F,        0.0F,       0.9238796F, 0.0F,       -0.3826834F, 0.4F,   0.4F,   0.4F,   0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.HEAD,                    transformation(1.0F,        1.0F,        1.0F,        0.0F,       1.0F,       0.0F,        0.0F,       0.25F,  0.25F,  0.25F,  0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GUI,                     transformation(0.94194174F, 0.22936705F, 0.34375006F,-0.1F,       0.9F,       0.23911762F,-0.37F,      0.625F, 0.625F, 0.625F, 0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.GROUND,                  transformation(0.375F,      0.5625F,     0.375F,      0.0F,       0.0F,       0.0F,        1.0F,       0.25F,  0.25F,  0.25F,  0.0F, 0.0F, 0.0F, 1.0F));
		builder.put(TransformType.FIXED,                   transformation(0.25F,       0.25F,       0.25F,       0.0F,       0.0F,       0.0F,        1.0F,       0.5F,   0.5F,   0.5F,   0.0F, 0.0F, 0.0F, 1.0F));
		BLOCK_STANDARD_TRANSFORMS = builder.build();
	}
}