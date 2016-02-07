package farcore.block;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.block.item.ItemBlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;

/**
 * The base class of block.
 * @author ueyudiud
 *
 */
public class BlockBase extends BlockBase3Render
{
	protected static NBTTagCompound setupNBT(ItemStack stack)
	{
		if(stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound;
	}

	protected BlockBase(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
		GameRegistry.registerBlock(this, ItemBlockBase.class, unlocalized);
	}
	protected BlockBase(Class<? extends ItemBlock> clazz, String unlocalized, Material Material)
	{
		super(unlocalized, Material);
		GameRegistry.registerBlock(this, clazz, unlocalized);
	}
	
	@Override
	public BlockBase setHardness(float hardness)
	{
		super.setHardness(hardness);
		return this;
	}
	
	@Override
	public BlockBase setResistance(float resistance)
	{
		super.setResistance(resistance);
		return this;
	}
	
	@Override
	public BlockBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
	public BlockBase setStepSound(SoundType sound)
	{
		super.setStepSound(sound);
		return this;
	}
	
	@Override
	public final int getDamageValue(World world, int x,
			int y, int z)
	{
		return getMeta(world, x, y, z);
	}
	
	/**
	 * Get default meta for block.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return The metadata of block.
	 */
	public int getMeta(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}

	public void setMeta(World world, int x, int y, int z, int meta)
	{
		setMeta(world, x, y, z, meta, true);
	}
	public void setMeta(World world, int x, int y, int z, int meta, boolean causeClientUpdate)
	{
		world.setBlock(x, y, z, this, meta, causeClientUpdate ? 2 : 3);
	}
}