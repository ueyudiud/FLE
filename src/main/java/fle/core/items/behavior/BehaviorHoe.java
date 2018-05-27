/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import nebula.common.item.BehaviorBase;
import nebula.common.item.ITool;
import nebula.common.tool.EnumToolType;
import nebula.common.util.W;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class BehaviorHoe extends BehaviorBase
{
	int				hoeChance;
	EnumToolType	toolType;
	
	public BehaviorHoe(EnumToolType toolType, int chance)
	{
		this.toolType = toolType;
		this.hoeChance = chance;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!player.canPlayerEdit(pos, facing, stack))
			return EnumActionResult.FAIL;
		else
		{
			UseHoeEvent event = new UseHoeEvent(player, stack, world, pos);
			if (MinecraftForge.EVENT_BUS.post(event))
				return EnumActionResult.FAIL;
			else if (event.getResult() == Result.ALLOW) return EnumActionResult.SUCCESS;
			
			if (facing == EnumFacing.UP)
			{
				player.setActiveHand(hand);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.PASS;
		}
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		int tick = stack.getMaxItemUseDuration() - timeLeft;
		if (tick > 20)
		{
			RayTraceResult result = W.rayTrace(world, entity, false);
			if (result != null && result.typeOfHit == Type.BLOCK && result.sideHit == EnumFacing.UP)
			{
				if (tick > 40 || entity.getRNG().nextInt(100) < this.hoeChance)
				{
					if (!world.isRemote)
					{
						if (entity instanceof EntityPlayer)
						{
							world.playSound((EntityPlayer) entity, result.getBlockPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
						}
						world.setBlockState(result.getBlockPos(), Blocks.FARMLAND.getDefaultState());
					}
					((ITool) stack.getItem()).onToolUse(entity, stack, this.toolType, 1.0F);
				}
				else
				{
					((ITool) stack.getItem()).onToolUse(entity, stack, this.toolType, 0.3F);
				}
			}
		}
	}
}
