package farcore.alpha.enums;

/**
 * The all kinds of block type current here.
 * @author ueyudiud
 *
 */
public enum EnumBlockType
{
	machine,//The machine block, it has some behavior when act on it.
	leaves,//The plant but belong to another block.
	plant,//The plant type.
	ground,//The block compose environment (Such as dirt, sand, clay, rock).
	building,//The building block, made by player and use to make building.
	decoration,//The decoration block, made by player but can not compose skeleton of a building.
	other;//Other block, such as technical block (Ender door, piston hand, etc).
}