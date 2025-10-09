// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class night_vision_throw_potion extends EntityModel<Entity> {
	private final ModelPart bottle;
	private final ModelPart opaque;
	public night_vision_throw_potion(ModelPart root) {
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
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bottle.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		opaque.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}