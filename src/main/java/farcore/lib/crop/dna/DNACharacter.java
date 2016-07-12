package farcore.lib.crop.dna;

import farcore.lib.crop.CropInfo;

public class DNACharacter
{
	public char chr;

	public int grain;
	public int growth;
	public int coldResistance;
	public int hotResistance;
	public int weedResistance;
	public int dryResistance;
	public String prop;
	public int level;
	
	public void affectOn(CropInfo info)
	{
		info.grain += grain;
		info.growth += growth;
		info.coldResistance += coldResistance;
		info.hotResistance += hotResistance;
		info.weedResistance += weedResistance;
		info.dryResistance += dryResistance;
		if(prop != null)
		{
			info.map.put(prop, level);
		}
	}
}