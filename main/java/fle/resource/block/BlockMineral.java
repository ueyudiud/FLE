package fle.resource.block;

import static fle.resource.block.BlockRock.rockProperty;
import static fle.resource.block.BlockRock.stateProperty;

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
import farcore.block.BlockResource;
import farcore.block.EnumRockState;
import farcore.block.properties.FlePropertyString;
import farcore.substance.Substance;
import farcore.util.Part;
import flapi.FleResource;
import fle.init.Substance1;
import fle.resource.block.item.ItemMineral;
import fle.resource.tile.TileEntityMineral;

public class BlockMineral extends BlockResource implements ITileEntityProvider
{
	static final FlePropertyString<Substance> mineableProperty = new FlePropertyString<Substance>("mine", FleResource.mineral)
			{
		public String getName(Comparable value)
		{
			return FleResource.mineral.get((String) value).getName();
		}
			};

	public BlockMineral(String unlocalized)
	{
		super(unlocalized, ItemMineral.class, Material.rock);
		FarCore.registerTileEntity(TileEntityMineral.class, "fle.mineral", 
				Substance.LOADER, Substance.SAVER);
	}
	
	@Override
	public float getBlockHardness(World worldIn, BlockPos pos)
	{
		return Math.max(getRock(worldIn, pos).getBlockHardness(), getMineral(worldIn, pos).getBlockHardness());
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
		TileEntityMineral tile = getTile(worldIn, pos);
		if(tile != null)
		{
			return state.withProperty(rockProperty, tile.rock.getName())
					.withProperty(mineableProperty, tile.ore.getName())
					.withProperty(stateProperty, tile.state);
		}
		return state;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, rockProperty, stateProperty, mineableProperty);
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
		return Math.max(getRock(state).blockMineLevel, getMineral(state).blockMineLevel);
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return "hardHammer".equals(type) || "pickaxe".equals(type) || "crusher".equals(state);
	}
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(String tag : mineableProperty)
		{
			if("void".equals(tag)) continue;
			for(EnumRockState state : EnumRockState.values())
			{
				list.add(setMineral(new ItemStack(item), Substance1.stone.getName(), state, tag, 144));
			}
		}
	}
	
	@Override
	public List<ItemStack> getDrops(World world, BlockPos pos,
			IBlockState state, int fortune, TileEntity tile)
	{
		ArrayList<ItemStack> ret = new ArrayList();
		if(tile instanceof TileEntityMineral)
		{
			EnumRockState s = ((TileEntityMineral) tile).state;
			if(s == EnumRockState.resource)
			{
				s = EnumRockState.cobble;
			}
			ret.add(setMineral(new ItemStack(this, 1), ((TileEntityMineral) tile).rock, s, ((TileEntityMineral) tile).ore, ((TileEntityMineral) tile).amount));
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
		if(te instanceof TileEntityMineral)
		{
			itemstack = setMineral(new ItemStack(this), ((TileEntityMineral) te).rock, ((TileEntityMineral) te).state, ((TileEntityMineral) te).ore, ((TileEntityMineral) te).amount);
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
		TileEntityMineral tile = getTile(world, pos);
		if(tile != null)
		{
			tile.ore = getMineral(stack);
			tile.rock = getRock(stack);
			tile.state = getRockState(stack);
			tile.amount = getMineralAmount(stack);
		}
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}
	
	@Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
		return false;
    }
	
	private TileEntityMineral getTile(IBlockAccess world, BlockPos pos)
	{
		return (TileEntityMineral) world.getTileEntity(pos);
	}

	public Substance getRock(World world, BlockPos pos)
	{
		return getTile(world, pos).rock;
	}	
	public Substance getMineral(World world, BlockPos pos)
	{
		return getTile(world, pos).ore;
	}
	public Substance getRock(IBlockState blockState)
	{
		return rockProperty.getValue((String) blockState.getValue(rockProperty));
	}
	public String getRockStateName(IBlockState state)
	{
		return getRockState(state).name();
	}
	public EnumRockState getRockState(IBlockState state)
	{
		return (EnumRockState) state.getValue(stateProperty);
	}
	public Substance getMineral(IBlockState state)
	{
		return mineableProperty.getValue((String) state.getValue(mineableProperty));
	}
	
	public Substance getRock(ItemStack stack)
	{
		return rockProperty.getValue(getRockName(stack));
	}
	public String getRockName(ItemStack stack)
	{
		return stack.getSubCompound("mineral", true).getString("rock");
	}
	public EnumRockState getRockState(ItemStack stack)
	{
		return EnumRockState.valueOf(stack.getSubCompound("mineral", true).getString("state"));
	}	
	public Substance getMineral(ItemStack stack)
	{
		return mineableProperty.getValue(getMineralName(stack));
	}
	public String getMineralName(ItemStack stack)
	{
		return stack.getSubCompound("mineral", true).getString("ore");
	}
	public int getMineralAmount(ItemStack stack)
	{
		return stack.getSubCompound("mineral", true).getInteger("amount");
	}
	
	public ItemStack setMineral(ItemStack stack, Substance value)
	{
		return setMineral(stack, Substance1.stone, EnumRockState.resource, value, Part.ore.resolution);
	}
	public ItemStack setMineral(ItemStack stack, Substance value, EnumRockState state, Substance value1, int amount)
	{
		return setMineral(stack, FleResource.rock.name(value), state, FleResource.mineral.name(value1), amount);
	}
	public ItemStack setMineral(ItemStack stack, String value, EnumRockState state, String value1, int amount)
	{
		NBTTagCompound nbt = stack.getSubCompound("mineral", true);
		nbt.setString("rock", value);
		nbt.setString("state", state.name());
		nbt.setString("ore", value1);
		nbt.setInteger("amount", amount);
		return stack;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMineral();
	}
}