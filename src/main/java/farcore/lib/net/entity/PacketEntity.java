package farcore.lib.net.entity;

import java.io.IOException;

import farcore.lib.net.PacketWorld;
import farcore.network.IDescribable;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

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
		entityID = entity.getEntityId();
		if(entity instanceof IDescribable)
			nbt = ((IDescribable) entity).writeDescriptionsToNBT(new NBTTagCompound());
	}

	@Override
	protected void encode(PacketBuffer output) throws IOException
	{
		super.encode(output);
		output.writeInt(entityID);
		output.writeNBTTagCompoundToBuffer(nbt);
	}

	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		super.decode(input);
		entityID = input.readInt();
		nbt = input.readNBTTagCompoundFromBuffer();
	}

	@Override
	public IPacket process(Network network)
	{
		Entity entity = world().getEntityByID(entityID);
		if(entity instanceof IDescribable)
			((IDescribable) entity).readDescriptionsFromNBT(nbt);
		return null;
	}
}