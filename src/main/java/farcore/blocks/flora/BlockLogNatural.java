/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.blocks.flora;

import java.util.Random;

import farcore.data.Config;
import farcore.lib.tree.Tree;
import farcore.util.runnable.BreakTree;
import nebula.NebulaLog;
import nebula.common.LanguageManager;
import nebula.common.block.IBlockStateRegister;
import nebula.common.block.IToolableBlock;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.W;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockLogNatural extends BlockLog implements IToolableBlock
{
	public static BlockLogNatural create(Tree $tree)
	{
		return new BlockLogNatural($tree)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return $tree.createLogStateContainer(this, false);
			}
			
			@Override
			public int getDataFromState(IBlockState state)
			{
				return $tree.getLogMeta(state, false);
			}
			
			@Override
			public IBlockState getStateFromData(int meta)
			{
				return $tree.getLogState(this, meta, false);
			}
			
			@Override
			public void registerStateToRegister(IBlockStateRegister register)
			{
				$tree.registerLogExtData(this, false, register);
			}
			
			@Override
			protected IBlockState initDefaultState(IBlockState state)
			{
				return $tree.initLogState(false, super.initDefaultState(state));
			}
		};
	}
	
	protected BlockLogNatural(Tree tree)
	{
		super("log.natural." + tree.material().name, tree);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), tree.material().localName + " NebulaLog");
		if (tree.tickLogUpdate())
		{
			setTickRandomly(true);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		this.tree.registerRender();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		this.tree.breakLog(worldIn, pos, state, false);
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 20;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		updateTick(worldIn, pos, state, random);
		state = worldIn.getBlockState(pos);
		if (state.getBlock() == this)
		{
			this.tree.updateLog(worldIn, pos, random, false);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		label:
		{
		if (!worldIn.isRemote)
		{
			if (worldIn.isSideSolid(pos.down(), EnumFacing.UP))
			{
				break label;
			}
			for (int h = -1; h <= 1; h++)
			{
				for (int g = -1; g <= 1; g++)
				{
					for (int f = -1; f <= 1; f++)
						if ((h | g | f) != 0 && isLog(worldIn, pos.add(h, g, f)))
						{
							break label;
						}
				}
			}
			worldIn.setBlockToAir(pos);
		}
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		if (world instanceof World)
		{
			if (((World) world).isRemote) return;
			((World) world).scheduleUpdate(pos, this, tickRate((World) world));
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return this.tree.onLogRightClick(playerIn, worldIn, pos, Direction.of(side), hitX, hitY, hitZ, false);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		harvestBlock(worldIn, player, pos, state, null, player.getHeldItemMainhand());
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		if (!worldIn.isRemote)
		{
			player.addExhaustion(0.1F);
			breakTree(worldIn, pos);
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		try
		{
			breakTree(world, pos);
		}
		catch (OutOfMemoryError error)
		{
			NebulaLog.warn("The out of memory prevent this tree destory.");
		}
	}
	
	public boolean isLog(World worldIn, BlockPos pos)
	{
		return W.isBlock(worldIn, pos, this, -1, false);
	}
	
	private void breakTree(World world, BlockPos pos)
	{
		BreakTree runnable = new BreakTree(this, world, pos);
		if (Config.breakTreeMultiThread)
		{
			new Thread(runnable).start();
		}
		else
		{
			runnable.run();
		}
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return this.tree.onToolClickLog(player, tool, level, stack, world, pos, side, hitX, hitY, hitZ, false);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 25;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 18;
	}
}
