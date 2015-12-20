package fle.resource.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.block.interfaces.IDebugableBlock;
import flapi.block.old.BlockFle;
import flapi.plant.TreeInfo;
import flapi.util.FleValue;

public class BlockFleLeaves extends BlockFle implements IDebugableBlock
{
	TreeInfo tree;
	private IIcon[] icons;
	
	public BlockFleLeaves(TreeInfo aTree)
	{
		super(ItemLeaves.class, "leaves_" + aTree.getName(), Material.leaves);
        setTickRandomly(true);
        setHardness(0.2F);
        setLightOpacity(1);
        setStepSound(soundTypeGrass);
		setCreativeTab(FleValue.tabFLE);
		tree = aTree;
		String s = tree.getName();
		FleAPI.langManager.registerLocal(new ItemStack(this).getUnlocalizedName(), Character.toString(s.charAt(0)).toUpperCase() + s.substring(1) + " Leaves");
	}
    
    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
    		ForgeDirection side) 
    {
    	return false;
    }
    
    @Override
    public boolean isOpaqueCube() 
    {
    	return false;
    }
    
    @Override
    public boolean isLeaves(IBlockAccess world, int x, int y, int z) 
    {
    	return true;
    }
    
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z,
    		ForgeDirection face)
    {
    	return tree.getFlammability(world, x, y, z, false);
    }
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		try
		{
			return icons[tree.getDefaultLeavesIconID(ForgeDirection.values()[side])];
		}
		catch(Throwable e)
		{
			return blockIcon;
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x,
			int y, int z, int side)
	{
		return icons[tree.getLeavesIconID(ForgeDirection.values()[side], world, x, y, z)];
	}
	
	@Override
	public void registerBlockIcons(IIconRegister aRegister)
	{
		blockIcon = aRegister.registerIcon(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
		icons = new IIcon[tree.getTextureLocate(false).getLocateSize()];
		for(int i = 0; i < icons.length; ++i)
		{
			icons[i] = aRegister.registerIcon(tree.getTextureLocate(false).getTextureFileName(i) + ":" + tree.getTextureLocate(false).getTextureName(i));
		}
	}

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int aColor)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = world.getBiomeGenForCoords(x + l1, z + k1).getBiomeFoliageColor(x + l1, y, z + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }
    
    public void breakBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
    {
        byte b0 = 1;
        int i1 = b0 + 1;

        if (aWorld.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
        {
            for (int j1 = -b0; j1 <= b0; ++j1)
            {
                for (int k1 = -b0; k1 <= b0; ++k1)
                {
                    for (int l1 = -b0; l1 <= b0; ++l1)
                    {
                        Block block = aWorld.getBlock(x + j1, y + k1, z + l1);
                        if (block.isLeaves(aWorld, x + j1, y + k1, z + l1))
                        {
                            block.beginLeavesDecay(aWorld, x + j1, y + k1, z + l1);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer,
    		ForgeDirection dir, float xPos, float yPos, float zPos)
    {
    	return tree.onLeavesToss(aWorld, x, y, z, aPlayer, aPlayer.getCurrentEquippedItem());
    }

    int[] leaveCache;
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World aWorld, int x, int y, int z, Random aRand)
    {
		onNeighborBlockChange(aWorld, x, y, z, null);
    	tree.onLeavesUpdate(aWorld, x, y, z, aRand);
    	
        if (!aWorld.isRemote)
        {
            int l = aWorld.getBlockMetadata(x, y, z);

            if ((l & 8) != 0 && (l & 4) == 0)
            {
                byte b0 = 4;
                int i1 = b0 + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (leaveCache == null)
                {
                    leaveCache = new int[b1 * b1 * b1];
                }

                int l1;

                if (aWorld.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
                {
                    int i2;
                    int j2;

                    for (l1 = -b0; l1 <= b0; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                Block block = aWorld.getBlock(x + l1, y + i2, z + j2);

                                if (!block.canSustainLeaves(aWorld, x + l1, y + i2, z + j2))
                                {
                                    if (block.isLeaves(aWorld, x + l1, y + i2, z + j2))
                                    {
                                        leaveCache[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                    }
                                    else
                                    {
                                        leaveCache[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                    }
                                }
                                else
                                {
                                    leaveCache[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                for (int k2 = -b0; k2 <= b0; ++k2)
                                {
                                	int i = (i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1;
                                    if (leaveCache[i] == l1 - 1)
                                    {
                                        if (leaveCache[i - j1] == -2)
                                        {
                                            leaveCache[i - j1] = l1;
                                        }

                                        if (leaveCache[i + j1] == -2)
                                        {
                                            leaveCache[i + j1] = l1;
                                        }

                                        if (leaveCache[i - b1] == -2)
                                        {
                                            leaveCache[i - b1] = l1;
                                        }

                                        if (leaveCache[i + b1] == -2)
                                        {
                                            leaveCache[i + b1] = l1;
                                        }

                                        if (leaveCache[i - 1] == -2)
                                        {
                                            leaveCache[i - 1] = l1;
                                        }

                                        if (leaveCache[i + 1] == -2)
                                        {
                                            leaveCache[i + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = leaveCache[k1 * j1 + k1 * b1 + k1];

                if (l1 >= 0)
                {
                    aWorld.setBlockMetadataWithNotify(x, y, z, l & -9, 4);
                }
                else
                {
                    removeLeaves(aWorld, x, y, z);
                }
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World aWorld, int aX, int aY, int aZ, Random rand)
    {
        if (aWorld.canLightningStrikeAt(aX, aY + 1, aZ) && !World.doesBlockHaveSolidTopSurface(aWorld, aX, aY - 1, aZ) && rand.nextInt(15) == 1)
        {
            double d0 = (double)((float)aX + rand.nextFloat());
            double d1 = (double)aY - 0.05D;
            double d2 = (double)((float)aZ + rand.nextFloat());
            aWorld.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    private void removeLeaves(World aWorld, int x, int y, int z)
    {
        dropBlockAsItem(aWorld, x, y, z, aWorld.getBlockMetadata(x, y, z), 0);
        aWorld.setBlockToAir(x, y, z);
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z)
    {
        int i2 = world.getBlockMetadata(x, y, z);

        if ((i2 & 8) == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, i2 | 8, 4);
        }
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
    		int metadata, int fortune)
    {
    	return tree.getLeavesDrop(world, x, y, z, metadata, new ArrayList<ItemStack>());
    }

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		try
		{
			tree.getLeavesInfomation(aWorld, x, y, z, aList);
		}
		catch(Throwable e)
		{
			aList.add("This tree log is lost NBT! Please report this bug if this world did'n lost chunk NBT before.");
		}
	}
}