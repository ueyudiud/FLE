package farcore.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import farcore.block.interfaces.IHarvestHandler;
import farcore.resource.BlockTextureManager;
import farcore.resource.TileMetaTextureManager;
import flapi.util.BlockTextureHandler;

public class BlockFleWithMetadata extends BlockFle implements ITileEntityProvider, IHarvestHandler
{
	protected final String[] names = new String[16];
	protected final Class<? extends TEBase>[] teClazz = new Class[16];
	
	protected BlockFleWithMetadata(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
		iconHandler = new BlockTextureHandler(new TileMetaTextureManager());
	}
	protected BlockFleWithMetadata(Class<? extends ItemBlock> clazz,
			String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
		iconHandler = new BlockTextureHandler(new TileMetaTextureManager());
	}

	protected void addSubBlock(int i, String name, Class<? extends TEBase> tile, BlockTextureManager manager)
	{
		addSubBlock(i, name, tile, manager, true);
	}
	protected void addSubBlock(int i, String name, Class<? extends TEBase> tile, BlockTextureManager manager, boolean registerTile)
	{
		if(i >= 16 || i < 0)
		{
			throw new ArrayIndexOutOfBoundsException("FLE API : Block id can not more than 16.");
		}
		if(registerTile)
			GameRegistry.registerTileEntity(tile, "fle.tile." + name.replace(' ', '.').toLowerCase());
		names[i] = name;
		teClazz[i] = tile;
		((TileMetaTextureManager) iconHandler.getHandler()).addTexture(name, i, manager);
	}
	
	@Override
	protected String getTranslateName(int i)
	{
		return getUnlocalizedName() + "@" + i;
	}
	
	@Override
	public BlockFle setBlockTextureName(String name)
	{
		return this;
	}
	
	@Override
	public boolean onPlacedAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		if (!world.setBlock(x, y, z, this, metadata, 3))
		{
			return false;
		}
		
		if (world.getBlock(x, y, z) == this)
		{
			onBlockPlacedBy(world, x, y, z, player, stack);
			onPostBlockPlaced(world, x, y, z, metadata);
		}
		
		if (world.getTileEntity(x, y, z) instanceof TEBase)
		{
			a(world, x, y, z).onBlockPlaced(stack, player, side, hitX, hitY, hitZ);
		}

		return true;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,
			int y, int z, EntityPlayer player,
			int side, float hitX, float hitY,
			float hitZ)
	{
		if(world.getTileEntity(x, y, z) instanceof TEBase)
		{
			return a(world, x, y, z).onBlockActivated(player, side, hitX, hitY, hitZ);
		}
		return false;
	}
	
	@Override
	public boolean canHarvestBlock(World world, int x, int y, int z,
			int metadata, String toolKey, int level)
	{
		return a(world, x, y, z).canHarvestBlock(metadata, toolKey, level);
	}
	
	@Override
	public ArrayList<ItemStack> getHarvestDrop(World world, int x, int y,
			int z, int metadata, String toolKey, int level)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		a(world, x, y, z).onDrop(this, ret, metadata, toolKey, level);
		return ret;
	}
	
	@Override
	public boolean recolourBlock(World world, int x, int y, int z,
			ForgeDirection side, int colour)
	{
		return a(world, x, y, z).recolourBlock(side, colour);
	}
	
	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z)
	{
		if(world.getTileEntity(x, y, z) instanceof TEBase)
		{
			a(world, x, y, z).getLightOpacity();
		}
		return super.getLightOpacity(world, x, y, z);
	}
	
	@Override
	public void onEntityWalking(World world, int x,
			int y, int z, Entity entity)
	{
		a(world, x, y, z).onEntityWalking(entity);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab,
			List list)
	{
		for(int i = 0; i < 16; ++i)
			if(teClazz[i] != null)
				list.add(new ItemStack(item, 1, i));
	}
	
	@Override
	public TEBase createNewTileEntity(World world, int meta)
	{
		try
		{
			return teClazz[meta].newInstance();
		}
		catch(Throwable e)
		{
			return null;
		}
	}
	
	protected static TEBase a(IBlockAccess world, int x, int y, int z)
	{
		return (TEBase) world.getTileEntity(x, y, z);
	}
}