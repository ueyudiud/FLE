/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.ditchs;

import static fle.api.ditch.DitchInformation.DITCH_INFORMATION_PROPERTY;

import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.material.Mat;
import fle.api.ditch.DitchBlockHandler;
import fle.api.ditch.DitchFactory;
import fle.api.ditch.DitchInformation;
import fle.api.tile.IDitchTile;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.fluid.FluidTankN;
import nebula.common.util.EnumChatFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class DefaultDitchFactory implements DitchFactory
{
	static
	{
		add(M.oak, 200, 12000, 20, 500);
		add(M.spruce, 200, 12000, 20, 500);
		add(M.birch, 200, 12000, 20, 500);
		add(M.ceiba, 200, 12000, 20, 500);
		add(M.acacia, 200, 12000, 20, 500);
		add(M.oak_black, 200, 12000, 20, 500);
		add(M.aspen, 200, 12000, 20, 500);
		add(M.morus, 200, 12000, 20, 500);
		add(M.willow, 200, 12000, 20, 500);
		
		add(M.stone, 400, 16000, 50, 1200);
		add(M.compact_stone, 500, 24000, 75, 1500);
		add(M.andesite, 500, 24000, 50, 1500);
		add(M.basalt, 500, 24000, 50, 1500);
		add(M.diorite, 500, 24000, 50, 1500);
		add(M.gabbro, 500, 24000, 50, 1500);
		add(M.granite, 500, 24000, 50, 1500);
		add(M.kimberlite, 500, 24000, 50, 1500);
		add(M.limestone, 500, 24000, 50, 1200);
		add(M.marble, 700, 32000, 100, 1200);
		add(M.netherrack, 300, 20000, 50, 2400);
		add(M.obsidian, 600, 24000, 50, 1500);
		add(M.peridotite, 500, 24000, 50, 1500);
		add(M.rhyolite, 500, 24000, 50, 1500);
		add(M.graniteP, 500, 24000, 50, 1500);
		add(M.whitestone, 500, 24000, 50, 1500);
	}
	
	static void add(Mat material, int capacity, int speed, int limit, int temperature)
	{
		DitchInformation information = new DitchInformation();
		information.tankCapacity = capacity;
		information.speedMultiple = speed;
		information.maxTransferLimit = limit;
		information.destroyTemperature = temperature;
		material.builder().addProperty(DITCH_INFORMATION_PROPERTY, information);
		DitchBlockHandler.addMaterial(material);
	}
	
	public DefaultDitchFactory()
	{
		DitchBlockHandler.rawFactory = this;
		LanguageManager.registerLocal("info.ditch.factory.default.material", "Material : " + EnumChatFormatting.YELLOW + "%s");
		LanguageManager.registerLocal("info.ditch.factory.default.capacity", "Capacity : " + EnumChatFormatting.AQUA + "%dL");
		LanguageManager.registerLocal("info.ditch.factory.default.transfer.limit", "Transfer Limit : " + EnumChatFormatting.BLUE + "%dL/t");
		LanguageManager.registerLocal("info.ditch.factory.default.speed", "Speed : " + EnumChatFormatting.GREEN + "%dm^2s");
		LanguageManager.registerLocal("info.ditch.factory.default.destory.temperature", "Temperature Limit : " + EnumChatFormatting.RED + "%dK");
		LanguageManager.registerTooltip("info.ditch.factory.default.speed.guide", "The ditch block can only transfer liquid.", "The ditch block speed provide basic flow speed multiplier, caculate real flow speed by m / v. The m is ditch block transfer Speed, and v is Viscosity of fluid.");
	}
	
	@Override
	public boolean access(Mat material)
	{
		return material.getProperty(DITCH_INFORMATION_PROPERTY) != null;
	}
	
	@Override
	public FluidTankN apply(IDitchTile tile)
	{
		return new FluidTankN(tile.getMaterial().getProperty(DITCH_INFORMATION_PROPERTY).tankCapacity).enableTemperature();
	}
	
	@Override
	public void onUpdate(IDitchTile tile)
	{
		FluidTankN tank = tile.getTank();
		DitchInformation information = tile.getMaterial().getProperty(DITCH_INFORMATION_PROPERTY);
		if (information.destroyTemperature < tank.getTemperature())
		{
			if (EnumBlock.fire.block.canPlaceBlockAt(tile.world(), tile.pos()))
			{
				tile.setBlockState(EnumBlock.fire.block.getDefaultState(), 3);
			}
		}
	}
	
	@Override
	public int getSpeedMultiple(IDitchTile tile)
	{
		return tile.getMaterial().getProperty(DITCH_INFORMATION_PROPERTY).speedMultiple;
	}
	
	@Override
	public int getMaxTransferLimit(IDitchTile tile)
	{
		return tile.getMaterial().getProperty(DITCH_INFORMATION_PROPERTY).maxTransferLimit;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getMaterialIcon(Mat material)
	{
		return material.contain(SubTags.WOOD) ? MaterialTextureLoader.getIcon(material, MC.plankBlock) : material.contain(SubTags.ROCK) ? MaterialTextureLoader.getIcon(material, MC.stone) : MaterialTextureLoader.getIcon(material, MC.brickBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addTooltip(Mat material, UnlocalizedList list)
	{
		DitchInformation information = material.getProperty(DITCH_INFORMATION_PROPERTY);
		list.add("info.ditch.factory.default.material", material.getLocalName());
		list.add("info.ditch.factory.default.capacity", information.tankCapacity);
		list.add("info.ditch.factory.default.transfer.limit", information.maxTransferLimit);
		list.add("info.ditch.factory.default.speed", information.speedMultiple);
		list.add("info.ditch.factory.default.destory.temperature", information.destroyTemperature);
		list.addToolTip("info.ditch.factory.default.speed.guide");
	}
}
