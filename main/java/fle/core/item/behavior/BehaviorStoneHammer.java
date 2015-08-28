package fle.core.item.behavior;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import fle.api.item.ICrushableTool;
import fle.api.item.ItemFleMetaBase;

public class BehaviorStoneHammer extends BehaviorDigable implements ICrushableTool
{
	float effect;
	int level;
	
	public BehaviorStoneHammer(int aLevel, float aEffective) 
	{
		effect = aEffective;
		level = aLevel;
	}
	
	@Override
	public boolean canHarvestBlock(ItemFleMetaBase item, Block aBlock,
			int aMeta, ItemStack aStack) 
	{
		return false;
	}
	
	@Override
	public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, int aMetadata) 
	{
		return super.getDigSpeed(item, aStack, aBlock, aMetadata) * effect;
	}

	@Override
	public boolean isBlockEffective(ItemStack aStack, Block aBlock, int aMeta) 
	{
		if(aBlock == Blocks.obsidian)
		{
			return true;
		}
		if(aBlock.isToolEffective("pickaxe", aMeta) && aBlock.getHarvestLevel(aMeta) <= level)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean doCrush(World aWorld, int x, int y, int z, ItemStack aStack)
	{
		return true;
	}
}