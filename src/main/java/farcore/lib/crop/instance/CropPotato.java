package farcore.lib.crop.instance;

import farcore.lib.crop.CropAbstract;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;

public class CropPotato extends CropAbstract
{
	public CropPotato(Mat material)
	{
		super(material);
		dnaHelper = new CropDNAHelper();
		maxStage = 7;
		growReq = 1300;
	}
}