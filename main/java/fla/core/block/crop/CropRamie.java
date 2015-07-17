package fla.core.block.crop;

import static fla.api.crop.CropRenderType.O_LIKE;

public class CropRamie extends CropFla
{
	public CropRamie()
	{
		super("ramie");
		this.setCropLevel(1);
		this.setMaturationStage(7);
		this.setTextureName("crop/ramie");
		this.setGrowTick(750);
		this.setCropRender(O_LIKE);
	}
	
}
