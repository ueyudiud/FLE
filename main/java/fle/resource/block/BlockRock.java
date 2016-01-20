package fle.resource.block;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import farcore.FarCore;
import farcore.block.BlockFactory;
import farcore.block.BlockResource;
import farcore.block.EnumRockState;
import farcore.block.properties.FlePropertyEnum;
import farcore.block.properties.FlePropertyString;
import farcore.substance.Substance;
import flapi.FleResource;
import fle.resource.block.item.ItemRock;
import fle.resource.tile.TileEntityRock;

public class BlockRock extends BlockResource implements ITileEntityProvider
{
	static final FlePropertyEnum<EnumRockState> stateProperty = new FlePropertyEnum<EnumRockState>("state", EnumRockState.class);
	static final FlePropertyString<Substance> rockProperty = new FlePropertyString<Substance>("rock", FleResource.rock)
			{
		public String getName(Comparable value)
		{
			return FleResource.rock.get((String) value).getName();
		}
			};
	
	public BlockRock(String unlocalized)
	{
		super(unlocalized, ItemRock.class, Material.rock);
		FarCore.registerTileEntity(TileEntityRock.class, "fle.rock", Substance.LOADER, Substance.SAVER);
	}
	
	@Override
	public float getBlockHardness(World worldIn, BlockPos pos)
	{
		return getRock(worldIn, pos).getBlockHardness();
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
		TileEntityRock tile = getTile(worldIn, pos);
		if(tile != null)
		{
			return state.withProperty(rockProperty, tile.rock.getName())
			.withProperty(stateProperty, tile.state);
		}
		return state;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, rockProperty, stateProperty);
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
	protected IBlockState addDefaultropertiesTo(IBlockState state)
	{
		return state.withProperty(stateProperty, EnumRockState.resource);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state,
			EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return getRock(state).blockMineLevel;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return "hardHammer".equals(type) || "pickaxe".equals(type) || "crusher".equals(state);
	}
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(String tag : rockProperty)
		{
			if("void".equals(tag)) continue;
			for(EnumRockState state : EnumRockState.values())
			{
				list.add(setRock(new ItemStack(item), tag, state));
			}
		}
	}
	
	@Override
	public List<ItemStack> getDrops(World world, BlockPos pos,
			IBlockState state, int fortune, TileEntity tile)
	{
		ArrayList<ItemStack> ret = new ArrayList();
		if(tile instanceof TileEntityRock)
		{
			EnumRockState s = ((TileEntityRock) tile).state;
			if(s == EnumRockState.resource)
			{
				s = EnumRockState.cobble;
			}
			ret.add(setRock(new ItemStack(this, 1), ((TileEntityRock) tile).rock, s));
		}
		else ret.add(new ItemStack(Blocks.stone));
		return ret;
	}

	@Override
	protected void onSilkHarvest(World world, EntityPlayer player,
			BlockPos pos, IBlockState state, TileEntity te)
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack itemstack;
		if(te instanceof TileEntityRock)
		{
			itemstack = setRock(new ItemStack(this), ((TileEntityRock) te).rock, ((TileEntityRock) te).state);
		}
		else
		{
			itemstack = new ItemStack(Blocks.stone);
		}

		if (itemstack != null)
		{
			items.add(itemstack);
		}

		ForgeEventFactory.fireBlockHarvesting(items, world, pos, world.getBlockState(pos), 0, 1.0f, true, player);
		for (ItemStack stack : items)
		{
			spawnAsEntity(world, pos, stack);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack)
	{
		TileEntityRock tile = getTile(world, pos);
		if(tile != null)
		{
			tile.rock = getRock(stack);
			tile.state = getRockState(stack);
		}
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}
	
	@Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
		return BlockFactory.canSustainPlant(world, pos, getTile(world, pos).rock, direction, plantable);
    }
	
	private TileEntityRock getTile(IBlockAccess world, BlockPos pos)
	{
		return (TileEntityRock) world.getTileEntity(pos);
	}
	
	public ItemStack setRock(ItemStack stack, Substance value)
	{
		return setRock(stack, value, EnumRockState.resource);
	}
	
	public ItemStack setRock(ItemStack stack, Substance value, EnumRockState state)
	{
		return setRock(stack, FleResource.rock.name(value), state);
	}

	public ItemStack setRock(ItemStack stack, String value, EnumRockState state)
	{
		NBTTagCompound nbt = stack.getSubCompound("rock", true);
		nbt.setString("rock", value);
		nbt.setString("state", state.name());
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
	
	public Substance getRock(World world, BlockPos pos)
	{
		return getTile(world, pos).rock;
	}
	
	public Substance getRock(IBlockState blockState)
	{
		return rockProperty.getValue((String) blockState.getValue(rockProperty));
	}

	public String getRockStateName(IBlockState state)
	{
		return getRockState(state).name();
	}

	public EnumRockState getRockState(World world, BlockPos pos)
	{
		return getTile(world, pos).state;
	}

	public EnumRockState getRockState(IBlockState state)
	{
		return (EnumRockState) state.getValue(stateProperty);
	}

	public EnumRockState getRockState(ItemStack stack)
	{
		return EnumRockState.valueOf(stack.getSubCompound("rock", true).getString("state"));
	}
	
	@Override
	protected ItemStack createStackedBlock(IBlockState state)
	{
		return setRock(new ItemStack(this), getRock(state), getRockState(state));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityRock();
	}
}