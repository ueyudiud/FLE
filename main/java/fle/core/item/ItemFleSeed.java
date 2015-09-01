package fle.core.item;

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
import fle.FLE;
import fle.api.FleValue;
import fle.api.crop.CropCard;
import fle.api.crop.ICropSeed;
import fle.api.enums.EnumFoodType;
import fle.api.item.IFoodStat;
import fle.api.item.ItemFleFood;
import fle.api.util.ITextureLocation;
import fle.api.util.Register;
import fle.core.init.Crops;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.FoodStandard;
import fle.core.te.TileEntityCrop;
import fle.core.util.TextureLocation;

public class ItemFleSeed extends ItemFleFood implements IPlantable, ICropSeed
{
	public ItemFleSeed init()
	{
		addSubItem(0, Crops.soybean, "Soybeen", new TextureLocation("crop/soybean"));
		addSubItem(1, Crops.ramie, "Ramie Seed", new TextureLocation("crop/ramie_seed"));
		addSubItem(2, Crops.millet, "Millet Seed", new TextureLocation("crop/millet_seed"));
		addSubItem(3, Crops.suger_cances, "Suger Cances", new TextureLocation("crop/reed"), new FoodStandard(EnumFoodType.Resource, 1, 0.1F));
		addSubItem(4, Crops.wheat, "Wheat Seed", new TextureLocation("crop/wheat_seed"));
		return this;
	}

	public final ItemFleSeed addSubItem(int aMetaValue, CropCard crop, String aLocalized, ITextureLocation aLocate, IFoodStat<ItemFleFood> aFoodBehavior)
	{
		addSubItem(aMetaValue, crop.getCropName(), aLocalized, aLocate, new BehaviorBase(), aFoodBehavior);
		register.register(crop, crop.getCropName());
	    return this;
	}
	public final ItemFleSeed addSubItem(int aMetaValue, CropCard crop, String aLocalized, ITextureLocation aLocate)
	{
		addSubItem(aMetaValue, crop.getCropName(), aLocalized, aLocate, new BehaviorBase(), null);
		register.register(crop, crop.getCropName());
	    return this;
	}
	
	private Register<CropCard> register = new Register();
	
    private Block plantBlock;
    /** BlockID of the block the seeds can be planted on. */
    private Block soilBlock;

    public ItemFleSeed(Block plant, Block soil)
    {
    	super("fle.seed", "fle.seed");
        plantBlock = plant;
        soilBlock = soil;
        setHasSubtypes(true);
    }

    public static ItemStack a(String cropName)
    {
    	return new ItemStack(IB.cropSeed, 1, ((ItemFleSeed) IB.cropSeed).register.serial(cropName));
    }
    public static ItemStack a(int size, String cropName)
    {
    	return new ItemStack(IB.cropSeed, size, ((ItemFleSeed) IB.cropSeed).register.serial(cropName));
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) 
    {
    	return super.getUnlocalizedName(stack) + ":" + register.name(stack.getItemDamage());
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
    	TileEntityCrop tile = (TileEntityCrop) world.getTileEntity(x, y, z);
    	if(tile != null)
    	{
    		return FLE.fle.getCropRegister().getCropID(tile.getCrop());
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

	@Override
	public String getCropSeedName(ItemStack stack)
	{
		return getCrop(stack).getCropName();
	}
}