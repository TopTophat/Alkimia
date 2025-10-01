package net.toptophat.alkimia.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.ItemPedestal;
import net.toptophat.alkimia.block.entity.custom.ItemPedestalBlockEntity;
import net.toptophat.alkimia.block.entity.custom.LensBlockEntity;
import net.toptophat.alkimia.block.entity.custom.LightCollectorBlockEntity;
import org.joml.Vector3f;

import java.util.List;

public class LensBlockEntityRenderer implements BlockEntityRenderer<LensBlockEntity> {
    public LensBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(LensBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Vec3d toCenter = new Vec3d(entity.boundToGraphics[0] - entity.getPos().toCenterPos().x, (entity.boundToGraphics[1] + 0.4f) - (entity.getPos().toCenterPos().y + 11f / 16f), entity.boundToGraphics[2] - entity.getPos().toCenterPos().z);
        double dx = toCenter.x;
        double dy = toCenter.y;
        double dz = toCenter.z;
        float yrot = (float) Math.toDegrees(Math.atan2(dx, dz));
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        float xrot = (float) Math.toDegrees(Math.atan2(dy, horizontalDist));

        if (entity.isBoundValid) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yrot));

            itemRenderer.renderItem(ModBlocks.LENS_ROTARY_STAND.asItem().getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            matrices.push();
            matrices.translate(0.5f, 15f / 16f, 0.5f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yrot));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-xrot), 0f, 0f, 0f);
            itemRenderer.renderItem(ModBlocks.LENS_LENS.asItem().getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            if (entity.recipeIndex != 0 && entity.getWorld().getBlockEntity(new BlockPos(entity.boundTo[0], entity.boundTo[1], entity.boundTo[2])) instanceof LightCollectorBlockEntity lightCollector && lightCollector.isRunning)
            {
                //entity.getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, entity.getPos().getX() + 0.5f + (entity.getWorld().random.nextGaussian() * 0.1f), entity.getPos().getY() + 1f + (entity.getWorld().random.nextGaussian() * 0.1f), entity.getPos().getZ() + 0.5f + (entity.getWorld().random.nextGaussian() * 0.1f), toCenter.normalize().x * 3f + (entity.getWorld().random.nextGaussian() * 0.1f), toCenter.normalize().y * 3f + (entity.getWorld().random.nextGaussian() * 0.1f), toCenter.normalize().z * 3f + (entity.getWorld().random.nextGaussian() * 0.1f));
                Vec3d targetPoint = new Vec3d(entity.boundToGraphics[0], entity.boundToGraphics[1] - 0.1f, entity.boundToGraphics[2]).relativize(Vec3d.of(entity.getPos())).negate();
                Vec3d pointNWadd = new Vec3d(-( 6f / 16f), 6f / 16f, 1 / 16f).rotateX((float) Math.toRadians(xrot)).rotateY((float) Math.toRadians(yrot));
                Vec3d pointNEadd = new Vec3d(6f / 16f, 6f / 16f, 1 / 16f).rotateX((float) Math.toRadians(xrot)).rotateY((float) Math.toRadians(yrot));
                Vec3d pointSWadd = new Vec3d(-( 6f / 16f), -(6f / 16f), 1 / 16f).rotateX((float) Math.toRadians(xrot)).rotateY((float) Math.toRadians(yrot));
                Vec3d pointSEadd = new Vec3d(6f / 16f, -(6f / 16f), 1 / 16f).rotateX((float) Math.toRadians(xrot)).rotateY((float) Math.toRadians(yrot));

                Vec3d pointNW = new Vec3d(0.5f, 15f / 16f, 0.5f).add(pointNWadd);
                Vec3d pointNE = new Vec3d(0.5f, 15f / 16f, 0.5f).add(pointNEadd);
                Vec3d pointSW = new Vec3d(0.5f, 15f / 16f, 0.5f).add(pointSWadd);
                Vec3d pointSE = new Vec3d(0.5f, 15f / 16f, 0.5f).add(pointSEadd);


                FluidVariant variant = FluidVariant.of(Fluids.WATER);
                Sprite sprite = FluidVariantRendering.getSprites(variant)[0];
                int color = 0x32C2D4ED;
                if (entity.recipeIndex == 1)
                {
                    color = 0x32E2E3AA;
                }
                if (entity.recipeIndex == 2)
                {
                    color = 0x32C2D4ED;
                }
                if (entity.recipeIndex == 3)
                {
                    color = 0x32D6D6D6;
                }

                float minU = sprite.getFrameU(1);
                float maxU = sprite.getFrameU(1);
                float minV = sprite.getFrameV(1);
                float maxV = sprite.getFrameV(1);

                drawSpike(vertexConsumers.getBuffer(RenderLayer.getTextBackgroundSeeThrough()), matrices.peek(), minU, minV, maxU, maxV, color, light, overlay, targetPoint, pointNE, pointNW, pointSE, pointSW);
            }

        } else {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);

            itemRenderer.renderItem(ModBlocks.LENS_ROTARY_STAND.asItem().getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            matrices.push();
            matrices.translate(0.5f, 15f / 16f, 0.5f);

            itemRenderer.renderItem(ModBlocks.LENS_LENS.asItem().getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

    public void drawSpike(VertexConsumer vertexConsumer, MatrixStack.Entry entry, float minU, float minV, float maxU, float maxV, int color, int light, int overlay, Vec3d tip, Vec3d NE, Vec3d NW, Vec3d SE, Vec3d SW)
    {
        // top
        vertexConsumer.vertex(entry, NE.toVector3f())
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, NW.toVector3f())
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);
        // west
        vertexConsumer.vertex(entry, NW.toVector3f())
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, SW.toVector3f())
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        // east

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, SE.toVector3f())
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, NE.toVector3f())
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        // bottom
        vertexConsumer.vertex(entry, SE.toVector3f())
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, SW.toVector3f())
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, tip.toVector3f())
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        //back

        vertexConsumer.vertex(entry, SE.toVector3f())
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, SW.toVector3f())
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, NW.toVector3f())
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, NE.toVector3f())
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);
    }
}
