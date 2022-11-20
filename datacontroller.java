
package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListenerWithExceptions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.io.OutputStream;

import java.nio.ByteBuffer;

public class dataController implements SerialPortMessageListenerWithExceptions {
    private static final byte[] DELIMITER = new byte[]{'\n'};
    private final ObservableList<XYChart.Data<Number, Number>> dataPoints;

    private final OutputStream outputStream;

    public dataController(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.dataPoints = FXCollections.observableArrayList();
    }

    public ObservableList<XYChart.Data<Number, Number>> getDataPoints() {
        return dataPoints;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }



    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

        if(serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED){
            return;
        }

        var data = serialPortEvent.getReceivedData();


        Number dataInt = ByteBuffer.wrap(data).getInt();

        int newData = (int)dataInt;



        Number time = System.currentTimeMillis();

        var dataPoint = new XYChart.Data<>(time, dataInt);

        Platform.runLater(()-> this.dataPoints.add(dataPoint));



    }


    @Override
    public void catchException(Exception e){ e.printStackTrace();}



    @Override
    public byte[] getMessageDelimiter(){ return DELIMITER;}



    @Override
    public boolean delimiterIndicatesEndOfMessage(){return true;}
}


