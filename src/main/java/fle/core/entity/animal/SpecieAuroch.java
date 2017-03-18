/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.animal;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.entity.animal.IAnimalSpecie;
import fle.core.entity.ai.EntityAIAvoidPlayerWhenHitIt;
import nebula.common.data.Misc;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

/**
 * @author ueyudiud
 */
public class SpecieAuroch implements IAnimalSpecie<EntityCow>
{
	@Override
	public GeneticMaterial createNativeGeneticMaterial()
	{
		return new GeneticMaterial(getFamily().getRegisteredName(), 0, Misc.LONGS_EMPTY, Misc.INTS_EMPTY);
	}
	
	@Override
	public GeneticMaterial createGameteGeneticMaterial(EntityCow biology, GeneticMaterial gm)
	{
		return new GeneticMaterial(getFamily().getRegisteredName(), gm.generation, Misc.LONGS_EMPTY, Misc.INTS_EMPTY);
	}
	
	@Override
	public void expressTrait(EntityCow biology, GeneticMaterial gm)
	{
		//TODO
	}
	
	@Override
	public String getRegisteredName()
	{
		return "fle.ox.auroch";
	}
	
	@Override
	public FamilyOx getFamily()
	{
		return FamilyOx.INSTANCE;
	}
	
	@Override
	public void addAITasks(EntityCow entity, EntityAITasks tasks)
	{
		tasks.addTask(0, new EntityAISwimming(entity));
		tasks.addTask(1, new EntityAIPanic(entity, 2.0D));
		tasks.addTask(2, new EntityAIAvoidPlayerWhenHitIt(entity, 1.5, 2.2, 15.0F, 7.0F));
		tasks.addTask(3, new EntityAIMate(entity, 1.0D));
		tasks.addTask(4, new EntityAITempt(entity, 1.25D, Items.WHEAT, false));
		tasks.addTask(5, new EntityAIFollowParent(entity, 1.25D));
		tasks.addTask(6, new EntityAIWander(entity, 1.0D));
		tasks.addTask(7, new EntityAIWatchClosest(entity, EntityPlayer.class, 20.0F, 0.005F));
		tasks.addTask(8, new EntityAILookIdle(entity));
	}
	
	@Override
	public int getMaxBuf(EntityCow entity)
	{
		return 10000;
	}
}