package farcore.lib.crop.instance;

import farcore.lib.crop.CropAbstract;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;

public class CropMillet extends CropAbstract
{
	public CropMillet(Mat material)
	{
		super(material);
		dnaHelper = new CropDNAHelper();
		maxStage = 8;
		growReq = 900;
	}
}