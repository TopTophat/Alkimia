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
        Vec3d toCenter = new Vec3d(entity.boundTo[0] - entity.getPos().getX(), (entity.boundTo[1] + 0.4f) - (entity.getPos().getY() + 1f), entity.boundTo[2] - entity.getPos().getZ());
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
                entity.getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, entity.getPos().getX() + 0.5f + (entity.getWorld().random.nextGaussian() * 0.1f), entity.getPos().getY() + 1f + (entity.getWorld().random.nextGaussian() * 0.1f), entity.getPos().getZ() + 0.5f + (entity.getWorld().random.nextGaussian() * 0.1f), toCenter.normalize().x * 3f + (entity.getWorld().random.nextGaussian() * 0.1f), toCenter.normalize().y * 3f + (entity.getWorld().random.nextGaussian() * 0.1f), toCenter.normalize().z * 3f + (entity.getWorld().random.nextGaussian() * 0.1f));
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

    private void drawBeam(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            Vec3d startWorld,
            Vec3d endWorld,
            World world,
            BlockPos lightPos
    ) {
        // ----------- Compute direction & length ----------
        Vec3d dir = endWorld.subtract(startWorld);
        float length = (float) dir.length();
        if (length < 0.001f) return;

        // center beam around origin for rotation
        matrices.push();

        // Translate to the lens tip (relative to the block entity position)
        matrices.translate(startWorld.x - lightPos.getX() - 0.5,
                startWorld.y - lightPos.getY(),
                startWorld.z - lightPos.getZ() - 0.5);

        // Find yaw (around Y) and pitch (around X) to align beam with direction
        double yaw = Math.atan2(dir.x, dir.z);
        double horizontalDist = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        double pitch = Math.atan2(dir.y, horizontalDist);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) yaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) -pitch));

        // ----------- Draw a thin quad along +Z -----------
        float halfWidth = 0.05f;
        Sprite sprite = MinecraftClient.getInstance().getBlockRenderManager()
                .getModels().getModel(Blocks.YELLOW_CONCRETE.getDefaultState()).getParticleSprite();

        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
        MatrixStack.Entry entry = matrices.peek();
        int light = getLightLevel(world, lightPos);

        // front face
        drawQuad(vc, entry,
                -halfWidth, -halfWidth, 0,
                halfWidth,  halfWidth, length,
                sprite.getMinU(), sprite.getMinV(),
                sprite.getMaxU(), sprite.getMaxV(),
                0xFFFFFFFF,
                light,
                OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}
