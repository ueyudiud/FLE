/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.util;

import java.text.DecimalFormat;

import javax.annotation.Nullable;

import farcore.data.SubTags;
import farcore.lib.item.IToolStat;
import farcore.lib.material.Mat;
import nebula.client.util.UnlocalizedList;
import nebula.common.item.IFoodStat;
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
	public static void addToolMaterialInformation(Mat material, @Nullable IToolStat stat, UnlocalizedList unlocalizedList)
	{
		if(!material.contain(SubTags.TOOL)) return;
		unlocalizedList.add("info.tool.head.name", material.getLocalName());
		unlocalizedList.add("info.tool.harvest.level", material.toolHarvestLevel);
		unlocalizedList.add("info.tool.hardness", FORMAT_1.format(material.toolHardness));
		unlocalizedList.add("info.tool.attack", FORMAT_1.format(material.toolDamageToEntity));
		unlocalizedList.addToolTip("info.material.custom." + material.name);
	}
	
	@SideOnly(Side.CLIENT)
	public static void addFluidInformation(FluidStack stack, UnlocalizedList unlocalizedList)
	{
		unlocalizedList.add("info.fluidcontainer.contain", stack == null ? "Empty" : (stack.getLocalizedName() + " " + stack.amount + "L"));
	}
}