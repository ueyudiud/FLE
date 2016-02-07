package farcore.net;

import java.io.IOException;

import farcore.FarCore;
import farcore.entity.EntityFleFallingBlock;
import farcore.entity.IEntityMessageHandler;
import farcore.util.FleLog;
import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class FleFallingBlockPacket extends FleAbstractPacket
{
	int dim;
	int entityID;
	Block falling;
	int meta;
	NBTTagCompound nbt;
	
	public FleFallingBlockPacket()
	{
		
	}
	public FleFallingBlockPacket(EntityFleFallingBlock entity)
	{
		dim = entity.worldObj.provider.dimensionId;
		entityID = entity.getEntityId();
		falling = entity.block;
		meta = entity.meta;
		nbt = entity.tileEntityData;
	}

	@Override
	protected void write(FleDataOutputStream buffer) throws IOException
	{
		buffer.writeInt(dim);
		buffer.writeInt(entityID);
		buffer.writeBlock(falling);
		buffer.writeInt(meta);
		buffer.writeNBT(nbt);
	}

	@Override
	protected void read(FleDataInputStream buffer) throws IOException
	{
		dim = buffer.readInt();
		entityID = buffer.readInt();
		falling = buffer.readBlock();
		meta = buffer.readInt();
		nbt = buffer.readNBT();
	}

	@Override
	public IPacket process(INetworkHandler handler)
	{
		try
		{
			Entity entity = FarCore.getWorldInstance(dim).getEntityByID(entityID);
			if(entity instanceof EntityFleFallingBlock)
			{
				EntityFleFallingBlock falling = (EntityFleFallingBlock) entity;
				falling.block = this.falling;
				falling.meta = meta;
				falling.tileEntityData = nbt;
			}
		}
		catch(Exception exception)
		{
			FleLog.warn("A exception throwed during process message to falling block.");
		}
		return null;
	}
}