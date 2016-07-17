package farcore.lib.tree.dna;

import farcore.lib.bio.DNAProp;
import farcore.lib.collection.Stack;

public class TreeDNAProp extends DNAProp<TreeCharacter>
{
	public TreeDNAProp(Stack<TreeCharacter>...characters)
	{
		super(characters);
	}
	
	@Override
	protected TreeCharacter[] createCharacters(int length)
	{
		return new TreeCharacter[length];
	}	
}