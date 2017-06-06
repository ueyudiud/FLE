/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.instance;

import static nebula.common.data.Misc.PROP_EAST;
import static nebula.common.data.Misc.PROP_NORTH;
import static nebula.common.data.Misc.PROP_SOUTH;
import static nebula.common.data.Misc.PROP_UP;
import static nebula.common.data.Misc.PROP_WEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.CT;
import farcore.data.ColorMultiplier;
import farcore.data.EnumBlock;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.V;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.BlockMaterial;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.world.WorldPropHandler;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.block.IHitByFallenBehaviorBlock;
import nebula.common.block.ISmartFallableBlock;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.util.L;
import nebula.common.util.Players;
import nebula.common.util.Properties;
import nebula.common.util.Properties.EnumStateName;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This provide the ground related block, which is fallable.
 * @author ueyudiud
 *
 */
public class BlockSoil extends BlockMaterial implements ISmartFallableBlock
{
	protected static boolean canFallBelow(World world, BlockPos pos, IBlockState state)
	{
		BlockPos pos1 = pos.down();
		IBlockState state1 = world.getBlockState(pos1);
		if(state1.getBlock().isAir(state1, world, pos1))
			return true;
		else if(state1.getBlock() instanceof IHitByFallenBehaviorBlock)
		{
			IHitByFallenBehaviorBlock block = (IHitByFallenBehaviorBlock) state1.getBlock();
			return block.isPermeatableBy(world, pos1, state1, state);
		}
		else return state1.getMaterial().isReplaceable();
	}
	protected static List<EnumFacing> canFallNearby(World world, BlockPos pos, IBlockState state)
	{
		BlockPos pos1 = pos.down();
		List<EnumFacing> result = new ArrayList();
		BlockPos pos2;
		pos2 = pos1.north();
		if(canFallBelow(world, pos2, state))
		{
			result.add(EnumFacing.NORTH);
		}
		pos2 = pos1.south();
		if(canFallBelow(world, pos2, state))
		{
			result.add(EnumFacing.SOUTH);
		}
		pos2 = pos1.west();
		if(canFallBelow(world, pos2, state))
		{
			result.add(EnumFacing.WEST);
		}
		pos2 = pos1.east();
		if(canFallBelow(world, pos2, state))
		{
			result.add(EnumFacing.EAST);
		}
		return result;
	}
	
	@EnumStateName("cover")
	public static enum EnumCoverType implements IStringSerializable
	{
		NONE("none"),
		FROZEN("frozen"),
		GRASS("grass"),
		TUNDRA("tundra"),
		MYCELIUM("mycelium"),
		TUNDRA_FROZEN("tundra_frozen"),
		WATER("water"),
		GRASS_WATER("grass_water"),
		TUNDRA_WATER("tundra_water"),
		MYCELIUM_WATER("mycelium_water"),
		SNOW("snow"),
		FROZEN_SNOW("frozen_snow"),
		GRASS_SNOW("grass_snow"),
		TUNDRA_SNOW("tundra_snow"),
		TUNDRA_FROZEN_SNOW("tundra_frozen_snow"),
		MYCELIUM_SNOW("mycelium_snow");
		
		public static final EnumCoverType[] VALUES = values();
		
		static
		{
			setCover(NONE, WATER, SNOW);
			setCover(GRASS, GRASS_WATER, GRASS_SNOW);
			setCover(TUNDRA, TUNDRA_WATER, TUNDRA_SNOW);
			setCover(MYCELIUM, MYCELIUM_WATER, MYCELIUM_SNOW);
			setCover(FROZEN, FROZEN, FROZEN_SNOW);
			setCover(TUNDRA_FROZEN, TUNDRA_FROZEN, TUNDRA_FROZEN_SNOW);
			FROZEN.noFrozen = FROZEN_SNOW.noFrozen = NONE;
			TUNDRA_FROZEN.noFrozen = TUNDRA_FROZEN_SNOW.noFrozen = TUNDRA;
			TUNDRA_FROZEN.isWet = FROZEN.isWet = false;
			FROZEN.isFrozen = FROZEN_SNOW.isFrozen = TUNDRA_FROZEN.isFrozen = TUNDRA_FROZEN_SNOW.isFrozen = true;
		}
		
		static void setCover(EnumCoverType no, EnumCoverType water, EnumCoverType snow)
		{
			no.noCover    = water.noCover    = snow.noCover    = no;
			no.waterCover = water.waterCover = snow.waterCover = water;
			no.snowCover  = water.snowCover  = snow.snowCover  = snow;
			water.isWet = true;
			snow.isSnow = true;
		}
		
		String name;
		EnumCoverType noFrozen = this;
		EnumCoverType noCover = this;
		EnumCoverType waterCover = this;
		EnumCoverType snowCover = this;
		boolean isWet;
		boolean isSnow;
		boolean isFrozen;
		
