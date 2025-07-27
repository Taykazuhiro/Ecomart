package br.com.alura.ecomart.ecomart.Controller;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorizador")
public class CategorizadorDeProdutoController {
    private final ChatClient chatClient;

    public CategorizadorDeProdutoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public String categorizar(String produto) {
        var system = """
                Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado
                
                    Escolha uma categoria dentro da lista abaixo:
                    1. Higiene pessoal
                    2. Eletrônicos
                    3. Esportes
                    4. Outros
                
                    ###### exemplo de uso:
                
                    Pergunta: Bola de futebol
                    Resposta: Esportes
                """;

        var token = contarTokens(system, produto);
        System.out.println("Qtd de tokens: " + token);

        //fazer lógica para mudança dinâmica na escolha do modelo

        return this.chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(system)
                .user(produto)
                .options(ChatOptions.builder()
                        .temperature(0.50)
                        .maxTokens(50)
                        .model("gpt-4.1-mini")
                        .build())
                .call()
                .content();

    }

    private int contarTokens(String system, String user){
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_4O_MINI);
        return enc.countTokens(system + user);
    }
}
