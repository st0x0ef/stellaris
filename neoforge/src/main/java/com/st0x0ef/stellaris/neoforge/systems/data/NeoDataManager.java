package com.st0x0ef.stellaris.neoforge.systems.data;

import com.st0x0ef.stellaris.common.systems.data.DataManager;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

public class NeoDataManager<T> implements DataManager<T> {
    private final Supplier<AttachmentType<T>> attachmentType;
    public NeoDataManager(Supplier<AttachmentType<T>> attachmentType) {
        this.attachmentType = attachmentType;
    }

    @Override
    public T getData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.getData(attachmentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public T getDataOrThrow(Object dataHolder) {
        if (!hasData(dataHolder)) {
            throw new RuntimeException(dataHolder + "is not initialized");
        } else {
            return getData(dataHolder);
        }
    }

    @Override
    public T getDataOrInit(Object dataHolder, T data) {
        if (!hasData(dataHolder)) {
            setData(dataHolder, data);
        }
        return getData(dataHolder);
    }

    @Override
    public T setData(Object dataHolder, T data) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.setData(attachmentType, data);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public boolean hasData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.hasData(attachmentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }
}
