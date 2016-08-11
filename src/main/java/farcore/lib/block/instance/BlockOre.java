package farcore.lib.block.instance;

import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumToolType;
import farcore.lib.block.BlockTileUpdatable;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.block.IToolableBlock;
import farcore.lib.block.material.MaterialOre;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.util.Direction;
import farcore.util.BlockStateWrapper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOre extends BlockTileUpdatable
implements ITileEntityProvider, IThermalCustomBehaviorBlock, IToolableBlock
{
	public static class OreStateWrapper extends BlockStateWrapper
	{
		public final TEOre ore;
		
		OreStateWrapper(IBlockState state, TEOre ore)
		{
			super(state);
			this.ore = ore;
		}
		
		@Override
		protected BlockStateWrapper wrapState(IBlockState state)
		{
			return new OreStateWrapper(state, ore);
		}
	}

	private static final MaterialOre ORE = new MaterialOre();

	public BlockOre()
	{
		super(FarCore.ID, "ore", ORE);
		setTickRandomly(true);
		EnumBlock.ore.set(this);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
			return new OreStateWrapper(state, (TEOre) tile);
		return state;
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 1.0F;
		return ((TEOre) tile).getHardness();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 1.0F;
		return ((TEOre) tile).getExplosionResistance();
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return;
		((TEOre) tile).ore.oreProperty.onEntityWalk((TEOre) tile, entityIn);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return false;
		ItemStack stack = player.inventory.getCurrentItem();
		String tool = EnumToolType.pickaxe.name();
		if (stack == null)
			return player.canHarvestBlock(getDefaultState());
		int toolLevel = stack.getItem().getHarvestLevel(stack, tool);
		if (toolLevel < 0)
			return player.canHarvestBlock(getDefaultState());
		return toolLevel >= ((TEOre) tile).getHarvestLevel();
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return EnumToolType.pickaxe.name();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEOre();
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TEOre)
		{
			((TEOre) tile).ore.oreProperty.updateTick((TEOre) tile, rand);
		}
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
			return ((TEOre) tile).rockType.burnable ? 80 : 0;
		return 0;
	}
	
	@Override
	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
		{
			if(((TEOre) tile).ore.oreProperty.onBurn((TEOre) tile, burnHardness, direction)) return true;
			if(((TEOre) tile).rockType.burnable)
			{
				((TEOre) tile).rockType = ((TEOre) tile).rockType.burned();
				tile.markDirty();
			}
		}
		return false;
	}
	
	@Override
	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
		{
			if(((TEOre) tile).ore.oreProperty.onBurningTick((TEOre) tile, rand, fireSourceDir, fireState)) return true;
		}
		return false;
	}
	
	@Override
	public float getThermalConduct(World world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEOre)
			return ((TEOre) tile).getThermalConduct();
		return -1F;
	}
	
	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return 0;
	}

	
	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 0F;
		return ((TEOre) tile).ore.oreProperty.onToolClick(player, tool, stack, (TEOre) tile, side, hitX, hitY, hitZ);
	}

	
	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TEOre)) return 0F;
		return ((TEOre) tile).ore.oreProperty.onToolUse(player, tool, stack, (TEOre) tile, side, hitX, hitY, hitZ, useTick);
	}
}