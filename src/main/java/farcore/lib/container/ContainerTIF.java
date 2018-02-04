/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.container;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.solid.SolidSlot;
import farcore.lib.solid.SolidStack;
import nebula.Nebula;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.ContainerDataHandlerManager;
import nebula.common.gui.ContainerTileInventory;
import nebula.common.gui.IContainerDataHandler;
import nebula.common.network.packet.PacketContainerDataUpdateAll;
import nebula.common.network.packet.PacketContainerDataUpdateSingle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ContainerTIF<T extends TileEntity & IInventory> extends ContainerTileInventory<T>
{
	protected List<SolidSlot> solidSlots = new ArrayList<>();
	
	protected class IFContainerDataHandler<C extends ContainerBase> extends ContainerTileInventory<T>.InventoryContainerDataHandler<C>
	{
		protected SolidStack[] stacks3;
		
		@Override
		protected PacketContainerDataUpdateAll getAllDataPacket()
		{
			if (this.stacks1 == null)
			{
				this.stacks1 = new ItemStack[ContainerTIF.this.inventorySlots.size()];
				this.stacks2 = new FluidStack[ContainerTIF.this.fluidSlots.size()];
				this.stacks3 = new SolidStack[ContainerTIF.this.solidSlots.size()];
				this.values = new int[ContainerTIF.this.tile.getFieldCount()];
			}
			List<Object[]> list = new ArrayList<>(4);
			if (this.stacks1.length != 0)
				list.add(concat(ContainerDataHandlerManager.BS_IS, this.stacks1));
			if (this.stacks2.length != 0)
				list.add(concat(ContainerDataHandlerManager.BS_FS, this.stacks2));
			if (this.values.length != 0)
			{
				Integer[] value = new Integer[this.values.length];
				for (int i = 0; i < value.length; value[i] = this.values[i], ++i);
				list.add(concat(ContainerDataHandlerManager.BS_INT, value));
			}
			if (this.stacks3.length != 0)
				list.add(concat(SolidStack.BS, this.stacks3));
			return new PacketContainerDataUpdateAll(ContainerTIF.this, list.toArray(new Object[list.size()][]));
		}
		
		@Override
		public void detectAndSendChanges()
		{
			if (this.stacks3 == null)
			{
				this.stacks3 = new SolidStack[ContainerTIF.this.solidSlots.size()];
			}
			for (int i = 0; i < this.stacks3.length; ++i)
			{
				SolidStack stack1 = ContainerTIF.this.solidSlots.get(i).getStackInSlot();
				SolidStack stack2 = this.stacks3[i];
				if (!SolidStack.areStackEqual(stack1, stack2))
				{
					this.stacks3[i] = stack2 = SolidStack.copyOf(stack1);
					for (IContainerListener listener : ContainerTIF.this.listeners)
					{
						if (listener instanceof EntityPlayerMP)
						{
							Nebula.network.sendToPlayer(new PacketContainerDataUpdateSingle(ContainerTIF.this, SolidStack.BS, i, stack1), (EntityPlayer) listener);
						}
					}
				}
			}
			super.detectAndSendChanges();
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public <U> void updateValue(Class<U> type, int id, U value)
		{
			if (type == SolidStack.class)
			{
				ContainerTIF.this.solidSlots.get(id).putStack((SolidStack) value);
			}
			else super.updateValue(type, id, value);
		}
	}
	
	public ContainerTIF(T tile, EntityPlayer player)
	{
		super(tile, player);
	}
	
	@Override
	protected IContainerDataHandler createHandler()
	{
		return new IFContainerDataHandler<>();
	}
	
	protected void addSlotToContainer(SolidSlot slot)
	{
		slot.slotNumber = this.solidSlots.size();
		this.solidSlots.add(slot);
	}
	
	public List<SolidSlot> getSolidSlots()
	{
		return this.solidSlots;
	}
}
