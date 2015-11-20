package fle.api.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.te.IObjectInWorld;

public final class BlockPos implements IObjectInWorld
{
	public final IBlockAccess access;
	public final int x, z;
	public final short y;

	public BlockPos(IBlockAccess access, double x, double y, double z)
	{
		this.access = access;
		this.x = (int) x;
		this.y = (short) y;
		this.z = (int) z;
	}
	public BlockPos(Entity entity)
	{
		this(entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
	}
	
	public BlockPos toPos(ForgeDirection d)
	{
		return new BlockPos(access, x + d.offsetX, y + d.offsetY, z + d.offsetZ);
	}
	
	public BlockPos toPos(int xOffset, int yOffset, int zOffset)
	{
		return new BlockPos(access, x + xOffset, y + yOffset, z + zOffset);
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
		return new ChunkPos(x / 16, z / 16);
	}
	
	public Block getBlock()
	{
		return y < 256 && y > 0 ? access.getBlock(x, y, z) : null;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof BlockPos)
		{
			BlockPos tPos = (BlockPos) obj;
			return tPos.getDim() == getDim() && tPos.x == x && tPos.y == y && tPos.z == z;
		}
		return super.equals(obj);
	}
	
	public int getDim()
	{
        if(access instanceof World) return ((World) access).provider.dimensionId;
        else if(FleAPI.mod.getPlatform().getPlayerInstance() != null) return FleAPI.mod.getPlatform().getPlayerInstance().worldObj.provider.dimensionId;
        else return 0;
	}
	
	public World world()
	{
        if(access instanceof World) return (World) access;
        return FleAPI.mod.getPlatform().getWorldInstance(getDim());
	}
	
	@Override
	public BlockPos getBlockPos() 
	{
		return this;
	}
	
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

	@Override
	public int hashCode() 
	{
		int code = y;
        code = 255 * code + x;
        code = 0xFFFFFF * code + z;
		return code;
	}

	public boolean isAir()
	{
		return getBlock().isAir(access, x, y, z);
	}

	public boolean isReplacable()
	{
		Block tBlock = getBlock();
		if(tBlock == Blocks.fire || tBlock == Blocks.air)
			return true;
		return getBlock().isReplaceable(access, x, y, z) && !(tBlock.getMaterial() == Material.water || tBlock.getMaterial() == Material.lava);
	}
}