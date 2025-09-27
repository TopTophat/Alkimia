package net.falling_pan.alkimia.block.entity.renderer;

import net.falling_pan.alkimia.block.entity.custom.InfuserOutputBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class InfuserOutputBlockEntityRenderer implements BlockEntityRenderer<InfuserOutputBlockEntity> {
    public InfuserOutputBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(InfuserOutputBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack stack = entity.getStack(0);

        matrices.push();
        matrices.translate(0.5f, 0.6f, 0.5f);
        matrices.scale(0.65f, 0.65f, 0.65f);
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));

        itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
