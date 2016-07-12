package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.data.EnumToolType;
import farcore.lib.block.BlockBase;
import farcore.lib.block.IToolableBlock;
import farcore.lib.material.Mat;
import farcore.lib.tree.ITree;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.LanguageManager;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockLeaves extends BlockBase implements IShearable, IToolableBlock
{
	public ITree info;

	public BlockLeaves(Mat material)
	{
		this("leaves." + material.name, material.tree, material.localName + " Leaves");
	}
	public BlockLeaves(String name, ITree info, String localName)
	{
		super(name, Material.leaves);
		this.info = info;
		setHardness(0.5F);
		setResistance(0.02F);
		
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), localName);
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		info.registerLeavesIcon(register);
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return info.getLeavesIcon(register, meta, side);
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
    public int getRenderColor(int meta)
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
                l += (i2 & 0XFF0000) >> 16;
                i1 += (i2 & 0XFF00) >> 8;
                j1 += i2 & 0xFF;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
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
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z,
    		Block block)
    {
    	world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block,
    		int meta)
    {
    	info.breakLeaves(world, x, y, z, meta);
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	info.updateLeaves(world, x, y, z, rand);
    }
    
    @Override
    public void beginLeavesDecay(World world, int x, int y, int z)
    {
    	info.beginLeavesDency(world, x, y, z);
    	world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }
    
    @Override
    public int tickRate(World world)
    {
    	return 12;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (U.Worlds.isCatchingRain(world, x, y, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1)
        {
            double d0 = (double)((float)x + random.nextFloat());
            double d1 = (double)y - 0.05D;
            double d2 = (double)((float)z + random.nextFloat());
            world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
    		boolean silkTouching)
    {
    	return info.getLeavesDrops(world, x, y, z, metadata, fortune, silkTouching, new ArrayList());
    }
    
    @Override
    public int damageDropped(int meta)
    {
    	return 0;
    }

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
	{
		return new ArrayList(Arrays.asList(createStackedBlock(damageDropped(world.getBlockMetadata(x, y, z)))));
	}

	@Override
	public float onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ)
	{
		return info.onToolClickLeaves(player, tool, stack, world, x, y, z, side, hitX, hitY, hitZ);
	}
	
	@Override
	public float onToolUse(EntityPlayer player, EnumToolType tool, ItemStack stack, World world, long useTick, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return info.onToolUseLeaves(player, tool, stack, world, useTick, x, y, z, side, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean isLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
}