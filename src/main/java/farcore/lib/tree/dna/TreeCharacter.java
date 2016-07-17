package farcore.lib.tree.dna;

import farcore.lib.bio.DNACharacter;
import farcore.lib.tree.TreeInfo;

public class TreeCharacter extends DNACharacter<TreeInfo>
{
	public int height;
	public int growth;
	public int coldResistance;
	public int hotResistance;
	public int dryResistance;

	public String prop;
	public int level;

	public TreeCharacter(char character)
	{
		super(character);
	}

	@Override
	public void affectOn(TreeInfo info)
	{
		info.height += height;
		info.growth += growth;
		info.coldResistance += coldResistance;
		info.hotResistance += hotResistance;
		info.dryResistance += dryResistance;
		if(prop != null)
		{
			info.map.put(prop, level);
		}
	}
}