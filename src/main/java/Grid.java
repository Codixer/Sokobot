public class Grid
{
    final int GROUND = 0;
    final int WALL = 1;
    final int BOX = 2;
    final int DESTINATION = 3;
    final int PLAYER = 4;
    final int MAX_BOXES = 8;
    Tile[][] grid;
    Box[] boxes;
    Destination[] destinations;
    int boxCount;
    int height = 0;
    int width = 0;
    int color = 0;
    Player player;
    Randomizer rand = new Randomizer();
    public Grid(int width, int height, int boxCount) //create a random grid with specific width, height, and number of boxes
    {
        player = new Player(2, 2, this);
        if (boxCount > MAX_BOXES)
        {
            boxCount = MAX_BOXES;
        }
        this.boxCount = boxCount;
        boxes = new Box[boxCount];
        destinations = new Destination[boxCount];
        this.height = height;
        this.width = width;
        grid = new Tile[width][height];
        createBoxes();
        createDestinations();
        updateGrid();
    }
    public Player getPlayer()
    {
        return player;
    }
    public void reset()
    {
        player.setPosition(2, 2);
        for (int i = 0; i < boxCount; i++)
        {
            boxes[i].reset();
        }
        updateGrid();
    }
    public void setStatus(int x, int y, int status)
    {
        grid[x][y].setStatus(status);
    }
    public int getStatus(int x, int y)
    {
        return grid[x][y].getStatus();
    }
    public boolean isWall(int x, int y)
    {
        return grid[x][y].getStatus() == WALL;
    }
    public boolean isBox(int x, int y)
    {
        return grid[x][y].getStatus() == BOX;
    }
    public boolean isBoxRaw(int x, int y) //allows you to check if a box is at a position before grid is set up
    {
        for (int i = 0; i < boxCount; i++)
        {
            if (x == boxes[i].getX() && y == boxes[i].getY())
            {
                return true;
            }
        }
        return false;
    }
    public boolean isDestination(int x, int y)
    {
        return grid[x][y].getStatus() == DESTINATION;

    }
    public Box getBox(int x, int y)
    {
        for (int i = 0; i < boxCount; i++)
        {
            if (x == boxes[i].getX() && y == boxes[i].getY())
            {
                return boxes[i];
            }
        }
        return null;
    }
    public void createBoxes()
    {
        color = rand.nextInt(6); //runs after each level
        for (int i = 0; i < boxCount; i++)
        {
            int x = rand.nextInt(width - 4) + 2;
            int y = rand.nextInt(height - 4) + 2;
            for (int j = 0; j < i; j++)
            {
                while ((x == boxes[j].getX() && y == boxes[j].getY()) || (x == 2 && y == 2))
                {
                    x = rand.nextInt(width - 4) + 2;
                    y = rand.nextInt(height - 4) + 2;
                }
            }
            boxes[i] = new Box(x, y, this);
        }
    }
    public void createDestinations()
    {
        for (int i = 0; i < boxCount; i++)
        {
            int x = rand.nextInt(width - 2) + 1;
            int y = rand.nextInt(height - 2) + 1;
            for (int j = 0; j < i; j++)
            {
                while (((x == destinations[j].getX() && y == destinations[j].getY())) || isBoxRaw(x, y))
                {
                    x = rand.nextInt(width - 2) + 1;
                    y = rand.nextInt(height - 2) + 1;
                }
            }
            destinations[i] = new Destination(x, y, this);
        }
    }
    public void updateGrid()
    {
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                grid[j][i] = new Tile(GROUND);
                if (j == 0 || j == width - 1 || i == 0 || i == height - 1)
                {
                    grid[j][i] = new Tile(WALL, color);
                }
                for (int k = 0; k < boxCount; k++)
                {
                    if (destinations[k].getX() == j && destinations[k].getY() == i)
                    {
                        grid[j][i] = new Tile(DESTINATION);
                    }
                }
                if (player.getX() == j && player.getY() == i)
                {
                    grid[j][i] = new Tile(PLAYER);
                }
                for (int k = 0; k < boxCount; k++)
                {
                    if (boxes[k].getX() == j && boxes[k].getY() == i)
                    {
                        if (boxes[k].onDestination())
                        {
                            grid[j][i] = new Tile(WALL, color);
                        } else {
                            grid[j][i] = new Tile(BOX);
                        }

                    }
                }
            }
        }
    }
    public boolean hasWon()
    {
        for (int i = 0; i < boxCount; i++)
        {
            if (!destinations[i].hasBox(this))
            {
                return false;
            }
        }
        return true;
    }
    public Tile[][] getGrid()
    {
        return grid;
    }
    public Box[] getBoxes()
    {
        return boxes;
    }
    public Destination[] getDestinations()
    {
        return destinations;
    }
    public int getBoxCount()
    {
        return boxCount;
    }
    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }
    public String toString()
    {
        updateGrid();
        String result = "";
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                result += grid[j][i];
            }
            result += "\n";
        }
        return result;
    }

}