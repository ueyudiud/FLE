/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.tree.TreeInfo;
import farcore.lib.world.ICalendar;
import farcore.lib.world.instance.CalendarSurface;
import fle.loader.IBFS;
import nebula.common.util.L;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class TreeApple extends TreeWithFruitLeaves
{
	private final TreeGenSimple generator = new TreeGenSimple(this, 0.03F, false);
	
	public TreeApple(Mat material)
	{
		super(material);
		this.generator.setTreeLeavesShape(2, 6, 1, 2.0F);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if (info != null)
		{
			this.generator.setTreeLogShape(4 + info.height / 3, 2 + info.height / 2);
		}
		else
		{
			this.generator.setTreeLogShape(4, 2);
		}
		return this.generator.generateTreeAt(world, x, y, z, random, info);
	}
	
	@Override
	protected ItemStack createFruit(World world, BlockPos pos, IBlockState state)
	{
		return IBFS.iCropRelated.getSubItem("apple");
	}
	
	@Override
	protected boolean canGrowFruit(ICalendar calendar, World world)
	{
		if (calendar instanceof CalendarSurface)
		{
			long month = ((CalendarSurface) calendar).monthInYear(world);
			return L.inRange(10, 6, month);
		}
		return false;
	}
	
	@Override
	protected boolean canMatureFruit(ICalendar calendar, World world)
	{
		if (calendar instanceof CalendarSurface)
		{
			long month = ((CalendarSurface) calendar).monthInYear(world);
			return L.inRange(10, 8, month);
		}
		return false;
	}
}
