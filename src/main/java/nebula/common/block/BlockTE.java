package nebula.common.block;

import java.util.ArrayList;
import java.util.List;

import nebula.client.model.NebulaBlockModelLoader;
import nebula.client.model.StateMapperExt;
import nebula.common.base.IRegister;
import nebula.common.base.Register;
import nebula.common.block.property.PropertyTE;
import nebula.common.block.property.PropertyTE.TETag;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockTE extends BlockSingleTE implements IExtendedDataBlock
{
	private boolean registerTE;
	
	public BlockTE(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	public BlockTE(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
	}
	public BlockTE(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
	}
	public BlockTE(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
	}
	
	@Override
	public void registerStateToRegister(ExtendedBlockStateRegister register)
	{
		register.registerStates(this, this.property_TE);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		if(this.registerTE)
		{
			String path = getRegistryName().getResourceDomain() + "." + getRegistryName().getResourcePath();
			for(TETag tag : this.property_TE.getAllowedValues())
			{
				tag.registerTileEntity(path);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerCustomBlockRender(StateMapperExt map, int meta, String location)
	{
		NebulaBlockModelLoader.registerModel(map.getModelResourceLocation(this.property_TE.withProperty(getDefaultState(), meta)), new ResourceLocation(getRegistryName().getResourceDomain(), location));
	}
	
	public PropertyTE property_TE;
	
	protected abstract boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register);
	
	protected final PropertyTE createTEProperty()
	{
		IRegister<Class<? extends TileEntity>> map = new Register<>();
		this.registerTE = registerTileEntities(map);
		return this.property_TE = PropertyTE.create("tile", map);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, createTEProperty());
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state;
	}
	
	@Override
	public IBlockState getStateFromData(int meta)
	{
		return this.property_TE.withProperty(getDefaultState(), meta);
	}
	
	@Override
	public int getDataFromState(IBlockState state)
	{
		return this.property_TE.getMetaFromState(state);
	}
	
	@Override
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		return this.property_TE.withProperty(getDefaultState(), stackIn.getMetadata());
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.property_TE.withProperty(state, worldIn.getTileEntity(pos));
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return state.getValue(this.property_TE).newInstance();
	}
	
	@Override
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (TETag tag : this.property_TE.getAllowedValues())
		{
			list.add(new ItemStack(item, 1, tag.id()));
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune,
			boolean silkTouch)
	{
		if(tile instanceof ITP_Drops)
			return ((ITP_Drops) tile).getDrops(state, fortune, silkTouch);
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(this, 1, this.property_TE.getMetaFromState(state)));
		return list;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return this.property_TE.getTileFromMeta(meta);
	}
}