package hackathon.summer2019;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Gauge;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;
import java.nio.charset.StandardCharsets;

import java.math.BigInteger;
import java.util.logging.Logger;

public class Processor {

  private static final Logger LOGGER = Logger.getLogger(Processor.class.getSimpleName());

  // https://github.com/prometheus/client_java
  private static final Gauge requests = Gauge.build()
      .name("requests_total").help("Total requests.").register();
  private static final Summary objectify_String_Duration = Summary.build()
           .name("objectify_String_seconds").help("Duration of objectify_String in seconds.").register();

  private static final Summary useAES_Duration = Summary.build()
            .name("useAES_seconds").help("Duration of useAES in seconds.").register();

  public void processData(String data) {

    Summary.Timer requestTimer = objectify_String_Duration.startTimer();
    try{
        objectify_String(data);
    } finally {
      requestTimer.observeDuration();
      increase_gauge();
    }

    Summary.Timer aesTimer = useAES_Duration.startTimer();
    try{
        useAES(data);
    } catch (Exception e) {
      LOGGER.info("Encryption failed: " + e.getMessage());
    } finally {
      aesTimer.observeDuration();
      increase_gauge();
    }


  }

  private void useAES(String data) throws Exception{
    Cryptor cryptor = new Cryptor();
    byte[] iv = cryptor.getIV();
    byte[] key = cryptor.getKey();
    byte[] encoded = cryptor.encrypt(data.getBytes("UTF-8"), new String(key, "UTF-8"));
    byte[] decoded = cryptor.decrypt(encoded, new String(key, "UTF-8"));
    if (data.equals(new String(decoded))) {
      //LOGGER.info("successfully using crypto");
    } else {
      //LOGGER.info("crypto is not working");
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
