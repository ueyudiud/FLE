/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.chest;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import fle.api.item.IIDKeyItem;
import fle.api.tile.IIDOpenableTile;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.environment.EnviornmentBlockPos;
import nebula.common.gui.Container03TileEntity;
import nebula.common.inventory.IItemContainer;
import nebula.common.item.IUpdatableItem;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockPlacedBy;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.TE06HasGui;
import nebula.common.util.Direction;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import nebula.common.util.Players;
import nebula.common.util.TileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author ueyudiud
 */
public abstract class TEChest extends TE06HasGui<Container03TileEntity> implements INetworkedSyncTile, IIDOpenableTile, ITP_Drops, ITB_BlockActived, ITB_BlockPlacedBy
{
	//	protected final IItemHandler	handler	= InventoryWrapFactory.wrap(getName(), this);
	protected final TimeMarker		marker	= new TimeMarker(200, this::checkRangePlayers);
	
	protected final boolean	portable;
	protected final boolean	hasLock;
	protected long			lockID	= IIDOpenableTile.EMPTY_UUID;
	
	protected int numPlayersUsing;
	
	/** The current angle of the lid (between 0 and 2) */
	public float	lidAngle;
	/** The angle of the lid last tick */
	public float	prevLidAngle;
	
	public EnumFacing facing = EnumFacing.NORTH;
	
	public TEChest(boolean portable, boolean hasLock)
	{
		this.portable = portable;
		this.hasLock = hasLock;
	}
	
	@Override
	public abstract String getName();
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTs.setEnum(compound, "facing", this.facing);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.facing = NBTs.getEnumOrDefault(compound, "facing", EnumFacing.NORTH);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		NBTs.setEnum(nbt, "f", this.facing);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.facing = NBTs.getEnumOrDefault(nbt, "f", EnumFacing.NORTH);
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 1:
			this.numPlayersUsing = buf.readInt();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 1:
			buf.writeInt(this.numPlayersUsing);
			break;
		default:
			break;
		}
	}
	
	@Override
	public long getOpenUUID()
	{
		return this.hasLock ? this.lockID : IIDOpenableTile.EMPTY_UUID;
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		this.facing = placer.getHorizontalFacing();
		readFromItemStackNBT(ItemStacks.getSubOrSetupNBT(stack, "chest", false));
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (tileGUICheck(hand))
		{
			if (this.hasLock && this.lockID != IIDOpenableTile.EMPTY_UUID && !Players.isOp(player))
			{
				if (stack != null)
				{
					Item item = stack.getItem();
					if (item instanceof IIDKeyItem && canOpenTileEntity(stack, (IIDKeyItem) item))
					{
						openGUI(player, 0);
						return EnumActionResult.SUCCESS;
					}
				}
				return EnumActionResult.FAIL;
			}
			openGUI(player, 0);
			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.SUCCESS;
		}
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		updateItems();
		this.marker.onUpdate();
	}
	
	@Override
	protected void updateClient()
	{
		super.updateClient();
		updateLidAngle();
	}
	
	protected void updateItems()
	{
		EnviornmentBlockPos enviornment = new EnviornmentBlockPos(this);
		for (IItemContainer container : this.items.getContainers())
		{
			ItemStack stack = container.getStackInContainer();
			if (stack != null && stack.getItem() instanceof IUpdatableItem)
			{
				container.setStackInContainer(ItemStacks.valid(((IUpdatableItem) stack.getItem()).updateItem(enviornment, stack)));
			}
		}
	}
	
	private void updateLidAngle()
	{
		this.prevLidAngle = this.lidAngle;
		
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
		{
			this.world.playSound(null, this.pos.getX() + .5, this.pos.getY() + .5, this.pos.getZ() + .5, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}
		
		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
		{
			float f2 = this.lidAngle;
			
			if (this.numPlayersUsing > 0)
			{
				this.lidAngle += 0.1F;
			}
			else
			{
				this.lidAngle -= 0.1F;
			}
			
			if (this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}
			
			if (this.lidAngle < 0.5F && f2 >= 0.5F)
			{
				this.world.playSound(null, this.pos.getX() + .5, this.pos.getY() + .5, this.pos.getZ() + .5, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			}
			
			if (this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}
	}
	
	private void checkRangePlayers()
	{
		if (this.numPlayersUsing != 0)
		{
			this.numPlayersUsing = 0;
			for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.add(-5, -5, -5), this.pos.add(5, 5, 5))))
			{
				if (entityplayer.openContainer instanceof Container03TileEntity<?>)
				{
					if (((Container03TileEntity<?>) entityplayer.openContainer).tile == this)
					{
						++ this.numPlayersUsing;
					}
				}
			}
		}
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		super.openInventory(player);
		if (!player.isSpectator())
		{
			this.numPlayersUsing++;
			
			NebulaSynchronizationHandler.markTileEntityForUpdate(this, 1);
			this.world.notifyBlockOfStateChange(this.pos, getBlockType());
			this.world.notifyBlockOfStateChange(this.pos.down(), getBlockType());
		}
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
		super.closeInventory(player);
		if (!player.isSpectator() && !isInvalid())
		{
			this.numPlayersUsing--;
			
			NebulaSynchronizationHandler.markTileEntityForUpdate(this, 1);
			this.world.notifyBlockOfStateChange(this.pos, getBlockType());
			this.world.notifyBlockOfStateChange(this.pos.down(), getBlockType());
		}
	}
	
	//	@Override
	//	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	//	{
	//		return capability == Capabilities.CAPABILITY_ITEM;
	//	}
	//
	//	@Override
	//	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	//	{
	//		return capability == Capabilities.CAPABILITY_ITEM ? Capabilities.CAPABILITY_ITEM.cast(this.handler) : null;
	//	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, getBlockMetadata());
		writeToItemStackNBT(ItemStacks.getSubOrSetupNBT(stack, "chest", true));
		return Lists.newArrayList(stack);
	}
	
	protected void writeToItemStackNBT(NBTTagCompound compound)
	{
		if (this.portable)
		{
			this.items.writeTo(compound, "items");
		}
	}
	
	protected void readFromItemStackNBT(NBTTagCompound compound)
	{
		if (this.portable)
		{
			this.items.readFrom(compound, "items");
		}
	}
	
	@Override
	public void onBlockBreak(IBlockState state)
	{
		if (!this.portable)
			TileEntities.dropItemStacks(this);
	}
}
