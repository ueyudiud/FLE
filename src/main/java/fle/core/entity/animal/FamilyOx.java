/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.entity.animal;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.entity.animal.IAnimalFamily;
import farcore.lib.entity.animal.IAnimalSpecie;

/**
 * @author ueyudiud
 */
public class FamilyOx extends IAnimalFamily.Impl<EntityCow>
{
	public static final FamilyOx INSTANCE = new FamilyOx();
	
	private SpecieAuroch auroch = new SpecieAuroch();
	
	public FamilyOx()
	{
		super("fle.ox");
	}
	
	@Override
	public IAnimalSpecie<EntityCow> getSpecieFromGM(GeneticMaterial gm)
	{
		return this.auroch;
	}
	
	@Override
	public Collection<? extends IAnimalSpecie<EntityCow>> getSpecies()
	{
		return ImmutableList.of(this.auroch);
	}
}
