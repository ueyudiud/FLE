package flapi.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import flapi.FleAPI;
import flapi.cover.Cover;
import fle.api.te.ICoverTE;

/**
 * The base class of block.
 * @author ueyudiud
 *
 */
public class BlockFle extends Block
{
	protected int maxSize;

	/**
	 * The unlocalized name of block, override in block.
	 * @see net.minecraft.block.Block
	 */
	protected final String unlocalizedName;

	protected BlockFle(String unlocalized, Material Material)
	{
		super(Material);
		setBlockName(unlocalizedName = unlocalized);
		GameRegistry.registerBlock(this, unlocalized);
	}
	protected BlockFle(Class<? extends ItemBlock> clazz, String unlocalized, Material Material)
	{
		super(Material);
		setBlockName(unlocalizedName = unlocalized);
		GameRegistry.registerBlock(this, clazz, unlocalized);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return super.getUnlocalizedName();
	}
	
	@Override
	public String getLocalizedName()
	{
		return FleAPI.langManager.translateToLocal(getUnlocalizedName(), getTransObject());
	}
	
	protected Object[] getTransObject()
	{
		return new Object[0];
	}
	
	/**
	 * Add tool tip on GUI when display this item on slot.
	 * @param aStack
	 * @param aList
	 * @param aPlayer
	 */
	public void addInformation(ItemStack aStack, List<String> aList, EntityPlayer aPlayer)
	{
		;
	}
	
	/**
	 * Get max stack size of block.
	 * @return
	 */
	public int getMaxStackSize()
	{
		return maxSize;
	}
	
	/**
	 * Get entity facing from world.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param entity
	 * @return
	 */
    protected ForgeDirection getPointFacing(World world, int x, int y, int z, EntityLivingBase entity)
    {
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch(l)
        {
        case 0 : return ForgeDirection.SOUTH;
        case 1 : return ForgeDirection.WEST;
        case 2 : return ForgeDirection.NORTH;
        case 3 : return ForgeDirection.EAST;
        default : return ForgeDirection.UNKNOWN;
        }
    }

    /**
     * Get facing from active position.
     * @param world
     * @param x
     * @param y
     * @param z
     * @param xPos
     * @param yPos
     * @param zPos
     * @return
     */
	protected ForgeDirection getPointFacing(World world, int x, int y, int z, double xPos, double yPos, double zPos)
    {
    	double a = xPos;
    	double b = yPos;
    	double c = zPos;
    	
    	ForgeDirection dir = ForgeDirection.UNKNOWN;
    	
    	if(b == 0.00D)
    	{
    		dir = ForgeDirection.DOWN;
    	}
    	if(a == 0.00D)
    	{
    		dir = ForgeDirection.WEST;
    	}
    	if(c == 0.00D)
    	{
    		dir = ForgeDirection.SOUTH;
    	}
    	if(b == 1.00D)
    	{
    		dir = ForgeDirection.UP;
    	}
    	if(a == 1.00D)
    	{
    		dir = ForgeDirection.EAST;
    	}
    	if(c == 1.00D)
    	{
    		dir = ForgeDirection.NORTH;
    	}
    	return dir;
    }
	
	/**
	 * The local thread correct tile entity of block broken.
	 */
	protected ThreadLocal<TileEntity> tileThread = new ThreadLocal();
	/**
	 * The local thread correct meta of block broken.
	 */
	protected ThreadLocal<Integer> metaThread = new ThreadLocal();
	
	/**
	 * Saving data during breaking block.
	 */
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
	{
		if(aWorld.getTileEntity(x, y, z) != null)
		{
			TileEntity tile = aWorld.getTileEntity(x, y, z);
			tileThread.set(tile);
			if(tile instanceof ICoverTE)
			{
				for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
				{
					Cover cover = ((ICoverTE) tile).getCover(dir);
					if(cover != null)
					{
						cover.onBlockBreak(aWorld, x, y, z);
					}
				}
			}
		}
		metaThread.set(new Integer(getDamageValue(aWorld, x, y, z)));
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}
	
private MapColor mapColor = null;
	
