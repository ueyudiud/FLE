package fle.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import fle.FLE;
import fle.api.enums.EnumAtoms;
import fle.api.enums.EnumIons;
import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.material.MatterReactionRegister;
import fle.api.material.IAtoms.EnumCountLevel;
import fle.api.te.IMatterContainer;
import fle.api.util.IChemCondition;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;
import fle.api.world.BlockPos;

public class MatterContainer implements IMatterContainer, Runnable
{	
	private Random rand = new Random();

	private WeightHelper<IAtoms> wh = new WeightHelper<IAtoms>();
	
	public synchronized WeightHelper<IAtoms> getHelper()
	{
		return wh;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		Map<IAtoms, Integer> matterMap = new HashMap<IAtoms, Integer>();
		NBTTagList list = nbt.getTagList("Progress", 11);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			Matter matter = Matter.getMatterFromName(nbt1.getString("CFN"));
			int size = nbt1.getInteger("C");
			if(matter != null && size > 0)
				matterMap.put(matter, size);
		}
		wh = new WeightHelper<IAtoms>(matterMap);
	}
	public void writeToNBT(NBTTagCompound nbt)
	{
		Map<IAtoms, Integer> matterMap = wh.asMap(wh.getList());
		NBTTagList list = new NBTTagList();
		for(Entry<IAtoms, Integer> entry : matterMap.entrySet())
		{
			if(entry.getKey() == null) continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setString("CFN", entry.getKey().getChemicalFormulaName());
			nbt1.setInteger("C", entry.getValue());
			list.appendTag(nbt1);
		}
		nbt.setTag("MatterMap", list);
	}
	
	private synchronized void updateMatter(BlockPos aPos, IChemCondition condition)
	{
		WeightHelper<IAtoms> wh1 = FLE.fle.getAirConditionProvider().getAirLevel(aPos).getIonHelper(EnumCountLevel.Matter);
		int r = (int) (Math.log(Math.sqrt(condition.getTemperature() + size()) + 1));
		for(int i = 0; i < r; ++i)
			getHelper().add(wh1.randomGet());
		if(size() < 3) return;
		IAtoms a1;
		IAtoms a2;
		Stack<IAtoms>[] as;
		MatterReactionRegister.getReactionResult(condition, wh);
		//int loop = (int) Math.ceil((double) size() * condition.getTemperature() / 10000D);
		//for(int i = 0; i < loop; ++i)
		//{
		//}
	}
	
	public int size()
	{
		return wh.allWeight();
	}
	
	public void add(Stack<IAtoms>...stacks)
	{
		wh.add(stacks);
	}
	
	private final IAtoms selectMatter(Stack<IAtoms>...stacks)
	{
		return stacks[rand.nextInt(stacks.length)].getObj();
	}
	
	private final IAtoms selectMatter(IAtoms...atoms)
	{
		return atoms[rand.nextInt(atoms.length)];
	}

	@Override
	public Map<IAtoms, Integer> getMatterContain()
	{
		return WeightHelper.asMap(wh.getList());
	}

	@Override
	public void setMatterContain(Map<IAtoms, Integer> map)
	{
		wh = new WeightHelper<IAtoms>(map);
	}
	
	public void clear()
	{
		synchronized(wh)
		{
			wh = new WeightHelper<IAtoms>();
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