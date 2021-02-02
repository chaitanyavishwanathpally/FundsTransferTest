package com.rest.api.persistance;


import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

public class ExtendedClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider
{
  protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
  private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
  private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

  public ExtendedClassPathScanningCandidateComponentProvider(boolean useDefaultFilters)
  {
    super(useDefaultFilters);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public Set<BeanDefinition> findCandidateComponents(String basePackage)
  {
    Set candidates = new LinkedHashSet();
    try {
      String packageSearchPath = "classpath*:" + 
        ClassUtils.convertClassNameToResourcePath(basePackage) + "/" + "**/*.class";

      Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
      for (int i = 0; i < resources.length; i++) {
        Resource resource = resources[i];
        MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);

        if (isCandidateComponent(metadataReader.getAnnotationMetadata().getAnnotationTypes())) {
          ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
          sbd.setSource(resource);
          if (isCandidateComponent(sbd))
            candidates.add(sbd);
        }
      }
    }
    catch (IOException ex)
    {
      throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
    }
    return candidates;
  }

  private boolean isCandidateComponent(Set<String> annotationTypes) {
    for (String type : annotationTypes) {
      if ("javax.persistence.Entity".equals(type)) {
        return true;
      }

      if ("javax.persistence.Embeddable".equals(type)) {
        return true;
      }
    }
    return false;
  }
}