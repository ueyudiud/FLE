package fle.resource.crop;

import net.minecraftforge.common.EnumPlantType;

public class CropSweetPotato extends CropBase
{
	public CropSweetPotato()
	{
		super("sweetpotato");
		setCropLevel(2);
		setMaturationStage(6);
		setTextureName("crop/sweetpotato");
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}