package net.toptophat.alkimia.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.entity.custom.NightVisionThrowPotionEntity;
import net.toptophat.alkimia.item.ModItems;
import net.toptophat.alkimia.item.custom.NightVisionThrowPotion;

public class NightVisionThrowPotionEntityRenderer extends EntityRenderer<NightVisionThrowPotionEntity> {
    protected NightVisionThrowPotionEntityModel model;

    public NightVisionThrowPotionEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new NightVisionThrowPotionEntityModel(ctx.getPart(NightVisionThrowPotionEntityModel.NIGHT_VISION));
    }

    @Override
    public void render(NightVisionThrowPotionEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw())));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getRenderingRotation() * 5f), 0.5f, 0.7f, 0.5f);
        matrices.translate(0, 0f, 0);

//        VertexConsumer vertexconsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers,
//                this.model.getLayer(Identifier.of(Alkimia.MOD_ID, "textures/entity/night_vision_throw_potion/night_vision_throw_potion.png")), false, false);
//        this.model.render(matrices, vertexconsumer, light, OverlayTexture.DEFAULT_UV);

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        itemRenderer.renderItem(ModItems.NIGHT_VISION_THROW_POTION.getDefaultStack(), ModelTransformationMode.GUI, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(NightVisionThrowPotionEntity entity) {
        return Identifier.of(Alkimia.MOD_ID, "textures/entity/night_vision_throw_potion/night_vision_throw_potion.png");
    }
}
