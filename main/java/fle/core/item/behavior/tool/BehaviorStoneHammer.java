package fle.core.item.behavior.tool;

import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.ICrushableTool;
import fle.core.tool.StoneHammerHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
		return StoneHammerHandler.isHammerEffective(aBlock, aMeta, aStack);
	}

	@Override
	public boolean doCrush(World aWorld, int x, int y, int z, ItemStack aStack)
	{
		return true;
	}
}