package farcore.lib.model.block;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.M;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.crop.ICrop;
import farcore.lib.material.Mat;
import farcore.lib.util.SubTag;
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
		for(Mat material : Mat.filt(SubTag.CROP))
		{
			for(String tag : material.getProperty(M.property_crop, ICrop.VOID).getAllowedState())
			{
				builder.put(state.withProperty(BlockCrop.PROP_CROP_TYPE, tag), new ModelResourceLocation(FarCore.ID + ":crop/" + material.name, "state=" + tag));
			}
		}
		return builder.build();
	}
}