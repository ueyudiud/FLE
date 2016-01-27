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
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import farcore.FarCore;
import farcore.biology.SpecieRegistry;
import farcore.biology.plant.IPlant;
import farcore.biology.plant.IPlantSpecies;
import farcore.biology.plant.PlantCover;
import farcore.block.BlockFactory;
import farcore.block.BlockResource;
import farcore.block.EnumDirtCover;
import farcore.block.EnumDirtState;
import farcore.block.IPropagatedableBlock;
import farcore.block.properties.FlePropertyEnum;
import farcore.block.properties.FlePropertyString;
import farcore.substance.Substance;
import farcore.util.Direction;
import flapi.FleResource;
import flapi.debug.BlockStateJsonWriter.BlockModel;
import flapi.debug.IModelStateProvider;
import flapi.debug.ModelJsonWriter;
import flapi.util.FleValue;
import fle.FLE;
import fle.debug.ModelResource;
import fle.resource.block.item.ItemDirt;
import fle.resource.tile.TileEntityDirt;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockDirt extends BlockResource
		implements ITileEntityProvider, IPropagatedableBlock
{
	private void debug()
	{
		ModelResource.add(this, (IBlockState state) -> {
			return String.format("%s:dirt/%s.%s.%s", FleValue.TEXTURE_FILE,
					(String) dirtProperty.getName(state.getValue(dirtProperty)),
					((EnumDirtState) state.getValue(DIRT_STATE)).name(),
					((EnumDirtCover) state.getValue(COVER)).name());
		} , (ModelJsonWriter writer, BlockModel model) -> {
			writer.setParent("fle:block/dirt_base")
					.setTextures("base",
							FleValue.TEXTURE_FILE + ":blocks/dirt/"
									+ (String) model.getState()
											.getValue(dirtProperty))
					.setTextures("cover",
							FleValue.TEXTURE_FILE + ":blocks/dirt/cover/"
									+ ((EnumDirtCover) model.getState()
											.getValue(COVER)).name()
									+ "_side")
					.setTextures("covertop",
							FleValue.TEXTURE_FILE + ":blocks/dirt/cover/"
									+ ((EnumDirtCover) model.getState()
											.getValue(COVER)).name()
									+ "_top")
					.setTextures("grow",
							FleValue.TEXTURE_FILE + ":blocks/dirt/state/"
									+ ((EnumDirtState) model.getState()
											.getValue(DIRT_STATE)).name()
									+ "_side")
					.setTextures("growtop",
							FleValue.TEXTURE_FILE + ":blocks/dirt/state/"
									+ ((EnumDirtState) model.getState()
											.getValue(DIRT_STATE)).name()
									+ "_top");
		} , new IModelStateProvider[0]);
	}
	
	static final FlePropertyEnum<EnumDirtState> DIRT_STATE = new FlePropertyEnum(
			"grow", EnumDirtState.class);
	static final FlePropertyEnum<EnumDirtCover> COVER = new FlePropertyEnum(
			"cover", EnumDirtCover.class);
			
	static final FlePropertyString<Substance> dirtProperty = new FlePropertyString(
			"dirt", FleResource.dirt)
	{
		@Override
		public String getName(Comparable value)
		{
			return FleResource.dirt.get((String) value).getName();
		}
	};
	
	public BlockDirt(String unlocalized)
	{
		super(unlocalized, ItemDirt.class, Material.gourd);
		setTickRandomly(true);
		FarCore.registerTileEntity(TileEntityDirt.class, "fle.dirt",
				Substance.LOADER, Substance.SAVER, SpecieRegistry.LOADER,
				SpecieRegistry.SAVER);
		if (FLE.DEBUG)
			debug();
	}
	
	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}
	
	@Override
	public int getLightOpacity(IBlockAccess world, BlockPos pos)
	{
		return isSideSolide(world, pos, Direction.UP) ? 255 : 100;
	}
	
	@Override
	public boolean isSideSolide(IBlockAccess world, BlockPos pos,
			Direction side)
	{
		return getTile(world, pos).state == EnumDirtState.farmland
				? side != Direction.UP : true;
	}
	
	@Override
	protected IBlockState addDefaultropertiesTo(IBlockState state)
	{
		return state.withProperty(DIRT_STATE, EnumDirtState.dirt)
				.withProperty(COVER, EnumDirtCover.nothing);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state,
			EntityPlayer player)
	{
		return true;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, DIRT_STATE, COVER, dirtProperty);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityDirt();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn,
			BlockPos pos)
	{
		TileEntityDirt tile = getTile(worldIn, pos);
		if (tile != null)
			return state.withProperty(COVER, EnumDirtCover.nothing)
					.withProperty(dirtProperty, tile.dirt.getName())
					.withProperty(DIRT_STATE, tile.state);
		return super.getActualState(state, worldIn, pos);
	}
	
	@Override
	public float getBlockHardness(World worldIn, BlockPos pos)
	{
		return getDirt(worldIn, pos).getBlockHardness();
	}
	
	public Substance getDirt(IBlockState state)
	{
		return dirtProperty.getValue(getDirtName(state));
	}
	
	public Substance getDirt(ItemStack stack)
	{
		return dirtProperty.getValue(getDirtName(stack));
	}
	
	public Substance getDirt(World world, BlockPos pos)
	{
		return getTile(world, pos).dirt;
	}
	
	public String getDirtName(IBlockState state)
	{
		return (String) state.getValue(dirtProperty);
	}
	
	public String getDirtName(ItemStack stack)
	{
		return stack.getSubCompound("dirt", true).getString("dirt");
	}
	
	@Override
	protected List<ItemStack> getDrops(World world, BlockPos pos,
			IBlockState state, int fortune, TileEntity tile)
	{
		if (tile instanceof TileEntityDirt)
		{
			ItemStack drop = setDirt(new ItemStack(this),
					((TileEntityDirt) tile).dirt.getName(), EnumDirtState.dirt);
			((TileEntityDirt) tile).getNBTHandler().saveToNBT(tile,
					drop.getSubCompound("nutrition", true));
			ArrayList<ItemStack> list = new ArrayList();
			list.add(drop);
			return list;
		}
		return super.getDrops(world, pos, state, fortune, tile);
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos,
			Entity exploder, Explosion explosion)
	{
		return getBlockHardness(world, pos) * 1.5F;
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return getDirt(state).blockMineLevel;
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "shovel";
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
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (String tag : dirtProperty)
		{
			if ("void".equals(tag))
			{
				continue;
			}
			for (EnumDirtState state : EnumDirtState.values())
			{
				list.add(setDirt(new ItemStack(item), tag, EnumDirtState.dirt));
			}
		}
	}
	
	private TileEntityDirt getTile(IBlockAccess world, BlockPos pos)
	{
		return !(world.getTileEntity(pos) instanceof TileEntityDirt)
				? new TileEntityDirt()
				: (TileEntityDirt) world.getTileEntity(pos);
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return "shovel".equals(type);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack)
	{
		TileEntityDirt tile = getTile(worldIn, pos);
		if (tile != null)
		{
			NBTTagCompound nbt = stack.getSubCompound("nutrition", true);
			tile.getNBTHandler().loadFromNBT(tile, nbt);
			tile.dirt = getDirt(stack);
			tile.state = getState(stack);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	protected void onSilkHarvest(World world, EntityPlayer player, BlockPos pos,
			IBlockState state, TileEntity te)
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack itemstack;
		if (te instanceof TileEntityDirt)
		{
			itemstack = setDirt(new ItemStack(this, 1, getMetaFromState(state)),
					((TileEntityDirt) te).dirt.getName(),
					((TileEntityDirt) te).state);
			((TileEntityDirt) te).getNBTHandler().saveToNBT(te,
					itemstack.getSubCompound("nutrition", true));
		}
		else
		{
			itemstack = new ItemStack(Blocks.dirt);
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
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn,
			float fallDistance)
	{
		if (entityIn instanceof EntityLivingBase
				&& getTile(worldIn, pos).state == EnumDirtState.farmland)
		{
			if (!worldIn.isRemote
					&& worldIn.rand.nextFloat() < fallDistance - 0.5F)
			{
				if (!(entityIn instanceof EntityPlayer) && !worldIn
						.getGameRules().getGameRuleBooleanValue("mobGriefing"))
				{
					return;
				}
				
				getTile(worldIn, pos).state = EnumDirtState.dirt;
			}
			
		}
		super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos,
			IBlockState state, Entity entityIn)
	{
		switch ((EnumDirtCover) state.getValue(COVER)) {
		case snow:
		{
			entityIn.motionX *= 1.05D;
			entityIn.motionZ *= 1.05D;
			break;
		}
		case water:
		{
			entityIn.motionX *= 1.025D;
			entityIn.motionZ *= 1.025D;
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		if (worldIn.rand.nextInt(5) == 0 && worldIn.getBlockState(pos)
				.getValue(COVER) == EnumDirtCover.nothing)
		{
			worldIn.setBlockState(pos,
					worldIn.getBlockState(pos).withProperty(COVER,
							worldIn.getBiomeGenForCoords(pos).isSnowyBiome()
									? EnumDirtCover.snow
									: EnumDirtCover.water));
		}
		super.fillWithRain(worldIn, pos);
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos,
			EnumFacing direction, IPlantable plantable)
	{
		return plantable.getPlantType(world,
				pos.offset(direction)) == EnumPlantType.Crop
						? getTile(world, pos).state == EnumDirtState.farmland
						: BlockFactory.canSustainPlant(world, pos,
								getTile(world, pos).dirt, direction, plantable);
	}
	
	public EnumDirtState getState(ItemStack stack)
	{
		return EnumDirtState
				.valueOf(stack.getSubCompound("dirt", true).getString("state"));
	}
	
	public ItemStack setDirt(ItemStack stack, String tag, EnumDirtState state)
	{
		stack.getSubCompound("dirt", true).setString("dirt", tag);
		stack.getSubCompound("dirt", true).setString("state", state.name());
		return stack;
	}
	
	@Override
	public boolean canPropagatedable(World world, BlockPos pos,
			IPlantSpecies specie)
	{
		return specie instanceof PlantCover
				? getTile(world, pos).state == EnumDirtState.dirt
						|| getTile(world, pos).state == EnumDirtState.farmland
								&& getTile(world, pos).PH > 30000
								&& getTile(world, pos).PH < 75000
				: false;
	}
	
	@Override
	public void onPropagatedable(World world, BlockPos pos, IPlant plant)
	{
		TileEntityDirt tile = getTile(world, pos);
		if (tile != null)
		{
			if (plant.getSpecie() instanceof PlantCover)
			{
				tile.state = ((PlantCover) plant.getSpecie()).getState();
				tile.markForUpdate();
			}
			tile.plant = plant;
		}
	}
	
	@Override
	public IPlant getSpecie(World world, BlockPos pos)
	{
		return getTile(world, pos).plant;
	}
	
	@Override
	public boolean setDead(World world, BlockPos pos)
	{
		if (getTile(world, pos) != null)
		{
			getTile(world, pos).plant = null;
			return true;
		}
		return false;
	}
}