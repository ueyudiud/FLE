/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.model.block.statemap;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import farcore.blocks.flora.BlockCrop;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.crop.ICropSpecie;
import farcore.lib.material.Mat;
import nebula.client.model.ModelLocation;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StateMapperCrop implements IStateMapper
{
	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
	{
		IBlockState state = blockIn.getDefaultState();
		ImmutableMap.Builder<IBlockState, ModelResourceLocation> builder = ImmutableMap.builder();
		for (Mat material : Mat.filt(SubTags.CROP))
		{
			ICropSpecie crop = material.getProperty(MP.property_crop);
			L.consume(1, 1 + crop.getMaxStage(), idx -> builder.put(state.withProperty(BlockCrop.PROP_CROP_TYPE, material.name + "_" + idx), new ModelLocation(material.modid, "crop/" + material.name, "state=" + idx)));
		}
		return builder.build();
	}
}
