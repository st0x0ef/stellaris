package com.st0x0ef.stellaris.client.screens.info;

import com.st0x0ef.stellaris.client.screens.record.PSystemRecord;

import java.util.List;

public record PSystemInfo(String name, String translatable, String parent, String id,
                          List<PSystemRecord.StarPosition> stars, float centerX, float centerY) {
}
