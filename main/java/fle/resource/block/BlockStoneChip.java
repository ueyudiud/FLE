package fle.resource.block;

import static fle.resource.block.BlockRock.rockProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import farcore.FarCore;
import farcore.block.BlockResource;
import farcore.block.properties.FleProperty;
import farcore.block.properties.FlePropertyEnum;
import farcore.substance.Substance;
import flapi.debug.BlockStateJsonWriter.BlockModel;
import flapi.debug.IModelLocateProvider;
import flapi.debug.IModelProvider;
import flapi.debug.IModelStateProvider;
import flapi.debug.ModelJsonWriter;
import flapi.enums.EnumRockSize;
import flapi.util.FleValue;
import fle.FLE;
import fle.debug.ModelResource;
import fle.resource.block.item.ItemStoneChip;
import fle.resource.tile.TileEntityStoneChip;

public class BlockStoneChip extends BlockResource implements ITileEntityProvider
{
	private void debug()
	{
		ModelResource.add(this, new IModelLocateProvider()
		{
			@Override
			public String apply(IBlockState state)
			{
				return String.format("%s:rock/chip.%s.%s",
						FleValue.TEXTURE_FILE,
						(String) rockProperty
								.getName(state.getValue(rockProperty)),
						((EnumRockSize) state.getValue(sizeProperty)).name());
			}
		}, new IModelProvider()
		{
			@Override
			public void provide(ModelJsonWriter writer, BlockModel model)
			{
				EnumRockSize size = (EnumRockSize) model.getState()
						.getValue(sizeProperty);
				float f1 = 16 * (0.5F - size.getWidth() * .5F);
				float f2 = 16 * (0.5F + size.getWidth() * .5F);
				float f3 = 0;
				float f4 = 16 * size.getHeiht();
				writer.element().from(f1, f3, f1).to(f2, f4, f2)
						.face(EnumFacing.DOWN).uv(f1, f1, f2, f2).texture("all")
						.build().face(EnumFacing.UP).uv(f1, f1, f2, f2)
						.texture("all").build().face(EnumFacing.NORTH)
						.uv(f1, f3, f2, f4).texture("all").build()
						.face(EnumFacing.WEST).uv(f1, f3, f2, f4).texture("all")
						.build().face(EnumFacing.SOUTH).uv(f1, f3, f2, f4)
						.texture("all").build().face(EnumFacing.EAST)
						.uv(f1, f3, f2, f4).texture("all").build().build()
						.setTextures("all",
								FleValue.TEXTURE_FILE + ":blocks/rock/"
										+ (String) model.getState()
												.getValue(rockProperty))
						.setTextures("particle",
								FleValue.TEXTURE_FILE + ":blocks/rock/"
										+ (String) model.getState()
												.getValue(rockProperty));
			}
		}, new IModelStateProvider[0]);
	}
	
	static final FleProperty sizeProperty = new FlePropertyEnum<EnumRockSize>(
			"size", EnumRockSize.class);
			
	public BlockStoneChip(String unlocalized)
	{
		super(unlocalized, ItemStoneChip.class, Material.circuits);
		setTickRandomly(true);
		FarCore.registerTileEntity(TileEntityStoneChip.class, "stoneChip",
				Substance.LOADER, Substance.SAVER);
		if (FLE.DEBUG)
			debug();
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, sizeProperty, rockProperty);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn,
			BlockPos pos)
	{
		TileEntityStoneChip tile = getTile(worldIn, pos);
		if (tile != null)
		{
			return state.withProperty(rockProperty, tile.rock.getName())
					.withProperty(sizeProperty, tile.size);
		}
		return state;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		EnumRockSize size = getSize(worldIn, pos);
		return new AxisAlignedBB(
				(double) pos.getX() + .5F - size.getWidth() * .5F,
				(double) pos.getY(),
				(double) pos.getZ() + .5F - size.getWidth() * .5F,
				(double) pos.getX() + .5F + size.getWidth() * .5F,
				(double) pos.getY() + size.getHeiht(),
				(double) pos.getZ() + .5F + size.getWidth() * .5F);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos,
			IBlockState state)
	{
		return null;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state,
			Random rand)
	{
		if (!worldIn.isRemote && !canPlaceBlockAt(worldIn, pos))
		{
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos,
			IBlockState state, Block neighborBlock)
	{
		updateTick(worldIn, pos, state, worldIn.rand);
	}
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (EnumRockSize state : EnumRockSize.values())
		{
			for (String tag : rockProperty)
			{
				if ("void".equals(tag))
					continue;
				list.add(setRock(new ItemStack(item), tag, state));
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.isSideSolid(pos.offset(EnumFacing.DOWN), EnumFacing.UP);
	}
	
	@Override
	public List<ItemStack> getDrops(World world, BlockPos pos,
			IBlockState state, int fortune, TileEntity tile)
	{
		ArrayList<ItemStack> ret = new ArrayList();
		if (tile instanceof TileEntityStoneChip)
		{
			ret.add(setRock(new ItemStack(this, 1),
					((TileEntityStoneChip) tile).rock,
					((TileEntityStoneChip) tile).size));
		}
		return ret;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack)
	{
		TileEntityStoneChip tile = getTile(worldIn, pos);
		if (tile != null)
		{
			tile.rock = getRock(stack);
			tile.size = getRockSize(stack);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	protected TileEntityStoneChip getTile(IBlockAccess world, BlockPos pos)
	{
		return !(world.getTileEntity(pos) instanceof TileEntityStoneChip)
				? new TileEntityStoneChip()
				: (TileEntityStoneChip) world.getTileEntity(pos);
	}
	
	public Substance getRock(IBlockAccess world, BlockPos pos)
	{
		return getTile(world, pos).rock;
	}
	
	public EnumRockSize getSize(IBlockAccess world, BlockPos pos)
	{
		return getTile(world, pos).size;
	}
	
	public ItemStack setRock(ItemStack stack, Substance rock, EnumRockSize size)
	{
		return setRock(stack, rock.getName(), size);
	}
	
	public ItemStack setRock(ItemStack stack, String rock, EnumRockSize size)
	{
		stack.getSubCompound("rock", true).setString("rock", rock);
		stack.getSubCompound("rock", true).setString("size", size.name());
		return stack;
	}
	
	public String getRockName(ItemStack stack)
	{
		return stack.getSubCompound("rock", true).getString("rock");
	}
	
	public Substance getRock(ItemStack stack)
	{
		return rockProperty.getValue(getRockName(stack));
	}
	
	public EnumRockSize getRockSize(ItemStack stack)
	{
		return EnumRockSize
				.valueOf(stack.getSubCompound("rock", true).getString("size"));
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityStoneChip();
	}
}