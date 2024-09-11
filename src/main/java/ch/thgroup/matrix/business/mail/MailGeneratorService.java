package ch.thgroup.matrix.business.mail;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MailGeneratorService {
    static final String PROPERTY_PREFIX = "common.mail.template.";

    @EqualsAndHashCode
    @AllArgsConstructor
    static class TemplateId {

        MailType name;

        Language lang;
    }

    @AllArgsConstructor
    static class Template {

        String subject;

        String body;
    }

    final Environment env;

    final Map<TemplateId, Template> templates;

    public MailGeneratorService(Environment env) {
        this.env = env;
        this.templates = Collections.unmodifiableMap(allTemplates(env));
    }

    private static Map<TemplateId, Template> allTemplates(Environment env) {
        final Map<TemplateId, Template> templates = new HashMap<>();

        for (MailType type : MailType.values()) {
            for (Language language : Language.values()) {
                final String templateName = type.getTemplateName();

                final String subjectTemplateKey = PROPERTY_PREFIX + templateName + "." + language + ".subject";
                final String bodyTemplateKey = PROPERTY_PREFIX + templateName + "." + language + ".body";

                String subjectTemplate = env.getProperty(subjectTemplateKey);
                if (subjectTemplate == null) {
                    throw new MissingTemplateException("Missing email template subject: " + subjectTemplateKey);
                }
                String bodyTemplate = env.getProperty(bodyTemplateKey);
                if (bodyTemplate == null) {
                    throw new MissingTemplateException("Missing email template body: " + bodyTemplateKey);
                }

                templates.put(new TemplateId(type, language), new Template(subjectTemplate, bodyTemplate));

            }
        }

        return templates;
    }

    public SimpleMailMessage generatePartial(MailType type, Language language, Map<String, String> params) {
        final TemplateId key = new TemplateId(type, language);
        final Template template = templates.get(key);
        final SimpleMailMessage message = new SimpleMailMessage();
        final StringSubstitutor sub = new StringSubstitutor(params, "{", "}");
        sub.setDisableSubstitutionInValues(true);
        sub.setEnableUndefinedVariableException(true);

        try {
            message.setSubject(sub.replace(template.subject));
            message.setText(sub.replace(template.body));
        }
        catch (IllegalArgumentException e) {
            throw new InvalidTemplateParametersException(
                    String.format("Could not generate mail of type %s for %s", type, language), e);
        }

        return message;
    }
}
