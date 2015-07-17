package fla.core.block.crop;

import fla.api.crop.ICropTile;
import fla.api.recipe.DropInfo;

public class CropSoybean extends CropFla
{
	public CropSoybean()
	{
		super("soybean");
		this.setCropLevel(1);
		this.setMaturationStage(7);
		this.setTextureName("crop/soybean");
		this.setGrowTick(800);
	}
}