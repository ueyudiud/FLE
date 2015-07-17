package fla.core.item.tool;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import fla.api.item.tool.IStoneHammer;
import fla.api.item.tool.ItemDamageResource;
import fla.api.util.FlaValue;
import fla.core.tool.StoneHammerManager;

public class ItemStoneHammer extends ItemFlaDigableTool implements IStoneHammer
{
	protected float digSpeed;

	public ItemStoneHammer(ToolMaterial material)
	{
		super(3.5F);
		digSpeed = material.getEfficiencyOnProperMaterial();
		setMaxDamage(material.getMaxUses());
		setNoRepair();
	}

	@Override
	public boolean canCrushBlock(Block block, int meta) 
	{
		return StoneHammerManager.canBlockCrush(block, meta);
	}

	@Override
	public void damageItem(EntityLivingBase entity, ItemStack stack,
			ItemDamageResource resource) 
	{
		stack.damageItem(1, entity);
	}

	@Override
	public boolean canToolHarvestBlock(ItemStack stack, Block block, int meta) 
	{
		Set<String> sets = getToolClasses(stack);
		for(String str : sets)
		{
			if(block.isToolEffective(str, meta) && block.getHarvestLevel(meta) < 1)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isToolEffective(ItemStack stack, Block block, int meta) 
	{
		return ForgeHooks.isToolEffective(new ItemStack(Items.wooden_pickaxe), block, meta);
	}

	@Override
	public float getToolDigSpeed(ItemStack stack, Block block, int meta) 
	{
		return digSpeed;
	}

	@Override
	public int getToolMaxDamage(ItemStack stack) 
	{
		return stack.getMaxDamage();
	}

	@Override
	public int getToolDamage(ItemStack stack) 
	{
		return stack.getItemDamage();
	}

	@Override
	public String getToolType(ItemStack stack) 
	{
		return FlaValue.hammer_rock;
	}
}