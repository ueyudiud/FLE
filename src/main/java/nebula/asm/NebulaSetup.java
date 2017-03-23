/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFGE;
import static org.objectweb.asm.Opcodes.IFGT;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ACMPEQ;
import static org.objectweb.asm.Opcodes.IF_ACMPNE;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGE;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.IF_ICMPLE;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.IINC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INSTANCEOF;
import static org.objectweb.asm.Opcodes.INVOKEDYNAMIC;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.JSR;
import static org.objectweb.asm.Opcodes.LDC;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LOOKUPSWITCH;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.MULTIANEWARRAY;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RET;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.TABLESWITCH;
import static org.objectweb.asm.tree.AbstractInsnNode.FIELD_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.INT_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.METHOD_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.MULTIANEWARRAY_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.TYPE_INSN;
import static org.objectweb.asm.tree.AbstractInsnNode.VAR_INSN;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;

import nebula.common.util.Jsons;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

/**
 * @author ueyudiud
 */
public class NebulaSetup implements IFMLCallHook
{
	/**
	 * The ASM file version, uses to determine if it need replaced ASM files.
	 */
	private static final int VERSION = 5;
	
	final JsonDeserializer<OpInformation> DESERIALIZER1 = (json, typeOfT, context) -> {
		if (!json.isJsonObject()) throw new JsonParseException("The json should be an object.");
		JsonObject object = json.getAsJsonObject();
		OpInformation information = new OpInformation(object.get("name").getAsString());
		try
		{
			if (!object.has("modification"))
			{
				ClassTransformer.LOG.warn("No modification of " + information.mcpname + " detected.");
			}
			else
			{
				JsonArray array = object.getAsJsonArray("modification");
				for (JsonElement json1 : array)
				{
					JsonObject object1 = json1.getAsJsonObject();
					String name = object1.get("name").getAsString();
					List<OpLabel> list = new ArrayList<>();
					for (JsonElement json2 : object1.getAsJsonArray("labels"))
					{
						list.add(context.deserialize(json2, OpLabel.class));
					}
					if (!list.isEmpty())
					{
						information.modifies.put(name, list);
					}
				}
			}
		}
		catch (RuntimeException exception)
		{
			throw new JsonParseException("Can not parse asm config of " + information.mcpname, exception);
		}
		return information;
	};
	final JsonSerializer<OpInformation> SERIALIZER1 = (src, typeOfSrc, context) -> {
		JsonObject object = new JsonObject();
		object.addProperty("name", src.mcpname);
		JsonArray array = new JsonArray();
		for (Entry<String, List<OpLabel>> entry : src.modifies.entrySet())
		{
			JsonObject object1 = new JsonObject();
			object1.addProperty("name", entry.getKey());
			JsonArray array1 = new JsonArray();
			for (OpLabel label : entry.getValue())
			{
				array1.add(context.serialize(label, OpLabel.class));
			}
			object1.add("labels", array1);
			array.add(object1);
		}
		object.add("modification", array);
		return object;
	};
	final JsonDeserializer<OpLabel> DESERIALIZER2 = (json, typeOfT, context) -> {
		if (!json.isJsonObject()) throw new JsonParseException("The json should be an object.");
		JsonObject object = json.getAsJsonObject();
		OpType type = OpType.parseValue(object.get("type").getAsString());
		int off = Jsons.getOrDefault(object, "off", 0);
		int len = Jsons.getOrDefault(object, "len", 1);
		List<AbstractInsnNode> nodes;
		if (object.has("nodes"))
		{
			nodes = new ArrayList<>();
			for (JsonElement element : object.getAsJsonArray("nodes"))
			{
				AbstractInsnNode node = context.deserialize(element, AbstractInsnNode.class);
				if (node == null)
					throw new JsonParseException("No node exist!");
				nodes.add(node);
			}
		}
		else nodes = null;
		OpLabel label;
		if (object.has("name"))
		{
			String owner = object.get("owner").getAsString();
			String name = object.get("name").getAsString();
			String desc = object.get("desc").getAsString();
			int count = Jsons.getOrDefault(object, "count", 1);//Each prefer first for replacement.
			label = new OpLabel.OpLabelMethodAsTag(count, owner, name, desc, off, len, type, nodes);
		}
		else
		{
			int line = Jsons.getOrDefault(object, "line", 0);
			label = new OpLabel.OpLabelLineNumber(line, off, len, type, nodes);
		}
		return label;
	};
	final JsonSerializer<OpLabel> SERIALIZER2 = (src, typeOfSrc, context) -> {
		JsonObject object = new JsonObject();
		object.addProperty("type", src.type.name);
		if (src instanceof OpLabel.OpLabelLineNumber)
		{
			object.addProperty("line", ((OpLabel.OpLabelLineNumber) src).line);
		}
		else
		{
			object.addProperty("count", ((OpLabel.OpLabelMethodAsTag) src).count);
			object.addProperty("owner", ((OpLabel.OpLabelMethodAsTag) src).owner);
			object.addProperty("name", ((OpLabel.OpLabelMethodAsTag) src).name);
			object.addProperty("desc", ((OpLabel.OpLabelMethodAsTag) src).desc);
		}
		object.addProperty("off", src.off);
		object.addProperty("len", src.len);
		if (src.nodes != null)
		{
			JsonArray array = new JsonArray();
			for (AbstractInsnNode node : src.nodes)
			{
				array.add(context.serialize(node, AbstractInsnNode.class));
			}
			object.add("nodes", array);
		}
		return object;
	};
	final JsonDeserializer<AbstractInsnNode> DESERIALIZER3 = (json, typeOfT, context) -> {
		if (!json.isJsonObject())
		{
			return new InsnNode(json.getAsInt());
		}
		JsonObject object = json.getAsJsonObject();
		int val = object.get("opcode").getAsInt();
		switch (val)
		{
		case BIPUSH :
		case SIPUSH :
		case NEWARRAY :
			int operand = object.get("operand").getAsInt();
			return new IntInsnNode(val, operand);
		case LDC :
			throw new RuntimeException("This node can not used, sorry.");
		case ILOAD :
		case LLOAD :
		case FLOAD :
		case DLOAD :
		case ALOAD :
		case ISTORE :
		case LSTORE :
		case FSTORE :
		case ASTORE :
		case RET :
			int var = object.get("var").getAsInt();
			return new VarInsnNode(val, var);
		case IINC :
			int incr = object.get("incr").getAsInt();
			return new IincInsnNode(val, incr);
		case IFEQ :
		case IFNE :
		case IFLT :
		case IFGE :
		case IFGT :
		case IFLE :
		case IF_ICMPEQ :
		case IF_ICMPNE :
		case IF_ICMPLT :
		case IF_ICMPGE :
		case IF_ICMPGT :
		case IF_ICMPLE :
		case IF_ACMPEQ :
		case IF_ACMPNE :
		case GOTO :
		case JSR :
		case IFNULL :
		case IFNONNULL :
			throw new RuntimeException("This node can not used, sorry.");
		case TABLESWITCH :
			throw new RuntimeException("This node can not used, sorry.");
		case LOOKUPSWITCH :
			throw new RuntimeException("This node can not used, sorry.");
		case GETSTATIC :
		case PUTSTATIC :
		case GETFIELD :
		case PUTFIELD :
			String owner = object.get("owner").getAsString();
			String name = object.get("name").getAsString();
			String desc = object.get("desc").getAsString();
			return new FieldInsnNode(val, owner, name, desc);
		case INVOKEVIRTUAL :
		case INVOKESPECIAL :
		case INVOKESTATIC :
		case INVOKEINTERFACE :
			owner = object.get("owner").getAsString();
			name = object.get("name").getAsString();
			desc = object.get("desc").getAsString();
			boolean itf = object.get("itf").getAsBoolean();
			return new MethodInsnNode(val, owner, name, desc, itf);
		case INVOKEDYNAMIC :
			name = object.get("name").getAsString();
			desc = object.get("desc").getAsString();
			throw new RuntimeException("This node can not used, sorry.");
		case NEW :
		case ANEWARRAY :
		case CHECKCAST :
		case INSTANCEOF :
			desc = object.get("desc").getAsString();
			return new TypeInsnNode(val, desc);
		case MULTIANEWARRAY :
			desc = object.get("desc").getAsString();
			int dims = object.get("dims").getAsInt();
			return new MultiANewArrayInsnNode(desc, dims);
		default :
			return new InsnNode(val);
		}
	};
	final JsonSerializer<AbstractInsnNode> SERIALIZER3 = (src, typeOfSrc, context) -> {
		JsonObject object = new JsonObject();
		object.addProperty("opcode", src.getOpcode());
		switch (src.getType())
		{
		case INT_INSN :
			object.addProperty("operand", ((IntInsnNode) src).operand);
			break;
		case VAR_INSN :
			object.addProperty("var", ((VarInsnNode) src).var);
			break;
		case FIELD_INSN :
			object.addProperty("owner", ((FieldInsnNode) src).owner);
			object.addProperty("name", ((FieldInsnNode) src).name);
			object.addProperty("desc", ((FieldInsnNode) src).desc);
			break;
		case METHOD_INSN :
			object.addProperty("owner", ((MethodInsnNode) src).owner);
			object.addProperty("name", ((MethodInsnNode) src).name);
			object.addProperty("desc", ((MethodInsnNode) src).desc);
			object.addProperty("itf", ((MethodInsnNode) src).itf);
			break;
		case TYPE_INSN :
			object.addProperty("desc", ((TypeInsnNode) src).desc);
			break;
		case MULTIANEWARRAY_INSN :
			object.addProperty("dims", ((MultiANewArrayInsnNode) src).dims);
			object.addProperty("desc", ((MultiANewArrayInsnNode) src).desc);
			break;
		default :
			throw new RuntimeException("Can not extract node.");
		}
		return object;
	};
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(OpInformation.class, this.DESERIALIZER1)
			.registerTypeAdapter(OpLabel.class, this.DESERIALIZER2)
			.registerTypeAdapter(AbstractInsnNode.class, this.DESERIALIZER3)
			.registerTypeAdapter(OpInformation.class, this.SERIALIZER1)
			.registerTypeAdapter(OpLabel.class, this.SERIALIZER2)
			.registerTypeAdapter(AbstractInsnNode.class, this.SERIALIZER3)
			.create();
	
