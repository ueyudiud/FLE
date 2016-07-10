package farcore.lib.entity;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.util.Log;
import farcore.network.IDescribable;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

public class EntityFallingBlockExtended extends EntityFallingBlock implements IDescribable
{
	public static boolean canFallAt(World world, int x, int y, int z, Block target)
	{
		if(y < 0) return true;
		Block block = world.getBlock(x, y - 1, z);
		return (block.isAir(world, x, y - 1, z) || block instanceof IFluidBlock || block.isReplaceable(world, x, y - 1, z));
	}
	
	public static boolean replaceFallingBlock(World world, int x, int y, int z, Block target, int metadata)
	{
		Block hited = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		if(!hited.isAir(world, x, y, z))
		{
			hited.onBlockPreDestroy(world, x, y, z, meta);
			hited.breakBlock(world, x, y, z, hited, meta);
			if(hited.isReplaceable(world, x, y, z))
			{
				U.Worlds.spawnDropsInWorld(world, x, y, z, hited.getDrops(world, x, y, z, meta, 0));
			}
			return world.setBlock(x, y, z, target, metadata, 3);
		}
		return world.setBlock(x, y, z, target, metadata, 3);
	}
	
	private Block block;
	private int meta;
	private NBTTagCompound nbt;
	private int startX;
	private int startY;
	private int startZ;
	private int lifeTime;
	private boolean hitEntity;
	
	private List<EntityPlayer> list = new ArrayList(4);
	
	public EntityFallingBlockExtended(World world)
	{
		super(world);
	}
	public EntityFallingBlockExtended(World world, int xPos, int yPos, int zPos, Block block)
	{
		super(world, xPos, yPos, zPos, block);
		try
		{
			this.startX = xPos;
			this.startY = yPos;
			this.startZ = zPos;
			this.block = block;
			this.meta = world.getBlockMetadata(xPos, yPos, zPos);
			TileEntity tile = world.getTileEntity(xPos, yPos, zPos);
			if(tile != null)
			{
				tile.writeToNBT(nbt = new NBTTagCompound());
			}
		}
		catch(Exception exception)
		{
			Log.error("Fail to create a new falling block, disable this falling action.", exception);
			setDead();
		}
	}
	
    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {}

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    	if(worldObj.isRemote && block == null)
    	{
    		FarCore.network.sendToServer(new PacketEntityAsk(this));
    		return;
    	}
    	if(isDead)
    	{
    		;
    	}
    	else
    	{
    		if(block == null || block.isAir(worldObj, startX, startY, startZ))
    		{
    			setDead();
    			return;
    		}
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.lifeTime++;
            this.motionY -= 0.04D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.98D;
            this.motionY *= 0.98D;
            this.motionZ *= 0.98D;
            if (!worldObj.isRemote)
            {
                if (lifeTime == 1)
                {
                    if (worldObj.getBlock(startX, startY, startZ) != block)
                    {
                        setDead();
                        return;
                    }
                    
                    if(block instanceof ISmartFallableBlock)
                    {
                    	((ISmartFallableBlock) block).onStartFalling(worldObj, startX, startY, startZ);
                    }

                    worldObj.setBlockToAir(startX, startY, startZ);
                    worldObj.removeTileEntity(startX, startY, startZ);
                }
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.posY);
                int k = MathHelper.floor_double(this.posZ);

        		for(EntityPlayer player : list)
        		{
        			FarCore.network.sendToPlayer(new PacketEntity(this), player);
        		}
        		list.clear();
                if (this.onGround)
                {
                    this.motionX *= 0.7D;
                    this.motionZ *= 0.7D;
                    this.motionY *= -0.5D;

                    this.setDead();

                    label:
                    if (worldObj.canPlaceEntityOnSide(this.block, i, j, k, true, 1, (Entity)null, (ItemStack)null) && 
                    		((block instanceof ISmartFallableBlock && ((ISmartFallableBlock) block).canFallingBlockStay(worldObj, i, j, k, meta)) || 
                    				!canFallAt(worldObj, i, j, k, block)))
                    {
                    	if(block instanceof ISmartFallableBlock)
                    	{
                    		if(((ISmartFallableBlock) block).onFallOnGround(worldObj, i, j, k, meta, startY - j, nbt))
                    		{
                    			break label;
                    		}
                    	}
                    	replaceFallingBlock(worldObj, i, j, k, block, meta);
                        if (nbt != null)
                        {
                            TileEntity tile = worldObj.getTileEntity(i, j, k);

                            if (tile != null)
                            {
                                tile.readFromNBT(nbt);
                                tile.xCoord = i;
                                tile.yCoord = j;
                                tile.zCoord = k;
                                tile.markDirty();
                            }
                        }
                    }
                    else if (this.field_145813_c)
                    {
                    	if(block instanceof ISmartFallableBlock && ((ISmartFallableBlock) block).onDropFallenAsItem(worldObj, i, j, k, meta, nbt))
                    	{
                    		
                    	}
                    	else
                    	{
                    		entityDropItem(new ItemStack(block, 1, block.damageDropped(meta)), 0.0F);
                    	}
                    }
                }
                else if (lifeTime > 100 && !this.worldObj.isRemote && (j < 1 || j > 256) || this.lifeTime > 600)
                {
                    if (field_145813_c)
                    {
                    	if(block instanceof ISmartFallableBlock && ((ISmartFallableBlock) block).onDropFallenAsItem(worldObj, i, j, k, meta, nbt))
                    	{
                    		
                    	}
                    	else
                    	{
                    		entityDropItem(new ItemStack(this.block, 1, this.block.damageDropped(this.field_145814_a)), 0.0F);
                    	}
                    }

                    setDead();
                }
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
    		ArrayList<Entity> arraylist = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
    		
    		float amount;
    		for(Entity entity : arraylist)
    		{
    			amount = block instanceof ISmartFallableBlock ? ((ISmartFallableBlock) block).onFallOnEntity(worldObj, this, entity) : 2.0F;
    			if(amount > 0)
    			{
        			entity.attackEntityFrom(DamageSource.fallingBlock, 
        					(float)Math.min(MathHelper.floor_float((float)i * amount), 100F));
    			}
    			hitEntity = true;
    		}
    	}
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setString("block", GameData.getBlockRegistry().getNameForObject(block));
        nbt.setByte("data", (byte) this.meta);
        nbt.setByte("time", (byte) this.lifeTime);
        nbt.setBoolean("drop", this.field_145813_c);
        nbt.setBoolean("hit", this.hitEntity);
        nbt.setShort("startY", (short) startY);

