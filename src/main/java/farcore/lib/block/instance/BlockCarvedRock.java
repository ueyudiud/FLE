package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;

import farcore.data.EnumBlock;
import farcore.data.EnumToolType;
import farcore.lib.block.BlockTileUpdatable;
import farcore.lib.block.IToolableBlock;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCarvedRock extends BlockTileUpdatable
implements ITileEntityProvider, IToolableBlock
{
	public BlockCarvedRock()
	{
		super("carved_rock", Material.ROCK);
		EnumBlock.carved_rock.set(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(!(tile instanceof TECustomCarvedStone)) return 1.0F;
		return ((TECustomCarvedStone) tile).rock().blockHardness;
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TECustomCarvedStone)) return 1.0F;
		return ((TECustomCarvedStone) tile).rock().blockExplosionResistance;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TECustomCarvedStone)
			return ((TECustomCarvedStone) tile).isFullCube();
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return isNormalCube(state, world, pos) ? 255 : 3;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TECustomCarvedStone)
			return ((TECustomCarvedStone) tile).isSideSolid(side);
		return super.isSideSolid(base_state, world, pos, side);
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TECustomCarvedStone)
			return !((TECustomCarvedStone) tile).isCarved(1, 3, 1) &&
					!((TECustomCarvedStone) tile).isCarved(2, 3, 1) &&
					!((TECustomCarvedStone) tile).isCarved(1, 3, 2) &&
					!((TECustomCarvedStone) tile).isCarved(2, 3, 2);
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TECustomCarvedStone)
			return ((TECustomCarvedStone) tile).getCollisionBoundingBox();
		return FULL_BLOCK_AABB;
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}

	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		return new ArrayList();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TECustomCarvedStone();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager)
	{
		TileEntity tile = world.getTileEntity(target.getBlockPos());
		if(tile instanceof TECustomCarvedStone)
		{
			IBlockState state2 = ((TECustomCarvedStone) tile).rock().rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, ((TECustomCarvedStone) tile).type);
			U.Client.addBlockHitEffect(world, RANDOM, state2, target.sideHit, target.getBlockPos(), manager);
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TECustomCarvedStone)
		{
			IBlockState state2 = ((TECustomCarvedStone) tile).rock().rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, ((TECustomCarvedStone) tile).type);
			U.Client.addBlockDestroyEffects(world, pos, state2, manager);
			return true;
		}
		return false;
	}
	
	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, BlockPos pos,
			Direction side, float hitX, float hitY, float hitZ)
	{
		if(tool == EnumToolType.chisel)
		{
			TECustomCarvedStone tile = (TECustomCarvedStone) world.getTileEntity(pos);
			if(player.canPlayerEdit(pos, side.of(), stack))
				return tile.carveRock(player, hitX, hitY, hitZ);
		}
		return 0;
	}
	
	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return 0;
	}
}