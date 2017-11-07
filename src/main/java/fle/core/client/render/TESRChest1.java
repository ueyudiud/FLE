/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.render;

import fle.core.FLE;
import fle.core.tile.chest.TEChest1;
import nebula.client.NebulaTextureHandler;
import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRChest1 extends TESRChest<TEChest1> implements IIconLoader
{
	TextureAtlasSprite[][] icons;
	
	public TESRChest1()
	{
		super(1.0F / 16.0F, 0.0F / 16.0F, 2.0F / 16.0F, 6.0F / 16.0F, 15.0F / 16.0F, 10.0F / 16.0F, 14.0F / 16.0F);
		NebulaTextureHandler.addIconLoader(this);
	}
	
	@Override
	public void registerIcon(IIconRegister register)
	{
		this.icons = new TextureAtlasSprite[TEChest1.ChestType.values().length][8];
		for (int i = 0; i < this.icons.length; ++i)
		{
			final String type = TEChest1.ChestType.values()[i].name;
			this.icons[i][0] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_bottom");
			this.icons[i][1] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_top");
			this.icons[i][2] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_front");
			this.icons[i][3] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_left");
			this.icons[i][4] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_right");
			this.icons[i][5] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_back");
			this.icons[i][6] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_midi");
			this.icons[i][7] = register.registerIcon(FLE.MODID, "blocks/iconset/chest/small_" + type + "_midii");
			TEChest1.ChestType.values()[i].icon = register.registerIcon(FLE.MODID, "blocks/iconset/" + type);
		}
	}
	
	@Override
	protected void renderChestBody(TEChest1 te)
	{
		int type = te.getChestType().ordinal();
		TextureAtlasSprite[] sprites = { this.icons[type][0], this.icons[type][6], this.icons[type][2], this.icons[type][5], this.icons[type][3], this.icons[type][4] };
		renderCube(this.minX, this.minY, this.minZ, this.maxX, this.midY, this.maxZ, sprites);
	}
	
	@Override
	protected void renderChestTop(TEChest1 te)
	{
		int type = te.getChestType().ordinal();
		TextureAtlasSprite[] sprites = { this.icons[type][7], this.icons[type][1], this.icons[type][2], this.icons[type][5], this.icons[type][3], this.icons[type][4] };
		renderCube(this.minX, this.midY, this.minZ, this.maxX, this.maxY, this.maxZ, sprites);
	}
}
