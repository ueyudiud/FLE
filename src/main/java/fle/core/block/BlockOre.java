package fle.core.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.block.BlockBase;
import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import farcore.enums.EnumBlock;
import farcore.enums.EnumBlock.IInfoSpawnable;
import farcore.enums.EnumItem;
import farcore.interfaces.ISmartHarvestBlock;
import farcore.lib.substance.SubstanceMineral;
import farcore.lib.substance.SubstanceRock;
import fle.core.tile.statics.TileEntityOre;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockOre extends BlockHasTile
implements IInfoSpawnable, ISmartHarvestBlock
{
	private String tool;
	private int level;
	
	public BlockOre()
	{
		super("ore", ItemOre.class, Material.rock);
		setResistance(8.0F);
		EnumItem.ore_block.set(new ItemStack(this));
		EnumBlock.ore.setBlock(this, EnumItem.ore_block);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(SubstanceMineral mineral : SubstanceMineral.getMinerals())
		{
			if(mineral == SubstanceMineral.VOID_MINERAL) continue;
			int id = mineral.getID();
			for(SubstanceRock rock : SubstanceRock.getRocks())
			{
				if(rock == SubstanceRock.VOID_ROCK) continue;
				list.add(new ItemStack(item, 1, (rock.getID() << 10) | id));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		for(SubstanceMineral mineral : SubstanceMineral.getMinerals())
		{
			mineral.icon = register.registerIcon(getTextureName() + "/" + mineral.getName());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.stone.getIcon(side, meta);
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderType()
	{
		return FarCore.handlerB.getRenderId();
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityOre)
		{
			return (((TileEntityOre) tile).rock.getID() << 10) | ((TileEntityOre) tile).mineral.getID();
		}
		return 0;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
		TileEntityOre tile;
		if(world.getTileEntity(x, y, z) instanceof TileEntityOre)
		{
			tile = (TileEntityOre) world.getTileEntity(x, y, z);
			tile.rock = SubstanceRock.getSubstance(stack.getItemDamage() >> 10);
			tile.mineral = SubstanceMineral.getSubstance(stack.getItemDamage() & 0x3FF);
			tile.setup = true;
		}
	}

	@Override
	public boolean canHarvestBlock(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityOre)
		{
			TileEntityOre ore = (TileEntityOre) world.getTileEntity(x, y, z);
			tool = ore.mineral.harvestTool;
			level = Math.max(ore.mineral.harvestLevel, ore.rock.harvestLevel);
			return ForgeHooks.canHarvestBlock(this, player, 0);
		}
		return false;
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityOre)
		{
			return Math.max(((TileEntityOre) tile).mineral.hardness, ((TileEntityOre) tile).rock.hardness);
		}
		return 1.0F;
	}
	
	@Override
	public String getHarvestTool(int metadata)
	{
		return tool;
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		return level;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune, boolean silktouch,
			TileEntity tile)
	{
		if(tile instanceof TileEntityOre)
		{
			ArrayList<ItemStack> list = new ArrayList();
			list.add(new ItemStack(this, 1, (((TileEntityOre) tile).rock.getID() << 10) | ((TileEntityOre) tile).mineral.getID()));
			return list;
		}
		return super.getDrops(world, x, y, z, metadata, fortune, silktouch, tile);
	}

	@Override
	public void harvestAndDropBlock(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		harvestBlock(world, player, x, y, z, meta);
	}

	@Override
	public boolean spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof SubstanceMineral)
			{
				if(!world.setBlock(x, y, z, this)) return false;
				TileEntity tile = world.getTileEntity(x, y, z);
				if(tile instanceof TileEntityOre)
				{
					((TileEntityOre) tile).mineral = (SubstanceMineral) objects[0];
					return true;
				}
			}
			else if(objects[0] instanceof String)
			{
				if(!world.setBlock(x, y, z, this)) return false;
				TileEntity tile = world.getTileEntity(x, y, z);
				if(tile instanceof TileEntityOre)
				{
					((TileEntityOre) tile).mineral = SubstanceMineral.getSubstance((String) objects[0]);
					return true;
				}
			}
		}
		else if(objects.length == 2)
		{
			if(objects[0] instanceof SubstanceMineral && objects[1] instanceof SubstanceRock)
			{
				if(!world.setBlock(x, y, z, this)) return false;
				TileEntity tile = world.getTileEntity(x, y, z);
				if(tile instanceof TileEntityOre)
				{
					((TileEntityOre) tile).mineral = (SubstanceMineral) objects[0];
					((TileEntityOre) tile).rock = (SubstanceRock) objects[1];
					return true;
				}
			}
			else if(objects[0] instanceof String && objects[1] instanceof String)
			{
				if(!world.setBlock(x, y, z, this)) return false;
				TileEntity tile = world.getTileEntity(x, y, z);
				if(tile instanceof TileEntityOre)
				{
					((TileEntityOre) tile).mineral = SubstanceMineral.getSubstance((String) objects[0]);
					((TileEntityOre) tile).rock = SubstanceRock.getSubstance((String) objects[1]);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityOre();
	}
}