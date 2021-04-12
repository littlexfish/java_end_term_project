package hang_up_game.java.game;

import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.pos.Block;
import hang_up_game.java.pos.Chunk;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MiningMap {
	
	private static long mapSeed = FileHolder.config.getConfig("gameData/mapSeed", Long.class);
	private static long chestSeed = FileHolder.config.getConfig("gameData/chestSeed", Long.class);
	private static int prevent0 = (int)((Math.random() * mapSeed) / chestSeed);
	private static int prevent0_ = (int)(Math.random() * Mineral.getTotalWeight());
	private static int preventBlock0 = (int)(Math.random() * 9) + 1;
	
	public static Mineral getMineral(Chunk chunk) {
		int px;
		int py;
		if(chunk.X == 0) {
			if(prevent0 == 0) {
				px = (int)(mapSeed / prevent0_);
			}
			else {
				px = (int)(mapSeed / prevent0);
			}
		}
		else {
			px = (int)(mapSeed / chunk.X);
		}
		if(chunk.Y == 0) {
			if(prevent0 == 0) {
				py = (int)(mapSeed / prevent0_);
			}
			else {
				py = (int)(mapSeed / prevent0);
			}
		}
		else {
			py = (int)(mapSeed / chunk.Y);
		}
		int sum = px + py;
		return Mineral.getRandom(sum % Mineral.getTotalWeight());
	}
	public static Mineral getMineral(int chunkX, int chunkY) {
		return getMineral(new Chunk(chunkX, chunkY));
	}
	public static Set<Item> findItem(Block block) {
		int px;
		int py;
		if(block.blockInChunkX == 0) {
			px = block.chunk.X / preventBlock0;
		}
		else {
			px = block.chunk.X / block.blockInChunkX;
		}
		if(block.blockInChunkY == 0) {
			py = block.chunk.Y / preventBlock0;
		}
		else {
			py = block.chunk.Y / block.blockInChunkY;
		}
		int sum = px + py;
		if(sum == 0) {
			sum = prevent0;
			if(sum == 0) sum = prevent0_;
		}
		int wi = (int)(chestSeed / sum);
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
		int tmpC = multi % maxChestCount;
		if(tmpC == 0) {
			tmpC = prevent0_ % maxChestCount;
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
