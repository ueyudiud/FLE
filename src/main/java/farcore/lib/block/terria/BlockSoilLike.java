/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.terria;

import static nebula.common.data.Misc.PROP_EAST;
import static nebula.common.data.Misc.PROP_NORTH;
import static nebula.common.data.Misc.PROP_SOUTH;
import static nebula.common.data.Misc.PROP_UP;
import static nebula.common.data.Misc.PROP_WEST;

import java.util.List;
import java.util.Random;

import farcore.data.CT;
import farcore.data.ColorMultiplier;
import farcore.data.MC;
import farcore.data.V;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.block.BlockMaterial;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import farcore.lib.world.WorldPropHandler;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.util.Properties;
import nebula.common.util.Worlds;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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
 * @author ueyudiud
 */
public class BlockSoilLike<P extends PropertyBlockable<? super BlockSoilLike<?>>> extends BlockMaterial<P>
{
	public static final PropertyEnum<EnumCoverType> COVER_TYPE = Properties.get(EnumCoverType.class);
	
	protected BlockSoilLike(String modid, String name, Material materialIn, Mat mat, P property)
	{
		super(modid, name, materialIn, mat, property);
		setTickRandomly(true);
		setCreativeTab(CT.TERRIA);
	}
	
	protected BlockSoilLike(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn, Mat mat, P property)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn, mat, property);
		setTickRandomly(true);
		setCreativeTab(CT.TERRIA);
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
		// The meta higher than 5 can not be harvested, so no name display.
		MC.soil.registerOre(this.material, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		Renders.registerColorMultiplier(ColorMultiplier.SOIL_COLOR, this);
		Renders.registerColorMultiplier(ColorMultiplier.ITEM_SOIL_COLOR, this.item);
		Renders.registerCompactModel(new StateMapperExt(this.material.modid, "soil/" + this.material.name, null), this, EnumCoverType.VALUES.length);
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
		return state.withProperty(COVER_TYPE, EnumCoverType.NONE).withProperty(PROP_UP, true).withProperty(PROP_NORTH, false).withProperty(PROP_SOUTH, false).withProperty(PROP_EAST, false).withProperty(PROP_WEST, false);
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
		if (!state2.getBlock().isNormalCube(state2, worldIn, pos.up()) || state2.getMaterial() == Material.SNOW)
		{
			state = state.withProperty(PROP_UP, true).withProperty(PROP_NORTH, matchConnectable(state, worldIn, pos, EnumFacing.NORTH)).withProperty(PROP_SOUTH, matchConnectable(state, worldIn, pos, EnumFacing.SOUTH)).withProperty(PROP_EAST, matchConnectable(state, worldIn, pos, EnumFacing.EAST))
					.withProperty(PROP_WEST, matchConnectable(state, worldIn, pos, EnumFacing.WEST));
		}
		return state;
	}
	
	protected boolean matchConnectable(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		BlockPos pos1;
		IBlockState state1;
		if (Worlds.isSideSolid(world, pos1 = pos.offset(facing), facing.getOpposite(), true)) return false;
		return (state1 = world.getBlockState(pos1.down())).getBlock() instanceof BlockSoilLike<?> ? state.getValue(COVER_TYPE).noCover == state1.getValue(COVER_TYPE).noCover : false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (int i = 0; i < 5; ++i)
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
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
	{
		EnumCoverType type = state.getValue(COVER_TYPE);
		float hardness = super.getBlockHardness(state, worldIn, pos);
		if (type.isFrozen)
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
		if (type.isFrozen)
		{
			resistance *= 2.0F;
		}
		return resistance;
	}
	
	protected IBlockState updateBase(World worldIn, BlockPos pos, IBlockState state, Random rand, boolean update)
	{
		IBlockState oldState = state;
		EnumCoverType type = state.getValue(COVER_TYPE);
		if (type.noCover != EnumCoverType.NONE && type.noCover != EnumCoverType.FROZEN)
		{
			if (!canBlockGrass(worldIn, pos))
			{
				state = state.withProperty(COVER_TYPE, type = type.noCover == EnumCoverType.TUNDRA_FROZEN ? EnumCoverType.FROZEN : EnumCoverType.NONE);
			}
		}
		if (type.isFrozen)
		{
			if (ThermalNet.getTemperature(worldIn, pos, true) >= V.WATER_FREEZE_POINT_F + 25 && rand.nextInt(4) == 0)
			{
				state = state.withProperty(COVER_TYPE, type.noFrozen);
			}
		}
		if (oldState != state && update)
		{
			worldIn.setBlockState(pos, state, 2);// Already updated, will not
													// cause block update.
		}
		return state;
	}
	
	protected void spreadCoverPlant(World world, BlockPos pos, IBlockState state, Random rand)
	{
		EnumCoverType type = state.getValue(COVER_TYPE).noCover;
		if (type == EnumCoverType.NONE || type == EnumCoverType.FROZEN) return;
		MutableBlockPos pos2 = new MutableBlockPos();
		int brightness = world.getLightFromNeighbors(pos.up());
		int active;
		if (brightness < 9) return;
		float temperature = ThermalNet.getTemperature(world, pos, true);
		float humidity = WorldPropHandler.getWorldProperty(world).getHumidity(world, pos);
		switch (type)
		{
		case GRASS:
			if (temperature > V.WATER_FREEZE_POINT_F + 50)
				return;
			else if (temperature > V.WATER_FREEZE_POINT_F + 38)
			{
				active = 3;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F + 30)
			{
				active = 4;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F + 17)
			{
				active = 3;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F - 3)
			{
				active = 2;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F - 18)
			{
				active = 1;
			}
			else
				return;
			if (humidity < 0.1F)
				return;
			else if (humidity < 0.2F)
			{
				--active;
			}
			else if (humidity > 0.8F)
			{
				++active;
			}
			if (brightness > 12 && rand.nextInt(4) == 0)
			{
				++active;
			}
			break;
		case MYCELIUM:
			if (temperature > V.WATER_FREEZE_POINT_F + 47)
				return;
			else if (temperature > V.WATER_FREEZE_POINT_F + 34)
			{
				active = 3;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F + 27)
			{
				active = 4;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F + 19)
			{
				active = 3;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F + 4)
			{
				active = 2;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F - 7)
			{
				active = 1;
			}
			else
				return;
			if (humidity < 0.3F)
				return;
			else if (humidity > 0.5F)
			{
				++active;
			}
			else if (humidity > 0.8F)
			{
				active += 2;
			}
			break;
		case TUNDRA:
		case TUNDRA_FROZEN:
			if (temperature > V.WATER_FREEZE_POINT_F + 38)
				return;
			else if (temperature > V.WATER_FREEZE_POINT_F + 17)
			{
				active = 3;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F + 4)
			{
				active = 2;
			}
			else if (temperature > V.WATER_FREEZE_POINT_F - 7)
			{
				active = 1;
			}
			else
				return;
			if (humidity < 0.025F)
				return;
			else if (humidity > 0.28F)
			{
				active++;
			}
			break;
		default:
			return;
		}
		for (int i = 0; i < active; ++i)
		{
			pos2.setPos(pos.getX() + rand.nextInt(5) - 3, pos.getY() + rand.nextInt(3) - 1, pos.getZ() + rand.nextInt(5) - 3);
			if (pos2.getY() < 0 || pos2.getY() >= 256 || !world.isBlockLoaded(pos2) || pos2.equals(pos))
			{
				continue;
			}
			IBlockState state1 = world.getBlockState(pos2);
			if (state1.getBlock() instanceof BlockSoil && canBlockGrass(world, pos2))
			{
				EnumCoverType type1 = state1.getValue(COVER_TYPE);
				if (type1.noCover != EnumCoverType.NONE)
				{
					continue;
				}
				if (type1.isSnow)
				{
					type = type.snowCover;
				}
				world.setBlockState(pos2, state1.withProperty(COVER_TYPE, type), 3);
			}
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		EnumCoverType type = state.getValue(COVER_TYPE);
		switch (plantable.getPlantType(world, pos.offset(direction)))
		{
		case Plains:
			return !type.isFrozen;
		default:
			return super.canSustainPlant(state, world, pos, direction, plantable);
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
			case GRASS:
			case TUNDRA:
			case MYCELIUM:
				type = EnumCoverType.NONE;
				break;
			default:
				break;
			}
			world.setBlockState(pos, state.withProperty(COVER_TYPE, type));
		}
	}
	
	protected boolean canBlockGrass(IBlockAccess world, BlockPos pos)
	{
		BlockPos up = pos.up();
		IBlockState state = world.getBlockState(up);
		return state.getBlock().isAir(state, world, up) || (!state.getBlock().isNormalCube(state, world, up) && !(state.getBlock() instanceof BlockFluidBase) && !(state.getBlock() instanceof BlockLiquid));
	}
}
