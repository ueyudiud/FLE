package fle.api.gui;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import fle.api.util.IDataChecker;
import fle.api.util.Register;

public class GuiCondition implements IDataChecker<IConditionContainer>
{
	private static Register<GuiCondition> register = new Register();
	
	public GuiCondition(String aName) 
	{
		name = aName;
		register.register(this, aName);
	}
	
	protected final String name;
	protected String textureName;
	protected IIcon icon;
	
	public final String getName() 
	{
		return name;
	}
	
	public String getTextureName()
	{
		return textureName;
	}
	
	public void registerIcon(IIconRegister aRegister)
	{
		icon = aRegister.registerIcon(getTextureName());
	}
	
	public IIcon getIcon()
	{
		return icon;
	}
	
	@Override
	public boolean isTrue(IConditionContainer aContainer) 
	{
		return aContainer.contain(this);
	}
}