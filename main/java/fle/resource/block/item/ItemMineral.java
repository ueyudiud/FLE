package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import farcore.block.item.ItemBlockBase;
import farcore.item.enums.EnumItemSize;
import farcore.item.enums.EnumParticleSize;
import farcore.item.interfaces.IItemSize;
import flapi.FleResource;
import flapi.block.item.ItemFleMultipassRender;
import flapi.item.ItemFle;
import fle.core.enums.EnumRockState;
import fle.resource.block.auto.BlockMineral;

public class ItemMineral extends ItemFleMultipassRender<BlockMineral>
implements IItemSize
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
		return (block.getRockState(stack).ordinal() * FleResource.rock.size()
				+ FleResource.rock.serial(block.getRockName(stack))) * FleResource.mineral.size() 
				+ FleResource.mineral.serial(block.getMineralName(stack));
	}

	@Override
	public EnumItemSize getItemSize(ItemStack stack)
	{
		return EnumItemSize.medium;
	}

	@Override
	public EnumParticleSize getParticleSize(ItemStack stack)
	{
		EnumRockState state = block.getRockState(stack);
		return state == EnumRockState.resource ? EnumParticleSize.block :
			state == EnumRockState.cobble ? EnumParticleSize.chunk :
				state == EnumRockState.crush ? EnumParticleSize.ingotic :
					EnumParticleSize.unused;
	}
}