	private File mcPath;
	private boolean runtimeDeobf;
	
	public void searchAndPut(File file)
	{
		try
		{
			ClassTransformer.LOG.info("Searching modifications at {}", file.getCanonicalPath());
		}
		catch (IOException exception)
		{
			ClassTransformer.LOG.warn("Unknown fil path.");
		}
		try
		{
			for (File file2 : file.listFiles(name -> name.getName().endsWith(".json")))
			{
				try
				{
					OpInformation information = this.gson.fromJson(new BufferedReader(new FileReader(file2)), OpInformation.class);
					information.put();
					ClassTransformer.LOG.info("Loaded {} modifications.", information.mcpname);
				}
				catch (RuntimeException exception)
				{
					ClassTransformer.LOG.error("Fail to parse OperationInformation at " + file2.getPath(), exception);
				}
				catch (IOException exception)
				{
					ClassTransformer.LOG.error("Fail to load OperationInformation", exception);
				}
			}
		}
		catch (RuntimeException exception)
		{
			ClassTransformer.LOG.error("Failed to searching ASM files.", exception);
		}
	}
	
	private boolean markVersion(File destination) throws IOException
	{
		File file = new File(destination, "version.txt");
		if (!file.exists())
		{
			OutputStream stream = null;
			try
			{
				file.createNewFile();
				stream = new BufferedOutputStream(new FileOutputStream(file));
				stream.write(VERSION);
				return true;
			}
			catch (Exception exception)
			{
				ClassTransformer.LOG.error("Fail to add version file.", exception);
			}
			finally
			{
				if (stream != null)
				{
					stream.close();
				}
			}
		}
		else
		{
			try
			{
				InputStream stream1 = new BufferedInputStream(new FileInputStream(file));
				int version = stream1.read();
				if (version != VERSION)
				{
					stream1.close();
					OutputStream stream2 = new BufferedOutputStream(new FileOutputStream(file));
					stream2.write(VERSION);
					stream2.close();
					return true;
				}
				else
				{
					stream1.close();
					return false;
				}
			}
			catch (Exception exception)
			{
				ClassTransformer.LOG.error("Fail to check version file.", exception);
			}
		}
		return true;
	}
	
