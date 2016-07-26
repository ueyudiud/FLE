package farcore.lib.model.block;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public abstract class ModelBase implements IModel
{
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}
	
	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}
}