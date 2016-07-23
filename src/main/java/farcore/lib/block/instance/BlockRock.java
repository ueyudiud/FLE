package farcore.lib.block.instance;

import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.data.MC;
import farcore.lib.block.BlockBase;
import farcore.lib.block.IBurnCustomBehaviorBlock;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.material.Mat;
import farcore.lib.util.Direction;
import farcore.lib.util.LanguageManager;
import farcore.util.U;
import farcore.util.U.OreDict;
import farcore.util.U.Worlds;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRock extends BlockBase implements ISmartFallableBlock, IBurnCustomBehaviorBlock
{
	public static final PropertyBool HEATED = PropertyBool.create("heated");
	public static final PropertyEnum<RockType> ROCK_TYPE = PropertyEnum.create("rock_type", RockType.class);
	
	public static enum RockType implements IStringSerializable
	{
		resource("%s"),
		cobble("Craked %s"),
		cobble_art("%s Cobble"),
		smoothed("Smoothed %s"),
		mossy("Mossy %s"),
		brick("%s Brick"),
		brick_crushed("Cracked %s Brick"),
		brick_mossy("Mossy %s Brick"),
		brick_compacted("Compacted %s Brick"),
		chiseled("Chiseled %s");
		
		static
		{
			resource.noSilkTouchDropMeta = cobble_art.ordinal();
			cobble.noSilkTouchDropMeta = cobble_art.ordinal();
			cobble.displayInTab = false;
			mossy.burnable = true;
			brick_mossy.burnable = true;
		}
		
		int noMossy = ordinal();
		int noSilkTouchDropMeta = ordinal();
		boolean burnable;
		boolean displayInTab = true;
		String local;
		
		private RockType(String local)
		{
			this.local = local;
		}
		
		@Override
		public String getName()
		{
			return name();
		}
		
		public RockType burned()
		{
			return values()[noMossy];
		}
	}
	
	public final Mat material;
	public final float hardnessMultiplier;
	public final float resistanceMultiplier;
	public final int harvestLevel;
	private final float detTempCap;
	public final BlockRockSlab[] slabGroup;
	
	public BlockRock(String name, Mat material, String localName)
	{
		super(material.modid, name, Material.ROCK);
		this.material = material;
		harvestLevel = material.blockHarvestLevel;
		detTempCap = material.minDetHeatForExplosion;
		slabGroup = makeSlabs(name, material, localName);
		setSoundType(SoundType.STONE);
		setHardness(hardnessMultiplier = material.blockHardness);
		setResistance(resistanceMultiplier = material.blockExplosionResistance);
		setCreativeTab(FarCore.tabResourceBlock);
		setTickRandomly(true);
		setDefaultState(getDefaultState().withProperty(HEATED, false));
		
		for(RockType type : RockType.values())
		{
			LanguageManager.registerLocal(getTranslateNameForItemStack(type.ordinal()), String.format(type.local, localName));
		}
		MC.stone.registerOre(material, new ItemStack(this, 1, 0));
		MC.stone.registerOre(material, new ItemStack(this, 1, 1));
		MC.cobble.registerOre(material, new ItemStack(this, 1, 2));
		MC.cobble.registerOre(material, new ItemStack(this, 1, 4));
		MC.brick.registerOre(material, new ItemStack(this, 1, 5));
		MC.brick.registerOre(material, new ItemStack(this, 1, 6));
		MC.brick.registerOre(material, new ItemStack(this, 1, 7));
		MC.brick.registerOre(material, new ItemStack(this, 1, 8));
		MC.brick.registerOre(material, new ItemStack(this, 1, 9));
		OreDict.registerValid("stoneSmoothed" + material.oreDictName, new ItemStack(this, 1, 3));
		OreDict.registerValid("stoneSlab" + material.oreDictName, slabGroup[0]);
		OreDict.registerValid("cobbleSlab" + material.oreDictName, slabGroup[2]);
		OreDict.registerValid("stoneSmoothedSlab" + material.oreDictName, slabGroup[3]);
		OreDict.registerValid("cobbleSlab" + material.oreDictName, slabGroup[4]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[5]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[6]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[7]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[8]);
		OreDict.registerValid("brickSlab" + material.oreDictName, slabGroup[9]);
		Item item = Item.getItemFromBlock(this);
		for(RockType type : RockType.values())
		{
			U.Mod.registerItemBlockModel(item, type.ordinal(), material.modid, "rock/" + material.name + "/" +
					(type == RockType.cobble_art ? RockType.cobble.name() : type.name()));
		}
	}
	
	protected BlockRockSlab[] makeSlabs(String name, Mat material, String localName)
	{
		BlockRockSlab[] ret = new BlockRockSlab[RockType.values().length];
		for(RockType type : RockType.values())
		{
			ret[type.ordinal()] = new BlockRockSlab(type.ordinal(), this, ret, name + "." + type.name(), material, String.format(type.local, localName));
		}
		return ret;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_TYPE, HEATED);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ROCK_TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(ROCK_TYPE, RockType.values()[meta]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(RockType type : RockType.values())
			if(type.displayInTab)
			{
				list.add(new ItemStack(itemIn, 1, type.ordinal()));
			}
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		RockType type = state.getValue(ROCK_TYPE);
		switch (type)
		{
		case cobble_art:
			return 1;
		case cobble :
		case mossy :
			return harvestLevel / 2;
		default:
			return harvestLevel;
		}
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		RockType type = state.getValue(ROCK_TYPE);
		switch (type)
		{
		default:
			break;
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!canBlockStay(worldIn, pos, state))
		{
			Worlds.fallBlock(worldIn, pos, state);
		}
	}
	
	public boolean canBlockStay(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		RockType type = state.getValue(ROCK_TYPE);
		switch (type)
		{
		case cobble :
		case cobble_art :
			return world.isSideSolid(pos.down(), EnumFacing.UP, false);
		default:
			return true;
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		if(world instanceof World)
		{
			((World) world).scheduleUpdate(pos, this, tickRate((World) world));
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if(!canBlockStay(worldIn, pos, state))
		{
			Worlds.fallBlock(worldIn, pos, state);
		}
	}
	
	@Override
	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
	{
		IBlockState state;
		RockType type = (state = world.getBlockState(pos)).getValue(ROCK_TYPE);
		if(type.burnable)
		{
			world.setBlockState(pos, state.withProperty(ROCK_TYPE, type.burned()), 3);
		}
		return false;
	}
	
	@Override
	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir)
	{
		return false;
	}
	
	@Override
	public float getThermalConduct(World world, BlockPos pos)
	{
		return material.thermalConduct;
	}
	
	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public void onStartFalling(World world, BlockPos pos)
	{
		
	}
	
	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return canBlockStay(world, pos, state);
	}
	
	@Override
	public boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	@Override
	public boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		return (float) ((1.0F + material.toolDamageToEntity) * block.motionY * block.motionY * 0.25F);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(ROCK_TYPE).burnable;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return isFlammable(world, pos, face) ? 40 : 0;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 0;
	}
}