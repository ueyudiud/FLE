package farcore.block.plant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.BlockBase;
import farcore.block.ItemBlockBase;
import farcore.interfaces.ISmartHarvestBlock;
import farcore.interfaces.ISmartPlantableBlock;
import farcore.lib.recipe.DropHandler;
import farcore.util.SubTag;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockClassicalBush extends BlockBase 
implements ISmartPlantableBlock, IShearable
{
	protected DropHandler[] handlers = new DropHandler[16];
	
	protected BlockClassicalBush(String name, Class<? extends ItemBlockBase> clazz, Material material,
			Object...objects)
	{
		super(name, clazz, material, objects);
		setTickRandomly(true);
        float f = 0.05F;
        setBlockBounds(f, 0.0F, f, 1F - f, 1F - f, 1F - f);
	}
	protected BlockClassicalBush(String name, Class<? extends ItemBlockBase> clazz,
			Object...objects)
	{
		this(name, clazz, Material.plants, objects);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		checkAndRemove(world, x, y, z);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		checkAndRemove(world, x, y, z);
	}

	public void checkAndRemove(World world, int x, int y, int z)
	{
		if(!canBlockStay(world, x, y, z))
		{
			world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return U.Plants.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}
	
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public int getRenderType()
	{
		return 1;
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}
	
	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public boolean useDefaultType()
	{
		return true;
	}

	@Override
	public String getSmartPlantType(IBlockAccess world, int x, int y, int z)
	{
		return SubTag.PlantType_Plains.name();
	}
	
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
	
	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
	{
		return new ArrayList(Arrays.asList(new ItemStack(this, 1, damageDropped(world.getBlockMetadata(x, y, z)))));
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching)
	{
		return handlers[metadata] == null ? 
				(handlers[metadata] = getDropHandler(metadata)).randomDrops(world.rand) :
					handlers[metadata].randomDrops(world.rand);
	}
	
	protected DropHandler getDropHandler(int meta)
	{
		return DropHandler.EMPTY;
	}
}