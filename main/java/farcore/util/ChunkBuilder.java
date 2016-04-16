package farcore.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.compress.compressors.z.ZCompressorInputStream;

import farcore.lib.collection.IntArray;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import scala.inline;

/**
 * Far land era chunk builder.<br>
 * This builder help to set block and meta, also set tile entity(Such like 
 * far land rock) to cache when generate terrain.
 * @author ueyudiud
 *
 */
public class ChunkBuilder
{
	private final Block[] blocks = new Block[16 * 16 * 256];
	private final byte[] metas = new byte[16 * 16 * 256];
	private final Map<IntArray, TileEntity> tiles = new HashMap();
	
	public ChunkBuilder()
	{
		
	}
	
	public Block[] getBlocks()
	{
		return blocks;
	}

	public byte[] getMetas()
	{
		return metas;
	}
	
	public void add(int x, int y, int z, Block block)
	{
		blocks[(x & 15) << 12 | (z & 15) << 8 | (y & 255)] = block;
	}
	
	public void add(int x, int y, int z, Block block, int meta)
	{
		blocks[(x & 15) << 12 | (z & 15) << 8 | (y & 255)] = block;
		metas[(x & 15) << 12 | (z & 15) << 8 | (y & 255)] = (byte) (meta & 15);
	}
	
	public Block get(int x, int y, int z)
	{
		return blocks[(x & 15) << 12 | (z & 15) << 8 | (y & 255)];
	}
	
	public void set(int x, int y, int z, int meta)
	{
		metas[(x & 15) << 12 | (z & 15) << 8 | (y & 255)] = (byte) (meta & 15);
	}
	
	public void set(int x, int y, int z, TileEntity tile)
	{
		tile.invalidate();
		tiles.put(new IntArray(new int[]{x, y, z}), tile);
	}
	
	public void remove(int x, int y, int z)
	{
		add(x, y, z, null, 0);
		IntArray array = new IntArray(new int[]{x, y, z});
		if(tiles.containsKey(array))
		{
			tiles.remove(array);
		}
	}
	
	public Chunk build(World world, int x, int z, BiomeGenBase[] biomes)
	{
		Chunk chunk = new Chunk(world, blocks, metas, x, z);
		for(Entry<IntArray, TileEntity> entry : tiles.entrySet())
		{
			chunk.func_150812_a(entry.getKey().array[0], entry.getKey().array[1], entry.getKey().array[2], entry.getValue());
			world.addTileEntity(entry.getValue());
		}
		chunk.generateSkylightMap();
		byte[] bytes = chunk.getBiomeArray();

        for (int k = 0; k < bytes.length; ++k)
        {
            bytes[k] = (byte) biomes[k].biomeID;
        }
		return chunk;
	}
	
	public void reset()
	{
		Arrays.fill(blocks, null);
		Arrays.fill(metas, (byte) 0);
		tiles.clear();
	}
}