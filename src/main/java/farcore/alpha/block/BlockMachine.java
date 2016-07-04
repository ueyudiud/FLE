package farcore.alpha.block;

import farcore.alpha.enums.EnumBlockType;
import net.minecraft.block.material.Material;

public class BlockMachine extends BlockBase
{
	protected BlockMachine(String name, Class<? extends ItemBlockBase> clazz, Material material, Object...objects)
	{
		super(EnumBlockType.machine, name, clazz, material, objects);
	}
	protected BlockMachine(String name, Material material)
	{
		super(EnumBlockType.machine, name, material);
	}
	
	@Override
	public int getMobilityFlag()
	{
		return 2;
	}
}