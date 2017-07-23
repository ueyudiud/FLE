/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.fluid;

import java.io.IOException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.IFluidHandlerIO;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import nebula.common.util.NBTs;
import nebula.common.world.ICoord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

/**
 * Nebula fluid tank, include temperature option.
 * @author ueyudiud
 */
public class FluidTankN implements IFluidTank, IFluidHandlerIO, INBTCompoundReaderAndWritter<FluidTankN>
{
	protected boolean floatCapacity = false;
	protected boolean enableTemperature = false;
	protected Predicate<Direction> input;
	protected BiPredicate<Direction, FluidStack> output;
	
	protected int capacity;
	protected FluidStack stack;
	protected float temperature = -1.0F;
	
	public FluidTankN(int capacity)
	{
		this.capacity = capacity;
	}
	
	public FluidTankN enableTemperature()
	{
		this.enableTemperature = true;
		return this;
	}
	
	public FluidTankN setIO(Predicate<Direction> input, BiPredicate<Direction, FluidStack> output)
	{
		this.input = input;
		this.output = output;
		return this;
	}
	
	@Override
	public void readFromNBT1(NBTTagCompound nbt)
	{
		if (this.floatCapacity)
		{
			this.capacity = NBTs.getIntOrDefault(nbt, "capacity", 0);
		}
		if (this.capacity > 0)
		{
			this.stack = NBTs.getFluidStackOrDefault(nbt, "stack", null);
		}
		if (this.enableTemperature)
		{
			this.temperature = NBTs.getFloatOrDefault(nbt, "temperature", -1.0F);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (this.floatCapacity)
		{
			NBTs.setNumber(nbt, "capacity", this.capacity);
		}
		NBTs.setFluidStack(nbt, "stack", this.stack, false);
		if (this.enableTemperature)
		{
			NBTs.setNumber(nbt, "temperature", this.temperature);
		}
		return nbt;
	}
	
	@Override
	public final FluidTankN readFromNBT(NBTTagCompound nbt)
	{
		readFromNBT1(nbt);
		return this;
	}
	
	@Override
	public final void writeToNBT(FluidTankN target, NBTTagCompound nbt)
	{
		target.writeToNBT(nbt);
	}
	
	public boolean isFull()
	{
		return (this.stack != null && this.stack.amount >= this.capacity);
	}
	
	@Override
	public boolean canExtractFluid(Direction to)
	{
		return (this.stack != null) && (this.input == null || this.input.test(to));
	}
	
	public boolean canExtractFluidWithType(Direction to, FluidStack stack)
	{
		return (this.stack == null || this.stack.isFluidEqual(stack)) && canExtractFluid(to);
	}
	
	@Override
	public boolean canInsertFluid(Direction from, FluidStack stack)
	{
		return (stack == null || stack.amount > 0) &&
				!isFull() &&
				(this.output == null || stack == null || this.output.test(from, stack));
	}
	
	@Override
	public @Nullable FluidStackExt extractFluid(int amount, Direction to, boolean simulate)
	{
		if (!canExtractFluid(to)) return null;
		return drain(amount, !simulate);
	}
	
	@Override
	public @Nullable FluidStackExt extractFluid(@Nullable FluidStack suggested, Direction to, boolean simulate)
	{
		if (!canExtractFluid(to) || suggested == null || !this.stack.isFluidEqual(suggested)) return null;
		return drain(suggested.amount, !simulate);
	}
	
	/**
	 * Take <tt>insert</tt> action, means stack will be filled only source stack can fully
	 * insert into tank.
	 * @param stack the source stack.
	 * @param simulate do tank stack not changed in <tt>insert</tt> action, if do this,
	 * the tank will only give similate result.
	 * @return return <tt>true</tt> if stack fully insert into tank.
	 */
	public boolean insertFluid(@Nullable FluidStack stack, boolean simulate)
	{
		if (stack == null) return true;
		if (fill(stack, false) == stack.amount)
		{
			if (!simulate)
				fill(stack, true);
			return true;
		}
		return false;
	}
	
	@Override
	public int insertFluid(FluidStack stack, Direction from, boolean simulate)
	{
		if (!canInsertFluid(from, stack)) return 0;
		return fill(stack, !simulate);
	}
	
	public boolean hasFluid()
	{
		return this.stack != null;
	}
	
	@Override
	public FluidStack getFluid()
	{
		return this.stack;
	}
	
	@Override
	public int getFluidAmount()
	{
		return hasFluid() ? this.stack.amount : 0;
	}
	
	@Override
	public int getCapacity()
	{
		return this.capacity;
	}
	
	public float getTemperature()
	{
		return this.temperature;
	}
	
	@Override
	public FluidTankInfo getInfo()
	{
		return new FluidTankInfo(this);
	}
	
	public void update(ICoord coord)
	{
		//		if (this.stack != null)
		//		{
		//			;
		//		}
	}
	
	@Override
	public int fill(@Nullable FluidStack resource, boolean doFill)
	{
		if (resource == null) return 0;
		if (this.stack == null)
		{
			int amount = Math.min(resource.amount, this.capacity);
			if (doFill && amount > 0)
			{
				this.stack = FluidStacks.sizeOf(resource, amount);
				if (this.stack instanceof FluidStackExt)
				{
					if (this.enableTemperature)
					{
						this.temperature = ((FluidStackExt) this.stack).temperature;
					}
					this.stack = ((FluidStackExt) this.stack).toSimple();
				}
			}
			return amount;
		}
		else
		{
			if (!this.stack.isFluidEqual(resource)) return 0;
			int amount = Math.min(resource.amount, this.capacity - this.stack.amount);
			if (doFill && amount > 0)
			{
				int a = this.stack.amount;
				this.stack.amount += amount;
				if (this.enableTemperature)
				{
					int temp = FluidStacks.getTemperature(resource, -1);
					this.temperature = (a * this.temperature + amount * temp) / this.stack.amount;
				}
			}
			return amount;
		}
	}
	
	public @Nullable FluidStackExt drain(FluidStack target, boolean doDrain)
	{
		if (target instanceof FluidStackExt)
			target = ((FluidStackExt) target).toSimple();
		return hasFluid() && target.isFluidEqual(this.stack) ? drain(target.amount, doDrain) : null;
	}
	
	@Override
	public @Nullable FluidStackExt drain(int maxDrain, boolean doDrain)
	{
		if (this.stack == null) return null;
		int amt = Math.min(this.stack.amount, maxDrain);
		FluidStackExt result;
		if (this.enableTemperature)
		{
			if (this.temperature < 0.0F)
			{
				this.temperature = FluidStacks.getTemperature(this.stack, -1);
			}
			result = new FluidStackExt(this.stack, amt);
		}
		else
		{
			result = new FluidStackExt(this.stack, amt);
		}
		if (doDrain && amt > 0)
		{
			if (this.stack.amount > amt)
			{
				this.stack.amount -= amt;
			}
			else
			{
				this.stack = null;
				this.temperature = -1.0F;
			}
		}
		return result;
	}
	
	public void setFluid(FluidStack stack)
	{
		if (this.enableTemperature)
		{
			if (stack instanceof FluidStackExt)
			{
				this.temperature = ((FluidStackExt) stack).temperature;
				this.stack = ((FluidStackExt) stack).toSimple();
			}
			else
			{
				this.stack = FluidStacks.copy(stack);
			}
		}
		else
		{
			this.stack = FluidStacks.copy(stack);
		}
	}
	
	public String getInformation()
	{
		return "Tank: " + (this.stack == null ? "Empty" : this.stack.getUnlocalizedName() + " " + this.stack.amount + "/" + this.capacity + " L");
	}
	
	public void writeToPacket(PacketBufferExt buf) throws IOException
	{
		buf.writeFluidStack(this.stack);
		buf.writeFloat(this.temperature);
	}
	
	public void readFromPacket(PacketBufferExt buf) throws IOException
	{
		this.stack = buf.readFluidStack();
		this.temperature = buf.readFloat();
	}
}