package farcore.lib.entity;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.lib.item.IProjectileItem;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.network.IDescribable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityProjectileItem extends Entity implements IProjectile, IDescribable
{
	private int[] blockPos = new int[3];

	boolean inited = false;
	public ItemStack currentItem;
	protected EntityLivingBase shooter;

	private List<EntityPlayer> list = new ArrayList(4);

	public EntityProjectileItem(World world, ItemStack stack)
	{
		super(world);
		currentItem = stack;
		renderDistanceWeight = 10F;
		blockPos[1] = -1;
		setSize(.5F, .5F);
	}

	public EntityProjectileItem(World world, double x, double y, double z, ItemStack stack)
	{
		this(world, stack);
		setPosition(x, y, z);
		yOffset = 0;
	}

	public EntityProjectileItem(World world, EntityLivingBase shooter, float hardness, ItemStack stack)
	{
		this(world, shooter.posX, shooter.posY + shooter.getEyeHeight() - .1, shooter.posZ, stack);
		this.shooter = shooter;
		setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI);
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float)Math.PI);
		motionY = (-MathHelper.sin(rotationPitch / 180.0F * (float)Math.PI));
		setThrowableHeading(motionX, motionY, motionZ, hardness * 1.5F, 1.0F);
	}

	@Override
	public void setThrowableHeading(double motionX, double motionY, double motionZ, float hardness,
			float shake)
	{
		float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		motionX /= f2;
		motionY /= f2;
		motionZ /= f2;
		motionX += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * shake;
		motionY += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * shake;
		motionZ += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * shake;
		motionX *= hardness;
		motionY *= hardness;
		motionZ *= hardness;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		prevRotationYaw = rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float)(Math.atan2(motionY, f3) * 180.0D / Math.PI);
	}

	@Override
	protected void entityInit()
	{
		dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
	 * posY, posZ, yaw, pitch
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int i)
	{
		setPosition(x, y, z);
		setRotation(yaw, pitch);
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z)
	{
		motionX = x;
		motionY = y;
		motionZ = z;

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
	public void onUpdate()
	{
		if(worldObj.isRemote && currentItem == null)
		{
			FarCore.network.sendToServer(new PacketEntityAsk(this));
			return;
		}
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
					((IProjectileItem) currentItem.getItem()).initEntity(this);
				inited = true;
			}
			for(EntityPlayer player : list)
				FarCore.network.sendToPlayer(new PacketEntity(this), player);
			list.clear();
		}
		super.onUpdate();

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
			recountYawAndPitch();

		Block block = worldObj.getBlock(blockPos[0], blockPos[1], blockPos[2]);

		if (!block.isAir(worldObj, blockPos[0], blockPos[1], blockPos[2]))
		{
			block.setBlockBoundsBasedOnState(worldObj, blockPos[0], blockPos[1], blockPos[2]);
			AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(worldObj, blockPos[0], blockPos[1], blockPos[2]);

			if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(posX, posY, posZ)))
			{
				hitOnGround();
				return;
			}
		}

		Vec3 vec31 = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 vec3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition movingobjectposition = worldObj.func_147447_a(vec31, vec3, false, true, false);
		vec31 = Vec3.createVectorHelper(posX, posY, posZ);
		vec3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

		if (movingobjectposition != null)
			vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);

		Entity entity = null;
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double d0 = 0.0D;
		int i;
		float f1;

		for (i = 0; i < list.size(); ++i)
		{
			Entity entity1 = (Entity)list.get(i);

			if (entity1.canBeCollidedWith() && entity1 != shooter)
			{
				f1 = 0.3F;
				AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f1, f1, f1);
				MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

				if (movingobjectposition1 != null)
				{
					double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

					if (d1 < d0 || d0 == 0.0D)
					{
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}
		if (entity != null)
			hitOnEntity(entity);
		float f2, f4;
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		f2 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
		for (rotationPitch = (float)(Math.atan2(motionY, f2) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F);
		while (rotationPitch - prevRotationPitch >= 180.0F)
			prevRotationPitch += 360.0F;
		while (rotationYaw - prevRotationYaw < -180.0F)
			prevRotationYaw -= 360.0F;
		while (rotationYaw - prevRotationYaw >= 180.0F)
			prevRotationYaw += 360.0F;
		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		float f3 = 0.99F;
		f1 = 0.05F;
		if (isInWater())
		{
			for (int l = 0; l < 4; ++l)
			{
				f4 = 0.25F;
				worldObj.spawnParticle("bubble", posX - motionX * f4, posY - motionY * f4, posZ - motionZ * f4, motionX, motionY, motionZ);
			}

			f3 = 0.8F;
		}
		if (isWet())
			extinguish();
		motionX *= f3;
		motionY *= f3;
		motionZ *= f3;
		motionY -= f1;
		setPosition(posX, posY, posZ);
		func_145775_I();
	}

	private void hitOnGround()
	{
		if(currentItem.getItem() instanceof IProjectileItem)
			if(((IProjectileItem) currentItem.getItem()).onHitGround(worldObj, blockPos[0], blockPos[1], blockPos[2], this))
				return;
		EntityItem entity = new EntityItem(worldObj, posX - motionX, posY - motionY, posZ - motionZ, currentItem);
		entity.motionX = motionX + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
		entity.motionY = motionY + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08 + 0.2;
		entity.motionZ = motionZ + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
		entity.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entity);
		setDead();
	}

	private void hitOnEntity(Entity entity)
	{
		if(currentItem.getItem() instanceof IProjectileItem)
			if(((IProjectileItem) currentItem.getItem()).onHitEntity(worldObj, entity, this))
				return;
		entity.addVelocity(motionX * .1, motionY * .1 + 0.1, motionZ * .1);
		EntityItem entity1 = new EntityItem(worldObj, posX - motionX, posY - motionY, posZ - motionZ, currentItem);
		entity1.motionX = motionX + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
		entity1.motionY = motionY + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08 + 0.2;
		entity1.motionZ = motionZ + rand.nextDouble() * 0.08 - rand.nextDouble() * 0.08;
		entity1.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entity1);
		setDead();
	}

	private void recountYawAndPitch()
	{
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		prevRotationYaw = rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float)(Math.atan2(motionY, f) * 180.0D / Math.PI);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		blockPos = nbt.getIntArray("blockPos");
		currentItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setIntArray("blockPos", blockPos);
		nbt.setTag("item", currentItem.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt)
	{
		nbt.setIntArray("p", blockPos);
		nbt.setTag("i", currentItem.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public void readDescriptionsFromNBT(NBTTagCompound nbt)
	{
		blockPos = nbt.getIntArray("p");
		currentItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("i"));
	}

	@Override
	public void markNBTSync(EntityPlayer player)
	{
		list.add(player);
	}
}