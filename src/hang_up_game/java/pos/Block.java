package hang_up_game.java.pos;

public class Block {
	
	public final Chunk chunk;
	public final int blockInChunkX;
	public final int blockInChunkY;
	private int lastMineral;
	
	public Block(Chunk c, int x, int y, int lastMineral) {
		if(x < 1 || x > 9 || y < 0 || y > 9) throw new IllegalArgumentException("A block in chunk must in 0~9");
		if(lastMineral < 0 || lastMineral > 5) throw new IllegalArgumentException("Mineral must in 0~5");
		chunk = c;
		blockInChunkX = x;
		blockInChunkY = y;
		this.lastMineral = lastMineral;
	}
	
	public void setMineral(int value) {
		if(value < 0 || value > 5) throw new IllegalArgumentException("Mineral must in 0~5");
		lastMineral = value;
	}
	
	public int getLastMineral() {
		return lastMineral;
	}
	
}
