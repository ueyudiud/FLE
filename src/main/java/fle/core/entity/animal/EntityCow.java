/*
 * copyright© 2016-2018 ueyudiud
 */

package fle.core.entity.animal;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.entity.animal.EntityAnimalExt;
import farcore.lib.entity.animal.IAnimalSpecie;
import nebula.common.data.DataSerializers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * @author ueyudiud
 */
public class EntityCow extends EntityAnimalExt
{
	public static final DataParameter<Boolean> IS_WILD = EntityDataManager.createKey(EntityCow.class, DataSerializers.BOOLEAN);
	
	public EntityCow(World worldIn)
	{
		super(worldIn, FamilyOx.INSTANCE);
		setSize(0.9F, 1.4F);
	}
	
	public EntityCow(World worldIn, GeneticMaterial gm)
	{
		super(worldIn, gm);
		setSize(0.9F, 1.4F);
	}
	
	public EntityCow(World worldIn, IAnimalSpecie<?> specie)
	{
		super(worldIn, specie);
		setSize(0.9F, 1.4F);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(IS_WILD, true);
	}
	
	public boolean isDomesticated()
	{
		return this.dataManager.get(IS_WILD);
	}
	
	public void setDomesticated(boolean flag)
	{
		this.dataManager.set(IS_WILD, flag);
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_COW_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound()
	{
		return SoundEvents.ENTITY_COW_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_COW_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
	}
	
	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume()
	{
		return 0.4F;
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootTableList.ENTITIES_COW;
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		return new EntityCow(this.world);
	}
	
	@Override
	public boolean doesAnimalAfraidPlayer()
	{
		return true;
	}
	
	@Override
	public float getEyeHeight()
	{
		return isChild() ? this.height : 1.3F;
	}
}
