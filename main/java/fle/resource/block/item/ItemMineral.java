package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import farcore.block.item.ItemBlockBase;
import fle.resource.block.BlockMineral;

public class ItemMineral extends ItemBlockBase<BlockMineral>
{
	public ItemMineral(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + this.block.getMineralName(stack).replace(' ', '.') + "." + this.block.getRockState(stack).name();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(this.block.getRockState(stack).getUnlocalized() + ".ore.name", translateToLocal(this.block.getMineral(stack).getTranslateName(), new Object[0]));
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		//return FleResource.mineral.serial(stack.getSubCompound("mineral", true).getString("ore"));
		return 0;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		//stack.getSubCompound("mineral", true).setString("ore", FleResource.mineral.name(damage));
	}
}