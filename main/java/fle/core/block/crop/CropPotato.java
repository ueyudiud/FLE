package fle.core.block.crop;

import net.minecraftforge.common.EnumPlantType;

public class CropPotato extends CropBase
{
	public CropPotato()
	{
		super("potato");
		setCropLevel(2);
		setMaturationStage(7);
		setTextureName("crop/potatoes");
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}