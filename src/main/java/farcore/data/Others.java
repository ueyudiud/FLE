package farcore.data;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

public class Others
{
	public static final Item ITEM_AIR = Item.getItemFromBlock(Blocks.AIR);
	
	public static final PropertyBool PROP_NORTH = PropertyBool.create("north");
	public static final PropertyBool PROP_EAST = PropertyBool.create("east");
	public static final PropertyBool PROP_SOUTH = PropertyBool.create("south");
	public static final PropertyBool PROP_WEST = PropertyBool.create("west");
	public static final PropertyBool PROP_UP = PropertyBool.create("up");
	public static final PropertyBool PROP_DOWN = PropertyBool.create("down");
	public static final PropertyEnum<EnumFacing> PROP_FACING_ALL = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.VALUES);
	public static final PropertyEnum<EnumFacing> PROP_FACING_HORIZONTALS = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);

	public static final IAttribute PROJECTILE_DAMAGE = (new RangedAttribute((IAttribute)null, "far.projectile.damage", 0.0D, 0, Double.MAX_VALUE)).setShouldWatch(true);
}