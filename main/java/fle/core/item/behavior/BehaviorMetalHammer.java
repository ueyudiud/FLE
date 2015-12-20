package fle.core.item.behavior;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import flapi.enums.EnumCraftingType;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.ICrushableTool;
import flapi.item.interfaces.ISubPolishTool;
import flapi.recipe.CraftingState;
import fle.core.tool.StoneHammerHandler;
import fle.tool.ToolMaterialInfo;
import fle.tool.item.behavior.BehaviorTool;

public class BehaviorMetalHammer extends BehaviorTool implements ICrushableTool, ISubPolishTool<ItemFleMetaBase>
{
	@Override
	public boolean onItemDamageBlock(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, EntityLivingBase aEntity, World aWorld, int aX,
			int aY, int aZ)
	{
		item.damageItem(aStack, aEntity, EnumDamageResource.DestoryBlock, 1F);
		return true;
	}
	
	public boolean isBlockEffective(ItemStack aStack, Block aBlock, int aMeta)
	{
		return (aBlock.getMaterial() != Material.iron && aBlock.getMaterial() != Material.anvil && aBlock.getMaterial() != Material.rock) ? ForgeHooks.isToolEffective(new ItemStack(Items.iron_pickaxe), aBlock, aMeta) : true;
	}
	
	@Override
	public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, int aMetadata)
	{
		return StoneHammerHandler.isHammerEffective(aBlock, aMetadata, aStack) ? new ToolMaterialInfo(item.setupNBT(aStack)).getHardness() * 3.0F : super.getDigSpeed(item, aStack, aBlock, aMetadata);
	}
	
	@Override
	public boolean doCrush(World aWorld, int x, int y, int z, ItemStack aStack)
	{
		return true;
	}

	@Override
	public ItemStack getOutput(ItemFleMetaBase item, ItemStack aStack,
			EntityPlayer aPlayer)
	{
		item.damageItem(aStack, aPlayer, EnumDamageResource.Crafting, 1F);
		return aStack;
	}

	@Override
	public CraftingState getState(ItemFleMetaBase item, ItemStack aStack,
			EnumCraftingType aType, CraftingState aState)
	{
		if(aType == EnumCraftingType.polish) return CraftingState.CRUSH;
		return aState;
	}
}