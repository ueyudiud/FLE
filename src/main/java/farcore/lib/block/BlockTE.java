package farcore.lib.block;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.prop.PropertyTE;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Drops;
import farcore.lib.tile.instance.TELossTile;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockTE extends BlockSingleTE
/**
 * The far core tile entity block use custom provide tile policy.
 */
//, ITileEntityProvider
{
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

	public PropertyTE property_TE;
	
	protected abstract boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register);

	protected final PropertyTE createTEProperty()
	{
		IRegister<Class<? extends TileEntity>> map = new Register();
		map.register(0, "void", TELossTile.class);
		if(registerTileEntities(map))
		{
			for(String tag : map.names())
			{
				if("void".equals(tag))
				{
					continue;
				}
				GameRegistry.registerTileEntity(map.get(tag), blockName + "." + tag);
			}
		}
		return property_TE = PropertyTE.create("tile", map);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, createTEProperty());
	}

	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(property_TE, property_TE.parseValue("void").get());
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		return property_TE.withProperty(getDefaultState(), meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return property_TE.withProperty(state, worldIn.getTileEntity(pos));
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return state.getValue(property_TE).newInstance();
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
		List<ItemStack> list = new ArrayList();
		list.add(new ItemStack(this, 1, property_TE.getMetaFromState(state)));
		return list;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return property_TE.getTileFromMeta(meta);
	}
}