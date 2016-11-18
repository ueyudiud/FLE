package farcore.lib.crop;

import java.util.Random;

import farcore.lib.bio.IBiology;
import farcore.lib.world.ICoord;
import net.minecraft.world.biome.Biome;

public interface ICropAccess extends ICoord, IBiology
{
	ICrop crop();

	CropInfo info();

	Biome biome();

	boolean isWild();

	int stage();

	Random rng();

	int getWaterLevel();

	void grow(int growth);

	void setStage(int stage);

	int useWater(int amount);

	float temp();

	void killCrop();
}