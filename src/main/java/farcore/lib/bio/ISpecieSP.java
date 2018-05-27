/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Random;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
public interface ISpecieSP extends ISpecie
{
	@Nullable
	BioData getGamete(BioData data, Random rand);
}
