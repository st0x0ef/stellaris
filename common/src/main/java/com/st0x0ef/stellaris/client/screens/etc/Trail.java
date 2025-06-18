package com.st0x0ef.stellaris.client.screens.etc;

import java.util.LinkedList;
import java.util.List;

public class Trail {
    public static final int MAX_TRAIL_LENGTH = 506;
    private final LinkedList<float[]> positions = new LinkedList<>();

    public void addPosition(float x, float y) {
        positions.add(new float[]{x, y});
        if (positions.size() > MAX_TRAIL_LENGTH) {
            positions.removeFirst();
        }
    }

    public List<float[]> getPositions() {
        return positions;
    }
}
