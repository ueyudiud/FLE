package farcore.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import farcore.block.IFallable;
import farcore.net.FleEntityPacket;
import flapi.FleAPI;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityFleFallingBlock extends EntityFallingBlock
		implements IEntityMessageHandler
{
	public IBlockState displayState;
	private IBlockState fallTile;
	public int fallTime;
	public boolean shouldDropItem = true;
	// private boolean field_145808_f;
	private boolean hurtEntities;
	public int fallHurtMax = 40;
	private float fallHurtAmount = 2.0F;
	public NBTTagCompound tileEntityData;
	
	public EntityFleFallingBlock(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityFleFallingBlock(World worldIn, BlockPos pos,
			IBlockState fallingBlockState)
	{
		super(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
				fallingBlockState);
		this.fallTile = fallingBlockState;
		displayState = fallTile.getBlock().getActualState(fallTile, worldIn,
				pos);
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
			FleAPI.mod.getNetworkHandler().sendLargePacket(
					new FleEntityPacket(this, (byte) 1,
							new Object[]{displayState, tileEntityData}),
					new TargetPoint(worldObj.provider.getDimensionId(), posX,
							posY, posZ, 72F));
		}
	}
	
	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		Block block = null;
		if (fallTile != null)
		{
			block = fallTile.getBlock();
			if (block.getMaterial() == Material.air)
			{
				setDead();
				return;
			}
		}
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		BlockPos blockpos;
		
		if (fallTime++ == 0)
		{
			blockpos = new BlockPos(this);
			
			if (worldObj.getBlockState(blockpos).getBlock() == block)
			{
				worldObj.setBlockToAir(blockpos);
			}
			else if (!this.worldObj.isRemote)
			{
				setDead();
				return;
			}
		}
		if (fallTime % 5 == 0)
			sendPacket();
		motionY -= 0.03999999910593033D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;
		
		if (!this.worldObj.isRemote)
		{
			blockpos = new BlockPos(this);
			
			if (onGround)
			{
				motionX *= 0.699999988079071D;
				motionZ *= 0.699999988079071D;
				motionY *= -0.5D;
				
				setDead();
				
				if (worldObj.canBlockBePlaced(block, blockpos, true,
						EnumFacing.UP, (Entity) null, (ItemStack) null)
						&& !(block instanceof IFallable
								? ((IFallable) block).canFallInto(worldObj,
										blockpos.down())
								: BlockFalling.canFallInto(this.worldObj,
										blockpos.down()))
						&& worldObj.setBlockState(blockpos, this.fallTile, 3))
				{
					if (block instanceof IFallable)
					{
						((IFallable) block).onEndFalling(worldObj, blockpos);
					}
					
					if (tileEntityData != null && block.hasTileEntity(fallTile))
					{
						TileEntity tileentity = worldObj
								.getTileEntity(blockpos);
								
						if (tileentity != null)
						{
							tileentity.readFromNBT(tileEntityData);
							tileentity.setPos(blockpos);
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
								block.damageDropped(fallTile)), 0.0F);
					}
				}
			}
			else if (fallTime > 100 && !worldObj.isRemote
					&& (blockpos.getY() < 1 || blockpos.getY() > 256)
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
							block.damageDropped(fallTile)), 0.0F);
				}
				
				setDead();
			}
		}
	}
	
	public void fall(float distance, float damageMultiplier)
	{
		if (fallTile == null)
			return;
		Block block = fallTile.getBlock();
		
		if (hurtEntities)
		{
			int i = MathHelper.ceiling_float_int(distance - 1.0F);
			
			if (i > 0)
			{
				ArrayList arraylist = Lists.newArrayList(
						worldObj.getEntitiesWithinAABBExcludingEntity(this,
								getEntityBoundingBox()));
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
		Block block = this.fallTile != null ? this.fallTile.getBlock()
				: Blocks.air;
		ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry
				.getNameForObject(block);
		tagCompound.setString("Block",
				resourcelocation == null ? "" : resourcelocation.toString());
		tagCompound.setByte("Data",
				(byte) block.getMetaFromState(this.fallTile));
		tagCompound.setByte("Time", (byte) this.fallTime);
		tagCompound.setBoolean("DropItem", this.shouldDropItem);
		tagCompound.setBoolean("HurtEntities", this.hurtEntities);
		tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
		tagCompound.setInteger("FallHurtMax", this.fallHurtMax);
		
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
		
		if (tagCompund.hasKey("Block", 8))
		{
			this.fallTile = Block
					.getBlockFromName(tagCompund.getString("Block"))
					.getStateFromMeta(i);
		}
		else if (tagCompund.hasKey("TileID", 99))
		{
			this.fallTile = Block.getBlockById(tagCompund.getInteger("TileID"))
					.getStateFromMeta(i);
		}
		else
		{
			this.fallTile = Block.getBlockById(tagCompund.getByte("Tile") & 255)
					.getStateFromMeta(i);
		}
		
		this.fallTime = tagCompund.getByte("Time") & 255;
		Block block = this.fallTile.getBlock();
		
		if (tagCompund.hasKey("HurtEntities", 99))
		{
			this.hurtEntities = tagCompund.getBoolean("HurtEntities");
			this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
			this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
		}
		else if (block == Blocks.anvil)
		{
			this.hurtEntities = true;
		}
		
		if (tagCompund.hasKey("DropItem", 99))
		{
			this.shouldDropItem = tagCompund.getBoolean("DropItem");
		}
		
		if (tagCompund.hasKey("TileEntityData", 10))
		{
			this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
		}
		
		if (block == null || block.getMaterial() == Material.air)
		{
			this.fallTile = Blocks.sand.getDefaultState();
		}
	}
	
	@Override
	public void setHurtEntities(boolean hurtEntities)
	{
		this.hurtEntities = hurtEntities;
	}
	
	@Override
	public IBlockState getBlock()
	{
		return displayState;
	}
	
	@Override
	public void process(byte type, Object obj)
	{
		if (type == 1)
		{
			Object[] array = (Object[]) obj;
			displayState = (IBlockState) array[0];
			tileEntityData = ((NBTTagCompound) array[1]);
		}
	}
}