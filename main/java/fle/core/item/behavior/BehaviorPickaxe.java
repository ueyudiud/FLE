package fle.core.item.behavior;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.item.IItemBehaviour;
import fle.api.item.ItemFleMetaBase;

public class BehaviorPickaxe extends BehaviorDigable
{ 
	private static final Set set = Sets.newHashSet(new Block[] {Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail});

	@Override
	public boolean canHarvestBlock(ItemFleMetaBase item, Block aBlock,
			int aMeta, ItemStack aStack)
	{
		return aBlock != Blocks.diamond_block && aBlock != Blocks.diamond_ore ? 
				(aBlock != Blocks.emerald_ore && aBlock != Blocks.emerald_block ? 
						(aBlock != Blocks.gold_block && aBlock != Blocks.gold_ore ? 
								(aBlock != Blocks.iron_block && aBlock != Blocks.iron_ore ? 
										(aBlock != Blocks.lapis_block && aBlock != Blocks.lapis_ore ? 
												(aBlock != Blocks.redstone_ore && aBlock != Blocks.lit_redstone_ore ? 
														(aBlock.getMaterial() == Material.rock ? true : 
															(aBlock.getMaterial() == Material.iron ? true : 
																aBlock.getMaterial() == Material.anvil)) : 
																	item.getHarvestLevel(aStack, "pickaxe") >= 2) : 
																		item.getHarvestLevel(aStack, "pickaxe") >= 1) : 
																			item.getHarvestLevel(aStack, "pickaxe") >= 1) : 
																				item.getHarvestLevel(aStack, "pickaxe") >= 2) : 
																					item.getHarvestLevel(aStack, "pickaxe") >= 2) : 
																						item.getHarvestLevel(aStack, "pickaxe") >= 2;
	}
	
	@Override
	public boolean isBlockEffective(ItemStack aStack, Block aBlock, int aMeta)
	{
		return (aBlock.getMaterial() != Material.iron && aBlock.getMaterial() != Material.anvil && aBlock.getMaterial() != Material.rock) && !set.contains(aBlock) ? super.isBlockEffective(aStack, aBlock, aMeta) : true;
	}
}