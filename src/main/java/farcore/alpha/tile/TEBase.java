package farcore.alpha.tile;

import java.util.Random;

import farcore.FarCoreSetup;
import farcore.enums.Direction;
import farcore.network.IPacket;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TEBase extends TileEntity
{
	public Random random = new Random();
	public boolean isUpdating;
	
	public TEBase()
	{
		
	}
	
	public boolean isInitialized()
	{
		return true;
	}
	
	public boolean isUpdating()
	{
		return isUpdating;
	}
	
	@Override
	public boolean isInvalid()
	{
		return super.isInvalid();
	}
	
	public boolean isClient()
	{
		return worldObj == null ? !U.Sides.isSimulating() : worldObj.isRemote;
	}
	
	public boolean isServer()
	{
		return worldObj == null ? U.Sides.isSimulating() : !worldObj.isRemote;
	}
	
	@Override
	public final void updateEntity()
	{
		isUpdating = false;
		updateEntity1();
		isUpdating = true;
	}
	
	protected void updateEntity1()
	{
		
	}
	
	public void sendToAll(IPacket player)
	{
		FarCoreSetup.network.sendToAll(player);
	}
	
	public void sendToServer(IPacket packet)
	{
		FarCoreSetup.network.sendToServer(packet);
	}
	
	public void sendToPlayer(IPacket packet, EntityPlayer player)
	{
		FarCoreSetup.network.sendToPlayer(packet, player);
	}
	
	public void sendLargeToPlayer(IPacket packet, EntityPlayer player)
	{
		FarCoreSetup.network.sendLargeToPlayer(packet, player);
	}
	
	public void sendToNearby(IPacket packet, float range)
	{
		if(worldObj != null)
		{
			FarCoreSetup.network.sendToNearBy(packet, worldObj.provider.dimensionId, xCoord, yCoord, zCoord, range);
		}
	}
	
	public void sendToDim(IPacket packet)
	{
		if(worldObj != null)
		{
			sendToDim(packet, worldObj.provider.dimensionId);
		}
	}
	
	public void sendToDim(IPacket packet, int dim)
	{
		FarCoreSetup.network.sendToDim(packet, dim);
	}
	
	@Override
	public double getDistanceFrom(double x, double y, double z)
	{
		return super.getDistanceFrom(x, y, z);
	}
	
	public double getDistanceFrom(Entity entity)
	{
		return getDistanceFrom(entity.posX, entity.posY, entity.posZ);
	}
	
	public Direction getRotation()
	{
		return Direction.Q;
	}
	
	/**
	 * The rotate for block check,
	 * Info : The direction must be 2D rotation!
	 * @param frontOffset
	 * @param lrOffset
	 * @param udOffset
	 * @param direction
	 * @param block
	 * @param meta
	 * @param ignoreUnloadChunk
	 * @return
	 */
	public boolean matchBlock(int frontOffset, int lrOffset, int udOffset, Direction direction, Block block, int meta, boolean ignoreUnloadChunk)
	{
		if(worldObj == null) return false;
		int x = xCoord + frontOffset * direction.x + lrOffset * direction.z;
		int y = yCoord + udOffset;
		int z = zCoord + frontOffset * direction.z + lrOffset * direction.x;
		return matchBlock(x, y, z, block, meta, ignoreUnloadChunk);
	}
	
	public boolean matchBlock(int offsetX, int offsetY, int offsetZ, Block block, int meta, boolean ignoreUnloadChunk)
	{
		return worldObj == null ? false :
			U.Worlds.isBlock(worldObj, xCoord + offsetX, yCoord + offsetY, zCoord + offsetZ, block, meta, ignoreUnloadChunk);
	}
	
	public void removeBlock()
	{
		removeBlock(0, 0, 0);
	}
	
	public void removeBlock(int xOffset, int yOffset, int zOffset)
	{
		worldObj.removeTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
		worldObj.setBlockToAir(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
	}
	
	public boolean canBlockStay()
	{
		return worldObj == null ? true : getBlockType().canBlockStay(worldObj, xCoord, yCoord, zCoord);
	}
	
	public TileEntity getTile(int xOffset, int yOffset, int zOffset)
	{
		return worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
	}
	
	public boolean isCatchRain()
	{
		return U.Worlds.isCatchingRain(worldObj, xCoord, yCoord, zCoord);
	}
	
	public float getBaseTemperature()
	{
		return U.Worlds.getTemp(worldObj, xCoord, yCoord, zCoord);
	}
	
	public int getRainfall()
	{
		return worldObj.getBiomeGenForCoords(xCoord, zCoord).getIntRainfall();
	}
}