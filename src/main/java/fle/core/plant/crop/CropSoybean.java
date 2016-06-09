package fle.core.plant.crop;

import java.util.List;
import java.util.Random;

import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import net.minecraft.item.ItemStack;

public class CropSoybean extends CropFle
{
	public CropSoybean()
	{
		super("soybean");
		setTextureName("fle:crop/soybean");
		waterWaste = 3;
		maxGrowWaterUse = 8;
		maxStage = 7;
		maxTemp = 406;
		minAliveTemp = 259;
		minGrowTemp = 279;
		minAliveWaterReq = 180;
		minGrowWaterReq = 480;
		growReq = 1600;
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
		return random.nextInt(3) == 0 ? "cold" : "xerophily";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		if(access.getStage() > 5)
		{
			CropInfo info = access.getInfo();
			int grain = info.grain;
			list.add(applyChildSeed(access.rng().nextInt(grain + 3) + 2, info));
		}
	}
}