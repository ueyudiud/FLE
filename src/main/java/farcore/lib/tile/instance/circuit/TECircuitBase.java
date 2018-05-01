/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import farcore.blocks.BlockRedstoneCircuit;
import farcore.data.EnumToolTypes;
import farcore.data.M;
import farcore.data.MC;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.material.Mat;
import nebula.client.util.Client;
import nebula.common.data.Misc;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddDestroyEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_AddHitEffects;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockDestroyedByPlayer;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BoundingBox;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ComparatorInputOverride;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ConnectRedstone;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_RedstonePower;
import nebula.common.tile.IToolableTile;
import nebula.common.tile.IUpdatableTile;
import nebula.common.tile.TE04Synchronization;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.Facing;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
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

public abstract class TECircuitBase extends TE04Synchronization
implements ITP_RedstonePower, ITP_ConnectRedstone, ITP_ComparatorInputOverride, ITB_BlockPlacedBy, IToolableTile, ITB_BlockDestroyedByPlayer, IUpdatableTile, ITP_Drops, ITP_BoundingBox, ITP_BlockHardness, ITP_ExplosionResistance, ITB_AddDestroyEffects, ITB_AddHitEffects
{
	protected volatile boolean circuitRedsignalMarker = false;
	
	protected static final ActionResult<Float>	SCREW_DRIVER_DAMAGE	= new ActionResult<>(EnumActionResult.SUCCESS, 0.2F);
	protected static final AxisAlignedBB		REDSTONE_DIODE_AABB	= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
	
	public Mat			material	= M.stone;
	public Direction	facing		= Direction.N;
	
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
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player)
	{
		return player.getDigSpeed(state, this.pos) / getBlockHardness(state) / 30F;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.material = Mat.getMaterialByNameOrDefault(nbt, "material", M.stone);
		this.facing = Direction.readFromNBT(nbt, "facing", Direction.T_2D_NONNULL);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", this.material.name);
		this.facing.writeToNBT(nbt, "facing", Direction.T_2D_NONNULL);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.material = NBTs.getValueByIDOrDefault(nbt, "m", Mat.materials(), this.material);
		this.facing = NBTs.getEnumOrDefault(nbt, "f", this.facing);
		markBlockRenderUpdate();
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("m", this.material.name);
		NBTs.setEnum(nbt, "f", this.facing);
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
	public int getRedstonePower(EnumFacing side, Direction offset)
	{
		int power = super.getRedstonePower(side, offset);
		if (power >= 15)
		{
			return power;
		}
		IBlockState state = getBlockState(offset);
		if (state.getBlock() instanceof BlockRedstoneWire)
		{
			return Math.max(power, state.getValue(BlockRedstoneWire.POWER));
		}
		else
			return power;
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
		return Mat.material(ItemStacks.getOrSetupNBT(stack, false).getString("material"), M.stone);
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.material = getRockType(stack);
		this.facing = Direction.heading(placer);
	}
	
	protected void notifyNeighbors()
	{
		IBlockState state = getBlockState();
		Facing[] facings1 = getOutputFacings();
		EnumFacing[] facings2 = new EnumFacing[facings1.length];
		for (int i = 0; i < facings1.length; ++i)
		{
			facings2[i] = facings1[i].toDirection(this.facing).of();
		}
		if (ForgeEventFactory.onNeighborNotify(this.world, this.pos, state, EnumSet.copyOf(Arrays.asList(facings2))).isCanceled()) return;
		Block block = state.getBlock();
		for (EnumFacing facing : facings2)
		{
			BlockPos pos1 = this.pos.offset(facing);
			this.world.notifyBlockOfStateChange(pos1, block);
			this.world.notifyNeighborsOfStateExcept(pos1, block, facing.getOpposite());
		}
	}
	
	@Override
	public Direction getRotation()
	{
		return this.facing;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state)
	{
		return REDSTONE_DIODE_AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return REDSTONE_DIODE_AABB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity)
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
	
	protected int getRedstonePower(Facing offset)
	{
		Direction direction = offset.toDirection(this.facing);
		return getRedstonePower(direction.opposite().of(), direction);
	}
	
	@Override
	public boolean onBlockDestroyedByPlayer(IBlockState state)
	{
		Block block = state.getBlock();
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			this.world.notifyNeighborsOfStateChange(this.pos.offset(facing), block);
		}
		return this.world.setBlockState(this.pos, Misc.AIR, isClient() ? 11 : 3);
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tool == EnumToolTypes.SCREW_DRIVER)
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
		nbt.setString("material", this.material.name);
	}
	
	@Override
	protected int getRenderUpdateRange()
	{
		return 0;
	}
	
	/**
	 * Used for render type.
	 * 
	 * @return
	 */
	public String getState()
	{
		return "_";
	}
	
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(ParticleManager manager)
	{
		Client.addBlockDestroyEffects(this.world, this.pos, getBlockState(), manager, MaterialTextureLoader.getIcon(this.material, MC.stone));
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(RayTraceResult target, ParticleManager manager)
	{
		Client.addBlockHitEffect(this.world, this.random, getBlockState(), target.sideHit, this.pos, manager, MaterialTextureLoader.getIcon(this.material, MC.stone));
		return false;
	}
}
