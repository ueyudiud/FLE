package farcore.lib.net.entity;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import farcore.interfaces.IDescribable;
import farcore.lib.net.PacketWorld;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.util.io.DataStream;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;

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
	public IPacket process(NetworkBasic network)
	{
		Entity entity = world().getEntityByID(entityID);
		if(entity instanceof IDescribable)
		{
			((IDescribable) entity).markNBTSync(getPlayer());
		}
		return null;
	}
}