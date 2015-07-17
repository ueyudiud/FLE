package fla.core.item.tool;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Sets;

import fla.api.item.tool.ItemDamageResource;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.api.util.FlaValue;
import fla.core.tool.AxeManager;

public class ItemFlaAxe extends ItemFlaDigableTool
{
    private static final Set blockCanHeaverst = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin});

	protected ToolMaterial material;

	public ItemFlaAxe(ToolMaterial material)
	{
		super(2.0F);
		this.material = material;
		setNoRepair();
		setMaxDamage(material.getMaxUses());
		AxeManager.registryAxe(new ItemChecker(this, OreDictionary.WILDCARD_VALUE));
	}

	@Override
	public boolean canToolHarvestBlock(ItemStack stack, Block block, int meta) 
	{
		return false;
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
	public float getToolDigSpeed(ItemStack stack, Block block, int meta) 
	{
		return material.getEfficiencyOnProperMaterial();
	}

	@Override
	public void damageItem(EntityLivingBase entity, ItemStack stack,
			ItemDamageResource resource) 
	{
		stack.damageItem(1, entity);
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
		return FlaValue.axe;
	}

}
