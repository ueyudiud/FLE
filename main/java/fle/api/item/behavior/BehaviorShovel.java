package fle.api.item.behavior;

import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class BehaviorShovel extends BehaviorDigableTool
{
	private static final Set blockCanHeaverst = Sets.newHashSet(new Block[] { Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium });
	private static final Set toolClasses = Sets.newHashSet("shovel");

	private float damageVsEntity;
	
	public BehaviorShovel(float dve)
	{
		destroyBlockDamage = 1;
		hitEntityDamage = 2;
		damageVsEntity = dve;
	}
		
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		Material material = block.getMaterial();
		return material == Material.snow  || super.canHarvestBlock(block, stack);
	}
	
	@Override
	public boolean isToolEffective(ItemStack stack, Block block, int metadata) 
	{
		Material material = block.getMaterial();
		return material == Material.ground || material == Material.sand || material == Material.grass ||
				blockCanHeaverst.contains(block);
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return toolClasses;
	} 
	
	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemDamageUUID, "Tool modifier", damageVsEntity, 0));
	}
}