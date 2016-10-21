package farcore.lib.tile.instance.circuit;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import farcore.data.EnumToolType;
import farcore.data.M;
import farcore.lib.block.instance.BlockRedstoneCircuit;
import farcore.lib.material.Mat;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockDestroyedByPlayer;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_ComparatorInputOverride;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_ConnectRedstone;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_Drops;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITP_RedstonePower;
import farcore.lib.tile.IToolableTile;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.TESynchronization;
import farcore.lib.util.Direction;
import farcore.lib.util.Facing;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class TECircuitBase extends TESynchronization
implements ITP_RedstonePower, ITP_ConnectRedstone, ITP_ComparatorInputOverride,
ITB_BlockPlacedBy, IToolableTile, ITB_BlockDestroyedByPlayer, IUpdatableTile,
ITP_Drops
{
	public Mat material = M.stone;
	public Direction facing = Direction.N;

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		material = Mat.material(nbt.getString("material"), M.stone);
		facing = Direction.directions[nbt.getByte("facing")];
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", material.name);
		nbt.setByte("facing", (byte) facing.ordinal());
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("m"))
		{
			material = Mat.material(nbt.getString("m"), M.stone);
		}
		if(nbt.hasKey("f"))
		{
			facing = Direction.directions[nbt.getByte("f")];
		}
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setString("m", material.name);
		nbt.setByte("f", (byte) facing.ordinal());
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
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		material = Mat.material(U.ItemStacks.setupNBT(stack, false).getString("material"), M.stone);
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
	
	protected abstract Facing[] getOutputFacings();

	protected int getWeakPower(Facing offset)
	{
		Direction direction = offset.toDirection(facing);
		return getWeakPower(direction.getOpposite().of(), direction);
	}

	protected int getStrongPower(Facing offset)
	{
		Direction direction = offset.toDirection(facing);
		return getStrongPower(direction.getOpposite().of(), direction);
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
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, ((BlockRedstoneCircuit) state.getBlock()).getMetaFromState(state));
		NBTTagCompound nbt = new NBTTagCompound();
		stack.setTagCompound(nbt);
		setDropNBT(nbt);
		return Arrays.asList(stack);
	}
	
	private void setDropNBT(NBTTagCompound nbt)
	{
		nbt.setString("material", material.name);
	}
}