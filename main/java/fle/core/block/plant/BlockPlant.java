package fle.core.block.plant;

import static net.minecraftforge.common.EnumPlantType.Plains;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockFle;
import fle.api.enums.EnumWorldNBT;
import fle.api.plant.PlantCard;
import fle.api.util.DropInfo;
import fle.api.util.FleEntry;
import fle.api.world.BlockPos;
import fle.core.world.biome.FLEBiome;

public class BlockPlant extends BlockFle implements IPlantable
{
	private IIcon[] icons;
	
    public BlockPlant()
    {
    	super(ItemPlant.class, "flePlant", Material.plants);
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerGrass.getGrassColor(d0, d1);
    }
    
    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
        return FLE.fle.getPlantRegister().getPlant(meta).shouldUseBiomeColor() ? ColorizerGrass.getGrassColor(0.5D, 1.0D) : 0xFFFFFF;
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        boolean flag = card(world, x, y, z).shouldUseBiomeColor();
        return !flag ? 0xFFFFFF : world.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
    	return icons[meta % icons.length];
    }
    
    @Override
    public IIcon getIcon(IBlockAccess world, int x,
    		int y, int z, int side)
    {
    	return icons[getPlantMetadata(world, x, y, z) % icons.length];
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
    	icons = new IIcon[FLE.fle.getPlantRegister().getPlantSize()];
    	Iterator<PlantCard> itr = FLE.fle.getPlantRegister().getPlants();
    	int i;
    	while (itr.hasNext())
    	{
			PlantCard plant = itr.next();
			i = FLE.fle.getPlantRegister().getPlantID(plant);
			icons[i] = register.registerIcon(FleValue.TEXTURE_FILE + ":" + plant.getPlantTextureName());
		}
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World aWorld, int x, int y, int z)
    {
        return super.canPlaceBlockAt(aWorld, x, y, z) && canBlockStay(aWorld, x, y, z);
    }

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block aBlock)
    {
        return aBlock == Blocks.grass || aBlock == Blocks.dirt || aBlock == Blocks.farmland;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World aWorld, int x, int y, int z, Block aBlock)
    {
        super.onNeighborBlockChange(aWorld, x, y, z, aBlock);
        checkAndDropBlock(aWorld, x, y, z);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World aWorld, int x, int y, int z, Random aRand)
    {
    	if(!checkAndDropBlock(aWorld, x, y, z))
    	{
    		card(aWorld, x, y, z).updatePlant(aWorld, x, y, z);
    	}
    }

    /**
     * checks if the block can stay, if not drop as item
     */
    protected boolean checkAndDropBlock(World aWorld, int x, int y, int z)
    {
        if (!canBlockStay(aWorld, x, y, z))
        {
            dropBlockAsItem(aWorld, x, y, z, getDamageValue(aWorld, x, y, z), 0);
            aWorld.setBlock(x, y, z, Blocks.air, 0, 2);
            return true;
        }
        return false;
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World aWorld, int x, int y, int z)
    {
        return  aWorld.getBlock(x, y - 1, z).canSustainPlant(aWorld, x, y - 1, z, ForgeDirection.UP, this);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int x, int y, int z)
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
        return FleValue.FLE_NOINV_RENDER_ID;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab,
    		List list)
    {
    	Iterator<PlantCard> itr = FLE.fle.getPlantRegister().getPlants();
    	while (itr.hasNext())
    	{
			PlantCard plantCard = itr.next();
			list.add(new ItemStack(item, 1, FLE.fle.getPlantRegister().getPlantID(plantCard)));
		}
    }
    
    @Override
    public void breakBlock(World aWorld, int x, int y, int z, Block aBlock,
    		int aMeta)
    {
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		metaThread.set(new Integer(FLE.fle.getWorldManager().getData(tPos, EnumWorldNBT.Metadata)));
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
		//Remove all meta of this block in FWM
		FleAPI.mod.getWorldManager().removeData(tPos);
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
    		int metadata, int fortune)
    {
    	Integer meta = metaThread.get();
    	return meta == null ? new ArrayList<ItemStack>() : FLE.fle.getPlantRegister().getPlant(meta).getDropInfo().getDrops();
    }
    
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return card(world, x, y, z).getPlantType();
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        return FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata);
    }
    
    @Override
    public int getDamageValue(World aWorld, int x, int y, int z)
    {
    	return FLE.fle.getWorldManager().getData(new BlockPos(aWorld, x, y, z), EnumWorldNBT.Metadata);
    }
    
    private PlantCard card(IBlockAccess world, int x, int y, int z)
    {
    	PlantCard card = FLE.fle.getPlantRegister().getPlant(getPlantMetadata(world, x, y, z));
    	return card != null ? card : VOID;
    }
    
    private static final PlantCard VOID = new PlantCard()
    {	
		public EnumPlantType getPlantType() {return Plains;}
		public String getPlantName() {return "VOID";}
		public String getPlantTextureName() {return null;}
		public DropInfo getDropInfo() {return DropInfo.empty();}
		public boolean shouldUseBiomeColor() {return false;}
	};
}