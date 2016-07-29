package farcore.lib.crop.instance;

import java.util.ArrayList;

import farcore.lib.crop.CropAbstract;
import farcore.lib.crop.ICropAccess;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CropReed extends CropAbstract
{
	public CropReed(Mat material)
	{
		super(material);
		dnaHelper = new CropDNAHelper();
		maxStage = 6;
		growReq = 1400;
	}

	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		super.getDrops(access, list);
		list.add(new ItemStack(Items.REEDS, access.rng().nextInt(2 + access.info().growth) + 1));
	}
}