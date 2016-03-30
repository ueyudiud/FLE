package fle.core.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.block.BlockBase;
import farcore.enums.Direction;
import farcore.enums.EnumBlock;
import farcore.enums.EnumBlock.IInfoSpawnable;
import farcore.enums.EnumItem;
import farcore.interfaces.ISmartBurnableBlock;
import farcore.interfaces.ISmartBurnableBlock.SmartBurnableBlock;
import farcore.interfaces.energy.IThermalProviderBlock;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFire extends BlockBase 
implements IInfoSpawnable, IThermalProviderBlock
{
	@SideOnly(Side.CLIENT)
	public IIcon iconMovement;
	
	public BlockFire()
	{
		super("fire", ItemFire.class, Material.fire);
		setTickRandomly(true);
		setLightLevel(11);
		EnumItem.fire.set(new ItemStack(this));
		EnumBlock.fire.setBlock(this, EnumItem.fire);
	}
	
	/**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
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

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return FarCoreSetup.handlerA.getRenderId();
    }
    
    @Override
    public int quantityDropped(Random rand)
    {
    	return 0;
    }
    
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World world)
    {
        return 30;
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        label:
        {
        	if(canPlaceBlockAt(world, x, y, z)) break label;
            for(Direction direction : Direction.directions)
        	{
        		if(direction != Direction.U &&
        				getBurnableBlock(world, x + direction.x, y + direction.y, z + direction.z).isFlammable(world, x + direction.x, y + direction.y, z + direction.z, direction.of(), (int) (world.getBlockMetadata(x, y, z) * 40 - world.getBiomeGenForCoords(x, z).rainfall * 60)))
        		{
        			break label;
        		}
        	}
            world.setBlockToAir(x, y, z);
            return;
        }
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!world.getGameRules().getGameRuleBooleanValue("doFireTick")) return;
        if(U.Worlds.isCatchingRain(world, x, y, z))
        {
        	world.setBlockToAir(x, y, z);
        	return;
        }
        ISmartBurnableBlock[] blocks = new ISmartBurnableBlock[6];
        for(Direction direction : Direction.directions)
        {
        	blocks[direction.ordinal()] = getBurnableBlock(world, x + direction.x, y + direction.y, z + direction.z);
        }
    	boolean flag = false;
        int i = 0;
        do
        {
        	ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[i];
			flag = blocks[i].isFireSource(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite());
			++i;
		}
        while (!flag && i < 6);
        int l = world.getBlockMetadata(x, y, z);
        int l1 = l;
        int level = (int) ((16 - l) * 40 - world.getBiomeGenForCoords(x, z).rainfall * 60);
        label:
        {
        	if(canPlaceBlockAt(world, x, y, z)) break label;
            for(Direction direction : Direction.directions)
        	{
        		if(direction != Direction.U &&
        				blocks[direction.ordinal()].isFlammable(world, x + direction.x, y + direction.y, z + direction.z, direction.of(), level))
        		{
        			break label;
        		}
        	}
            world.setBlockToAir(x, y, z);
            return;
        }
    	world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + rand.nextInt(10));
    	if(world.isRaining())
        {
        	level -= 200;
        }
        if(l == 15)
        {
        	
        }
        else
        {
        	l1 = l + rand.nextInt(5) / 2;
        	if(l1 < 15)
        	{
        		if(!flag && l1 != l)
        		{
        			world.setBlockMetadataWithNotify(x, y, z, l1, 4);
        		}
        	}
        	else
        	{
        		world.setBlockToAir(x, y, z);
        	}
        }
        if(level <= 0) level = 1;
        label:
        if(!canPlaceBlockAt(world, x, y, z))
        {
        	i = 0;
        	for(Direction direction : Direction.directions)
        	{
        		if(blocks[2].isFlammable(world, x + direction.x, y + direction.y, z + direction.z, direction.of(), level))
        		{
        			break label;
        		}
        	}
        	world.setBlockToAir(x, y, z);
        	return;
        }
        if (!flag && world.isRaining() && (world.canLightningStrikeAt(x, y, z) || 
        				world.canLightningStrikeAt(x - 1, y, z) || world.canLightningStrikeAt(x + 1, y, z) || world.canLightningStrikeAt(x, y, z - 1) || world.canLightningStrikeAt(x, y, z + 1)))
        {
        	world.setBlockToAir(x, y, z);
        	return;
        }
        for(Direction direction : Direction.directions)
        {
        	ISmartBurnableBlock block = blocks[direction.ordinal()];
        	if(block.onBurning(world, x + direction.x, y + direction.y, z + direction.z, level))
        	{
        		if(flag)
        		{
        			
        		}
        		else
        		{
            		int chance = block.canBeBurned(world, x + direction.x, y + direction.y, z + direction.z, level);
            		if(chance > 0 && rand.nextInt(level) > chance)
            		{
            			world.setBlockToAir(x + direction.x, y + direction.y, z + direction.z);
            			if(canPlaceBlockAt(world, x + direction.x, y + direction.y, z + direction.z))
            			{
            				world.setBlock(x + direction.x, y + direction.y, z + direction.z, this, l1, 3);
            				world.setBlockToAir(x, y, z);
            			}
            		}
        		}
        	}
        }
        for(int xO = -2; xO <= 2; ++xO)
        	for(int yO = -1; yO <= 3; ++yO)
        		for(int zO = -2; zO <= 2; ++zO)
        		{
        			if((xO | yO | zO) == 0) continue;
        			int level1 = (level + rand.nextInt(10)) / (xO * xO + yO * yO + zO * zO);
    				if(!world.isAirBlock(x + xO, y + yO, z + zO)) continue;
        			label2:
    				for(Direction direction : Direction.directions)
        			{
        				ISmartBurnableBlock block = getBurnableBlock(world, x + xO + direction.x, y + yO + direction.y, z + zO + direction.z);
            			if(block.isFlammable(world, x + xO + direction.x, y + yO + direction.y, z + zO + direction.z, direction.of(), level1))
            			{
            				int flammability = block.getFlammability(world, x + xO + direction.x, y + yO + direction.y, z + zO + direction.z, direction.of(), level1);
            				if(flammability > 0 && (flammability > level1 || rand.nextInt(level1) < flammability))
            				{
            					int l2 = l1 + rand.nextInt(5) / 2;
            					for(Direction direction2 : Direction.directions)
            					{
            						block = getBurnableBlock(world, x + xO + direction2.x, y + yO + direction2.y, z + zO + direction2.z);
            						if(!block.onFlame(world, x + xO + direction2.x, y + yO + direction2.y, z + zO + direction2.z, direction2.of(), level1))
            						{
            							break label2;
            						}
            					}
            					spawnFireSource(world, x + xO, y + yO, z + zO, l2);
            					break;
            				}
            			}
        			}
        		}
    }

    public boolean func_149698_L()
    {
        return false;
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable()
    {
        return false;
    }
    
    public static ISmartBurnableBlock getBurnableBlock(IBlockAccess world, int x, int y, int z)
    {
    	Block block = world.getBlock(x, y, z);
    	return block instanceof ISmartBurnableBlock ? (ISmartBurnableBlock) block : ISmartBurnableBlock.instance;
    }

	@Override
	public int getBlockTemperature(World world, int x, int y, int z)
	{
		int l = world.getBlockMetadata(x, y, z);
		int level = (int) ((16 - l) * 40 - world.getBiomeGenForCoords(x, z).rainfall * 60);
        if(world.isRaining())
        {
        	level -= 200;
        }
        ISmartBurnableBlock block = getBurnableBlock(world, x, y - 1, z);
		if(block != ISmartBurnableBlock.instance)
		{
			return block.getBurningTemperature(world, x, y, z, level);
		}		
		return 293 - l * l + l * 48;
	}
    
    /**
     * Spawn a fire source.
     * @param world
     * @param x
     * @param y
     * @param z
     * @param level To show how many range can fire spread, from 1 to 15.
     */
    public static void spawnFireSource(World world, int x, int y, int z, int level)
    {
    	world.setBlock(x, y, z, EnumBlock.fire.block(), 16 - level, 2);
    }
    
    public static void spawnFireNoExtinguished(World world, int x, int y, int z)
    {
    	world.setBlock(x, y, z, EnumBlock.fire.block(), 15, 2);
    }

	@Override
	public void spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 0)
		{
			spawnFireNoExtinguished(world, x, y, z);
		}
		else if(objects[0] instanceof Integer)
		{
			spawnFireSource(world, x, y, z, ((Integer) objects[0]).intValue());
		}
	}

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        if (rand.nextInt(24) == 0)
        {
            world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }

        int l;
        float f;
        float f1;
        float f2;

        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !Blocks.fire.canCatchFire(world, x, y - 1, z, UP))
        {
            if (Blocks.fire.canCatchFire(world, x - 1, y, z, EAST))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + rand.nextFloat() * 0.1F;
                    f1 = (float)y + rand.nextFloat();
                    f2 = (float)z + rand.nextFloat();
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x + 1, y, z, WEST))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)(x + 1) - rand.nextFloat() * 0.1F;
                    f1 = (float)y + rand.nextFloat();
                    f2 = (float)z + rand.nextFloat();
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x, y, z - 1, SOUTH))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + rand.nextFloat();
                    f1 = (float)y + rand.nextFloat();
                    f2 = (float)z + rand.nextFloat() * 0.1F;
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x, y, z + 1, NORTH))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + rand.nextFloat();
                    f1 = (float)y + rand.nextFloat();
                    f2 = (float)(z + 1) - rand.nextFloat() * 0.1F;
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(world, x, y + 1, z, DOWN))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)x + rand.nextFloat();
                    f1 = (float)(y + 1) - rand.nextFloat() * 0.1F;
                    f2 = (float)z + rand.nextFloat();
                    world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        else
        {
            for (l = 0; l < 3; ++l)
            {
                f = (float)x + rand.nextFloat();
                f1 = (float)y + rand.nextFloat() * 0.5F + 0.5F;
                f2 = (float)z + rand.nextFloat();
                world.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z,
    		Entity entity)
    {
    	entity.attackEntityFrom(DamageSource.inFire, 2.0F);
    	entity.setFire(2);
    }
}