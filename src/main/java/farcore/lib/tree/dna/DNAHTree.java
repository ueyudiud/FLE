package farcore.lib.tree.dna;

import farcore.lib.bio.DNAHandler;
import farcore.lib.bio.DNAPair;
import farcore.lib.tree.TreeInfo;

public class DNAHTree extends DNAHandler<TreeInfo>
{
	public DNAHTree(int id, String name)
	{
		super(id, name);
	}
	public DNAHTree(String name)
	{
		super(name);
	}
	
	@Override
	public void expressTrait(TreeInfo target, DNAPair<TreeInfo> pair)
	{
		super.expressTrait(target, pair);
	}
}