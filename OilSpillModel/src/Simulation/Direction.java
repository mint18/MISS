package Simulation;

public enum Direction {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    public static Direction stringToDirection(String directionText) {
        switch (directionText) {
            case "N":
                return Direction.N;
            case "NE":
                return Direction.NE;
            case "E":
                return Direction.E;
            case "SE":
                return Direction.SE;
            case "S":
                return Direction.S;
            case "SW":
                return Direction.SW;
            case "W":
                return Direction.W;
            case "NW":
                return Direction.NW;
            default:
                return null;
        }
    }
}
