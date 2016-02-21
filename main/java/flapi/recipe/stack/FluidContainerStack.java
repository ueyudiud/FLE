package flapi.recipe.stack;

import java.util.ArrayList;
import java.util.List;

import farcore.collection.abs.AStack;
import farcore.util.U;
import farcore.util.U.I;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class FluidContainerStack extends TemplateStack
{
	boolean isDrain;
	FluidStack fluid;

	public FluidContainerStack()
	{
		isDrain = false;
	}
	public FluidContainerStack(Fluid fluid)
	{
		this(fluid, 1);
	}
	public FluidContainerStack(Fluid fluid, int amount)
	{
		this(new FluidStack(fluid, amount));
	}
	public FluidContainerStack(FluidStack stack)
	{
		this.fluid = stack;
		this.isDrain = true;
	}

	@Override
	public boolean contain(ItemStack arg)
	{
		if(arg == null) return false;
		if(!isDrain)
		{
			return arg.getItem() instanceof IFluidContainerItem ||
					FluidContainerRegistry.fillFluidContainer(fluid, arg) != null;
		}
		FluidStack contain = U.F.getContain(arg);
		return U.F.contain(U.F.multiply(contain, arg.stackSize), fluid);
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		if(stack == null) return false;
		if(!isDrain)
		{
			return stack.getItem() instanceof IFluidContainerItem ||
					FluidContainerRegistry.fillFluidContainer(fluid, stack) != null;
		}
		FluidStack contain = U.F.getContain(stack);
		return U.F.contain(contain, fluid.getFluid());
	}
	
	@Override
	public List<ItemStack> create()
	{
		List<ItemStack> list = new ArrayList();
		for(FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData())
		{
			if(!isDrain && data.emptyContainer != null && U.F.contain(data.fluid, fluid.getFluid()))
			{
				list.add(data.emptyContainer);
			}
			else if(isDrain && U.F.contain(data.fluid, fluid))
			{
				list.add(data.filledContainer);
			}
		}
		return list;
	}
	
	public ItemStack fillOrDrainFluid(ItemStack stack)
	{
		if(isDrain)
		{
			return FluidContainerRegistry.drainFluidContainer(stack);
		}
		else
		{
			return FluidContainerRegistry.fillFluidContainer(this.fluid, stack);
		}
	}
	
	@Override
	public int size(ItemStack stack)
	{
		if(isDrain)
		{
			FluidStack stack2 = U.F.getContain(stack);
			int amount = stack2.amount >= fluid.amount ? 1 :
					fluid.amount % stack2.amount == 0 ? fluid.amount / stack2.amount :
						(int) Math.ceil((double) fluid.amount / (double) stack2.amount);
			return amount;
		}
		else
		{
			int capacity = U.F.getRemainderCapacity(stack);
			int amount = Math.min(fluid.amount / capacity, stack.stackSize);
			return amount;
		}
	}
	
	private ItemStack instance;
	
	@Override
	public ItemStack instance()
	{
		if(instance == null)
		{
			for(FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData())
			{
				if(!isDrain && data.emptyContainer != null && U.F.contain(data.fluid, fluid.getFluid()))
				{
					return (instance = data.emptyContainer).copy();
				}
				else if(isDrain && U.F.contain(data.fluid, fluid))
				{
					return (instance = data.filledContainer).copy();
				}
			}
		}
		return instance.copy();
	}
}