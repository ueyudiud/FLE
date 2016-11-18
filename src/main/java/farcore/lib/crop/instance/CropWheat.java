package farcore.lib.crop.instance;

import java.util.ArrayList;

import farcore.lib.crop.CropAbstract;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CropWheat extends CropAbstract
{
	public CropWheat(Mat material)
	{
		super(material);
		dnaHelper = new CropDNAHelper();
		maxStage = 8;
		growReq = 800;
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		super.getDrops(access, list);
		if(access.stage() > 6)
		{
			CropInfo info = access.info();
			int grain = info.grain;
			if(grain > 0)
			{
				list.add(new ItemStack(Items.WHEAT));
			}
		}
	}
}