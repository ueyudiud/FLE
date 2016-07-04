package farcore.alpha.block.tree;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.block.BlockBase;
import farcore.alpha.block.ItemBlockBase;
import farcore.alpha.enums.EnumBlockType;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.block.ISmartBurnableBlock;
import farcore.enums.EnumBiome;
import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.lib.substance.SubstanceWood;
import farcore.util.U.OreDict;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
			BlockLogNatural log = (BlockLogNatural) new BlockLogNatural("alpha.log." + wood.getName(), Material.wood, wood, wood.getLocalName(), 1F + wood.hardness / 2F, wood.hardness / 2.5F)
					.setBlockTextureName("farcore:logs/" + wood.getName());
			BlockLogArtificial log1 = (BlockLogArtificial) new BlockLogArtificial("alpha.log.artificial" + wood.getName(), Material.wood, wood.getLocalName(), 1F + wood.hardness / 2F, wood.hardness / 2.5F)
					.setBlockTextureName("farcore:logs/" + wood.getName());
			BlockLeaves leaves = (BlockLeaves) new BlockLeaves(log, wood, "alpha.leaves." + wood.getName(), Material.leaves)
					.setBlockTextureName("farcore:leaves/" + wood.getName());
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
				EnumItem.log_block.set(log1);
			}
			OreDict.registerValid("treeLeaves", leaves);
			OreDict.registerValid("logWood", log1);
			wood.generator.initLogBlock(log, leaves);
		}
		Block sapling;
		OreDict.registerValid("treeSapling", sapling = new BlockSapling("alpha.sapling").setBlockTextureName("farcore:sapling"));
		Map<String, BlockLogArtificial> map = builder.build();
		EnumBlock.sapling.setBlock(sapling);
		EnumItem.log_block.setInfomationable((int size, Object...objects) ->
		{
			if(objects.length == 1)
			{
				Block block;
				if(objects[0] instanceof SubstanceWood)
				{
					block = map.get(((SubstanceWood) objects[0]).getName());
				}
				else
				{
					block = map.get(objects[0]);
				}
				if(block != null)
				{
					return new ItemStack(block, size);
				}
			}
			return null;
		});
		GameRegistry.registerTileEntity(TESapling.class, "sapling");
	}
	
	public final float hardnessMultiplier;
	public final float resistanceMultiplier;
	
	protected BlockLog(String name, Class<? extends ItemBlockBase> clazz, Material material, float hardness, float resistance, Object...objects)
	{
		super(EnumBlockType.plant, name, clazz, material, objects);
		setStepSound(soundTypeWood);
		setHardness(this.hardnessMultiplier = hardness);
		setResistance(this.resistanceMultiplier = resistance);
	}
	protected BlockLog(String name, Material material, float hardness, float resistance)
	{
		super(EnumBlockType.plant, name, material);
		setStepSound(soundTypeWood);
		setHardness(this.hardnessMultiplier = hardness);
		setResistance(this.resistanceMultiplier = resistance);
	}
	
	@Override
	public String getHarvestTool(int metadata)
	{
		return "axe";
	}
	
	@Override
	public boolean isToolEffective(String type, int metadata)
	{
		return "axe".equals(type);
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		register.registerIcon("side", getTextureName() + "_side");
		register.registerIcon("top", getTextureName() + "_top");
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return  register.getIconFromName((meta == 0 ? side == 0 || side == 1 : 
			meta == 1 ? side == 2 || side == 3 :
				meta == 2 ? side == 4 || side == 5 :
					false) ? "top" : "side");
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta & (~0x3);
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
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face, int level)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getFireIcon(IBlockAccess world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public int canBeBurned(World world, int x, int y, int z, int level)
	{
		return 12;
	}
}