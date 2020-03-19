public class Board {
    private Tile[][] tiles = new Tile[8][8];


    public void Boardd()  {
        for(int i = 0; i < tiles.length; i++) {
            for(int u = 0; u < tiles.length; u++) {
                this.tiles[i][u] = new Tile(i,u);
            }
        }
}




}

