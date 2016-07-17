package farcore.lib.tree.dna;

import farcore.lib.bio.DNAHelper;
import farcore.lib.tree.TreeInfo;

public class TreeDNAHelper extends DNAHelper<TreeInfo, TreeDNAProp>
{
	public TreeDNAHelper(TreeDNAProp...props)
	{
		super(props);
	}
	
	@Override
	protected void errorOn(TreeInfo target, int type)
	{
		target.map.put("error", type);
	}
}