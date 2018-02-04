/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.model.block.statemap;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.blocks.BlockRedstoneCircuit;
import nebula.client.model.IStateMapperExt;
import nebula.client.model.StateMapperExt;
import nebula.common.block.property.PropertyTE;
import nebula.common.block.property.PropertyTE.TETag;
import nebula.common.data.Misc;
import nebula.common.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StateMapperCircuit implements IStateMapperExt
{
	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
	{
		IBlockState state = blockIn.getDefaultState();
		ImmutableMap.Builder<IBlockState, ModelResourceLocation> builder = ImmutableMap.builder();
		PropertyTE property = ((BlockRedstoneCircuit) blockIn).property_TE;
		for (TETag tag : property.getAllowedValues())
		{
			if (tag.name().equals("void"))
			{
				continue;
			}
			IBlockState state2 = state.withProperty(property, tag);
			for (String substate : BlockRedstoneCircuit.ALLOWED_STATES.get(tag.name()))
			{
				for (Direction facing : Misc.PROP_DIRECTION_HORIZONTALS.getAllowedValues())
				{
					IBlockState state3 = state2.withProperty(BlockRedstoneCircuit.CUSTOM_VALUE, substate).withProperty(Misc.PROP_DIRECTION_HORIZONTALS, facing);
					Map<IProperty<?>, Comparable<?>> map = new HashMap(state3.getProperties());
					StateMapperExt.removeAndGetName(property, map);
					builder.put(state3, new ModelResourceLocation(FarCore.ID + ":circuit/" + tag.name(), StateMapperExt.getPropertyKey(map)));
				}
			}
		}
		return builder.build();
	}
	
	@Override
	public ModelResourceLocation getLocationFromState(IBlockState state)
	{
		PropertyTE property = ((BlockRedstoneCircuit) state.getBlock()).property_TE;
		Map<IProperty<?>, Comparable<?>> map = new HashMap<>(state.getProperties());
		return new ModelResourceLocation(FarCore.ID + ":circuit/" + StateMapperExt.removeAndGetName(property, map), StateMapperExt.getPropertyKey(map));
	}
}
