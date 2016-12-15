/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.bio;

import java.util.Random;

import javax.annotation.Nullable;

import farcore.lib.collection.Register;
import farcore.util.L;

/**
 * @author ueyudiud
 */
public class DNAHandler<T extends IBiology>
{
	public static final Register<DNAHandler> REGISTER = new Register();
	
	public static DNAPair[] createNativeDNAs(DNAHandler[] handlers)
	{
		return handlers == null ? new DNAPair[0] : L.transform(handlers, DNAPair.class, handler -> handler.createNative(null));
	}
	
	public DNAHandler(String name)
	{
		REGISTER.register(name, this);
	}
	public DNAHandler(int id, String name)
	{
		REGISTER.register(id, name, this);
	}
	
	public void expressTrait(T target, DNAPair<T> pair)
	{
		
	}
	
	public DNAPair<T> createNative(@Nullable T target)
	{
		return createNative(target, IBiology.random(target));
	}
	
	protected DNAPair<T> createNative(@Nullable T target, Random random)
	{
		throw new RuntimeException("THIS IS BUG! Check your mod if there are some mod forget override this method. Or report this bug to modder.");
	}
	
	public DNAPair<T> createGamete(T target, DNAPair<T> pair, boolean mutate)
	{
		return new DNAPair(this, pair.randSelect(getRandomFromTarget(target)));
	}
	
	protected Random getRandomFromTarget(T target)
	{
		return L.random();
	}
	
	public DNAPair<T> mixGamete(Random random, DNAPair<T> pair1, DNAPair<T> pair2)
	{
		return new DNAPair(this, pair1.randSelect(random), pair2.randSelect(random));
	}
}