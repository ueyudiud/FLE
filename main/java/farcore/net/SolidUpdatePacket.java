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
//public class SolidUpdatePacket extends FleAbstractPacket
//{
//	private int id;
//	private int tankIndex;
//	private SolidStack infos;
//	
//	public SolidUpdatePacket()
//	{
//		
//	}
//	public SolidUpdatePacket(ContainerBase container, int selectTank)
//	{
//		id = container.windowId;
//		tankIndex = selectTank;
//		infos = container.solidSlotList.get(selectTank).getStack();
//	}
//
//	@Override
//	public void read(FleDataInputStream is) throws IOException
//	{
//		id = is.readInt();
//		tankIndex = is.readInt();
//		infos = is.readSolidStack();
//	}
//
//	@Override
//	public void write(FleDataOutputStream buffer) throws IOException
//	{
//		buffer.writeInt(id);
//		buffer.writeInt(tankIndex);
//		buffer.writeSolidStack(infos);
//	}
//
//	@Override
//	public Object process(FleNetworkHandler handler)
//	{
//		EntityPlayer player = FleAPI.mod.getPlatform().getPlayerInstance();
//		if(player.openContainer.windowId == id)
//		{
//			ContainerBase container = (ContainerBase) player.openContainer;
//			container.solidSlotList.get(tankIndex).setStack(infos);
//		}
//		return null;
//	}
//}