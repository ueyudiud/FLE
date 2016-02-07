package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import farcore.block.item.ItemBlockBase;
import farcore.item.enums.EnumItemSize;
import farcore.item.enums.EnumParticleSize;
import farcore.item.interfaces.IItemSize;
import flapi.FleResource;
import flapi.block.item.ItemFleMultipassRender;
import fle.resource.block.auto.BlockUniversalSand;

public class ItemSand extends ItemFleMultipassRender<BlockUniversalSand>
implements IItemSize
{
	public ItemSand(Block block)
	{
		super(block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + this.block.getSandName(stack).replace(' ', '.');
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(this.block.getSand(stack).getTranslateName(), new Object[0]);
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		return FleResource.sand.serial(block.getSandName(stack));
	}

	@Override
	public EnumItemSize getItemSize(ItemStack stack)
	{
		return EnumItemSize.large;
	}

	@Override
	public EnumParticleSize getParticleSize(ItemStack stack)
	{
		return EnumParticleSize.dust;
	}
}