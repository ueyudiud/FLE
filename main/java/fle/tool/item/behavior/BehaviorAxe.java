package fle.tool.item.behavior;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Sets;

import flapi.item.ItemFleMetaBase;

public class BehaviorAxe extends BehaviorDigable
{
    private static final Set blockCanHeaverst = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin});

    float effective;
    
    public BehaviorAxe(float aBaseEffective) 
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
    	return aBlock.getMaterial() == Material.wood || aBlock.getMaterial() == Material.plants || aBlock.getMaterial() == Material.vine ? true : blockCanHeaverst.contains(aBlock) ? true : super.isBlockEffective(aStack, aBlock, aMeta);
    }
    
    @Override
    public boolean canHarvestBlock(ItemFleMetaBase item, Block aBlock,
    		int aMeta, ItemStack aStack) 
    {
    	return false;
    }
}