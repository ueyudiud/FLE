package fle.resource.lib.infomation;

import farcore.collection.Register;
import farcore.substance.Atom;
import farcore.substance.Substance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class IngotInfo
{
	public static final Register<IngotInfo> register = new Register();

	public final String name;
	
	public IngotInfo(Substance substance)
	{
		name = substance.getName();
		this.substance = substance;
	}
	public IngotInfo(Atom atom)
	{
		name = atom.name;
		this.atom = atom;
	}
	
	protected Substance substance;
	protected Atom atom;
	
	public Substance getSubstance()
	{
		return substance;
	}
	
	public Atom getAtom()
	{
		return atom;
	}
	
	public void onEntityUpdate(EntityItem item)
	{
		;
	}
	
	public void onItemUpdate(ItemStack stack, World world, int x, int y, int z)
	{
		;
	}
}