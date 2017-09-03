package nebula.common.gui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import nebula.Nebula;
import nebula.base.Node;
import nebula.common.fluid.FluidStackExt;
import nebula.common.fluid.container.IItemFluidContainer;
import nebula.common.network.packet.PacketFluidUpdateAll;
import nebula.common.network.packet.PacketFluidUpdateSingle;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class ContainerBase extends Container implements IGuiActionListener
{
	public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters)
	{
		return stack == null ||
				slotIn.isItemValid(stack) && (!slotIn.getHasStack() ||
						((slotIn != null && slotIn.getHasStack() && ItemStacks.isItemAndTagEqual(stack, slotIn.getStack())) &&
								(slotIn.getStack().stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= Math.min(slotIn.getItemStackLimit(stack), stack.getMaxStackSize()))));
	}
	public static ItemStack extractStackFrom(@Nullable EntityPlayer player, SlotBase slotIn, int maxSize, boolean simulate)
	{
		if(player != null)
		{
			if(!slotIn.canTakeStack(player)) return null;
		}
		if(slotIn.getHasStack())
		{
			ItemStack stack2 = slotIn.getStack();
			int size = Math.min(stack2.getMaxStackSize(), stack2.stackSize);
			ItemStack stack3;
			if(!simulate)
			{
				stack3 = stack2.splitStack(size);
				ItemStack stack4 = stack2.copy();
				if(stack2.stackSize == 0)
				{
					slotIn.putStack(null);
				}
				stack3.stackSize = size;
				slotIn.onSlotChange(stack3, stack4);
				if(player != null)
				{
					slotIn.onPickupFromSlot(player, stack3);
				}
				else
				{
					slotIn.onSlotChanged();
				}
			}
			else
			{
				stack3 = stack2.copy();
				stack3.stackSize = size;
			}
			return stack3;
		}
		return null;
	}
	public static int extractStackFrom(@Nullable EntityPlayer player, SlotBase slotIn, ItemStack stack, boolean simulate)
	{
		stack = ItemStacks.valid(stack);
		if(stack == null) return 0;
		if(player != null)
		{
			if(!slotIn.canTakeStack(player)) return 0;
		}
		if(slotIn.getHasStack())
		{
			ItemStack stack2 = slotIn.getStack();
			if(ItemStacks.isItemAndTagEqual(stack, stack2))
			{
				int size = Math.min(stack.getMaxStackSize() - stack.stackSize, stack2.stackSize);
				if(!simulate)
				{
					ItemStack stack3 = stack2.copy();
					ItemStack stack4 = stack2.copy();
					stack.stackSize += size;
					stack2.stackSize -= size;
					if(stack2.stackSize == 0)
					{
						slotIn.putStack(null);
					}
					stack3.stackSize = size;
					slotIn.onSlotChange(stack3, stack4);
					if(player != null)
					{
						slotIn.onPickupFromSlot(player, stack3);
					}
					else
					{
						slotIn.onSlotChanged();
					}
				}
				return size;
			}
		}
		return 0;
	}
	public static int injectStackIn(@Nullable EntityPlayer player, SlotBase slotIn, ItemStack stack, boolean simulate)
	{
		stack = ItemStacks.valid(stack);
		if(stack == null) return 0;
		if(!slotIn.isItemValid(stack)) return 0;
		if(player != null)
		{
			if(!slotIn.canPutStack(player, stack)) return 0;
		}
		int max = Math.min(stack.getMaxStackSize(), slotIn.getItemStackLimit(stack));
		if(slotIn.getHasStack())
		{
			ItemStack stack2 = slotIn.getStack();
			if(!ItemStacks.isItemAndTagEqual(stack, stack2)) return 0;
			int size = Math.min(max - stack2.stackSize, stack.stackSize);
			if(size <= 0) return 0;
			if(!simulate)
			{
				stack2.stackSize += size;
				stack.stackSize -= size;
			}
			return size;
		}
		else
		{
			int size = Math.min(max, stack.stackSize);
			if(size <= 0) return 0;
			if(!simulate)
			{
				slotIn.putStack(stack.splitStack(size));
				slotIn.onSlotChanged();
			}
			return size;
		}
	}
	
	//	private static final byte START    = 0x0;
	//	private static final byte ADD_SLOT = 0x1;
	//	private static final byte END      = 0x2;
	//
	//	private static final byte EVENLY_SPLIT     = 0x0;
	//	private static final byte ONE_ITEM_BY_SLOT = 0x1;
	//	private static final byte NOT_USED         = 0x2;
	//
	//	private static final byte TOTAL_PICK = 0x0;
	//	private static final byte SPLIT_PICK = 0x1;
	
	protected TL locationPlayer;
	protected TL locationBag;
	protected TL locationHand;
	
	protected boolean isClosed;
	
	protected EntityPlayer opener;
	protected List<TL> transferLocates = new ArrayList<>();
	protected List<FluidSlot> fluidSlots = new ArrayList<>();
	private List<FluidStack> lastFluidStacks = new ArrayList<>();
	
	public ContainerBase(EntityPlayer player)
	{
		this.opener = player;
	}
	
	public EntityPlayer getOpener()
	{
		return this.opener;
	}
	
	public List<FluidSlot> getFluidSlots()
	{
		return this.fluidSlots;
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		if(listener instanceof EntityPlayerMP)
		{
			Nebula.network.sendToPlayer(new PacketFluidUpdateAll(this), (EntityPlayerMP) listener);
		}
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < this.fluidSlots.size(); ++i)
		{
			FluidStack stack = this.fluidSlots.get(i).getStackInSlot();
			FluidStack stack1 = this.lastFluidStacks.get(i);
			
			if (!FluidStackExt.areFluidStackEqual(stack, stack1))
			{
				stack1 = FluidStackExt.copyOf(stack);
				this.lastFluidStacks.set(i, stack1);
				
				for (int j = 0; j < this.listeners.size(); ++j)
				{
					IContainerListener listener = this.listeners.get(j);
					if(listener instanceof EntityPlayerMP)
					{
						Nebula.network.sendToPlayer(new PacketFluidUpdateSingle(this, i, stack1), (EntityPlayer) listener);
					}
				}
			}
		}
	}
	
	protected void addOpenerSlots()
	{
		int id = this.inventorySlots.size();
		this.locationBag = new TL(id, id + 27, false).addToList();
		this.locationHand = new TL(id + 27, id + 36, false).addToList();
		this.locationPlayer = new TL(id, id + 36, false);
		addOpenerBagSlots();
		addOpenerHandSlots();
	}
	
	protected void addOpenerBagSlots()
	{
		addOpenerBagSlots(8, 84);
	}
	protected void addOpenerBagSlots(int x, int y)
	{
		addStandardSlotMatrix(this.opener.inventory, x, y, 9, 3, 9, 18, 18);
	}
	protected void addOpenerHandSlots()
	{
		addOpenerHandSlots(8, 142);
	}
	protected void addOpenerHandSlots(int x, int y)
	{
		addStandardSlotMatrix(this.opener.inventory, x, y, 9, 1, 0, 18, 18);
	}
	protected void addStandardSlotMatrix(IInventory inventory, int x, int y, int widthSlot, int heightSlot, int offSlot, int spacingU, int spacingV)
	{
		for(int i = 0; i < heightSlot; ++i)
		{
			for(int j = 0; j < widthSlot; ++j)
			{
				addSlotToContainer(new SlotBase(inventory, offSlot + i * widthSlot + j, x + j * spacingU, y + i * spacingV));
			}
		}
	}
	protected void addOutputSlotMatrix(IInventory inventory, int x, int y, int widthSlot, int heightSlot, int offSlot, int spacingU, int spacingV)
	{
		for(int i = 0; i < heightSlot; ++i)
		{
			for(int j = 0; j < widthSlot; ++j)
			{
				addSlotToContainer(new SlotOutput(inventory, offSlot + i * widthSlot + j, x + j * spacingU, y + i * spacingV));
			}
		}
	}
	
	protected FluidSlot addSlotToContainer(FluidSlot slot)
	{
		slot.slotNumber = this.inventorySlots.size();
		this.fluidSlots.add(slot);
		this.lastFluidStacks.add((FluidStack) null);
		return slot;
	}
	
	@Override
	protected Slot addSlotToContainer(Slot slotIn)
	{
		if(!(slotIn instanceof SlotBase))
			throw new IllegalArgumentException("The slot must extended by SlotBase.");
		return super.addSlotToContainer(slotIn);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		this.isClosed = true;
	}
	
	@Override
	public boolean canDragIntoSlot(Slot slotIn)
	{
		return !(slotIn instanceof SlotTool);
	}
	
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn)
	{
		return !(slotIn instanceof SlotTool);
	}
	
	protected ItemStack onToolClick(@Nullable ItemStack tool, IInventory inventoryBelong, int index)
	{
		return tool;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		switch (clickTypeIn)
		{
		case PICKUP :
		case QUICK_MOVE :
			resetDrag();
			Slot slot;
			if (slotId >= 0 && (slot = getSlot(slotId)) instanceof SlotTool)
			{
				ItemStack stack = onToolClick(player.inventory.getItemStack(), slot.inventory, slot.getSlotIndex());
				if (stack != null && stack.stackSize <= 0)
				{
					player.inventory.setItemStack(null);
				}
				return stack;
			}
			break;
		default :
			if (slotId >= 0 && getSlot(slotId) instanceof SlotTool)
			{
				return null;
			}
			break;
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack().copy();
			itemstack = itemstack1.copy();
			TL tl = L.get(this.transferLocates, label->label.contain(index));
			if (tl != null)
			{
				if(!tl.tryTransferItemStack(itemstack1))
					return null;
				if (itemstack1.stackSize == itemstack.stackSize)
				{
					slot.onSlotChanged();
					return null;
				}
				else
				{
					/**
					 * Shift click always can not check decr in inventory for some uses,
					 * for this, I use decr stack size instead of slot.onSlotChanged().
					 */
					slot.decrStackSize(itemstack.stackSize - itemstack1.stackSize);
				}
				slot.onPickupFromSlot(playerIn, itemstack1);
			}
			else return null;
		}
		return itemstack;
	}
	
	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean flag = false;
		int i = reverseDirection ? endIndex - 1 : startIndex;
		if (stack.isStackable())
		{
			while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
			{
				Slot slot = this.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();
				if (itemstack != null && ItemStacks.isItemAndTagEqual(stack, itemstack))
				{
					int maxSize = Math.min(slot.getItemStackLimit(itemstack), stack.getMaxStackSize());
					if(itemstack.stackSize >= maxSize)
					{
						if (reverseDirection) --i; else ++i;
						continue;
					}
					int j = itemstack.stackSize + stack.stackSize;
					if (j <= maxSize)
					{
						stack.stackSize = 0;
						itemstack.stackSize = j;
						slot.onSlotChanged();
						flag = true;
						break;
					}
					else if (itemstack.stackSize < maxSize)
					{
						stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
						itemstack.stackSize = stack.getMaxStackSize();
						slot.onSlotChanged();
						flag = true;
					}
				}
				if (reverseDirection) --i; else ++i;
			}
		}
		
		if (stack.stackSize > 0)
		{
			i = reverseDirection ? endIndex - 1 : startIndex;
			while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)
			{
				Slot slot1 = this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();
				int maxSize = Math.min(slot1.getItemStackLimit(stack), stack.getMaxStackSize());
				if (itemstack1 == null && slot1.isItemValid(stack))
				{
					if(stack.stackSize <= maxSize)
					{
						slot1.putStack(stack.copy());
						slot1.onSlotChanged();
						stack.stackSize = 0;
						flag = true;
						break;
					}
					else
					{
						slot1.putStack(stack.splitStack(maxSize));
						slot1.onSlotChanged();
						flag = true;
					}
				}
				if (reverseDirection)
				{
					--i;
				}
				else
				{
					++i;
				}
			}
		}
		return flag;
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		
	}
	
	/**
	 * The transfer location, use for shift click transfer items.
	 * @author ueyudiud
	 */
	public class TL
	{
		Node<TL> transferTargets;
		int startId;
		int endId;
		boolean reverseDirection;
		
		public TL(int id)
		{
			this(id, id + 1, false);
		}
		public TL(int startId, int endId)
		{
			this(startId, endId, false);
		}
		public TL(int startId, int endId, boolean reverseDirection)
		{
			this.startId = startId;
			this.endId = endId;
			this.reverseDirection = reverseDirection;
		}
		
		public TL addToList()
		{
			ContainerBase.this.transferLocates.add(this);
			return this;
		}
		
		public TL appendTransferLocate(TL transferLocate)
		{
			if(this.transferTargets == null)
			{
				this.transferTargets = Node.first(transferLocate);
			}
			else
			{
				this.transferTargets.addNext(transferLocate);
			}
			return this;
		}
		
		public boolean contain(int i)
		{
			return i >= this.startId && i < this.endId;
		}
		
		public boolean isItemValid(ItemStack stack)
		{
			return true;
		}
		
		public boolean tryTransferItemStack(ItemStack stack)
		{
			Node<TL> node = this.transferTargets;
			while (node != null)
			{
				if (node.value().mergeItemStack(stack)) return true;
				node = node.next();
			}
			return false;
		}
		
		public boolean mergeItemStack(ItemStack itemstack)
		{
			return !isItemValid(itemstack) ? false :
				ContainerBase.this.mergeItemStack(itemstack, this.startId, this.endId, this.reverseDirection);
		}
	}
	
	public class TLFluidContainerOnly extends TL
	{
		public TLFluidContainerOnly(int id)
		{
			super(id);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return stack.getItem() instanceof IItemFluidContainer ||
					stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		}
	}
}