        if (this.nbt != null)
        {
            nbt.setTag("tile", this.nbt);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("block", 99))
        {
            this.block = GameData.getBlockRegistry().getObject(nbt.getString("block"));
            if(block == null)
            {
            	setDead();
            }
        }
        else
        {
        	setDead();
        }

        this.meta = nbt.getByte("data") & 255;
        this.lifeTime = nbt.getByte("time") & 255;

        this.hitEntity = nbt.getBoolean("hit");
        this.startY = nbt.getShort("startY");
        
        if (nbt.hasKey("drop", 99))
        {
            this.field_145813_c = nbt.getBoolean("drop");
        }

        if (nbt.hasKey("tile", 10))
        {
            this.nbt = nbt.getCompoundTag("tile");
        }
    }

    public void func_145806_a(boolean flag)
    {
        this.hitEntity = flag;
    }

    public void addEntityCrashInfo(CrashReportCategory category)
    {
        super.addEntityCrashInfo(category);
        category.addCrashSection("Immitating block name", block.getUnlocalizedName());
        category.addCrashSection("Immitating block data", Integer.valueOf(this.meta));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
        return false;
    }
    
    public Block getBlock()
    {
		return this.block;
	}

    public final Block func_145805_f()
    {
        return getBlock();
    }

    public int getMeta()
    {
    	return meta;
    }
	
    @Override
	public NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt)
	{
        nbt.setString("block", GameData.getBlockRegistry().getNameForObject(block));
        nbt.setByte("data", (byte) this.meta);
		return nbt;
	}


    @Override
	public void readDescriptionsFromNBT(NBTTagCompound nbt)
	{
        this.block = GameData.getBlockRegistry().getObject(nbt.getString("block"));
        this.meta = nbt.getByte("data") & 255;
        if(block == null)
        {
        	block = Blocks.bedrock;
        }
	}

	@Override
	public void markNBTSync(EntityPlayer player)
	{
		list.add(player);
	}
}