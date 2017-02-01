package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.entity.IDescribable;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class PacketEntity extends PacketWorld
{
	int entityID;
	NBTTagCompound nbt;
	
	public PacketEntity()
	{
		
	}
	public PacketEntity(Entity entity)
	{
		super(entity.world);
		this.entityID = entity.getEntityId();
		if(entity instanceof IDescribable)
		{
			this.nbt = ((IDescribable) entity).writeDescriptionsToNBT(new NBTTagCompound());
		}
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeInt(this.entityID);
		output.writeCompoundTag(this.nbt);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.entityID = input.readInt();
		this.nbt = input.readCompoundTag();
	}
	
	@Override
	public IPacket process(Network network)
	{
		Entity entity = world().getEntityByID(this.entityID);
		if(entity instanceof IDescribable)
		{
			((IDescribable) entity).readDescriptionsFromNBT(this.nbt);
		}
		return null;
	}
}