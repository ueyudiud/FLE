//package flapi.net;
//
//import java.io.IOException;
//
//import net.minecraft.entity.player.EntityPlayer;
//import flapi.FleAPI;
//import flapi.gui.ContainerBase;
//import flapi.solid.SolidStack;
//import flapi.util.io.FleDataInputStream;
//import flapi.util.io.FleDataOutputStream;
//
//public class SolidWindowPacket extends FleAbstractPacket
//{
//	private int id;
//	private SolidStack[] infos;
//	
//	public SolidWindowPacket()
//	{
//		
//	}
//	public SolidWindowPacket(ContainerBase container)
//	{
//		id = container.windowId;
//		infos = new SolidStack[container.solidSlotList.size()];
//		for(int i = 0; i < infos.length; ++i)
//			infos[i] = container.solidSlotList.get(i).getStack();
//	}
//
//	@Override
//	protected void read(FleDataInputStream is) throws IOException
//	{
//		id = is.readInt();
//		infos = new SolidStack[is.readInt()];
//		for(int i = 0; i < infos.length; ++i)
//		{
//			infos[i] = is.readSolidStack();
//		}
//	}
//
//	@Override
//	protected void write(FleDataOutputStream os) throws IOException
//	{
//		os.writeInt(id);
//		os.writeInt(infos.length);
//		for(int i = 0; i < infos.length; ++i)
//		{
//			os.writeSolidStack(infos[i]);
//		}
//	}
//
//	@Override
//	public Object process(FleNetworkHandler handler)
//	{
//		EntityPlayer player = FleAPI.mod.getPlatform().getPlayerInstance();
//		if(player.openContainer.windowId == id)
//		{
//			ContainerBase container = (ContainerBase) player.openContainer;
//			for(int i = 0; i < infos.length; ++i)
//			{
//				container.solidSlotList.get(i).setStack(infos[i]);
//			}
//		}
//		return null;
//	}
//}
