package fle.core.entity;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

import com.mojang.authlib.GameProfile;

import fle.core.item.tool.ToolMaterialInfo;

@Deprecated
public class EntityFleArrow extends EntityArrow
{
	private int hitBlockX = -1;
	private int hitBlockY = -1;
	private int hitBlockZ = -1;
	private Block hitBlock = Blocks.air;
	private int hitBlockMeta = 0;
	private boolean inGround = false;
	private int ticksAlive = 0;
	private int ticksInAir = 0;
	private int knockback = 0;
	private ItemStack arrow = null;
	  
	public EntityFleArrow(World aWorld)
	{
		super(aWorld);
	}

	public EntityFleArrow(World aWorld, EntityLivingBase aEntity, float aSpeed, ItemStack aStack)
	{
		super(aWorld, aEntity, aSpeed);
		setArrowItem(aStack);
	}

	public EntityFleArrow(World aWorld, double x, double y, double z)
	{
		super(aWorld, x, y, z);
	}
	
	public EntityFleArrow(EntityArrow aArrow, ItemStack aStack)
	{
		super(aArrow.worldObj);
	    NBTTagCompound tNBT = new NBTTagCompound();
	    aArrow.writeToNBT(tNBT);
	    readFromNBT(tNBT);
	    setArrowItem(aStack);
	}

