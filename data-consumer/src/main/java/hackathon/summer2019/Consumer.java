package hackathon.summer2019;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

public class Consumer {

  // https://github.com/prometheus/client_java
  private static final Counter requests = Counter.build()
      .name("requests_total").help("Total requests.").register();

  public static void main(String[] args) throws Exception {

    

    HTTPServer server = new HTTPServer(9999);
  }

}
