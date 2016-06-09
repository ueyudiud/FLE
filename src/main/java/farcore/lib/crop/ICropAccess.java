package farcore.lib.crop;

import java.util.Random;

import farcore.lib.bio.IBiology;
import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import scala.collection.generic.BitOperations.Int;

public interface ICropAccess extends IBiology
{	
	CropCard getCrop();
	
	CropInfo getInfo();
	
	World getWorld();
	
	BiomeBase getBiome();

	boolean isWild();
	
	int getStage();

	Random rng();
	
	void setStage(int stage);
	
	void grow(int amount);
	
	void markUpdate();
	
	boolean isBlock(int offsetX, int offsetY, int offsetZ, Block block, int meta);
	
	/**
	 * To check is block near by the bottom block of this crop.
	 * @param block
	 * @param meta
	 * @return
	 */
	boolean isBlockNearby(Block block, int meta);
	
	/**
	 * Count whole water block.
	 * @param rangeXZ
	 * @param rangeY
	 * @param checkSea Count water in ocean biome if this is true.
	 * @return
	 */
	int countWater(int rangeXZ, int rangeY, boolean checkSea);
	
	void absorbWater(int rangeXZ, int rangeY, int amount, boolean checkSea);
	
	/**
	 * Give 4 weight for rainfall and 1 always count and multiply the
	 * rainfall of biome.
	 * @return
	 */
	float getRainfall();
	
	float getTemp();
	
	int getWaterLevel();
	
	int useWater(int amount);
	
	void killCrop();
}