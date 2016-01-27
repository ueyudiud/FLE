package fle.resource.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import farcore.FarCore;
import farcore.block.BlockFactory;
import farcore.block.BlockResource;
import farcore.block.IFallable;
import farcore.block.properties.FlePropertyString;
import farcore.entity.EntityFleFallingBlock;
import farcore.substance.Substance;
import flapi.FleResource;
import flapi.debug.BlockStateJsonWriter.BlockModel;
import flapi.debug.IModelStateProvider;
import flapi.debug.ModelJsonWriter;
import flapi.util.FleValue;
import fle.FLE;
import fle.debug.ModelResource;
import fle.resource.block.item.ItemSand;
import fle.resource.tile.TileEntitySand;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockSand extends BlockResource
		implements IFallable, ITileEntityProvider
{
	private void debug()
	{
		ModelResource.add(this, (IBlockState state) -> {
			return String.format("%s:sand/%s", FleValue.TEXTURE_FILE,
					(String) sandProperty
							.getName(state.getValue(sandProperty)));
		} , (ModelJsonWriter writer, BlockModel model) -> {
			writer.setParent("fle:block/base")
					.setTextures("render", FleValue.TEXTURE_FILE
							+ ":blocks/sand/"
							+ (String) model.getState().getValue(sandProperty));
		} , new IModelStateProvider[0]);
	}
	
	static final FlePropertyString<Substance> sandProperty = new FlePropertyString<Substance>(
			"sand", FleResource.sand)
	{
		public String getName(Comparable value)
		{
			return FleResource.sand.get((String) value).getName();
		}
	};
	
	public BlockSand(String unlocalized)
	{
		super(unlocalized, ItemSand.class, Material.sand);
		FarCore.registerTileEntity(TileEntitySand.class, "fle.sand",
				Substance.LOADER, Substance.SAVER);
		if (FLE.DEBUG)
			debug();
	}
	
	@Override
	public float getBlockHardness(World worldIn, BlockPos pos)
	{
		return getSand(worldIn, pos).getBlockHardness();
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos,
			Entity exploder, Explosion explosion)
	{
		return getBlockHardness(world, pos) * 1.5F;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn,
			BlockPos pos)
	{
		TileEntitySand tile = getTile(worldIn, pos);
		if (tile != null)
		{
			return state.withProperty(sandProperty, tile.sand.getName());
		}
		return state;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, sandProperty);
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
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (String tag : sandProperty)
		{
			if ("void".equals(tag))
				continue;
			list.add(setSand(new ItemStack(item), tag));
		}
	}
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	/**
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World worldIn, BlockPos pos,
			IBlockState state, Block neighborBlock)
	{
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state,
			Random rand)
	{
		if (!worldIn.isRemote)
		{
			checkFallable(worldIn, pos);
		}
	}
	
	private void checkFallable(World worldIn, BlockPos pos)
	{
		if (canFallInto(worldIn, pos.down()) && pos.getY() >= 0)
		{
			BlockFactory.makeFallingBlock(worldIn, pos, this);
		}
	}
	
	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World worldIn)
	{
		return 2;
	}
	
	public boolean canFallInto(World world, BlockPos pos)
	{
		if (world.isAirBlock(pos))
			return true;
		Block block = world.getBlockState(pos).getBlock();
		Material material = block.getMaterial();
		return block == Blocks.fire || material == Material.air
				|| material == Material.water || material == Material.lava;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntitySand();
	}
	
	@Override
	public void onStartFalling(EntityFleFallingBlock entity)
	{
	
	}
	
	@Override
	public void onEndFalling(World worldIn, BlockPos pos)
	{
	
	}
	
	@Override
	public void onHitEntity(EntityFleFallingBlock entity)
	{
	
	}
	
	@Override
	protected void onSilkHarvest(World world, EntityPlayer player, BlockPos pos,
			IBlockState state, TileEntity te)
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack itemstack;
		if (te instanceof TileEntitySand)
		{
			itemstack = setSand(new ItemStack(this),
					((TileEntitySand) te).sand.getName());
		}
		else
		{
			itemstack = new ItemStack(Blocks.sand);
		}
		
		if (itemstack != null)
		{
			items.add(itemstack);
		}
		
		ForgeEventFactory.fireBlockHarvesting(items, world, pos,
				world.getBlockState(pos), 0, 1.0f, true, player);
		for (ItemStack stack : items)
		{
			spawnAsEntity(world, pos, stack);
		}
	}
	
	@Override
	protected List<ItemStack> getDrops(World world, BlockPos pos,
			IBlockState state, int fortune, TileEntity tileEntity)
	{
		if (tileEntity instanceof TileEntitySand)
		{
			ArrayList<ItemStack> list = new ArrayList<ItemStack>();
			list.add(setSand(new ItemStack(this),
					((TileEntitySand) tileEntity).sand.getName()));
			return list;
		}
		return super.getDrops(world, pos, state, fortune, tileEntity);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack)
	{
		TileEntitySand tile = getTile(worldIn, pos);
		if (tile != null)
		{
			tile.sand = getSand(stack);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public List<ItemStack> onBlockDropAsItem(EntityFleFallingBlock entity)
	{
		ArrayList<ItemStack> list = new ArrayList();
		list.add(setSand(new ItemStack(this), getSandName(entity.getBlock())));
		return list;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return "shovel".equals(type);
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos,
			EnumFacing direction, IPlantable plantable)
	{
		return BlockFactory.canSustainPlant(world, pos,
				getTile(world, pos).sand, direction, plantable);
	}
	
	private TileEntitySand getTile(IBlockAccess world, BlockPos pos)
	{
		return !(world.getTileEntity(pos) instanceof TileEntitySand)
				? new TileEntitySand()
				: (TileEntitySand) world.getTileEntity(pos);
	}
	
	public Substance getSand(World world, BlockPos pos)
	{
		return getTile(world, pos).sand;
	}
	
	public String getSandName(IBlockState state)
	{
		return (String) state.getValue(sandProperty);
	}
	
	public Substance getSand(ItemStack stack)
	{
		return FleResource.sand.get(getSandName(stack));
	}
	
	public String getSandName(ItemStack stack)
	{
		return stack.getSubCompound("sand", true).getString("sand");
	}
	
	public ItemStack setSand(ItemStack stack, String value)
	{
		stack.getSubCompound("sand", true).setString("sand", value);
		return stack;
	}
}