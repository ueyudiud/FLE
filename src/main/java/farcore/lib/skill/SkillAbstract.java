package farcore.lib.skill;

import nebula.common.LanguageManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;

public class SkillAbstract implements ISkill
{
	private final String name;
	private final int id;
	private float expIncrease;
	private float expBase;
	private float maxLevel;
	
	public SkillAbstract(String name)
	{
		this.name = name;
		this.id = REGISTER.register(name, this);
	}
	public SkillAbstract(String name, String localName)
	{
		this(name);
		LanguageManager.registerLocal("skill." + name + ".name", localName);
	}
	
	public SkillAbstract setExpInfo(int maxLv, float expBase, float expIncr)
	{
		this.maxLevel = maxLv;
		this.expBase = expBase;
		this.expIncrease = expIncr;
		return this;
	}
	
	public String getLocalName()
	{
		return LanguageManager.translateToLocal("skill." + this.name + ".name");
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.name;
	}
	
	@Override
	public int level(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if(tag.hasKey("skill"))
			return tag.getCompoundTag("skill").getCompoundTag(this.name).getByte("lv");
		return 0;
	}
	
	@Override
	public void using(EntityPlayer player, float exp)
	{
		if(exp == 0) return;
		NBTTagCompound tag = player.getEntityData();
		if(!tag.hasKey("skill"))
		{
			tag.setTag("skill", new NBTTagCompound());
		}
		tag = tag.getCompoundTag("skill");
		NBTTagCompound tag1;
		if(!tag.hasKey(this.name))
		{
			tag1 = new NBTTagCompound();
			tag.setTag(this.name, tag1);
		}
		else
		{
			tag1 = tag.getCompoundTag(this.name);
		}
		byte level = tag1.getByte("lv");
		if(level < 0)
		{
			level = 0;
		}
		if(level < this.maxLevel)
		{
			float e = tag1.getFloat("exp");
			float eNeed = (float) Math.exp(level * this.expIncrease) * this.expBase;
			e += exp;
			if(e >= eNeed)
			{
				tag1.setByte("lv", (byte) (level + 1));
				tag1.setFloat("exp", e - eNeed);
				player.sendMessage(new TextComponentString(
						LanguageManager.translateToLocal("skill.upgrade.info", getLocalName(), level, level + 1)));
			}
			else
			{
				tag1.setFloat("exp", e);
			}
		}
	}
	
	@Override
	public void set(EntityPlayer player, int level)
	{
		NBTTagCompound tag = player.getEntityData();
		if(!tag.hasKey("skill"))
		{
			tag.setTag("skill", new NBTTagCompound());
		}
		tag = tag.getCompoundTag("skill");
		NBTTagCompound tag1;
		if(!tag.hasKey(this.name))
		{
			tag1 = new NBTTagCompound();
			tag.setTag(this.name, tag1);
		}
		else
		{
			tag1 = tag.getCompoundTag(this.name);
		}
		tag1.setByte("lv", (byte) level);
	}
}