package farcore.lib.net.tile;

import java.io.IOException;

import farcore.interfaces.tile.IDescribableTile;
import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.TileEntitySyncable;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.util.FleLog;
import farcore.util.io.DataStream;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTileSyncable extends PacketBlockCoord
{
	private NBTTagCompound datas;
	
	public PacketTileSyncable()
	{
		
	}
	public PacketTileSyncable(TileEntitySyncable syncable)
	{
		this(syncable.getWorldObj(), syncable.xCoord, syncable.yCoord, syncable.zCoord, syncable.writeDescriptionsToNBT(new NBTTagCompound()));
	}
	public PacketTileSyncable(World world, int x, int y, int z, NBTTagCompound datas)
	{
		super(world, x, y, z);
		this.datas = datas;
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeNBT(datas);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		datas = input.readNBT();
	}

	@Override
	public IPacket process(NetworkBasic network)
	{
		TileEntity tile = world().getTileEntity(x, y, z);
		if(tile instanceof IDescribableTile)
		{
			IDescribableTile describable = (IDescribableTile) tile;
			describable.readDescriptionsFromNBT(datas);
		}
		return null;
	}
}