package farcore.lib.tile.instance.circuit;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import farcore.data.EnumToolType;
import farcore.data.IC;
import farcore.data.M;
import farcore.lib.block.instance.BlockRedstoneCircuit;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockDestroyedByPlayer;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_CollisionBoundingBox;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_ComparatorInputOverride;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_ConnectRedstone;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Drops;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_RedstonePower;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_SelectedBoundingBox;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.abstracts.TESynchronization;
import farcore.lib.util.Direction;
import farcore.lib.util.Facing;
import farcore.util.U;
import farcore.util.U.NBTs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TECircuitBase extends TESynchronization
implements ITP_RedstonePower, ITP_ConnectRedstone, ITP_ComparatorInputOverride,
ITB_BlockPlacedBy, IToolableTile, ITB_BlockDestroyedByPlayer, IUpdatableTile,
ITP_Drops, ITP_CollisionBoundingBox, ITP_SelectedBoundingBox, ITP_BlockHardness,
ITP_ExplosionResistance, ITB_AddDestroyEffects, ITB_AddHitEffects
{
	protected static final ActionResult<Float> SCREW_DRIVER_DAMAGE = new ActionResult<Float>(EnumActionResult.SUCCESS, 0.2F);
	protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
	
	public Mat material = M.stone;
	public Direction facing = Direction.N;

	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 0.5F;
	}

	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 0.2F;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		material = NBTs.getMaterialByNameOrDefault(nbt, "material", M.stone);
		facing = Direction.readFromNBT(nbt, "facing", Direction.T_2D_NONNULL);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", material.name);
		facing.writeToNBT(nbt, "facing", Direction.T_2D_NONNULL);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		material = NBTs.getValueByIDOrDefault(nbt, "m", Mat.materials(), material);
		facing = NBTs.getValueByByteOrDefault(nbt, "f", Direction.DIRECTIONS_2D, facing);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("m", material.name);
		facing.writeToNBT(nbt, "f", Direction.T_2D_NONNULL);
	}
	
	@Override
	protected void initServer()
	{
		super.initServer();
	}
	
	protected void markNeighbourNotify()
	{

	}
	
	@Override
	public abstract int getStrongPower(IBlockState state, Direction side);
	
	@Override
	public abstract int getWeakPower(IBlockState state, Direction side);

	@Override
	public boolean listenWeakChanges()
	{
		return false;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return false;
	}

	public static Mat getRockType(ItemStack stack)
	{
		return Mat.material(U.ItemStacks.getOrSetupNBT(stack, false).getString("material"), M.stone);
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		material = getRockType(stack);
		facing = Direction.heading(placer);
	}

	protected void notifyNeighbors()
	{
		IBlockState state = getBlockState();
		Facing[] facings1 = getOutputFacings();
		EnumFacing[] facings2 = new EnumFacing[facings1.length];
		for(int i = 0; i < facings1.length; ++i)
		{
			facings2[i] = facings1[i].toDirection(facing).of();
		}
		if(ForgeEventFactory.onNeighborNotify(worldObj, pos, state, EnumSet.copyOf(Arrays.asList(facings2))).isCanceled())
			return;
		Block block = state.getBlock();
		for(EnumFacing facing : facings2)
		{
			BlockPos pos1 = pos.offset(facing);
			worldObj.notifyBlockOfStateChange(pos1, block);
			worldObj.notifyNeighborsOfStateExcept(pos1, block, facing.getOpposite());
		}
	}

	@Override
	public Direction getRotation()
	{
		return facing;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state)
	{
		return REDSTONE_DIODE_AABB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
			Entity entity)
	{
		collidingBoxes.add(getCollisionBoundingBox(state));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state)
	{
		return REDSTONE_DIODE_AABB;
	}
	
	protected abstract Facing[] getOutputFacings();

	protected int getWeakPower(Facing offset)
	{
		Direction direction = offset.toDirection(facing);
		return getWeakPower(direction.of(), direction);
	}

	protected int getStrongPower(Facing offset)
	{
		Direction direction = offset.toDirection(facing);
		return getStrongPower(direction.of(), direction);
	}

	@Override
	public void onBlockDestroyedByPlayer(IBlockState state)
	{
		Block block = state.getBlock();
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			worldObj.notifyNeighborsOfStateChange(pos.offset(facing), block);
		}
	}

	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		if(tool == EnumToolType.screw_driver)
		{
			onScrewDriverUsed(player, side, hitX, hitY, hitZ);
			return SCREW_DRIVER_DAMAGE;
		}
		return DEFAULT_RESULT;
	}
	
	protected void onScrewDriverUsed(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{

	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, ((BlockRedstoneCircuit) state.getBlock()).property_TE.getTileMeta(this));
		NBTTagCompound nbt = new NBTTagCompound();
		stack.setTagCompound(nbt);
		setDropNBT(nbt);
		return Arrays.asList(stack);
	}
	
	protected void setDropNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", material.name);
	}

	@Override
	protected int getRenderUpdateRange()
	{
		return 0;
	}

	/**
	 * Used for render type.
	 * @return
	 */
	public String getState()
	{
		return "_";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		Map<RockType, TextureAtlasSprite> icons = IC.ROCK_ICONS.get(material);
		if(icons != null)
		{
			U.Client.addBlockDestroyEffects(worldObj, pos, getBlockState(), manager, icons.get(RockType.resource));
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		Map<RockType, TextureAtlasSprite> icons = IC.ROCK_ICONS.get(material);
		if(icons != null)
		{
			U.Client.addBlockHitEffect(worldObj, random, getBlockState(), target.sideHit, pos, manager, icons.get(RockType.resource));
		}
		return false;
	}
}