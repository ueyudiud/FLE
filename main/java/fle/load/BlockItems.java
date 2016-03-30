package fle.load;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.util.FleLog;
import fle.core.block.BlockCobble;
import fle.core.block.BlockFire;
import fle.core.block.BlockIce;
import fle.core.block.BlockRock;
import fle.core.block.BlockTorch;
import fle.core.block.BlockWater;
import fle.core.tile.TileEntityTorch;
import fle.core.tile.statics.TileEntityRock;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class BlockItems
{
	public static Block torch;
	public static Block fire;
	public static Block water;
	public static Block ice;
	public static Block rock;
	public static Block cobble;
	
	public static void init()
	{
		FleLog.getLogger().info("Start register fle blocks and items.");
		torch = new BlockTorch().setBlockTextureName("fle:tools/torch").setCreativeTab(CreativeTabs.tabDecorations);
		GameRegistry.registerTileEntity(TileEntityTorch.class, "torch");
		fire = new BlockFire().setBlockTextureName("fle:iconsets/fire");
		water = new BlockWater().setBlockTextureName("fle:fluids/water");
		ice = new BlockIce().setBlockTextureName("fle:iconsets/ice");
		rock = new BlockRock().setBlockTextureName("fle:rock").setCreativeTab(CreativeTabs.tabBlock);
		cobble = new BlockCobble().setBlockTextureName("fle:rock").setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerTileEntity(TileEntityRock.class, "rock");
	}
}