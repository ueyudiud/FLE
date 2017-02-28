package farcore.lib.crop;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IBiology;
import farcore.lib.bio.IFamily;
import nebula.common.world.ICoord;
import net.minecraft.world.biome.Biome;

public interface ICropAccess extends ICoord, IBiology
{
	@Override
	default ICrop getSpecie()
	{
		return crop();
	}
	
	@Override
	default IFamily<ICropAccess> getFamily()
	{
		return crop().getFamily();
	}
	
	ICrop crop();
	
	CropInfo info();
	
	Biome biome();
	
	boolean isWild();
	
	int stage();
	
	float stageBuffer();
	
	int getWaterLevel();
	
	void grow(int growth);
	
	void setStage(int stage);
	
	int useWater(int amount);
	
	float temp();
	
	void killCrop();
	
	/**
	 * Pollinating pollen or spore to spread genetic material to
	 * this crop.
	 * @param gm
	 */
	void pollinate(GeneticMaterial gm);
}