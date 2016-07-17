package farcore.lib.crop.dna;

import java.util.LinkedList;
import java.util.List;

import farcore.lib.bio.DNAHelper;
import farcore.lib.crop.CropInfo;
import farcore.util.U;

public class CropDNAHelper extends DNAHelper<CropInfo, CropDNAProp>
{
	public CropDNAHelper(CropDNAProp...props)
	{
		super(props);
	}
	
	@Override
	protected void errorOn(CropInfo target, int type)
	{
		target.map.put("error", type);
	}
}