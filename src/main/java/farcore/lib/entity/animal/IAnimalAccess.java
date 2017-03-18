/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.entity.animal;

import farcore.lib.bio.IBiology;
import fle.core.entity.animal.EnumGender;
import net.minecraft.entity.Entity;

/**
 * @author ueyudiud
 */
public interface IAnimalAccess<T extends Entity & IAnimalAccess> extends IBiology
{
	boolean doesAnimalAfraidPlayer();
	
	EnumGender getGender();
	
	void setDead();
	
	void setAge(int age);
	
	void addBuf(int buf);
	
	@Override
	IAnimalSpecie<T> getSpecie();
}