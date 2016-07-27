package farcore.lib.block.instance;

import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.lib.block.BlockTileUpdatable;
import farcore.lib.crop.ICrop;
import farcore.lib.tile.instance.TECrop;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrop extends BlockTileUpdatable implements ITileEntityProvider, IPlantable
{
	private static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(.03125F, .0F, .03125F, .96875F, .96875F, .96875F);
	
	public BlockCrop()
	{
		super(FarCore.ID, "crop", Material.PLANTS);
		EnumBlock.crop.set(this);
		setHardness(0.5F);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{ICrop.CROP_NAME, ICrop.CROP_STATE});
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IExtendedBlockState state2 = (IExtendedBlockState) state;
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TECrop)
		{
			state2.withProperty(ICrop.CROP_NAME, ((TECrop) tile).crop().getRegisteredName());
			state2.withProperty(ICrop.CROP_STATE, ((TECrop) tile).getStateName());
		}
		return state2;
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
}