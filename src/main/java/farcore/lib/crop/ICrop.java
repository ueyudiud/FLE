package farcore.lib.crop;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.bio.IDNADecoder;
import farcore.lib.util.IRegisteredNameable;
import net.minecraft.item.ItemStack;

public interface ICrop extends IRegisteredNameable, IDNADecoder<ICropAccess>
{
	ICrop VOID = new CropVoid();

	String getLocalName(String dna);

	int getMaxStage();

	long tickUpdate(ICropAccess access);

	void onUpdate(ICropAccess access);

	int getGrowReq(ICropAccess access);

	void addInformation(ICropAccess access, List<String> list);

	boolean canPlantAt(ICropAccess access);

	void getDrops(ICropAccess access, ArrayList<ItemStack> list);
}