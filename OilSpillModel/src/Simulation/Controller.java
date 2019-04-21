package Simulation;

import Simulation.Tint;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class Controller {

    Area area;
    boolean _run;
    LinkedList<Double> windSpeedList;
    LinkedList<Double> currentSpeedList;
    LinkedList<Double> temperatureList;
    LinkedList<String> currentDirectionList;
    LinkedList<String> windDirectionList;
    int step=0;


    @FXML
    private GridPane map;
    @FXML
    private Button loadButton;
    @FXML
    private Button startButton;
    @FXML
    private Button stateButton;
    @FXML
    private Label positionText;
    @FXML
    private Label oilEnd;



    public void initialize() {
        this.area = new Area(300);
        createGrid(this.area);
        positionText.setVisible(false);
        this.startButton.setDisable(true);
    }

    public void setParams() {
        this.area.setSimulationData(_getWindDirection(step), _getWindSpeed(step), _getCurrentDirection(step), _getCurrentSpeed(step), _getTemperature(step));
        this.area.genWindVectors();
    }

    public double _getWindSpeed(int _step){
        return windSpeedList.get(_step);
    }
    public double _getCurrentSpeed(int _step){
        return currentSpeedList.get(_step);
    }
    public double _getTemperature(int _step){
        return temperatureList.get(_step);
    }
    public String _getWindDirection(int _step){
        return windDirectionList.get(_step);
    }
    public String _getCurrentDirection(int _step){
        return currentDirectionList.get(_step);
    }

    @FXML
    void loadSimulation(ActionEvent e) {
        this.windSpeedList = DataFromFile.loadValues("./src/Files/WindSpeed.txt");
        this.currentSpeedList = DataFromFile.loadValues("./src/Files/CurrentSpeed.txt");
        this.temperatureList = DataFromFile.loadValues("./src/Files/Temperature.txt");
        this.currentDirectionList = DataFromFile.loadDirections("./src/Files/CurrentDirection.txt");
        this.windDirectionList = DataFromFile.loadDirections("./src/Files/WindDirection.txt");

        setParams();
        this.step+=1;

        if (this.area.sourceX == -1) {
            this.area.genSource();
        }
        printGrid(this.area);

        this.loadButton.setDisable(true);
        this.stateButton.setDisable(false);
        this.startButton.setDisable(false);
    }

    @FXML
    void startSimulation(ActionEvent event) {

        setParams();

        this.startButton.setDisable(true);
        this.stateButton.setDisable(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                _run=true;
                iterate();

            }
        }).start();

    }

    public void iterate() {
        setParams();
        this.step+=1;
        try {
            sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                area.checkOilLevel();
                showOilCount();
                printGrid(area);

                if (_run==true && step<windSpeedList.size() && step<currentSpeedList.size() && step<temperatureList.size() && step<currentDirectionList.size() && step<windDirectionList.size()) {
                    iterate();
                } else {
                    startButton.setDisable(false);
                }
            }
        });
    }

    @FXML
    void resetSimulation(ActionEvent event) {
        this.area = new Area(300);
        printGrid(area);
        resetStatisctics();
        this.step=0;

        this.loadButton.setDisable(false);
        this.startButton.setDisable(true);
    }

    @FXML
    void pauseSimulation(ActionEvent event) {
        this._run=false;
        this.stateButton.setDisable(false);
    }

    @FXML
    void nextState(ActionEvent event) {
        this._run=false;
        iterate();
    }


    public void createGrid(Area area) {
        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                map.add(new Tint(area.getCell(i, j).getOilLevel(), area.getCell(i, j).getType(), j, i), j, i);
            }
        }
    }


    public void printGrid(Area area) {
        ObservableList<Node> childrens = map.getChildren();
        for (Node node : childrens) {
            Tint tint = (Tint) node;
            tint.setFill(area.getCell(tint.y, tint.x).getOilLevel(), area.getCell(tint.y, tint.x).getType());
        }
    }


    public void showOilCount(){
        int oilCount = Area.getOilCount(area);
        oilEnd.setText(" "+(oilCount));
    }

    public void resetStatisctics(){
        oilEnd.setText("");
    }
}
