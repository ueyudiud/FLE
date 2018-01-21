/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nebula.base.A;
import nebula.base.ObjArrayParseHelper;
import nebula.common.data.IBufferSerializer;
import nebula.common.network.PacketBufferExt;
import nebula.common.util.FluidStacks;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class ContainerDataHandlerManager
{
	private static final Map<Class<? extends ContainerBase>, int[]> MAP = new HashMap<>();
	
	private static final ClassLoader LOADER = ContainerDataHandlerManager.class.getClassLoader();
	
	public static final List<IBufferSerializer<? extends PacketBuffer, ?>> SERIALIZERS = new ArrayList<>(32);
	
	public static final IBufferSerializer<PacketBuffer, Integer> BS_INT = new IBufferSerializer.Impl<PacketBuffer, Integer>()
	{
		@Override
		public void write(PacketBuffer buffer, Integer value)
		{
			buffer.writeInt(value);
		}
		
		@Override
		public Integer read(PacketBuffer buffer) throws IOException
		{
			return buffer.readInt();
		}
	};
	public static final IBufferSerializer<PacketBuffer, ItemStack> BS_IS = new IBufferSerializer.Impl<PacketBuffer, ItemStack>()
	{
		public boolean changed(ItemStack oldValue, ItemStack newValue)
		{
			return ItemStacks.areItemAndTagEqual(oldValue, newValue);
		}
		
		@Override
		public void write(PacketBuffer buffer, ItemStack value)
		{
			buffer.writeItemStack(value);
		}
		
		@Override
		public ItemStack read(PacketBuffer buffer) throws IOException
		{
			return buffer.readItemStack();
		}
	};
	public static final IBufferSerializer<PacketBufferExt, FluidStack> BS_FS = new IBufferSerializer.Impl<PacketBufferExt, FluidStack>()
	{
		public boolean changed(FluidStack oldValue, FluidStack newValue)
		{
			return FluidStacks.areFluidStacksEqual(oldValue, newValue);
		}
		
		@Override
		public void write(PacketBufferExt buffer, FluidStack value)
		{
			buffer.writeFluidStack(value);
		}
		
		@Override
		public FluidStack read(PacketBufferExt buffer) throws IOException
		{
			return buffer.readFluidStack();
		}
	};
	
	public static void registerSerializer(IBufferSerializer<? extends PacketBuffer, ?> serializer)
	{
		assert !SERIALIZERS.contains(serializer);
		SERIALIZERS.add(SERIALIZERS.size(), serializer);
	}
	
	/**
	 * Register container synch data, this method <i>should</i> called by
	 * container class.
	 * 
	 * @param objects the format data, for each element can be
	 *            <tt>(IBufferSerializer serializer)</tt> or
	 *            <tt>(IBufferSerializer serializer, Integer size)</tt>, for add
	 *            <tt>size</tt>(default to be 1) size of serializers.
	 */
	public static void registerDatas(Object...objects)
	{
		try
		{
			Class<?> class1 = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName(), false, LOADER);
			assert ContainerBase.class.isAssignableFrom(class1);
			int[] values = new int[256];
			int size = 0;
			ObjArrayParseHelper helper = ObjArrayParseHelper.create(objects);
			while (helper.hasNext())
			{
				IBufferSerializer<? extends PacketBuffer, ?> serializer = helper.read();
				int idx = SERIALIZERS.indexOf(serializer);
				if (idx == -1) throw new IllegalStateException("The sericalizer " + serializer + " is not initalized yet.");
				int length;
				switch (length = helper.readOrSkip(1))
				{
				case 0:
					continue;
				case 1:
					values[size++] = idx;
					continue;
				default:
					Arrays.fill(values, size, size += length, idx);
					continue;
				}
			}
			MAP.put((Class<? extends ContainerBase>) class1, A.sublist(values, 0, size));
		}
		catch (ClassNotFoundException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	public static ContainerDataHandlerManager.ContainerDataHandlerHelper createHelper(ContainerBase container)
	{
		return new ContainerDataHandlerHelper(container.getClass());
	}
	
	public static class ContainerDataHandlerHelper
	{
		private int[] datas;
		
		private ContainerDataHandlerHelper(Class<? extends ContainerBase> class1)
		{
			this.datas = MAP.get(class1);
			if (this.datas == null)
				throw new RuntimeException("Missing a data controlor for " + class1);
		}
		
		public int getDataSize()
		{
			return this.datas.length;
		}
		
		public IBufferSerializer<? extends PacketBuffer, ?> getSerializer(int i)
		{
			return SERIALIZERS.get(this.datas[i]);
		}
	}
	
	static
	{
		registerSerializer(BS_INT);
		registerSerializer(BS_IS);
		registerSerializer(BS_FS);
	}
}
