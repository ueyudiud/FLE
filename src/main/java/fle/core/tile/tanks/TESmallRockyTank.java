/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.tanks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import farcore.data.Capabilities;
import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.MP;
import farcore.lib.capability.IFluidHandlerHelper;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.fluid.FluidTankN;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Light;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_SideSolid;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import nebula.common.util.L;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

/**
 * @author ueyudiud
 */
public class TESmallRockyTank extends TESynchronization implements IFluidHandlerHelper, ITP_BlockHardness, ITP_ExplosionResistance, ITB_BlockPlacedBy, ITP_Drops, ITP_SideSolid, ITP_Light, IDebugableTile, INetworkedSyncTile
{
	private Mat			material	= M.stone;
	private FluidTankN	tank		= new FluidTankN(4000).enableTemperature();
	
	private byte light;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		this.tank.writeToNBT(nbt, "tank");
		nbt.setString("material", this.material.name);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt, "tank");
		this.material = Mat.getMaterialByNameOrDefault(nbt, "material", M.stone);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setShort("m", this.material.id);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("m")) this.material = Mat.getMaterialByIDOrDefault(nbt, "m", M.stone);
	}
	
	@Override
	public void writeToClientInitalization(NBTTagCompound nbt)
	{
		super.writeToClientInitalization(nbt);
		this.tank.writeToNBT(nbt, "t");
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 1:
			this.tank.writeToPacket(buf);
			break;
		}
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 1:
			this.tank.readFromPacket(buf);
			break;
		}
	}
	
	@Override
	protected void initClient(NBTTagCompound nbt)
	{
		super.initClient(nbt);
		this.tank.readFromNBT(nbt, "t");
		markBlockRenderUpdate();
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return this.material.getProperty(MP.property_rock).hardness * 0.75F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return this.material.getProperty(MP.property_rock).explosionResistance * 0.75F;
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.material = Mat.getMaterialFromStack(stack, "material", this.material);
		syncToNearby();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		List<ItemStack> list = new ArrayList<>();
		
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
		Mat.setMaterialToStack(stack, "material", this.material);
		list.add(stack);
		
		return list;
	}
	
	@Override
	public boolean isSideSolid(Direction side)
	{
		return side != Direction.U;
	}
	
	@Override
	public int getLightValue(IBlockState state)
	{
		int light = FluidStacks.getLuminosity(this.tank.getFluid(), 0);
		float p = (float) this.tank.getFluidAmount() / (float) this.tank.getCapacity();
		return (int) (0.5F * p * (2.0F - p) * light);
	}
	
	@Override
	public int getLightOpacity(IBlockState state)
	{
		return 4;
	}
	
	@Override
	protected void updateServer()
	{
		if (this.tank.getTemperature() >= this.material.getProperty(MP.property_basic).meltingPoint)
		{
			setBlockState(EnumBlock.fire.block.getDefaultState(), 0x3);
			return;
		}
		super.updateServer();
		this.tank.update(this);
	}
	
	@Override
	protected void updateClient()
	{
		byte l1 = (byte) getLightValue(null);
		if (this.light != l1)
		{
			this.light = l1;
			markLightForUpdate(EnumSkyBlock.BLOCK);
		}
	}
	
	public IFluidTank getFluidTank()
	{
		return this.tank;
	}
	
	public Mat getMaterial()
	{
		return this.material;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.CAPABILITY_FLUID;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == Capabilities.CAPABILITY_FLUID ? L.castAny(new IFluidHandlerHelper.FluidHandlerWrapper(this, Direction.of(facing))) : null;
	}
	
	@Override
	public boolean canFill(Direction direction, FluidStack stack)
	{
		return !FluidStacks.isGaseous(stack) && (direction.horizontal || direction == Direction.U);
	}
	
	@Override
	public boolean canDrain(Direction direction, FluidStack stack)
	{
		return direction.horizontal;
	}
	
	@Override
	public int fill(Direction direction, FluidStack resource, boolean process)
	{
		if (canFill(direction, resource))
		{
			int amt = this.tank.fill(resource, process);
			if (amt > 0 && process) NebulaSynchronizationHandler.markTileEntityForUpdate(this, 1);
			return amt;
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(Direction direction, int maxAmount, boolean process)
	{
		if (canDrain(direction, null))
		{
			FluidStack stack = this.tank.drain(maxAmount, process);
			if (stack != null && process) NebulaSynchronizationHandler.markTileEntityForUpdate(this, 1);
			return stack;
		}
		return null;
	}
	
	@Override
	public SidedFluidIOProperty getProperty(Direction direction)
	{
		return new SidedFluidIOTankNPropertyWrapper(this.tank);
	}
	
	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add("Material: " + this.material.name);
		list.add(this.tank.getInformation());
	}
}
