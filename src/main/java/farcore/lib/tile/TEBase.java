package farcore.lib.tile;

import java.util.Random;

import farcore.FarCore;
import farcore.lib.util.Direction;
import farcore.lib.world.ICoord;
import farcore.network.IPacket;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TEBase extends TileEntity implements ICoord
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
		if(worldObj != null)
		{
			FarCore.network.sendToAll(player);
		}
	}
	
	public void sendToServer(IPacket packet)
	{
		if(worldObj != null)
		{
			FarCore.network.sendToServer(packet);
		}
	}
	
	public void sendToPlayer(IPacket packet, EntityPlayer player)
	{
		if(worldObj != null)
		{
			FarCore.network.sendToPlayer(packet, player);
		}
	}
	
	public void sendLargeToPlayer(IPacket packet, EntityPlayer player)
	{
		if(worldObj != null)
		{
			FarCore.network.sendLargeToPlayer(packet, player);
		}
	}
	
	public void sendToNearby(IPacket packet, float range)
	{
		if(worldObj != null)
		{
			FarCore.network.sendToNearBy(packet, worldObj.provider.dimensionId, xCoord, yCoord, zCoord, range);
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
		if(worldObj != null)
		{
			FarCore.network.sendToDim(packet, dim);
		}
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
	
	public void onNeighbourBlockChange()
	{
		
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
	
	public boolean matchBlockNearby(int offsetX, int offsetY, int offsetZ, Block block, int meta, boolean ignoreUnloadChunk)
	{
		return worldObj == null ? false :
			U.Worlds.isBlockNearby(worldObj, xCoord + offsetX, yCoord + offsetY, zCoord + offsetZ, block, meta, ignoreUnloadChunk);
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

	public void onBlockBreak(Block block, int meta)
	{
		
	}

	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	public boolean onBlockClicked(EntityPlayer player, int side)
	{
		return false;
	}
	
	public boolean canBlockStay()
	{
		return worldObj == null ? true : getBlockType().canBlockStay(worldObj, xCoord, yCoord, zCoord);
	}
	
	public Block getBlock(int xOffset, int yOffset, int zOffset)
	{
		return worldObj.getBlock(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
	}
	
	public TileEntity getTile(int xOffset, int yOffset, int zOffset)
	{
		return worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);
	}
	
	public boolean isCatchRain()
	{
		return U.Worlds.isCatchingRain(worldObj, xCoord, yCoord, zCoord);
	}
	
//	public float getBaseTemperature()
//	{
//		return U.Worlds.getTemp(worldObj, xCoord, yCoord, zCoord);
//	}
//	
//	public float getRainfall()
//	{
//		return worldObj.getBiomeGenForCoords(xCoord, zCoord).rainfall;
//	}

	@Override
	public World world()
	{
		return worldObj;
	}

	@Override
	public double[] coordD()
	{
		return new double[]{xCoord + 0.5, yCoord + 0.5, zCoord + 0.5};
	}
	
	public int[] coordI() 
	{
		return new int[]{xCoord, yCoord, zCoord};
	}
}