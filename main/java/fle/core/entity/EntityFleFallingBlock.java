package fle.core.entity;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;
import flapi.block.interfaces.IMovableBlock;
import flapi.world.BlockPos;
import fle.FLE;

public class EntityFleFallingBlock extends EntityFallingBlock
{
	public static void setDropBlockInWorld(World aWorld, BlockPos aPos)
	{
		Block aBlock = aPos.getBlock();
		if(aBlock.getMaterial() != Material.air)
		{
			EntityFleFallingBlock entity = new EntityFleFallingBlock(aWorld, aPos, aBlock, aPos.getBlockMeta());
			entity.dataList = FLE.fle.getWorldManager().getDatas(aPos);
			if(aBlock instanceof IMovableBlock)
			{
				((IMovableBlock) aBlock).onBlockStartMove(aWorld, aPos);
			}
			FLE.fle.getWorldManager().removeData(aPos);
			aWorld.spawnEntityInWorld(entity);
		}
	}
	
	short[] dataList;
	protected Block block;
    protected boolean hitEntity;
    protected float fallHurtAmount = 2.0F;
    protected int fallHurtMax = 40;

    public EntityFleFallingBlock(World aWorld) 
    {
    	super(aWorld);
	}
	public EntityFleFallingBlock(World aWorld, BlockPos aPos, Block aTarget, int aMetadata)
	{
		this(aWorld, aPos.x, aPos.y, aPos.z, aTarget, aMetadata);
	}
	public EntityFleFallingBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
	{
		super(aWorld, (double) x + 0.5F, (double) y + 0.5F, (double) z + 0.5F, Blocks.sand, aMeta);
		block = aBlock;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		int[] aList = nbt.getIntArray("FWMData");
		for(int i = 0; i < aList.length; dataList[i++] = (short) aList[i]);
		block = GameData.getBlockRegistry().getObject(nbt.getString("Block"));
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		if(dataList != null)
		{
			int[] aList = new int[dataList.length];
			for(int i = 0; i < aList.length; aList[i++] = dataList[i]);
			nbt.setIntArray("FWMData", aList);
		}
		nbt.setString("Block", GameData.getBlockRegistry().getNameForObject(block));
	}

    /**
     * Called to update the entity's position/logic.
     */
	@Override
    public void onUpdate()
    {
		prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        ++field_145812_b;
        motionY -= 0.03999999910593033D;
        moveEntity(this.motionX, this.motionY, this.motionZ);
        motionX *= 0.9800000190734863D;
        motionY *= 0.9800000190734863D;
        motionZ *= 0.9800000190734863D;
        if (!this.worldObj.isRemote)
        {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);

            if (field_145812_b == 1)
            {
                if (worldObj.getBlock(i, j, k) != getFallingBlock())
                {
                    setDead();
                    return;
                }

                worldObj.setBlockToAir(i, j, k);
            }

            if (this.onGround)
            {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
                this.motionY *= -0.5D;
                
                setDead();
                boolean flag = !this.hitEntity;
                    
                boolean flag1 = worldObj.canPlaceEntityOnSide(getFallingBlock(), i, j, k, true, 1, (Entity)null, (ItemStack)null) && !BlockFalling.func_149831_e(this.worldObj, i, j - 1, k);
                if(getFallingBlock() instanceof IMovableBlock)
                	flag = flag && ((IMovableBlock) getFallingBlock()).canBlockFallOn(worldObj, new BlockPos(worldObj, i, j, k), flag1);                        		
                else if(!flag1) 
                	flag = false;
                if (getFallingBlock() instanceof IMovableBlock)
                {
                    ((IMovableBlock) getFallingBlock()).onBlockEndMove(worldObj, new BlockPos(worldObj, i, j, k), field_145814_a);
                	FLE.fle.getWorldManager().setDatas(new BlockPos(worldObj, i, j, k), dataList, true);
                }
                else if (flag && worldObj.setBlock(i, j, k, getFallingBlock(), this.field_145814_a, 3))
                {
                	FLE.fle.getWorldManager().setDatas(new BlockPos(worldObj, i, j, k), dataList, true);

                    if (field_145810_d != null && getFallingBlock() instanceof ITileEntityProvider)
                    {
                        TileEntity tileentity = this.worldObj.getTileEntity(i, j, k);

                        if (tileentity != null)
                        {
                            NBTTagCompound nbttagcompound = new NBTTagCompound();
                            tileentity.writeToNBT(nbttagcompound);
                            Iterator iterator = this.field_145810_d.func_150296_c().iterator();

                            while (iterator.hasNext())
                            {
                                String s = (String)iterator.next();
                                NBTBase nbtbase = this.field_145810_d.getTag(s);

                                if (!s.equals("x") && !s.equals("y") && !s.equals("z"))
                                {
                                    nbttagcompound.setTag(s, nbtbase.copy());
                                }
                            }

                            tileentity.readFromNBT(nbttagcompound);
                            tileentity.markDirty();
                        }
                    }
                }
                else if (field_145813_c && !hitEntity)
                {
                    entityDropItem();
                }
            }
            else if (field_145812_b > 100 && !this.worldObj.isRemote && (j < 1 || j > 256) || field_145812_b > 600)
            {
                if (field_145813_c)
                {
                    entityDropItem();
                }

                setDead();
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float height)
    {
    	int i = MathHelper.ceiling_float_int(height - 1.0F);

        if (i > 0)
        {
            ArrayList arraylist = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
            boolean flag = getFallingBlock() instanceof IMovableBlock;
            DamageSource damagesource = DamageSource.fallingBlock;
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor_float((float)i * this.fallHurtAmount), this.fallHurtMax));

                if (flag && entity instanceof EntityLivingBase)
                {
                    hitEntity = true;
                    ((IMovableBlock) getFallingBlock()).onBlockHitEntity(worldObj, (EntityLivingBase) entity);
                }
            }
        }
    }
    
    public EntityItem entityDropItem()
    {
    	return entityDropItem(new ItemStack(getFallingBlock(), 1, getFallingBlock().damageDropped(field_145814_a)), 0.0F);
    }
	
	public Block getFallingBlock()
	{
		return block;
	}
	
	public void addEntityCrashInfo(CrashReportCategory report)
    {
        super.addEntityCrashInfo(report);
        report.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(getFallingBlock())));
        report.addCrashSection("Immitating block data", Integer.valueOf(this.field_145814_a));
    }

    @Override
    public Block func_145805_f()
    {
        return getFallingBlock();
    }
}