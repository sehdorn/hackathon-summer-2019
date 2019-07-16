package hackathon.summer2019;

import com.github.javafaker.Faker;

public class Producer {

  public static void main(String[] args) {

    Faker faker = new Faker();
    //System.out.println(faker.book().author());
    System.out.println(faker.beer().hop());
  }
}
