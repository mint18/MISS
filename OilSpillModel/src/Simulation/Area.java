package Simulation;


public class Area {

    private int size;
    private double windForce;
    private Direction windDirection;
    private double[] windVecValue = new double[8];
    private double temperature;
    private Cell[][] areaGrid;
    public int sourceX = -1;
    public int sourceY = -1;
    private double sourceLevel = 500;
    int sX=150;
    int sY=150;

    public Area(int size) {
        this.size = size;
        areaGrid = new Cell[this.size][this.size];
        generateArea();
    }

    public void genWindVectors() {
        int windDirection = this.windDirection.ordinal();
        double k=1;
        this.windVecValue[windDirection] = k*this.windForce;
        this.windVecValue[(windDirection + 4) % 8] = -k*this.windForce;
        this.windVecValue[(windDirection + 1) % 8] = k*this.windForce / 2;
        this.windVecValue[(windDirection + 7) % 8] = k*this.windForce / 2;
        this.windVecValue[(windDirection + 3) % 8] = -k*this.windForce / 2;
        this.windVecValue[(windDirection + 5) % 8] = -k*this.windForce / 2;
        this.windVecValue[(windDirection + 2) % 8] = 0;
        this.windVecValue[(windDirection + 6) % 8] = 0;
    }

    public void generateArea() {

        for (int x = 0; x < this.size; x++)
            for (int y = 0; y < this.size; y++) {
                areaGrid[x][y] = new Cell(x, y, Type.WATER);
                areaGrid[x][y].setType(Type.WATER);
            }
    }


    public void genSource() {
        this.generateSpillSource(this.sX, this.sY);
    }

    public void generateSpillSource(int x, int y) {
        areaGrid[x][y].setType(Type.SOURCE);
        this.sourceX = x;
        this.sourceY = y;
    }


    public void updateSource() {
        if (sourceLevel <= 0) return;
        sourceLevel -= 10.0 - getSource().getOilLevel();
        getSource().setOilLevel(10.0 - (sourceLevel >= 0 ? 0 : sourceLevel));
    }

    public void spreading() {

        for (int x = 1; x < this.getSize() - 1; x++) {
            for (int y = 1; y < this.getSize() - 1; y++) {
                areaGrid[x][y].updateOilLevel();
            }
        }
        updateSource();
    }

    public void checkOilLevel() {

        for (int x = 1; x < this.getSize() - 1; x++) {
            for (int y = 1; y < this.getSize() - 1; y++) {
                areaGrid[x][y].genNewOilLevel(this);
            }
        }
        this.spreading();
    }

    public void setSimulationData(String windDirection, Double windSpeed, String waterDirection, Double waterSpeed, double temperature) {
        this.windDirection = Direction.stringToDirection(windDirection);
        this.windForce = windSpeed;
        this.temperature = temperature;
        for (int x = 0; x < this.size; x++)
            for (int y = 0; y < this.size; y++) {
                areaGrid[x][y].setCurrVec(waterSpeed, Direction.stringToDirection(waterDirection));
            }

    }

    public static int getOilCount(Area area) {
        int oils = 0;

        int result;

        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                switch (area.getCell(i, j).getType()) {
                    case OIL:
                        oils++;
                        break;
                    default:
                        break;
                }
            }
        }
        result = oils;

        return result;
    }

    public int getSize() {
        return this.size;
    }

    private Cell getSource() {
        return areaGrid[sourceX][sourceY];
    }

    public double getTemperature() {
        return this.temperature;
    }

    public Cell getCell(int x, int y) {
        return areaGrid[x][y];
    }

    public double getWindVecValue(Direction windDirection) {
        return this.windVecValue[windDirection.ordinal()];
    }
}
