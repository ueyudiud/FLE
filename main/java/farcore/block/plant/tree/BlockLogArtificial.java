package farcore.block.plant.tree;

import farcore.lib.substance.SubstanceWood;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLogArtificial extends BlockLog
{
	private BlockLog parent;
	private SubstanceWood wood;
	private int cacheDrop = 0;
	
	protected BlockLogArtificial(BlockLog parent, SubstanceWood wood)
	{
		super("log.artificial." + wood.getName(), Material.wood);
		setTickRandomly(true);
		setBlockTextureName("fle:log/" + wood.getName());
		setHarvestLevel("axe", 0);
		setResistance(0.3F);
		setCreativeTab(CreativeTabs.tabBlock);
		blockHardness = wood.hardness / 5F + 1F;
		this.parent = parent;
		this.wood = wood;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return parent.getIcon(side, meta);
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