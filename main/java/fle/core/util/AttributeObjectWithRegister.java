package fle.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.registry.GameData;
import fle.api.soild.Solid;
import fle.api.soild.SolidRegistry;
import fle.core.init.IB;

public class AttributeObjectWithRegister<T> extends Attribute<T>
{
	public static class AttributeItem extends AttributeObjectWithRegister<Item>
	{
		public static final Method getItem;
		public static final Method getName;
		
		static
		{
			Method a = null;
			Method b = null;
			try
			{
				a = AttributeItem.class.getMethod("a", String.class);
				b = AttributeItem.class.getMethod("b", Item.class);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
			getItem = a;
			getName = b;
		}

		public static Item a(String name)
		{
			return GameData.getItemRegistry().getObject(name);
		}
		public static String b(Item obj)
		{
			return GameData.getItemRegistry().getNameForObject(obj);
		}
				
		public AttributeItem(String aName, int hashValue)
		{
			super(Item.class, aName, Items.stick, hashValue, getItem, getName);
		}		
	}
	public static class AttributeBlock extends AttributeObjectWithRegister<Block>
	{
		public static final Method getItem;
		public static final Method getName;
		
		static
		{
			Method a = null;
			Method b = null;
			try
			{
				a = AttributeBlock.class.getMethod("a", String.class);
				b = AttributeBlock.class.getMethod("b", Block.class);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
			getItem = a;
			getName = b;
		}

		public static Block a(String name)
		{
			return GameData.getBlockRegistry().getObject(name);
		}
		public static String b(Block obj)
		{
			return GameData.getBlockRegistry().getNameForObject(obj);
		}
				
		public AttributeBlock(String aName, int hashValue)
		{
			super(Block.class, aName, Blocks.stone, hashValue, getItem, getName);
		}		
	}
	public static class AttributeFluid extends AttributeObjectWithRegister<Fluid>
	{
		public static final Method getFluid;
		public static final Method getName;
		
		static
		{
			Method a = null;
			Method b = null;
			try
			{
				a = AttributeFluid.class.getMethod("a", String.class);
				b = AttributeFluid.class.getMethod("b", Fluid.class);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
			getFluid = a;
			getName = b;
		}

		public static Fluid a(String name)
		{
			return FluidRegistry.getFluid(name);
		}
		public static String b(Fluid obj)
		{
			return FluidRegistry.getFluidName(obj);
		}
				
		public AttributeFluid(String aName, int hashValue)
		{
			super(Fluid.class, aName, FluidRegistry.WATER, hashValue, getFluid, getName);
		}		
	}
	public static class AttributeSolid extends AttributeObjectWithRegister<Solid>
	{
		public static final Method getSolid;
		public static final Method getName;
		
		static
		{
			Method a = null;
			Method b = null;
			try
			{
				a = AttributeSolid.class.getMethod("a", String.class);
				b = AttributeSolid.class.getMethod("b", Solid.class);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
			getSolid = a;
			getName = b;
		}

		public static Solid a(String name)
		{
			return SolidRegistry.getSolidFromName(name);
		}
		public static String b(Solid obj)
		{
			return SolidRegistry.getSolidName(obj);
		}
				
		public AttributeSolid(String aName, int hashValue)
		{
			super(Solid.class, aName, IB.plant_ash, hashValue, getSolid, getName);
		}		
	}
	
	private Method getObject;
	private Method getName;
	
	protected AttributeObjectWithRegister(Class<? extends T> clazz, String aName,
			T aValue, int hashValue, Method get, Method name)
	{
		super(clazz, aName, aValue, hashValue);
		getObject = get;
		getName = name;
	}

	@Override
	public void write(DataOutputStream stream, T art) throws IOException
	{
		try 
		{
			stream.writeUTF((String) getName.invoke(null, art));
		}
		catch (Throwable e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public T read(DataInputStream stream) throws IOException
	{
		try
		{
			return (T) getObject.invoke(null, stream.readUTF());
		}
		catch (Throwable e)
		{
			throw new IOException(e);
		}
	}
}