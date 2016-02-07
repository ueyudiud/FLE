package farcore.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.registry.GameData;
import farcore.FarCore;
import farcore.block.interfaces.IFallable;
import farcore.net.FleFallingBlockPacket;
import farcore.world.BlockPos;
import farcore.world.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFleFallingBlock extends EntityFallingBlock
{
	public Block block;
	public int meta;
	public int fallTime;
	public boolean shouldDropItem = true;
	// private boolean field_145808_f;
	private boolean hurtEntities;
	public int fallHurtMax = 40;
	private float fallHurtAmount = 2.0F;
	public NBTTagCompound tileEntityData;
	private int dropX;
	private int dropY;
	private int dropZ;
	
	public EntityFleFallingBlock(World world)
	{
		super(world);
	}

	public EntityFleFallingBlock(World worldIn, int x, int y, int z, Block block)
	{
		this(worldIn, x, y, z, block, 0);
	}
	public EntityFleFallingBlock(BlockPos pos)
	{
		this(pos.world(), pos.x, pos.y, pos.z, pos.block(), pos.meta());
	}
	public EntityFleFallingBlock(World worldIn, int x, int y, int z, Block block, int meta)
	{
		super(worldIn, x + 0.5D, y, z + 0.5D, block);
		this.block = block;
		this.meta = meta;
		this.dropX = x;
		this.dropY = y;
		this.dropZ = z;
	}	
	
	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	protected void entityInit()
	{
	
	}
	
	private void sendPacket()
	{
		if (!worldObj.isRemote)
		{
			FarCore.mod.getNetworkHandler().sendLargePacket(
					new FleFallingBlockPacket(this),
					new TargetPoint(worldObj.provider.dimensionId, posX,
							posY, posZ, 72F));
		}
	}
	
	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		BlockPos blockpos;
		
		if (this.block == null)
		{
			return;
		}
		else if(block.getMaterial() == Material.air)
		{
			setDead();
			return;
		}
		if (fallTime++ == 0)
		{
			if (worldObj.getBlock(dropX, dropY, dropZ) == block)
			{
				worldObj.setBlockToAir(dropX, dropY, dropZ);
			}
			else if (!this.worldObj.isRemote)
			{
				setDead();
				return;
			}
		}
		if (fallTime % 5 == 0)
			sendPacket();
		motionY -= 0.05;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.98;
		motionY *= 0.98;
		motionZ *= 0.98;
		
		if (!this.worldObj.isRemote)
		{
			blockpos = new BlockPos(this.worldObj, dropX, posY, dropZ);
			
			if (onGround)
			{
				motionX *= 0.7D;
				motionZ *= 0.7D;
				motionY *= -0.5D;
				
				setDead();
				
				if (worldObj.canPlaceEntityOnSide(block, blockpos.x, blockpos.y, blockpos.z, true, 1,
						(Entity) null, (ItemStack) null)
						&& !(block instanceof IFallable
								? ((IFallable) block).canFallInto(blockpos.offset(Direction.DOWN))
								: BlockFalling.func_149831_e(worldObj, blockpos.x, blockpos.y, blockpos.z))
						&& worldObj.setBlock(blockpos.x, blockpos.y, blockpos.z, block, meta, 3))
				{
					if (block instanceof IFallable)
					{
						((IFallable) block).onEndFalling(blockpos);
					}
					
					if (tileEntityData != null && block.hasTileEntity(meta))
					{
						TileEntity tileentity = blockpos.tile();
								
						if (tileentity != null)
						{
							tileentity.readFromNBT(tileEntityData);
							tileentity.xCoord = blockpos.x;
							tileentity.yCoord = blockpos.y;
							tileentity.zCoord = blockpos.z;
							tileentity.markDirty();
						}
					}
				}
				else if (shouldDropItem && worldObj.getGameRules()
						.getGameRuleBooleanValue("doTileDrops"))
				{
					if (block instanceof IFallable)
					{
						List<ItemStack> list = ((IFallable) block)
								.onBlockDropAsItem(this);
						if (list != null)
							for (ItemStack stack : list)
							{
								entityDropItem(stack.copy(), 0);
							}
					}
					else
					{
						entityDropItem(new ItemStack(block, 1,
								block.damageDropped(meta)), 0.0F);
					}
				}
			}
			else if (fallTime > 100 && !worldObj.isRemote
					&& (blockpos.y < 1 || blockpos.y > 256)
					|| fallTime > 600)
			{
				if (block instanceof IFallable)
				{
					List<ItemStack> list = ((IFallable) block)
							.onBlockDropAsItem(this);
					if (list != null)
						for (ItemStack stack : list)
						{
							entityDropItem(stack.copy(), 0);
						}
				}
				else
				{
					entityDropItem(new ItemStack(block, 1,
							block.damageDropped(meta)), 0.0F);
				}
				
				setDead();
			}
		}
	}
	
	public void fall(float distance, float damageMultiplier)
	{
		if (block == null)
		{
			return;
		}		
		if (hurtEntities)
		{
			int i = MathHelper.ceiling_float_int(distance - 1.0F);
			
			if (i > 0)
			{
				ArrayList arraylist = Lists.newArrayList(
						worldObj.getEntitiesWithinAABBExcludingEntity(this,
								getBoundingBox()));
				DamageSource damagesource = DamageSource.fallingBlock;
				Iterator iterator = arraylist.iterator();
				
				while (iterator.hasNext())
				{
					Entity entity = (Entity) iterator.next();
					entity.attackEntityFrom(damagesource,
							(float) Math.min(
									MathHelper.floor_float(
											(float) i * fallHurtAmount),
							fallHurtMax));
				}
				
				if (block instanceof IFallable)
				{
					((IFallable) block).onHitEntity(this);
				}
			}
		}
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		Block block = this.block != null ? this.block : Blocks.air;
		tagCompound.setString("Block", GameData.getBlockRegistry().getNameForObject(block));
		tagCompound.setByte("Data", (byte) meta);
		tagCompound.setShort("Time", (short) this.fallTime);
		tagCompound.setBoolean("DropItem", this.shouldDropItem);
		tagCompound.setBoolean("HurtEntities", this.hurtEntities);
		tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
		tagCompound.setInteger("FallHurtMax", this.fallHurtMax);
		tagCompound.setInteger("X", dropX);
		tagCompound.setInteger("Z", dropZ);
		
		if (this.tileEntityData != null)
		{
			tagCompound.setTag("TileEntityData", this.tileEntityData);
		}
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		int i = tagCompund.getByte("Data") & 255;
		
		if (tagCompund.hasKey("Block"))
		{
			this.block = GameData.getBlockRegistry().getObject(tagCompund.getString("Block"));
		}
		
		this.fallTime = tagCompund.getShort("Time");
		this.dropX = tagCompund.getInteger("X");
		this.dropZ = tagCompund.getInteger("Z");

		if (tagCompund.hasKey("HurtEntities"))
		{
			this.hurtEntities = tagCompund.getBoolean("HurtEntities");
			this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
			this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
		}
		
		if (tagCompund.hasKey("DropItem"))
		{
			this.shouldDropItem = tagCompund.getBoolean("DropItem");
		}
		
		if (tagCompund.hasKey("TileEntityData", 10))
		{
			this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
		}
	}
	
	@Override
	public void func_145806_a(boolean hurt)
	{
		setHurtEntities(hurt);
	}
	
	public void setHurtEntities(boolean hurtEntities)
	{
		this.hurtEntities = hurtEntities;
	}
	
	@Override
	public Block func_145805_f()
	{
		return block;
	}

	public Block getBlock()
	{
		return func_145805_f();
	}

	public World getWorld()
	{
		return worldObj;
	}
}