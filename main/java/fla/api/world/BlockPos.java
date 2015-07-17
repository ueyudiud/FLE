package fla.api.world;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public final class BlockPos 
{
	public final IBlockAccess access;
	public final int x, y, z;
	
	public BlockPos(IBlockAccess access, int x, int y, int z)
	{
		this.access = access;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockPos toPos(ForgeDirection d)
	{
		return new BlockPos(access, x + d.offsetX, y + d.offsetY, z + d.offsetZ);
	}
	
	public BlockPos toPos(int xMove, int yMove, int zMove)
	{
		return new BlockPos(access, x + xMove, y + yMove, z + zMove);
	}
	
	public int getBlockMeta()
	{
		return access.getBlockMetadata(x, y, z);
	}
	
	public BiomeGenBase getBiome()
	{
		return access.getBiomeGenForCoords(x, z);
	}
	
	public TileEntity getBlockTile()
	{
		return y < 256 && y > 0 ? access.getTileEntity(x, y, z) : null;
	}
	
	public ChunkPos getChunkPos()
	{
		return new ChunkPos(x >> 4, z >> 4);
	}
	
	public static final class ChunkPos
	{
		public final long x, z;
		
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
			return (int) (x << 20 + z);//For zPos more than 1024 * 1024 / 2 may have somethings wrong.
		}
	}
	
	public Block getBlock()
	{
		return y < 256 && y > 0 ? access.getBlock(x, y, z) : null;
	}
}
