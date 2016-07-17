package farcore.lib.crop.dna;

import farcore.lib.bio.DNAProp;
import farcore.lib.collection.Stack;
import farcore.util.U;

public class CropDNAProp extends DNAProp<CropCharacter>
{
	public CropDNAProp(Stack<CropCharacter>...characters)
	{
		super(characters);
	}

	@Override
	protected CropCharacter[] createCharacters(int length)
	{
		return new CropCharacter[length];
	}
}