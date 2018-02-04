/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.entity.animal;

import java.util.Collection;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.GeneticMaterial.GenticMaterialFactory;
import farcore.lib.bio.IFamily;
import fle.core.entity.animal.EnumGender;
import nebula.common.data.DataSerializers;
import nebula.common.util.Entities;
import nebula.common.util.L;
import nebula.common.util.NBTs;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public abstract class EntityAnimalExt extends EntityAnimal implements IAnimalAccess, IAnimalFamily<EntityAnimalExt>
{
	public static final DataParameter<EnumGender>	GENDER	= Entities.createKey(EntityAnimalExt.class, EnumGender.class);
	public static final DataParameter<Short>		AGE		= EntityDataManager.createKey(EntityAnimalExt.class, DataSerializers.SHORT);
	
	// Fake final value.
	protected IAnimalFamily<? super EntityAnimalExt>	family;
	protected IAnimalSpecie<? super EntityAnimalExt>	specie;
	
	protected EnumGender		gender	= hasGender() ? L.random(EnumGender.SEXUAL, getRNG()) : EnumGender.UNKNOWN;
	protected int				growBuf;
	protected int				growAge;
	protected GeneticMaterial	gm;
	
	public EntityAnimalExt(World world, IAnimalFamily<?> family)
	{
		super(world);
		this.family = (IAnimalFamily<? super EntityAnimalExt>) family;
		this.specie = (IAnimalSpecie<? super EntityAnimalExt>) family.getSpecies().iterator().next();
	}
	
	public EntityAnimalExt(World worldIn, IAnimalSpecie<?> specie)
	{
		super(worldIn);
		initEntitySpecie(specie);
	}
	
	public EntityAnimalExt(World worldIn, GeneticMaterial gm)
	{
		super(worldIn);
		initEntitySpecie(gm);
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.family.getRegisteredName();
	}
	
	@Override
	public final IAnimalSpecie getSpecieFromGM(GeneticMaterial gm)
	{
		return this.family.getSpecieFromGM(gm);
	}
	
	@Override
	public final Collection<? extends IAnimalSpecie<EntityAnimalExt>> getSpecies()
	{
		return (Collection) this.family.getSpecies();
	}
	
	@Override
	public final EntityAnimalExt createNativeAnimal(World world, IAnimalSpecie specie)
	{
		return (EntityAnimalExt) this.family.createNativeAnimal(world, specie);
	}
	
	@Override
	public final EntityAnimalExt createChildAnimal(World world, GeneticMaterial gm)
	{
		return (EntityAnimalExt) this.family.createChildAnimal(world, gm);
	}
	
	protected boolean hasGender()
	{
		return true;
	}
	
	@Override
	public final EnumGender getGender()
	{
		return this.gender;
	}
	
	@Override
	public final GeneticMaterial getGeneticMaterial()
	{
		return this.gm;
	}
	
	@Override
	public final IFamily getFamily()
	{
		return this.family;
	}
	
	@Override
	public final IAnimalSpecie getSpecie()
	{
		return this.specie;
	}
	
	public void resetGeneticMaterial(GeneticMaterial gm)
	{
		assert this.family != null;
		this.specie = this.family.getSpecieFromGM(this.gm = gm);
	}
	
	@Override
	public void setAge(int age)
	{
		this.growAge = age;
	}
	
	@Override
	public void addBuf(int buf)
	{
		this.growBuf = buf;
		if (this.growBuf >= this.specie.getMaxBuf(this))
		{
			this.specie.onAnimalGrow(this, this.growAge + 1);
		}
	}
	
	protected void initEntitySpecie(GeneticMaterial gm)
	{
		this.family = (IAnimalFamily) IAnimalSpecie.getFamily(getClass());
		this.specie = this.family.getSpecieFromGM(gm);
		this.gm = gm;
	}
	
	protected void initEntitySpecie(IAnimalSpecie<?> specie)
	{
		this.family = (IAnimalFamily) specie.getFamily();
		this.specie = (IAnimalSpecie) specie;
		this.gm = specie.createNativeGeneticMaterial();
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(GENDER, this.gender);
		this.dataManager.register(AGE, (short) this.growAge);
	}
	
	@Override
	protected void initEntityAI()
	{
		this.specie.addAITasks(this, this.tasks);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		NBTs.setEnum(compound, "Gender", this.gender);
		NBTs.setNumber(compound, "GrowAge", this.growAge);
		NBTs.setNumber(compound, "GrowBuf", this.growBuf);
		NBTs.setObj(compound, "GeneticMaterial", this.gm, GenticMaterialFactory.INSTANCE);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.gender = NBTs.getEnumOrDefault(compound, "Gender", EnumGender.UNKNOWN);
		this.growAge = NBTs.getIntOrDefault(compound, "GrowAge", this.growAge);
		this.growBuf = NBTs.getIntOrDefault(compound, "GrowBuf", this.growBuf);
		resetGeneticMaterial(NBTs.getObj(compound, "GeneticMaterial", GenticMaterialFactory.INSTANCE));
	}
}
