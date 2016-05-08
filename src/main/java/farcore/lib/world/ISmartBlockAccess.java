package farcore.lib.world;

import java.util.Arrays;
import java.util.List;

import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public interface ISmartBlockAccess extends IBlockAccess
{
	static final SmartBlockAccess instance = new SmartBlockAccess();
	
	Block getBlock(int x, int y, int z);
	
	TileEntity getTileEntity(int x, int y, int z);
	
	int getLightBrightnessForSkyBlocks(int x, int y, int z, int type);
	
	int getBlockMetadata(int x, int y, int z);
	
	int isBlockProvidingPowerTo(int x, int y, int z, int side);
	
	boolean isAirBlock(int x, int y, int z);
	
	BiomeGenBase getBiomeGenForCoords(int x, int z);
	
	int getSmartMetadata(int x, int y, int z);
	
	int getDimID();
	
	public static SmartBlockAccess warp(IBlockAccess access)
	{
		return instance.provide(access);
	}
	
	public static class SmartBlockAccess implements ISmartBlockAccess
	{
		ThreadLocal<IBlockAccess> parent = new ThreadLocal();
		
		public SmartBlockAccess provide(IBlockAccess access)
		{
			parent.set(access);
			return this;
		}

		@Override
		public Block getBlock(int x, int y, int z)
		{
			return parent.get().getBlock(x, y, z);
		}

		@Override
		public TileEntity getTileEntity(int x, int y, int z)
		{
			return parent.get().getTileEntity(x, y, z);
		}

		@Override
		public int getLightBrightnessForSkyBlocks(int x, int y, int z, int type)
		{
			return parent.get().getLightBrightnessForSkyBlocks(x, y, z, type);
		}

		@Override
		public int getBlockMetadata(int x, int y, int z)
		{
			return parent.get().getBlockMetadata(x, y, z);
		}

		@Override
		public int isBlockProvidingPowerTo(int x, int y, int z, int side)
		{
			return parent.get().isBlockProvidingPowerTo(x, y, z, side);
		}

		@Override
		public boolean isAirBlock(int x, int y, int z)
		{
			return parent.get().isAirBlock(x, y, z);
		}

		@Override
		public BiomeGenBase getBiomeGenForCoords(int x, int z)
		{
			return parent.get().getBiomeGenForCoords(x, z);
		}

		@Override
		public int getHeight()
		{
			return parent.get().getHeight();
		}

		@Override
		public boolean extendedLevelsInChunkCache()
		{
			return parent.get().extendedLevelsInChunkCache();
		}

		@Override
		public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
		{
			return parent.get().isSideSolid(x, y, z, side, _default);
		}
		
		private static final List<String> list = Arrays.asList("field_72815_e", "worldObj");
		
		@Override
		public int getDimID()
		{
			IBlockAccess access = parent.get();
			if(access instanceof ChunkCache)
			{
				return ((World) U.Reflect.getValue(ChunkCache.class, list, (ChunkCache) access)).provider.dimensionId;
			}
			else if(access instanceof World)
			{
				return ((World) access).provider.dimensionId;
			}
			else if(access instanceof ISmartBlockAccess)
			{
				return ((ISmartBlockAccess) access).getDimID();
			}
			return 0;
		}

		@Override
		public int getSmartMetadata(int x, int y, int z)
		{
			return U.Worlds.datas.getSmartMetadata(getDimID(), x, y, z);
		}
	}
}