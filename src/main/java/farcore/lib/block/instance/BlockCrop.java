package farcore.lib.block.instance;

import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.lib.block.BlockTileUpdatable;
import farcore.lib.crop.ICropAccess;
import farcore.lib.tile.instance.TECrop;
import farcore.util.BlockStateWrapper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrop extends BlockTileUpdatable implements ITileEntityProvider, IPlantable
{
	public static class CropState extends BlockStateWrapper
	{
		public ICropAccess access;
		
		public CropState(ICropAccess access, IBlockState state)
		{
			super(state);
			this.access = access;
		}

		@Override
		protected BlockStateWrapper wrapState(IBlockState state)
		{
			return new CropState(access, state);
		}
	}
	
	private static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(.03125F, .0F, .03125F, .96875F, .96875F, .96875F);
	
	public BlockCrop()
	{
		super(FarCore.ID, "crop", Material.PLANTS);
		EnumBlock.crop.set(this);
		setHardness(0.5F);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ICropAccess)
			return new CropState((ICropAccess) tile, getDefaultState());
		return state;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return null;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return CROP_AABB;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!canBlockStay(worldIn, pos))
		{
			worldIn.setBlockToAir(pos);
		}
	}

	public boolean canBlockStay(World world, BlockPos pos)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(pos)) instanceof TECrop)
			return ((TECrop) tile).canPlantAt();
		return true;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TECrop ? ((TECrop) tile).getPlantType() : EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return getDefaultState();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TECrop();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		return true;
	}
}