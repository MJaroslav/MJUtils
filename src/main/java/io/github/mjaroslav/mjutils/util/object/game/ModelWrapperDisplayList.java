package io.github.mjaroslav.mjutils.util.object.game;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Thx Dahaka for GL Lists <a href="https://forum.mcmodding.ru/threads/uskorenie-rendera-modelej.10481/">guide</a>
 *
 * @author Dahaka
 */
@SideOnly(Side.CLIENT)
public class ModelWrapperDisplayList implements IModelCustom {
    private final Map<String, Integer> lists = new HashMap<>();
    private final IntBuffer bufAll;
    private final String type;

    public ModelWrapperDisplayList(@NotNull WavefrontObject model) {
        type = model.getType();
        var list = glGenLists(model.groupObjects.size());
        for (var obj : model.groupObjects) {
            glNewList(list, GL11.GL_COMPILE);
            model.renderPart(obj.name);
            glEndList();
            lists.put(obj.name, list++);
        }
        bufAll = initBuffer();
    }

    private @NotNull IntBuffer initBuffer() {
        val buf = BufferUtils.createIntBuffer(lists.size());
        for (var i : lists.values()) buf.put(i);
        buf.flip();
        return buf;
    }

    @Override
    public @NotNull String getType() {
        return type;
    }

    @Override
    public void renderAll() {
        glCallLists(bufAll);
    }

    @Override
    public void renderOnly(@NotNull String... groupNames) {
        if (groupNames == null || groupNames.length == 0) return;
        for (String group : groupNames) renderPart(group);
    }

    @Override
    public void renderPart(@NotNull String partName) {
        val list = lists.get(partName);
        if (list != null) glCallList(list);
    }

    @Override
    public void renderAllExcept(@NotNull String... groupNames) {
        if (groupNames == null || groupNames.length == 0) {
            renderAll();
            return;
        }
        for (var entry : lists.entrySet())
            if (Arrays.binarySearch(groupNames, entry.getKey(), String::compareTo) < 0)
                glCallList(entry.getValue());
    }
}
