package farcore.alpha.block.tree;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.block.ItemBlockBase;
import farcore.lib.substance.SubstanceWood;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemSapling extends ItemBlockBase
{
	public ItemSapling(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return block.getIcon(0, meta);
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