/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.entity.IDescribable;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;
import net.minecraft.entity.Entity;

public class PacketEntityAsk extends PacketWorld
{
	int entityID;
	
	public PacketEntityAsk()
	{
		
	}
	
	public PacketEntityAsk(Entity entity)
	{
		super(entity.world);
		this.entityID = entity.getEntityId();
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeInt(this.entityID);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.entityID = input.readInt();
	}
	
	@Override
	public IPacket process(Network network)
	{
		Entity entity = world().getEntityByID(this.entityID);
		if (entity instanceof IDescribable)
		{
			((IDescribable) entity).markNBTSync(getPlayer());
		}
		return null;
	}
}
