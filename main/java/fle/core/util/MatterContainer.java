package fle.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import flapi.collection.ArrayStandardStackList;
import flapi.collection.CollectionUtil;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.material.IChemCondition;
import flapi.material.IMolecular;
import flapi.material.IMolecular.EnumCountLevel;
import flapi.material.Matter;
import flapi.material.MatterReactionRegister;
import flapi.te.interfaces.IMatterContainer;
import flapi.world.BlockPos;
import fle.FLE;

public class MatterContainer implements IMatterContainer, Runnable
{	
	private IStackList<Stack<IMolecular>,IMolecular> wh = new ArrayStandardStackList();
	
	public IStackList<Stack<IMolecular>,IMolecular> getHelper()
	{
		return wh;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		Map<IMolecular, Integer> matterMap = new HashMap<IMolecular, Integer>();
		NBTTagList list = nbt.getTagList("Progress", 11);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			Matter matter = Matter.getMatterFromName(nbt1.getString("CFN"));
			int size = nbt1.getInteger("C");
			if(matter != null && size > 0)
				matterMap.put(matter, size);
		}
		wh = new ArrayStandardStackList(matterMap);
	}
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		Map<IMolecular, Integer> matterMap = CollectionUtil.asMap(wh.toArray());
		NBTTagList list = new NBTTagList();
		for(Entry<IMolecular, Integer> entry : matterMap.entrySet())
		{
			if(entry.getKey() == null) continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setString("CFN", entry.getKey().getChemName());
			nbt1.setInteger("C", entry.getValue());
			list.appendTag(nbt1);
		}
		nbt.setTag("MatterMap", list);
		return nbt;
	}
	
	private synchronized void updateMatter(BlockPos aPos, IChemCondition condition)
	{
		IStackList<Stack<IMolecular>,IMolecular> wh1 = FLE.fle.getAirConditionProvider().getAirLevel(aPos).getIonHelper(EnumCountLevel.Matter);
		int r = (int) (Math.log(Math.sqrt(condition.getTemperature()) + 1) - size() / 10);
		for(int i = 0; i < r; ++i)
			getHelper().add(wh1.randomGet());
		if(size() < 3) return;
		MatterReactionRegister.getReactionResult(condition, wh);
		//int loop = (int) Math.ceil((double) size() * condition.getTemperature() / 10000D);
		//for(int i = 0; i < loop; ++i)
		//{
		//}
	}
	
	public int size()
	{
		return wh.size();
	}
	
	public void add(Stack<IMolecular>...stacks)
	{
		wh.addAll(stacks);
	}
	
	@Override
	public Map<IMolecular, Integer> getMatterContain()
	{
		return CollectionUtil.asMap(wh.toArray());
	}

	@Override
	public void setMatterContain(Map<IMolecular, Integer> map)
	{
		wh = new ArrayStandardStackList(map);
	}
	
	public void clear()
	{
		synchronized(wh)
		{
			wh = new ArrayStandardStackList();
		}
	}
	
	BlockPos posCache;
	IChemCondition conditionCache;

	public void update(BlockPos aPos, IChemCondition condition)
	{
		posCache = aPos;
		conditionCache = condition;
		updateMatter(aPos, condition);
	}
	
	@Override
	public void run()
	{
		updateMatter(posCache, conditionCache);
	}
}