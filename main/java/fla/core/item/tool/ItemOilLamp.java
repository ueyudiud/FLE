package fla.core.item.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import fla.api.item.tool.ItemDamageResource;
import fla.api.recipe.Fuel;
import fla.api.recipe.FuelOilLamp;
import fla.api.util.FlaValue;
import fla.core.FlaBlocks;
import fla.core.tileentity.TileEntityOilLamp;

public class ItemOilLamp extends ItemFlaTool
{
	private static final int capacity = FlaValue.CAPACITY_OIL_LAMP;
	private static List<FuelOilLamp> fuelList = new ArrayList();
	private static Map<FuelOilLamp, String> iconTextMap = new HashMap();
	private Map<FuelOilLamp, IIcon> iconMap;
	private IIcon void_contain;
	private IIcon candle_off;
	private IIcon candle_on;

	public static void registerLampFuel(FuelOilLamp fuel, String itemTextName)
	{
		fuelList.add(fuel);
		iconTextMap.put(fuel, itemTextName);
	}
	
	public ItemOilLamp() 
	{
		setMaxDamage(1);
		setHasSubtypes(true);
	}
	
	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player,
			World world, int x, int y, int z,
			int side, float xPos, float yPos,
			float zPos)
	{
		Block block = world.getBlock(x, y, z);

	    if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
	    {
	        side = 1;
	    }
	    else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z))
	    {
	        if (side == 0)
	        {
	            --y;
	        }

	        if (side == 1)
	        {
	            ++y;
	        }

	        if (side == 2)
	        {
	            --z;
	        }

	        if (side == 3)
	        {
	            ++z;
	        }

	        if (side == 4)
	        {
	            --x;
	        }

	        if (side == 5)
	        {
	            ++x;
	        }
	    }

	    if (item.stackSize == 0)
	    {
	        return false;
	    }
	    else if (!player.canPlayerEdit(x, y, z, side, item))
	    {
	        return false;
	    }
	    else if (y == 255)
	    {
	        return false;
	    }
	    else if (world.canPlaceEntityOnSide(FlaBlocks.oilLamp, x, y, z, false, side, player, item))
	    {
	        if (placeBlockAt(FlaBlocks.oilLamp, item, player, world, x, y, z, side, xPos, yPos, zPos))
	        {
	            world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), FlaBlocks.oilLamp.stepSound.func_150496_b(), (FlaBlocks.oilLamp.stepSound.getVolume() + 1.0F) / 2.0F, FlaBlocks.oilLamp.stepSound.getPitch() * 0.8F);
	            --item.stackSize;
	        }

	        return true;
	    }
	    else
	    {
	        return false;
	    }
	}
	
	private boolean placeBlockAt(Block block, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, double xPos, double yPos, double zPos)
	{
		block.onBlockPlacedBy(world, x, y, z, player, stack);
		TileEntityOilLamp tile = (TileEntityOilLamp) block.createTileEntity(world, stack.getItemDamage());
		if(tile != null)
		{
			tile.setContainFluid((FuelOilLamp) getLiquidType(stack), getLiquidContain(stack));
			if(hasWick(stack))
				tile.setWick();
			if(isBurning(stack))
				tile.setBurning();
		}
		world.setBlock(x, y, z, block);
		world.setTileEntity(x, y, z, tile);
		return true;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		iconMap = new HashMap();
		void_contain = register.registerIcon(FlaValue.VOID_FILE_NAME);
		candle_off = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":tools/stone/lamp/a");
		candle_on = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":tools/stone/lamp/b");
		for(FuelOilLamp fuel : iconTextMap.keySet())
		{
			iconMap.put(fuel, register.registerIcon(iconTextMap.get(fuel)));
		}
		super.registerIcons(register);
	}
	
	public IIcon getFuelIcon(String fuelName)
	{
		return iconMap.get(Fuel.getFuel(fuelName));
	}
	
	@Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
		switch(pass)
		{
		case 0 : return itemIcon;
		case 1 : return getLiquidType(stack) != null ? iconMap.get(getLiquidType(stack)) : void_contain;
		case 2 : return hasWick(stack) ? (isBurning(stack) ? candle_on : candle_off) : void_contain;
		}
		return this.void_contain;
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() 
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata) 
	{
		return 3;
	}
	
	protected FuelOilLamp getLiquidType(ItemStack itemstack)
	{
		setupNBT(itemstack);
		Fuel fuel = Fuel.getFuel(itemstack.getTagCompound().getString("Type"));
		return fuel == null ? null : (FuelOilLamp) fuel;
	}
	
	protected boolean hasWick(ItemStack itemstack)
	{
		setupNBT(itemstack);
		
		return itemstack.getTagCompound().getBoolean("Wick");
	}
	
	protected boolean isBurning(ItemStack itemstack)
	{
		setupNBT(itemstack);
		
		return itemstack.getTagCompound().getBoolean("Burning");
	}
	
	protected int getLiquidContain(ItemStack itemstack)
	{
		setupNBT(itemstack);
		int ret = itemstack.getTagCompound().getShort("Contain");
		return ret;
	}
	
	public void setLiquid(ItemStack itemstack, FuelOilLamp type, int contain)
	{
		setupNBT(itemstack);
		if(contain > 1000) contain = 1000;
		if(contain < 0) contain = 0;
		itemstack.getTagCompound().setShort("Contain", (short) contain);
		itemstack.getTagCompound().setString("Type", type.getName());
	}
	
	public void setLiquidContain(ItemStack itemstack, int contain)
	{
		setupNBT(itemstack);
		if(contain > 1000) contain = 1000;
		if(contain < 0) contain = 0;
		itemstack.getTagCompound().setShort("Contain", (short) contain);
	}
	
	public void setBurning(ItemStack itemstack, boolean isBurning)
	{
		setupNBT(itemstack);
		itemstack.getTagCompound().setBoolean("Burning", isBurning);
	}
	
	public void setWick(ItemStack itemstack, boolean hasLine)
	{
		setupNBT(itemstack);
		itemstack.getTagCompound().setBoolean("Wick", hasLine);
	}
	
	@Override
	public void damageItem(EntityLivingBase entity, ItemStack stack,
			ItemDamageResource resource) 
	{
		if(resource == ItemDamageResource.UseItem)
		{
			setLiquidContain(stack, 0);
		}
	}

	@Override
	public int getToolMaxDamage(ItemStack stack) 
	{
		return capacity;
	}

	@Override
	public int getToolDamage(ItemStack stack) 
	{
		return getToolMaxDamage(stack) - getLiquidContain(stack);
	}

	@Override
	public String getToolType(ItemStack stack) 
	{
		return FlaValue.oil_lamp;
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world,
			Entity entity, int p_77663_4_, boolean isFirstSlot) 
	{
		super.onUpdate(itemstack, world, entity, p_77663_4_, isFirstSlot);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) 
	{
		super.getSubItems(item, tab, list);
		ItemStack stack;
		for(int i = 0; i < 2; ++i)
		{
			for(FuelOilLamp fuel : fuelList)
			{
				stack = new ItemStack(item);
				stack.setTagCompound(new NBTTagCompound());
				if(i == 1)	setWick(stack, true);
				setLiquid(stack, fuel, 500);
				list.add(stack.copy());
				setLiquidContain(stack, 1000);
				list.add(stack.copy());
			}
		}
	}
}