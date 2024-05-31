package com.st0x0ef.stellaris.fabric.systems.data;

import earth.terrarium.botarium.common.data.DataManager;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

@SuppressWarnings("UnstableApiUsage")
public class FabricDataManager<T> implements DataManager<T> {
    private final AttachmentType<T> attachmentType;

    public FabricDataManager(AttachmentType<T> attachmentType) {
        this.attachmentType = attachmentType;
    }

    @Override
    public T getData(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrCreate(attachmentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T getDataOrThrow(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrThrow(this.attachmentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T getDataOrInit(Object dataHolder, T data) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrSet(this.attachmentType, data);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T setData(Object dataHolder, T data) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.setAttached(this.attachmentType, data);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public boolean hasData(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.hasAttached(this.attachmentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }
}
