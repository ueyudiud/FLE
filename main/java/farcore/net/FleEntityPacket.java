package farcore.net;

import java.io.IOException;

import farcore.entity.IEntityMessageHandler;
import farcore.util.FleLog;
import flapi.FleAPI;
import net.minecraft.entity.Entity;

public class FleEntityPacket extends FleAbstractPacket
{
	int dim;
	int entityID;
	byte type;
	Object contain;
	
	public FleEntityPacket()
	{
	}
	public FleEntityPacket(Entity entity, byte type, Object contain)
	{
		dim = entity.worldObj.provider.getDimensionId();
		this.type = type;
		entityID = entity.getEntityId();
		this.contain = contain;
	}

	@Override
	protected void write(FlePacketBuffer buffer) throws IOException
	{
		buffer.writeInt(dim);
		buffer.writeInt(entityID);
		buffer.writeByte(type);
		buffer.write(contain);
	}

	@Override
	protected void read(FlePacketBuffer buffer) throws IOException
	{
		dim = buffer.readInt();
		entityID = buffer.readInt();
		type = buffer.readByte();
		contain = buffer.read();
	}

	@Override
	public IPacket process(INetworkHandler handler)
	{
		try
		{
			Entity entity = FleAPI.getWorld(dim).getEntityByID(entityID);
			if(entity instanceof IEntityMessageHandler)
			{
				((IEntityMessageHandler) entity).process(type, contain);
			}
		}
		catch(Exception exception)
		{
			FleLog.warn("A exception throwed during process message to entity.");
		}
		return null;
	}
}