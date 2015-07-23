package fla.core.network;

import io.netty.buffer.ByteBufInputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import fla.api.network.IListenerContainer;
import fla.api.recipe.ErrorType;
import fla.api.tech.Technology;
import fla.api.world.BlockPos;
import fla.core.Fla;
import fla.core.network.FlaPacket.FlaGuiPacket;
import fla.core.network.FlaPacket.FlaHeatUpdatePacket;
import fla.core.network.FlaPacket.FlaTechPacket;
import fla.core.network.FlaPacket.FlaTileUpdatePacket;

public class NetWorkManager 
{
	public NetWorkManager() 
	{
		FlaPacket.channel.register(this);
	}
	
	public void updateTileNBT(TileEntity tile)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		tile.writeToNBT(nbt);
		new FlaTileUpdatePacket(tile, nbt).sendPacket();
	}
	
	public void initiateHeatUpdate(int dimId, BlockPos pos, ForgeDirection dir, int pkg)
	{
		new FlaHeatUpdatePacket(dimId, pos, dir, pkg);
	}
	
	public void updateInventoryTileSlot(TileEntity tile, int slotId, ItemStack stack)
	{
		new FlaTileUpdatePacket(tile, slotId, stack).sendPacket();
	}
	
	public void initiateContainerError(ErrorType type, EntityPlayerMP player)
	{
		new FlaGuiPacket(player.serverPosX, player.serverPosY, player.serverPosZ, (byte) 2, (short) type.ordinal()).sendPacket(player);
	}
	
	public void initiateKeyPress(int key)
	{
		;
	}
	
	public void initiateGuiButtonPress(GuiContainer container, EntityPlayer player, int x, int y, int z, int buttonId)
	{
		;
	}
	
	public void initatePlayerTechupdate(EntityPlayerMP player, Technology tech, byte state)
	{
		new FlaTechPacket(tech, state).sendPacket(player);
	}

	@SubscribeEvent
	public void readPkt(FMLNetworkEvent.ServerCustomPacketEvent evt)
	{
		if(getClass() == NetWorkManager.class)
			onPacketData(new ByteBufInputStream(evt.packet.payload()), ((NetHandlerPlayServer)evt.handler).playerEntity);
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
			case FlaPacket.keyPacketType :
			{
				int type = is.readInt();
				Fla.fla.km.get().processKeyUpdate(player, type);
				break;
			}
			default : return;
			}
			return;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
