package fle.core.plant.crop;

import java.util.List;
import java.util.Random;

import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import net.minecraft.item.ItemStack;

public class CropSweetPotato extends CropFle
{
	public CropSweetPotato()
	{
		super("sweetpotato");
		setTextureName("fle:crop/sweetpotato");
		waterWaste = 6;
		maxGrowWaterUse = 9;
		maxStage = 6;
		maxTemp = 402;
		minAliveTemp = 266;
		minGrowTemp = 279;
		minAliveWaterReq = 275;
		minGrowWaterReq = 600;
		growReq = 1500;
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.coldResistance = 1;
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
		return random.nextBoolean() ? "growth" : "cold";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		if(access.getStage() > 4)
		{
			CropInfo info = access.getInfo();
			int grain = info.grain;
			list.add(applyChildSeed(access.rng().nextInt(grain / 2 + 1) + 2, info));
		}
	}
}