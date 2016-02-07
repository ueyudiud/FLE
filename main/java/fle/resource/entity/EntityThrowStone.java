package fle.resource.entity;

import java.util.List;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.registry.GameData;
import farcore.entity.EntityFleFallingBlock;
import farcore.entity.IEntityMessageHandler;
import farcore.net.FleEntityPacket;
import farcore.substance.Substance;
import flapi.FleResource;
import fle.core.FLE;
import fle.core.enums.EnumRockSize;
import fle.init.Blocks;
import fle.resource.block.auto.BlockUniversalStoneChip;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityThrowStone extends Entity implements IProjectile, IEntityMessageHandler
{
	public int xIntPos;
	public int yIntPos = -1;
	public int zIntPos;
	public EnumRockSize size;
	public Substance rock;
	private EntityLivingBase shootingEntity;
	private boolean inGround;
	private int ticksInGround;
	private int ticksInAir;
	private int arrowShake;
	private Block hitBlock;
	private int hitInData;
	
	public EntityThrowStone(World world)
	{
		super(world);
		isImmuneToFire = true;
		renderDistanceWeight = 12.0F;
	}
	public EntityThrowStone(World world, ItemStack stack, 
			EntityLivingBase entity, float hardness)
	{
		this(world);
		size = ((BlockUniversalStoneChip) Blocks.stoneChip).getRockSize(stack);
		rock = ((BlockUniversalStoneChip) Blocks.stoneChip).getRock(stack);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = entity;

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, hardness * 1.5F, 1.0F);
	}

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double x, double y, double z, float hardness, float shake)
    {
        float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double)f2;
        y /= (double)f2;
        z /= (double)f2;
        x += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.0075D * (double)shake;
        y += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.0075D * (double)shake;
        z += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.0075D * (double)shake;
        x *= (double)hardness;
        y *= (double)hardness;
        z *= (double)hardness;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f3 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

	@Override
	protected void entityInit()
	{
		;
	}
	
	public boolean canAttackWithItem()
	{
		return false;
	}
	
	public void sendPacket()
	{
		if(!worldObj.isRemote)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeEntityToNBT(nbt);
			FLE.fle.getNetworkHandler().sendLargePacket(
					new FleEntityPacket(this, (byte) -1, nbt), 
					new TargetPoint(worldObj.provider.dimensionId, posX,
							posY, posZ, 72F));
		}
	}
	
	@Override
	public void process(byte type, Object obj)
	{
		readEntityFromNBT((NBTTagCompound) obj);
	}
	
	/**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        Block block = this.worldObj.getBlock(this.xIntPos, this.yIntPos, this.zIntPos);

        if (block.getMaterial() != Material.air)
        {
            block.setBlockBoundsBasedOnState(this.worldObj, this.xIntPos, this.yIntPos, this.zIntPos);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.xIntPos, this.yIntPos, this.zIntPos);

            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround)
        {
            int j = this.worldObj.getBlockMetadata(this.xIntPos, this.yIntPos, this.zIntPos);

            if (block == this.hitBlock && j == this.hitInData)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200)
                {
                    this.setDead();
                }
            }
            else
            {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else
        {
            ++this.ticksInAir;
            Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
            vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            float f1;

            for (i = 0; i < list.size(); ++i)
            {
                Entity entity1 = (Entity)list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double)f1, (double)f1, (double)f1);
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
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
                {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f4;

            if (movingobjectposition != null)
            {
                if (movingobjectposition.entityHit != null)
                {
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int k = MathHelper.ceiling_double_int((double)f2 * size.getDamage());

                    DamageSource damagesource = null;

                    if (shootingEntity == null)
                    {
                        damagesource = DamageSource.causeThrownDamage(this, this);
                    }
                    else
                    {
                        damagesource = DamageSource.causeThrownDamage(this, shootingEntity);
                    }

                    if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                        movingobjectposition.entityHit.setFire(5);
                    }

                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float)k))
                    {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase)
                        {
                            EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;

                            if (!this.worldObj.isRemote)
                            {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                            if (f4 > 0.0F)
                            {
                                movingobjectposition.entityHit.addVelocity(this.motionX * 0.6D / (double)f4, 0.1D, this.motionZ * 0.6D / (double)f4);
                            }
                            
                            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase)
                            {
                                EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase)this.shootingEntity, entitylivingbase);
                            }

                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                            {
                                ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                            }
                        }

                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                        {
                            this.setDead();
                        }
                    }
                    else
                    {
                        this.motionX *= -0.1D;
                        this.motionY *= -0.1D;
                        this.motionZ *= -0.1D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
                else
                {
                    this.xIntPos = movingobjectposition.blockX;
                    this.yIntPos = movingobjectposition.blockY;
                    this.zIntPos = movingobjectposition.blockZ;
                    this.hitBlock = this.worldObj.getBlock(this.xIntPos, this.yIntPos, this.zIntPos);
                    this.hitInData = this.worldObj.getBlockMetadata(this.xIntPos, this.yIntPos, this.zIntPos);
                    this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)f2 * 0.05;
                    this.posY -= this.motionY / (double)f2 * 0.05;
                    this.posZ -= this.motionZ / (double)f2 * 0.05;
                    this.inGround = true;
                    this.arrowShake = 7;

                    if (this.hitBlock.getMaterial() != Material.air)
                    {
                        this.hitBlock.onEntityCollidedWithBlock(this.worldObj, this.xIntPos, this.yIntPos, this.zIntPos, this);
                    }
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
            float f3 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater())
            {
                for (int l = 0; l < 4; ++l)
                {
                    f4 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ);
                }

                f3 = 0.8F;
            }

            if (isWet())
            {
                extinguish();
            }

            this.motionX *= (double)f3;
            this.motionY *= (double)f3;
            this.motionZ *= (double)f3;
            this.motionY -= (double)f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            func_145775_I();
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer player)
    {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
        {
        	boolean flag = true;
        	
        	if (!player.inventory.addItemStackToInventory(
        			((BlockUniversalStoneChip) Blocks.stoneChip).setRock(new ItemStack(Blocks.stoneChip), rock, size)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setShort("xTile", (short)this.xIntPos);
        nbt.setShort("yTile", (short)this.yIntPos);
        nbt.setShort("zTile", (short)this.zIntPos);
        nbt.setShort("life", (short)this.ticksInGround);
        nbt.setString("rock", this.rock.getName());
        nbt.setString("size", size.name());
        if(hitBlock != null)
        {
            nbt.setString("inTile", GameData.getBlockRegistry().getNameForObject(hitBlock));
            nbt.setByte("inData", (byte) this.hitInData);
        }
        nbt.setByte("shake", (byte)this.arrowShake);
        nbt.setBoolean("inGround", inGround);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.xIntPos = nbt.getShort("xTile");
        this.yIntPos = nbt.getShort("yTile");
        this.zIntPos = nbt.getShort("zTile");
        this.ticksInGround = nbt.getShort("life");
        this.rock = FleResource.rock.get(nbt.getString("rock"));
        this.size = EnumRockSize.valueOf(nbt.getString("size"));
        this.hitBlock = GameData.getBlockRegistry().getObject(nbt.getString("inTile"));
        this.hitInData = nbt.getByte("inData") & 255;
        this.arrowShake = nbt.getByte("shake") & 255;
        this.inGround = nbt.getBoolean("inGround");
    }
}