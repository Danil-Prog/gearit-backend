package com.gearit.api.utils.resourceloader;

import org.springframework.core.io.*;
import org.springframework.stereotype.*;

@Component
public class ResourceLoaderUtils {

    private final ResourceLoader resourceLoader;


    public ResourceLoaderUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Возвращает ресурс из `resources`, иначе null
     *
     * @param filePath - имя искомого ресурса
     * @return - Resource
     */
    public Resource getResourceByFilename(String filePath) {
        return resourceLoader
                .getResource(filePath)
                .exists() ? resourceLoader.getResource(filePath) : null;
    }
}
