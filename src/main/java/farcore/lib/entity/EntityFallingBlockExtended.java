package farcore.lib.entity;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;
import farcore.lib.block.IExtendedDataBlock;
import farcore.lib.block.IHitByFallenBehaviorBlock;
import farcore.lib.block.ISmartFallableBlock;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.util.Log;
import farcore.network.IDescribable;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFallingBlockExtended extends Entity implements IDescribable
{
	public static boolean canFallAt(World world, BlockPos pos, IBlockState target)
	{
		if(pos.getY() < 0) return true;
		pos = pos.add(0, -1, 0);
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof IHitByFallenBehaviorBlock)
			return !((IHitByFallenBehaviorBlock) state.getBlock()).isPermeatableBy(world, pos, state, target);
		return (state.getBlock().isAir(state, world, pos) ||
				state.getBlock() instanceof IFluidBlock ||
				state.getBlock().isReplaceable(world, pos));
	}
	
	public static void replaceFallingBlock(World world, BlockPos pos, IBlockState state, int height)
	{
		IBlockState hited = world.getBlockState(pos);
		if(!hited.getBlock().isAir(hited, world, pos))
		{
			if(hited.getBlock() instanceof IHitByFallenBehaviorBlock &&
					((IHitByFallenBehaviorBlock) hited.getBlock()).onFallingOn(world, pos, hited, state, height))
				return;
			hited.getBlock().breakBlock(world, pos, hited);
			if(!hited.getBlock().isReplaceable(world, pos))
			{
				U.Worlds.spawnDropsInWorld(world, pos, hited.getBlock().getDrops(world, pos, hited, 0));
			}
		}
	}
	
	public boolean shouldDropItem = true;
	
	private IBlockState state;
	private NBTTagCompound nbt;
	private int startX;
	private int startY;
	private int startZ;
	private int lifeTime;
	@SideOnly(Side.CLIENT)
	private BlockPos pos;
	private boolean hitEntity;
	
	private List<EntityPlayer> list = new ArrayList(4);
	
	public EntityFallingBlockExtended(World world)
	{
		super(world);
	}
	public EntityFallingBlockExtended(World world, BlockPos pos, BlockPos pos1, IBlockState state, TileEntity tile)
	{
		super(world);
		try
		{
			this.preventEntitySpawning = true;
			this.startX = pos.getX();
			this.startY = pos.getY();
			this.startZ = pos.getZ();
			this.state = state;
			setSize(0.98F, 0.98F);
			setPosition(pos1.getX() + .5, pos1.getY() + (double)((1.0F - this.height) / 2.0F), pos1.getZ() + .5);
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.prevPosX = this.startX;
			this.prevPosY = this.startY;
			this.prevPosZ = this.startZ;
			if(tile != null)
			{
				tile.writeToNBT(this.nbt = new NBTTagCompound());
			}
		}
		catch(Exception exception)
		{
			Log.error("Fail to create a new falling block, disable this falling action.", exception);
			setDead();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public BlockPos getOrigin()
	{
		return this.pos;
	}
	
	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override
	protected void entityInit() {}
	
	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}
	
	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if(this.worldObj.isRemote && this.state == null)
		{
			FarCore.network.sendToServer(new PacketEntityAsk(this));
			return;
		}
		if(this.isDead)
		{
			;
		}
		else
		{
			if(this.state == null || this.state.getMaterial() == Material.AIR)
			{
				setDead();
				return;
			}
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.lifeTime++;
			this.motionY -= 0.04D;
			moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.98D;
			this.motionY *= 0.98D;
			this.motionZ *= 0.98D;
			BlockPos pos = new BlockPos(this);
			if (!this.worldObj.isRemote)
			{
				if (this.lifeTime == 1)
				{
					//					if (worldObj.getBlockState(pos = new BlockPos(startX, startY, startZ)).getBlock() != state.getBlock())
					//					{
					//						setDead();
					//						return;
					//					}
					
					if(this.state.getBlock() instanceof ISmartFallableBlock)
					{
						((ISmartFallableBlock) this.state.getBlock()).onStartFalling(this.worldObj, pos);
					}
					
					this.worldObj.setBlockToAir(pos);
				}
				
				for(EntityPlayer player : this.list)
				{
					FarCore.network.sendToPlayer(new PacketEntity(this), player);
				}
				this.list.clear();
				if (this.onGround)
				{
					this.motionX *= 0.7D;
					this.motionZ *= 0.7D;
					this.motionY *= -0.5D;
					
					setDead();
					
					label:
						if (this.worldObj.canBlockBePlaced(this.state.getBlock(), pos, true, EnumFacing.UP, (Entity)null, (ItemStack)null) &&
								((this.state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) this.state.getBlock()).canFallingBlockStay(this.worldObj, pos, this.state)) ||
										!canFallAt(this.worldObj, pos, this.state)))
						{
							if(this.state.getBlock() instanceof ISmartFallableBlock)
								if(((ISmartFallableBlock) this.state.getBlock()).onFallOnGround(this.worldObj, pos, this.state, this.startY - pos.getY(), this.nbt))
								{
									break label;
								}
							replaceFallingBlock(this.worldObj, pos, this.state, this.startY - pos.getY());
							this.worldObj.setBlockState(pos, this.state, 3);
							if (this.nbt != null)
							{
								TileEntity tile = this.worldObj.getTileEntity(pos);
								
								if (tile != null)
								{
									tile.readFromNBT(this.nbt);
									tile.setPos(pos);
									tile.markDirty();
								}
							}
						}
						else if (this.shouldDropItem)
							if(this.state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) this.state.getBlock()).onDropFallenAsItem(this.worldObj, pos, this.state, this.nbt))
							{
								
							}
							else
							{
								entityDropItem(new ItemStack(this.state.getBlock(), 1, this.state.getBlock().damageDropped(this.state)), 0.0F);
							}
				}
				else if (this.lifeTime > 100 && !this.worldObj.isRemote && (pos.getY() < 1 || pos.getY() > 256) || this.lifeTime > 600)
				{
					if (this.shouldDropItem)
						if(this.state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) this.state.getBlock()).onDropFallenAsItem(this.worldObj, pos, this.state, this.nbt))
						{
							
						}
						else
						{
							entityDropItem(new ItemStack(this.state.getBlock(), 1, this.state.getBlock().damageDropped(this.state)), 0.0F);
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
			ArrayList<Entity> arraylist = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()));
			
			float amount;
			for(Entity entity : arraylist)
			{
				amount = this.state.getBlock() instanceof ISmartFallableBlock ? ((ISmartFallableBlock) this.state.getBlock()).onFallOnEntity(this.worldObj, this, entity) : 2.0F;
				if(amount > 0)
				{
					entity.attackEntityFrom(DamageSource.fallingBlock,
							Math.min(MathHelper.floor_float(i * amount), 100F));
				}
				this.hitEntity = true;
			}
		}
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setString("block", this.state == null ? Blocks.AIR.getRegistryName().toString() :
			this.state.getBlock().getRegistryName().toString());
		if(this.state.getBlock() instanceof IExtendedDataBlock)
		{
			nbt.setInteger("data1", ((IExtendedDataBlock) this.state.getBlock()).getDataFromState(this.state));
		}
		else
		{
			nbt.setByte("data", (byte) this.state.getBlock().getMetaFromState(this.state));
		}
		nbt.setByte("time", (byte) this.lifeTime);
		nbt.setBoolean("drop", this.shouldDropItem);
		nbt.setBoolean("hit", this.hitEntity);
		nbt.setShort("startY", (short) this.startY);
		if (this.nbt != null)
		{
			nbt.setTag("tile", this.nbt);
		}
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		Block block;
		if (nbt.hasKey("block", 99))
		{
			block = Block.getBlockFromName(nbt.getString("block"));
			if(block == null)
			{
				setDead();
			}
		}
		else
		{
			setDead();
			return;
		}
		if(block instanceof IExtendedDataBlock)
		{
			this.state = ((IExtendedDataBlock) block).getStateFromData(nbt.getInteger("data1"));
		}
		else
		{
			this.state = block.getStateFromMeta(nbt.getByte("data") & 255);
		}
		this.lifeTime = nbt.getByte("time") & 255;
		
		this.hitEntity = nbt.getBoolean("hit");
		this.startY = nbt.getShort("startY");
		
		if (nbt.hasKey("drop", 99))
		{
			this.shouldDropItem = nbt.getBoolean("drop");
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
	
	@Override
	public void addEntityCrashInfo(CrashReportCategory category)
	{
		super.addEntityCrashInfo(category);
		category.addCrashSection("Immitating block name", this.state.getBlock().getUnlocalizedName());
		category.addCrashSection("Immitating block data", this.state.getProperties().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}
	
	/**
	 * Return whether this entity should be rendered as on fire.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire()
	{
		return false;
	}
	
	public IBlockState getBlock()
	{
		return this.state;
	}
	
	@Override
	public boolean ignoreItemEntityData()
	{
		return true;
	}
	
	@Override
	public NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt)
	{
		nbt.setString("block", this.state.getBlock().getRegistryName().toString());
		if(this.state.getBlock() instanceof IExtendedDataBlock)
		{
			nbt.setInteger("data1", ((IExtendedDataBlock) this.state.getBlock()).getDataFromState(this.state));
		}
		else
		{
			nbt.setByte("data", (byte) this.state.getBlock().getMetaFromState(this.state));
		}
		nbt.setLong("origin", new BlockPos(this).toLong());
		return nbt;
	}
	
	@Override
	public void readDescriptionsFromNBT(NBTTagCompound nbt)
	{
		Block block = Block.getBlockFromName(nbt.getString("block"));
		if(block == null)
		{
			this.state = Blocks.AIR.getDefaultState();
		}
		else
		{
			if(block instanceof IExtendedDataBlock)
			{
				this.state = ((IExtendedDataBlock) block).getStateFromData(nbt.getInteger("data1"));
			}
			else
			{
				this.state = block.getStateFromMeta(nbt.getByte("data") & 255);
			}
		}
		this.pos = BlockPos.fromLong(nbt.getLong("origin"));
	}
	
	@Override
	public void markNBTSync(EntityPlayer player)
	{
		this.list.add(player);
	}
}