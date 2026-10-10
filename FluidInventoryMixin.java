package example.createfix.mixin;

import com.zurrtum.create.AllDataComponents;
import com.zurrtum.create.infrastructure.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

/**
 * Same bug as ContainerExtensionMixin, for fluids: FluidInventory.setMaxSize() puts an
 * Optional<Integer> into the FluidStack's component patch map (FLUID_MAX_CAPACITY), which
 * crashes when a pipe/tank/basin saves or syncs its fluid.
 */
@Mixin(targets = "com.zurrtum.create.infrastructure.fluids.FluidInventory")
public interface FluidInventoryMixin {

    @Overwrite
    default void setMaxSize(FluidStack stack, Optional<Integer> limit) {
        if (limit.isPresent()) {
            stack.set(AllDataComponents.FLUID_MAX_CAPACITY, limit.get());
        }
    }

    @Overwrite
    default FluidStack removeMaxSize(FluidStack stack, Optional<Integer> limit) {
        Integer current = stack.directComponents().get(AllDataComponents.FLUID_MAX_CAPACITY);
        if (limit.isPresent() && limit.get().equals(current)) {
            stack.remove(AllDataComponents.FLUID_MAX_CAPACITY);
        }
        return stack;
    }
}
