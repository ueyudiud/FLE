package farcore.alpha.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.alpha.enums.EnumBlockType;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.IRegisteredNameable;
import farcore.alpha.util.IconHook;
import farcore.alpha.util.LangHook.UnlocalizedList;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class BlockBase extends Block
implements IRegisteredNameable
{
	public final EnumBlockType type;
	public final String blockName;
	
	protected BlockBase(EnumBlockType type, String name, Class<? extends ItemBlockBase> clazz, Material material, Object...objects)
	{
		super(material);
		this.type = type;
		setBlockName(blockName = name);
		GameRegistry.registerBlock(this, clazz, name, objects);
	}
	protected BlockBase(EnumBlockType type, String name, Material material)
	{
		super(material);
		this.type = type;
		setBlockName(blockName = name);
		GameRegistry.registerBlock(this, ItemBlockBase.class, name);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return blockName;
	}
	
	@Override
	public String getLocalizedName()
	{
		return FarCoreSetup.lang.translateToLocal(getUnlocalizedName() + ".name");
	}
	
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		return getTranslateNameForItemStack(stack.getItemDamage());
	}
	
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName() + "@" + metadata;
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float xPos, float yPos, float zPos)
	{
		return false;
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}
		
	@SideOnly(Side.CLIENT)
	public final void registerBlockIcons(IIconRegister register)
	{
		IconHook.push(this);
		IconHook.set(register);
		registerIcon(IconHook.instance);
		IconHook.unset();
		IconHook.pop();
	}
	
	@SideOnly(Side.CLIENT)
	public final IIcon getIcon(int side, int meta)
	{
		IconHook.push(this);
		IIcon ret = getIcon(side, meta, IconHook.instance);
		IconHook.pop();
		return ret == null ? V.voidBlockIcon : ret;
	}
	
	@SideOnly(Side.CLIENT)
	public final IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) 
	{
		IconHook.push(this);
		IIcon ret = getIcon(world, x, y, z, side, IconHook.instance);
		IconHook.pop();
		return ret == null ? V.voidBlockIcon : ret;
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(IBlockAccess world, int x, int y, int z, int side, INamedIconRegister register)
	{
		return getIcon(side, world.getBlockMetadata(x, y, z), register);
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return register.getIconFromName(null);
	}

	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		register.registerIcon(null, getTextureName());
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}
	
	@Override
	public Item getItemDropped(int meta, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}
	
	@Override
	public String getRegisteredName()
	{
		return GameData.getBlockRegistry().getNameForObject(this);
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
    public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
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
    	else
    	{
    		return super.getDrops(world, x, y, z, metadata, fortune);
    	}
    }
    
    public void onBlockHarvest(World world, EntityPlayer player, int x, int y, int z, int meta, boolean silkTouching)
    {
    	
    }
    
    @SideOnly(Side.CLIENT)
    public final void addInfomation(ItemStack stack, EntityPlayer player, List<String> list, boolean deepInfo)
    {
    	addUnlocalizedInfomation(stack, player, new UnlocalizedList(list), deepInfo);
    }

    @SideOnly(Side.CLIENT)
    public void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean deepInfo)
    {
    	
    }
}