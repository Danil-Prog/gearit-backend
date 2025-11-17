package com.gearit.api.service.notification;

import com.gearit.api.entity.notification.NotificationTemplate;
import com.gearit.api.utils.resourceloader.ResourceLoaderUtils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Подгружает в память список шаблонов, для уведомлений
 */
@Service
public class NotificationTemplateEngine {

    private final Map<NotificationTemplate, String> templates;
    private final ResourceLoaderUtils resourceLoader;

    private final Logger logger = LoggerFactory.getLogger(NotificationTemplateEngine.class);

    @Autowired
    public NotificationTemplateEngine(ResourceLoaderUtils resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.templates = new HashMap<>();
    }

    @PostConstruct
    private void getTemplates() {
        logger.info("Start initialize HTML body for notification from resource");

        Arrays.stream(NotificationTemplate.values()).forEach(template -> {
            String htmlName = template.getFilePath();
            try {
                String htmlBody = loadHtmlFromResource(htmlName);
                templates.put(template, htmlBody);
            } catch (IOException e) {
                logger.error("Resource with filename: {}, not found from resources directory", htmlName);
            }
        });
    }

    public String renderTemplate(NotificationTemplate notificationTemplate, Map<String, Object> variables) {
        String result = getTemplateByType(notificationTemplate);
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{ " + entry.getKey() + " }}";
            result = result.replace(placeholder, (String) entry.getValue());
        }

        return result;
    }

    private String getTemplateByType(NotificationTemplate notificationTemplate) {
        return templates.get(notificationTemplate);
    }

    private String loadHtmlFromResource(String htmlName) throws IOException {
        Resource resource = resourceLoader.getResourceByFilename(htmlName);
        return Files.readString(resource.getFile().toPath());
    }
}
