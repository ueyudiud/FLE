package farcore.block.plant.crop;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockHasTile;
import farcore.enums.EnumBlock;
import farcore.lib.crop.CropCard;
import farcore.lib.crop.CropManager;
import farcore.lib.crop.TileEntityCrop;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrop extends BlockHasTile
{
	public BlockCrop(String name)
	{
		super(name, ItemCrop.class, Material.plants);
		EnumBlock.crop.setBlock(this);
		setHardness(0.5F);
		minX = 0.03125F;
		minZ = 0.03125F;
		maxX = 0.96875F;
		maxY = 0.96875F;
		maxZ = 0.96875F;
	}
	
	@Override
	public int getRenderBlockPass()
	{
		return 0;
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
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(!canBlockStay(world, x, y, z))
		{
			world.setBlockToAir(x, y, z);
		}
		super.updateTick(world, x, y, z, rand);
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof TileEntityCrop)
		{
			return ((TileEntityCrop) tile).canPlantAt();
		}
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityCrop)
		{
			((TileEntityCrop) tile).initCrop(ItemCrop.getGeneration(stack), ItemCrop.getDNA(stack), ItemCrop.getCrop(stack));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		for(CropCard card : CropManager.getRegister())
		{
			card.registerIcon(register);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		TileEntityCrop tile = (TileEntityCrop) world.getTileEntity(x, y, z);
		return tile.getCrop().getIcon(tile);
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
		((TileEntityCrop) tile).getCrop().getDrops((TileEntityCrop) tile, list);
		return list;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityCrop();
	}
}