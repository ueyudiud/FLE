package farcore.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumItem;
import farcore.enums.EnumItem.IInfomationable;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemFluidDisplay extends ItemBase implements IFluidContainerItem, IInfomationable
{
	public ItemFluidDisplay()
	{
		super("fluid.display");
		EnumItem.display_fluid.set(new ItemStack(this));
		hasSubtypes = true;
	}

	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		V.voidBlockIcon = register.registerIcon("fle:void");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		Fluid fluid = FluidRegistry.getFluid(meta);
		return fluid == null ? FluidRegistry.WATER.getStillIcon() : fluid.getStillIcon();
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		Fluid fluid = FluidRegistry.getFluid(stack.getItemDamage());
		return fluid == null ? 0xFFFFFF : fluid.getColor();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		Fluid fluid = FluidRegistry.getFluid(stack.getItemDamage());
		if(fluid != null)
		{
			return new FluidStack(fluid, 1).getUnlocalizedName();
		}
		return "fluid.unknown";
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		Fluid fluid = FluidRegistry.getFluid(stack.getItemDamage());
		if(fluid != null)
		{
			return new FluidStack(fluid, 1).getLocalizedName();
		}
		return "Unknown";
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		Fluid fluid = FluidRegistry.getFluid(stack.getItemDamage());
		int amount = stack.stackTagCompound != null ? 
				stack.stackTagCompound.getInteger("amount") : 0;
		if(fluid != null)
		{
			list.add(EnumChatFormatting.WHITE + "Name : " + fluid.getLocalizedName(new FluidStack(fluid, 1)));
		}
		if(amount > 0)
		{
			list.add(EnumChatFormatting.BLUE + "Amount : " + amount + "L");
		}
		list.add(EnumChatFormatting.RED + "Temperature : " + fluid.getTemperature() + "K");
		list.add(EnumChatFormatting.GREEN + "Viscosity : " + (int) (1E6 / fluid.getViscosity()) / 1000F + "mm/s^2");
		list.add(EnumChatFormatting.YELLOW + "State : " + (fluid.isGaseous() ? "Gas" : "Liquid"));
	}

	@Override
	public FluidStack getFluid(ItemStack container)
	{
		Fluid fluid = FluidRegistry.getFluid(container.getItemDamage());
		return fluid == null ? null : new FluidStack(fluid, Integer.MAX_VALUE);
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof Fluid)
			{
				return new ItemStack(this, size, ((Fluid) objects[0]).getID());
			}
			else if(objects[0] instanceof FluidStack)
			{
				ItemStack stack = new ItemStack(this, size, ((FluidStack) objects[0]).getFluidID());
				U.Inventorys.setupNBT(stack, true).setInteger("amount", ((FluidStack) objects[0]).amount);
				return stack;
			}
			else if(objects[0] instanceof String)
			{
				if(!FluidRegistry.isFluidRegistered((String) objects[0])) return new ItemStack(this);
				return new ItemStack(this, size, FluidRegistry.getFluidID((String) objects[0]));
			}
		}
		else if(objects.length == 2)
		{
			if(objects[0] instanceof Fluid && objects[1] instanceof Number)
			{
				return provide(size, new FluidStack((Fluid) objects[0], ((Number) objects[1]).intValue()));
			}
		}
		return null;
	}
}