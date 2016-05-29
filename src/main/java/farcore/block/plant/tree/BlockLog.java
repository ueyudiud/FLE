package farcore.block.plant.tree;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.BlockBase;
import farcore.block.ItemBlockBase;
import farcore.enums.EnumItem;
import farcore.interfaces.ISmartBurnableBlock;
import farcore.lib.collection.Register;
import farcore.lib.substance.SubstanceWood;
import farcore.util.LanguageManager;
import farcore.util.U.OreDict;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockLog extends BlockBase implements ISmartBurnableBlock
{
	public static void init()
	{
		Builder<String, BlockLogArtificial> builder = ImmutableMap.builder();
		for(SubstanceWood wood : SubstanceWood.getWoods())
		{
			BlockLogNatural log = new BlockLogNatural(wood);
			BlockLogArtificial log1 = new BlockLogArtificial(log, wood);
			BlockLeaves leaves = new BlockLeaves(wood);
			builder.put(wood.getName(), log1);
			leaves.log = log;
			wood.log = log;
			wood.logArt = log1;
			wood.leaves = leaves;
			if(wood == SubstanceWood.WOOD_VOID)
			{
				log.setCreativeTab(null);
				log1.setCreativeTab(null);
				leaves.setCreativeTab(null);
				EnumItem.log_block.set(new ItemStack(log1));
			}
			OreDict.registerValid("treeLeaves", leaves);
			OreDict.registerValid("logWood", log1);
			wood.generator.initLogBlock(log, leaves);
		}
		ItemLogArtificial.map = builder.build();
	}
	
	@SideOnly(Side.CLIENT)
	private IIcon sideIcon;
	
	protected BlockLog(String name, Material material)
	{
		super(name, material);
	}
	protected BlockLog(String name, Class<? extends ItemBlockBase> clazz, Material material, Object...objects)
	{
		super(name, clazz, material, objects);
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager) 
	{
		manager.registerLocal(getUnlocalizedName() + ".name", "%s Log");
	}

	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(getTextureName() + "_top");
		sideIcon = register.registerIcon(getTextureName() + "_side");
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return (meta == 0 ? side == 0 || side == 1 : 
			meta == 1 ? side == 2 || side == 3 :
				meta == 2 ? side == 4 || side == 5 :
					false) ? 
				blockIcon : sideIcon;
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) & (~0x3);
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
		meta &= (~0x3);
		if(side == 0 || side == 1)
		{
			meta |= 0x0;
		}
		else if(side == 2 || side == 3)
		{
			meta |= 0x1;
		}
		else if(side == 4 || side == 5)
		{
			meta |= 0x2;
		}
		return meta;
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta & (~0x3);
	}
	
	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean onBurning(World world, int x, int y, int z, int level)
	{
		return true;
	}

	@Override
	public boolean onFlame(World world, int x, int y, int z, ForgeDirection face, int level)
	{
		return false;
	}
	
	@Override
	public boolean isFlammable(World world, int x, int y, int z, ForgeDirection face, int level)
	{
		return true;
	}
	
	@Override
	public IIcon getFireIcon(IBlockAccess world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public int canBeBurned(World world, int x, int y, int z, int level)
	{
		return 6;
	}
}