package fle.core.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.block.BlockFle;
import fle.api.block.IDebugableBlock;
import fle.api.crop.IFertilableBlock;
import fle.api.crop.IIrrigationHandler;
import fle.api.enums.EnumWorldNBT;
import fle.api.world.BlockPos;
import fle.core.init.Config;

public class BlockFleFarmland extends BlockFle implements IDebugableBlock, IFertilableBlock
{
	@SideOnly(Side.CLIENT)
	private IIcon topIcon_d;
	@SideOnly(Side.CLIENT)
	private IIcon topIcon_w;
	
	public BlockFleFarmland(String aName, String aLocalized)
	{
		super(aName, aLocalized, Material.ground);
        setTickRandomly(true);
        setLightOpacity(255);
        setStepSound(soundTypeGravel);
        setHardness(0.6F);
	}
	
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public IIcon getIcon(int side, int meta)
    {
    	return side == 1 ? topIcon_d : Blocks.dirt.getBlockTextureFromSide(side);
    }
    
    @Override
    public IIcon getIcon(IBlockAccess world, int x,
    		int y, int z, int side)
    {
    	return side == 1 ? (world.getBlockMetadata(x, y, z) > 0 ? topIcon_w : topIcon_d) : 
    		Blocks.dirt.getBlockTextureFromSide(side);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World aWorld, int x, int y, int z, Random aRand)
    {
    	int level = getWaterLevel(aWorld, x, y, z, true);
        if (level == 0 && !aWorld.canLightningStrikeAt(x, y + 1, z))
        {
            int l = aWorld.getBlockMetadata(x, y, z);

            if (l > 0)
            {
                aWorld.setBlockMetadataWithNotify(x, y, z, l - 1, 2);
            }
            else if (!checkPlant(aWorld, x, y, z) && aRand.nextInt(3) == 0)
            {
                aWorld.setBlock(x, y, z, Blocks.dirt);
            }
        }
        else
        {
            aWorld.setBlockMetadataWithNotify(x, y, z, level, 2);
        }
        int N = 0, P = 0, K = 0, Ca = 0;
        if(aRand.nextInt(64) == 0)
        {
        	N = 1;
        }
        if(aRand.nextInt(64) == 0)
        {
        	K = 1;
        }
        if(aRand.nextInt(64) == 0)
        {
        	P = 1;
        }
        if(aRand.nextInt(512) == 0)
        {
        	Ca = 1;
        }
        if((N | P | K | Ca) != 0)
        {
        	useFertilizer(aWorld, x, y, z, new FertitleLevel(N, P, K, Ca));
        }
    }

    /**
     * Block's chance to react to an entity falling on it.
     */
    public void onFallenUpon(World aWorld, int x, int y, int z, Entity entity, float height)
    {
        if (!aWorld.isRemote && aWorld.rand.nextFloat() < height * aWorld.rand.nextFloat())
        {
            if (!(entity instanceof EntityPlayer) && !aWorld.getGameRules().getGameRuleBooleanValue("mobGriefing"))
            {
                return;
            }

            aWorld.setBlock(x, y, z, Blocks.dirt);
        }
    }

    private boolean checkPlant(World aWorld, int x, int y, int z)
    {
        byte b0 = 0;

        for (int l = x - b0; l <= x + b0; ++l)
        {
            for (int i1 = z - b0; i1 <= z + b0; ++i1)
            {
                Block block = aWorld.getBlock(l, y + 1, i1);

                if (block instanceof IPlantable && canSustainPlant(aWorld, x, y, z, ForgeDirection.UP, (IPlantable)block))
                {
                    return true;
                }
            }
        }

        return false;
    }
    
    private static final ForgeDirection[] dirs = {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST};
    private int b = -1;
    