	protected void initRotationPitch()
	{
	    if ((prevRotationPitch == 0.0F) && (prevRotationYaw == 0.0F))
	    {
	    	float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
	    	prevRotationYaw = (rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI));
	    	prevRotationPitch = (rotationPitch = (float)(Math.atan2(motionY, f) * 180.0D / Math.PI));
	    }
	}
	
	protected void onArrowInGround(Block aBlock)
	{
    	int j = worldObj.getBlockMetadata(hitBlockX, hitBlockY, hitBlockZ);
    	if ((aBlock != hitBlock) || (j != hitBlockMeta))
    	{
    		inGround = false;
    		motionX *= rand.nextFloat() * 0.2F;
    		motionY *= rand.nextFloat() * 0.2F;
    		motionZ *= rand.nextFloat() * 0.2F;
    		ticksAlive = 0;
    		ticksInAir = 0;
    	}
	}
	
	protected Entity checkArrowInEntity(Entity aShootingEntity, Vec3 vec3, Vec3 vec31)
	{
		Entity tHitEntity = null;
		List tAllPotentiallyHitEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
    	double tLargestDistance = 1.7976931348623157E+308D;
    	for (int i = 0; i < tAllPotentiallyHitEntities.size(); i++)
    	{
    		Entity entity1 = (Entity)tAllPotentiallyHitEntities.get(i);
    		if ((entity1.canBeCollidedWith()) && ((entity1 != aShootingEntity) || (ticksInAir >= 5)))
    		{
    			AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(0.3D, 0.3D, 0.3D);
    			MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
    			if (movingobjectposition1 != null)
    			{
    				double tDistance = vec31.distanceTo(movingobjectposition1.hitVec);
    				if (tDistance < tLargestDistance)
    				{
    					tHitEntity = entity1;
    					tLargestDistance = tDistance;
    				}
    			}
    		}
    	}
    	return tHitEntity;
	}
	
	protected void hitEntity(MovingObjectPosition aVector, Entity aShootingEntity, Entity aHitEntity)
	{
		ToolMaterialInfo tInfo = new ToolMaterialInfo(arrow.getTagCompound());
		
		float tMagicDamage = (aVector.entityHit instanceof EntityLivingBase) ? EnchantmentHelper.func_152377_a(arrow, ((EntityLivingBase)aVector.entityHit).getCreatureAttribute()) : 0.0F;
		float tDamage = MathHelper.ceiling_double_int(MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ) * (getDamage() + ((tInfo != null) && (tInfo.getMaterialBase() != null) ? tInfo.getMaterialBase().getPropertyInfo().getHardness() / 2.0F - 1.0F + tInfo.getMaterialBase().getPropertyInfo().getDenseness() : 0.0F)));
		if (getIsCritical())
		{
			tDamage += rand.nextInt((int)(tDamage / 2.0D + 2.0D));
		}
		int tFireDamage = isBurning() ? 5 : 0;
		int tKnockback = knockback;
		int tHitTimer = -1;
  
		int[] tDamages = onHitEntity(aVector.entityHit, aShootingEntity == null ? this : aShootingEntity, arrow == null ? new ItemStack(Items.arrow, 1) : arrow, (int)(tDamage * 2.0F), (int)(tMagicDamage * 2.0F), tKnockback, tFireDamage, tHitTimer);
		if (tDamages != null)
		{
			tDamage = tDamages[0] / 2.0F;
			tMagicDamage = tDamages[1] / 2.0F;
			tKnockback = tDamages[2];
			tFireDamage = tDamages[3];
			tHitTimer = tDamages[4];
			if ((tFireDamage > 0) && (!(aVector.entityHit instanceof EntityEnderman)))
			{
				aVector.entityHit.setFire(tFireDamage);
			}
			if ((!(aHitEntity instanceof EntityPlayer)) && (EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, arrow) > 0))
			{
				EntityPlayer tPlayer = null;
				if ((worldObj instanceof WorldServer))
				{
					tPlayer = FakePlayerFactory.get((WorldServer)worldObj, new GameProfile(new UUID(0L, 0L), (aShootingEntity instanceof EntityLivingBase) ? ((EntityLivingBase)aShootingEntity).getCommandSenderName() : "Arrow"));
				}
				if (tPlayer != null)
				{
					tPlayer.inventory.currentItem = 0;
					tPlayer.inventory.setInventorySlotContents(0, getArrowItem());
					aShootingEntity = tPlayer;
					tPlayer.setDead();
				}
			}
			DamageSource tDamageSource = DamageSource.causeArrowDamage(this, aShootingEntity == null ? this : aShootingEntity);
			if ((tDamage + tMagicDamage > 0.0F) && (aVector.entityHit.attackEntityFrom(tDamageSource, tDamage + tMagicDamage)))
			{
				if ((aVector.entityHit instanceof EntityLivingBase))
				{
					if (tHitTimer >= 0)
					{
						aVector.entityHit.hurtResistantTime = tHitTimer;
					}
					if (((aVector.entityHit instanceof EntityCreeper)) && (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, arrow) > 0))
					{
						((EntityCreeper)aVector.entityHit).func_146079_cb();
					}
					EntityLivingBase tHitLivingEntity = (EntityLivingBase)aVector.entityHit;
					if (!worldObj.isRemote)
					{
						tHitLivingEntity.setArrowCountInEntity(tHitLivingEntity.getArrowCountInEntity() + 1);
					}
					if (tKnockback > 0)
					{
						float tKnockbackDivider = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
						if (tKnockbackDivider > 0.0F)
						{
							tHitLivingEntity.addVelocity(motionX * tKnockback * 0.6000000238418579D / tKnockbackDivider, 0.1D, motionZ * tKnockback * 0.6000000238418579D / tKnockbackDivider);
						}
					}
					if ((aShootingEntity != null) && (tHitLivingEntity != aShootingEntity) && ((tHitLivingEntity instanceof EntityPlayer)) && ((aShootingEntity instanceof EntityPlayerMP)))
					{
						((EntityPlayerMP)aShootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
					}
				}
				if (((aShootingEntity instanceof EntityPlayer)) && (tMagicDamage > 0.0F))
				{
					((EntityPlayer)aShootingEntity).onEnchantmentCritical(aVector.entityHit);
				}
				if ((!(aVector.entityHit instanceof EntityEnderman)) || (((EntityEnderman)aVector.entityHit).getActivePotionEffect(Potion.weakness) != null))
				{
					if (tFireDamage > 0)
					{
						aVector.entityHit.setFire(tFireDamage);
					}
					playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
					setDead();
				}
			}
			else
			{
				motionX *= -0.1000000014901161D;
				motionY *= -0.1000000014901161D;
				motionZ *= -0.1000000014901161D;
				rotationYaw += 180.0F;
				prevRotationYaw += 180.0F;
				ticksInAir = 0;
			}
		}
	}
	
	protected ItemStack getArrowItem()
	{
		return null;
	}

	protected int[] onHitEntity(Entity entityHit, Entity entity,
			ItemStack itemStack, int aRegularDamage, int aMagicDamage, int aKnockback, 
			int aFireDamage, int aHitTimer)
	{
		return new int[] {aRegularDamage, aMagicDamage, aKnockback, aFireDamage, aHitTimer};
	}

	public void onArrowUpdate()
	{
		onEntityUpdate();
	    if ((arrow == null) && (!worldObj.isRemote))
	    {
	    	setDead();
	    	return;
	    }
	    Entity tShootingEntity = shootingEntity;
	    if (ticksAlive++ >= 3000)
	    {
	    	setDead();
	    }
	    initRotationPitch();
	    Block tBlock = worldObj.getBlock(hitBlockX, hitBlockY, hitBlockZ);
	    if (tBlock.getMaterial() != Material.air)
	    {
	    	tBlock.setBlockBoundsBasedOnState(worldObj, hitBlockX, hitBlockY, hitBlockZ);
	    	AxisAlignedBB axisalignedbb = tBlock.getCollisionBoundingBoxFromPool(worldObj, hitBlockX, hitBlockY, hitBlockZ);
	    	if ((axisalignedbb != null) && (axisalignedbb.isVecInside(Vec3.createVectorHelper(posX, posY, posZ))))
	    	{
	    		inGround = true;
	    	}
	    }
	    if(arrowShake > 0)
	    	--arrowShake;
	    if (inGround)
	    {
	    	onArrowInGround(tBlock);
	    }
	    else
	    {
	    	ticksInAir += 1;
	    	Vec3 vec31 = Vec3.createVectorHelper(posX, posY, posZ);
	    	Vec3 vec3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
	    	MovingObjectPosition tVector = worldObj.func_147447_a(vec31, vec3, false, true, false);
	    	vec31 = Vec3.createVectorHelper(posX, posY, posZ);
	    	vec3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
	    	if (tVector != null)
	    	{
	    		vec3 = Vec3.createVectorHelper(tVector.hitVec.xCoord, tVector.hitVec.yCoord, tVector.hitVec.zCoord);
	    	}
	    	Entity tHitEntity = checkArrowInEntity(tShootingEntity, vec3, vec31);
	    	
	    	if (tHitEntity != null)
	    	{
	    		tVector = new MovingObjectPosition(tHitEntity);
	    	}
	    	if ((tVector != null) && (tVector.entityHit != null) && ((tVector.entityHit instanceof EntityPlayer)))
	    	{
	    		EntityPlayer entityplayer = (EntityPlayer)tVector.entityHit;
	    		if ((entityplayer.capabilities.disableDamage) || (((tShootingEntity instanceof EntityPlayer)) && (!((EntityPlayer)tShootingEntity).canAttackPlayer(entityplayer))))
	    		{
	    			tVector = null;
	    		}
	    	}
	    	if (tVector != null)
	    	{
	    		if (tVector.entityHit != null)
	    		{
	    			hitEntity(tVector, tShootingEntity, tHitEntity);
	    		}
		        else
		        {
		        	hitBlockX = tVector.blockX;
		        	hitBlockY = tVector.blockY;
		        	hitBlockZ = tVector.blockZ;
		        	hitBlock = worldObj.getBlock(hitBlockX, hitBlockY, hitBlockZ);
		        	hitBlockMeta = worldObj.getBlockMetadata(hitBlockX, hitBlockY, hitBlockZ);
		        	motionX = ((float)(tVector.hitVec.xCoord - posX));
		        	motionY = ((float)(tVector.hitVec.yCoord - posY));
		        	motionZ = ((float)(tVector.hitVec.zCoord - posZ));
		        	float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		        	posX -= motionX / f2 * 0.0500000007450581D;
		        	posY -= motionY / f2 * 0.0500000007450581D;
		        	posZ -= motionZ / f2 * 0.0500000007450581D;
		        	playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
		        	inGround = true;
		        	arrowShake = 7;
		        	setIsCritical(false);
		        	if (hitBlock.getMaterial() != Material.air)
		        	{
		        		hitBlock.onEntityCollidedWithBlock(worldObj, hitBlockX, hitBlockY, hitBlockZ, this);
		        	}
		        	if (breaksOnImpact())
		        	{
		        		setDead();
		        	}
		        }
	        }
	    }
	    if (getIsCritical())
	    {
	    	for (int i = 0; i < 4; i++)
	    	{
	          worldObj.spawnParticle("crit", posX + motionX * i / 4.0D, posY + motionY * i / 4.0D, posZ + motionZ * i / 4.0D, -motionX, -motionY + 0.2D, -motionZ);
	        }
	    }
	    posX += motionX;posY += motionY;posZ += motionZ;
	    rotationYaw = ((float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI));
	    for (rotationPitch = ((float)(Math.atan2(motionY, MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ)) * 180.0D / Math.PI)); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {}
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
	    rotationPitch = (prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F);
	    rotationYaw = (prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F);
	    float tFrictionMultiplier = 0.99F;
	    if (isInWater())
	    {
	    	for (int l = 0; l < 4; l++)
	    	{
	    		worldObj.spawnParticle("bubble", posX - motionX * 0.25D, posY - motionY * 0.25D, posZ - motionZ * 0.25D, motionX, motionY, motionZ);
	        }
	        tFrictionMultiplier = 0.8F;
	    }
	    if (isWet())
	    {
	    	extinguish();
	    }
	    motionX *= tFrictionMultiplier;
	    motionY *= tFrictionMultiplier;
	    motionZ *= tFrictionMultiplier;
	    motionY -= 0.0500000007450581D;
	    setPosition(posX, posY, posZ);
	    func_145775_I();
	}
	
	@Override
	public void onUpdate()
	{
		onArrowUpdate();
	}
	
    public void onCollideWithPlayer(EntityPlayer aPlayer)
    {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = arrow != null && (this.canBePickedUp == 1 || this.canBePickedUp == 2 && aPlayer.capabilities.isCreativeMode);

            if (this.canBePickedUp == 1 && !aPlayer.inventory.addItemStackToInventory(arrow))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                aPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }
	
	private boolean breaksOnImpact()
	{
		return false;
	}

	public void writeEntityToNBT(NBTTagCompound aNBT)
	{
		super.writeEntityToNBT(aNBT);
	    aNBT.setShort("xTile", (short)hitBlockX);
	    aNBT.setShort("yTile", (short)hitBlockY);
	    aNBT.setShort("zTile", (short)hitBlockZ);
	    aNBT.setShort("life", (short)ticksAlive);
	    aNBT.setByte("inTile", (byte)Block.getIdFromBlock(hitBlock));
	    aNBT.setByte("inData", (byte)hitBlockMeta);
	    aNBT.setByte("shake", (byte)arrowShake);
	    aNBT.setByte("inGround", (byte)(inGround ? 1 : 0));
	    aNBT.setByte("pickup", (byte)canBePickedUp);
	    aNBT.setDouble("damage", getDamage());
	    aNBT.setTag("mArrow", arrow == null ? null : arrow.writeToNBT(new NBTTagCompound()));
	}

	public void readEntityFromNBT(NBTTagCompound aNBT)
	{
		super.readEntityFromNBT(aNBT);
	    hitBlockX = aNBT.getShort("xTile");
	    hitBlockY = aNBT.getShort("yTile");
	    hitBlockZ = aNBT.getShort("zTile");
	    ticksAlive = aNBT.getShort("life");
	    hitBlock = Block.getBlockById(aNBT.getByte("inTile") & 0xFF);
	    hitBlockMeta = (aNBT.getByte("inData") & 0xFF);
	    arrowShake = (aNBT.getByte("shake") & 0xFF);
	    inGround = (aNBT.getByte("inGround") == 1);
	    setDamage(aNBT.getDouble("damage"));
	    canBePickedUp = aNBT.getByte("pickup");
	    arrow = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("mStack"));
	}

	public void setArrowItem(ItemStack aStack)
	{
		arrow = aStack;
	}
}