package farcore.data;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum EnumSlabState implements IStringSerializable
{
	down,
	up,
	south,
	north,
	west,
	east,
	double_ud,
	double_ns,
	double_we;

	public static final PropertyEnum<EnumSlabState> PROPERTY = PropertyEnum.create("facing", EnumSlabState.class);
	public static final EnumSlabState[][] rotationState = {
			{down, up, west, east, north, south, double_ud, double_we, double_ns},
			{down, up, east, west, south, north, double_ud, double_we, double_ns},
			{east, west, south, north, down, up, double_we, double_ns, double_ud},
			{west, east, south, north, up, down, double_we, double_ns, double_ud},
			{south, north, up, down, west, east, double_ns, double_ud, double_we},
			{north, south, down, up, west, east, double_ns, double_ud, double_we}};

	static
	{
		double_ns.dropMul = 2;
		double_ns.fullCube = true;
		double_we.dropMul = 2;
		double_we.fullCube = true;
		double_ud.dropMul = 2;
		double_ud.fullCube = true;
	}

	public int dropMul = 1;
	public boolean fullCube;

	@Override
	public String getName()
	{
		return name();
	}
}