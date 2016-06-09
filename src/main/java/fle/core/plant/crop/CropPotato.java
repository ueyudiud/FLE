package fle.core.plant.crop;

import java.util.List;
import java.util.Random;

import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import net.minecraft.item.ItemStack;

public class CropPotato extends CropFle
{
	public CropPotato()
	{
		super("potato");
		setTextureName("fle:crop/potatoes");
		waterWaste = 5;
		maxGrowWaterUse = 9;
		maxStage = 7;
		maxTemp = 402;
		minAliveTemp = 266;
		minGrowTemp = 279;
		minAliveWaterReq = 250;
		minGrowWaterReq = 700;
		growReq = 1300;
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.hotResistance = 1;
		info.dryResistance = 1;
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
		case "growth" : info.growth += 2;
		break;
		case "resistance" : info.weedResistance += 2;
		break;
		case "cold" : info.coldResistance += 3;
		break;
		case "hot" : info.hotResistance += 2;
		break;
		}
	}

	@Override
	public String instanceDNA(Random random)
	{
		return random.nextBoolean() ? "xerophily" : "cold";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		if(access.getStage() > 5)
		{
			CropInfo info = access.getInfo();
			int grain = info.grain;
			list.add(applyChildSeed(access.rng().nextInt(grain / 2 + 1) + 2, info));
		}
	}
}