package fle.resource.block.sand;

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.List;
import java.util.Random;

import farcore.block.BlockFactory;
import farcore.block.interfaces.IFallable;
import farcore.block.item.ItemBlockBase;
import farcore.entity.EntityFleFallingBlock;
import farcore.nbt.ToolInfo.ToolType;
import farcore.world.BlockPos;
import flapi.block.BlockSub;
import flapi.block.item.ItemBlockSub;
import flapi.util.Values;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSand extends BlockSub implements IFallable
{
	private IIcon[] icons = new IIcon[16];

	protected BlockSand(String unlocalized)
	{
		super("sand." + unlocalized, Material.sand);
	}
	protected BlockSand(Class<? extends ItemBlockSub> clazz, String unlocalized)
	{
		super(clazz, "sand." + unlocalized, Material.sand);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		for(int i = 0; i < informations.length; ++i)
		{
			if(informations[i] != null)
			{
				icons[i] = register.registerIcon(informations[i].getIconName());
			}
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		IIcon icon = icons[world.getBlockMetadata(x, y, z) % icons.length];
		return icon == null ? Values.EMPTY_BLOCK_ICON : icon;
	}
	
	@Override
	public IIcon getBlockIcon(int side, int meta)
	{
		IIcon icon = icons[meta % icons.length];
		return icon == null ? Values.EMPTY_BLOCK_ICON : icon;
	}
	
	public void onBlockAdded(World worldIn, int x, int y, int z)
	{
		worldIn.scheduleBlockUpdate(x, y, z, this, tickRate(worldIn));
	}
	
	/**
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World worldIn, int x, int y, int z, 
			Block neighborBlock)
	{
		worldIn.scheduleBlockUpdate(x, y, z, this, tickRate(worldIn));
	}
	
	@Override
	public int tickRate(World world)
	{
		return 2;
	}
	
	public void updateTick(World worldIn, int x, int y, int z,
			Random rand)
	{
		if (!worldIn.isRemote)
		{
			checkFallable(worldIn, x, y, z);
		}
	}
	
	private void checkFallable(World world, int x, int y, int z)
	{
		if (canFallInto(new BlockPos(world, x, y - 1, z)) && y >= 0)
		{
			BlockFactory.makeFallingBlock(world, new BlockPos(world, x, y, z), this);
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, 
			ForgeDirection direction, IPlantable plantable)
	{
		switch(plantable.getPlantType(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
		{
		case Desert: return true;
        case Beach:
            return (world.getBlock(x - 1, y, z    ).getMaterial() == Material.water ||
                    world.getBlock(x + 1, y, z    ).getMaterial() == Material.water ||
                    world.getBlock(x,     y, z - 1).getMaterial() == Material.water ||
                    world.getBlock(x,     y, z + 1).getMaterial() == Material.water);
		default : return false;
		}
	}

	@Override
	public boolean isToolEffective(String type, int meta)
	{
		return ToolType.shovel.name.equals(type);
	}

	@Override
	public void onStartFalling(EntityFleFallingBlock entity)
	{
		
	}

	@Override
	public void onHitEntity(EntityFleFallingBlock entity)
	{
		
	}

	@Override
	public IIcon getFallingIcon(EntityFleFallingBlock entity, int side)
	{
		return getBlockIcon(side, entity.meta);
	}

	@Override
	public List<ItemStack> onBlockDropAsItem(EntityFleFallingBlock entity)
	{
		return getDrops(entity.worldObj, 
				(int) entity.posX, (int) entity.posY, (int) entity.posZ, 
				entity.meta, 0);
	}

	@Override
	public boolean canFallInto(BlockPos pos)
	{
		if (pos.isAir())
			return true;
		Block block = pos.block();
		Material material = block.getMaterial();
		return block == Blocks.fire || material == Material.air
				|| material == Material.water || material == Material.lava;
	}

	@Override
	public void onEndFalling(BlockPos pos)
	{
		
	}
}