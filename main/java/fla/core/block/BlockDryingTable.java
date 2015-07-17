package fla.core.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.Fla;
import fla.core.tileentity.TileEntityDryingTable;
import fla.core.tileentity.TileEntityPolishTable;

public class BlockDryingTable extends BlockBaseHasTile
{
	public BlockDryingTable()
	{
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) 
	{
		return super.canPlaceBlockAt(world, x, y, z) && world.getBlock(x, y - 1, z).isSideSolid(world, x, y, z, ForgeDirection.UP);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,
			int y, int z, EntityPlayer player,
			int side, float xPos, float yPos,
			float zPos)
	{
		if(world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityDryingTable tile = (TileEntityDryingTable) world.getTileEntity(x, y, z);
			if(tile != null)
			{
				player.openGui(Fla.MODID, 2, world, x, y, z);
			}
			return true;
		}
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UNKNOWN;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDryingTable();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) 
	{
        TileEntityDryingTable tile = (TileEntityDryingTable) world.getTileEntity(x, y, z);

        Random rand = new Random();
        if (tile != null)
        {
            for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tile.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return blockIcon;
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return blockIcon;
	}

	@Override
	public int getRenderType() 
	{
		return FlaValue.ALL_RENDER_ID;
	}

	@Override
	public boolean isNormalCube() 
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}

	@Override
	public boolean hasSubs() 
	{
		return false;
	}

	@Override
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
	{
		return false;
	}
}