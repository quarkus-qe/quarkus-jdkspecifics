package io.quarkus.ts.jdk21.preview.records;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

/**
 * As this is only showcase value1 will be used for substitution
 *
 * @param value2 String value
 * @param value3 Data which will be different type
 */
@CheckedTemplate(requireTypeSafeExpressions = false)
public record UnnamedRecordQute(String value1, Object value2) implements TemplateInstance {
}
