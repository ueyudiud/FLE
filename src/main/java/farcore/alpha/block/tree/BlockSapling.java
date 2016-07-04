package farcore.alpha.block.tree;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.alpha.block.BlockBase;
import farcore.alpha.enums.EnumBlockType;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.interfaces.ISmartPlantableBlock;
import farcore.lib.substance.SubstanceWood;
import farcore.util.FleLog;
import farcore.util.SubTag;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSapling extends BlockBase
implements ISmartPlantableBlock, ITileEntityProvider
{
	public BlockSapling(String name)
	{
		super(EnumBlockType.plant, name, ItemSapling.class, Material.plants);
	}
	
	@Override
	public int getRenderType()
	{
		return FarCore.handlerA.getRenderId();
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
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		for(SubstanceWood wood : SubstanceWood.getWoods())
		{
			register.registerIcon(wood.getName(), getTextureName() + "/" + wood.getName());
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(IBlockAccess world, int x, int y, int z, int side, INamedIconRegister register)
	{
		try
		{
			return register.getIconFromName(((TESapling) world.getTileEntity(x, y, z)).wood.getName());
		}
		catch(Exception exception)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().catching(exception);
			}
			return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return register.getIconFromName(SubstanceWood.getWoods().name(meta));	
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		for(SubstanceWood wood : SubstanceWood.getWoods())
		{
			list.add(ItemSapling.setWood(new ItemStack(item), wood));
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y,
			int z)
	{
		return null;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if(!canBlockStay(world, x, y, z))
		{
			world.setBlockToAir(x, y, z);
			world.removeTileEntity(x, y, z);
		}
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
		return 0;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return U.Plants.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
	{
		return side == 1 && canPlaceBlockAt(world, x, y, z);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof TESapling)
		{
			((TESapling) tile).setWood(ItemSapling.getWood(stack));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TESapling();
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
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public boolean useDefaultType()
	{
		return true;
	}

	@Override
	public String getSmartPlantType(IBlockAccess world, int x, int y, int z)
	{
		return SubTag.PlantType_Plains.name();
	}
	
	@Override
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		return true;
	}
}