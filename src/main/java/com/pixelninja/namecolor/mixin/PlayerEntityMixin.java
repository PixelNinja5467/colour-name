package com.pixelninja.namecolor.mixin;
import com.pixelninja.namecolor.ColoredPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin implements ColoredPlayerEntity {

    @Shadow
    public abstract Text getName();

    Formatting color;

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    public void colorName(CallbackInfoReturnable<Text> cir) {
        Text text = new LiteralText(getName().asString()).setStyle(Style.EMPTY.withColor(color));
        cir.setReturnValue(text);
    }

    @Override
    public Formatting getColor() {
        return color;
    }

    @Override
    public void setColor(Formatting color) {
        this.color = color;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void saveColor(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("CustomNameColor", color.ordinal());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void loadColor(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CustomNameColor")) {
            color = Formatting.values()[nbt.getInt("CustomNameColor")];
        } else {
            color = Formatting.RESET;
        }
    }
}
