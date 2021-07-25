package com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.github.mjaroslav.mjutils.mod.lib.ModInfo.LOGGER_HOOKS;

public class HookClassTransformer {
    protected HashMap<String, List<AsmHook>> hooksMap = new HashMap<>();
    private final HookContainerParser containerParser = new HookContainerParser(this);

    public void registerHook(AsmHook hook) {
        if (hooksMap.containsKey(hook.getTargetClassName())) {
            hooksMap.get(hook.getTargetClassName()).add(hook);
        } else {
            List<AsmHook> list = new ArrayList<>(2);
            list.add(hook);
            hooksMap.put(hook.getTargetClassName(), list);
        }
    }

    public void registerHookContainer(String className) {
        containerParser.parseHooks(className);
    }

    public void registerHookContainer(InputStream classData) {
        containerParser.parseHooks(classData);
    }

    public byte[] transform(String className, byte[] bytecode) {
        List<AsmHook> hooks = hooksMap.get(className);

        if (hooks != null) {
            Collections.sort(hooks);
            try {
                LOGGER_HOOKS.info("Injecting hooks into class " + className);
                int numHooks = hooks.size();
                int majorVersion = ((bytecode[6] & 0xFF) << 8) | (bytecode[7] & 0xFF);
                boolean java7 = majorVersion > 50;

                ClassReader cr = new ClassReader(bytecode);
                ClassWriter cw = new ClassWriter(java7 ? ClassWriter.COMPUTE_FRAMES : 0);
                HookInjectorClassVisitor hooksWriter = createInjectorClassVisitor(cw, hooks);
                cr.accept(hooksWriter, java7 ? ClassReader.SKIP_FRAMES : ClassReader.EXPAND_FRAMES);

                int numInjectedHooks = numHooks - hooksWriter.hooks.size();
                LOGGER_HOOKS.info("Successfully injected " + numInjectedHooks + " hook" + (numInjectedHooks == 1 ? "" : "s"));
                for (AsmHook notInjected : hooksWriter.hooks)
                    LOGGER_HOOKS.warn("Can not found target method of hook " + notInjected);

                return cw.toByteArray();
            } catch (Exception e) {
                LOGGER_HOOKS.error("A problem has occurred during transformation of class " + className + ".");
                LOGGER_HOOKS.error("Attached hooks:");
                for (AsmHook hook : hooks)
                    LOGGER_HOOKS.error(hook.toString());
                LOGGER_HOOKS.error("Stack trace:", e);
            }
        }
        return bytecode;
    }

    protected HookInjectorClassVisitor createInjectorClassVisitor(ClassWriter cw, List<AsmHook> hooks) {
        return new HookInjectorClassVisitor(cw, hooks);
    }

}
