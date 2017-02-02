package nebula.common.entity;

import java.util.ArrayList;

import nebula.Log;
import nebula.common.block.IHitByFallenBehaviorBlock;
import nebula.common.block.ISmartFallableBlock;
import nebula.common.data.DataSerializers;
import nebula.common.data.Misc;
import nebula.common.util.Worlds;
import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFallingBlockExtended extends Entity
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
				Worlds.spawnDropsInWorld(world, pos, hited.getBlock().getDrops(world, pos, hited, 0));
			}
		}
	}
	
	public static final DataParameter<IBlockState> STATE = EntityDataManager.createKey(EntityFallingBlockExtended.class, DataSerializers.BLOCK_STATE);
	public static final DataParameter<BlockPos> ORGIN = EntityDataManager.createKey(EntityFallingBlockExtended.class, DataSerializers.BLOCK_POS);
	
	private static final DefaultFallableHandler INSTANCE = new DefaultFallableHandler();
	
	private static final class DefaultFallableHandler implements ISmartFallableBlock
	{
		public void onStartFalling(World world, BlockPos pos) { }
		
		public boolean canFallingBlockStay(World world, BlockPos pos, IBlockState state) { return !canFallAt(world, pos, state); }
		
		public boolean onFallOnGround(World world, BlockPos pos, IBlockState state, int height, NBTTagCompound tileNBT) { return false; }
		
		public boolean onDropFallenAsItem(World world, BlockPos pos, IBlockState state, NBTTagCompound tileNBT) { return false; }
		
		public float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target) { return 2.0F; }
	}
	
	public boolean shouldDropItem = true;
	
	private ISmartFallableBlock fallable;
	private IBlockState state;
	private NBTTagCompound nbt;
	private int startX;
	private int startY;
	private int startZ;
	private int lifeTime;
	private boolean hitEntity;
	
	public EntityFallingBlockExtended(World world)
	{
		super(world);
		this.fallable = INSTANCE;
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
			setState(state);
			this.dataManager.set(STATE, state);
			this.dataManager.set(ORGIN, pos1);
			
			setSize(0.98F, 0.98F);
			setPosition(pos1.getX() + .5, pos1.getY() + (double)((1.0F - this.height) / 2.0F), pos1.getZ() + .5);
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			if(tile != null)
			{
				tile.writeToNBT(this.nbt = new NBTTagCompound());
			}
		}
		catch(Exception exception)
		{
			Log.error("Fail to create a new falling block, disable this falling action.", exception);
			setDead();
			world.setBlockState(pos, state);
			world.setTileEntity(pos, tile);
		}
	}
	
	public void setState(IBlockState state)
	{
		this.state = state;
		this.fallable = (state.getBlock() instanceof ISmartFallableBlock) ? (ISmartFallableBlock) state.getBlock() : INSTANCE;
	}
	
	@SideOnly(Side.CLIENT)
	public BlockPos getOrigin()
	{
		return this.dataManager.get(ORGIN);
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
	protected void entityInit()
	{
		this.dataManager.register(STATE, Misc.AIR);
		this.dataManager.register(ORGIN, BlockPos.ORIGIN);
	}
	
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
		if(this.world.isRemote && (this.state == null || this.state == Misc.AIR))
		{
			this.state = this.dataManager.get(STATE);
			return;
		}
		if(this.isDead)
		{
			;
		}
		else
		{
			if(this.state == null || this.state == Misc.AIR)
			{
				setDead();
				return;
			}
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.lifeTime++;
			this.fallable.moveEntity(this.world, this);
			BlockPos pos = new BlockPos(this);
			if (!this.world.isRemote)
			{
				if (this.lifeTime == 1)
				{
					this.fallable.onStartFalling(this.world, pos);
					this.world.setBlockToAir(pos);
				}
				
				if (this.onGround)
				{
					this.motionX *= 0.7D;
					this.motionZ *= 0.7D;
					this.motionY *= -0.5D;
					
					setDead();
					
					label:
						if (this.world.canBlockBePlaced(this.state.getBlock(), pos, true, EnumFacing.UP, (Entity)null, (ItemStack)null) &&
								this.fallable.canFallingBlockStay(this.world, pos, this.state))
						{
							if(this.fallable.onFallOnGround(this.world, pos, this.state, this.startY - pos.getY(), this.nbt))
							{
								break label;
							}
							replaceFallingBlock(this.world, pos, this.state, this.startY - pos.getY());
							this.world.setBlockState(pos, this.state, 3);
							if (this.nbt != null)
							{
								TileEntity tile = this.world.getTileEntity(pos);
								
								if (tile != null)
								{
									tile.readFromNBT(this.nbt);
									tile.setPos(pos);
									tile.markDirty();
								}
							}
						}
						else if (this.shouldDropItem && !this.fallable.onDropFallenAsItem(this.world, pos, this.state, this.nbt))
						{
							entityDropItem(new ItemStack(this.state.getBlock(), 1, this.state.getBlock().damageDropped(this.state)), 0.0F);
						}
				}
				else if (this.lifeTime > 100 && !this.world.isRemote && (pos.getY() < 1 || pos.getY() > 256) || this.lifeTime > 600)
				{
					if (this.shouldDropItem && !this.fallable.onDropFallenAsItem(this.world, pos, this.state, this.nbt))
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
		int i = MathHelper.ceil(height - 1.0F);
		
		if (i > 0)
		{
			ArrayList<Entity> arraylist = new ArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()));
			
			float amount;
			for(Entity entity : arraylist)
			{
				amount = this.fallable.onFallOnEntity(this.world, this, entity);
				if(amount > 0)
				{
					entity.attackEntityFrom(DamageSource.fallingBlock,
							Math.min(MathHelper.floor(i * amount), 100F));
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
		nbt.setInteger("data", ExtendedBlockStateRegister.getStateData(this.state));
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
		if (nbt.hasKey("data", 99))
		{
			this.state = ExtendedBlockStateRegister.getStateFromData(nbt.getInteger("data"));
			if (this.state == Misc.AIR)
			{
				setDead();
				return;
			}
			setState(this.state);
		}
		else
		{
			setDead();
			return;
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
		if (this.world.isRemote)
		{
			IBlockState state = this.dataManager.get(STATE);
			if (state != null && state != this.state)
			{
				this.state = state;
			}
		}
		return this.state;
	}
	
	@Override
	public boolean ignoreItemEntityData()
	{
		return true;
	}
}