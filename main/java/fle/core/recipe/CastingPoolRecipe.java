package fle.core.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleValue;
import fle.api.material.MaterialAbstract;
import fle.api.material.MatterDictionary;
import fle.api.material.MatterDictionary.IFreezingRecipe;
import fle.api.recipe.AbstractRecipe;
import fle.api.util.SubTag;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.inventory.InventoryCastingPool;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemToolHead;

public class CastingPoolRecipe implements IFreezingRecipe
{
	private static Object[][] array0 = {{IB.copper, Materials.Copper}, {IB.cu_as_0, Materials.CuAs}, {IB.cu_as_1, Materials.CuAs2}, {IB.cu_pb_0, Materials.CuPb}, {IB.cu_pb_1, Materials.CuPb2}, {IB.cu_sn_0, Materials.CuSn}, {IB.cu_sn_1, Materials.CuSn2}};
	
	public static void init()
	{
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.copper, FleValue.ingot_mol * 3 * 1), "xxxx xxxx", ItemFleSub.a("ingot_cu")));
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.cu_as_0, FleValue.ingot_mol * 3 * 1), "xxxx xxxx", ItemFleSub.a("ingot_cu_as_0")));
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.cu_as_1, FleValue.ingot_mol * 3 * 1), "xxxx xxxx", ItemFleSub.a("ingot_cu_as_1")));
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.cu_pb_0, FleValue.ingot_mol * 3 * 1), "xxxx xxxx", ItemFleSub.a("ingot_cu_pb_0")));
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.cu_pb_1, FleValue.ingot_mol * 3 * 1), "xxxx xxxx", ItemFleSub.a("ingot_cu_pb_1")));
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.cu_sn_0, FleValue.ingot_mol * 3 * 1), "xxxx xxxx", ItemFleSub.a("ingot_cu_sn_0")));
		MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack(IB.cu_sn_1, FleValue.ingot_mol * 3 * 1), "xxxx xxxx", ItemFleSub.a("ingot_cu_sn_1")));
		for(Object[] a : array0)
		{
			if(SubTag.type_tool_metal_tier0.isTrue(((MaterialAbstract) a[1])))
			{
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 3), "x  xx xxx", ItemToolHead.a("metal_axe", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 3), "   xxxxxx", ItemToolHead.a("metal_pickaxe", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 1), "x xxxxxxx", ItemToolHead.a("metal_shovel", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 5), "     xxxx", ItemToolHead.a("metal_hammer", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 5), "   x  xxx", ItemToolHead.a("metal_hammer", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 2), " xxx xxxx", ItemToolHead.a("metal_chisel", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 2), "xx x xxxx", ItemToolHead.a("metal_chisel", (MaterialAbstract) a[1], 12)));
			}
			if(SubTag.type_tool_metal_tier1.isTrue(((MaterialAbstract) a[1])))
			{
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 2), " xxx xxx ", ItemToolHead.a("metal_bowsaw", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 2), "xx x x xx", ItemToolHead.a("metal_bowsaw", (MaterialAbstract) a[1], 12)));
				MatterDictionary.registerMatter(new CastingPoolRecipe(new FluidStack((Fluid) a[0], FleValue.ingot_mol * 3 * 2), "xxxx  xxx", ItemToolHead.a("metal_adz", (MaterialAbstract) a[1], 12)));
			}
		}
		MatterDictionary.registerMatter(new CeramicFurnaceOutletRecipe(new FluidStack(IB.copper, FleValue.ingot_mol * 3 * 1), ItemFleSub.a("ingot_cu")));
		MatterDictionary.registerMatter(new CeramicFurnaceOutletRecipe(new FluidStack(IB.cu_as_0, FleValue.ingot_mol * 3 * 1), ItemFleSub.a("ingot_cu_as_0")));
	}
	
	FluidStack input;
	int s;
	ItemStack output;
	
	public CastingPoolRecipe(FluidStack aInput, String str, ItemStack aOutput)
	{
		try
		{
			input = aInput.copy();
			if(str.length() != 9)
			{
				throw new RuntimeException();
			}
			s = 0;
			for(int i = 0; i < str.length(); ++i)
			{
				s += str.charAt(i) != ' ' ? 1 << i : 0;
			}
			output = aOutput.copy();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			String c = "";
			c += aInput.getUnlocalizedName() + ", ";
			c += str + ", ";
			c += aOutput.getUnlocalizedName();
			throw new RuntimeException(c);
		}
	}

	@Override
	public boolean match(FluidStack aStack, IInventory inv)
	{
		if(aStack == null || !(inv instanceof InventoryCastingPool)) return false;
		if((!input.isFluidEqual(aStack)) || input.amount > aStack.amount) return false;
		for(int i = 0; i < 9; ++i)
		{
			boolean flag = (s & (1 << i)) != 0;
			if((inv.getStackInSlot(i) != null) != flag) return false;
		}
		return true;
	}

	@Override
	public int getMatterRequire(FluidStack aStack, IInventory inv)
	{
		return input.amount;
	}

	@Override
	public ItemStack getOutput(FluidStack aStack, IInventory inv)
	{
		return output.copy();
	}	
}