package com.st0x0ef.stellaris.common.blocks.machines.gauge;

public class GaugeValueFluidStack{

//    private FluidStack stack;
    private int capacity;

    public GaugeValueFluidStack() {

    }

//    public GaugeValueFluidStack(FluidStack stack, int capacity) {
//        this.setStack(stack);
//        this.setCapacity(capacity);
//    }

//    @Override
//    public void deserializeNBT(CompoundTag compound) {
//        this.setStack(FluidStack.loadFluidStackFromNBT(compound.getCompound("stack")));
//        this.setCapacity(compound.getInt("capacity"));
//    }
//
//    @Override
//    public CompoundTag serializeNBT() {
//        CompoundTag compound = new CompoundTag();
//        CompoundTag stackCompound = new CompoundTag();
//        this.getStack().writeToNBT(stackCompound);
//        compound.put("stack", stackCompound);
//        compound.putLong("capacity", this.getCapacity());
//
//        return compound;
//    }

//    @Override
//    public int getColor() {
//        FluidStack fluidStack = this.getStack();
//        Fluid fluid = fluidStack.getFluid();
//        if (fluid == Fluids.EMPTY
//                || !(fluid..getRenderPropertiesInternal() instanceof IClientFluidTypeExtensions props)) {
//            return 0;
//        }
//        return props.getTintColor(fluidStack);
//    }

//    public FluidStack getStack() {
//        return this.stack;
//    }
//
//    public void setStack(FluidStack stack) {
//        this.stack = stack;
//    }
//
//    @Override
//    public Component getDisplayName() {
//        return this.getStack().getDisplayName();
//    }
//
//    @Override
//    public String getUnit() {
//        return GaugeValueHelper.FLUID_UNIT;
//    }
//
//    @Override
//    public long getAmount() {
//        return this.getStack().getAmount();
//    }

//    @Override
//    public long getCapacity() {
//        return this.capacity;
//    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

//    @Override
//    public boolean isReverse() {
//        return false;
//    }

}
