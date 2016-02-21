package farcore.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockFleMeta2 extends BlockFleName1
{
	/**
	 * The local thread correct meta of block broken.
	 */
	protected ThreadLocal<Integer> metaThread = new ThreadLocal();
	
	protected BlockFleMeta2(String unlocalized, Material material)
	{
		super(unlocalized, material);
	}
	protected BlockFleMeta2(Class<? extends ItemBlock> clazz, String unlocalized, Material material)
	{
		super(clazz, unlocalized, material);
	}
	
	protected boolean hasSub()
	{
		return false;
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return hasSub() ? meta : 0;
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		return hasSub() ? world.getBlockMetadata(x, y, z) : 0;
	}

	@Override
    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(this, 1, damageDropped(meta));
    }
}