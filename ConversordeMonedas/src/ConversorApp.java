import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Scanner;

public class ConversorApp {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/c9c5862b703112d014fa5797/latest/USD";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Menú de opciones:");
            System.out.println("1. Dólar => Peso argentino");
            System.out.println("2. Peso argentino => Dólar");
            System.out.println("3. Dólar => Real brasileño");
            System.out.println("4. Real brasileño => Dólar");
            System.out.println("5. Dólar => Peso colombiano");
            System.out.println("6. Peso colombiano => Dólar");
            System.out.println("7. Salir");
            System.out.print("Elija una opción válida: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    convertCurrency("USD", "ARS");
                    break;
                case 2:
                    convertCurrency("ARS", "USD");
                    break;
                case 3:
                    convertCurrency("USD", "BRL");
                    break;
                case 4:
                    convertCurrency("BRL", "USD");
                    break;
                case 5:
                    convertCurrency("USD", "COP");
                    break;
                case 6:
                    convertCurrency("COP", "USD");
                    break;
                case 7:
                    exit = true;
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción del 1 al 7.");
                    break;
            }
        }
        scanner.close();
    }

    private static void convertCurrency(String fromCurrency, String toCurrency) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            JsonObject jsonObject = JsonParser.parseString(result.toString()).getAsJsonObject();
            JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");
            double rate = conversionRates.get(toCurrency).getAsDouble();
            System.out.printf("La tasa de cambio de %s a %s es: %.2f%n", fromCurrency, toCurrency, rate);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese la cantidad que desea convertir: ");
            double amount = scanner.nextDouble();
            double convertedAmount = amount * rate;
            System.out.printf("%.2f %s equivalen a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Hubo un error al obtener los datos de la API.");
        }
    }
}
