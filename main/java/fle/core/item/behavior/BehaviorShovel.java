package fle.core.item.behavior;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Sets;

import fle.api.item.ItemFleMetaBase;

public class BehaviorShovel extends BehaviorDigable
{
	private static final Set blockCanHeaverst = Sets.newHashSet(new Block[] {Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium});

    float effective;
    
    public BehaviorShovel(float aBaseEffective) 
    {
    	effective = aBaseEffective;
	}
    
    @Override
    public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
    		Block aBlock, int aMetadata) 
    {
    	return super.getDigSpeed(item, aStack, aBlock, aMetadata) * effective;
    }
    
	@Override
	public boolean isBlockEffective(ItemStack aStack, Block aBlock, int aMeta) 
	{
		return aBlock.getMaterial() == Material.ground || aBlock.getMaterial() == Material.grass || aBlock.getMaterial() == Material.sand ? true : blockCanHeaverst.contains(aBlock) ? true : super.isBlockEffective(aStack, aBlock, aMeta);
	}
	
	@Override
	public boolean canHarvestBlock(ItemFleMetaBase item, Block aBlock,
			int aMeta, ItemStack aStack)
	{
		return aBlock == Blocks.snow || aBlock == Blocks.snow_layer ? true : super.canHarvestBlock(item, aBlock, aMeta, aStack);
	}
}