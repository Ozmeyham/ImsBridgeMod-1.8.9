package com.github.ozmeyham.imsbridge.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import net.minecraft.launchwrapper.IClassTransformer;

import static org.objectweb.asm.Opcodes.*;

public class IMSBridgeClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("net.minecraft.client.entity.EntityPlayerSP".equals(transformedName)) {
            return transformEntityPlayerSP(basicClass);
        }
        return basicClass;
    }

    private byte[] transformEntityPlayerSP(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if ("sendChatMessage".equals(method.name) ||
                    "func_71165_d".equals(method.name)) { // Obfuscated name for sendChatMessage

                InsnList toInsert = new InsnList();
                toInsert.add(new VarInsnNode(ALOAD, 1)); // Load the chat message (String)
                toInsert.add(new MethodInsnNode(INVOKESTATIC,
                        "ozmeyham/imsbridge/IMSBridge",
                        "handleOutgoingChat",
                        "(Ljava/lang/String;)Z",
                        false));
                LabelNode continueLabel = new LabelNode();
                toInsert.add(new JumpInsnNode(IFEQ, continueLabel));
                toInsert.add(new InsnNode(RETURN));
                toInsert.add(continueLabel);

                method.instructions.insert(toInsert);
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}