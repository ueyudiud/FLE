package farcore.block.plant.tree;

import farcore.block.ItemBlockBase;
import farcore.lib.substance.SubstanceWood;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemSapling extends ItemBlockBase
{
	public ItemSapling(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	public static SubstanceWood getWood(ItemStack stack)
	{
		return SubstanceWood.getSubstance(stack.getItemDamage());
	}
	
	public static ItemStack setWood(ItemStack stack, SubstanceWood wood)
	{
		stack.setItemDamage(wood.getID());
		return stack;
	}
}