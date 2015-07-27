package fle.api.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import cpw.mods.fml.common.registry.GameData;

public class FleDataOutputStream 
{
	private DataOutputStream stream;
	
	public FleDataOutputStream(DataOutputStream aStream)
	{
		stream = aStream;
	}

	public void writeFloat(float f) throws IOException
	{
		stream.writeFloat(f);
	}

	public void writeDouble(double d) throws IOException
	{
		stream.writeDouble(d);
	}
	
	public void writeBoolean(boolean b) throws IOException
	{
		stream.writeBoolean(b);
	}
	
	public void writeByte(byte b) throws IOException
	{
		stream.writeByte(b);
	}

	public void writeShort(short s) throws IOException
	{
		stream.writeShort(s);
	}

	public void writeInt(int i) throws IOException
	{
		stream.writeInt(i);
	}

	public void writeLong(long l) throws IOException
	{
		stream.writeLong(l);
	}

	public void writeString(String s) throws IOException
	{
		stream.writeUTF(s);
	}

	public void writeNBT(NBTTagCompound nbt) throws IOException
	{
		if(nbt != null)
		{
			stream.writeBoolean(true);
			CompressedStreamTools.write(nbt, stream);
		}
		else
		{
			stream.writeBoolean(false);
		}
	}
	
	public void writeItem(Item item) throws IOException
	{
		stream.writeUTF(GameData.getItemRegistry().getNameForObject(item));
	}
	
	public void writeBlock(Block block) throws IOException
	{
		stream.writeUTF(GameData.getBlockRegistry().getNameForObject(block));
	}

	public void writeItemStack(ItemStack stack) throws IOException
	{
		if(stack != null)
		{
			stream.writeBoolean(true);
			writeItem(stack.getItem());
			stream.writeShort(stack.stackSize);
			stream.writeShort(stack.getItemDamage());
			writeNBT(stack.stackTagCompound);
		}
		else
		{
			stream.writeBoolean(false);
		}
	}

	public void writeFluidStack(FluidStack stack) throws IOException
	{
		if(stack != null)
		{
			stream.writeBoolean(true);
			stream.writeInt(stack.getFluidID());
			stream.writeInt(stack.amount);
			writeNBT(stack.tag);
		}
		else
		{
			stream.writeBoolean(false);
		}
	}

	public void writeFluidTank(IFluidTank tank) throws IOException
	{
		stream.writeInt(tank.getCapacity());
		writeFluidStack(tank.getFluid());
	}
	
	public void writeUUID(UUID uuid) throws IOException
	{
		stream.writeLong(uuid.getMostSignificantBits());
		stream.writeLong(uuid.getLeastSignificantBits());
	}

	public void writeWorld(World world) throws IOException
	{
		stream.writeInt(world.provider.dimensionId);
	}

	public void writeTileEntity(TileEntity tile) throws IOException 
	{
		if(tile == null)
		{
			stream.writeBoolean(false);
		}
		else
		{
			stream.writeBoolean(true);
			writeWorld(tile.getWorldObj());
			stream.writeInt(tile.xCoord);
			stream.writeShort(tile.yCoord);
			stream.writeInt(tile.zCoord);
		}
	}
	
	public void close() throws IOException
	{
		stream.close();
	}
}