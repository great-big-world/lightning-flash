package dev.creoii.lightningflash.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.ExtendedLivingEntityRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getAnimationCounter(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)F"))
    private <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> float gbw$wrapOverlayForLightningOverlay(LivingEntityRenderer<T, S, M> instance, S state, Operation<Float> original, @Local(argsOnly = true) S livingEntityRenderState) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            return 1f;
        }
        return original.call(instance, state);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"))
    private <S extends LivingEntityRenderState> void gbw$wrapRenderForLightningOverlay(OrderedRenderCommandQueue instance, Model<S> model, Object o, MatrixStack matrixStack, RenderLayer renderLayer, int l, int i, int k, Sprite sprite, int outlineColor, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand, Operation<Void> original, @Local(argsOnly = true) S livingEntityRenderState) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            original.call(instance, model, o, matrixStack, renderLayer, l, Colors.WHITE, Colors.WHITE, sprite, Colors.WHITE, crumblingOverlayCommand);
        } else original.call(instance, model, o, matrixStack, renderLayer, l, i, k, sprite, outlineColor, crumblingOverlayCommand);
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private <T extends LivingEntity, S extends LivingEntityRenderState> void gbw$updateExtendedLivingRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && livingEntity instanceof ExtendedLivingEntity extendedLivingEntity) {
            extended.gbw$setStruckByLightning(extendedLivingEntity.gbw$getStruckByLightningTime() > 0);
        }
    }

    @Inject(method = "getOverlay", at = @At("HEAD"), cancellable = true)
    private static void gbw$applyLightningOverlay(LivingEntityRenderState state, float whiteOverlayProgress, CallbackInfoReturnable<Integer> cir) {
        if (state instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            cir.setReturnValue(OverlayTexture.packUv(OverlayTexture.getU(1f), OverlayTexture.getV(false)));
        }
    }
}
