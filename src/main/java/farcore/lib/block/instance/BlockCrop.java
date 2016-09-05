package farcore.lib.block.instance;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.lib.block.BlockTE;
import farcore.lib.collection.IRegister;
import farcore.lib.crop.ICropAccess;
import farcore.lib.tile.instance.TECrop;
import farcore.util.BlockStateWrapper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrop extends BlockTE implements IPlantable
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

	public BlockCrop()
	{
		super(FarCore.ID, "crop", Material.PLANTS);
		EnumBlock.crop.set(this);
		setHardness(0.5F);
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(1, "crop", TECrop.class);
		return true;
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
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
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
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		return true;
	}
}