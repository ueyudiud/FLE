package farcore.lib.crop.instance;

import farcore.lib.crop.CropAbstract;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;

public class CropRamie extends CropAbstract
{
	public CropRamie(Mat material)
	{
		super(material);
		dnaHelper = new CropDNAHelper();
		maxStage = 7;
		growReq = 1250;
	}
}