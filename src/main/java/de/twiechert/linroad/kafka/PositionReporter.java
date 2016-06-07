package de.twiechert.linroad.kafka;

import de.twiechert.linroad.jdriver.DataDriverLibrary;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

public class PositionReporter {

    public enum Key   {
        Type, Time, VID
    }

    public static enum Value   {
        Spd, XWay, Lane, Dir, Seg, Pos
    }

    public static final String TOPIC = "position_report";

    /**
     * This callback processes tuples generated from the native c implementation.
     */
    public static class PositionTupleReceivedCallback implements DataDriverLibrary.TupleReceivedCallback {

        private final Producer<StrTuple, StrTuple> producer;

        public PositionTupleReceivedCallback(Producer<StrTuple, StrTuple> producer) {
            this.producer = producer;
        }

        public void invoke(String s) {
            String[] tuple = s.split(",");
            // in this implementation, the first three elements represent the key, as they have identifying character
            // (Type = 0, Time, VID, Spd, XWay, Lane, Dir, Seg, Pos) key -> Type = 0, Time, VID,  | value -> Spd, XWay, Lane, Dir, Seg, Pos
            StrTuple key = new StrTuple(Arrays.copyOfRange(tuple, 0, 3));
            StrTuple value =  new StrTuple(Arrays.copyOfRange(tuple, 3, 9));
            producer.send(new ProducerRecord<StrTuple, StrTuple>(TOPIC, key, value));
        }
    }


    private final DataDriverLibrary dataDriverLibrary;

    private final String filePath;

    private final Properties properties;

    public PositionReporter(DataDriverLibrary dataDriverLibrary, String datPath, Properties properties) {
        this.dataDriverLibrary = dataDriverLibrary;
        this.filePath = datPath;
        this.properties = properties;
    }

    public void startPositionReport() {
        Producer<StrTuple, StrTuple> producer = new KafkaProducer<StrTuple, StrTuple>(properties);

        this.dataDriverLibrary.startProgram(filePath, new PositionTupleReceivedCallback(producer));
        producer.close();
    }
}
