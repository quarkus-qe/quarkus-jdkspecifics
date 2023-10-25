package io.quarkus.ts.jdk21.preview.records;

import io.quarkus.qute.TemplateInstance;

public record WebTemplatePreview(String headerOne, String customText) implements TemplateInstance {
}
