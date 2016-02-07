package farcore.world;

import farcore.FarCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

public final class BlockPos implements IObjectInWorld
{	
	public static final class ChunkPos
	{
		public final long x, z;

		public ChunkPos(Chunk aChunk)
		{
			x = aChunk.xPosition;
			z = aChunk.zPosition;
		}
		public ChunkPos(long x, long z)
		{
			this.x = x;
			this.z = z;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof ChunkPos) 
			{
				ChunkPos pos = (ChunkPos) obj;
				return pos.x == x && pos.z == z;
			}
			return super.equals(obj);
		}
		
		@Override
		public int hashCode()
		{
			return (int) (x << 20 + z);
		}
	}
	
	public final IBlockAccess access;
	public final int 
	/** The dimension of position.*/
	t, 
	x, 
	z;
	/** Only used 256 number of y. */
	public final short y;

	public BlockPos(int t, double x, double y, double z)
	{
		access = FarCore.getWorldInstance(t);
		this.t = t;
		this.x = (int) x;
		this.y = (short) y;
		this.z = (int) z;
	}
	public BlockPos(IBlockAccess access, double x, double y, double z)
	{
		this.access = access;
		this.t = dim();
		this.x = (int) x;
		this.y = (short) y;
		this.z = (int) z;
	}
	public BlockPos(IObjectInWorld object)
	{
		this.access = object.world();
		this.t = dim();
		this.x = object.pos().x;
		this.y = object.pos().y;
		this.z = object.pos().z;
	}
	public BlockPos(Entity entity)
	{
		this(entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
	}
	
	private int dim()
	{
        if(access instanceof World) return ((World) access).provider.dimensionId;
        else if(FarCore.getPlayerInstance() != null) return FarCore.getPlayerInstance().worldObj.provider.dimensionId;
        else return 0;
	}
	
	public BlockPos offset(ForgeDirection direction)
	{
		return new BlockPos(access, x + direction.offsetX, 
				y + direction.offsetY, z + direction.offsetZ);
	}
	
	public BlockPos offset(Direction direction)
	{
		return new BlockPos(t + direction.d, x + direction.x, 
				y + direction.y, z + direction.z);
	}
	
	public BlockPos offset(int xOffset, int yOffset, int zOffset)
	{
		return new BlockPos(access, x + xOffset, y + yOffset, z + zOffset);
	}
	
	public World world()
	{
        if(access instanceof World) return (World) access;
        return FarCore.getWorldInstance(t);
	}
	
	@Override
	public BlockPos pos()
	{
		return this;
	}
	
	public Block block()
	{
		return y < 256 && y > 0 ? access.getBlock(x, y, z) : Blocks.air;
	}
	
	public int meta()
	{
		return access.getBlockMetadata(x, y, z);
	}
	
	public Material material()
	{
		return block().getMaterial();
	}
	
	public BiomeGenBase biome()
	{
		return access.getBiomeGenForCoords(x, z);
	}
	
	public TileEntity tile()
	{
		return y < 256 && y > 0 ? access.getTileEntity(x, y, z) : null;
	}
	
	public ChunkPos chunkPos()
	{
		return new ChunkPos(x / 16, z / 16);
	}
	
	public int tempreture()
	{
		return FarCore.getEnviormentTemperature(world(), this);
	}

	public int rainfall()
	{
		return biome().getIntRainfall();
	}
	
	public boolean isAir()
	{
		return access.isAirBlock(x, y, z);
	}

	public boolean isReplacable()
	{
		Block tBlock = block();
		if(tBlock == Blocks.fire || tBlock == Blocks.air)
			return true;
		return block().isReplaceable(access, x, y, z) && !(tBlock.getMaterial() == Material.water || tBlock.getMaterial() == Material.lava);
	}
	
	public boolean isLoaded(int range)
	{
		return world().checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range);
	}
	
	public boolean isRaining()
	{
		return world().isRaining();
	}
	
	public boolean isThundering()
	{
		return world().isThundering();
	}
	
	public boolean isDaytime()
	{
		return world().isDaytime();
	}
	
	public int getLight()
	{
		return (int) (world().getLightBrightness(x, y, z) * 16);
	}
	
	//--------------World set start-----------------
	public void set(Block block)
	{
		world().setBlock(x, y, z, block);
	}
	public void set(Block block, int meta, int flag)
	{
		world().setBlock(x, y, z, block, meta, flag);
	}
	
	public void setToAir()
	{
		world().setBlockToAir(x, y, z);
	}
	
	public void tickUpdate()
	{
		tickUpdate(block().tickRate(world()));
	}
	
	public void tickUpdate(int delay)
	{
		world().scheduleBlockUpdate(x, y, z, block(), delay);
	}
	
	public void markForUpdate()
	{
		world().markBlockForUpdate(x, y, z);
	}
	
	public void markRenderForUpdate(int range)
	{
		world().markBlockRangeForRenderUpdate(x - range, y - range, z - range,
				x + range, y + range, z + range);
	}
	
	public void createExplosion(float strength, boolean breakBlock)
	{
		world().createExplosion(null, x + .5F, y + .5F, z + .5F,
				strength, breakBlock);
	}
	
	public void createExplosion(float strength, boolean burning,
			boolean breakBlock)
	{
		world().newExplosion(null, x + .5F, y + .5F, z + .5F, strength,
				burning, breakBlock);
	}
	//------------------World set end---------------------
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof BlockPos)
		{
			BlockPos tPos = (BlockPos) obj;
			return tPos.t == t && tPos.x == x && tPos.y == y && tPos.z == z;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() 
	{
		return ((t * 31 + x) * 31 + y) * 31 + z;
	}
}