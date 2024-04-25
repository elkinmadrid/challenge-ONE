package co.elkinmadrid;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        JsonObject conversion_rates = requestAPI();
        int option;
        do {
            System.out.println("""
                    *********************************************
                    *********************************************
                                        
                    1- Dolar => Peso argentino
                    2- Peso argentino => Dolar
                    3- Dolar => Real brasileño
                    4- Real brasileño => Dolar
                    5- Dolar => Peso Colombiano
                    6- Peso Colombiano => Dolar
                    7- Salir
                                        
                    Digite una opción valida:
                    *********************************************
                    *********************************************
                    """);

            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
            double amount = 0;
            if (option >= 1 && option <= 6) {
                System.out.println("Ingrese la cantidad que desea convertir: ");
                amount = scanner.nextDouble();
            }

            double currencyExchange = switch (option) {

                case 1 -> {
                    System.out.println("Dolar a peso argentino");
                    yield converterCurrency("USD", "ARS", conversion_rates, amount);
                }
                case 2 -> {
                    System.out.println("Peso argentino a Dolar");
                    yield converterCurrency("ARS", "USD", conversion_rates, amount);
                }
                case 3 -> {
                    System.out.println("Dolar a real brasileño");
                    yield converterCurrency("USD", "BRL", conversion_rates, amount);
                }
                case 4 -> {
                    System.out.println("Real brasileño a Dolar");
                    yield converterCurrency("BRL", "USD", conversion_rates, amount);
                }
                case 5 -> {
                    System.out.println("Dolar a Peso Colombiano");
                    yield converterCurrency("USD", "COP", conversion_rates, amount);
                }
                case 6 -> {
                    System.out.println("Peso Colombiano a Dolar");
                    yield converterCurrency("COP", "USD", conversion_rates, amount);
                }
                case 7 -> {
                    System.out.println("Saliendo del programa");
                    yield 0;
                }
                default -> {
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                    yield 0;
                }
            };
            if (option != 7) {
                System.out.println("El valor de " + amount + " corresponde al valor de => " + currencyExchange);
            }
        } while (option != 7);


    }

    static double converterCurrency(String to, String from, JsonObject conversion_rates, double amount) {
        double tasaOrigen = conversion_rates.get(to).getAsDouble();
        double tasaDestino = conversion_rates.get(from).getAsDouble();
        double cantidadEnOrigen = amount / tasaOrigen;
        return cantidadEnOrigen * tasaDestino;
    }

    static JsonObject requestAPI() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v6.exchangerate-api.com/v6/fffa750de38bc301f2675101/latest/USD"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject responseParse = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseParse.get("conversion_rates").getAsJsonObject();
    }
}