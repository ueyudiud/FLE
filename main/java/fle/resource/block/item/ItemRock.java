package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import farcore.block.item.ItemBlockBase;
import fle.resource.block.BlockRock;

public class ItemRock extends ItemBlockBase<BlockRock>
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
		//return FleResource.rock.serial(stack.getSubCompound("rock", true).getString("rock"));
		return 0;
	}
	
	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		//stack.getSubCompound("rock", true).setString("rock", FleResource.rock.name(damage));
	}
}