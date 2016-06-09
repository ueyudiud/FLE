package fle.api.item.behavior;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.eventhandler.Event.Result;
import farcore.enums.Direction;
import farcore.enums.EnumDamageResource;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class BehaviorHoe extends BehaviorDigableTool
{
	public BehaviorHoe()
	{
		craftingDamage = 0.2F;
		destroyBlockDamageBase = 0.05F;
		destroyBlockDamageHardnessMul = 0.2F;
		hitEntityDamage = 1F;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		Direction direction = Direction.directions[side];
		if(direction != Direction.U)
		{
			return false;
		}
		else
		{
			if(!player.canPlayerEdit(x, y, z, side, stack))
			{
				return false;
			}
			else
			{
				Block block = world.getBlock(x, y, z);
				if(block == Blocks.dirt || block == Blocks.grass)
				{
					player.setItemInUse(stack, getMaxItemUseDuration(stack));
					return true;
				}
			}
		}
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
	{
		if(tick < 100) return;
		MovingObjectPosition mop = ((ItemBase) stack.getItem()).getMovingObjectPositionFromPlayer(world, player, false);
		if(mop.typeOfHit == MovingObjectType.BLOCK)
		{
			int x = mop.blockX;
			int y = mop.blockY;
			int z = mop.blockZ;
			int side = mop.sideHit;
			if(!player.canPlayerEdit(x, y, z, side, stack))
			{
				return;
			}
			else
			{
				Block block = world.getBlock(x, y, z);
				if(block == Blocks.dirt || block == Blocks.grass)
				{
					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getStepResourcePath(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
					if(world.isRemote)
					{
						return;
					}
					if(player.getRNG().nextFloat() < 0.4F)
					{
						world.setBlock(x, y, z, Blocks.farmland, 0, 3);
					}
					U.Inventorys.damage(stack, player, 0.2F, EnumDamageResource.USE);
				}
			}
		}
		else
		{
			super.onPlayerStoppedUsing(stack, world, player, tick);
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 200;
	}
	
	@Override
	public boolean isToolEffective(ItemStack stack, Block block, int metadata)
	{
		Material material = block.getMaterial();
		return material == Material.plants || material == Material.vine ||
				material == Material.web;
	}

	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemDamageUUID, "Tool modifier", 1.0F, 0));
	}
}