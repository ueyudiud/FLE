package fle.core.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockHasTile;
import farcore.enums.Direction;
import farcore.enums.EnumDamageResource;
import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.lib.substance.SubstanceWood;
import farcore.util.LanguageManager;
import farcore.util.U;
import fle.core.tile.TileEntityTorch;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTorch extends BlockHasTile
{
	@SideOnly(Side.CLIENT)
	public IIcon iconExtinguished;
	
	public BlockTorch()
	{
		super("torch", ItemTorch.class, Material.circuits);
		setTickRandomly(true);
		EnumItem.torch.set(new ItemStack(this, 1));
	}
		
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal(getUnlocalizedName() + ".name", "Torch");
	}

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return FarCore.handlerA.getRenderId();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
    	TileEntity tile = world.getTileEntity(x, y, z);
    	if(tile instanceof TileEntityTorch)
    	{
    		return ((TileEntityTorch) tile).isBurning() ? blockIcon : iconExtinguished;
    	}
    	return iconExtinguished;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
    	iconExtinguished = register.registerIcon(getTextureName() + "_extinguished");
    	blockIcon = register.registerIcon(getTextureName() + "_burning");
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tag, List list)
    {
    	for(SubstanceWood wood : SubstanceWood.getWoods())
    	{
    		ItemStack stack = new ItemStack(item, 1);
    		list.add(setSubstance(stack, wood));
    	}
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
    	TileEntity tile;
    	return ((tile = world.getTileEntity(x, y, z)) instanceof TileEntityTorch ?
    			((TileEntityTorch) tile).isBurning() ? 13 : 0 : 0);
    }
    
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityTorch)
		{
			((TileEntityTorch) tile).substance = SubstanceWood.WOOD_VOID;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityTorch)
		{
			((TileEntityTorch) tile).substance = getSubstance(stack);
			((TileEntityTorch) tile).currentBurnTick = getBurnTick(stack);
			((TileEntityTorch) tile).isWet = getIsWet(stack);
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return world.isSideSolid(x - 1, y, z, EAST,  true) ||
				world.isSideSolid(x + 1, y, z, WEST,  true) ||
				world.isSideSolid(x, y, z - 1, SOUTH, true) ||
				world.isSideSolid(x, y, z + 1, NORTH, true) ||
				(World.doesBlockHaveSolidTopSurface(world, x, y, z) || 
						world.getBlock(x, y, z).canPlaceTorchOnTop(world, x, y, z));
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
	{
		if(side == Direction.U.ordinal())
			return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || 
					world.getBlock(x, y - 1, z).canPlaceTorchOnTop(world, x, y - 1, z);
		if(side != Direction.D.ordinal())
		{
			Direction direction = Direction.directions[side].getOpposite();
			return world.isSideSolid(x + direction.x, y, z + direction.z, direction.of(), true);
		}
		return false;
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
        int ret = meta;
        if (side != 0 && canPlaceBlockOnSide(world, x, y, z, side))
        {
            ret = side;
        }
        return ret;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float xPos, float yPos, float zPos)
	{
		if(player.getCurrentEquippedItem() != null &&
				EnumToolType.firestarter.match(player.getCurrentEquippedItem()))
		{
			U.Inventorys.damage(player.getCurrentEquippedItem(), player, 0.08F, EnumDamageResource.USE);
			if(!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityTorch)
			{
				((TileEntityTorch) world.getTileEntity(x, y, z)).setBurnState(true);
			}
			world.updateLightByType(EnumSkyBlock.Block, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		super.onNeighborBlockChange(world, x, y, z, block);
		if(!world.isRemote)
		{
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		}
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(world.isRemote) return;
		if(!canPlaceBlockOnSide(world, x, y, z, world.getBlockMetadata(x, y, z)))
		{
			if(world.getTileEntity(x, y, z) instanceof TileEntityTorch)
			{
				TileEntityTorch tile = (TileEntityTorch) world.getTileEntity(x, y, z);
				dropBlockAsItem(world, x, y, z, tile.getDrop(this));
				tile.removeBlock(0, 0, 0);
				return;
			}
			else
			{
				U.Worlds.removeBlock(world, x, y, z);
			}
		}
	}
	
	@Override
	public void fillWithRain(World world, int x, int y, int z)
	{
		if(!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileEntityTorch)
			{
				((TileEntityTorch) tile).setBurnState(false);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityTorch();
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune, boolean silktouch, TileEntity tile)
	{
		if(tile instanceof TileEntityTorch)
		{
			return new ArrayList(
					Arrays.asList(((TileEntityTorch) tile).getDrop(this)));
		}
		return new ArrayList();
	}
	
	public Direction getBase(World world, int x, int y, int z)
	{
		return Direction.directions[world.getBlockMetadata(x, y, z)];
	}
	
	public void setDirection(World world, int x, int y, int z, Direction direction)
	{
		world.setBlockMetadataWithNotify(x, y, z, direction.ordinal(), 2);
	}
	
	/**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec1, Vec3 vec2)
    {
        int l = world.getBlockMetadata(x, y, z) & 7;
        float f = 0.15F;

        if (l == 5)
        {
            setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (l == 4)
        {
            setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (l == 3)
        {
            setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (l == 2)
        {
            setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else
        {
            f = 0.1F;
            setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }

        return super.collisionRayTrace(world, x, y, z, vec1, vec2);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
    	if(!(world.getTileEntity(x, y, z) instanceof TileEntityTorch &&
    			((TileEntityTorch) world.getTileEntity(x, y, z)).isBurning()))
    	{
    		return;
    	}
        int l = world.getBlockMetadata(x, y, z);
        double d0 = (double)((float)x + 0.5F);
        double d1 = (double)((float)y + 0.7F);
        double d2 = (double)((float)z + 0.5F);
        double d3 = 0.22D;
        double d4 = 0.27D;

        if (l == 5)
        {
            world.spawnParticle("smoke", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 4)
        {
            world.spawnParticle("smoke", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 3)
        {
            world.spawnParticle("smoke", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 2)
        {
            world.spawnParticle("smoke", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            world.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

	public static SubstanceWood getSubstance(ItemStack stack)
	{
		return SubstanceWood.getSubstance(U.Inventorys.setupNBT(stack, false).getString("material"));
	}
	
	public static int getBurnTick(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getInteger("tick");
	}
	
	public static boolean getIsWet(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getBoolean("wet");
	}
	
	public static ItemStack setItemStack(ItemStack stack, SubstanceWood substance, int tick, boolean isWet)
	{
		NBTTagCompound nbt = U.Inventorys.setupNBT(stack, true);
		nbt.setString("material", substance.getName());
		nbt.setInteger("tick", tick);
		nbt.setBoolean("wet", isWet);
		return stack;
	}
	
	public static ItemStack setSubstance(ItemStack stack, SubstanceWood substance)
	{
		NBTTagCompound nbt = U.Inventorys.setupNBT(stack, true);
		nbt.setString("material", substance.getName());
		nbt.setInteger("tick", TileEntityTorch.getWoodBurnTime(substance));
		return stack;
	}
	
	public static ItemStack setBurnTick(ItemStack stack, int tick)
	{
		U.Inventorys.setupNBT(stack, true).setInteger("tick", tick);
		return stack;
	}
}