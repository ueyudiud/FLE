package farcore.lib.world;

import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;

public class FarCoreWorldAccess implements IWorldAccess
{
	protected World world;
	
	public FarCoreWorldAccess(World world)
	{
		this.world = world;
	}
	
	@Override
	public void markBlockForUpdate(int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		if(block.isAir(world, x, y, z))
		{
			U.Worlds.datas.setSmartMetadata(world, x, y, z, 0);
		}
	}

	@Override
	public void markBlockForRenderUpdate(int x, int y, int z)
	{
		
	}

	@Override
	public void markBlockRangeForRenderUpdate(int minX, int minY, int minZ, int maxX,
			int maxY, int maxZ)
	{
		
	}

	@Override
	public void playSound(String tag, double xPos, double yPos, double zPos, float p_72704_8_,
			float p_72704_9_)
	{
		
	}

	@Override
	public void playSoundToNearExcept(EntityPlayer player, String tag, double xPos, double yPos,
			double zPos, float tick, float dur)
	{
		
	}

	@Override
	public void spawnParticle(String tag, double xPos, double yPos, double zPos,
			double xMove, double yMove, double zMove)
	{
		
	}

	@Override
	public void onEntityCreate(Entity entity)
	{
		
	}

	@Override
	public void onEntityDestroy(Entity entity)
	{
		
	}

	@Override
	public void playRecord(String tag, int x, int y, int z)
	{
		
	}

	@Override
	public void broadcastSound(int x, int y, int z, int p_82746_4_, int p_82746_5_)
	{
		
	}

	@Override
	public void playAuxSFX(EntityPlayer player, int x, int y, int z, int p_72706_5_,
			int p_72706_6_)
	{
		
	}

	@Override
	public void destroyBlockPartially(int x, int y, int z, int p_147587_4_,
			int p_147587_5_)
	{
		
	}

	@Override
	public void onStaticEntitiesChanged()
	{
		
	}
}