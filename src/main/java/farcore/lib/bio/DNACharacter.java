package farcore.lib.bio;

public abstract class DNACharacter<T>
{
	public final char chr;
	
	public DNACharacter(char character)
	{
		this.chr = character;
	}

	public abstract void affectOn(T info);
}