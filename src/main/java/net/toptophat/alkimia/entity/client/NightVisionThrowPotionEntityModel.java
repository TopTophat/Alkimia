package net.toptophat.alkimia.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.entity.custom.NightVisionThrowPotionEntity;

public class NightVisionThrowPotionEntityModel extends EntityModel<NightVisionThrowPotionEntity> {
    public static final EntityModelLayer NIGHT_VISION = new EntityModelLayer(Identifier.of(Alkimia.MOD_ID, "night_vision_throw_potion_entity"), "main");
    private final ModelPart bottle;
    private final ModelPart opaque;
    public NightVisionThrowPotionEntityModel(ModelPart root) {
        this.bottle = root.getChild("bottle");
        this.opaque = root.getChild("opaque");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bottle = modelPartData.addChild("bottle", ModelPartBuilder.create().uv(0, 0).cuboid(-14.0F, -1.0F, 2.0F, 12.0F, 1.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 28).cuboid(-14.0F, -8.0F, 9.0F, 12.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 34).cuboid(-14.0F, -8.0F, 2.0F, 12.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 44).cuboid(-14.0F, -8.0F, 7.0F, 5.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(38, 47).cuboid(-7.0F, -8.0F, 7.0F, 5.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(48, 4).cuboid(-7.0F, -11.0F, 7.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(22, 40).cuboid(-10.0F, -11.0F, 9.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(48, 0).cuboid(-10.0F, -11.0F, 6.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(38, 50).cuboid(-10.0F, -11.0F, 7.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(40, 13).cuboid(-14.0F, -7.0F, 13.0F, 12.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(40, 20).cuboid(-14.0F, -7.0F, 2.0F, 12.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(34, 28).cuboid(-14.0F, -7.0F, 3.0F, 1.0F, 6.0F, 10.0F, new Dilation(0.0F))
                .uv(0, 40).cuboid(-3.0F, -7.0F, 3.0F, 1.0F, 6.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

        ModelPartData opaque = modelPartData.addChild("opaque", ModelPartBuilder.create().uv(22, 44).cuboid(-2.0F, -14.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 13).cuboid(-5.0F, -6.0F, -5.0F, 10.0F, 5.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(NightVisionThrowPotionEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        bottle.render(matrices, vertexConsumer, light, overlay, 0x1AFFFFFF);
        opaque.render(matrices, vertexConsumer, light, overlay);
    }
}