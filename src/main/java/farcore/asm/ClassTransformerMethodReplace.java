package farcore.asm;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

@Deprecated
public class ClassTransformerMethodReplace implements IClassTransformer
{
	private Map<String, Modification> modifications = new HashMap();

	public ClassTransformerMethodReplace()
	{
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		Modification modification = modifications.get(transformedName);
		if(modification != null)
		{
			byte[] modifiedClass = visiteByteCode(basicClass, modification);
			if(modifiedClass != null)
				return modifiedClass;
		}
		return basicClass;
	}
	
	public byte[] visiteByteCode(byte[] code0, Modification modification)
	{
		try
		{
			ClassReader reader1 = new ClassReader(Launch.classLoader.getClassBytes(modification.targetClassName));
			ClassReader reader2 = new ClassReader(code0);
			ClassNode node1 = new ClassNode();
			ClassNode node2 = new ClassNode();
			reader1.accept(node1, 0);
			reader2.accept(node2, 0);
			for(MethodNode mn : node2.methods)
			{
				String value = mn.name + "|" + mn.desc;
				if(modification.map.containsKey(value))
				{
					for(MethodNode mn1 : node1.methods)
					{
						if(value.equals(mn1.name + ":" + mn1.desc))
						{
							mn.instructions.clear();
							mn.instructions.add(mn1.instructions);
							break;
						}
					}
				}
			}
			ClassWriter writer = new ClassWriter(0);
			node2.accept(writer);
			return writer.toByteArray();
		}
		catch (Exception exception)
		{
			ClassTransformerModifyMethod.LOG.error(exception);
			return null;
		}
	}

	private class Modification
	{
		String sourceClassName;
		String targetClassName;
		
		Modification(String source, String target)
		{
			modifications.put(sourceClassName = source, this);
			modifications.put(targetClassName = target, this);
		}

		Map<String, String> map = new HashMap();
		
		Modification put(String mcp, String obf)
		{
			map.put(mcp, mcp);
			map.put(obf, mcp);
			return this;
		}
	}
}