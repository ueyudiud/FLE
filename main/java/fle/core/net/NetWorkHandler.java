package fle.core.net;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fle.FLE;
import fle.api.net.FleAbstractPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.net.FlePackets.CoderFWMUpdate;
import fle.api.net.FlePackets.CoderGuiUpdate;
import fle.api.net.FlePackets.CoderInventoryUpdate;
import fle.api.net.FlePackets.CoderMatterUpdate;
import fle.api.net.FlePackets.CoderNBTUpdate;
import fle.api.net.FlePackets.CoderPTUpdate;
import fle.api.net.FlePackets.CoderTankUpdate;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.core.net.FlePackets.CoderCropUpdate;
import fle.core.net.FlePackets.CoderFWMAskMeta;
import fle.core.net.FlePackets.CoderKeyType;

public class NetWorkHandler implements FleNetworkHandler
{
	public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(FLE.MODID.toLowerCase());

	private int idx = 0;

	public void init()
	{
		registerMessage(CoderFWMUpdate.class, Side.CLIENT);
		registerMessage(CoderFWMAskMeta.class, Side.SERVER);
		registerMessage(CoderKeyType.class, Side.SERVER);
		registerMessage(CoderGuiUpdate.class, Side.SERVER);
		registerMessage(CoderInventoryUpdate.class, Side.CLIENT);
		registerMessage(CoderTankUpdate.class, Side.CLIENT);
		registerMessage(CoderNBTUpdate.class, Side.CLIENT);
		registerMessage(CoderPTUpdate.class, Side.CLIENT);
		registerMessage(CoderTileUpdate.class, Side.CLIENT);
		registerMessage(CoderCropUpdate.class, Side.CLIENT);
		registerMessage(CoderMatterUpdate.class, Side.CLIENT);
	}
	
	@Override
	public <T extends FleAbstractPacket> void registerMessage(Class<? extends T> aType, Side side)
	{
		try
		{
			instance.registerMessage(aType.newInstance(), aType, idx++, side);
		} 
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void sendTo(FleAbstractPacket aPacket)
	{
		if(FLE.fle.getPlatform().isSimulating())
			instance.sendToAll(aPacket);
		else instance.sendToServer(aPacket);
	}
	
	@Override
	public void sendToPlayer(FleAbstractPacket aPacket, EntityPlayerMP aPlayer)
	{
		instance.sendTo(aPacket, aPlayer);
	}
	
	@Override
	public void sendToDim(FleAbstractPacket aPacket, int dim)
	{
		instance.sendToDimension(aPacket, dim);
	}
	
	@Override
	public void sendToServer(FleAbstractPacket aPacket)
	{
		instance.sendToServer(aPacket);
	}
	
	@Override
	public void sendToNearBy(FleAbstractPacket aPacket, TargetPoint aPoint)
	{
		instance.sendToAllAround(aPacket, aPoint);
	}
}