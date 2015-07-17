package fla.core.item.tool;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.ForgeHooks;
import fla.api.item.tool.ItemDamageResource;
import fla.api.util.FlaValue;

public class ItemFlaShovel extends ItemFlaDigableTool
{
	private static final Set blockCanHeaverst = Sets.newHashSet(new Block[] {Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium});

	protected ToolMaterial material;

	public ItemFlaShovel(ToolMaterial material)
	{
		super(0.5F);
		this.material = material;
		setNoRepair();
		setMaxDamage(material.getMaxUses());
	}

	@Override
	public boolean canToolHarvestBlock(ItemStack stack, Block block, int meta) 
	{
		return block == Blocks.snow || block == Blocks.snow_layer;
	}

	@Override
	public boolean isToolEffective(ItemStack stack, Block block, int meta) 
	{
        if (ForgeHooks.isToolEffective(stack, block, meta))
        {
            return true;
        }
		return block.getMaterial() != Material.ground && block.getMaterial() != Material.grass && block.getMaterial() != Material.sand ? blockCanHeaverst.contains(block) : true;
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
		return FlaValue.shovel;
	}

}
