package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.M;
import farcore.lib.block.BlockBase;
import farcore.lib.block.ItemBlockBase;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TESapling;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.UnlocalizedList;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSapling extends BlockBase implements IPlantable, ITileEntityProvider
{
	public BlockSapling()
	{
		super("sapling", ItemSapling.class, Material.plants);
		setHardness(0.3F);
        setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.8F, 0.9F);
        EnumBlock.sapling.set(this);
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		for(Mat material : Mat.register)
		{
			if(material.hasTree)
			{
				register.push(material);
				register.registerIcon(null, material.modid + ":" + getTextureName() + "/" + material.name);
				register.pop();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		register.push(Mat.register.get(meta));
		IIcon icon = register.getIconFromName(null);
		register.pop();
		return icon;
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(IBlockAccess world, int x, int y, int z, int side, INamedIconRegister register)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TESapling)
		{
			register.push(((TESapling) tile).material);
			IIcon icon = register.getIconFromName(null);
			register.pop();
			return icon;
		}
		else
		{
			return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		for(Mat material : Mat.register)
		{
			if(material.hasTree)
			{
				list.add(new ItemStack(item, 1, material.id));
			}
		}
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
		return 0;
	}
	
	@Override
	public int damageDropped(int meta) 
	{
		return 0;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta)
	{
		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching)
	{
		return new ArrayList();
	}
		
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof TESapling)
		{
			((TESapling) tile).setTree(entity, Mat.register.get(stack.getItemDamage(), M.VOID));
		}
	}
	
    /**
     * Can this block stay at this position.
     * Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
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
        return 1;
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TESapling();
	}
	
	@SideOnly(Side.CLIENT)
	public void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean deepInfo)
	{
		
	}
}