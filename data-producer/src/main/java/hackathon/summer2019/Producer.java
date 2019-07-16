package hackathon.summer2019;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Producer {

  private static final Logger LOGGER = Logger.getLogger(Producer.class.getSimpleName());

  private static final List<String> GRPC_HOSTS = Lists.newArrayList("localhost");

  private static final int GRCP_PORT = 50051;

  public static void main(String[] args) throws Exception {

    Faker faker = new Faker();

    List<GrpcClient> clients = GRPC_HOSTS.stream()
        .map(host -> new GrpcClient(host, GRCP_PORT))
        .collect(Collectors.toList());

    while (true) {
      String data = faker.chuckNorris().fact();
      LOGGER.info("sending data: " + data);
      clients.forEach(client -> client.sendData(data));
    }
  }
}
