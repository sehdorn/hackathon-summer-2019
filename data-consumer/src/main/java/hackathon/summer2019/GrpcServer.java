package hackathon.summer2019;

import hackathon.summer2019.grpc.DataRequest;
import hackathon.summer2019.grpc.DataResponse;
import hackathon.summer2019.grpc.DataServiceGrpc;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.prometheus.client.Counter;
import java.io.IOException;
import java.util.logging.Logger;

public class GrpcServer {

  private static final Logger LOGGER = Logger.getLogger(GrpcServer.class.getName());

  private static final Counter REQUESTS = Counter.build()
      .name("requests_total").help("Total requests.").register();

  public GrpcServer(int port, Processor processor) throws IOException {
    ServerBuilder.forPort(port)
        .addService(new DataServiceImpl(processor))
        .build()
        .start();
    LOGGER.info("Grpc server started, listening on " + port);
  }

  static class DataServiceImpl extends DataServiceGrpc.DataServiceImplBase {

    private final Processor processor;

    public DataServiceImpl(Processor processor) {
      this.processor = processor;
    }

    @Override
    public void sendData(DataRequest request, StreamObserver<DataResponse> responseObserver) {
      processor.processData(request.getData());
      DataResponse response = DataResponse.newBuilder().setMessage("done").build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      REQUESTS.inc();
    }
  }
}
