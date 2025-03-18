package com.zenika.genai.codelab;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.V;

import java.util.List;
import java.util.Scanner;

interface Translator {
    @SystemMessage("Direct translation from English to Japanese, using polite form.")
    TokenStream englishToJapanese(@V("userInput") String userInput);
}

public class ChatBot {

    private static final String OLLAMA_URL = "http://localhost:11434";
    private static final String MODEL = "hf.co/Tonic/GemmaX2-28-2B-gguf:latest";

    public static void main(String[] args) {
        System.out.println("Init LLM");

        StreamingChatLanguageModel llm = OllamaStreamingChatModel.builder()
                .baseUrl(OLLAMA_URL)
                .modelName(MODEL)
                .temperature(0.1)
                .build();

        System.out.println("Enter your sentence to translate");

        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();

        Translator assistant = AiServices.create(Translator.class, llm);
        TokenStream tokenStream = assistant.englishToJapanese(userInput);

        tokenStream
                .onNext(System.out::print)
                .onError((Throwable error) -> error.printStackTrace())
                .start();
    }

}
