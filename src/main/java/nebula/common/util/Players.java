/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.util.ArrayList;
import java.util.List;

import nebula.Nebula;
import nebula.common.data.EnumToolType;
import nebula.common.item.ITool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/**
 * @author ueyudiud
 */
public class Players
{
	public static EntityPlayer player()
	{
		return Nebula.proxy.playerInstance();
	}
	
	public static List<EnumToolType> getCurrentToolType(EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemMainhand();
		//		if(stack == null)
		//		{
		//			stack = player.getHeldItemOffhand();
		//		}
		if(stack == null) return EnumToolType.HAND_USABLE_TOOL;
		if(stack.getItem() instanceof ITool)
			return ((ITool) stack.getItem()).getToolTypes(stack);
		List<EnumToolType> list = new ArrayList();
		for(EnumToolType toolType : EnumToolType.getToolList())
		{
			if(toolType.match(stack))
			{
				list.add(toolType);
			}
		}
		return list;
	}
	
	public static boolean matchCurrentToolType(EntityPlayer player, EnumToolType...types)
	{
		ItemStack stack = player.getHeldItemMainhand();
		if(stack == null)
		{
			stack = player.getHeldItemOffhand();
		}
		if(stack == null)
		{
			for(EnumToolType type : types)
				if(type == EnumToolType.HAND)
					return true;
			return false;
		}
		List<EnumToolType> list;
		if(stack.getItem() instanceof ITool)
		{
			list = ((ITool) stack.getItem()).getToolTypes(stack);
			for(EnumToolType type : types)
			{
				if(list.contains(type)) return true;
			}
			return false;
		}
		final ItemStack stack2 = stack;
		return L.or(types, type -> type.toolMatch(stack2));
	}
	
	public static void destoryPlayerCurrentItem(EntityPlayer player)
	{
		if(player == null) return;
		if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().stackSize <= 0)
		{
			player.renderBrokenItemStack(player.getHeldItemMainhand());
			player.setHeldItem(EnumHand.MAIN_HAND, null);
		}
		if(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().stackSize <= 0)
		{
			player.renderBrokenItemStack(player.getHeldItemOffhand());
			player.setHeldItem(EnumHand.OFF_HAND, null);
		}
	}
	
	public static Entity moveEntityToAnotherDim(Entity entity, int dim, double x, double y, double z)
	{
		WorldServer targetWorld = DimensionManager.getWorld(dim);
		if(targetWorld != null)
			return moveEntityToAnotherDim(entity, dim, x, y, z, targetWorld.getDefaultTeleporter());
		return null;
	}
	
	public static Entity moveEntityToAnotherDim(Entity entity, int dim, double x, double y, double z, Teleporter teleporter)
	{
		WorldServer targetWorld = DimensionManager.getWorld(dim);
		WorldServer originalWorld = DimensionManager.getWorld(entity.world.provider.getDimension());
		if(targetWorld != null && originalWorld != null && targetWorld != originalWorld)
		{
			if(entity.isRiding())
			{
				entity.dismountRidingEntity();
			}
			if(entity.isBeingRidden())
			{
				entity.removePassengers();
			}
			if (entity instanceof EntityPlayerMP)
			{
				EntityPlayerMP player = (EntityPlayerMP) entity;
				player.mcServer.getPlayerList().transferPlayerToDimension(player, dim, teleporter);
				player.connection.sendPacket(new SPacketEffect(1032, BlockPos.ORIGIN, 0, false));
				player.setLocationAndAngles(x, y, z, player.rotationYaw, player.rotationPitch);
				return player;
			}
			else
			{
				Entity newEntity = entity.changeDimension(dim);
				if (newEntity != null)
				{
					entity.setPosition(x + 0.5D, y + 0.5D, z + 0.5D);
				}
				return newEntity;
			}
		}
		return null;
	}
}