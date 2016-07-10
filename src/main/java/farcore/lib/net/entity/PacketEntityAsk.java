package farcore.lib.net.entity;

import java.io.IOException;

import farcore.lib.io.DataStream;
import farcore.lib.net.PacketWorld;
import farcore.network.IDescribable;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.entity.Entity;

public class PacketEntityAsk extends PacketWorld
{
	int entityID;
	
	public PacketEntityAsk()
	{
		
	}
	public PacketEntityAsk(Entity entity)
	{
		super(entity.worldObj);
		this.entityID = entity.getEntityId();
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeInt(entityID);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		entityID = input.readInt();
	}
	
	@Override
	public IPacket process(Network network)
	{
		Entity entity = world().getEntityByID(entityID);
		if(entity instanceof IDescribable)
		{
			((IDescribable) entity).markNBTSync(getPlayer());
		}
		return null;
	}
}