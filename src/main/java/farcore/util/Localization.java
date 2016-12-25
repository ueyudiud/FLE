/*
 * copyright© 2016 ueyudiud
 */

package farcore.util;

import java.text.DecimalFormat;

import farcore.lib.item.behavior.IFoodStat;
import farcore.lib.item.behavior.IToolStat;
import farcore.lib.material.Mat;
import farcore.lib.util.SubTag;
import farcore.lib.util.UnlocalizedList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class Localization
{
	private static final DecimalFormat FORMAT_1 = new DecimalFormat("#0.0");
	
	@SideOnly(Side.CLIENT)
	public static void addFoodStatInformation(IFoodStat stat, ItemStack stack, UnlocalizedList unlocalizedList)
	{
		if(stat != null && stat != IFoodStat.NO_EATABLE)
		{
			if(unlocalizedList.isSneakDown())
			{
				unlocalizedList.add("info.food.label");
				try
				{
					unlocalizedList.add("info.food.display", FORMAT_1.format(stat.getFoodAmount(stack)), FORMAT_1.format(stat.getSaturation(stack)), FORMAT_1.format(stat.getDrinkAmount(stack)));
				}
				catch(Exception exception)
				{
					;
				}
			}
			else
			{
				unlocalizedList.addShiftClickInfo();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void addDamageInformation(long now, long max, UnlocalizedList unlocalizedList)
	{
		unlocalizedList.add("info.tool.damage", now, max);
	}
	
	@SideOnly(Side.CLIENT)
	public static void addToolMaterialInformation(Mat material, IToolStat stat, UnlocalizedList unlocalizedList)
	{
		if(!material.contain(SubTag.TOOL)) return;
		unlocalizedList.add("info.tool.head.name", material.getLocalName());
		unlocalizedList.add("info.tool.harvest.level", material.toolHarvestLevel);
		unlocalizedList.add("info.tool.hardness", FORMAT_1.format(material.toolHardness));
		unlocalizedList.addToolTip("info.material.custom." + material.name);
	}
	
	@SideOnly(Side.CLIENT)
	public static void addFluidInformation(FluidStack stack, UnlocalizedList unlocalizedList)
	{
		unlocalizedList.add("info.fluidcontainer.contain", stack == null ? "Empty" : (stack.getLocalizedName() + "x" + stack.amount + "L"));
	}
}