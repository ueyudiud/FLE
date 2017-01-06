/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.part;

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
public interface IFarCoreBakedModelPart
{
	static IFarCoreBakedModelPart bake(BakedQuad...quads)
	{
		return bake(Arrays.asList(quads));
	}
	static IFarCoreBakedModelPart bake(List<BakedQuad> quads)
	{
		return new WrapedBakedModelPart(quads);
	}
	static IFarCoreBakedModelPart bake(Function<ItemStack, List<BakedQuad>> function)
	{
		return new WrapedBakedItemStackModelPart(function);
	}
	
	@SideOnly(Side.CLIENT)
	interface IFarCoreWorldPropBakedModelPart extends IFarCoreBakedModelPart
	{
		void putQuads(List<BakedQuad> quads, @Nullable EnumFacing facing, IBlockState state, long rand);
	}
	
	@SideOnly(Side.CLIENT)
	interface IFarCoreItemPropBakedModelPart extends IFarCoreBakedModelPart
	{
		void putQuads(List<BakedQuad> quads, ItemStack stack);
	}
	
	@SideOnly(Side.CLIENT)
	class WrapedBakedItemStackModelPart implements IFarCoreItemPropBakedModelPart
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
	class WrapedBakedModelPart implements IFarCoreItemPropBakedModelPart, IFarCoreWorldPropBakedModelPart
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