package farcore.lib.item.instance;

import farcore.FarCore;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.util.LanguageManager;
import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTreeLog extends ItemMulti
{
	public ItemTreeLog(MatCondition mc)
	{
		super(FarCore.ID, MC.log);
		enableChemicalFormula = false;
	}

	@Override
	public void postInitalizedItems()
	{
		for(Mat material : Mat.filt(condition))
		{
			ItemStack templete = new ItemStack(this, 1, material.id);
			LanguageManager.registerLocal(getTranslateName(templete), condition.getLocal(material));
			condition.registerOre(material, templete);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
	}

	public static void setLogSize(ItemStack stack, int size)
	{
		U.ItemStacks.getOrSetupNBT(stack, true).setShort("length", (short) size);
	}

	public static int getLogSize(ItemStack stack)
	{
		return U.ItemStacks.getOrSetupNBT(stack, false).getShort("length");
	}
}