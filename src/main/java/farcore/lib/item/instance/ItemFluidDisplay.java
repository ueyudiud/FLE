package farcore.lib.item.instance;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.lib.item.ItemBase;
import farcore.lib.util.UnlocalizedList;
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

public class ItemFluidDisplay extends ItemBase
implements IFluidContainerItem
{
	public ItemFluidDisplay()
	{
		super("fluid.display");
		EnumItem.display_fluid.set(this);
		hasSubtypes = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void registerIcons(farcore.lib.util.INamedIconRegister register)
	{
		FarCore.voidBlockIcon = register.registerIcon(null, "farcore:void");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		Fluid fluid = FluidRegistry.getFluid(meta);
		return fluid == null ? FluidRegistry.WATER.getStillIcon() : fluid.getStillIcon();
	}

	@Override
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
			return new FluidStack(fluid, 1).getUnlocalizedName();
		return "fluid.unknown";
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		Fluid fluid = FluidRegistry.getFluid(stack.getItemDamage());
		if(fluid != null)
			return new FluidStack(fluid, 1).getLocalizedName();
		return "Unknown";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addUnlocalInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean F3H)
	{
		Fluid fluid = FluidRegistry.getFluid(getDamage(stack));
		int amount = stack.stackTagCompound != null ? stack.stackTagCompound.getInteger("amount") : 0;
		if(fluid != null)
			list.add(EnumChatFormatting.WHITE + "Name : " + fluid.getLocalizedName(new FluidStack(fluid, 1)));
		if(amount > 0)
			list.add(EnumChatFormatting.BLUE + "Amount : " + amount + "L");
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
}