package flapi.gui;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import flapi.collection.Register;
import flapi.util.IDataChecker;

public class GuiCondition implements IDataChecker<IConditionContainer>
{
	public static Register<GuiCondition> register = new Register();
	
	public GuiCondition(String aName) 
	{
		name = aName;
		register.register(this, aName);
	}
	
	protected final String name;
	protected String[] textureName;
	protected IIcon[] icon;
	
	public final String getName() 
	{
		return name;
	}
	
	public GuiCondition setTextureName(String...textureName)
	{
		this.textureName = textureName;
		return this;
	}
	
	public String getTextureName()
	{
		return textureName == null ? "MISSING_ICON_NAME_CONDITION_" + name : textureName[0];
	}
	
	public void registerIcon(IIconRegister aRegister)
	{
		icon = new IIcon[textureName.length];
		for(int i = 0; i < icon.length; ++i)
		{
			icon[i] = aRegister.registerIcon(textureName[i]);
		}
	}
	
	public int getRenderPass()
	{
		return icon.length;
	}
	
	public IIcon getIcon(int pass)
	{
		return icon[pass];
	}
	
	@Override
	public boolean isTrue(IConditionContainer aContainer) 
	{
		return aContainer.contain(this);
	}
}