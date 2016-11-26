/*
 * copyright© 2016 ueyudiud
 */

package farcore.data;

import farcore.lib.material.MatCondition;
import net.minecraft.util.IStringSerializable;

/**
 * @author ueyudiud
 */
public enum RockType implements IStringSerializable
{
	resource		(MC.stone, 		""			, "%s"),
	cobble			(MC.cobble,		"standard"	, "Craked %s"),
	cobble_art		(MC.cobble,		"standard"	, "%s Cobble"),
	smoothed		(MC.brickBlock,	"smoothed"	, "Smoothed %s"),
	mossy			(MC.cobble,		"standard"	, "Mossy %s"),
	brick			(MC.brickBlock,	"standard"	, "%s Brick"),
	brick_crushed	(MC.brickBlock,	"crushed"	, "Cracked %s Brick"),
	brick_mossy		(MC.brickBlock,	"mossy"		, "Mossy %s Brick"),
	brick_compacted	(MC.brickBlock,	"compacted"	, "Compacted %s Brick"),
	chiseled		(MC.brickBlock,	"chiseled"	, "Chiseled %s");
	
	static
	{
		resource.noSilkTouchDropMeta = cobble_art.ordinal();
		resource.fallBreakMeta = cobble.ordinal();
		brick.fallBreakMeta = brick_crushed.ordinal();
		cobble.noSilkTouchDropMeta = cobble_art.ordinal();
		cobble.displayInTab = false;
		mossy.burnable = true;
		brick_mossy.burnable = true;
	}
	
	public int noMossy = ordinal();
	public int noSilkTouchDropMeta = ordinal();
	public int fallBreakMeta = ordinal();
	public boolean burnable;
	public boolean displayInTab = true;
	public String local;
	
	/** For texture using. */
	public MatCondition condition;
	public String variant;
	
	private RockType(MatCondition condition, String variant, String local)
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
		return burnable;
	}
	
	public RockType burned()
	{
		return values()[noMossy];
	}
}