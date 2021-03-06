/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.data;

import farcore.lib.material.MatCondition;
import nebula.common.util.Properties.EnumStateName;
import net.minecraft.util.IStringSerializable;

/**
 * The rock types.
 * 
 * @author ueyudiud
 */
@EnumStateName("rocktype")
public enum EnumRockType implements IStringSerializable
{
	resource(MC.stone, "", "%s"),
	cobble(MC.cobble, "standard", "Craked %s"),
	cobble_art(MC.cobble, "standard", "%s Cobble"),
	smoothed(MC.brickBlock, "smoothed", "Smoothed %s"),
	mossy(MC.cobble, "standard", "Mossy %s"),
	brick(MC.brickBlock, "standard", "%s Brick"),
	brick_crushed(MC.brickBlock, "crushed", "Cracked %s Brick"),
	brick_mossy(MC.brickBlock, "mossy", "Mossy %s Brick"),
	brick_compacted(MC.brickBlock, "compacted", "Compacted %s Brick"),
	chiseled(MC.brickBlock, "chiseled", "Chiseled %s");
	
	static
	{
		resource.noSilkTouchDropMeta = cobble_art.ordinal();
		cobble.noSilkTouchDropMeta = cobble_art.ordinal();
		resource.fallBreakMeta = cobble.ordinal();
		brick.fallBreakMeta = brick_crushed.ordinal();
		cobble.displayInTab = false;
		mossy.burnable = brick_mossy.burnable = true;
	}
	
	/**
	 * The state when removed mossy.
	 */
	public int		noMossy				= ordinal();
	/**
	 * The changed meta when block drop without silk touching.
	 */
	public int		noSilkTouchDropMeta	= ordinal();
	/**
	 * The changed meta when rock fall down.
	 */
	public int		fallBreakMeta		= ordinal();
	/**
	 * Is block state burnable.
	 */
	public boolean	burnable;
	/**
	 * Should this state rock display in creative tab.
	 */
	public boolean	displayInTab		= true;
	/**
	 * The local name format of rock.
	 */
	public String	local;
	
	/** For texture using. */
	public MatCondition	condition;
	/**
	 * The variant mark of rock, for texture using.
	 */
	public String		variant;
	
	private EnumRockType(MatCondition condition, String variant, String local)
	{
		this.local = local;
		this.condition = condition;
		this.variant = variant;
	}
	
	@Override
	public String getName()
	{
		return name();
	}
	
	public boolean isBurnable()
	{
		return this.burnable;
	}
	
	public EnumRockType burned()
	{
		return values()[this.noMossy];
	}
}
