package fla.core.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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
import fla.core.recipe.machine.PolishRecipe;
import fla.core.tileentity.TileEntityPolishTable;
import fla.core.tileentity.base.TileEntityBaseWithFacing;

public class BlockPolishTable extends BlockBaseHasTile
{
	private TileEntityBaseWithFacing tile = new TileEntityPolishTable();
	
	protected IIcon emptyTopIcon;
	protected IIcon topIcon;
	protected IIcon sideIcon;
	protected IIcon downIcon;
	
	public BlockPolishTable()
	{
		super(Material.wood);
		this.setHardness(1.5F);
		this.setResistance(1.0F);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public void setBlockFacing(BlockPos pos, ForgeDirection dir) 
	{
		tile = tile.setFacing(dir);
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
			TileEntityPolishTable tile = (TileEntityPolishTable) world.getTileEntity(x, y, z);
			if(tile != null)
			{
				if(tile.getStackInSlot(0) == null)
				{
					if(player.getCurrentEquippedItem() != null)
					{
						ItemStack itemstack = player.getCurrentEquippedItem().copy();
						if(PolishRecipe.canPolish(itemstack))
						{
							--itemstack.stackSize;
							if(itemstack.stackSize != 0)
							{
								--player.getCurrentEquippedItem().stackSize;
							}
							else
							{
								player.setCurrentItemOrArmor(0, null);
							}
							itemstack.stackSize = 1;
							tile.setInventorySlotContents(0, itemstack.copy());
						}
						else if(tile.getStackInSlot(1) != null)
						{
							Random rand = new Random();
							double x1 = (double)x + rand.nextDouble();
							double y1 = (double)y + 1.0625D;
							double z1 = (double)z + rand.nextDouble();
							EntityItem entity = new EntityItem(world, x1, y1, z1, tile.getStackInSlot(1).copy());
	                        entity.motionY = (double)((float)rand.nextGaussian() * 0.05F + 0.1F);
							world.spawnEntityInWorld(entity);
							tile.setInventorySlotContents(1, null);
						}
					}
					else
					{
						if(tile.getStackInSlot(1) != null)
						{
							Random rand = new Random();
							double x1 = (double)x + rand.nextDouble();
							double y1 = (double)y + 1.0625D;
							double z1 = (double)z + rand.nextDouble();
							EntityItem entity = new EntityItem(world, x1, y1, z1, tile.getStackInSlot(1).copy());
	                        entity.motionY = (double)((float)rand.nextGaussian() * 0.05F + 0.1F);
	                        world.spawnEntityInWorld(entity);
							tile.setInventorySlotContents(1, null);
						}
					}
				}
				else
				{
					player.openGui(Fla.MODID, 1, world, x, y, z);
				}
			}
			return true;
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) 
	{
        TileEntityPolishTable tile = (TileEntityPolishTable)world.getTileEntity(x, y, z);

        Random rand = new Random();
        if (tile != null)
        {
            for (int i1 = 0; i1 < tile.getSizeInventory() - 1; ++i1)
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
	public void registerBlockIcons(IIconRegister register)
	{
		emptyTopIcon = register.registerIcon(getTextureName() + "_top_empty");
		topIcon = register.registerIcon(getTextureName() + "_top");
		sideIcon = register.registerIcon(getTextureName() + "_side");
		downIcon = register.registerIcon(getTextureName() + "_down");
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ((TileEntityPolishTable) pos.getBlockTile()).getBlockDirection(pos);
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

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return side == ForgeDirection.UP ? (((TileEntityPolishTable) pos.getBlockTile()).getStackInSlot(0) != null ? topIcon : emptyTopIcon) : side == ForgeDirection.DOWN ? downIcon : sideIcon;
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return side == ForgeDirection.UP ? topIcon : side == ForgeDirection.DOWN ? downIcon : sideIcon;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return tile;
	}

	@Override
	public boolean isOpaqueCube() 
	{
		return true;
	}
}