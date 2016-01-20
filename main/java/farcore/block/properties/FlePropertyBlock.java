package farcore.block.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameData;

public class FlePropertyBlock extends FleProperty<String>
{
	private static List<String> createNetList(Collection<Block> collection)
	{
		List<String> ret = new ArrayList();
		for(Block block : collection)
		{
			ret.add((String) GameData.getBlockRegistry().getNameForObject(block));
		}
		return ret;
	}
	
	public FlePropertyBlock(String name, Block...values)
	{
		this(name, Arrays.asList(values));
	}

	public FlePropertyBlock(String name, Collection<Block> values)
	{
		super(name, String.class, createNetList(values));
	}

	@Override
	protected String name(String value)
	{
		return GameData.getBlockRegistry().getObject(value).getLocalizedName();
	}
}