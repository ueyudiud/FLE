package farcore.block;

import java.util.ArrayList;
import java.util.List;

import farcore.block.item.ItemBlockBase;
import farcore.util.Direction;
import farcore.util.FarHook;
import flapi.FleAPI;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * The base class of block.
 * 
 * @author ueyudiud
 *
 */
public class BlockBase extends Block implements IHarvestCheck
{
	protected String unlocalized;

	private boolean useFleLang = false;

	protected ThreadLocal<TileEntity> tileThread = new ThreadLocal<TileEntity>();

	public BlockBase(String unlocalized, Class<? extends ItemBlockBase> clazz,
			Material materialIn)
	{
		super(materialIn);
		this.unlocalized = unlocalized;
		GameRegistry.registerBlock(this, clazz, unlocalized);
		setUnlocalizedName(unlocalized);
		setDefaultState(addDefaultropertiesTo(blockState.getBaseState()));
	}

	public BlockBase(String unlocalized, Material materialIn)
	{
		super(materialIn);
		this.unlocalized = unlocalized;
		GameRegistry.registerBlock(this, ItemBlockBase.class, unlocalized);
		setUnlocalizedName(unlocalized);
	}

	protected IBlockState addDefaultropertiesTo(IBlockState state)
	{
		return state;
	}

	@Override
	public boolean canBeHarvested(IBlockState state, String tool, int toolLevel)
	{
		String t = getHarvestTool(state);
		return t == null ? true
				: t.equals(tool) && getHarvestLevel(state) <= toolLevel;
	}

	public boolean canConnectRedstone(IBlockAccess world, BlockPos pos,
			Direction side)
	{
		return canProvidePower();
	}

	@Override
	public final boolean canConnectRedstone(IBlockAccess world, BlockPos pos,
			EnumFacing side)
	{
		return side == null ? false
				: canConnectRedstone(world, pos, Direction.toDirection(side));
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos,
			EntityPlayer player)
	{
		return FarHook.canHarvestBlock(this, player, world, pos);
	}

	public boolean canPlaceBlockOnSide(World world, BlockPos pos,
			Direction side)
	{
		return canPlaceBlockAt(world, pos);
	}

