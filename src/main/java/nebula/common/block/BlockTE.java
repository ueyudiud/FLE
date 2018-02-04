/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block;

import java.util.ArrayList;
import java.util.List;

import nebula.base.register.IRegister;
import nebula.base.register.Register;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.StateMapperExt;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.block.property.PropertyTE;
import nebula.common.block.property.PropertyTE.TETag;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The block with multi tile entity.
 * <p>
 * If you added ED(Extra Data) mod, there will be 20 bits to store tile entity
 * data, or only 4 bit instead.
 * 
 * @author ueyudiud
 * @see net.minecraft.tileentity.TileEntity TileEntity
 */
public abstract class BlockTE extends BlockSingleTE implements IExtendedDataBlock
{
	/**
	 * Mark for tile entity auto-register when {@link #postInitalizedBlocks()}.
	 */
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
	
	/**
	 * Register all allowed states to register.
	 * 
	 * @parm register the state register.
	 */
	@Override
	public void registerStateToRegister(IBlockStateRegister register)
	{
		register.registerStates(this, this.property_TE);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		if (this.registerTE)
		{
			String path = getRegistryName().getResourceDomain() + "." + getRegistryName().getResourcePath();
			for (TETag tag : this.property_TE.getAllowedValues())
			{
				tag.registerTileEntity(path);
			}
		}
	}
	
	/**
	 * Register model mapper with item and block to model manager.
	 * <p>
	 * Similar to method in <tt>Renders.registerCompactModel</tt>, but to
	 * prevent wrong data cause game crashed, this block can not split block
	 * meta by {@link #getMetaFromState(IBlockState)}, so the block is needed
	 * this internal method to register render mapper.
	 * 
	 * @param mapper
	 * @see nebula.client.util.Renders#registerCompactModel(StateMapperExt,
	 *      net.minecraft.block.Block, net.minecraft.block.properties.IProperty)
	 */
	@SideOnly(Side.CLIENT)
	protected void registerRenderMapper(StateMapperExt mapper)
	{
		final IBlockState state = getDefaultState();
		IBlockState state2 = state;
		do
			ModelLoader.setCustomModelResourceLocation(this.item, this.property_TE.getMetaFromState(state2), mapper.getLocationFromState(state2));
		while ((state2 = state2.cycleProperty(this.property_TE)) != state);
		ModelLoader.setCustomStateMapper(this, mapper);
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerCustomBlockRender(StateMapperExt map, int meta, String location)
	{
		NebulaModelLoader.registerModel(this, map, this.property_TE.withProperty(getDefaultState(), meta), new ResourceLocation(getRegistryName().getResourceDomain(), location));
	}
	
	/**
	 * The property of tile entity type.
	 */
	public PropertyTE property_TE;
	
	/**
	 * Add tile entities to register.
	 * 
	 * @param register
	 * @return Return true for all registered tile entity will in to tile entity
	 *         map.
	 */
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
	public IBlockState getStateFromMeta(int meta)
	{
		return getStateFromData(meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return getDataFromState(state) & 0xF;
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
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		return this.property_TE.withProperty(getDefaultState(), stackIn.getMetadata());
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.property_TE.withProperty(state, worldIn.getTileEntity(pos));
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BlockStateTileEntityWapper.wrap(world.getTileEntity(pos), super.getExtendedState(state, world, pos));
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
	public int damageDropped(IBlockState state)
	{
		return this.property_TE.getMetaFromState(state);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		if (tile instanceof ITP_Drops)
			return ((ITP_Drops) tile).getDrops(state, fortune, silkTouch);
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(this, 1, damageDropped(state)));
		return list;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return this.property_TE.getTileFromMeta(meta);
	}
}
