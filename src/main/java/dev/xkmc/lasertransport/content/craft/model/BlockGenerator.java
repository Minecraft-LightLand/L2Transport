package dev.xkmc.lasertransport.content.craft.model;

import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.lasertransport.content.craft.block.Orientation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.ModelFile;

public class BlockGenerator {

	private final DataGenContext<Block, DelegateBlock> ctx;
	private final RegistrateBlockstateProvider pvd;
	private final ModelFile[] files = new ModelFile[FaceType.values().length];

	public BlockGenerator(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		this.ctx = ctx;
		this.pvd = pvd;
		String texture = "block/node/craft/" + ctx.getName().split("_")[1];
		ResourceLocation side = pvd.modLoc(texture + "/side");
		ResourceLocation empty = pvd.modLoc("block/node/empty");
		for (int i = 0; i < FaceType.values().length; i++) {
			FaceType type = FaceType.values()[i];
			files[i] = pvd.models().withExistingParent(ctx.getName() + "_type_" + i,
							pvd.modLoc("block/craft_base" + (type.alt ? "_alt" : "")))
					.texture("north", pvd.modLoc(texture + "/front_" + type.id))
					.texture("south", pvd.modLoc(texture + "/back_" + type.id))
					.texture("up", type.open(Direction.UP) ? empty : side)
					.texture("down", type.open(Direction.DOWN) ? empty : side)
					.texture("east", type.open(Direction.EAST) ? empty : side)
					.texture("west", type.open(Direction.WEST) ? empty : side)
					.renderType("cutout");
		}
	}

	public void generate(IntegerProperty prop, boolean extra) {
		var builder = pvd.getMultipartBuilder(ctx.getEntry());
		boolean[] gen = new boolean[6 * 16];
		for (FaceType t : FaceType.values()) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					Orientation orient = Orientation.rotate(t.toOrientation(), new RotationHelper(i, j));
					if (gen[orient.ordinal]) continue;
					gen[orient.ordinal] = true;
					builder.part().modelFile(files[t.ordinal()]).rotationX(90 * i).rotationY(90 * j)
							.addModel().condition(prop, orient.ordinal).end();
				}
			}
		}
		if (extra) {
			String texture = "block/node/craft/" + ctx.getName().split("_")[1] + "/front_0";
			builder.part().modelFile(pvd.models().cubeAll(ctx.getName(),
							pvd.modLoc(texture)).renderType("cutout"))
					.addModel().condition(prop, 6 * 16).end();
		} else {
			pvd.models().withExistingParent(ctx.getName(), pvd.modLoc(ctx.getName() + "_type_0"));
		}
	}

}
