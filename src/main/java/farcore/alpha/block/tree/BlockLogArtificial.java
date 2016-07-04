package farcore.alpha.block.tree;

import farcore.FarCoreSetup;
import farcore.alpha.block.ItemBlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLogArtificial extends BlockLog
{
	protected BlockLogArtificial(String name, Material material, 
			String localName, float hardness, float resistance)
	{
		super(name, material, hardness, resistance);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName + " Log");
	}

	protected BlockLogArtificial(String name, Class<? extends ItemBlockBase> clazz, Material material, 
			String localName, float hardness, float resistance, Object[] objects)
	{
		super(name, clazz, material, hardness, resistance, objects);
		FarCoreSetup.lang.registerLocal(getTranslateNameForItemStack(0), localName + " Log");
	}

	@Override
	public boolean onBurned(World world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public int getFlammability(World world, int x, int y, int z, ForgeDirection face, int hardness) 
	{
		return 20;
	}

	@Override
	public int getBurningTemperature(World world, int x, int y, int z, int level)
	{
		return 500;
	}
}