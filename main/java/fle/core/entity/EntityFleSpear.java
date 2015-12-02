package fle.core.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFleSpear extends Entity implements IProjectile
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
	private int ticksInGround;
	private ItemStack spear = null;

	public EntityFleSpear(World world)
	{
		super(world);
	}

	@Override
	public void setThrowableHeading(double x, double y,
			double z, float range, float speed)
	{
        float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double)f2;
        y /= (double)f2;
        z /= (double)f2;
        x += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)speed;
        y += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)speed;
        z += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)speed;
        x *= (double) range;
        y *= (double) range;
        z *= (double) range;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f3 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
		
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}
	
	@Override
	protected void entityInit()
	{
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		
	}
}