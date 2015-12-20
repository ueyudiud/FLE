package fle.core.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.collection.Register;
import flapi.enums.EnumFoodType;
import flapi.item.interfaces.IFoodStat;
import flapi.plant.CropCard;
import flapi.plant.ICropSeed;
import fle.FLE;
import fle.core.init.IB;
import fle.core.init.Plants;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.FoodStandard;
import fle.core.util.ItemTextureHandler;
import fle.resource.block.TileEntityCrop;

public class ItemFleSeed extends flapi.item.ItemFleFood implements IPlantable, ICropSeed
{
	public ItemFleSeed init()
	{
		addSubItem(0, Plants.soybean, "Soybeen", new ItemTextureHandler("resource/seed/0"));
		addSubItem(1, Plants.ramie, "Ramie Seed", new ItemTextureHandler("resource/seed/1"));
		addSubItem(2, Plants.millet, "Millet Seed", new ItemTextureHandler("resource/seed/2"));
		addSubItem(3, Plants.suger_cances, "Suger Cances", new ItemTextureHandler("resource/seed/3"), new FoodStandard(EnumFoodType.Resource, 1, 0.1F));
		addSubItem(4, Plants.wheat, "Wheat Seed", new ItemTextureHandler("resource/seed/4"));
		addSubItem(5, Plants.cotton, "Cotton Seed", new ItemTextureHandler("resource/seed/5"));
		addSubItem(6, Plants.sweet_potato, "Sweet Potato", new ItemTextureHandler("resource/seed/6"), new FoodStandard(EnumFoodType.Resource, 1, 0.3F));
		addSubItem(7, Plants.potato, "Potato", new ItemTextureHandler("resource/seed/7"), new FoodStandard(EnumFoodType.Resource, 1, 0.3F));
		addSubItem(8, Plants.flax, "Flax", new ItemTextureHandler("resource/seed/8"));
		return this;
	}

	public final ItemFleSeed addSubItem(int aMetaValue, CropCard crop, String aLocalized, ItemTextureHandler aLocate, IFoodStat<flapi.item.ItemFleFood> aFoodBehavior)
	{
		register.register(crop, crop.getCropName());
		addSubItem(aMetaValue, crop.getCropName(), aLocalized, aLocate, new BehaviorBase(), aFoodBehavior);
	    return this;
	}
	public final ItemFleSeed addSubItem(int aMetaValue, CropCard crop, String aLocalized, ItemTextureHandler aLocate)
	{
		return addSubItem(aMetaValue, crop, aLocalized, aLocate, (IFoodStat) null);
	}
	
	private Register<CropCard> register = new Register();
	
    private Block plantBlock;
    /** BlockID of the block the seeds can be planted on. */
    @SuppressWarnings("unused")
	private Block soilBlock;

    public ItemFleSeed(Block plant, Block soil)
    {
    	super("seed", "seed");
        plantBlock = plant;
        soilBlock = soil;
        hasSubtypes = true;
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
    public String getMetaUnlocalizedName(int aMetadata)
    {
    	return register.name(aMetadata);
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