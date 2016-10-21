package farcore.lib.tile;

import java.util.Random;

import farcore.FarCore;
import farcore.lib.util.Direction;
import farcore.lib.world.ICoord;
import farcore.network.IPacket;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class TEBase extends TileEntity implements ICoord
{
	public Random random = new Random();
	public boolean isUpdating;
	private int lightLevel;

	public TEBase()
	{

	}

	@Override
	public void onLoad()
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
			FarCore.network.sendToNearBy(packet, this, range);
		}
	}

	public void sendToDim(IPacket packet)
	{
		if(worldObj != null)
		{
			sendToDim(packet, worldObj.provider.getDimension());
		}
	}

	public void sendToDim(IPacket packet, int dim)
	{
		if(worldObj != null)
		{
			FarCore.network.sendToDim(packet, dim);
		}
	}

	public void syncToAll()
	{

	}

	public void syncToDim()
	{

	}

	public void syncToNearby()
	{

	}

	public void syncToPlayer(EntityPlayer player)
	{

	}

	public void markBlockUpdate()
	{
		worldObj.notifyBlockOfStateChange(pos, getBlockType());
	}

	public void markBlockRenderUpdate()
	{
		worldObj.markBlockRangeForRenderUpdate(pos.add(-1, -1, -1), pos.add(1, 1, 1));
	}

	@Override
	public double getDistanceSq(double x, double y, double z)
	{
		return super.getDistanceSq(x, y, z);
	}

	public double getDistanceFrom(double x, double y, double z)
	{
		return Math.sqrt(getDistanceSq(x, y, z));
	}

	public double getDistanceSq(Entity entity)
	{
		return getDistanceSq(entity.posX, entity.posY, entity.posZ);
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
	 * INFO : The direction must be 2D rotation!
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
		int x = frontOffset * direction.x + lrOffset * direction.z;
		int y = udOffset;
		int z = frontOffset * direction.z + lrOffset * direction.x;
		return matchBlock(x, y, z, block, meta, ignoreUnloadChunk);
	}

	public boolean matchBlock(int offsetX, int offsetY, int offsetZ, Block block, int meta, boolean ignoreUnloadChunk)
	{
		return worldObj == null ? false :
			U.Worlds.isBlock(worldObj, pos.add(offsetX, offsetY, offsetZ), block, meta, ignoreUnloadChunk);
	}

	public boolean matchBlockNearby(int offsetX, int offsetY, int offsetZ, Block block, int meta, boolean ignoreUnloadChunk)
	{
		return worldObj == null ? false :
			U.Worlds.isBlockNearby(worldObj, pos.add(offsetX, offsetY, offsetZ), block, meta, ignoreUnloadChunk);
	}

	public void explode(boolean removeTile, float strength, boolean isFlaming, boolean isSmoking)
	{
		IBlockState state = null;
		if(!removeTile)
		{
			state = worldObj.getBlockState(pos);
		}
		worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), removeTile ? 3 : 4);
		worldObj.newExplosion(null, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, strength, isFlaming, isSmoking);
		if(!removeTile)
		{
			worldObj.setBlockState(pos, state, 4);
			worldObj.setTileEntity(pos, this);
		}
	}

	@Override
	public void removeBlock()
	{
		worldObj.setBlockToAir(pos);
	}
	
	public boolean canHarvestBlock(EntityPlayer player)
	{
		return ForgeHooks.canHarvestBlock(getBlockType(), player, worldObj, pos);
	}
	
	public float getBlockHardness(IBlockState state)
	{
		return 1.0F;
	}

	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 1.0F;
	}

	public int getLightOpacity(IBlockState state)
	{
		return 255;
	}

	public int getLightValue(IBlockState state)
	{
		return 255;
	}
	
	public void onBlockAdded(IBlockState state)
	{
		
	}

	public void onBlockBreak(IBlockState state)
	{

	}

	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.PASS;
	}

	public boolean onBlockClicked(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	/**
	 * Mark light for update.
	 * Usually used when machine state changed,
	 * example : furnace start burning fuel.
	 * Marked to recalculate light.
	 * @param type
	 */
	public void markLightForUpdate(EnumSkyBlock type)
	{
		int level = worldObj.getLightFor(type, pos);
		if(lightLevel != level)
		{
			U.Worlds.checkLight(worldObj, pos);
			lightLevel = level;
		}
	}

	public boolean canBlockStay()
	{
		return worldObj == null ? true :
			getBlockType().canPlaceBlockAt(worldObj, pos);
	}
	
	public void openGUI(EntityPlayer player, int id)
	{
		openGUI(player, FarCore.ID, id);
	}
	
	public void openGUI(EntityPlayer player, Object modid, int id)
	{
		player.openGui(modid, id, worldObj, pos.getX(), pos.getY(), pos.getZ());
	}

	public IBlockState getBlock(int xOffset, int yOffset, int zOffset)
	{
		return worldObj.getBlockState(pos.add(xOffset, yOffset, zOffset));
	}
	
	@Override
	public TileEntity getTE()
	{
		return this;
	}

	@Deprecated
	public TileEntity getTile(int xOffset, int yOffset, int zOffset)
	{
		return worldObj.getTileEntity(pos.add(xOffset, yOffset, zOffset));
	}

	public int getLight(int xOffset, int yOffset, int zOffset, EnumSkyBlock type)
	{
		return worldObj.getLightFor(type, pos.add(xOffset, yOffset, zOffset));
	}
	
	@Override
	public World world()
	{
		return worldObj;
	}

	@Override
	public BlockPos pos()
	{
		return pos;
	}
}