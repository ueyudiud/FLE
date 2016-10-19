package farcore.lib.entity;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;
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
		return (state.getBlock().isAir(state, world, pos) ||
				state.getBlock() instanceof IFluidBlock ||
				state.getBlock().isReplaceable(world, pos));
	}

	public static void replaceFallingBlock(World world, BlockPos pos, IBlockState state)
	{
		IBlockState hited = world.getBlockState(pos);
		if(!hited.getBlock().isAir(hited, world, pos))
		{
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
			preventEntitySpawning = true;
			startX = pos.getX();
			startY = pos.getY();
			startZ = pos.getZ();
			this.state = state;
			setSize(0.98F, 0.98F);
			setPosition(pos1.getX() + .5, pos1.getY() + (double)((1.0F - height) / 2.0F), pos1.getZ() + .5);
			motionX = 0.0D;
			motionY = 0.0D;
			motionZ = 0.0D;
			prevPosX = startX;
			prevPosY = startY;
			prevPosZ = startZ;
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
	
	@SideOnly(Side.CLIENT)
	public BlockPos getOrigin()
	{
		return pos;
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
		return !isDead;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if(worldObj.isRemote && state == null)
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
			if(state == null || state.getMaterial() == Material.AIR)
			{
				setDead();
				return;
			}
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			lifeTime++;
			motionY -= 0.04D;
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.98D;
			motionY *= 0.98D;
			motionZ *= 0.98D;
			BlockPos pos = new BlockPos(this);
			if (!worldObj.isRemote)
			{
				if (lifeTime == 1)
				{
					//					if (worldObj.getBlockState(pos = new BlockPos(startX, startY, startZ)).getBlock() != state.getBlock())
					//					{
					//						setDead();
					//						return;
					//					}

					if(state.getBlock() instanceof ISmartFallableBlock)
					{
						((ISmartFallableBlock) state.getBlock()).onStartFalling(worldObj, pos);
					}

					worldObj.setBlockToAir(pos);
				}

				for(EntityPlayer player : list)
				{
					FarCore.network.sendToPlayer(new PacketEntity(this), player);
				}
				list.clear();
				if (onGround)
				{
					motionX *= 0.7D;
					motionZ *= 0.7D;
					motionY *= -0.5D;

					setDead();

					label:
						if (worldObj.canBlockBePlaced(state.getBlock(), pos, true, EnumFacing.UP, (Entity)null, (ItemStack)null) &&
								((state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) state.getBlock()).canFallingBlockStay(worldObj, pos, state)) ||
										!canFallAt(worldObj, pos, state)))
						{
							if(state.getBlock() instanceof ISmartFallableBlock)
								if(((ISmartFallableBlock) state.getBlock()).onFallOnGround(worldObj, pos, state, startY - pos.getY(), nbt))
								{
									break label;
								}
							replaceFallingBlock(worldObj, pos, state);
							worldObj.setBlockState(pos, state, 3);
							if (nbt != null)
							{
								TileEntity tile = worldObj.getTileEntity(pos);

								if (tile != null)
								{
									tile.readFromNBT(nbt);
									tile.setPos(pos);
									tile.markDirty();
								}
							}
						}
						else if (shouldDropItem)
							if(state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) state.getBlock()).onDropFallenAsItem(worldObj, pos, state, nbt))
							{

							}
							else
							{
								entityDropItem(new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state)), 0.0F);
							}
				}
				else if (lifeTime > 100 && !worldObj.isRemote && (pos.getY() < 1 || pos.getY() > 256) || lifeTime > 600)
				{
					if (shouldDropItem)
						if(state.getBlock() instanceof ISmartFallableBlock && ((ISmartFallableBlock) state.getBlock()).onDropFallenAsItem(worldObj, pos, state, nbt))
						{

						}
						else
						{
							entityDropItem(new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state)), 0.0F);
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
			ArrayList<Entity> arraylist = new ArrayList(worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()));

			float amount;
			for(Entity entity : arraylist)
			{
				amount = state.getBlock() instanceof ISmartFallableBlock ? ((ISmartFallableBlock) state.getBlock()).onFallOnEntity(worldObj, this, entity) : 2.0F;
				if(amount > 0)
				{
					entity.attackEntityFrom(DamageSource.fallingBlock,
							Math.min(MathHelper.floor_float(i * amount), 100F));
				}
				hitEntity = true;
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setString("block", state == null ? Blocks.AIR.getRegistryName().toString() :
			state.getBlock().getRegistryName().toString());
		nbt.setByte("data", (byte) state.getBlock().getMetaFromState(state));
		nbt.setByte("time", (byte) lifeTime);
		nbt.setBoolean("drop", shouldDropItem);
		nbt.setBoolean("hit", hitEntity);
		nbt.setShort("startY", (short) startY);
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

		state = block.getStateFromMeta(nbt.getByte("data") & 255);
		lifeTime = nbt.getByte("time") & 255;

		hitEntity = nbt.getBoolean("hit");
		startY = nbt.getShort("startY");

		if (nbt.hasKey("drop", 99))
		{
			shouldDropItem = nbt.getBoolean("drop");
		}

		if (nbt.hasKey("tile", 10))
		{
			this.nbt = nbt.getCompoundTag("tile");
		}
	}

	public void func_145806_a(boolean flag)
	{
		hitEntity = flag;
	}

	@Override
	public void addEntityCrashInfo(CrashReportCategory category)
	{
		super.addEntityCrashInfo(category);
		category.addCrashSection("Immitating block name", state.getBlock().getUnlocalizedName());
		category.addCrashSection("Immitating block data", state.getProperties().toString());
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
		return state;
	}

	@Override
	public boolean ignoreItemEntityData()
	{
		return true;
	}

	@Override
	public NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt)
	{
		nbt.setString("block", state.getBlock().getRegistryName().toString());
		nbt.setByte("data", (byte) state.getBlock().getMetaFromState(state));
		nbt.setLong("origin", new BlockPos(this).toLong());
		return nbt;
	}

	@Override
	public void readDescriptionsFromNBT(NBTTagCompound nbt)
	{
		Block block = Block.getBlockFromName(nbt.getString("block"));
		if(block == null)
		{
			state = Blocks.AIR.getDefaultState();
		}
		else
		{
			state = block.getStateFromMeta(nbt.getByte("data") & 255);
		}
		pos = BlockPos.fromLong(nbt.getLong("origin"));
	}

	@Override
	public void markNBTSync(EntityPlayer player)
	{
		list.add(player);
	}
}