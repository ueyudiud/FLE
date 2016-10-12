package farcore.lib.block;

import farcore.lib.util.LanguageManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBase extends ItemBlock
{
	public BlockBase block;
	
	public ItemBlockBase(BlockBase block)
	{
		super(block);
		this.block = block;
		hasSubtypes = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return block.getUnlocalizedName() + "@" + getDamage(stack);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return LanguageManager.translateToLocal(block.getTranslateNameForItemStack(stack));
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return block.getCreativeTabs();
	}
}