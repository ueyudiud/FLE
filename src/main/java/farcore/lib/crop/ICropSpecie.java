/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.lib.bio.BioData;
import farcore.lib.bio.ISpecieSP;
import farcore.lib.material.Mat;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;

/**
 * @author ueyudiud
 */
public interface ICropSpecie extends ISpecieSP
{
	ICropSpecie VOID = new CropVoid();
	
	Mat material();
	
	BioData random(Random rand);
	
	@Override
	ICropFamily<?> getFamily();
	
	int getMaxStage();
	
	long tickUpdate(ICropAccess access);
	
	void onUpdate(ICropAccess access);
	
	int getGrowReq(ICropAccess access);
	
	void addInformation(ICropAccess access, List<String> list);
	
	boolean canPlantAt(ICropAccess access);
	
	void getDrops(ICropAccess access, ArrayList<ItemStack> list);
	
	default EnumPlantType getPlantType(ICropAccess access)
	{
		return access.isWild() ? EnumPlantType.Plains : EnumPlantType.Crop;
	}
	
	void expressTraits(CropInfo info, BioData data);
}
