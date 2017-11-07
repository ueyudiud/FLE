/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.terria;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.data.CT;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.Materials;
import farcore.lib.block.BlockMaterial;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBlockable;
import nebula.base.IntegerMap;
import nebula.base.ObjArrayParseHelper;
import nebula.base.function.Selector;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.block.IHitByFallenBehaviorBlock;
import nebula.common.block.ISmartFallableBlock;
import nebula.common.data.Misc;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.util.OreDict;
import nebula.common.util.Players;
import nebula.common.util.Properties;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockSand extends BlockMaterial implements ISmartFallableBlock
{
	protected static boolean canFallBelow(World world, BlockPos pos, IBlockState state)
	{
		IBlockState state1;
		BlockPos pos1 = pos.down();
		state1 = world.getBlockState(pos1);
		if (state1.getBlock().isAir(state1, world, pos1))
			return true;
		else if (state1.getBlock() instanceof IHitByFallenBehaviorBlock)
		{
			IHitByFallenBehaviorBlock block = (IHitByFallenBehaviorBlock) state1.getBlock();
			return block.isPermeatableBy(world, pos1, state1, state);
		}
		else if (state1.getBlock() == state.getBlock())
		{
			return !state1.isFullBlock();
		}
		else
			return state1.getMaterial().isReplaceable();
	}
	
	protected static @Nullable IntegerMap<EnumFacing> canFallNearby(World world, BlockPos pos, IBlockState state)
	{
		IntegerMap<EnumFacing> result = new IntegerMap<>(6);
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			BlockPos pos2;
			IBlockState state1 = world.getBlockState(pos2 = pos.offset(facing));
			if (state1.getBlock().isAir(state1, world, pos2) || state1.getBlock().isReplaceable(world, pos2))
			{
				int height = calculateHeight(world, pos2.down(), state);
				if (height < 16) result.put(facing, height);
			}
		}
		return result;
	}
	
	private static int calculateHeight(World world, BlockPos pos, IBlockState state)
	{
		IBlockState state2 = world.getBlockState(pos);
		if (state2.getBlock().isAir(state2, world, pos))
		{
			state2 = world.getBlockState(pos.down());
			if (state2.getBlock() == state.getBlock())
			{
				return state2.getValue(LAYER) - 16;
			}
			else
				return BlockSoil.canFallBelow(world, pos, state) ? -16 : 0;
		}
		else if (state2.getBlock() == state.getBlock())
		{
			return state2.getValue(LAYER);
		}
		else
			return 16;
	}
	
	public static final int					MAX_HEIGHT	= 16;
	public static final IProperty<Integer>	LAYER		= Properties.create("layer", 1, MAX_HEIGHT);
	
	public BlockSand(String modid, String name, Mat mat, PropertyBlockable sand)
	{
		super(modid, name, Materials.SAND, mat, sand);
		this.uneffectiveSpeedMultiplier = 1F / 80F;
		this.effectiveSpeedMultiplier = 1F / 15F;
		setSoundType(SoundType.SAND);
		setTickRandomly(true);
		setCreativeTab(CT.TERRIA);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), MC.block.getLocal(this.material));
		OreDict.registerValid("sand", new ItemStack(this.item));
		OreDict.registerValid("sand" + this.material.oreDictName, new ItemStack(this.item));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt map = new StateMapperExt(this.material.modid, "sands", null);
		map.setVariants("type", this.material.name);
		Renders.registerCompactModel(map, this, MAX_HEIGHT);
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName() + ".name";
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return super.initDefaultState(state).withProperty(LAYER, MAX_HEIGHT);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, LAYER);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(LAYER) - 1) ^ 0xF;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(LAYER, (meta ^ 0xF) + 1);
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
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, EnumFacing facing, ItemStack stack)
	{
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 5;
	}
	
	protected boolean checkAndFall(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (canFallBelow(world, pos, state))
		{
			return fallBlockAt(world, pos, pos, state);
		}
		else
		{
			IntegerMap<EnumFacing> map = canFallNearby(world, pos, state);
			if (!map.isEmpty())
			{
				final int j = state.getValue(LAYER) - 4;
				map.transformAll(i -> i < j ? j - i : 0);
				if (map.getSum() > 0)
				{
					EnumFacing facing = Selector.of(map).next(rand);
					return fallBlockAt(world, pos, pos.offset(facing), state);
				}
			}
			return false;
		}
	}
	
	public static int fillBlockWith(World world, BlockPos pos, IBlockState state, int amount)
	{
		int t = Math.min(16 - state.getValue(LAYER), amount);
		world.setBlockState(pos, state.withProperty(LAYER, state.getValue(LAYER) + t));
		return amount - t;
	}
	
	protected boolean fallBlockAt(World world, BlockPos source, BlockPos pos, IBlockState state)
	{
		IBlockState state1;
		if (source != pos)
		{
			state1 = world.getBlockState(pos);
			if (state1.getBlock() == this)
			{
				int t = state.getValue(LAYER) + state1.getValue(LAYER);
				int r = t / 2;
				world.setBlockState(pos, state1.withProperty(LAYER, r));
				world.setBlockState(source, state1.withProperty(LAYER, t - r));
				return true;
			}
		}
		state1 = world.getBlockState(pos.down());
		if (state1.getBlock() == this)
		{
			int i = fillBlockWith(world, pos.down(), state1, state.getValue(LAYER));
			if (i > 0)
				world.setBlockState(source, state.withProperty(LAYER, i));
			else
				world.setBlockToAir(source);
			return true;
		}
		else
		{
			return Worlds.fallBlock(world, source, pos, state);
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			checkAndFall(worldIn, pos, state, rand);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return Misc.AABB_LAYER[state.getValue(LAYER) - 1];
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return super.getBlockHardness(blockState, worldIn, pos) * 0.125F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder)
	{
		return super.getExplosionResistance(exploder) * 0.8F;
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
		IBlockState state;
		int layer = (state = world.getBlockState(pos)).getValue(LAYER);
		int layer1 = layer - world.rand.nextInt(3 + layer) / 2;
		if (layer > 0)
		{
			if (layer != layer1)
			{
				world.setBlockState(pos, state.withProperty(LAYER, layer1));
			}
		}
		else
		{
			world.setBlockToAir(pos);
		}
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if (player.isCreative()) return super.removedByPlayer(state, world, pos, player, willHarvest);
		int l = state.getValue(LAYER);
		if (l > 1)
		{
			world.setBlockState(pos, state.withProperty(LAYER, l - 1));
			harvestBlock(world, player, pos, state, null, null);
			return false;
		}
		else
		{
			return super.removedByPlayer(state, world, pos, player, willHarvest);
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		return ObjArrayParseHelper.newArrayList(ItemMulti.createStack(this.material, MC.pile));
	}
	
	@Override
	public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state)
	{
		return !BlockSoil.canFallBelow(world, pos, state);
	}
	
	@Override
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		list.add(new ItemStack(item, 1, 0));
	}
	
	@Override
	public boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT)
	{
		IBlockState state2;
		if ((state2 = world.getBlockState(pos)).getBlock() == this)
		{
			int layer = state2.getValue(LAYER);
			if (layer < MAX_HEIGHT)
			{
				int layer2 = state.getValue(LAYER);
				if (layer + layer2 <= MAX_HEIGHT)
				{
					world.setBlockState(pos, state.withProperty(LAYER, layer + layer2));
				}
				else
				{
					world.setBlockState(pos, state.withProperty(LAYER, MAX_HEIGHT));
					world.setBlockState(pos.up(), state.withProperty(LAYER, layer + layer2 - MAX_HEIGHT));
				}
				return true;
			}
		}
		EntityFallingBlockExtended.replaceFallingBlock(world, pos, state, height);
		world.setBlockState(pos, state);
		return true;
	}
	
	@Override
	public boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT)
	{
		return false;
	}
	
	@Override
	public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target)
	{
		float amt = this.material.toolDamageToEntity;
		amt *= block.getBlock().getValue(LAYER);
		amt *= block.motionY * block.motionY;
		amt /= 25F;
		return amt;
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		float amt = 1F - this.material.getProperty(MP.fallen_damage_deduction) / 10000F;
		entityIn.fall(fallDistance, amt);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return isFullBlock(state);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return isFullBlock(state);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return isFullBlock(state);
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return state.getValue(LAYER) == 16;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		switch (side)
		{
		case DOWN:
			return true;
		default:
			return base_state.isFullBlock();
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		if (isFullBlock(state))
		{
			if (direction == EnumFacing.UP)
			{
				switch (plantable.getPlantType(world, pos.up()))
				{
				case Desert:
					return true;
				case Beach:
					return world.getBlockState(pos.east()).getMaterial() == Materials.WATER || world.getBlockState(pos.west()).getMaterial() == Materials.WATER || world.getBlockState(pos.north()).getMaterial() == Materials.WATER || world.getBlockState(pos.south()).getMaterial() == Materials.WATER;
				default:
					break;
				}
			}
		}
		return super.canSustainPlant(state, world, pos, direction, plantable);
	}
}
