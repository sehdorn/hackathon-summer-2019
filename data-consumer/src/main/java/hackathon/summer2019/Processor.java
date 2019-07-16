package hackathon.summer2019;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;
import java.math.BigInteger;
import org.omg.CORBA.DATA_CONVERSION;

import io.prometheus.client.Gauge;
import java.math.BigInteger;

public class Processor {

  // https://github.com/prometheus/client_java
  private static final Gauge requests = Gauge.build()
      .name("requests_total").help("Total requests.").register();
  private static final Summary objectify_String_Duration = Summary.build()
           .name("objectify_String_seconds").help("Duration of objectify_String in seconds.").register();

  public void processData(String data) {

    Summary.Timer requestTimer = objectify_String_Duration.startTimer();
    try{
        objectify_String(data);
    } finally {
      requestTimer.observeDuration();
    }


  }

  private void objectify_String(String data){
    int count = 0;
    for (Character a : data.toCharArray()){
      BigInteger bigNumber = new BigInteger(String.valueOf((int)a.charValue()));
      bigNumber.multiply((bigNumber != null) ? bigNumber : new BigInteger(new byte[]{01}));
    }
    increase_gauge();
  }

  private void increase_gauge(){
    requests.inc();
  }

  private Gauge get_gauge(){
    return requests;
  }
}
