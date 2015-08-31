package fle.core.block.crop;

import net.minecraftforge.common.EnumPlantType;

public class CropSoybean extends CropBase
{
	public CropSoybean()
	{
		super("soybean");
		setCropLevel(1);
		setMaturationStage(7);
		setTextureName("crop/soybean");
		setGrowTick(1200);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}