    private int getWaterLevel(World aWorld, int x, int y, int z, boolean flag)
    {
    	//Init farmland consume.
    	if(b == -1) b = Config.getInteger("pFarmlandConsume");
    	int i, j, k;
    	BlockPos pos = new BlockPos(aWorld, x, y, z);
    	BlockPos pos1;
    	int count = 0;
    	BiomeGenBase biome = aWorld.getBiomeGenForCoords(x, z);
    	count += biome.getIntRainfall() / 256;
        if(aWorld.isRaining() && biome.getIntRainfall() > 1000) count += biome.isHighHumidity() ? 50 : 30;
        for (i = -2; i <= 2; ++i)
        {
        	for (k = -2; k <= 2; ++k)
            {
            	pos1 = pos.toPos(i, 0, k);
                if (pos1.getBlock() == Blocks.water)
                {
                   count += 40;
                }
            }
        }
        for(ForgeDirection dir : dirs)
        {
        	TileEntity tile = pos.toPos(dir).getBlockTile();
        	if(tile instanceof IIrrigationHandler)
        	{
        		if(flag)
        		{
            		if(((IIrrigationHandler) tile).canIrrigate(dir.getOpposite()))
            		{
            			count += ((IIrrigationHandler) tile).doIrrigate(dir.getOpposite(), b) * 12;
            		}
        		}
        		else if(((IIrrigationHandler) tile).canIrrigate(dir.getOpposite()))
        		{
        			count += 80;
        		}
        	}
        }

        return flag ? (count > 1200 ? 3 : count > 750 ? 2 : count > 400 ? 1 : 0) : count;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World aWorld, int x, int y, int z, Block aBlock)
    {
        super.onNeighborBlockChange(aWorld, x, y, z, aBlock);
        Block block = aWorld.getBlock(x, y + 1, z);

        if (block.isSideSolid(aWorld, x, y, z, ForgeDirection.DOWN) || block.getMaterial().isSolid())
        {
            aWorld.setBlock(x, y, z, Blocks.dirt);
        }
    }

    public Item getItemDropped(int i, Random aRand, int level)
    {
        return Blocks.dirt.getItemDropped(0, aRand, level);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World aWorld, int x, int y, int z)
    {
        return Item.getItemFromBlock(Blocks.dirt);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        topIcon_w = register.registerIcon(getTextureName() + "_wet");
        topIcon_d = register.registerIcon(getTextureName() + "_dry");
    }
    
    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z,
    		ForgeDirection direction, IPlantable plantable)
    {
    	if(direction == ForgeDirection.UP)
    	{
    		return plantable.getPlantType(world, x, y + 1, z) == EnumPlantType.Crop;
    	}
    	return false;
    }

	
    @Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
    {
		int waterLevel = getWaterLevel(aWorld, x, y, z, false);
		aList.add("Water Level : " + waterLevel);
		FertitleLevel lv = getFertileLevel(aWorld, x, y, z);
		aList.add("N Level : " + lv.N);
		aList.add("P Level : " + lv.P);
		aList.add("K Level : " + lv.K);
		aList.add("Ca Level : " + lv.Ca);
	}

    @Override
    public boolean isFertile(World world, int x, int y, int z)
    {
    	FertitleLevel l = getFertileLevel(world, x, y, z);
    	return l.Ca > 2 && l.K > 4 && l.P > 4 && l.N > 4;
    }

	@Override
	public FertitleLevel getFertileLevel(World world, int x, int y, int z)
	{
		return new FertitleLevel(FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata));
	}

	@Override
	public boolean needFertilize(BlockPos pos, FertitleLevel lv)
	{
		return new FertitleLevel(FLE.fle.getWorldManager().getData(pos, EnumWorldNBT.Metadata)).need(lv);
	}

	@Override
	public void doFertilize(World world, int x, int y, int z, FertitleLevel lv)
	{
		FertitleLevel lv1 = getFertileLevel(world, x, y, z);
		lv1.add(lv);
		FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata, lv1.hashCode());
	}

	
	@Override
	public boolean useFertilizer(World world, int x, int y, int z, FertitleLevel lv)
	{
		FertitleLevel lv1 = getFertileLevel(world, x, y, z);
		if(lv1.contain(lv))
		{
			lv1.add(-lv.N, -lv.P, -lv.K, -lv.Ca);
			FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata, lv1.hashCode());
			return true;
		}
		lv1.add(-lv.N, -lv.P, -lv.K, -lv.Ca);
		FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata, lv1.hashCode());
		return false;
	}

	@Override
	public int getWaterLevel(World world, int x, int y, int z)
	{
		return getWaterLevel(world, x, y, z, false);
	}
}