package farcore.lib.crop;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.bio.IDNADecoder;
import farcore.lib.material.Mat;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.property.IUnlistedProperty;

public interface ICrop extends IRegisteredNameable, IDNADecoder<ICropAccess>
{
	IUnlistedProperty<String> CROP_NAME = new IUnlistedProperty<String>()
	{
		@Override
		public String getName(){return "crop_name";}
		@Override
		public boolean isValid(String value){return Mat.register.contain(value) && Mat.register.get(value).isCrop;}
		@Override
		public Class<String> getType(){return String.class;}
		@Override
		public String valueToString(String value){return value;}
	};
	IUnlistedProperty<String> CROP_STATE = new IUnlistedProperty<String>()
	{
		@Override
		public String getName(){return "crop_state";}
		@Override
		public boolean isValid(String value){return true;}
		@Override
		public Class<String> getType(){return String.class;}
		@Override
		public String valueToString(String value){return value;}
	};
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

	String getIconKey(ICropAccess access);
	
	default EnumPlantType getPlantType(ICropAccess access)
	{
		return EnumPlantType.Crop;
	}
}