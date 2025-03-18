package com.zenika.genai.codelab;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ChatBot {

    private static final String OLLAMA_URL = "http://localhost:11434";
    private static final String MODEL = "7shi/llama-translate:8b-q4_K_M";

    public static void main(String[] args) {
        System.out.println("Init LLM");
        OllamaStreamingChatModel llm = new OllamaStreamingChatModel.OllamaStreamingChatModelBuilder()
                .baseUrl(OLLAMA_URL)
                .modelName(MODEL)
                .temperature(0.3)
                .build();

        System.out.println("Enter your sentence to translate");

        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();

        System.out.println("Prompt");
        PromptTemplate template = PromptTemplate
                .from("Translate from English to Japanese this sentence: {{userInput}}");

        UserMessage prompt = template.apply(Map.of("userInput", userInput))
                .toUserMessage();

        llm.generate(List.of(prompt), new StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String s) {
                System.out.print(s);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                System.out.println(" =>DONE");
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

}
