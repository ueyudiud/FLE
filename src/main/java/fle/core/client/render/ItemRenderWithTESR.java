/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.client.render;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import nebula.client.model.ICustomItemRenderModel;
import nebula.client.render.IIconRegister;
import nebula.client.render.IItemCustomRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ItemRenderWithTESR implements IItemCustomRender
{
	public static final ItemRenderWithTESR INSTANCE = new ItemRenderWithTESR();
	
	private RenderItem render;
	
	@Override
	public void registerIcon(IIconRegister register)
	{
		this.render = Minecraft.getMinecraft().getRenderItem();
	}
	
	@Override
	public void renderItemStack(ItemStack stack)
	{
		IBakedModel model = this.render.getItemModelMesher().getItemModel(stack);
		GL11.glPopMatrix();// For RenderItem will translate matrix again.
		this.render.renderItem(stack, new Wrapper(model));
		GL11.glPushMatrix();// Repush matrix.
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		ForgeHooksClient.renderTileItem(stack.getItem(), stack.getMetadata());
	}
	
	private class Wrapper implements IBakedModel, ICustomItemRenderModel, IPerspectiveAwareModel
	{
		IBakedModel model;
		
		Wrapper(IBakedModel model)
		{
			this.model = model;
		}
		
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
		{
			Pair<? extends IBakedModel, Matrix4f> pair;
			if (this.model instanceof IPerspectiveAwareModel)
			{
				pair = ((IPerspectiveAwareModel) this.model).handlePerspective(cameraTransformType);
				pair = Pair.of(new Wrapper(pair.getLeft()), pair.getRight());
			}
			else
			{
				Matrix4f matrix = new Matrix4f();
				matrix.setIdentity();
				pair = Pair.of(this, matrix);
			}
			return pair;
		}
		
		@Override
		public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
		{
			return this.model instanceof ICustomItemRenderModel ? ((ICustomItemRenderModel) this.model).getQuads(stack, facing, rand) : this.model.getQuads(null, facing, rand);
		}
		
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			return this.model.getQuads(state, side, rand);
		}
		
		@Override
		public boolean isAmbientOcclusion()
		{
			return this.model.isAmbientOcclusion();
		}
		
		@Override
		public boolean isGui3d()
		{
			return this.model.isGui3d();
		}
		
		@Override
		public boolean isBuiltInRenderer()
		{
			return false;
		}
		
		@Override
		public TextureAtlasSprite getParticleTexture()
		{
			return this.model.getParticleTexture();
		}
		
		@Override
		public ItemCameraTransforms getItemCameraTransforms()
		{
			return this.model.getItemCameraTransforms();
		}
		
		@Override
		public ItemOverrideList getOverrides()
		{
			return this.model.getOverrides();
		}
	}
}
