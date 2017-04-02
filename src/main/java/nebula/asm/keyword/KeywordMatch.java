/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm.keyword;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

/**
 * @author ueyudiud
 */
public final class KeywordMatch implements Opcodes
{
	private static List<FunctionalBuildMarker> createMatchMarkers(MethodNode node, AbstractInsnNode source, int off)
	{
		List<FunctionalBuildMarker> types = new ArrayList<>();
		AbstractInsnNode node1 = source;
		do
		{
			if (node1 == null)
				throw new IllegalArgumentException(
						"Missing array creating, "
								+ "the function array type SHOULD"
								+ "be initalize in method.");
			node1 = node1.getPrevious();
		}
		while (!(node1 instanceof MultiANewArrayInsnNode));
		do
		{
			if (node1 instanceof InvokeDynamicInsnNode)
			{
				FunctionalBuildMarker marker = new FunctionalBuildMarker();
				InvokeDynamicInsnNode node2 = (InvokeDynamicInsnNode) node1;
				marker.node = node2;
				String str = node2.desc.substring(node2.desc.indexOf('('), node2.desc.indexOf(')'));
				int l = str.length() == 0 ? 0 :
					str.indexOf(';') == -1 ? 1 :
						str.split(";").length;
				
				AbstractInsnNode node3 = node2;
				marker.argument = new AbstractInsnNode[l];
				for (int i = l - 1; i >= 0; --i)
				{
					marker.argument[l] = (node3 = node3.getPrevious());
				}
			}
		}
		while (node1 != source);
		return types;
	}
	
	static AbstractInsnNode build_match(MethodNode node, int off, MethodVisitor visitor)
	{
		AbstractInsnNode source = node.instructions.get(off);
		List<FunctionalBuildMarker> types = createMatchMarkers(node, source, off);
		visitor.visitCode();
		Label start = new Label();
		Label l1 = start, l2;
		Iterator<FunctionalBuildMarker> itr = types.iterator();
		
		while (itr.hasNext())
		{
			FunctionalBuildMarker marker = itr.next();
			visitor.visitLabel(l1);
			visitor.visitVarInsn(ALOAD, 0);
			visitor.visitTypeInsn(INSTANCEOF, marker.type);
			l1 = new Label();
			visitor.visitJumpInsn(IFEQ, l1);
			visitor.visitVarInsn(ALOAD, 0);
			visitor.visitTypeInsn(CHECKCAST, marker.type);
			marker.visitCheckCast(visitor);
			//invoke
			visitor.visitInsn(ARETURN);
		}
		{
			visitor.visitLabel(l1);
			visitor.visitTypeInsn(NEW, "nebula/asm/keyword/TypeMissmatchException");
			visitor.visitInsn(DUP);
			visitor.visitVarInsn(ALOAD, 0);
			visitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
			visitor.visitMethodInsn(INVOKESPECIAL, "nebula/asm/keyword/TypeMissmatchException", "<init>", "(Ljava/lang/Class;)V", false);
			visitor.visitInsn(ATHROW);
		}
		{
			l2 = new Label();
			visitor.visitLabel(l2);
			visitor.visitLocalVariable("arg", "Ljava/lang/Object;", null, start, l2, 0);
			visitor.visitMaxs(1, 1);
		}
		visitor.visitEnd();
		return source;
	}
	
	public static void injectMatchBlock(ClassNode cn, MethodNode mn)
	{
		int id = 0;
		for (int i = 0; i < mn.instructions.size(); ++i)
		{
			AbstractInsnNode in = mn.instructions.get(i);
			if (in instanceof MethodInsnNode)
			{
				MethodInsnNode node = (MethodInsnNode) in;
				if (node.owner.equals("nebula/asm/keyword/Keyword") &&
						node.name.equals("match") &&
						node.desc.equals("(Ljava/lang/Object;[Ljava/util/function/Function;)Ljava/lang/Object;"))
				{
					build_match(mn, i, cn.visitMethod(ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC, "match$" + id++,
							"(Ljava/lang/Object;)Ljava/lang/Object;", null, null));
				}
			}
		}
	}
}