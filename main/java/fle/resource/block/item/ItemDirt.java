package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import farcore.block.item.ItemBlockBase;
import farcore.item.enums.EnumItemSize;
import farcore.item.enums.EnumParticleSize;
import farcore.item.interfaces.IItemSize;
import flapi.FleResource;
import flapi.block.item.ItemFleMultipassRender;
import fle.resource.block.auto.BlockUniversalDirt;

public class ItemDirt extends ItemFleMultipassRender<BlockUniversalDirt>
implements IItemSize
{
	public ItemDirt(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "."
				+ this.block.getDirtName(stack).replace(' ', '.');
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(
				block.getState(stack).getUnlocalized() + ".name",
				translateToLocal(block.getDirt(stack).getTranslateName(),
						new Object[0]));
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		return block.getState(stack).ordinal() * FleResource.dirt.size() + 
				FleResource.dirt.serial(block.getDirtName(stack));
	}

	@Override
	public EnumItemSize getItemSize(ItemStack stack)
	{
		return EnumItemSize.medium;
	}

	@Override
	public EnumParticleSize getParticleSize(ItemStack stack)
	{
		return EnumParticleSize.colloid;
	}
}