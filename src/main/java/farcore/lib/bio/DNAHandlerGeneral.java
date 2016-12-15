/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.bio;

import java.util.Random;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
public final class DNAHandlerGeneral extends DNAHandler<IBiology>
{
	private char dominant;
	private char implicit;
	
	public DNAHandlerGeneral(int id, char chr)
	{
		super(id, "general" + chr);
		this.dominant = Character.toUpperCase(chr);
		this.implicit = Character.toLowerCase(chr);
	}
	
	@Override
	public DNAPair<IBiology> createGamete(IBiology target, DNAPair<IBiology> pair, boolean mutate)
	{
		return new DNAPair(this, pair.randSelect(target.rng()));
	}
	
	@Override
	protected DNAPair<IBiology> createNative(@Nullable IBiology target, Random random)
	{
		return new DNAPair(this, random.nextBoolean() ? this.dominant : this.implicit, random.nextBoolean() ? this.dominant : this.implicit);
	}
}