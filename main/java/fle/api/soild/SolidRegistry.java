package fle.api.soild;

import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fle.api.util.FleLog;
import fle.api.util.Register;

public class SolidRegistry
{
	static Register<Solid> register = new Register();
	private static Map<ContainerKey, SolidContainerData> containerSolidMap = Maps.newHashMap();
    private static Map<ContainerKey, SolidContainerData> filledContainerMap = Maps.newHashMap();
    private static Set<ContainerKey> emptyContainers = Sets.newHashSet();

    public boolean hasSolid(String name)
    {
    	return register.contain(name);
    }
    
	static void registrySoild(String name, Solid soild)
	{
		if(register.contain(name))
		{
			throw new RuntimeException("FLE API: cause by registried a soild with same name.");
		}
		register.register(soild, name);
	}

    public static boolean registerSolidContainer(SolidStack stack, ItemStack filledContainer)
    {
    	return registerSolidContainer(stack, filledContainer, null);
    }
    public static boolean registerSolidContainer(SolidStack stack, ItemStack filledContainer, ItemStack emptyContainer)
    {
        return registerSolidContainer(new SolidContainerData(filledContainer, stack, emptyContainer));
    }
    
    public static boolean registerSolidContainer(SolidContainerData data)
    {
        if (isFilledContainer(data.fillStack) || data.fillStack == null)
        {
            return false;
        }
        if (data.contain == null || data.contain.getObj() == null)
        {
        	FleLog.getLogger().warn(
        			String.format("FLE API: Invalid registration attempt for a solid container item %s has occurred. "
        					+ "The registration has been denied to prevent crashes. "
        					+ "The mod responsible for the registration needs to correct this.", 
        					data.fillStack.getUnlocalizedName()));
        	return false;
        }
        containerSolidMap.put(new ContainerKey(data.fillStack), data);

        if (data.emptyStack != null)
        {
            filledContainerMap.put(new ContainerKey(data.emptyStack, data.contain), data);
            emptyContainers.add(new ContainerKey(data.emptyStack));
        }
        
        return true;
    }

    public static boolean isContainer(ItemStack container)
    {
        return isEmptyContainer(container) || isFilledContainer(container);
    }

    public static boolean isEmptyContainer(ItemStack container)
    {
        return container != null && emptyContainers.contains(new ContainerKey(container));
    }

    public static boolean isFilledContainer(ItemStack container)
    {
        return container != null && getSolidForFilledItem(container) != null;
    }
    
    public static SolidStack getSolidForFilledItem(ItemStack container)
    {
        if (container == null)
        {
            return null;
        }

        SolidContainerData data = containerSolidMap.get(new ContainerKey(container));
        
        return data == null ? null : data.contain.copy();
    }

    public static ItemStack fillSolidContainer(SolidStack solid, ItemStack container)
    {
        if (solid == null)
        {
            return null;
        }

        SolidContainerData data = filledContainerMap.get(new ContainerKey(container, solid));
        if (data != null && solid.getSize() >= data.contain.getSize())
        {
            return data.fillStack.copy();
        }
        return null;
    }
    public static ItemStack drainSolidContainer(ItemStack container)
    {
        if (container == null)
        {
            return null;
        }

        SolidContainerData data = containerSolidMap.get(new ContainerKey(container));
        if (data != null)
        {
            return data.emptyStack.copy();
        }

        return null;
    }
    public static int getContainerCapacity(ItemStack container)
    {
        return getContainerCapacity(null, container);
    }
    public static int getContainerCapacity(SolidStack solid, ItemStack container)
    {
        if (container == null && solid == null)
        {
            return 0;
        }

        SolidContainerData data = containerSolidMap.get(new ContainerKey(container));

        if (data != null)
        {
            return data.contain.getSize();
        }

        if (solid != null)
        {
            data = filledContainerMap.get(new ContainerKey(container, solid));

            if (data != null)
            {
                return data.contain.getSize();
            }
        }

        return 0;
    }
	
    public static boolean containsSolid(ItemStack container, SolidStack solid)
    {
        if (container == null || solid == null)
        {
            return false;
        }

        SolidContainerData data = containerSolidMap.get(new ContainerKey(container));
        return data == null ? false : data.contain.contain(solid);
    }
    
	public static Solid getSolidFromName(String name)
	{
		return register.contain(name) ? register.get(name) : null;
	}
	
	public static class SolidContainerData
	{
		final ItemStack emptyStack;
		final ItemStack fillStack;
		final SolidStack contain;
		
		public SolidContainerData(ItemStack e, SolidStack c, ItemStack f)
		{
			if(e != null) emptyStack = e.copy();
			else emptyStack = null;
			if(c != null) contain = c.copy();
			else contain = null;
			if(f != null) fillStack = f.copy();
			else fillStack = null;
		}
	}
	
	private static class ContainerKey
	{
        ItemStack container;
        SolidStack stack;
        
        private ContainerKey()
        {
        	this(null);
		}
		private ContainerKey(ItemStack container)
        {
			if(container != null)
            this.container = container.copy();
        }
        private ContainerKey(ItemStack container, SolidStack stack)
        {
            this(container);
            this.stack = stack.copy();
        }
		
		@Override
		public int hashCode()
		{
			int code = 31;
			if(container != null)
			{
				code = code + container.getItem().hashCode();
				code = code * 31 + container.getItemDamage();
			}
			if(stack != null)
				code = code * 31 + stack.getObj().hashCode();
			return code;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof ContainerKey)) return false;
			ContainerKey ck = (ContainerKey) obj;
			if (!(container == null && ck.container == null))
			{
				if (container == null || ck.container == null) return false;
				if (container.getItem() != ck.container.getItem()) return false;
	            if (container.getItemDamage() != ck.container.getItemDamage()) return false;
			}
            if (stack == null && ck.stack != null) return false;
            if (stack != null && ck.stack == null) return false;
            if (stack == null && ck.stack == null) return true;
            if (!stack.isStackEqul(ck.stack)) return false;
            return true;
		}
	}

	public static Iterable<Solid> getSolidList()
	{
		return register;
	}
}