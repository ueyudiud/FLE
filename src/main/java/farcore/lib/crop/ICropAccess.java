package farcore.lib.crop;

import java.util.Random;

import farcore.lib.world.ICrood;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public interface ICropAccess extends ICrood
{
	ICrop crop();
	
	CropInfo info();
	
	BiomeGenBase biome();
	
	boolean isWild();
	
	int stage();
	
	Random rng();
	
	void grow(int growth);
	
	float temp();
}