package hackathon.summer2019;

import io.prometheus.client.exporter.HTTPServer;
import java.util.logging.Logger;

public class Consumer {

  private static final Logger LOGGER = Logger.getLogger(Consumer.class.getName());

  private static final int GRPC_PORT = 50051;

  private static final int PROMETHEUS_PORT = 9999;

  public static void main(String[] args) throws Exception {
    GrpcServer grpcServer = new GrpcServer(GRPC_PORT, new Processor());
    LOGGER.info("prometheus server started, listening on " + PROMETHEUS_PORT);
    HTTPServer prometheusServer = new HTTPServer(PROMETHEUS_PORT);
  }

}
