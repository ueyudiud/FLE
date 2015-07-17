package fla.core.network;

import io.netty.buffer.ByteBufInputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import fla.api.network.IListenerContainer;
import fla.api.recipe.ErrorType;
import fla.core.Fla;
import fla.core.network.FlaPacket.FlaGuiPacket;
import fla.core.network.FlaPacket.FlaKeyPacket;

public class NetWorkClient extends NetWorkManager
{
	@Override
	public void initiateKeyPress(int key)
	{
		new FlaKeyPacket(key).sendPacket();
	}
	
	@Override
	public void updateTileNBT(TileEntity tile) 
	{
		;
	}
	
	@Override
	public void updateInventoryTileSlot(TileEntity tile, int slotId, ItemStack stack)
	{
		;
	}
	
	@Override
	public void initiateContainerError(ErrorType type, EntityPlayerMP player)
	{
		;
	}
	
	@Override
	public void initiateGuiButtonPress(GuiContainer container, EntityPlayer player, int x, int y, int z, int buttonId)
	{
		new FlaGuiPacket(x, y, z, (byte)1, (byte)buttonId).sendPacket();
	}

	@SubscribeEvent
	public void readPkt(FMLNetworkEvent.ClientCustomPacketEvent evt)
	{
		this.onPacketData(new ByteBufInputStream(evt.packet.payload()), Minecraft.getMinecraft().thePlayer);
	}
	
	protected void onPacketData(InputStream isRaw, EntityPlayer player)
	{
		try 
		{
			if (isRaw.available() == 0)
				return;
			int id = isRaw.read();
			DataInputStream is = new DataInputStream(isRaw);
			switch(id)
			{
			case FlaPacket.guiPacketType :
				if(player.openContainer instanceof IListenerContainer)
				{
					int x = is.readInt();
					int y = is.readInt();
					int z = is.readInt();
					byte b = is.readByte();
					short contain = is.readShort();
					((IListenerContainer) player.openContainer).onPacketData(x, y, z, b, contain);
				}
			break;
			case FlaPacket.tileUpdatePacketType :
				World world = Fla.fla.p.get().getWorld(is.readInt());
				int x = is.readInt();
				int y = is.readInt();
				int z = is.readInt();
				byte type = is.readByte();
				TileEntity tile = world.getTileEntity(x, y, z);
				switch(type)
				{
				case (byte) 0 :
				{
					if(tile instanceof IInventory)
					{
						int slotId = is.readByte();
						ItemStack stack = null;
						if(is.readBoolean())
						{
							Item i = (Item) Item.itemRegistry.getObject(is.readUTF());
							int size = is.readInt();
							int damage = is.readInt();
							stack = new ItemStack(i, size, damage);
							if(is.readBoolean())
							{
								NBTTagCompound nbt = CompressedStreamTools.readCompressed(is);
								stack.stackTagCompound = nbt;
							}
						}
						((IInventory) tile).setInventorySlotContents(slotId, stack);
					}
					break;
				}
				case (byte) 1 :
				{
					NBTTagCompound nbt = CompressedStreamTools.readCompressed(is);
					if(tile != null)
						tile.readFromNBT(nbt);
				}
				default : return;
				}
			}
			return;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
