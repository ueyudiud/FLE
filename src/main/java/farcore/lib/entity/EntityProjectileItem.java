package farcore.lib.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import farcore.FarCore;
import farcore.data.KS;
import farcore.lib.item.IProjectileItem;
import farcore.lib.item.IUpdatableItem;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.util.Direction;
import farcore.lib.util.EnviornmentEntity;
import farcore.network.IDescribable;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityProjectileItem extends Entity implements IThrowableEntity, IProjectile, IDescribable
{
	private static final Predicate<Entity> ARROW_TARGETS =
			Predicates.and(new Predicate[] {
					EntitySelectors.NOT_SPECTATING,
					EntitySelectors.IS_ALIVE,
					(Object entity) -> {return ((Entity) entity).canBeCollidedWith();}});
	public boolean inGround = false;
	private int tickInGround;
	private int tickInAir;
	private boolean attacked;
	private IBlockState inBlock;
	private BlockPos blockPos;
	boolean inited = false;
	public int age;
	public ItemStack currentItem;
	public EntityLivingBase shooter;
	private List<EntityPlayer> list = new ArrayList();
	
	public EntityProjectileItem(World worldIn)
	{
		super(worldIn);
		blockPos = BlockPos.ORIGIN;
		setSize(.5F, .5F);
	}
	public EntityProjectileItem(World world, ItemStack stack)
	{
		this(world);
		currentItem = stack;
	}

	public EntityProjectileItem(World world, double x, double y, double z, ItemStack stack)
	{
		this(world, stack);
		setPosition(x, y, z);
	}
	
	public EntityProjectileItem(World world, EntityLivingBase shooter, float hardness, ItemStack stack)
	{
		this(world, shooter.posX, shooter.posY + shooter.getEyeHeight() - .1, shooter.posZ, stack);
		this.shooter = shooter;
		setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		posY -= 0.1;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI);
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI);
		motionY = (-MathHelper.sin(rotationPitch / 180.0F * (float)Math.PI));
		setThrowableHeading(motionX, motionY, motionZ, hardness * 1.5F, 1.0F);
	}
	
	public EntityProjectileItem(World world, EntityLivingBase shooter, float hardness, ItemStack stack, boolean useSkill)
	{
		this(world, shooter.posX, shooter.posY + shooter.getEyeHeight() - .1, shooter.posZ, stack);
		this.shooter = shooter;
		setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI);
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI);
		motionY = (-MathHelper.sin(rotationPitch / 180.0F * (float)Math.PI));
		float inaccuracy;
		if(useSkill && (shooter instanceof EntityPlayer))
		{
			inaccuracy = 6F / (1 + KS.SHOOTING.level((EntityPlayer) shooter));
		}
		else
		{
			inaccuracy = 1F;
		}
		setThrowableHeading(motionX, motionY, motionZ, hardness * 1.5F, inaccuracy);
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
	{
		float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		motionX /= f2;
		motionY /= f2;
		motionZ /= f2;
		motionX += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.0075 * inaccuracy;
		motionY += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.0075 * inaccuracy;
		motionZ += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.0075 * inaccuracy;
		motionX *= velocity;
		motionY *= velocity;
		motionZ *= velocity;
		float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		prevRotationYaw = rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float)(Math.atan2(motionY, f3) * 180.0D / Math.PI);
	}

	@Override
	public Entity getThrower()
	{
		return shooter;
	}

	@Override
	public void setThrower(Entity entity)
	{
		if(entity instanceof EntityLivingBase)
		{
			shooter = (EntityLivingBase) entity;
		}
	}

	@Override
	protected void entityInit()
	{
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z)
	{
		super.setVelocity(x, y, z);
		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(x * x + z * z);
			prevRotationYaw = rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float)(Math.atan2(y, f) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch;
			prevRotationYaw = rotationYaw;
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		tickInAir = compound.getInteger("tickInAir");
		tickInGround = compound.getInteger("tickInGround");
		inGround = compound.getBoolean("inGround");
		currentItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("item"));
		attacked = compound.getBoolean("attacked");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("tickInAir", tickInAir);
		compound.setInteger("tickInGround", tickInGround);
		compound.setTag("item", currentItem.writeToNBT(new NBTTagCompound()));
		compound.setBoolean("attacked", attacked);
		compound.setBoolean("inGround", inGround);
	}
	
	@Override
	public void onUpdate()
	{
		if(worldObj.isRemote && currentItem == null)
		{
			FarCore.network.sendToServer(new PacketEntityAsk(this));
			return;
		}
		++age;
		if(!worldObj.isRemote)
		{
			if(!inited)
			{
				if(currentItem == null)
				{
					setDead();
					return;
				}
				if(currentItem.getItem() instanceof IProjectileItem)
				{
					((IProjectileItem) currentItem.getItem()).initEntity(this);
				}
				if(inGround)
				{
					inBlock = worldObj.getBlockState(blockPos = new BlockPos(this));
					AxisAlignedBB axisAlignedBB = inBlock.getCollisionBoundingBox(worldObj, blockPos);
					if(axisAlignedBB == null || !axisAlignedBB.offset(blockPos).isVecInside(new Vec3d(posX, posY, posZ)))
					{
						inGround = false;
						inBlock = null;
					}
				}
				inited = true;
			}
			synchronized (list)
			{
				if(!list.isEmpty())
				{
					for(EntityPlayer player : list)
					{
						FarCore.network.sendToPlayer(new PacketEntity(this), player);
					}
					list.clear();
				}
			}
		}
		if(currentItem.getItem() instanceof IProjectileItem)
		{
			((IProjectileItem) currentItem.getItem()).onEntityTick(this);
		}
		if(currentItem.getItem() instanceof IUpdatableItem)
		{
			((IUpdatableItem) currentItem.getItem()).updateItem(new EnviornmentEntity(this), currentItem);
		}
		if(isDead)
			return;
		super.onEntityUpdate();
		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
		{
			recountYawAndPitch();
		}
		blockPos = new BlockPos(this);

		if(inGround)
		{
			IBlockState state = worldObj.getBlockState(blockPos);
			if(state != inBlock)
			{
				tickInAir = 0;
				tickInGround = 0;
				motionX *= rand.nextFloat() * 0.2F;
				motionY *= rand.nextFloat() * 0.2F;
				motionZ *= rand.nextFloat() * 0.2F;
				inGround = false;
			}
			else
			{
				++tickInGround;
				if(tickInGround >= 72000)
				{
					setDead();
					return;
				}
			}
		}
		else
		{
			tickInGround = 0;
			++tickInAir;
			if(tickInAir >= 20)
			{
				attacked = false;
			}
			if(!worldObj.isRemote)
			{
				if(posY <= -32 || tickInAir >= 3600)
				{
					setDead();
					return;
				}
			}
			Vec3d vec3d1 = new Vec3d(posX, posY, posZ);
			Vec3d vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
			RayTraceResult raytraceresult = worldObj.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			vec3d1 = new Vec3d(posX, posY, posZ);
			vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
			if (raytraceresult != null)
			{
				vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
			}
			if(!attacked)
			{
				Entity entity = findEntityOnPath(vec3d1, vec3d);
				if (entity != null)
				{
					raytraceresult = new RayTraceResult(entity);
				}
				if (raytraceresult != null && raytraceresult.entityHit != null && raytraceresult.entityHit instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;
					if (shooter instanceof EntityPlayer && !((EntityPlayer) shooter).canAttackPlayer(entityplayer))
					{
						raytraceresult = null;
					}
				}
				if (raytraceresult != null)
				{
					onHit(raytraceresult);
				}
			}
		}
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		if(!inGround)
		{
			float f4 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));
			for (rotationPitch = (float)(MathHelper.atan2(motionY, f4) * (180D / Math.PI)); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F)
			{
				;
			}
			while (rotationPitch - prevRotationPitch >= 180.0F)
			{
				prevRotationPitch += 360.0F;
			}
			while (rotationYaw - prevRotationYaw < -180.0F)
			{
				prevRotationYaw -= 360.0F;
			}
			while (rotationYaw - prevRotationYaw >= 180.0F)
			{
				prevRotationYaw += 360.0F;
			}
			rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
			float f1 = 0.99F;
			float f2 = 0.05F;
			
			if (isInWater())
			{
				for (int i = 0; i < 4; ++i)
				{
					float f3 = 0.25F;
					worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25D, posY - motionY * 0.25D, posZ - motionZ * 0.25D, motionX, motionY, motionZ, new int[0]);
				}
				
				f1 = 0.6F;
			}
			if (isWet())
			{
				extinguish();
			}
			
			motionX *= f1;
			motionY *= f1;
			motionZ *= f1;
			if (!func_189652_ae())
			{
				motionY -= 0.05;
			}
			setPosition(posX, posY, posZ);
		}
		doBlockCollisions();
	}
	
	private void recountYawAndPitch()
	{
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		prevRotationYaw = rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float)(Math.atan2(motionY, f) * 180.0D / Math.PI);
	}
	
	private void onHit(RayTraceResult result)
	{
		Entity entity = result.entityHit;
		IBlockState state = null;
		if (entity != null)
		{
			hitOnEntity(entity);
		}
		else
		{
			blockPos = result.getBlockPos();
			state = worldObj.getBlockState(blockPos);
			if(!state.getBlock().isAir(state, worldObj, blockPos))
			{
				AxisAlignedBB aabb = state.getCollisionBoundingBox(worldObj, blockPos).offset(blockPos);
				state.getBlock().onEntityCollidedWithBlock(worldObj, blockPos, state, this);
				hitOnGround(aabb);
			}
		}
		if(!isDead)
		{
			posX = result.hitVec.xCoord + motionX * 4E-2F;
			posY = result.hitVec.yCoord + motionY * 4E-2F;
			posZ = result.hitVec.zCoord + motionZ * 4E-2F;
			if(result.typeOfHit == Type.BLOCK)
			{
				motionX = 0;
				motionY = 0;
				motionZ = 0;
				inBlock = state;
				inGround = true;
			}
		}
	}

	private void hitOnGround(AxisAlignedBB aabb)
	{
		Direction direction = U.Worlds.getCollideSide(aabb, new double[]{posX, posY, posZ}, new double[]{posX + motionX, posY + motionY, posZ + motionZ});
		if(direction == null)
		{
			direction = Direction.Q;
		}
		if(currentItem.getItem() instanceof IProjectileItem)
			if(((IProjectileItem) currentItem.getItem()).onHitGround(worldObj, blockPos, this, direction))
				return;
		if(!worldObj.isRemote)
		{
			EntityItem entity = new EntityItem(worldObj, posX, posY, posZ, currentItem);
			entity.motionX = direction.boundX * motionX * .1 + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
			entity.motionY = direction.boundY * motionY * .1 + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08 + 0.2;
			entity.motionZ = direction.boundZ * motionZ * .1 + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
			worldObj.spawnEntityInWorld(entity);
		}
		setDead();
	}
	
	private void hitOnEntity(Entity entity)
	{
		if(currentItem.getItem() instanceof IProjectileItem)
			if(((IProjectileItem) currentItem.getItem()).onHitEntity(worldObj, entity, this))
			{
				attacked = true;
				return;
			}
		entity.addVelocity(motionX * .1, motionY * .1 + 0.1, motionZ * .1);
		if(!worldObj.isRemote)
		{
			EntityItem entity1 = new EntityItem(worldObj, posX - motionX, posY - motionY, posZ - motionZ, currentItem);
			entity1.motionX = motionX * .1 + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
			entity1.motionY = motionY * .1 + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08 + 0.2;
			entity1.motionZ = motionZ * .1 + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
			worldObj.spawnEntityInWorld(entity1);
		}
		setDead();
	}

	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end)
	{
		Entity entity = null;
		List<Entity> list = worldObj.getEntitiesInAABBexcluding(this, getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expandXyz(1.0D), ARROW_TARGETS);
		double d0 = 0.0D;
		
		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity1 = list.get(i);
			
			if (entity1 != shooter || ticksExisted >= 5)
			{
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);
				
				if (raytraceresult != null)
				{
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);
					
					if (d1 < d0 || d0 == 0.0D)
					{
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}
		
		return entity;
	}

	@Override
	public NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("p", blockPos.toLong());
		nbt.setTag("i", currentItem.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public void readDescriptionsFromNBT(NBTTagCompound nbt)
	{
		blockPos = BlockPos.fromLong(nbt.getLong("p"));
		currentItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("i"));
	}

	@Override
	public void markNBTSync(EntityPlayer player)
	{
		synchronized (list)
		{
			list.add(player);
		}
	}
}