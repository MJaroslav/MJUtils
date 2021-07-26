package com.github.mjaroslav.mjutils.object.item;

import com.github.mjaroslav.mjutils.util.common.UtilsItemStack;
import com.github.mjaroslav.mjutils.util.common.UtilsItemStack.CompareParameter;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Set for ItemStacks.
 */
// TODO: Доделать как полную реализацию Set<ItemStack>
@SuppressWarnings("UnusedReturnValue")
public class ItemStackSet implements Iterable<ItemStack> {
    private ItemStack[] data;
    private final CompareParameter[] params;

    public ItemStackSet(CompareParameter... params) {
        this.params = params;
        data = new ItemStack[]{};
    }

    public ItemStackSet(Collection<ItemStack> stacks, CompareParameter... params) {
        this.params = params;
        data = new ItemStack[]{};
        addAll(stacks);
    }

    public boolean add(ItemStack stack) {
        if (!contains(stack)) {
            ItemStack[] newData = new ItemStack[data.length + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[data.length] = stack;
            data = newData;
            return true;
        } else return false;
    }

    public void clear() {
        data = new ItemStack[]{};
    }

    public boolean remove(ItemStack stack) {
        boolean has = false;
        ItemStack[] newData = new ItemStack[data.length];
        int pos = 0;
        for (ItemStack check : data) {
            if (UtilsItemStack.isEquals(check, stack, params)) {
                has = true;
                break;
            } else {
                newData[pos] = check;
                pos++;
            }
        }
        if (has) {
            data = new ItemStack[data.length - 1];
            System.arraycopy(newData, 0, data, 0, data.length);
            return true;
        } else return false;
    }

    public boolean contains(ItemStack stack) {
        for (ItemStack check : data) {
            if (UtilsItemStack.isEquals(check, stack, params))
                return true;
        }
        return false;
    }

    public boolean addAll(Collection<ItemStack> stacks) {
        ItemStack[] newData = new ItemStack[stacks.size()];
        int delta = 0;
        int pos = 0;
        for (ItemStack stack : stacks)
            if (!contains(stack)) {
                newData[pos] = stack;
                pos++;
                delta++;
            }
        if (delta > 0) {
            data = new ItemStack[data.length + delta];
            System.arraycopy(newData, 0, data, data.length - delta, data.length);
            return true;
        } else return false;
    }

    public boolean removeAll(Collection<ItemStack> stacks) {
        int delta = 0;
        int pos = 0;
        ItemStack[] newData = new ItemStack[data.length];
        for (ItemStack stack : stacks)
            if (contains(stack))
                delta++;
            else {
                newData[pos] = stack;
                pos++;
            }
        if (delta > 0) {
            data = new ItemStack[data.length - delta];
            System.arraycopy(newData, 0, data, 0, data.length);
            return true;
        } else return false;
    }

    public ItemStackSet copy() {
        ItemStackSet result = new ItemStackSet(params);
        ItemStack[] copyData = new ItemStack[data.length];
        for (int i = 0; i < data.length; i++)
            copyData[i] = data[i].copy();
        result.data = copyData;
        return result;
    }

    public int size() {
        return data != null ? data.length : 0;
    }

    public ItemStack[] toArray() {
        return data.clone();
    }

    public List<ItemStack> list() {
        return data != null ? Arrays.asList(data) : Collections.emptyList();
    }

    public Set<ItemStack> set() {
        return data != null ? new HashSet<>(list()) : Collections.emptySet();
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return set().iterator();
    }
}
