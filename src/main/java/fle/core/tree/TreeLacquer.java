/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumToolTypes;
import farcore.lib.bio.BioData;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import fle.api.tile.ILogProductionCollector;
import fle.core.items.ItemSimpleFluidContainer;
import fle.loader.IBFS;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.block.IBlockStateRegister;
import nebula.common.data.Misc;
import nebula.common.item.ItemFluidDisplay;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.Game;
import nebula.common.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TreeLacquer extends Tree
{
	final static IProperty<Integer>		COLLECTING	= Properties.create("collecting", 0, 7);
	final static IProperty<Direction>	COL_FACING	= Misc.PROP_DIRECTION_HORIZONTALS;
	
	private final TreeGenSimple generator1 = new TreeGenSimple(this, 0.05F, false);
	
	public TreeLacquer(Mat material)
	{
		super(material);
		this.generators.add(this.generator1);
		this.generator1.setTreeLeavesShape(4, 13, 2, 2.5F);
		this.leavesCheckRange = 6;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "log/" + this.material.name + "_n", COL_FACING);
		Renders.registerCompactModel(mapper, this.blocks[0], null);
		mapper = new StateMapperExt(this.material.modid, "log/" + this.material.name, null);
		Renders.registerCompactModel(mapper, this.blocks[1], null);
		mapper = new StateMapperExt(this.material.modid, "leaves", null, net.minecraft.block.BlockLeaves.CHECK_DECAY);
		mapper.setVariants("type", this.material.name);
		Renders.registerCompactModel(mapper, this.blocks[2], null);
		Renders.registerCompactModel(mapper, this.blocks[3], null);
		Game.registerBiomeColorMultiplier(this.blocks[2]);
		Game.registerBiomeColorMultiplier(this.blocks[3]);
	}
	
	@Override
	public BlockStateContainer createLogStateContainer(Block block, boolean isArt)
	{
		return isArt ? new BlockStateContainer(block, BlockLog.LOG_AXIS) : new BlockStateContainer(block, COL_FACING, COLLECTING, BlockLog.LOG_AXIS);
	}
	
	@Override
	public void registerLogExtData(Block block, boolean isArt, IBlockStateRegister register)
	{
		if (isArt)
			super.registerLogExtData(block, true, register);
		else
			register.registerStates(block, COL_FACING, COLLECTING, BlockLog.LOG_AXIS);
	}
	
	@Override
	public boolean tickLogUpdate()
	{
		return true;
	}
	
	@Override
	public void updateLog(World world, BlockPos pos, Random rand, boolean isArt)
	{
		if (!isArt)
		{
			IBlockState state = world.getBlockState(pos);
			int c, c1;
			switch (c1 = c = state.getValue(COLLECTING))
			{
			case 0:
				return;
			case 1:
			case 2:
			case 3:
			{
				c++;
				tryFillLacquer(world, pos, state);
			}
			break;
			case 4:
			case 5:
			case 6:
				if (rand.nextInt(3) == 0)
				{
					c++;
				}
				break;
			case 7:
				if (rand.nextInt(8) == 0) c = 0;
			}
			if (c != c1)
			{
				world.setBlockState(pos, state.withProperty(COLLECTING, c));
			}
		}
		super.updateLog(world, pos, rand, isArt);
	}
	
	private void tryFillLacquer(World world, BlockPos pos, IBlockState state)
	{
		Direction direction = state.getValue(COL_FACING);
		TileEntity tile = world.getTileEntity(direction.offset(pos));
		if (tile instanceof ILogProductionCollector)
		{
			ILogProductionCollector collector = (ILogProductionCollector) tile;
			collector.collectLogProductFrom(direction.opposite(), new FluidStack(IBFS.fLacquer, 75));
		}
	}
	
	@Override
	public ActionResult<Float> onToolClickLog(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt)
	{
		if (!isArt)
		{
			if (tool == EnumToolTypes.BIFACE || tool == EnumToolTypes.KNIFE)
			{
				IBlockState state = world.getBlockState(pos);
				if (state.getValue(COLLECTING) == 0)
				{
					world.setBlockState(pos, state.withProperty(COLLECTING, 1).withProperty(COL_FACING, side));
					return new ActionResult<>(EnumActionResult.SUCCESS, 0.25F);
				}
			}
		}
		return super.onToolClickLog(player, tool, level, stack, world, pos, side, hitX, hitY, hitZ, isArt);
	}
	
	@Override
	@Optional.Method(modid = FarCore.JEI)
	public void addDropRecipe()
	{
		super.addDropRecipe();
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.blocks[0], 1),
				new AbstractStack[] { EnumToolTypes.BIFACE.stack(), new BaseStack(ItemSimpleFluidContainer.createItemStack("bowl_wooden", null)) },
				new AbstractStack[] { new BaseStack(ItemFluidDisplay.createFluidDisplay(new FluidStack(IBFS.fLacquer, 225), true))},
				null);
	}
	
	@Override
	public int getLogMeta(IBlockState state, boolean isArt)
	{
		return isArt ? super.getLogMeta(state, isArt) : state.getValue(COL_FACING).horizontalOrdinal << 5 | state.getValue(COLLECTING) << 2 | state.getValue(BlockLog.LOG_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getLogState(Block block, int meta, boolean isArt)
	{
		return isArt ? super.getLogState(block, meta, isArt) : block.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.values()[meta & 0x3]).withProperty(COLLECTING, (meta >> 2) & 0x7).withProperty(COL_FACING, Direction.DIRECTIONS_2D[(meta >> 5) & 0x3]);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, BioData info)
	{
		if (info != null)
		{
			this.generator1.setTreeLogShape(6 + info.capabilities[0] / 4, 2 + info.capabilities[0] / 3);
		}
		else
		{
			this.generator1.setTreeLogShape(6, 3);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}
