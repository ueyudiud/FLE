/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import farcore.lib.skill.SkillAbstract;

/**
 * This all kinds of skill and knowledge is only for other mod using,
 * doesn't has any effect in Far Core only, but for some generally
 * knowledge or skill.
 * @author ueyudiud
 *
 */
public class KS
{
	/**
	 * Just for debug.
	 */
	public static final SkillAbstract DEBUGGING = new SkillAbstract("debug"){}.setExpInfo(100, 1F, 1F);
	
	public static final SkillAbstract DIGGING = new SkillAbstract("digging", "Digging"){}.setExpInfo(20, 20F, 0.7F);
	public static final SkillAbstract FIGHTING = new SkillAbstract("fighting", "Fighting"){}.setExpInfo(20, 16F, 0.8F);
	public static final SkillAbstract SHOOTING = new SkillAbstract("shooting", "Shooting"){}.setExpInfo(20, 16F, 0.8F);
	public static final SkillAbstract FENCING = new SkillAbstract("fencing", "Fencing"){}.setExpInfo(20, 24F, 1.0F);
	public static final SkillAbstract FISHING = new SkillAbstract("fishing", "Fishing"){}.setExpInfo(20, 24F, 1.0F);
	public static final SkillAbstract HURLING = new SkillAbstract("hurling", "Hurling"){}.setExpInfo(25, 18F, 0.9F);
	public static final SkillAbstract COOKING = new SkillAbstract("cooking", "Cooking"){}.setExpInfo(40, 30F, 0.8F);
	public static final SkillAbstract CRAFTING = new SkillAbstract("crafting", "Crafting"){}.setExpInfo(40, 36F, 0.9F);
	public static final SkillAbstract RESEARCHING = new SkillAbstract("researching", "Researching"){}.setExpInfo(50, 60F, 0.8F);
}