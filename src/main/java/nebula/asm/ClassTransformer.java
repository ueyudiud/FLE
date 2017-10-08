/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.ImmutableList;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * The class transformer.
 * @author ueyudiud
 *
 */
public class ClassTransformer implements IClassTransformer
{
	static final Map<String, OpInformation> informations = new HashMap<>();
	
	OpInformation create(String name)
	{
		return new OpInformation(name);
	}
	
	private int numInsertions = 0;
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if (transformedName.startsWith("com.google.gson."))
			return basicClass;//Gson uses do not modify.
		NebulaASMLogHelper.outputInit();
		if (transformedName.startsWith("net.minecraft."))
		{
			NebulaASMLogHelper.keyOutputStream.println(name + "=" + transformedName);
		}
		OpInformation information;
		if ((information = informations.remove(transformedName)) != null)
			return modifyClass(transformedName, information, basicClass);
		return basicClass;
	}
	
	public byte[] modifyClass(String clazzName, OpInformation information, byte[] basicClass)
	{
		try
		{
			String clazzName1 = clazzName.substring(clazzName.lastIndexOf('.') + 1);
			NebulaASMLogHelper.LOG.info("Start to modify class {}({}).", clazzName1, clazzName);
			NebulaASMLogHelper.LOG.debug("Checking targets are {}", information.modifies);
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(basicClass);
			reader.accept(node, 0);
			List<String> methods = new ArrayList<>();
			Iterator<MethodNode> nodes = node.methods.iterator();
			while (nodes.hasNext())
			{
				MethodNode node2 = nodes.next();
				String name = node2.name + "|" + node2.desc;
				methods.add(name);
				if (information.modifies.containsKey(name))
				{
					List<OpLabel> list = information.modifies.remove(name);
					if (list == ImmutableList.<OpLabel>of())
					{
						nodes.remove();
						NebulaASMLogHelper.LOG.debug("Removed method {}.", name);
						continue;
					}
					else
					{
						NebulaASMLogHelper.LOG.debug("Injecting method  {}.", name);
						NebulaASMLogHelper.logOutput(clazzName1 + "." + node2.name, "source", name, node2.instructions);
						boolean success = modifyMethodNode(node2.instructions, list);
						NebulaASMLogHelper.logOutput(clazzName1 + "." + node2.name, "modified", name, node2.instructions);
						if (!success)
						{
							NebulaASMLogHelper.LOG.warn("Injected method {} failed.", name);
						}
						else
						{
							NebulaASMLogHelper.LOG.debug("Injected method {} success.", name);
						}
					}
					if (information.modifies.isEmpty()) break;
				}
			}
			NebulaASMLogHelper.logMethods(clazzName, methods);
			ClassWriter writer = new ClassWriter(1);
			node.accept(writer);
			NebulaASMLogHelper.LOG.info("End modify class {}.", clazzName);
			return writer.toByteArray();
		}
		catch(Exception exception)
		{
			NebulaASMLogHelper.LOG.error("Fail to modify class.", exception);
			return basicClass;
		}
	}
	
	private boolean modifyMethodNode(InsnList instructions, List<OpLabel> list)
	{
		list = new ArrayList<>(list);
		OpLabel info = null;
		if(!list.isEmpty())
		{
			info = list.get(0);
		}
		else return false;
		for (int idx = 0; (idx < instructions.size() && !list.isEmpty()); ++idx)
		{
			this.numInsertions = 0;
			while (info != null)
			{
				if (!info.matchNode(instructions.get(idx))) break;
				int off = info.performAnchorOperation(instructions, idx, this.numInsertions);
				if (info.off < 0)//If offset is negative, will return to last node to check (For it may modified before)
				{
					idx += off;
				}
				else
				{
					this.numInsertions += off;
				}
				list.remove(0);
				info = list.isEmpty() ? null : list.get(0);
			}
		}
		return list.isEmpty();
	}
}