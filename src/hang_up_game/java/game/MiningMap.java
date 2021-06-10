package hang_up_game.java.game;

import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.pos.Block;
import hang_up_game.java.pos.Chunk;
import org.lf.logger.Log;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MiningMap {
	
	private static final long mapSeed = FileHolder.config.getConfig("gameData/mapSeed", Integer.class);
	private static final long chestSeed = FileHolder.config.getConfig("gameData/chestSeed", Integer.class);
	private static final Random mapR = new Random(mapSeed);
	private static final Random chestR = new Random(chestSeed);
	
	static {
		Log.d("mining map", "set map seed:" + mapSeed);
		Log.d("mining map", "set chest seed:" + chestSeed);
	}
	
	public static Mineral getMineral(Chunk chunk) {
		for(int i = 0;i < chunk.X * chunk.Y;i++) {
			mapR.nextBoolean();
		}
		return Mineral.getRandom((int)(mapR.nextDouble() * Mineral.getTotalWeight()));
	}
	public static Mineral getMineral(int chunkX, int chunkY) {
		return getMineral(new Chunk(chunkX, chunkY));
	}
	public static Set<Item> findItem(Block block) {
		int chestC = FileHolder.map.getLastChestCount(block.chunk);
		if(chestC == -1) {
			chestC = calChestCount(block.chunk);
			FileHolder.map.setLastChestCount(block.chunk, chestC);
		}
		else if(chestC == 0) {
			return new HashSet<>();
		}
		ChestContent cc = ChestContent.getRandom((int)(chestR.nextDouble() * ChestContent.getTotalWeight()));
		int count = cc.minCount + chestR.nextInt(cc.maxCount - cc.minCount + 1);
		Set<Item> items = new HashSet<>(count);
		for(int i = 0;i < count;i++) {
			items.add(cc.content[chestR.nextInt(cc.content.length)]);
		}
		if(items.size() != 0) {
			FileHolder.map.setLastChestCount(block.chunk, chestC - 1);
		}
		return items;
	}
	private static int calChestCount(Chunk chunk) {
		int maxChestCount = 50;
		int multi = chunk.X * chunk.Y;
		int tmpC = multi * 10 % maxChestCount;
		if(tmpC == 0) {
			tmpC = (int)(chestSeed  % maxChestCount);
		}
		return (int)(chestSeed % tmpC);
	}
	public static Set<Item> findItem(Chunk chunk, int blockInChunkX, int blockInChunkY) {
		return findItem(new Block(chunk, blockInChunkX, blockInChunkY));
	}
	public static Set<Item> findItem(int chunkX, int chunkY, int blockInChunkX, int blockInChunkY) {
		return findItem(new Chunk(chunkX, chunkY), blockInChunkX, blockInChunkY);
	}
	
	
}
