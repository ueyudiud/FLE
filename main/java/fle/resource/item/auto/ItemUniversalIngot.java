package fle.resource.item.auto;

import java.util.ArrayList;
import java.util.List;

import farcore.render.item.ItemTextureHandler;
import farcore.substance.Atom;
import farcore.util.Util;
import flapi.item.ItemFleMetaBase;
import flapi.item.ItemFleMetaStandard;
import flapi.item.behavior.ItemBehaviorStandard;
import flapi.item.interfaces.IItemBehavior;
import fle.resource.item.behavior.ItemBehaviorIngot;
import fle.resource.lib.infomation.IngotAlkaliInfo;
import fle.resource.lib.infomation.IngotInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUniversalIngot extends ItemFleMetaStandard
{	
	public ItemUniversalIngot(String unlocalized)
	{
		super(unlocalized);
		init();
	}
	
	private ItemUniversalIngot init()
	{
		List<Atom> cache1 = new ArrayList();
		IngotInfo.register.register(Atom.H .ordinal(), new IngotAlkaliInfo(Atom.H ,   8), Atom.H.name.toLowerCase());
		IngotInfo.register.register(Atom.Li.ordinal(), new IngotAlkaliInfo(Atom.Li,  15), Atom.Li.name.toLowerCase());
		IngotInfo.register.register(Atom.Na.ordinal(), new IngotAlkaliInfo(Atom.Na,  60), Atom.Na.name.toLowerCase());
		IngotInfo.register.register(Atom.K .ordinal(), new IngotAlkaliInfo(Atom.K , 150), Atom.K .name.toLowerCase());
		IngotInfo.register.register(Atom.Rb.ordinal(), new IngotAlkaliInfo(Atom.Rb, 450), Atom.Rb.name.toLowerCase());
		IngotInfo.register.register(Atom.Cs.ordinal(), new IngotAlkaliInfo(Atom.Cs, 750), Atom.Cs.name.toLowerCase());
		cache1.add(Atom.H);
		cache1.add(Atom.Li);
		cache1.add(Atom.Na);
		cache1.add(Atom.K);
		cache1.add(Atom.Rb);
		cache1.add(Atom.Cs);
		for(Atom atom : Atom.values())
		{
			if(!cache1.contains(atom))
			{
				IngotInfo.register.register(atom.ordinal(), new IngotInfo(atom), atom.name.toLowerCase());
			}
		}
		for(IngotInfo info : IngotInfo.register.asCollection())
		{
			addSubItem2(IngotInfo.register.serial(info), info.name, 
					"resource/ingot/" + info.name.toLowerCase(), new ItemBehaviorIngot(info));
		}
		return this;
	}
	
	@Override
	public String getMetaUnlocalizedName(int metadata)
	{
		return IngotInfo.register.name(metadata);
	}
		
	public ItemUniversalIngot addSubItem2(int meta, String name, String iconName, IItemBehavior<ItemFleMetaBase> behavior)
	{
		addSubItem1(meta, name, new ItemTextureHandler(new String[]{iconName}), behavior);
		OreDictionary.registerOre("ingot" + Util.oreDictFormat(name, " "), new ItemStack(this, 1, meta));
		return this;
	}
}