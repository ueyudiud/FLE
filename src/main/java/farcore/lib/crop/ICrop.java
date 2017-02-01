package farcore.lib.crop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.ISpecie;
import nebula.common.LanguageManager;
import nebula.common.util.IRegisteredNameable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;

public interface ICrop extends IRegisteredNameable, ISpecie<ICropAccess>
{
	ICrop VOID = new CropVoid();
	
	default String getLocalName(GeneticMaterial dna)
	{
		return LanguageManager.translateToLocal("crop." + getTranslatedName(dna) + ".name");
	}
	
	String getTranslatedName(GeneticMaterial dna);
	
	int getMaxStage();
	
	long tickUpdate(ICropAccess access);
	
	void onUpdate(ICropAccess access);
	
	int getGrowReq(ICropAccess access);
	
	void addInformation(ICropAccess access, List<String> list);
	
	boolean canPlantAt(ICropAccess access);
	
	void getDrops(ICropAccess access, ArrayList<ItemStack> list);
	
	default EnumPlantType getPlantType(ICropAccess access)
	{
		return EnumPlantType.Crop;
	}
	
	Collection<String> getAllowedState();
	
	String getState(ICropAccess access);
	
}