package fle.core.block.crop;

public class CropSoybean extends CropBase
{
	public CropSoybean()
	{
		super("soybean");
		setCropLevel(1);
		setMaturationStage(7);
		setTextureName("crop/soybean");
		setGrowTick(800);
	}
}