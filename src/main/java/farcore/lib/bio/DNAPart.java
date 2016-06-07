package farcore.lib.bio;

import farcore.enums.EnumCharacter;

public class DNAPart
{
	public DNACol col;
	public final EnumCharacter character;
	public final char key;
	
	public DNAPart(char key, EnumCharacter character)
	{
		this.key = key;
		this.character = character;
	}
	
	public void setCollection(DNACol col)
	{
		this.col = col;
	}
}