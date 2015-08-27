package fle.core.block.crop;

import static fle.api.enums.EnumCropRender.O_LIKE;

public class CropRamie extends CropBase
{
	public CropRamie()
	{
		super("ramie");
		setCropLevel(1);
		setMaturationStage(7);
		setTextureName("crop/ramie");
		setGrowTick(1100);
		setCropRender(O_LIKE);
	}
}