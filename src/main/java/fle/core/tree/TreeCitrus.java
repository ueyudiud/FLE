/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.tree.TreeInfo;
import farcore.lib.world.ICalendar;
import farcore.lib.world.instance.CalendarSurface;
import fle.loader.IBF;
import nebula.common.util.L;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class TreeCitrus extends TreeWithFruitLeaves
{
	private TreeGenSimple generator = new TreeGenSimple(this, 0.05F, false);
	
	public TreeCitrus(Mat material)
	{
		super(material);
		this.generator.setTreeLeavesShape(2, 6, 1, 1.2F);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		this.generator.setTreeLogShape(3, 2);
		return this.generator.generateTreeAt(world, x, y, z, random, info);
	}
	
	@Override
	protected ItemStack createFruit(World world, BlockPos pos, IBlockState state)
	{
		return IBF.crop.getSubItem("citrus");
	}
	
	@Override
	protected boolean checkDate(ICalendar calendar, World world)
	{
		if (calendar instanceof CalendarSurface)
		{
			long month = ((CalendarSurface) calendar).monthInYear(world);
			return L.inRange(10, 9, month);
		}
		return false;
	}
}