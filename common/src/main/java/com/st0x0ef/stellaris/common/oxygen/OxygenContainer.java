package com.st0x0ef.stellaris.common.oxygen;

public class OxygenContainer {

    private final int maxOxygen;

    private int oxygenStored;

    public OxygenContainer(int maxOxygen) {
        this.maxOxygen = maxOxygen;
    }

    public int getOxygenStored() {
        return oxygenStored;
    }

    public void removeAllOxygenStored() {
        oxygenStored = 0;
    }

    public boolean removeOxygenStored(boolean simulate) {
        return removeOxygenStored(1, simulate);
    }

    public boolean removeOxygenStored(int amount, boolean simulate) {
        if (getOxygenStored() > 0) {
            if (!simulate) {
                oxygenStored -= amount;
            }

            return true;
        }

        return false;
    }

    public boolean addOxygenStored(int amount, boolean simulate) {
        if (amount + getOxygenStored() <= maxOxygen) {
            if (!simulate) {
                oxygenStored += amount;
                removeAllOxygenStored();
            }

            return true;
        }
        return false;
    }

    public void setOxygenStored(int amount) {
        oxygenStored = amount;
    }
}
