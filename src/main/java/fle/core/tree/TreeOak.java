/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import fle.loader.IBFS;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.util.L;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

public class TreeOak extends Tree
{
	private final TreeGenClassic	generator1	= new TreeGenClassic(this, 0.04F);
	private final TreeGenBig		generator2	= new TreeGenBig(this, 0.02F);
	
	public TreeOak(Mat material)
	{
		super(material);
	}
	
	@Override
	public List<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, boolean silkTouching, List<ItemStack> list)
	{
		super.getLeavesDrops(world, pos, state, fortune, silkTouching, list);
		if (L.nextInt(12) == 0)
		{
			list.add(IBFS.iCropRelated.getSubItem("acorn"));
		}
		return list;
	}
	
	@Override
	@Optional.Method(modid = FarCore.JEI)
	public void addDropRecipe()
	{
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.blocks[0], 1, -1),
				new AbstractStack[] { EnumToolTypes.AXE.stack() },
				new AbstractStack[] {new BaseStack(ItemMulti.createStack(this.material, MC.log_cutted))},
				new int[][] {{10000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.blocks[0], 1, -1),
				new AbstractStack[] { EnumToolTypes.BIFACE.stack() },
				new AbstractStack[] {new BaseStack(ItemMulti.createStack(this.material, MC.log_cutted))},
				new int[][] {{10000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.blocks[2], 1, -1),
				new AbstractStack[0],
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.branch)),
						new BaseStack(LEAVES_APPLIER1.get()),
						new BaseStack(EnumBlock.sapling.block, 1, this.material.id),
						new BaseStack(IBFS.iCropRelated.getSubItem("acorn"))},
				new int[][] {{1250}, {1427}, null, {10006 / 12}});
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if (info != null && info.map.get("huge") > 0)
		{
			this.generator2.setScale(1.0 + info.height * 0.03125, 1.0, 1.0);
			return this.generator2.generateTreeAt(world, x, y, z, random, info);
		}
		else
		{
			if (info != null)
			{
				this.generator1.setHeight(info.height / 4 + 4, info.height / 3 + 3);
			}
			else
			{
				this.generator1.setHeight(4, 3);
			}
			return this.generator1.generateTreeAt(world, x, y, z, random, info);
		}
	}
}
