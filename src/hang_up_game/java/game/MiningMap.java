package hang_up_game.java.game;

import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.pos.Block;
import hang_up_game.java.pos.Chunk;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MiningMap {
	
	private static final int mapSeed = FileHolder.config.getConfig("gameData/mapSeed", Integer.class);
	private static final int chestSeed = FileHolder.config.getConfig("gameData/chestSeed", Integer.class);
	private static final Random mapR = new Random(mapSeed);
	
	public static Mineral getMineral(Chunk chunk) {
		for(int i = 0;i < chunk.X;i++) {
			for(int j = 0;j < chunk.Y;i++) {
				mapR.nextBoolean();
			}
		}
		return Mineral.getRandom((int)(mapR.nextDouble() * Mineral.getTotalWeight()));
	}
	public static Mineral getMineral(int chunkX, int chunkY) {
		return getMineral(new Chunk(chunkX, chunkY));
	}
	public static Set<Item> findItem(Block block) {
		int px;
		int py;
		if(block.blockInChunkX == 0) {
			px = block.chunk.X / (int)(Math.random() * ChestContent.getTotalWeight());
		}
		else {
			px = block.chunk.X / block.blockInChunkX;
		}
		if(block.blockInChunkY == 0) {
			py = block.chunk.Y / (int)(Math.random() * ChestContent.getTotalWeight());
		}
		else {
			py = block.chunk.Y / block.blockInChunkY;
		}
		int sum = px + py;
		if(sum == 0) {
			sum = (int)(Math.random() * ChestContent.getTotalWeight());
			if(sum == 0) sum = (int)(Math.random() * ChestContent.getTotalWeight());
		}
		int wi = chestSeed / sum;
		ChestContent cc = ChestContent.getRandom(wi % ChestContent.getTotalWeight());
		Random r = new Random();
		int count = cc.minCount + r.nextInt(cc.maxCount - cc.minCount + 1);
		Set<Item> items = new HashSet<>(count);
		for(int i = 0;i < count;i++) {
			items.add(cc.content[r.nextInt(cc.content.length)]);
		}
		if(items.size() != 0) {
			int chestC = FileHolder.map.getLastChestCount(block.chunk);
			if(chestC == -1) {
				chestC = calChestCount(block.chunk);
			}
			FileHolder.map.setLastChestCount(block.chunk, chestC - 1);
		}
		return items;
	}
	private static int calChestCount(Chunk chunk) {
		int maxChestCount = 50;
		int multi = chunk.X * chunk.Y;
		int tmpC = multi * 10 % maxChestCount;
		if(tmpC == 0) {
			tmpC = chestSeed  % maxChestCount;
		}
		return chestSeed % tmpC;
	}
	public static Set<Item> findItem(Chunk chunk, int blockInChunkX, int blockInChunkY) {
		return findItem(new Block(chunk, blockInChunkX, blockInChunkY));
	}
	public static Set<Item> findItem(int chunkX, int chunkY, int blockInChunkX, int blockInChunkY) {
		return findItem(new Chunk(chunkX, chunkY), blockInChunkX, blockInChunkY);
	}
	
	
}
