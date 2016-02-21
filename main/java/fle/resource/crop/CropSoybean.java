package fle.resource.crop;

import net.minecraftforge.common.EnumPlantType;

public class CropSoybean extends CropBase
{
	public CropSoybean()
	{
		super("soybean");
		setCropLevel(1);
		setMaturationStage(7);
		setTextureName("crop/soybean");
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}