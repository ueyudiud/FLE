package fla.core.item;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.crop.CropCard;
import fla.api.crop.CropRegistry;
import fla.api.crop.ICropSeed;
import fla.api.util.Registry;
import fla.core.FlaItems;
import fla.core.tileentity.TileEntityCrops;

public class ItemFlaSeed extends ItemBase implements IPlantable, ICropSeed
{
	private static Registry<CropCard> register = new Registry();
	public static HashMap<String, String> iconTextMap = new HashMap();
	public static HashMap<String, IIcon> iconMap;
	
    private Block plantBlock;
    /** BlockID of the block the seeds can be planted on. */
    private Block soilBlock;

    public ItemFlaSeed(Block plant, Block soil)
    {
        this.plantBlock = plant;
        this.soilBlock = soil;
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    public static void registerCrop(CropCard crop, String seedTexture)
    {
    	register.register(crop, crop.getCropName());
    	iconTextMap.put(crop.getCropName(), seedTexture);
    }

    public static ItemStack a(String cropName)
    {
    	return new ItemStack(FlaItems.seeds, 1, register.serial(cropName));
    }
    public static ItemStack a(int size, String cropName)
    {
    	return new ItemStack(FlaItems.seeds, size, register.serial(cropName));
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) 
    {
    	return super.getUnlocalizedName(stack) + ":" + register.name(stack.getItemDamage());
    }
    
    @Override
    public void registerIcons(IIconRegister register)
    {
    	iconMap = new HashMap();
    	for(String str : iconTextMap.keySet())
    	{
    		iconMap.put(str, register.registerIcon(iconTextMap.get(str)));
    	}
    }
    
    @Override
    public IIcon getIconFromDamage(int meta)
    {
    	return iconMap.get(register.get(meta).getCropName());
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float xPos, float yPos, float zPos)
    {
        if (side != 1)
        {
            return false;
        }
        else if (player.canPlayerEdit(x, y, z, side, itemstack) && player.canPlayerEdit(x, y + 1, z, side, itemstack))
        {
            if (world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z))
            {
                plantBlock.onBlockPlacedBy(world, x, y, z, player, itemstack);
                world.setBlock(x, y + 1, z, this.plantBlock);
                --itemstack.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return plantBlock;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
    	TileEntityCrops tile = (TileEntityCrops) world.getTileEntity(x, y, z);
    	if(tile != null)
    	{
    		return CropRegistry.getCropID(tile.getCrop());
    	}
        return 0;
    }

    @Override
	public CropCard getCrop(ItemStack stack) 
    {
		return register.get(stack.getItemDamage());
	}

    @Override
    public void getSubItems(Item item, CreativeTabs tag, List list)
    {
    	for(CropCard crop : register)
    	{
    		list.add(new ItemStack(item, 1, register.serial(crop)));
    	}
    }
}