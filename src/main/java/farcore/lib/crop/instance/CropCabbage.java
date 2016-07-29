package farcore.lib.crop.instance;

import farcore.lib.crop.CropAbstract;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;

public class CropCabbage extends CropAbstract
{
	public CropCabbage(Mat material)
	{
		super(material);
		dnaHelper = new CropDNAHelper();
		maxStage = 6;
		growReq = 1500;
	}
}