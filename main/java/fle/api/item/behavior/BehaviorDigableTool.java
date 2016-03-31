package fle.api.item.behavior;

import farcore.enums.EnumDamageResource;
import farcore.util.U;
import fle.core.item.ItemToolFle;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BehaviorDigableTool extends BehaviorBase
{
	protected int destroyBlockDamage;
	protected int hitEntityDamage;
	
	@Override
	public float getDigSpeed(ItemStack stack, World world, int x, int y, int z, Block block, int metadata)
	{
		if(isToolEffective(stack, block, metadata) || ForgeHooks.isToolEffective(stack, block, metadata))
		{
			return ItemToolFle.getToolMaterial(stack).digSpeed;
		}
		return 1.0F;
	}
	
	public boolean isToolEffective(ItemStack stack, Block block, int metadata)
	{
		return false;
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		return super.canHarvestBlock(block, stack);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
			EntityLivingBase entity)
	{
		if(block.getBlockHardness(world, x, y, z) > 0)
		{
			U.Inventorys.damage(stack, entity, destroyBlockDamage, EnumDamageResource.KNOCK);
		}
		return true;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		U.Inventorys.damage(stack, player, hitEntityDamage, EnumDamageResource.HIT);
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}
}