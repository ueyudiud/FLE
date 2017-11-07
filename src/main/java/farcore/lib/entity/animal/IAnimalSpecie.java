/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.entity.animal;

import java.util.HashMap;
import java.util.Map;

import farcore.lib.bio.ISpecie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks;

/**
 * @author ueyudiud
 */
public interface IAnimalSpecie<E extends Entity & IAnimalAccess> extends ISpecie<E>
{
	static class AnimalSpecieDictionary
	{
		private static final Map<Class<?>, IAnimalFamily<?>> SPECIE_MAP = new HashMap<>();
	}
	
	static void registerSpecieFamily(Class<? extends Entity> entityClass, IAnimalFamily<?> family)
	{
		AnimalSpecieDictionary.SPECIE_MAP.put(entityClass, family);
	}
	
	static <E extends Entity & IAnimalAccess> IAnimalFamily<E> getFamily(Class<E> entityClass)
	{
		return (IAnimalFamily<E>) AnimalSpecieDictionary.SPECIE_MAP.get(entityClass);
	}
	
	@Override
	IAnimalFamily<E> getFamily();
	
	int getMaxBuf(E entity);
	
	void addAITasks(E entity, EntityAITasks tasks);
	
	default void onAnimalGrow(E entity, int age)
	{
		entity.setAge(age);
	}
}
