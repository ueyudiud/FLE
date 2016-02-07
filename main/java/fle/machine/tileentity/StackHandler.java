package fle.machine.tileentity;

import java.util.ArrayList;
import java.util.List;

import farcore.chem.RecipeCache;
import farcore.nbt.NBTLoader;
import farcore.nbt.NBTSaver;
import farcore.substance.SStack;
import farcore.substance.Substance;
import farcore.util.Part;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class StackHandler
{
	public static final NBTLoader LOADER = new NBTLoader()
	{		
		@Override
		public Object load(String name, NBTTagCompound tag)
		{
			StackHandler handler = new StackHandler();
			NBTTagCompound nbt1 = tag.getCompoundTag(name);
			NBTTagList list = nbt1.getTagList("contain", 10);
			for(int i = 0; i < list.tagCount(); ++i)
			{
				SStack stack = SStack.readFromNBT(list.getCompoundTagAt(i));
				if(stack != null)
					handler.stacks.add(stack);
			}
			list = nbt1.getTagList("cache", 10);
			for(int i = 0; i < list.tagCount(); ++i)
			{
				RecipeCache cache = RecipeCache.readFromNBT(list.getCompoundTagAt(i));
				if(cache != null)
					handler.caches.add(cache);
			}
			return handler;
		}
		
		@Override
		public boolean canLoad(Class clazz)
		{
			return StackHandler.class.equals(clazz);
		}
	};
	public static final NBTSaver SAVER = new NBTSaver()
	{		
		@Override
		public void save(String name, Object obj, NBTTagCompound nbt)
		{
			NBTTagCompound nbt1 = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for(SStack stack : ((StackHandler) obj).stacks)
			{
				list.appendTag(stack.writeToNBT(new NBTTagCompound()));
			}
			nbt1.setTag("contain", list);
			list = new NBTTagList();
			for(RecipeCache cache : ((StackHandler) obj).caches)
			{
				list.appendTag(cache.writeToNBT(new NBTTagCompound()));
			}
			nbt1.setTag("cache", list);
			nbt.setTag(name, nbt1);
		}
		
		@Override
		public boolean canSave(Class clazz)
		{
			return StackHandler.class.equals(clazz);
		}
	};
	
	public List<SStack> stacks = new ArrayList();
	public List<RecipeCache> caches = new ArrayList();
	
	public StackHandler()
	{
		
	}

	public void remove(Substance substance, int size)
	{
		int i = size;
		for(SStack stack : new ArrayList<SStack>(stacks))
		{
			if(stack.contain(substance))
			{
				int p = Math.min(i, stack.size);
				stack.size -= p;
				i -= p;
				if(stack.size > p)
					stack.size -= p;
				if(stack.size == 0)
					stacks.remove(stack);
				if(i == 0) break;
				continue;
			}
		}
	}

	public void remove(Substance substance, Part part, int size)
	{
		for(SStack stack : new ArrayList<SStack>(stacks))
		{
			if(stack.part == part.getEquivalencePart() && stack.getSubstance() == substance)
			{
				int p = Math.min(size, stack.size);
				size -= p;
				if(stack.size > p)
					stack.size -= p;
				if(stack.size == 0)
					stacks.remove(stack);
				return;
			}
		}
	}

	public void add(Substance substance, Part part, int size)
	{
		for(SStack stack : new ArrayList<SStack>(stacks))
		{
			if(stack.part == part.getEquivalencePart() && stack.getSubstance() == substance)
			{
				stack.size += size;
				return;
			}
		}
		stacks.add(new SStack(substance, part, size));
	}
}