CREATE EXTENSION IF NOT EXISTS vector;

ALTER TABLE current_available_products
    ADD COLUMN IF NOT EXISTS embedding vector(384),
    ADD COLUMN IF NOT EXISTS embedding_text text;

CREATE INDEX IF NOT EXISTS cap_embedding_hnsw
    ON current_available_products
        USING hnsw (embedding vector_cosine_ops);