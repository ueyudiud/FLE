package fle.api.item.behavior;

import com.google.common.collect.Multimap;

import farcore.enums.Direction;
import farcore.enums.EnumDamageResource;
import farcore.interfaces.ISmartPlantableBlock;
import farcore.item.ItemBase;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BehaviorSickle extends BehaviorDigableTool
{
	public BehaviorSickle()
	{
		craftingDamage = 0.2F;
		destroyBlockDamageBase = 0.05F;
		destroyBlockDamageHardnessMul = 0.2F;
		hitEntityDamage = 1F;
	}
	
	@Override
	public boolean isToolEffective(ItemStack stack, Block block, int metadata)
	{
		Material material = block.getMaterial();
		return material == Material.plants || material == Material.vine ||
				material == Material.web || material == Material.leaves;
	}

	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemDamageUUID, "Tool modifier", 1.0F, 0));
	}
}