import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {

    static Map<String, Double> taxasCambio;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        obterTaxasCambio();

        int opcao;
        do {
            System.out.println("\n*** Conversor de Moedas ***");
            System.out.println("Escolha a conversão:");
            System.out.println("1. BRL -> USD");
            System.out.println("2. USD -> BRL");
            System.out.println("3. BRL -> ARS");
            System.out.println("4. ARS -> BRL");
            System.out.println("5. BRL -> CLP");
            System.out.println("6. CLP -> BRL");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();

            if (opcao != 0) {
                System.out.print("Digite o valor: ");
                double valor = scanner.nextDouble();
                realizarConversao(opcao, valor);
            }

        } while (opcao != 0);

        scanner.close();
        System.out.println("Programa finalizado!");
    }

    public static void obterTaxasCambio() throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/5811ea9e1fe55b86b93e6b91/latest/BRL";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

        JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");


        taxasCambio = gson.fromJson(rates, Map.class);

        System.out.println("Taxas de câmbio carregadas com sucesso!");
    }

    public static void realizarConversao(int opcao, double valor) {
        double resultado = 0;

        if (taxasCambio == null || taxasCambio.isEmpty()) {
            System.out.println("Erro: As taxas de câmbio não foram carregadas.");
            return;
        }

        switch (opcao) {
            case 1:
                resultado = valor * taxasCambio.get("USD");
                System.out.printf("%.2f BRL = %.2f USD\n", valor, resultado);
                break;
            case 2:
                resultado = valor / taxasCambio.get("USD");
                System.out.printf("%.2f USD = %.2f BRL\n", valor, resultado);
                break;
            case 3:
                resultado = valor * taxasCambio.get("ARS");
                System.out.printf("%.2f BRL = %.2f ARS\n", valor, resultado);
                break;
            case 4:
                resultado = valor / taxasCambio.get("ARS");
                System.out.printf("%.2f ARS = %.2f BRL\n", valor, resultado);
                break;
            case 5:
                resultado = valor * taxasCambio.get("CLP");
                System.out.printf("%.2f BRL = %.2f CLP\n", valor, resultado);
                break;
            case 6:
                resultado = valor / taxasCambio.get("CLP");
                System.out.printf("%.2f CLP = %.2f BRL\n", valor, resultado);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }
}
