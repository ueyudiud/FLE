package farcore.lib.block.instance;

import java.util.Random;

import farcore.data.EnumToolType;
import farcore.lib.block.IToolableBlock;
import farcore.lib.material.Mat;
import farcore.lib.tree.ITree;
import farcore.lib.util.Direction;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.util.U;
import farcore.util.runnable.BreakTree;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLogNatural extends BlockLog implements IToolableBlock
{
	public static BlockLogNatural create(Mat material)
	{
		return new BlockLogNatural(material, material.tree)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return material.tree.createLogStateContainer(this, false);
			}

			@Override
			public int getMetaFromState(IBlockState state)
			{
				return material.tree.getLogMeta(state, false);
			}

			@Override
			public IBlockState getStateFromMeta(int meta)
			{
				return material.tree.getLogState(this, meta, false);
			}
		};
	}

	protected BlockLogNatural(Mat material, ITree tree)
	{
		super("log.natural." + material.name, material, tree);
		setHardness(material.blockHardness);
		setResistance(material.blockExplosionResistance);
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), material.localName + " Log");
		if(tree.tickLogUpdate())
			setTickRandomly(true);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		tree.breakLog(worldIn, pos, state, false);
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 20;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		tree.updateLog(worldIn, pos, rand, false);
		label:
		{
			if(!worldIn.isRemote)
			{
				if(worldIn.isSideSolid(pos.down(), EnumFacing.UP))
					break label;
				for (int h = -1; h <= 1; h++)
					for (int g = -1; g <= 1; g++)
						for (int f = -1; f <= 1; f++)
							if ((h | g | f) != 0 && isLog(worldIn, pos.add(h, g, f)))
								break label;
				worldIn.setBlockToAir(pos);
			}
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		if(world instanceof World)
		{
			if(((World) world).isRemote) return;
			((World) world).scheduleUpdate(pos, this, tickRate((World) world));
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return tree.onLogRightClick(playerIn, worldIn, pos, Direction.of(side), hitX, hitY, hitZ, false);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		harvestBlock(worldIn, player, pos, state, null, player.getHeldItemMainhand());
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		if(!worldIn.isRemote)
			if(EnumToolType.axe.match(stack))
				breakTree(worldIn, pos);
			else
				worldIn.setBlockState(pos, state, 3);
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		try
		{
			breakTree(world, pos);
		}
		catch(OutOfMemoryError error)
		{
			Log.warn("The out of memory prevent this tree destory.");
		}
	}

	public boolean isLog(World worldIn, BlockPos pos)
	{
		return U.Worlds.isBlock(worldIn, pos, this, -1, false);
	}

	private void breakTree(World world, BlockPos pos)
	{
		BreakTree runnable = new BreakTree(this, world, pos);
		new Thread(runnable).start();
	}

	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ)
	{
		return tree.onToolClickLog(player, tool, stack, world, pos, side, hitX, hitY, hitZ, false);
	}

	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return tree.onToolUseLog(player, tool, stack, world, useTick, pos, side, hitX, hitY, hitZ, false);
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