package nebula.common.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import nebula.Nebula;
import nebula.common.enviornment.EnviornmentEntity;
import nebula.common.item.IProjectileItem;
import nebula.common.item.IUpdatableItem;
import nebula.common.network.packet.PacketEntity;
import nebula.common.network.packet.PacketEntityAsk;
import nebula.common.util.Direction;
import nebula.common.util.Worlds;
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

/**
 * Some bug still contain, it is need to be solved.
 * 
 * @author ueyudiud FIXME
 */
public class EntityProjectileItem extends Entity implements IThrowableEntity, IProjectile, IDescribable
{
	private static final Predicate<Entity>	ARROW_TARGETS	= Predicates.<Entity> and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith);
	public boolean							inGround		= false;
	private int								tickInGround;
	private int								tickInAir;
	private boolean							attacked;
	private IBlockState						inBlock;
	private BlockPos						blockPos;
	boolean									inited			= false;
	public int								age;
	public ItemStack						currentItem;
	public EntityLivingBase					shooter;
	private List<EntityPlayer>				list			= new ArrayList<>();
	
	public EntityProjectileItem(World worldIn)
	{
		super(worldIn);
		this.blockPos = BlockPos.ORIGIN;
		setSize(.5F, .5F);
	}
	
	public EntityProjectileItem(World world, ItemStack stack)
	{
		this(world);
		this.currentItem = stack;
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
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.posY -= 0.1;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		setPosition(this.posX, this.posY, this.posZ);
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		setThrowableHeading(this.motionX, this.motionY, this.motionZ, hardness * 1.5F, 1.0F);
	}
	
	public EntityProjectileItem(World world, EntityLivingBase shooter, float hardness, ItemStack stack, float inaccuracy)
	{
		this(world, shooter.posX, shooter.posY + shooter.getEyeHeight() - .1, shooter.posZ, stack);
		this.shooter = shooter;
		setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.posY -= 0.1;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		setPosition(this.posX, this.posY, this.posZ);
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
		this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		setThrowableHeading(this.motionX, this.motionY, this.motionZ, hardness * 1.5F, inaccuracy);
	}
	
	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
	{
		float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.motionX /= f2;
		this.motionY /= f2;
		this.motionZ /= f2;
		this.motionX += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.0075 * inaccuracy;
		this.motionY += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.0075 * inaccuracy;
		this.motionZ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.0075 * inaccuracy;
		this.motionX *= velocity;
		this.motionY *= velocity;
		this.motionZ *= velocity;
		float f3 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f3) * 180.0D / Math.PI);
	}
	
	@Override
	public Entity getThrower()
	{
		return this.shooter;
	}
	
	@Override
	public void setThrower(Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			this.shooter = (EntityLivingBase) entity;
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
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt(x * x + z * z);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, f) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		}
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.tickInAir = compound.getInteger("tickInAir");
		this.tickInGround = compound.getInteger("tickInGround");
		this.inGround = compound.getBoolean("inGround");
		this.currentItem = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("item"));
		this.attacked = compound.getBoolean("attacked");
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("tickInAir", this.tickInAir);
		compound.setInteger("tickInGround", this.tickInGround);
		compound.setTag("item", this.currentItem.writeToNBT(new NBTTagCompound()));
		compound.setBoolean("attacked", this.attacked);
		compound.setBoolean("inGround", this.inGround);
	}
	
	@Override
	public void onUpdate()
	{
		if (this.world.isRemote && this.currentItem == null)
		{
			Nebula.network.sendToServer(new PacketEntityAsk(this));
			return;
		}
		++this.age;
		if (!this.world.isRemote)
		{
			if (!this.inited)
			{
				if (this.currentItem == null)
				{
					setDead();
					return;
				}
				if (this.currentItem.getItem() instanceof IProjectileItem)
				{
					((IProjectileItem) this.currentItem.getItem()).initEntity(this);
				}
				if (this.inGround)
				{
					this.inBlock = this.world.getBlockState(this.blockPos = new BlockPos(this));
					AxisAlignedBB axisAlignedBB = this.inBlock.getCollisionBoundingBox(this.world, this.blockPos);
					if (axisAlignedBB == null || !axisAlignedBB.offset(this.blockPos).isVecInside(new Vec3d(this.posX, this.posY, this.posZ)))
					{
						this.inGround = false;
						this.inBlock = null;
					}
				}
				this.inited = true;
			}
			synchronized (this.list)
			{
				if (!this.list.isEmpty())
				{
					for (EntityPlayer player : this.list)
					{
						Nebula.network.sendToPlayer(new PacketEntity(this), player);
					}
					this.list.clear();
				}
			}
		}
		if (this.currentItem.getItem() instanceof IProjectileItem)
		{
			((IProjectileItem) this.currentItem.getItem()).onEntityTick(this);
		}
		if (this.currentItem.getItem() instanceof IUpdatableItem)
		{
			((IUpdatableItem) this.currentItem.getItem()).updateItem(new EnviornmentEntity(this), this.currentItem);
		}
		if (this.isDead) return;
		super.onEntityUpdate();
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			recountYawAndPitch();
		}
		this.blockPos = new BlockPos(this);
		
		if (this.inGround)
		{
			IBlockState state = this.world.getBlockState(this.blockPos);
			if (state != this.inBlock)
			{
				this.tickInAir = 0;
				this.tickInGround = 0;
				this.motionX *= this.rand.nextFloat() * 0.2F;
				this.motionY *= this.rand.nextFloat() * 0.2F;
				this.motionZ *= this.rand.nextFloat() * 0.2F;
				this.inGround = false;
			}
			else
			{
				++this.tickInGround;
				if (this.tickInGround >= 72000)
				{
					setDead();
					return;
				}
			}
		}
		else
		{
			this.tickInGround = 0;
			++this.tickInAir;
			if (this.tickInAir >= 20)
			{
				this.attacked = false;
			}
			if (!this.world.isRemote)
			{
				if (this.posY <= -32 || this.tickInAir >= 3600)
				{
					setDead();
					return;
				}
			}
			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if (raytraceresult != null)
			{
				vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
			}
			if (!this.attacked)
			{
				Entity entity = findEntityOnPath(vec3d1, vec3d);
				if (entity != null)
				{
					raytraceresult = new RayTraceResult(entity);
				}
				if (raytraceresult != null && raytraceresult.entityHit != null && raytraceresult.entityHit instanceof EntityPlayer)
				{
					EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;
					if (this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer))
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
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		if (!this.inGround)
		{
			float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f4) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			{
				;
			}
			while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
			{
				this.prevRotationPitch += 360.0F;
			}
			while (this.rotationYaw - this.prevRotationYaw < -180.0F)
			{
				this.prevRotationYaw -= 360.0F;
			}
			while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
			{
				this.prevRotationYaw += 360.0F;
			}
			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f1 = 0.99F;
			
			if (isInWater())
			{
				for (int i = 0; i < 4; ++i)
				{
					this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
				}
				
				f1 = 0.6F;
			}
			if (isWet())
			{
				extinguish();
			}
			
			this.motionX *= f1;
			this.motionY *= f1;
			this.motionZ *= f1;
			if (!hasNoGravity())
			{
				this.motionY -= 0.05;
			}
			setPosition(this.posX, this.posY, this.posZ);
		}
		doBlockCollisions();
	}
	
	private void recountYawAndPitch()
	{
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI);
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
			this.blockPos = result.getBlockPos();
			state = this.world.getBlockState(this.blockPos);
			if (!state.getBlock().isAir(state, this.world, this.blockPos))
			{
				AxisAlignedBB aabb = state.getCollisionBoundingBox(this.world, this.blockPos).offset(this.blockPos);
				state.getBlock().onEntityCollidedWithBlock(this.world, this.blockPos, state, this);
				hitOnGround(aabb);
			}
		}
		if (!this.isDead)
		{
			this.posX = result.hitVec.xCoord + this.motionX * 4E-2F;
			this.posY = result.hitVec.yCoord + this.motionY * 4E-2F;
			this.posZ = result.hitVec.zCoord + this.motionZ * 4E-2F;
			if (result.typeOfHit == Type.BLOCK)
			{
				this.motionX = 0;
				this.motionY = 0;
				this.motionZ = 0;
				this.inBlock = state;
				this.inGround = true;
			}
		}
	}
	
	private void hitOnGround(AxisAlignedBB aabb)
	{
		Direction direction = Worlds.getCollideSide(aabb, new double[] { this.posX, this.posY, this.posZ }, new double[] { this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ });
		if (direction == null)
		{
			direction = Direction.Q;
		}
		if (this.currentItem.getItem() instanceof IProjectileItem) if (((IProjectileItem) this.currentItem.getItem()).onHitGround(this.world, this.blockPos, this, direction)) return;
		if (!this.world.isRemote)
		{
			EntityItem entity = new EntityItem(this.world, this.posX, this.posY, this.posZ, this.currentItem);
			entity.motionX = direction.boundX * this.motionX * .1 + this.rand.nextDouble() * 0.08 - this.rand.nextDouble() * 0.08;
			entity.motionY = direction.boundY * this.motionY * .1 + this.rand.nextDouble() * 0.08 - this.rand.nextDouble() * 0.08 + 0.2;
			entity.motionZ = direction.boundZ * this.motionZ * .1 + this.rand.nextDouble() * 0.08 - this.rand.nextDouble() * 0.08;
			this.world.spawnEntity(entity);
		}
		setDead();
	}
	
	private void hitOnEntity(Entity entity)
	{
		if (this.currentItem.getItem() instanceof IProjectileItem) if (((IProjectileItem) this.currentItem.getItem()).onHitEntity(this.world, entity, this))
		{
			this.attacked = true;
			return;
		}
		entity.addVelocity(this.motionX * .1, this.motionY * .1 + 0.1, this.motionZ * .1);
		if (!this.world.isRemote)
		{
			EntityItem entity1 = new EntityItem(this.world, this.posX - this.motionX, this.posY - this.motionY, this.posZ - this.motionZ, this.currentItem);
			entity1.motionX = this.motionX * .1 + this.rand.nextDouble() * 0.08 - this.rand.nextDouble() * 0.08;
			entity1.motionY = this.motionY * .1 + this.rand.nextDouble() * 0.08 - this.rand.nextDouble() * 0.08 + 0.2;
			entity1.motionZ = this.motionZ * .1 + this.rand.nextDouble() * 0.08 - this.rand.nextDouble() * 0.08;
			this.world.spawnEntity(entity1);
		}
		setDead();
	}
	
	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end)
	{
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D), ARROW_TARGETS);
		double d0 = 0.0D;
		
		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity1 = list.get(i);
			
			if (entity1 != this.shooter || this.ticksExisted >= 5)
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
		nbt.setLong("p", this.blockPos.toLong());
		nbt.setTag("i", this.currentItem.writeToNBT(new NBTTagCompound()));
		return nbt;
	}
	
	@Override
	public void readDescriptionsFromNBT(NBTTagCompound nbt)
	{
		this.blockPos = BlockPos.fromLong(nbt.getLong("p"));
		this.currentItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("i"));
	}
	
	@Override
	public void markNBTSync(EntityPlayer player)
	{
		synchronized (this.list)
		{
			this.list.add(player);
		}
	}
}
