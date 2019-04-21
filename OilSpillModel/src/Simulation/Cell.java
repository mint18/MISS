package Simulation;

public class Cell {


    public static final double OIL_B_ADJ = 0.14;
    public static final double OIL_B_DIA = 0.18 * OIL_B_ADJ;

    private int x;
    private int y;

    private Type type;

    private double oilLevel;
    private double nextOilLevel;

    private Direction CurrDirection;
    private double currForce;
    private double[] CurrVecValue = new double[8];

    public Cell(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void updateOilLevel() {
        if (this.nextOilLevel > 0) {
            this.oilLevel = this.nextOilLevel;
        } else {
            this.oilLevel = 0;
        }

        if (this.type == Type.WATER && this.oilLevel > 0) {
            this.type = Type.OIL;
        } else if (this.type == Type.OIL && this.oilLevel == 0) {
            this.type = Type.WATER;
        }
    }

    public double reduction(double oilLevel, Area area) {
        double nextOilLevel = oilLevel;

        double reduction_coeff = 1E-15;

        nextOilLevel -= reduction_coeff * (area.getTemperature() + 273);

        if (nextOilLevel < 0) {
            nextOilLevel = 0;
        }
        return nextOilLevel;
    }

    private void genCurrentVectors() {
        int direction = this.CurrDirection.ordinal();
        this.CurrVecValue[direction] = this.currForce;
        this.CurrVecValue[(direction + 4) % 8] = -this.currForce;
        this.CurrVecValue[(direction + 1) % 8] = this.currForce / 2;
        this.CurrVecValue[(direction + 7) % 8] = this.currForce / 2;
        this.CurrVecValue[(direction + 3) % 8] = -this.currForce / 2;
        this.CurrVecValue[(direction + 5) % 8] = -this.currForce / 2;
        this.CurrVecValue[(direction + 2) % 8] = 0;
        this.CurrVecValue[(direction + 6) % 8] = 0;
    }

    public void genNewOilLevel(Area area) {
        if (this.getCountObliqueCells(area, Type.OIL) == 0 &&
                this.getCountObliqueCells(area, Type.SOURCE) == 0) {
            return;
        }

        double nextOilLevel = this.oilLevel;

        double oilChange = 0;
        Cell cell;
        Type cellType;

        cell = area.getCell(x - 1, y);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.S) + this.getCurrVecValue(Direction.S)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x + 1, y);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.N) + this.getCurrVecValue(Direction.N)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x, y - 1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.E) + this.getCurrVecValue(Direction.E)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x, y + 1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.W) + this.getCurrVecValue(Direction.W)) * cell.getOilLevel() - this.oilLevel;

        nextOilLevel += oilChange * this.OIL_B_ADJ;
        oilChange = 0;

        cell = area.getCell(x - 1, y - 1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.SW) + this.getCurrVecValue(Direction.SW)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x + 1, y - 1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.NW) + this.getCurrVecValue(Direction.NW)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x - 1, y + 1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.SE) + this.getCurrVecValue(Direction.SE)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x + 1, y + 1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1 + area.getWindVecValue(Direction.NE) + this.getCurrVecValue(Direction.NE)) * cell.getOilLevel() - this.oilLevel;

        nextOilLevel += oilChange * this.OIL_B_DIA;

        nextOilLevel = reduction(nextOilLevel, area);

        this.nextOilLevel = nextOilLevel;
    }

    public int getCountPerpendicularCells(Area area, Type cellType) {
        int neighbours = 0;
        if (y > 0 && area.getCell(x, y - 1).getType() == cellType) // N
            neighbours++;
        if (x < area.getSize() - 1 && area.getCell(x + 1, y).getType() == cellType) // E
            neighbours++;
        if (y < area.getSize() - 1 && area.getCell(x, y + 1).getType() == cellType) // S
            neighbours++;
        if (x > 0 && area.getCell(x - 1, y).getType() == cellType) // W
            neighbours++;

        return neighbours;
    }

    public int getCountObliqueCells(Area area, Type cellType) {
        int neighbours = 0;

        neighbours += this.getCountPerpendicularCells(area, cellType);

        if (x > 0 && y > 0 && area.getCell(x - 1, y - 1).getType() == cellType) // NW
            neighbours++;
        if (x > 0 && y < area.getSize() - 1 && area.getCell(x - 1, y + 1).getType() == cellType) // SE
            neighbours++;
        if (x < area.getSize() - 1 && y > 0 && area.getCell(x + 1, y - 1).getType() == cellType) // NE
            neighbours++;
        if (x < area.getSize() - 1 && y < area.getSize() - 1 && area.getCell(x + 1, y + 1).getType() == cellType) // SE
            neighbours++;

        return neighbours;
    }

    public void setOilLevel(double oilLevel) {
        this.oilLevel = oilLevel;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setCurrVec(double currF, Direction direction) {
        this.currForce = currF;
        this.CurrDirection = direction;
        this.genCurrentVectors();
    }

    public double getOilLevel() {
        return oilLevel;
    }

    public Type getType() {
        return type;
    }

    public double getCurrVecValue(Direction direction) {
        return CurrVecValue[direction.ordinal()];
    }
}
