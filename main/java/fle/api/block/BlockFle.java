package fle.api.block;

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
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.energy.IThermalTileEntity;
import fle.api.util.DamageResources;
import fle.api.world.BlockPos;

public class BlockFle extends Block
{
	protected int maxStackSize = 64;
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
		FleAPI.lm.registerLocal(new ItemStack(this).getUnlocalizedName() + ".name", aLocalized);
	}
	protected BlockFle(Class<? extends ItemFleBlock> aItemClass, String aName, String aLocalized, Material aMaterial)
	{
		this(aItemClass, aName, aMaterial);
		FleAPI.lm.registerLocal(new ItemStack(this).getUnlocalizedName() + ".name", aLocalized);
	}
	
	@Override
	public void onNeighborBlockChange(World aWorld, int x,
			int y, int z, Block block)
	{
		super.onNeighborBlockChange(aWorld, x, y, z, block);
		byte b0 = 32;
		if(!aWorld.isRemote && aWorld.checkChunksExist(x - 32, y - 32, z - 32, x + 32, y + 32, z + 32))
		{
			FLE.fle.getWorldManager().sendData(new BlockPos(aWorld, x, y, z));
		}
	}
	
	@Override
	public String getLocalizedName()
	{
		return FleAPI.lm.translateToLocal(getUnlocalizedName() + ".name", new Object[0]);
	}
	
	public void setMaxStackSize(int maxStackSize)
	{
		this.maxStackSize = maxStackSize;
	}

	public void addInformation(ItemStack aStack, List<String> aList, EntityPlayer aPlayer)
	{
		;
	}
	
	public int getItemstackMetaOnDroped(World world, BlockPos pos)
	{
		return pos.getBlockMeta();
	}
	
	public int getMaxStackSize()
	{
		return maxStackSize;
	}
	
	public boolean hasSubs()
	{
		return false;
	}
	
	public short getHarvestData(int aDamage)
	{
		return (short) (hasSubs() ? aDamage : 0);
	}

	public boolean requiresMultipleRenderPasses() 
	{
		return false;
	}

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

	@Override
	public boolean isCollidable()
	{
		return true;
	}
	
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
	
	protected ThreadLocal<TileEntity> tileThread = new ThreadLocal();
	protected ThreadLocal<Integer> metaThread = new ThreadLocal();
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
	{
		if(aWorld.getTileEntity(x, y, z) != null)
			tileThread.set(aWorld.getTileEntity(x, y, z));
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		metaThread.set(new Integer(getDamageValue(aWorld, x, y, z)));
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
		FLE.fle.getWorldManager().removeData(tPos);
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
	
	@Override
	public MapColor getMapColor(int metadata)
	{
		return mapColor == null ? super.getMapColor(metadata) : mapColor;
	}
	
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