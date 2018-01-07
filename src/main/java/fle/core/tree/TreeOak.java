/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tree;

import java.util.ArrayList;
import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import fle.loader.IBFS;
import nebula.common.util.L;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TreeOak extends Tree
{
	private final TreeGenClassic	generator1	= new TreeGenClassic(this, 0.04F);
	private final TreeGenBig		generator2	= new TreeGenBig(this, 0.02F);
	
	public TreeOak(Mat material)
	{
		super(material);
	}
	
	@Override
	public ArrayList<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, boolean silkTouching, ArrayList list)
	{
		super.getLeavesDrops(world, pos, state, fortune, silkTouching, list);
		if (L.nextInt(12) == 0)
		{
			list.add(IBFS.iCropRelated.getSubItem("acorn"));
		}
		return list;
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
