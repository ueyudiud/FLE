/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.model.block.statemap;

import java.util.Map;

import farcore.lib.block.BlockStairV1;
import nebula.client.model.StateMapperExt;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class StateMapperStiars extends StateMapperExt
{
	private static final IProperty<String>	SHAPE	= StateMapperExt.createFakeProperty("shape", "normal", "inner", "outer");
	private static final IProperty<String>	STATE	= StateMapperExt.createFakeProperty("state", "bottom_north", "bottom_south", "bottom_east", "bottom_west", "top_north", "top_south", "top_east", "top_west");
	
	public StateMapperStiars(String modid, String path, IProperty main, IProperty...ignores)
	{
		super(modid, path, main, ignores);
	}
	
	@Override
	protected String modifyMap(Map<IProperty<?>, Comparable<?>> map)
	{
		String key = super.modifyMap(map);
		EnumFacing facing = StateMapperExt.removeAndGetValue(BlockStairV1.FACING, map);
		EnumHalf half = StateMapperExt.removeAndGetValue(BlockStairV1.HALF, map);
		EnumShape shape = StateMapperExt.removeAndGetValue(BlockStairV1.SHAPE, map);
		String shapeName;
		if (shape == EnumShape.INNER_LEFT || shape == EnumShape.OUTER_LEFT)
		{
			facing = facing.rotateYCCW();
		}
		switch (shape)
		{
		case INNER_LEFT:
		case INNER_RIGHT:
			shapeName = "inner";
			break;
		case OUTER_LEFT:
		case OUTER_RIGHT:
			shapeName = "outer";
			break;
		case STRAIGHT:
		default:
			shapeName = "normal";
			break;
		}
		map.put(STATE, half + "_" + facing);
		map.put(SHAPE, shapeName);
		return key;
	}
}
