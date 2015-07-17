package fla.core.item.tool;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import fla.api.item.tool.ItemDamageResource;
import fla.api.util.FlaValue;

public class ItemRoughAxe extends ItemFlaDigableTool
{
    private Set blockCanHeaverst = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin});

	public ItemRoughAxe() 
	{
		super(3.0F);
		setMaxDamage(8);
		setNoRepair();
	}

	@Override
	public boolean isToolEffective(ItemStack stack, Block block, int meta) 
	{
        if (ForgeHooks.isToolEffective(stack, block, meta))
        {
            return true;
        }
		return block.getMaterial() != Material.wood && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine ? blockCanHeaverst.contains(block) : true;
	}

	@Override
	public boolean canToolHarvestBlock(ItemStack stack, Block block, int meta) 
	{
		return false;
	}

	@Override
	public float getToolDigSpeed(ItemStack stack, Block block, int meta) 
	{
		return 2.0F;
	}

	@Override
	public void damageItem(EntityLivingBase entity, ItemStack stack,
			ItemDamageResource resource) 
	{
		stack.damageItem(1, entity);
	}

	@Override
	public int getToolDamage(ItemStack stack) 
	{
		return stack.getItemDamage();
	}
	
	@Override
	public int getToolMaxDamage(ItemStack stack) 
	{
		return 8;
	}

	@Override
	public String getToolType(ItemStack stack) 
	{
		return FlaValue.axe;
	}
}