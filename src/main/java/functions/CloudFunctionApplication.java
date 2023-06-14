package functions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Function;

@SpringBootApplication
public class CloudFunctionApplication {
  private static final int DEFAULT_FACTORIAL = 10;
  private static final int MAX_FACTORIAL = 1000;
  private static final String FACTORIAL_HEADER_STRING = "factorial";

  public static void main(String[] args) {
    SpringApplication.run(CloudFunctionApplication.class, args);
  }

  @Bean
  public Function<Message<String>, String> echo() {
    System.out.println("Triggered function!");
    
    return (inputMessage) -> {
      int factorial = DEFAULT_FACTORIAL;
      
      if (inputMessage.getHeaders().containsKey(FACTORIAL_HEADER_STRING)) {
        var factorialObject = inputMessage.getHeaders().get(FACTORIAL_HEADER_STRING);
        if (factorialObject != null) {
          try {
            factorial = Integer.parseInt(factorialObject.toString());
          } catch (Exception e) {
            System.err.println("Error parsing factorial: " + e.getMessage());
          }
        }
      }
      
      if (factorial > MAX_FACTORIAL) factorial = MAX_FACTORIAL;
      if (factorial < 1) factorial = 1;
      
      
      var result = this.calcFactorial(factorial);
      
      return String.format("Factorial of [%s] is [%s]", factorial, result);
    };
  }

  private long calcFactorial(int factorial) {
    long resultCalculation = 1;
    long currentValue = factorial;
    
    while (currentValue > 1) {
      resultCalculation = resultCalculation * currentValue;

      currentValue -= 1;
    }

    System.out.println("Calculated: " + resultCalculation);
    return resultCalculation;
  }
  
  /*@Bean
  public Function<Message<String>, String> echo() {
    return (inputMessage) -> {

      var stringBuilder = new StringBuilder();
      inputMessage.getHeaders()
        .forEach((key, value) -> {
          stringBuilder.append(key).append(": ").append(value).append(" ");
        });

      var payload = inputMessage.getPayload();

      if (!payload.isBlank()) {
        stringBuilder.append("echo: ").append(payload);
      }

      return stringBuilder.toString();
    };
  }*/
}
