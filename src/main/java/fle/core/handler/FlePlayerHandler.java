package fle.core.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fle.api.FLEAPI;
import fle.api.util.DamageSourceTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class FlePlayerHandler
{
	private static final Map<UUID, PlayerInformation> map = new HashMap();
	
	public static class PlayerInformation
	{
		boolean dronInToVoid = false;
		int respawnXReal;
		int respawnYReal;
		int respawnZReal;
	}
	
	private PlayerInformation getOrCreateInformation(EntityPlayer player)
	{
		UUID uuid = player.getGameProfile().getId();
		if(map.containsKey(uuid))
			return map.get(uuid);
		PlayerInformation information = new PlayerInformation();
		map.put(uuid, information);
		return information;
	}
	
	@SubscribeEvent
	public void onPlayerSetSpawnpoint(PlayerSetSpawnEvent event)
	{
		
	}
	
	@SubscribeEvent
	public void onPlayerDead(LivingDeathEvent event)
	{
		if(!event.getEntityLiving().isServerWorld()) return;
		if(event.getEntityLiving() instanceof EntityPlayerMP &&
				event.getSource() instanceof DamageSourceTransfer)
		{
			markTransferPlayerToVoid((EntityPlayerMP) event.getEntityLiving());
		}
	}
	
	private void markTransferPlayerToVoid(EntityPlayerMP player)
	{
		player.setSpawnChunk(new BlockPos(0, 128, 0), true, FLEAPI.voidDimID);
		PlayerInformation information = getOrCreateInformation(player);
		information.dronInToVoid = true;
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		
	}

	@SubscribeEvent
	public void onPlayerRespawn(LivingUpdateEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayerMP)
		{
			if(getOrCreateInformation((EntityPlayer) event.getEntityLiving()).dronInToVoid)
			{
				if(event.getEntityLiving().worldObj.provider.getDimension() != FLEAPI.voidDimID)
				{
					((EntityPlayerMP) event.getEntityLiving()).inventory.clear();
					event.getEntityLiving().setDead();
				}
			}
		}
	}
}