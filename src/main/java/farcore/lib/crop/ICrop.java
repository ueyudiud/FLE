package farcore.lib.crop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import farcore.lib.bio.IDNADecoder;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;

public interface ICrop extends IRegisteredNameable, IDNADecoder<ICropAccess>
{
	ICrop VOID = new CropVoid();
	
	default String getLocalName(String dna)
	{
		return LanguageManager.translateToLocal("crop." + getTranslatedName(dna) + ".name");
	}
	
	String getTranslatedName(String dna);
	
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