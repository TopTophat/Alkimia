package net.falling_pan.alkimia.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.block.entity.custom.TrayBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class TrayBlockEntityRenderer implements BlockEntityRenderer<TrayBlockEntity> {
    public TrayBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(TrayBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        boolean firstToRender = true;
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        if (!entity.getStack(0).isEmpty()) {
            matrices.push();
            matrices.translate(0.333f, 0.1f, 0.333f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            itemRenderer.renderItem(entity.getStack(0), ModelTransformationMode.GUI,
                    getLightLevel(entity.getWorld(), entity.getPos()), OverlayTexture.DEFAULT_UV,
                    matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }

        if (!entity.getStack(1).isEmpty()) {
            matrices.push();
            matrices.translate(0.333f, 0.1f, 0.666f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            itemRenderer.renderItem(entity.getStack(1), ModelTransformationMode.GUI,
                    getLightLevel(entity.getWorld(), entity.getPos()), OverlayTexture.DEFAULT_UV,
                    matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }

        if (!entity.getStack(2).isEmpty()) {
            matrices.push();
            matrices.translate(0.666f, 0.1f, 0.5f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
            itemRenderer.renderItem(entity.getStack(2), ModelTransformationMode.GUI,
                    getLightLevel(entity.getWorld(), entity.getPos()), OverlayTexture.DEFAULT_UV,
                    matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }
        if (entity.storedFluid.isBlank() || entity.amount <= 0)
            return;

        FluidVariant fluidVariant = entity.storedFluid;
        int amount = entity.amount;
        int capacity = entity.capacity;
        float fillPercentage = (float) amount / capacity;
//        System.out.println("Amount " + entity.amount);
//        System.out.println("Capacity " + entity.capacity);
//        System.out.println("Fill " + fillPercentage);
        fillPercentage = MathHelper.clamp(fillPercentage, 0, 1);

        int color = FluidVariantRendering.getColor(fluidVariant, entity.getWorld(), entity.getPos());
        Sprite sprite = FluidVariantRendering.getSprites(fluidVariant)[0];
        RenderLayer renderLayer = RenderLayer.getTranslucent();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

        float y1 = 1f / 16f;
        float y2 = ((fillPercentage * 1f) / 16f) + y1;

        float minU = sprite.getFrameU(3f / 16f);
        float maxU = sprite.getFrameU(13f / 16f);
        float minV = sprite.getFrameV(y1);
        float maxV = sprite.getFrameV(y2);

        MatrixStack.Entry entry = matrices.peek();

        // front face
        drawQuad(vertexConsumer, entry, 3f / 16f, y1, 3f / 16f, 13f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        // back face
        drawQuad(vertexConsumer, entry, 13f / 16f, y1, 13f / 16f, 3f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        // left face
        drawQuad(vertexConsumer, entry, 3f / 16f, y1, 13f / 16f, 3f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        // right face
        drawQuad(vertexConsumer, entry, 13f / 16f, y1, 3f / 16f, 13f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        if(fillPercentage < 1.1f) {
            minU = sprite.getFrameU(3f / 16f);
            maxU = sprite.getFrameU(13f / 16f);
            minV = sprite.getFrameV(3f / 16f);
            maxV = sprite.getFrameV(13f / 16f);

            vertexConsumer.vertex(entry, 3f / 16f, y2, 3f / 16f)
                    .color(color)
                    .texture(minU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 3f / 16f, y2, 13f / 16f)
                    .color(color)
                    .texture(minU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 13f / 16f, y2, 13f / 16f)
                    .color(color)
                    .texture(maxU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 13f / 16f, y2, 3f / 16f)
                    .color(color)
                    .texture(maxU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);
        }
    }

    private static void drawQuad(VertexConsumer vertexConsumer,
                                 MatrixStack.Entry entry,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float minU, float minV,
                                 float maxU, float maxV,
                                 int color,
                                 int light, int overlay) {
        vertexConsumer.vertex(entry, x1, y1, z1)
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, x1, y2, z1)
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, x2, y2, z2)
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, x2, y1, z2)
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
