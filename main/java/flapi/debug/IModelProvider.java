package flapi.debug;

import flapi.debug.BlockStateJsonWriter.BlockModel;

public interface IModelProvider
{
	void provide(ModelJsonWriter writer, BlockModel model);
}