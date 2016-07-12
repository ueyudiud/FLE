package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.lib.block.BlockUpdatable;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.IconHook;
import farcore.lib.util.SubTag;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrop extends BlockUpdatable implements ITileEntityProvider
{
	public BlockCrop()
	{
		super("crop", ItemCrop.class, Material.plants);
		EnumBlock.crop.set(this);
		setHardness(0.5F);
		minX = 0.03125F;
		minZ = 0.03125F;
		maxX = 0.96875F;
		maxY = 0.96875F;
		maxZ = 0.96875F;
	}
	
	@Override
	public int getRenderType()
	{
		return FarCore.handlerA.getRenderId();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if(!canBlockStay(world, x, y, z))
		{
			world.setBlockToAir(x, y, z);
		}
		super.onNeighborBlockChange(world, x, y, z, block);
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof TECrop)
		{
			return ((TECrop) tile).canPlantAt();
		}
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TECrop)
		{
			((TECrop) tile).initCrop(ItemCrop.getGeneration(stack), ItemCrop.getDNA(stack), ItemCrop.getCrop(stack));
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		for(Mat material : Mat.register)
		{
			if(material.contain(SubTag.CROP))
			{
				register.push(material);
				if(material.crop != null)
				{
					material.crop.registerIcon(register);
				}
				register.pop();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(IBlockAccess world, int x, int y, int z, int side, INamedIconRegister register)
	{
		TECrop tile = (TECrop) world.getTileEntity(x, y, z);
		return tile.crop().getIcon(tile, register);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune, boolean silktouch,
			TileEntity tile)
	{
		if(tile == null)
		{
			return new ArrayList();
		}
		ArrayList<ItemStack> list = new ArrayList();
		((TECrop) tile).crop().getDrops((TECrop) tile, list);
		return list;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TECrop();
	}
}