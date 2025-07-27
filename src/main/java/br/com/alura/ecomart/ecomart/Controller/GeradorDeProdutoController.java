package br.com.alura.ecomart.ecomart.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gerador")
public class GeradorDeProdutoController {
    private final ChatClient chatClient;

    //É possível configurar os parâmetros padrão no construtor do chat client
    public GeradorDeProdutoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptions.builder()
                        .model("gpt-4.1-mini")
                        .build())
                .build();
    }

    @GetMapping
    public String gerarProdutos() {
        var pergunta = "Gere 5 produtos ecológicos";

        return this.chatClient.prompt()
                .user(pergunta)
                .call()
                .content();

    }
}
