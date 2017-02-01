/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface INebulaBakedModelPart
{
	static INebulaBakedModelPart bake(BakedQuad...quads)
	{
		return bake(Arrays.asList(quads));
	}
	static INebulaBakedModelPart bake(List<BakedQuad> quads)
	{
		return new WrapedBakedModelPart(quads);
	}
	static INebulaBakedModelPart bake(Function<ItemStack, List<BakedQuad>> function)
	{
		return new WrapedBakedItemStackModelPart(function);
	}
	
	@SideOnly(Side.CLIENT)
	interface INebulaWorldPropBakedModelPart extends INebulaBakedModelPart
	{
		void putQuads(List<BakedQuad> quads, @Nullable EnumFacing facing, IBlockState state, long rand);
	}
	
	@SideOnly(Side.CLIENT)
	interface INebulaItemPropBakedModelPart extends INebulaBakedModelPart
	{
		void putQuads(List<BakedQuad> quads, ItemStack stack);
	}
	
	@SideOnly(Side.CLIENT)
	class WrapedBakedItemStackModelPart implements INebulaItemPropBakedModelPart
	{
		final Function<ItemStack, List<BakedQuad>> applier;
		
		WrapedBakedItemStackModelPart(Function<ItemStack, List<BakedQuad>> applier)
		{
			this.applier = applier;
		}
		
		@Override
		public void putQuads(List<BakedQuad> quads, ItemStack stack)
		{
			quads.addAll(applier.apply(stack));
		}
	}
	
	@SideOnly(Side.CLIENT)
	class WrapedBakedModelPart implements INebulaItemPropBakedModelPart, INebulaWorldPropBakedModelPart
	{
		final List<BakedQuad> quads;
		
		WrapedBakedModelPart(List<BakedQuad> quads)
		{
			this.quads = ImmutableList.copyOf(quads);
		}
		
		@Override
		public void putQuads(List<BakedQuad> quads, @Nullable EnumFacing facing, IBlockState state, long rand)
		{
			quads.addAll(this.quads);
		}
		
		@Override
		public void putQuads(List<BakedQuad> quads, ItemStack stack)
		{
			quads.addAll(this.quads);
		}
		
		public void putQuads(List<BakedQuad> quads)
		{
			quads.addAll(this.quads);
		}
	}
}