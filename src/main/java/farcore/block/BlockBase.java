package farcore.block;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.util.LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockBase extends Block
{
	private String unlocalized;

	protected BlockBase(String name, Class<? extends ItemBlockBase> clazz, Material material, Object...objects)
	{
		super(material);
		setBlockName(name);
		GameRegistry.registerBlock(this, clazz, name, objects);
	}
	protected BlockBase(String name, Material material)
	{
		super(material);
		setBlockName(name);
		GameRegistry.registerBlock(this, ItemBlockBase.class, name);
	}
	protected BlockBase(Material material)
	{
		super(material);
	}

	public void registerLocalizedName(LanguageManager manager)
	{
		
	}
	
	public ItemBlockBase getItem()
	{
		return (ItemBlockBase) Item.getItemFromBlock(this);
	}
	
	@Override
	public BlockBase setBlockName(String name)
	{
		this.unlocalized = name;
		return this;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return "block.fle." + unlocalized;
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}
	
	public String getMetadataName(int meta)
	{
		return null;
	}
	
	public Object[] getTranslateObject(ItemStack stack)
	{
		return new Object[0];
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float xPos, float yPos, float zPos)
	{
		return false;
	}
	
	/**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        //Instead by cause food exhaustion during digging.
//        player.addExhaustion(0.025F);

        if (canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player))
        {
        	onBlockHarvest(world, player, x, y, z, meta, true);
            ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, 0, true);
            
            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items)
            {
                dropBlockAsItem(world, x, y, z, is);
            }
        }
        else
        {
        	onBlockHarvest(world, player, x, y, z, meta, false);
            harvesters.set(player);
            int i1 = EnchantmentHelper.getFortuneModifier(player);
            dropBlockAsItem(world, x, y, z, meta, i1);
            harvesters.set(null);
        }
    }
    
    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z,
    		int meta, float chance, int fortune)
    {
        if (!world.isRemote && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, fortune, false);
            chance = ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, fortune, chance, false, harvesters.get());

            for (ItemStack item : items)
            {
                if (world.rand.nextFloat() <= chance)
                {
                    dropBlockAsItem(world, x, y, z, item);
                }
            }
        }
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	return getDrops(world, x, y, z, metadata, fortune, false);
    }
    
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune, boolean silkTouching)
    {
    	if(silkTouching)
    	{
    		ArrayList<ItemStack> ret = new ArrayList();
    		ItemStack stack = createStackedBlock(metadata);
    		if(stack != null)
    			ret.add(stack);
    		return ret;
    	}
    	return super.getDrops(world, x, y, z, metadata, fortune);
    }
    
    protected void onBlockHarvest(World world, EntityPlayer player, int x, int y, int z, int meta, boolean silkTouching)
    {
    	
    }
    
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x,
    		int y, int z)
    {
    	return super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }
}