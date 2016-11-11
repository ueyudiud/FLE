package farcore.lib.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import farcore.FarCore;
import farcore.lib.collection.Node;
import farcore.lib.fluid.FluidStackExt;
import farcore.lib.net.gui.PacketFluidUpdateAll;
import farcore.lib.net.gui.PacketFluidUpdateSingle;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class ContainerBase extends Container implements IGUIActionListener
{
	public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters)
	{
		return stack == null ||
				slotIn.isItemValid(stack) && (!slotIn.getHasStack() ||
						((slotIn != null && slotIn.getHasStack() && U.ItemStacks.isItemAndTagEqual(stack, slotIn.getStack())) &&
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
		stack = U.ItemStacks.valid(stack);
		if(stack == null) return 0;
		if(player != null)
		{
			if(!slotIn.canTakeStack(player)) return 0;
		}
		if(slotIn.getHasStack())
		{
			ItemStack stack2 = slotIn.getStack();
			if(U.ItemStacks.isItemAndTagEqual(stack, stack2))
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
		stack = U.ItemStacks.valid(stack);
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
			if(!U.ItemStacks.isItemAndTagEqual(stack, stack2)) return 0;
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
	
	private static final byte START    = 0x0;
	private static final byte ADD_SLOT = 0x1;
	private static final byte END      = 0x2;
	
	private static final byte EVENLY_SPLIT     = 0x0;
	private static final byte ONE_ITEM_BY_SLOT = 0x1;
	private static final byte NOT_USED         = 0x2;
	
	private static final byte TOTAL_PICK = 0x0;
	private static final byte SPLIT_PICK = 0x1;
	
	protected TL locationPlayer;
	protected TL locationBag;
	protected TL locationHand;

	protected boolean isClosed;

	protected EntityPlayer opener;
	protected List<TL> transferLocates = new ArrayList();
	private byte dragEvent;
	private byte dragMode;
	public List<FSlot> fluidSlots = new ArrayList();
	private List<FluidStack> lastFluidStacks = new ArrayList();
	private Set<Slot> dragSlots = new HashSet();
	
	public ContainerBase(EntityPlayer player)
	{
		opener = player;
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		if(listener instanceof EntityPlayerMP)
		{
			FarCore.network.sendToPlayer(new PacketFluidUpdateAll(this), (EntityPlayerMP) listener);
		}
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < fluidSlots.size(); ++i)
		{
			FluidStack stack = fluidSlots.get(i).getStackInSlot();
			FluidStack stack1 = lastFluidStacks.get(i);

			if (!FluidStackExt.areFluidStackEqual(stack, stack1))
			{
				stack1 = FluidStackExt.copyOf(stack);
				lastFluidStacks.set(i, stack1);

				for (int j = 0; j < listeners.size(); ++j)
				{
					IContainerListener listener = listeners.get(j);
					if(listener instanceof EntityPlayerMP)
					{
						FarCore.network.sendToPlayer(new PacketFluidUpdateSingle(this, i, stack1), (EntityPlayer) listener);
					}
				}
			}
		}
	}
	
	protected void addOpenerSlots()
	{
		int id = inventorySlots.size();
		locationPlayer = new TL(id, id + 27, false);
		locationHand = new TL(id + 27, id + 36, false);
		locationPlayer = new TL(id, id + 36, false);
		transferLocates.add(locationBag);
		transferLocates.add(locationHand);
		addOpenerBagSlots();
		addOpenerHandSlots();
	}
	
	protected void addOpenerBagSlots()
	{
		addOpenerBagSlots(8, 84);
	}
	protected void addOpenerBagSlots(int x, int y)
	{
		addStandardSlotMatrix(opener.inventory, x, y, 9, 3, 9, 18, 18);
	}
	protected void addOpenerHandSlots()
	{
		addOpenerBagSlots(8, 142);
	}
	protected void addOpenerHandSlots(int x, int y)
	{
		addStandardSlotMatrix(opener.inventory, x, y, 9, 1, 0, 18, 18);
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

	protected FSlot addSlotToContainer(FSlot slot)
	{
		slot.slotNumber = inventorySlots.size();
		fluidSlots.add(slot);
		lastFluidStacks.add((FluidStack) null);
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
		isClosed = true;
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
		if(clickTypeIn != ClickType.QUICK_CRAFT && dragEvent != 0)
		{
			resetDrag();
			return null;
		}
		ItemStack itemstack = null;
		InventoryPlayer inv = player.inventory;
		Slot slot;
		ItemStack itemstack1;
		switch (clickTypeIn)
		{
		case QUICK_CRAFT :
			byte i = dragEvent;
			dragEvent = (byte) getDragEvent(dragType);
			if ((i != ADD_SLOT || dragEvent != END) && i != dragEvent)
			{
				resetDrag();
			}
			else if (inv.getItemStack() == null)
			{
				resetDrag();
			}
			else
			{
				switch (dragEvent)
				{
				case START :
					dragMode = (byte) extractDragMode(dragType);
					if (isValidDragMode(dragMode, player))
					{
						dragEvent = ADD_SLOT;
						dragSlots.clear();
					}
					else
					{
						resetDrag();
					}
					break;
				case ADD_SLOT :
					slot = inventorySlots.get(slotId);
					if (slot != null && canAddItemToSlot(slot, inv.getItemStack(), true) && inv.getItemStack().stackSize > dragSlots.size() && canDragIntoSlot(slot))
					{
						dragSlots.add(slot);
					}
					break;
				case END :
					if (!dragSlots.isEmpty())
					{
						ItemStack itemstack3 = inv.getItemStack().copy();
						int j = inv.getItemStack().stackSize;
						
						for (Slot slot1 : dragSlots)
						{
							if (slot1 != null && canAddItemToSlot(slot1, inv.getItemStack(), true) && slot1.isItemValid(inv.getItemStack()) && inv.getItemStack().stackSize >= dragSlots.size() && canDragIntoSlot(slot1))
							{
								itemstack1 = itemstack3.copy();
								int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
								computeStackSize(dragSlots, dragMode, itemstack1, k);
								
								if (itemstack1.stackSize > itemstack1.getMaxStackSize())
								{
									itemstack1.stackSize = itemstack1.getMaxStackSize();
								}
								
								if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1))
								{
									itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
								}
								
								j -= itemstack1.stackSize - k;
								slot1.putStack(itemstack1);
							}
						}
						
						itemstack3.stackSize = j;
						
						if (itemstack3.stackSize <= 0)
						{
							itemstack3 = null;
						}
						inv.setItemStack(itemstack3);
					}
					resetDrag();
					break;
				default :
					resetDrag();
					break;
				}
			}
			break;
		case PICKUP :
		case QUICK_MOVE :
			if (!(dragType == 0 || dragType == 1))
			{
				break;
			}
			if (slotId == -999)
			{
				if(inv.getItemStack() != null)
				{
					switch (dragType)
					{
					case TOTAL_PICK :
						player.dropItem(inv.getItemStack(), true);
						inv.setItemStack((ItemStack) null);
						break;
					case SPLIT_PICK :
						player.dropItem(inv.getItemStack().splitStack(1), true);
						if (inv.getItemStack().stackSize == 0)
						{
							inv.setItemStack((ItemStack) null);
						}
						break;
					default:
						break;
					}
				}
			}
			else if (clickTypeIn == ClickType.QUICK_MOVE)
			{
				if (slotId < 0) return null;
				
				slot = inventorySlots.get(slotId);
				
				if (slot != null && slot.canTakeStack(player))
				{
					itemstack1 = slot.getStack();
					
					if (itemstack1 != null && itemstack1.stackSize <= 0)
					{
						slot.putStack(null);
						break;
					}
					itemstack1 = transferStackInSlot(player, slotId);
					if (itemstack1 != null)
					{
						Item item = itemstack1.getItem();
						itemstack = itemstack1.copy();
						
						if (slot.getStack() != null && slot.getStack().getItem() == item)
						{
							retrySlotClick(slotId, dragType, true, player);
						}
					}
				}
			}
			else
			{
				if (slotId < 0) return null;
				slot = inventorySlots.get(slotId);
				if (slot instanceof SlotTool)//Far core override.
				{
					itemstack1 = inv.getItemStack();
					ItemStack itemstack2 = onToolClick(itemstack1, slot.inventory, slot.getSlotIndex());
					if(itemstack2 != itemstack1)
					{
						inv.setItemStack(itemstack2);
					}
				}
				else if (slot != null)
				{
					itemstack1 = slot.getStack();
					ItemStack itemstack2 = inv.getItemStack();
					
					if (itemstack1 != null)
					{
						itemstack = itemstack1.copy();
					}
					
					if (itemstack1 == null)
					{
						if (itemstack2 != null && slot.isItemValid(itemstack2))
						{
							int l2 = dragType == TOTAL_PICK ? itemstack2.stackSize : 1;

							if (l2 > slot.getItemStackLimit(itemstack2))
							{
								l2 = slot.getItemStackLimit(itemstack2);
							}
							
							slot.putStack(itemstack2.splitStack(l2));
							
							if (itemstack2.stackSize == 0)
							{
								inv.setItemStack((ItemStack) null);
							}
						}
					}
					else if (slot.canTakeStack(player))
					{
						if (itemstack2 == null)
						{
							if (itemstack1.stackSize > 0)
							{
								int k2 = dragType == TOTAL_PICK ? itemstack1.stackSize : (itemstack1.stackSize + 1) / 2;
								inv.setItemStack(slot.decrStackSize(k2));
								
								if (itemstack1.stackSize <= 0)
								{
									slot.putStack((ItemStack) null);
								}
								
								slot.onPickupFromSlot(player, inv.getItemStack());
							}
							else
							{
								slot.putStack((ItemStack) null);
								inv.setItemStack((ItemStack) null);
							}
						}
						else if (slot.isItemValid(itemstack2))
						{
							if (U.ItemStacks.isItemAndTagEqual(itemstack1, itemstack2))
							{
								int j2 = U.L.min(dragType == TOTAL_PICK ? itemstack2.stackSize : 1, slot.getItemStackLimit(itemstack2) - itemstack1.stackSize, itemstack2.getMaxStackSize() - itemstack1.stackSize);
								
								itemstack2.splitStack(j2);
								if (itemstack2.stackSize == 0)
								{
									inv.setItemStack((ItemStack) null);
								}
								
								itemstack1.stackSize += j2;
							}
							else if (itemstack2.stackSize <= slot.getItemStackLimit(itemstack2))
							{
								slot.putStack(itemstack2);
								inv.setItemStack(itemstack1);
							}
						}
						else if (U.ItemStacks.isItemAndTagEqual(itemstack1, itemstack2) && itemstack2.getMaxStackSize() > 1)
						{
							int i2 = itemstack1.stackSize;
							
							if (i2 > 0 && i2 + itemstack2.stackSize <= itemstack2.getMaxStackSize())
							{
								itemstack2.stackSize += i2;
								itemstack1 = slot.decrStackSize(i2);
								
								if (itemstack1.stackSize == 0)
								{
									slot.putStack((ItemStack) null);
								}
								
								slot.onPickupFromSlot(player, inv.getItemStack());
							}
						}
					}
					slot.onSlotChanged();
				}
			}
			break;
		case SWAP :
			if(dragType >= 0 && dragType < 9)
			{
				Slot slot5 = inventorySlots.get(slotId);
				itemstack1 = inv.getStackInSlot(dragType);
				if (itemstack1 != null && itemstack1.stackSize <= 0)
				{
					itemstack1 = null;
					inv.setInventorySlotContents(dragType, (ItemStack) null);
				}
				if(slot5 instanceof SlotTool)//Far Core override
				{
					ItemStack itemstack2 = onToolClick(itemstack1, inv, slot5.getSlotIndex());
					if(itemstack2 != itemstack1)
					{
						inv.setInventorySlotContents(dragType, itemstack2);
					}
				}
				else
				{
					ItemStack itemstack2 = slot5.getStack();
					if (itemstack1 != null || itemstack2 != null)
					{
						if (itemstack1 == null)
						{
							if (slot5.canTakeStack(player))
							{
								inv.setInventorySlotContents(dragType, itemstack2);
								slot5.putStack(null);
								slot5.onPickupFromSlot(player, itemstack2);
							}
						}
						else if (itemstack2 == null)
						{
							if (slot5.isItemValid(itemstack1))
							{
								int k1 = slot5.getItemStackLimit(itemstack1);
								
								if (itemstack1.stackSize > k1)
								{
									slot5.putStack(itemstack1.splitStack(k1));
								}
								else
								{
									slot5.putStack(itemstack1);
									inv.setInventorySlotContents(dragType, null);
								}
							}
						}
						else if (slot5.canTakeStack(player) && slot5.isItemValid(itemstack1))
						{
							int l1 = slot5.getItemStackLimit(itemstack1);
							
							if (itemstack1.stackSize > l1)
							{
								slot5.putStack(itemstack1.splitStack(l1));
								slot5.onPickupFromSlot(player, itemstack2);
								
								if (!inv.addItemStackToInventory(itemstack2))
								{
									player.dropItem(itemstack2, true);
								}
							}
							else
							{
								slot5.putStack(itemstack1);
								inv.setInventorySlotContents(dragType, itemstack2);
								slot5.onPickupFromSlot(player, itemstack2);
							}
						}
					}
				}
			}
			break;
		case CLONE :
			if(player.capabilities.isCreativeMode && inv.getItemStack() == null && slotId >= 0)
			{
				slot = inventorySlots.get(slotId);
				if (slot != null && slot.getHasStack())
				{
					if (slot.getStack().stackSize > 0)
					{
						ItemStack itemstack6 = slot.getStack().copy();
						itemstack6.stackSize = itemstack6.getMaxStackSize();
						inv.setItemStack(itemstack6);
					}
					else
					{
						slot.putStack(null);
					}
				}
			}
			break;
		case THROW :
			slot = inventorySlots.get(slotId);
			if (slot != null && slot.getHasStack() && slot.canTakeStack(player))
			{
				ItemStack itemstack5 = slot.decrStackSize(dragType == 0 ? 1 : slot.getStack().stackSize);
				slot.onPickupFromSlot(player, itemstack5);
				player.dropItem(itemstack5, true);
			}
			break;
		case PICKUP_ALL :
			if(slotId >= 0)
			{
				Slot slot2 = inventorySlots.get(slotId);
				ItemStack itemstack4 = inv.getItemStack();

				if (itemstack4 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player)))
				{
					int i1 = dragType == 0 ? 0 : inventorySlots.size() - 1;
					int j1 = dragType == 0 ? 1 : -1;

					for (int i3 = 0; i3 < 2; ++i3)
					{
						for (int j3 = i1; j3 >= 0 && j3 < inventorySlots.size() && itemstack4.stackSize < itemstack4.getMaxStackSize(); j3 += j1)
						{
							Slot slot8 = inventorySlots.get(j3);

							if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true) && slot8.canTakeStack(player) && canMergeSlot(itemstack4, slot8) && (i3 != 0 || slot8.getStack().stackSize != slot8.getStack().getMaxStackSize()))
							{
								int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, slot8.getStack().stackSize);
								ItemStack itemstack2 = slot8.decrStackSize(l);
								itemstack4.stackSize += l;
								if (itemstack2.stackSize <= 0)
								{
									slot8.putStack(null);
								}
								slot8.onPickupFromSlot(player, itemstack2);
							}
						}
					}
				}
				detectAndSendChanges();
			}
			break;
		default : break;
		}
		return itemstack;
	}
	
	/**
	 * Reset the drag fields
	 */
	@Override
	protected void resetDrag()
	{
		dragEvent = 0;
		dragSlots.clear();
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			for(TL tl : transferLocates)
			{
				if(tl.contain(index))
				{
					if(!tl.tryTransferItemStack(itemstack1))
						return null;
					if (itemstack1.stackSize == 0)
					{
						slot.putStack((ItemStack) null);
					}
					else
					{
						slot.onSlotChanged();
					}
					if (itemstack1.stackSize == itemstack.stackSize)
						return null;
					slot.onPickupFromSlot(playerIn, itemstack1);
					break;
				}
			}
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
				Slot slot = inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();
				if (itemstack != null && U.ItemStacks.isItemAndTagEqual(stack, itemstack))
				{
					int maxSize = Math.min(slot.getItemStackLimit(itemstack), stack.getMaxStackSize());
					if(itemstack.stackSize >= maxSize)
					{
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
		
		if (stack.stackSize > 0)
		{
			i = reverseDirection ? endIndex - 1 : startIndex;
			while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)
			{
				Slot slot1 = inventorySlots.get(i);
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
	 *
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
		
		public TL appendTransferLocate(TL transferLocate)
		{
			if(transferTargets == null)
			{
				transferTargets = Node.first(transferLocate);
			}
			else
			{
				transferTargets.addNext(transferLocate);
			}
			return this;
		}
		
		public boolean contain(int i)
		{
			return i >= startId && i < endId;
		}

		public boolean isItemValid(ItemStack stack)
		{
			return true;
		}
		
		public boolean tryTransferItemStack(ItemStack stack)
		{
			Node<TL> node = transferTargets;
			while(node != null)
			{
				if(node.value().mergeItemStack(stack)) return true;
				node = node.next();
			}
			return false;
		}
		
		public boolean mergeItemStack(ItemStack itemstack)
		{
			return !isItemValid(itemstack) ? false :
				ContainerBase.this.mergeItemStack(itemstack, startId, endId, reverseDirection);
		}
	}
}