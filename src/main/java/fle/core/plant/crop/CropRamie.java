package fle.core.plant.crop;

import java.util.List;
import java.util.Random;

import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import net.minecraft.item.ItemStack;

public class CropRamie extends CropFle
{
	public CropRamie()
	{
		super("ramie");
		setTextureName("fle:crop/ramie");
		waterWaste = 5;
		maxGrowWaterUse = 12;
		maxStage = 7;
		maxTemp = 410;
		minAliveTemp = 270;
		minGrowTemp = 280;
		minAliveWaterReq = 250;
		minGrowWaterReq = 640;
		growReq = 1250;
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.hotResistance = 1;
	}
	
	@Override
	protected void applyEffect(CropInfo info, String prop)
	{
		switch (prop)
		{
		case "xerophily" : info.dryResistance += 3;
		break;
		case "grain" : info.grain += 1;
		break;
		case "growth" : info.growth += 1;
		break;
		case "resistance" : info.weedResistance += 2;
		break;
		case "cold" : info.coldResistance += 2;
		break;
		case "hot" : info.hotResistance += 2;
		break;
		}
	}

	@Override
	public String instanceDNA(Random random)
	{
		return random.nextBoolean() ? "hot" : "xerophily";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		if(access.getStage() > 5)
		{
			CropInfo info = access.getInfo();
			int grain = info.grain;
			list.add(EnumItem.plant.instance(1 + access.rng().nextInt(grain + 1), "ramie_fiber"));
			list.add(applyChildSeed(access.rng().nextInt(grain + 3) + 2, info));
		}
	}
}