package fle.core.net;

import java.io.IOException;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import flapi.net.FleCoordinatesPacket;
import flapi.net.FleNetworkHandler;
import flapi.te.interfaces.IObjectInWorld;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;

public class FleInventoryPacket extends FleCoordinatesPacket
{
	private ItemStack[] stacks;
	private int id = -1;
	
	public FleInventoryPacket()
	{
		super(true);
	}
	
	public FleInventoryPacket(IObjectInWorld tile)
	{
		this(A.ALL.ordinal(), tile);
	}
	
	public FleInventoryPacket(IObjectInWorld tile, int id)
	{
		this((id << 4) | A.ONE.ordinal(), tile);
	}
	
	public FleInventoryPacket(IObjectInWorld tile, int start, int end)
	{
		this((end << 16) | (start << 4) | A.RANGE.ordinal(), tile);
	}
	
	private FleInventoryPacket(int type, IObjectInWorld tile)
	{
		super(true, tile.getBlockPos());
		id = type;
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		BlockPos pos = pos();
		if(pos.getBlockTile() instanceof IInventory)
		{
			os.writeBoolean(true);
			IInventory inv = (IInventory) pos.getBlockTile();
			A a = getType();
			os.writeByte((byte) a.ordinal());
			switch(a)
			{
			case ALL :
				os.writeInt(inv.getSizeInventory());
				for(int i = 0; i < inv.getSizeInventory(); ++i)
				{
					os.writeItemStack(inv.getStackInSlot(i));
				}
				break;
			case RANGE :
				short start = (short) ((id & 0xFFF0) >> 4);
				short end = (short) (id >> 16);
				os.writeShort(start);
				os.writeShort(end);
				for(int i = start; i < end; ++i)
				{
					os.writeItemStack(inv.getStackInSlot(i));
				}
				break;
			case ONE :
				os.writeShort((short) (id & 0xFFF0));
				os.writeItemStack(inv.getStackInSlot(id & 0xFFF0));
				break;
			default :
				break;
			}
		}
		else
		{
			os.writeBoolean(false);				
		}
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException 
	{
		super.read(is);
		if(is.readBoolean())
		{
			id = is.readByte();
			A a = A.values()[id];
			switch(a)
			{
			case ALL : 
				int size = is.readInt();
				stacks = new ItemStack[size];
				for(int i = 0; i < size; ++i)
				{
					stacks[i] = is.readItemStack();
				}
				break;
			case RANGE : 
				short start = is.readShort();
				short end = is.readShort();
				id |= start << 4;
				id |= end << 16;
				stacks = new ItemStack[end - start + 1];
				for(int i = start; i < end; ++i)
				{
					stacks[i - start] = is.readItemStack();
				}
				break;
			case ONE :
				id |= is.readShort() << 4;
				stacks = new ItemStack[]{is.readItemStack()};
				break;
			default : break;
			}
		}
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		BlockPos pos = pos();
		if(pos.getBlockTile() instanceof IInventory)
		{
			IInventory inv = (IInventory) pos.getBlockTile();
			switch(getType())
			{
			case ALL:
				for(int i = 0; i < stacks.length; ++i)
				{
					inv.setInventorySlotContents(i, stacks[i]);
				}
				break;
			case RANGE:
				short start = (short) ((id & 0xFFF0) >> 4);
				short end = (short) (id >> 16);
				for(int i = start; i < end; ++i)
				{
					inv.setInventorySlotContents(i, stacks[i - start]);
				}
				break;
			case ONE:
				inv.setInventorySlotContents(id >> 4, stacks[0]);
				break;
			case NULL:
				break;
			default:
				break;
			}
		}
		return null;
	}
	
	private A getType()
	{
		return A.values()[id & 15];
	}
	
	public enum A
	{
		ALL,
		RANGE,
		ONE,
		NULL;
	}
}