	public BlockFle setMapColor(MapColor aColor)
	{
		mapColor = aColor;
		return this;
	}
	
	@Override
	public BlockFle setBlockTextureName(String aName)
	{
		super.setBlockTextureName(aName);
		return this;
	}
	
	@Override
	public BlockFle setHardness(float aH)
	{
		super.setHardness(aH);
		return this;
	}
	
	@Override
	public BlockFle setResistance(float aR)
	{
		super.setResistance(aR);
		return this;
	}
	
	@Override
	public BlockFle setCreativeTab(CreativeTabs aTab)
	{
		super.setCreativeTab(aTab);
		return this;
	}
	
	@Override
	public BlockFle setStepSound(SoundType aSound)
	{
		super.setStepSound(aSound);
		return this;
	}
	
	/**
	 * Get map color from block.
	 */
	@Override
	public MapColor getMapColor(int metadata)
	{
		return mapColor == null ? super.getMapColor(metadata) : mapColor;
	}
	
	/**
	 * Return if this block has sub block.
	 * @return
	 */
	protected boolean hasSubs()
	{
		return false;
	}
	
	@Override
	public final int getDamageValue(World world, int x,
			int y, int z)
	{
		return getMeta(world, x, y, z);
	}
	
	/**
	 * Get default meta for block.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return The metadata of block.
	 */
	public int getMeta(IBlockAccess world, int x, int y, int z)
	{
		return hasSubs() ? world.getBlockMetadata(x, y, z) : 0;
	}
	
	public int getMetaFromItemStack(ItemStack stack)
	{
		return hasSubs() ? stack.getItemDamage() : 0;
	}
	
	public int getPlacedMetaFromItemStack(World world, int x, int y, int z, int side, float xPos, float yPos, float zPos, ItemStack stack)
	{
		return getMetaFromItemStack(stack);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,
			int y, int z, EntityPlayer player,
			int side, float xPos, float yPos,
			float zPos)
	{
		return super.onBlockActivated(world, x, y, z, player, side, xPos, yPos, zPos);
	}
	
	@Override
	public int onBlockPlaced(World world, int x,
			int y, int z, int side,
			float xPos, float yPos, float zPos,
			int meta)
	{
		return super.onBlockPlaced(world, x, y, z, side, xPos, yPos, zPos, meta);
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z,
			Explosion explosion)
	{
		super.onBlockExploded(world, x, y, z, explosion);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x,
			int y, int z)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}
	
	@Override
	public boolean canBlockStay(World world, int x,
			int y, int z)
	{
		return true;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		super.registerBlockIcons(register);
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x,
			int y, int z, int s)
	{
		return getIcon(s, getMeta(world, x, y, z));
	}
	
	@Override
	public IIcon getIcon(int s, int meta)
	{
		return blockIcon;
	}
	
	@Override
	protected void dropBlockAsItem(World world, int x,
			int y, int z, ItemStack stack)
	{
		super.dropBlockAsItem(world, x, y, z, stack);
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return hasSubs() ? meta : 0;
	}
	
	@Override
	public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        getDrops(ret, world, x, y, z, metaThread.get(), fortune, tileThread.get());
        return ret;
	}
	
	protected void getDrops(List<ItemStack> list, World world, int x, int y, int z, int meta, int fortune, TileEntity tile)
	{
		Item i = getItemDropped(meta, world.rand, fortune);
		if(i != null)
		{
			list.add(new ItemStack(getItemDropped(meta, world.rand, fortune), quantityDropped(meta, fortune, world.rand), meta));
		}
	}
	
	@Override
	public ForgeDirection[] getValidRotations(World world, int x, int y,
			int z)
	{
		return RotationHelper.getValidVanillaBlockRotations(this);
	}
	
	@Override
	public boolean rotateBlock(World world, int x, int y, int z,
			ForgeDirection axis)
	{
		return false;
	}
}