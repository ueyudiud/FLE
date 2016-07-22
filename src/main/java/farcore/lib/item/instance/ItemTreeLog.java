package farcore.lib.item.instance;

import farcore.util.U;
import net.minecraft.item.ItemStack;

public class ItemTreeLog
{
	public static void setLogSize(ItemStack stack, int size)
	{
		U.ItemStacks.setupNBT(stack, true).setShort("length", (short) size);
	}
}