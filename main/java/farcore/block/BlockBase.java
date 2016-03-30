package farcore.block;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player))
        {
        	onBlockHarvest(world, player, x, y, z, meta, true);
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = createStackedBlock(meta);

            if (itemstack != null)
            {
                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items)
            {
                this.dropBlockAsItem(world, x, y, z, is);
            }
        }
        else
        {
        	onBlockHarvest(world, player, x, y, z, meta, false);
            harvesters.set(player);
            int i1 = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(world, x, y, z, meta, i1);
            harvesters.set(null);
        }
    }
    
    protected void onBlockHarvest(World world, EntityPlayer player, int x, int y, int z, int meta, boolean silkTouching)
    {
    	
    }
}