	@Override
	public final boolean canPlaceBlockOnSide(World worldIn, BlockPos pos,
			EnumFacing side)
	{
		return canPlaceBlockOnSide(worldIn, pos, Direction.toDirection(side));
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos,
			IBlockState state, float chance, int fortune)
	{
		// do not drop items while restoring blockstates, prevents item dupe.
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
		{
			List<ItemStack> items = getDrops(worldIn, pos, state, fortune,
					tileThread.get());
			chance = ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos,
					state, fortune, chance, false, harvesters.get());

			for (ItemStack item : items)
			{
				if (worldIn.rand.nextFloat() <= chance)
				{
					spawnAsEntity(worldIn, pos, item);
				}
			}
		}
	}

	protected List<ItemStack> getDrops(World world, BlockPos pos,
			IBlockState state, int fortune, TileEntity tileEntity)
	{
		return getDrops(world, pos, state, fortune);
	}

	protected int getFireSpreadSpeed()
	{
		return Blocks.fire.getEncouragement(this);
	}

	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos,
			Direction face)
	{
		return getFireSpreadSpeed();
	}

	@Override
	public final int getFireSpreadSpeed(IBlockAccess world, BlockPos pos,
			EnumFacing face)
	{
		return getFireSpreadSpeed(world, pos, Direction.toDirection(face));
	}

	protected int getFlammability()
	{
		return Blocks.fire.getFlammability(this);
	}

	public int getFlammability(IBlockAccess world, BlockPos pos, Direction face)
	{
		return getFlammability();
	}

	@Override
	public final int getFlammability(IBlockAccess world, BlockPos pos,
			EnumFacing face)
	{
		return getFlammability(world, pos, Direction.toDirection(face));
	}

	protected Object[] getLocalizedControlObj()
	{
		return new Object[0];
	}

	@Override
	public String getLocalizedName()
	{
		return useFleLang ? FleAPI.lang.translateToLocal(getUnlocalizedName(),
				getLocalizedControlObj()) : super.getLocalizedName();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return super.getMetaFromState(state);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "fle." + unlocalized;
	}

	protected Direction[] getValidDirections(World world, BlockPos pos)
	{
		return null;
	}

	@Override
	public final EnumFacing[] getValidRotations(World world, BlockPos pos)
	{
		return Direction.getAccessFacing(getValidDirections(world, pos));
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos,
			IBlockState state, TileEntity te)
	{
		player.triggerAchievement(
				StatList.mineBlockStatArray[getIdFromBlock(this)]);
		player.addExhaustion(0.025F);

		if (canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player)
				&& EnchantmentHelper.getSilkTouchModifier(player))
		{
			onSilkHarvest(worldIn, player, pos, state, te);
		}
		else
		{
			harvesters.set(player);
			tileThread.set(te);
			int i = EnchantmentHelper.getFortuneModifier(player);
			dropBlockAsItem(worldIn, pos, state, i);
			harvesters.set(null);
			tileThread.set(null);
		}
	}

	@Override
	public final boolean isSideSolid(IBlockAccess world, BlockPos pos,
			EnumFacing side)
	{
		return isSideSolide(world, pos, Direction.toDirection(side));
	}

	public boolean isSideSolide(IBlockAccess world, BlockPos pos,
			Direction side)
	{
		return isNormalCube(world, pos);
	}

	public boolean onBlockActivated(World world, BlockPos pos,
			IBlockState state, EntityPlayer player, Direction side, Vec3 vec)
	{
		return false;
	}

	@Override
	public final boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		return onBlockActivated(worldIn, pos, state, playerIn,
				Direction.toDirection(side), new Vec3(hitX - pos.getX(),
						hitY - pos.getY(), hitZ - pos.getZ()));
	}

	public IBlockState onBlockPlaced(World world, BlockPos pos,
			Direction facing, Vec3 hit, int meta, EntityLivingBase placer,
			ItemStack stack)
	{
		return getStateFromMeta(meta);
	}

	@Override
	public final IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer)
	{
		return onBlockPlaced(worldIn, pos, Direction.toDirection(facing),
				new Vec3(hitX - pos.getX(), hitY - pos.getY(),
						hitZ - pos.getZ()),
				meta, placer, placer.getEquipmentInSlot(0));
	}

	public final IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer, ItemStack stack)
	{
		return onBlockPlaced(worldIn, pos,
				Direction.toDirection(facing), new Vec3(hitX - pos.getX(),
						hitY - pos.getY(), hitZ - pos.getZ()),
				meta, placer, stack);
	}

	protected void onSilkHarvest(World worldIn, EntityPlayer player,
			BlockPos pos, IBlockState state, TileEntity te)
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack itemstack = createStackedBlock(state);

		if (itemstack != null)
		{
			items.add(itemstack);
		}

		ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos,
				worldIn.getBlockState(pos), 0, 1.0f, true, player);
		for (ItemStack stack : items)
		{
			spawnAsEntity(worldIn, pos, stack);
		}
	}

	public boolean rotateBlock(World world, BlockPos pos, Direction axis)
	{
		return false;
	}

	@Override
	public final boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		return rotateBlock(world, pos, Direction.toDirection(axis));
	}

	@Override
	public BlockBase setBlockUnbreakable()
	{
		super.setBlockUnbreakable();
		return this;
	}

	@Override
	public BlockBase setHardness(float hardness)
	{
		super.setHardness(hardness);
		return this;
	}

	public BlockBase setHardnessAndResistance(float hardness, float resistance)
	{
		setHardness(hardness);
		setResistance(resistance);
		return this;
	}

	public BlockBase setLocalizedName(String localized)
	{
		FleAPI.lang.registerLocal(getUnlocalizedName(), localized);
		useFleLang = true;
		return this;
	}

	public BlockBase setLocalizedName(String locale, String localized)
	{
		FleAPI.lang.registerLocal(locale, getUnlocalizedName(), localized);
		useFleLang = true;
		return this;
	}

	@Override
	public BlockBase setResistance(float resistance)
	{
		super.setResistance(resistance);
		return this;
	}
}