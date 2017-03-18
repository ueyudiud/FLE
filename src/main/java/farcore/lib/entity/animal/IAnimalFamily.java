/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.entity.animal;

import java.lang.reflect.Constructor;
import java.util.Collection;

import com.google.common.reflect.TypeToken;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IFamily;
import nebula.common.util.R;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public interface IAnimalFamily<E extends Entity & IAnimalAccess> extends IFamily<E>
{
	@Override
	IAnimalSpecie<E> getSpecieFromGM(GeneticMaterial gm);
	
	@Override
	Collection<? extends IAnimalSpecie<E>> getSpecies();
	
	E createNativeAnimal(World world, IAnimalSpecie specie);
	
	E createChildAnimal(World world, GeneticMaterial gm);
	
	abstract class Impl<E extends Entity & IAnimalAccess> implements IAnimalFamily<E>
	{
		protected final String name;
		private Constructor<?> nativeAnimalConstructor, childAnimalConstructor;
		
		public Impl(String name)
		{
			this.name = name;
			TypeToken<E> typeToken = new TypeToken<E>(getClass()){};
			Class<? super E> entityClass = typeToken.getRawType();
			try
			{
				nativeAnimalConstructor = entityClass.getConstructor(World.class, IAnimalSpecie.class);
				childAnimalConstructor = entityClass.getConstructor(World.class, GeneticMaterial.class);
			}
			catch (NoSuchMethodException | SecurityException exception)
			{
				throw new RuntimeException("Can not register eneity type of " + name + " with class :" + entityClass.getName() + ".", exception);
			}
			IAnimalSpecie.registerSpecieFamily((Class<? extends Entity>) entityClass, this);
		}
		
		@Override
		public final String getRegisteredName()
		{
			return name;
		}
		
		@Override
		public E createNativeAnimal(World world, IAnimalSpecie specie)
		{
			return R.<E>newInstance(nativeAnimalConstructor, world, specie);
		}
		
		@Override
		public E createChildAnimal(World world, GeneticMaterial gm)
		{
			return R.<E>newInstance(childAnimalConstructor, world, gm);
		}
	}
}