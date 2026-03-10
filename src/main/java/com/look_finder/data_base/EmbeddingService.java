package com.look_finder.data_base;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class EmbeddingService {

    public static final String MODEL_ID = "sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2";
    public static final int DIM = 384;

    private final ZooModel<String, float[]> model;
    private final Predictor<String, float[]> predictor;

    public EmbeddingService() throws ModelNotFoundException, IOException, URISyntaxException, MalformedModelException {
        Criteria<String, float[]> criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optModelUrls("djl://ai.djl.huggingface.pytorch/" + MODEL_ID)
                .build();

        this.model = criteria.loadModel();
        this.predictor = model.newPredictor();
    }

    public EmbeddingResult embed(EmbedingDTO dto) throws TranslateException {
        String text = buildEmbeddingText(dto);
        float[] vector = predictor.predict(text);

        l2NormalizeInPlace(vector);

        if(vector.length != DIM){
            throw new IllegalStateException("Unexpected vector length: " + vector.length);
        }

        return new EmbeddingResult(vector, text);
    }

    private void l2NormalizeInPlace(float[] vector) {
        double sum = 0.0;
        for (float f : vector) sum += (double) f * f;
        double normalizedSum = Math.sqrt(sum);
        if (normalizedSum < 1e-12) return;
        for (int i = 0; i < vector.length; i++) vector[i] = (float) (vector[i] / normalizedSum);
    }

    @PreDestroy
    public void close() {
        predictor.close();
        model.close();
    }

    private String buildEmbeddingText(EmbedingDTO dto) {
        return String.join(" | ",
                get(dto.name()),
                "origin: " + get(dto.origin()),
                "color: " + get(dto.color()),
                "size: " + get(dto.size()),
                "sex: " + get(dto.sex()),
                "category: " + get(dto.category())
        ).trim();
    }

    private String get(String s) {
        return s == null ? "" : s;
    }

    public record EmbeddingResult(float[] vector, String embeddingText) {}
}