	@Override
	public Void call() throws Exception
	{
		File destination = new File(new File(this.mcPath, "asm"), this.runtimeDeobf ? "obf" : "mcp");
		File file;
		
		if (!destination.exists())
		{
			if (!destination.mkdirs())
			{
				throw new RuntimeException("Can't create asm file.");
			}
		}
		else if (!destination.isDirectory())
		{
			throw new RuntimeException("Can't read custom asm file.");
		}
		if (markVersion(destination))
		{
			try //Insert Operation Files source to location.
			{
				URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
				file = new File(url.toURI());
				String n = this.runtimeDeobf ? "obf" : "mcp";
				if (file.getName().endsWith(".jar"))
				{
					JarFile jarFile = new JarFile(file);
					String targetLocation = "asm/" + n + "/";
					if (jarFile.getEntry(targetLocation) == null)
					{
						ClassTransformer.LOG.warn("Asm file does not exist or invalid!");
					}
					else
					{
						Enumeration<JarEntry> enumeration = jarFile.entries();
						while (enumeration.hasMoreElements())
						{
							JarEntry entry = enumeration.nextElement();
							if (!entry.isDirectory())
							{
								if (entry.getName().startsWith(targetLocation))
								{
									if (entry != null)
									{
										ClassTransformer.LOG.info("Copy asm data from :" + entry.getName());
										FileUtils.copyInputStreamToFile(jarFile.getInputStream(entry), new File(destination, entry.getName().substring(targetLocation.length())));
									}
								}
							}
						}
					}
					jarFile.close();
				}
				else
				{
					//for (int i = getClass().getPackage().getName().replaceAll("[^\\.]", "").length() + 1; i >= 0; i--)
					//{
					//	file = file.getParentFile();
					//}
					File[] files = new File(file, "asm").listFiles((dir, name) -> name.equals(n));
					if (files == null || files.length != 1)
					{
						ClassTransformer.LOG.warn("Asm file does not exist or invalid!");
					}
					else
					{
						ClassTransformer.LOG.info("Copy asm data from :" + files[0].getPath());
						FileUtils.copyDirectory(files[0], destination);
					}
				}
			}
			catch (Exception exception)
			{
				throw new RuntimeException("Fail to extract source files.", exception);
			}
		}
		searchAndPut(destination);
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		this.mcPath = (File) data.get("mcLocation");
		this.runtimeDeobf = ((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue();
	}
}