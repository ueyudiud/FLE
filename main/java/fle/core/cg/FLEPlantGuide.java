package fle.core.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import flapi.cg.GuiBookBase;
import flapi.cg.StandardPage;
import flapi.cg.StandardType;
import flapi.plant.CropCard;
import flapi.plant.PlantCard;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.block.plant.BlockPlant;
import fle.core.init.IB;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.resource.block.BlockFleCrop;
import fle.resource.item.ItemTreeLog;

public class FLEPlantGuide extends StandardType
{
	public static void init()
	{
		for(CropCard crop : FLE.fle.getCropRegister().getCrops())
		{
			try
			{
				List<ItemStack> list = crop.getHarvestDropsInfo(null).getDrops();
				addPlantInfomation(new PlantInfomation(IB.farmland, ((BlockFleCrop) IB.crop).getIcon(crop, 3), 
						new BaseStack(ItemFleSeed.a(crop.getCropName())), list.toArray(new ItemStack[list.size()])));
			}
			catch(Throwable e)
			{
				;
			}
		}
		for(PlantCard plant: FLE.fle.getPlantRegister().getPlants())
		{
			List<ItemStack> list = plant.getDropInfo().getDrops();
			addPlantInfomation(new PlantInfomation(IB.farmland, ((BlockPlant) IB.plant).getIcon(FLE.fle.getPlantRegister().getPlantID(plant), 3), 
					new BaseStack(Blocks.grass), list.toArray(new ItemStack[list.size()])));
		}
		addPlantInfomation(new PlantInfomation(Blocks.grass, new ItemStack(Blocks.sapling), 
				new BaseStack(Blocks.sapling, 0), ItemFleSub.a("leaves"), ItemFleSub.a("branch_oak"), 
				ItemFleSub.a("seed_oak"), ((ItemTreeLog) IB.treeLog).createStandardLog(Blocks.log, 0, 5)));
		addPlantInfomation(new PlantInfomation(Blocks.grass, new ItemStack(Blocks.sapling), 
				new BaseStack(Blocks.sapling, 1), ItemFleSub.a("leaves"), ItemFleSub.a("branch_spruce"), 
				ItemFleSub.a("seed_spruce"), ((ItemTreeLog) IB.treeLog).createStandardLog(Blocks.log, 1, 5)));
		addPlantInfomation(new PlantInfomation(Blocks.grass, new ItemStack(Blocks.sapling), 
				new BaseStack(Blocks.sapling, 2), ItemFleSub.a("leaves"), ItemFleSub.a("branch_birch"), 
				ItemFleSub.a("seed_birch"), ((ItemTreeLog) IB.treeLog).createStandardLog(Blocks.log, 2, 5)));
		addPlantInfomation(new PlantInfomation(Blocks.grass, new ItemStack(Blocks.sapling), 
				new BaseStack(Blocks.sapling, 3), ItemFleSub.a("leaves"), ItemFleSub.a("branch_jungle"), 
				ItemFleSub.a("seed_jungle"), ((ItemTreeLog) IB.treeLog).createStandardLog(Blocks.log, 3, 20)));
		addPlantInfomation(new PlantInfomation(Blocks.grass, new ItemStack(Blocks.sapling), 
				new BaseStack(Blocks.sapling, 4), ItemFleSub.a("leaves"), ItemFleSub.a("branch_acacia"), 
				ItemFleSub.a("seed_acacia"), ((ItemTreeLog) IB.treeLog).createStandardLog(Blocks.log2, 0, 5)));
		addPlantInfomation(new PlantInfomation(Blocks.grass, new ItemStack(Blocks.sapling), 
				new BaseStack(Blocks.sapling, 5), ItemFleSub.a("leaves"), ItemFleSub.a("branch_darkoak"), 
				ItemFleSub.a("seed_darkoak"), ((ItemTreeLog) IB.treeLog).createStandardLog(Blocks.log2, 1, 10)));
	}
	
	private static List<PlantInfomation> list = new ArrayList<FLEPlantGuide.PlantInfomation>();
	
	public static void addPlantInfomation(PlantInfomation info)
	{
		list.add(info);
	}
	
	public FLEPlantGuide()
	{
		
	}

	@Override
	public String getGuideName()
	{
		return "plant";
	}

	@Override
	public String getTypeName()
	{
		return "Plant";
	}

	@Override
	public List<IGuidePage> getAllPage()
	{
		return new ArrayList<IGuidePage>(list);
	}
	
	public static class PlantInfomation extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/plant.png");
		IIcon plantGround;
		IIcon cropIcon;
		ItemAbstractStack seed;
		ItemStack[] output;

		public PlantInfomation(Block block, ItemStack crop, ItemAbstractStack seed, ItemStack...stacks)
		{
			this(block, crop.getIconIndex(), seed, stacks);
		}
		public PlantInfomation(ItemStack ground, ItemStack crop, ItemAbstractStack seed, ItemStack...stacks)
		{
			this(ground.getIconIndex(), crop.getIconIndex(), seed, stacks);
		}
		public PlantInfomation(Block block, IIcon crop, ItemAbstractStack seed, ItemStack...stacks)
		{
			this(block.getIcon(2, 0), crop, seed, stacks);
		}
		public PlantInfomation(IIcon ground, IIcon crop, ItemAbstractStack seed, ItemStack...stacks)
		{
			plantGround = ground;
			cropIcon = crop;
			this.seed = seed;
			output = stacks;
		}
		
		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return new ItemAbstractStack[]{seed};
		}

		@Override
		protected ItemStack[] getOutputStacks()
		{
			return output;
		}

		@Override
		protected ItemStack[][] getInputStacksForDisplay()
		{
			return new ItemStack[][]{seed.toList()};
		}

		@Override
		public ResourceLocation getLocation()
		{
			return locate;
		}

		@Override
		public Rectangle getRectangle(Type type, int index)
		{
			return type == Type.ITEM ? 
					index == 0 ? new Rectangle(41, 41, 16, 16) : new Rectangle(94 + 18 * ((index - 1) % 2), 32 + 18 * ((index - 1) / 2), 16, 16) : null;
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			gui.bindTexture(TextureMap.locationBlocksTexture);
			gui.drawTexturedModelRectFromIcon(xOffset + 60, yOffset + 33, cropIcon, 16, 16);
			gui.drawTexturedModelRectFromIcon(xOffset + 60, yOffset + 49, plantGround, 16, 16);
			gui.bindTexture(getLocation());
		}
	}
}