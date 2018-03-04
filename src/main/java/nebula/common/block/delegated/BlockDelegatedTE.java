/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block.delegated;

import nebula.base.register.IRegister;
import nebula.base.register.Register;
import nebula.common.block.property.PropertyRegister;
import nebula.common.tile.TEBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class BlockDelegatedTE<B extends BlockDelegatedTE<B, T>, T extends TEBase> extends BlockDelegated<B, T>
{
	protected final IRegister<ITileDelegate<B, T>> delegates;
	public final PropertyRegister<ITileDelegate<B, T>> property;
	
	public BlockDelegatedTE(String name, Material materialIn)
	{
		super(name, materialIn);
		this.delegates = createDelegates();
		this.property = PropertyRegister.create("type", this.delegates);
		preloadDelegates();
	}
	
	public BlockDelegatedTE(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
		this.delegates = createDelegates();
		this.property = PropertyRegister.create("type", this.delegates);
		preloadDelegates();
	}
	
	public BlockDelegatedTE(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
		this.delegates = createDelegates();
		this.property = PropertyRegister.create("type", this.delegates);
		preloadDelegates();
	}
	
	public BlockDelegatedTE(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		this.delegates = createDelegates();
		this.property = PropertyRegister.create("type", this.delegates);
		preloadDelegates();
	}
	
	private final IRegister<ITileDelegate<B, T>> createDelegates()
	{
		Register<ITileDelegate<B, T>> register = new Register<>();
		registerDelegates(register);
		register.trimToMinCapacity();
		return register;
	}
	
	private final void preloadDelegates()
	{
		for (ITileDelegate delegate : this.delegates)
		{
			delegate.load(this);
		}
	}
	
	protected void registerDelegates(IRegister<ITileDelegate<B, T>> register)
	{
		
	}
	
	@Override
	protected ITileDelegate<B, T> delegateOf(IBlockState state)
	{
		return this.property.getValue(state);
	}
	
	@Override
	protected ITileDelegate<B, T> delegateOf(ItemStack stack)
	{
		return this.delegates.get(stack.getItemDamage(), (ITileDelegate) TileDelegateMissing.INSTANCE);
	}
	
	@Override
	protected Iterable<ITileDelegate<? super B, ? super T>> delegates()
	{
		return (Iterable) this.delegates.targets();
	}
}
