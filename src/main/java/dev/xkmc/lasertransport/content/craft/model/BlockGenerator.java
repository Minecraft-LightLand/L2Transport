package dev.xkmc.lasertransport.content.craft.model;

import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.lasertransport.content.craft.block.Orientation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.ModelFile;

public class BlockGenerator {

	private static Orientation.IRotate rotate(int y, int x) {
		return dire -> {
			for (int i = 0; i < y; i++) {
				dire = dire.getClockWise(Direction.Axis.Y);
			}
			for (int i = 0; i < x; i++) {
				dire = dire.getClockWise(Direction.Axis.X);
			}
			return dire;
		};
	}

	private final DataGenContext<Block, DelegateBlock> ctx;
	private final RegistrateBlockstateProvider pvd;
	private final ModelFile[] files = new ModelFile[FaceType.values().length];

	public BlockGenerator(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		this.ctx = ctx;
		this.pvd = pvd;
		String texture = "block/node/craft/" + ctx.getName().split("_")[1];
		for (int i = 0; i < FaceType.values().length; i++) {
			files[i] = pvd.models().cubeBottomTop(ctx.getName() + "_type_" + i,
							pvd.modLoc(texture + "/side"),
							pvd.modLoc(texture + "/bottom"),
							pvd.modLoc(texture + "/front_" + i))
					.renderType("cutout");
		}
	}

	public void generate(IntegerProperty prop, boolean extra) {
		var builder = pvd.getMultipartBuilder(ctx.getEntry());
		boolean[] gen = new boolean[6 * 16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (FaceType t : FaceType.values()) {
					int orient = t.toOrientation().ordinal;
					orient = Orientation.rotate(orient, rotate(i, j));
					if (gen[orient]) continue;
					gen[orient] = true;
					builder.part().modelFile(files[t.ordinal()]).rotationY(90 * i).rotationX(90 * j)
							.addModel().condition(prop, orient).end();
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
