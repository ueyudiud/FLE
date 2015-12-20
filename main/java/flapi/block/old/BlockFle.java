package flapi.block.old;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import flapi.FleAPI;
import flapi.cover.Cover;
import flapi.energy.IThermalTileEntity;
import flapi.util.DamageResources;
import flapi.util.FleValue;
import flapi.world.BlockPos;
import fle.api.te.ICoverTE;

/**
 * 
 * @author ueyudiud
 *
 */
public class BlockFle extends Block
{
	/**
	 * The max stack size of block.
	 * @see flapi.block.old.ItemFleBlock
	 */
	protected int maxStackSize = 64;
	/**
	 * The unlocalized name of block, override in block.
	 * @see net.minecraft.block.Block
	 */
	protected final String unlocalizedName;

	protected BlockFle(String aName, Material aMaterial)
	{
		super(aMaterial);
		setBlockName(unlocalizedName = aName);
	    GameRegistry.registerBlock(this, ItemFleBlock.class, getUnlocalizedName());
	}
	protected BlockFle(Class<? extends ItemFleBlock> aItemClass, String aName, Material aMaterial)
	{
		super(aMaterial);
		setBlockName(unlocalizedName = aName);
	    GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
	}
	protected BlockFle(String aName, String aLocalized, Material aMaterial)
	{
		this(aName, aMaterial);
		FleAPI.langManager.registerLocal(new ItemStack(this).getUnlocalizedName() + ".name", aLocalized);
	}
	protected BlockFle(Class<? extends ItemFleBlock> aItemClass, String aName, String aLocalized, Material aMaterial)
	{
		this(aItemClass, aName, aMaterial);
		FleAPI.langManager.registerLocal(new ItemStack(this).getUnlocalizedName() + ".name", aLocalized);
	}
	
	/**
	 * Mark block for update.
	 */
	@Override
	public void onNeighborBlockChange(World aWorld, int x,
			int y, int z, Block block)
	{
		super.onNeighborBlockChange(aWorld, x, y, z, block);
	}
	
	/**
	 * Get block localized name.
	 * @see fle.api.util.ILanguageManager
	 */
	@Override
	public String getLocalizedName()
	{
		return FleAPI.langManager.translateToLocal(getUnlocalizedName() + ".name", new Object[0]);
	}
	
	/**
	 * Set max size per stack.
	 * This method is similar to method of itemBlock of this item.
	 * @see flapi.block.old.ItemFleBlock
	 * @param maxStackSize The max stack size.
	 */
	public void setMaxStackSize(int maxStackSize)
	{
		this.maxStackSize = maxStackSize;
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
	 * Get meta of item during break block and harvest dropping.
	 * @param world Active world.
	 * @param pos The harvest block position.
	 * @return The value of metadata.
	 */
	public int getItemstackMetaOnDroped(World world, BlockPos pos)
	{
		return pos.getBlockMeta();
	}
	
	/**
	 * Get max stack size of block.
	 * @return
	 */
	public int getMaxStackSize()
	{
		return maxStackSize;
	}
	
	/**
	 * The method to check if block require meta blocks.
	 * @return 
	 */
	public boolean hasSubs()
	{
		return false;
	}
	
	/**
	 * Get harvest data.
	 * @param aDamage
	 * @return
	 */
	public short getHarvestData(int aDamage)
	{
		return (short) (hasSubs() ? aDamage : 0);
	}

	@Deprecated
	public boolean requiresMultipleRenderPasses() 
	{
		return false;
	}

	@Deprecated
	public int getRenderPasses() 
	{
		return requiresMultipleRenderPasses() ? 2 : 1;
	}
	
	public IIcon getIcon(int aPass, BlockPos aPos, int aSide)
	{
		return getIcon(aPos.access, aPos.x, aPos.y, aPos.z, aSide);
	}
	
	public IIcon getIcon(int aPass, int aMeta, int aSide)
	{
		return getIcon(aSide, aMeta);
	}
	
	@Override
	public int getDamageValue(World aWorld, int x, int y, int z)
	{
		return hasSubs() ? aWorld.getTileEntity(x, y, z) != null ? aWorld.getTileEntity(x, y, z).getBlockMetadata() : aWorld.getBlockMetadata(x, y, z) : 0;
	}
	
	@Override
	public int damageDropped(int aMeta) 
	{
		return hasSubs() ? aMeta : 0;
	}

	/**
	 * Check block can be select by player.
	 * @return 
	 */
	@Override
	public boolean isCollidable()
	{
		return true;
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
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		metaThread.set(new Integer(getDamageValue(aWorld, x, y, z)));
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
		//Remove all meta of this block in FWM
		FleAPI.mod.getWorldManager().removeData(tPos);
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
	
	@Override
	public final boolean onBlockActivated(World aWorld, int x,
			int y, int z, EntityPlayer aPlayer,
			int aSide, float xPos, float yPos,
			float zPos)
	{
		if(aWorld.getTileEntity(x, y, z) instanceof ICoverTE)
		{
			Cover cover = ((ICoverTE) aWorld.getTileEntity(x, y, z)).getCover(ForgeDirection.VALID_DIRECTIONS[aSide]);
			if(cover != null)
			{
				if(cover.onBlockActive(aWorld, x, y, z, aPlayer, xPos, yPos, zPos)) return true;
			}
		}
		return onBlockActivated(aWorld, x, y, z, aPlayer, ForgeDirection.VALID_DIRECTIONS[aSide], xPos, yPos, zPos);
	}
	
	protected boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer, ForgeDirection dir, float xPos, float yPos, float zPos)
	{
		return false;
	}
	
	/**
	 * Active when entity stand on this block.
	 * Attack entity when entity stand this hot block.
	 */
	@Override
	public void onEntityCollidedWithBlock(World aWorld, int x,
			int y, int z, Entity aEntity)
	{
		super.onEntityCollidedWithBlock(aWorld, x, y, z, aEntity);
		if(aWorld.getTileEntity(x, y, z) instanceof IThermalTileEntity)
		{
			if(((IThermalTileEntity) aWorld.getTileEntity(x, y, z)).getTemperature(ForgeDirection.UNKNOWN) > FleValue.WATER_FREEZE_POINT + 60)
			{
				int t = ((IThermalTileEntity) aWorld.getTileEntity(x, y, z)).getTemperature(ForgeDirection.UNKNOWN) - (FleValue.WATER_FREEZE_POINT + 60);
				aEntity.attackEntityFrom(DamageResources.getHeatDamageSource(), (float) t / 50F);
			}
		}
	}
	
	@Override
	public void onEntityWalking(World aWorld, int x,
			int y, int z, Entity aEntity)
	{
		super.onEntityWalking(aWorld, x, y, z, aEntity);
		if(aWorld.getTileEntity(x, y, z) instanceof IThermalTileEntity)
		{
			if(((IThermalTileEntity) aWorld.getTileEntity(x, y, z)).getTemperature(ForgeDirection.UP) > FleValue.WATER_FREEZE_POINT + 60)
			{
				int t = ((IThermalTileEntity) aWorld.getTileEntity(x, y, z)).getTemperature(ForgeDirection.UP) - (FleValue.WATER_FREEZE_POINT + 60);
				aEntity.attackEntityFrom(DamageResources.getHeatDamageSource(), (float) t / 50F);
			}
		}
	}
}