		EnumCoverType(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return this.name;
		}
		
		public EnumCoverType getNoCover()
		{
			return this.noCover;
		}
		
		public EnumCoverType getWaterCover()
		{
			return this.waterCover;
		}
		
		public EnumCoverType getSnowCover()
		{
			return this.snowCover;
		}
	}
	
	public static final PropertyEnum<EnumCoverType> COVER_TYPE = Properties.get(EnumCoverType.class);
	
	public BlockSoil(String modid, String name, Material materialIn, Mat mat, PropertyBlockable soil)
	{
		super(modid, name, materialIn, mat, soil);
		setTickRandomly(true);
		this.uneffectiveSpeedMultiplier = 1F / 150F;
		this.effectiveSpeedMultiplier = 1F / 20F;
		setCreativeTab(CT.tabTerria);
	}
	
	public BlockSoil(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn, Mat mat, PropertyBlockable soil)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn, mat, soil);
		setTickRandomly(true);
		this.uneffectiveSpeedMultiplier = 150F;
		this.effectiveSpeedMultiplier = 20F;
		setCreativeTab(CT.tabTerria);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), this.material.localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Frozen " + this.material.localName);
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), this.material.localName + " Grass");
		LanguageManager.registerLocal(getTranslateNameForItemStack(3), this.material.localName + " Tundra");
		LanguageManager.registerLocal(getTranslateNameForItemStack(4), this.material.localName + " Mycelium");
		//The meta higher than 5 can not be harvested, so no name display.
		MC.soil.registerOre(this.material, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		Renders.registerColorMultiplier(ColorMultiplier.SOIL_COLOR, this);
		Renders.registerColorMultiplier(ColorMultiplier.ITEM_SOIL_COLOR, this.item);
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "soil", null);
		mapper.setVariants("type", this.material.name);
		Renders.registerCompactModel(mapper, this, 16);
	}
	
	@Override
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		return getTranslateNameForItemStack(EnumCoverType.VALUES[stack.getItemDamage()].noCover.ordinal());
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, COVER_TYPE, PROP_UP, PROP_SOUTH, PROP_NORTH, PROP_EAST, PROP_WEST);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(COVER_TYPE, EnumCoverType.NONE)
				.withProperty(PROP_UP, true)
				.withProperty(PROP_NORTH, false)
				.withProperty(PROP_SOUTH, false)
				.withProperty(PROP_EAST, false)
				.withProperty(PROP_WEST, false);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(COVER_TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(COVER_TYPE, EnumCoverType.VALUES[meta]);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		IBlockState state2 = worldIn.getBlockState(pos.up());
		if(!state2.getBlock().isNormalCube(state2, worldIn, pos.up()) || state2.getMaterial() == Material.SNOW)
		{
			state = state.withProperty(PROP_UP, true)
					.withProperty(PROP_NORTH, matchConnectable(state, worldIn, pos, EnumFacing.NORTH))
					.withProperty(PROP_SOUTH, matchConnectable(state, worldIn, pos, EnumFacing.SOUTH))
					.withProperty(PROP_EAST, matchConnectable(state, worldIn, pos, EnumFacing.EAST))
					.withProperty(PROP_WEST, matchConnectable(state, worldIn, pos, EnumFacing.WEST));
		}
		return state;
	}
	
	protected boolean matchConnectable(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		BlockPos pos1;
		IBlockState state1;
		if(Worlds.isSideSolid(world, pos1 = pos.offset(facing), facing.getOpposite(), true)) return false;
		return (state1 = world.getBlockState(pos1.down())).getBlock() instanceof BlockSoil ?
				state.getValue(COVER_TYPE).noCover == state1.getValue(COVER_TYPE).noCover : false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for(int i = 0; i < 5; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean canBreakBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean canBreakEffective(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return Players.getCurrentToolType(player).contains(EnumToolTypes.SHOVEL);
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
	{
		EnumCoverType type = state.getValue(COVER_TYPE);
		float hardness = super.getBlockHardness(state, worldIn, pos);
		if(type.isFrozen)
		{
			hardness *= 2.4F;
		}
		else if(type.isWet)
		{
			hardness *= 1.2F;
		}
		return hardness;
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		EnumCoverType type = world.getBlockState(pos).getValue(COVER_TYPE);
		float resistance = super.getExplosionResistance(world, pos, exploder, explosion);
		if(type.isFrozen)
		{
			resistance *= 2.0F;
		}
		return resistance;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			EnumFacing facing, ItemStack stack)
	{
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		super.neighborChanged(state, worldIn, pos, blockIn);
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	protected boolean checkAndFall(World world, BlockPos pos, IBlockState state, Random rand, boolean checkFallToNearby)
	{
		if (canFallBelow(world, pos, state))
			return Worlds.fallBlock(world, pos, state);
		if (checkFallToNearby)
		{
			List<EnumFacing> sides = canFallNearby(world, pos, state);
			switch (state.getValue(COVER_TYPE).noCover)
			{
			case NONE :
				if(sides.size() >= 2)
					return Worlds.fallBlock(world, pos, pos.down().offset(L.random(sides, rand)), state);
				break;
			case FROZEN :
				if(!sides.isEmpty())
					return Worlds.fallBlock(world, pos, pos.down().offset(L.random(sides, rand)), state);
				break;
			case GRASS :
			case MYCELIUM :
			case TUNDRA :
			case TUNDRA_FROZEN :
				if(sides.size() >= 3 || (sides.size() == 2 && rand.nextInt(5) == 0))
					return Worlds.fallBlock(world, pos, pos.down().offset(L.random(sides, rand)), state);
				break;
			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if (!worldIn.isRemote)
		{
			state = updateBase(worldIn, pos, state, random);
			if(state == null) return;
			spreadCoverPlant(worldIn, pos, state, random);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			updateBase(worldIn, pos, state, rand);
		}
	}
	
	protected void spreadCoverPlant(World world, BlockPos pos, IBlockState state, Random rand)
	{
		EnumCoverType type = state.getValue(COVER_TYPE).noCover;
		if(type == EnumCoverType.NONE || type == EnumCoverType.FROZEN) return;
		MutableBlockPos pos2 = new MutableBlockPos();
		int brightness = world.getLightFromNeighbors(pos.up());
		int active;
		if (brightness < 9) return;
		float temperature = ThermalNet.getTemperature(world, pos, true);
		float humidity = WorldPropHandler.getWorldProperty(world).getHumidity(world, pos);
		switch (type)
		{
		case GRASS :
			if(temperature > V.WATER_FREEZE_POINT_F + 50) return;
			else if(temperature > V.WATER_FREEZE_POINT_F + 38) { active = 3; }
			else if (temperature > V.WATER_FREEZE_POINT_F + 30) { active = 4; }
			else if (temperature > V.WATER_FREEZE_POINT_F + 17) { active = 3; }
			else if(temperature > V.WATER_FREEZE_POINT_F - 3) { active = 2; }
			else if(temperature > V.WATER_FREEZE_POINT_F - 18) { active = 1; }
			else return;
			if(humidity < 0.1F) return;
			else if(humidity < 0.2F) { --active; }
			else if(humidity > 0.8F) { ++active; }
			if(brightness > 12 && rand.nextInt(4) == 0) { ++active; }
			break;
		case MYCELIUM :
			if(temperature > V.WATER_FREEZE_POINT_F + 47) return;
			else if(temperature > V.WATER_FREEZE_POINT_F + 34) { active = 3; }
			else if (temperature > V.WATER_FREEZE_POINT_F + 27) { active = 4; }
			else if (temperature > V.WATER_FREEZE_POINT_F + 19) { active = 3; }
			else if(temperature > V.WATER_FREEZE_POINT_F + 4) { active = 2; }
			else if(temperature > V.WATER_FREEZE_POINT_F - 7) { active = 1; }
			else return;
			if(humidity < 0.3F) return;
			else if(humidity > 0.5F) { ++active; }
			else if(humidity > 0.8F) { active += 2; }
			break;
		case TUNDRA :
		case TUNDRA_FROZEN :
			if(temperature > V.WATER_FREEZE_POINT_F + 38) return;
			else if(temperature > V.WATER_FREEZE_POINT_F + 17) { active = 3; }
			else if(temperature > V.WATER_FREEZE_POINT_F + 4) { active = 2; }
			else if(temperature > V.WATER_FREEZE_POINT_F - 7) { active = 1; }
			else return;
			if(humidity < 0.025F) return;
			else if(humidity > 0.28F) { active ++; }
			break;
		default : return;
		}
		for(int i = 0; i < active; ++i)
		{
			pos2.setPos(pos.getX() + rand.nextInt(5) - 3, pos.getY() + rand.nextInt(3) - 1, pos.getZ() + rand.nextInt(5) - 3);
			if(pos2.getY() < 0 || pos2.getY() >= 256 || !world.isBlockLoaded(pos2) || pos2.equals(pos))
			{
				continue;
			}
			IBlockState state1 = world.getBlockState(pos2);
			if(state1.getBlock() instanceof BlockSoil && canBlockGrass(world, pos2))
			{
				EnumCoverType type1 = state1.getValue(COVER_TYPE);
				if(type1.noCover != EnumCoverType.NONE)
				{
					continue;
				}
				if(type1.isSnow) { type = type.snowCover; }
				if(type1.isWet) { type = type.waterCover; }
				world.setBlockState(pos2, state1.withProperty(COVER_TYPE, type), 3);
			}
		}
	}
	
	protected IBlockState updateBase(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		IBlockState oldState = state;
		boolean wet = Worlds.isBlockNearby(worldIn, pos, EnumBlock.water.block, true);
		EnumCoverType type = state.getValue(COVER_TYPE);
		if(type.noCover != EnumCoverType.NONE && type.noCover != EnumCoverType.FROZEN)
		{
			if(!canBlockGrass(worldIn, pos))
			{
				state = state.withProperty(COVER_TYPE, type = type.noCover == EnumCoverType.TUNDRA_FROZEN ? EnumCoverType.FROZEN : EnumCoverType.NONE);
			}
		}
		if(type.isFrozen)
		{
			if(ThermalNet.getTemperature(worldIn, pos, true) >= V.WATER_FREEZE_POINT_F + 25 && rand.nextInt(4) == 0)
			{
				state = state.withProperty(COVER_TYPE, type.noFrozen);
			}
		}
		else if(type.isSnow)
		{
			if(ThermalNet.getTemperature(worldIn, pos, true) >= V.WATER_FREEZE_POINT_F && rand.nextInt(4) == 0)
			{
				state = state.withProperty(COVER_TYPE, type.waterCover);
			}
		}
		else
		{
			if(!type.isWet && wet)
			{
				state = state.withProperty(COVER_TYPE, type.waterCover);
			}
			else if(!wet && wet && rand.nextInt(8) == 0)
			{
				state = state.withProperty(COVER_TYPE, type.noCover);
			}
			wet |= type.isWet;
		}
		if(checkAndFall(worldIn, pos, state, rand, !wet)) return null;
		if(oldState != state)
		{
			worldIn.setBlockState(pos, state, 3);
		}
		return state;
	}
	
	protected boolean canBlockGrass(IBlockAccess world, BlockPos pos)
	{
		BlockPos up = pos.up();
		IBlockState state = world.getBlockState(up);
		return state.getBlock().isAir(state, world, up) ||
				(!state.getBlock().isNormalCube(state, world, up) &&
						!(state.getBlock() instanceof BlockFluidBase) &&
						!(state.getBlock() instanceof BlockLiquid));
	}
	
	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		if(canFallBelow(world, pos, state))
			return false;
		return canFallNearby(world, pos, state).size() < 2;
	}
	
	@Override
	public void onStartFalling(World world, BlockPos pos)
	{
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
		EnumCoverType type = block.getBlock().getValue(COVER_TYPE);
		float amt = this.material.getProperty(MP.property_tool).damageToEntity;
		amt *= block.motionY * block.motionY;
		amt /= 25F;
		return type.isFrozen ? amt * 1.2F : amt;
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		float amt = 1F - this.material.getProperty(MP.fallen_damage_deduction) / 10000F;
		EnumCoverType type = worldIn.getBlockState(pos).getValue(COVER_TYPE);
		if(type.isWet)
		{
			amt *= 0.6F;
		}
		else if(type.isSnow)
		{
			amt *= 0.8F;
		}
		entityIn.fall(fallDistance, amt);
	}
	
	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		super.fillWithRain(worldIn, pos);
		IBlockState state = worldIn.getBlockState(pos);
		EnumCoverType type = state.getValue(COVER_TYPE);
		boolean flag = worldIn.canSnowAt(pos, false);
		if(!flag)
		{
			if(worldIn.rand.nextInt(6) == 0)
			{
				worldIn.setBlockState(pos, state.withProperty(COVER_TYPE, type.waterCover), 3);
			}
		}
		else if(flag)
		{
			if(worldIn.rand.nextInt(type.isWet ? 12 : 7) == 0)
			{
				worldIn.setBlockState(pos, state.withProperty(COVER_TYPE, type.snowCover), 3);
			}
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		if(state.getValue(COVER_TYPE).isWet)
		{
			entityIn.motionX *= 0.85F;
			entityIn.motionZ *= 0.85F;
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
			IPlantable plantable)
	{
		EnumCoverType type = state.getValue(COVER_TYPE);
		switch (plantable.getPlantType(world, pos.offset(direction)))
		{
		case Plains : return !type.isFrozen;
		case Water : return !type.isFrozen && type.isWet;
		default : return super.canSustainPlant(state, world, pos, direction, plantable);
		}
	}
	
	@Override
	public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
	{
		if (!canBlockGrass(world, pos))
		{
			EnumCoverType type;
			switch (type = state.getValue(COVER_TYPE).noCover)
			{
			case GRASS :
			case TUNDRA :
			case MYCELIUM :
				type = EnumCoverType.NONE;
				break;
			default:
				break;
			}
			world.setBlockState(pos, state.withProperty(COVER_TYPE, type));
		}
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(COVER_TYPE).isFrozen ? EnumCoverType.FROZEN.ordinal() : EnumCoverType.NONE.ordinal();
	}
}