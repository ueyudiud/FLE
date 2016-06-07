package farcore.lib.net.entity;

import java.io.IOException;

import farcore.interfaces.IDescribable;
import farcore.lib.net.PacketWorld;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.io.DataStream;
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
		super(entity.worldObj);
		this.entityID = entity.getEntityId();
		if(entity instanceof IDescribable)
		{
			nbt = ((IDescribable) entity).writeDescriptionsToNBT(new NBTTagCompound());
		}
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeInt(entityID);
		output.writeNBT(nbt);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		entityID = input.readInt();
		nbt = input.readNBT();
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		Entity entity = world().getEntityByID(entityID);
		if(entity instanceof IDescribable)
		{
			((IDescribable) entity).readDescriptionsFromNBT(nbt);
		}
		return null;
	}
}