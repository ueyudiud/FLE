package farcore.lib.world;

import net.minecraft.world.World;

public interface IWorldDatas
{
	int getSmartMetadata(World world, int x, int y, int z);
	
	int getSmartMetadata(int dim, int x, int y, int z);
	
	void setSmartMetadata(World world, int x, int y, int z, int meta);

	void setSmartMetadataWithNotify(int dim, int x, int y, int z, short meta, int flag);
	
	void setSmartMetadataWithNotify(World world, int x, int y, int z, int meta, int flag);
	
	int[] saveChunkData(int dim, int x, int z);
	
	void loadChunkData(int dim, int x, int z, int[] data);

}