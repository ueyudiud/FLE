package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import farcore.block.item.ItemBlockBase;
import farcore.item.enums.EnumItemSize;
import farcore.item.enums.EnumParticleSize;
import farcore.item.interfaces.IItemSize;
import flapi.FleResource;
import flapi.block.item.ItemFleMultipassRender;
import fle.core.enums.EnumRockSize;
import fle.core.enums.EnumRockState;
import fle.resource.block.auto.BlockUniversalRock;

public class ItemRock extends ItemFleMultipassRender<BlockUniversalRock>
implements IItemSize
{
	public ItemRock(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + this.block.getRockName(stack).replace(' ', '.') + "." + this.block.getRockState(stack).name();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(this.block.getRockState(stack).getUnlocalized() + ".name", translateToLocal(this.block.getRock(stack).getTranslateName(), new Object[0]));
	}

	@Override
	public int getDamage(ItemStack stack)
	{
		return block.getRockState(stack).ordinal() * FleResource.rock.size() 
				+ FleResource.rock.serial(block.getRock(stack));
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