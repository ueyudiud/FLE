/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.skill;

import nebula.common.LanguageManager;
import nebula.common.util.EnumChatFormatting;
import nebula.common.util.NBTs;
import nebula.common.util.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;

public class SkillAbstract implements ISkill
{
	private final String	name;
	public final int		id;
	private float			expIncrease;
	private float			expBase;
	private float			maxLevel;
	
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
		return NBTs.getCompound(player.getEntityData(), "skill", false).getCompoundTag(this.name).getByte("lv");
	}
	
	private float getExperenceNeed(int lvl)
	{
		return (float) Math.exp(lvl * this.expIncrease) * this.expBase;
	}
	
	@Override
	public void using(EntityPlayer player, float exp)
	{
		if (exp == 0) return;
		NBTTagCompound tag = NBTs.getCompound(NBTs.getCompound(player.getEntityData(), "skill", true), this.name, true);
		byte level = tag.getByte("lv");
		if (level < 0)
		{
			level = 0;
		}
		if (level < this.maxLevel)
		{
			float e = tag.getFloat("exp");
			float eNeed = getExperenceNeed(level);
			e += exp;
			if (e >= eNeed)
			{
				tag.setByte("lv", (byte) (level + 1));
				tag.setFloat("exp", e - eNeed);
				player.sendMessage(new TextComponentString(LanguageManager.translateToLocal("skill.upgrade.info", getLocalName(), level, level + 1)));
			}
			else
			{
				tag.setFloat("exp", e);
			}
		}
	}
	
	@Override
	public void set(EntityPlayer player, int level)
	{
		NBTTagCompound tag = player.getEntityData();
		if (!tag.hasKey("skill"))
		{
			tag.setTag("skill", new NBTTagCompound());
		}
		tag = tag.getCompoundTag("skill");
		NBTTagCompound tag1;
		if (!tag.hasKey(this.name))
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
	
	@Override
	public String getSkillInfo(EntityPlayer player)
	{
		NBTTagCompound tag = NBTs.getCompound(player.getEntityData(), "skill", false).getCompoundTag(this.name);
		byte level = tag.getByte("lv");
		if (level < 0)
		{
			level = 0;
		}
		String expInfo;
		if (level < this.maxLevel)
		{
			float e = tag.getFloat("exp");
			float eNeed = getExperenceNeed(level);
			expInfo = "exp" + EnumChatFormatting.GREEN + Strings.getDecimalNumber(e, 1) + EnumChatFormatting.WHITE + "/" + Strings.getDecimalNumber(eNeed, 1);
		}
		else
		{
			expInfo = EnumChatFormatting.GREEN + "maxlevel";
		}
		return String.format("%s%s%s lv%s%d%s/%d %s", EnumChatFormatting.ITALIC.toString(), getLocalName(), EnumChatFormatting.RESET.toString(), EnumChatFormatting.GOLD.toString(), level, EnumChatFormatting.WHITE.toString(), (int) this.maxLevel, expInfo);
	}
}
