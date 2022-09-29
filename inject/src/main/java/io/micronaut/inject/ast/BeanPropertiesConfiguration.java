package io.micronaut.inject.ast;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.AccessorsStyle;
import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.BeanProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class BeanPropertiesConfiguration {

    private BeanProperties.Visibility visibility = BeanProperties.Visibility.DEFAULT;
    private EnumSet<BeanProperties.AccessKind> accessKinds = EnumSet.of(BeanProperties.AccessKind.METHOD);
    private Set<String> includes = Collections.emptySet();
    private Set<String> excludes = Collections.emptySet();
    private String[] readPrefixes = new String[]{AccessorsStyle.DEFAULT_READ_PREFIX};
    private String[] writePrefixes = new String[]{AccessorsStyle.DEFAULT_WRITE_PREFIX};
    private boolean allowSetterWithZeroArgs;
    private boolean allowSetterWithMultipleArgs;
    private boolean allowStaticProperties;

    public static BeanPropertiesConfiguration of(AnnotationMetadata annotationMetadata) {
        BeanPropertiesConfiguration conf = new BeanPropertiesConfiguration();
        Set<String> includes = new HashSet<>();
        Set<String> excludes = new HashSet<>();

        AnnotationValue<BeanProperties> annotation = annotationMetadata.getAnnotation(BeanProperties.class);
        if (annotation != null) {
            annotation.enumValue(BeanProperties.VISIBILITY, BeanProperties.Visibility.class)
                .ifPresent(conf::setVisibility);
            if (annotation.isPresent(BeanProperties.ACCESS_KIND)) {
                conf.setAccessKinds(
                    annotation.enumValuesSet(BeanProperties.ACCESS_KIND, BeanProperties.AccessKind.class)
                );
            }
            annotation.booleanValue(BeanProperties.ALLOW_WRITE_WITH_ZERO_ARGS)
                .ifPresent(conf::setAllowSetterWithZeroArgs);
            annotation.booleanValue(BeanProperties.ALLOW_WRITE_WITH_MULTIPLE_ARGS)
                .ifPresent(conf::setAllowSetterWithMultipleArgs);

            includes.addAll(Arrays.asList(annotation.stringValues(BeanProperties.INCLUDES)));
            excludes.addAll(Arrays.asList(annotation.stringValues(BeanProperties.EXCLUDES)));
        }

        // TODO: investigate why aliases aren't propagated
        includes.addAll(Arrays.asList(annotationMetadata.stringValues(ConfigurationProperties.class, BeanProperties.INCLUDES)));
        excludes.addAll(Arrays.asList(annotationMetadata.stringValues(ConfigurationProperties.class, BeanProperties.EXCLUDES)));

        conf.setIncludes(includes);
        conf.setExcludes(excludes);

        annotationMetadata.getValue(AccessorsStyle.class, "readPrefixes", String[].class)
            .ifPresent(conf::setReadPrefixes);
        annotationMetadata.getValue(AccessorsStyle.class, "writePrefixes", String[].class)
            .ifPresent(conf::setWritePrefixes);

        return conf;
    }

    public BeanProperties.Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(BeanProperties.Visibility visibility) {
        this.visibility = visibility;
    }

    public EnumSet<BeanProperties.AccessKind> getAccessKinds() {
        return accessKinds;
    }

    public void setAccessKinds(EnumSet<BeanProperties.AccessKind> accessKinds) {
        this.accessKinds = accessKinds;
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public void setIncludes(Set<String> includes) {
        this.includes = includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(Set<String> excludes) {
        this.excludes = excludes;
    }

    public String[] getReadPrefixes() {
        return readPrefixes;
    }

    public void setReadPrefixes(String[] readPrefixes) {
        this.readPrefixes = readPrefixes;
    }

    public String[] getWritePrefixes() {
        return writePrefixes;
    }

    public void setWritePrefixes(String[] writePrefixes) {
        this.writePrefixes = writePrefixes;
    }

    public boolean isAllowSetterWithZeroArgs() {
        return allowSetterWithZeroArgs;
    }

    public void setAllowSetterWithZeroArgs(boolean allowSetterWithZeroArgs) {
        this.allowSetterWithZeroArgs = allowSetterWithZeroArgs;
    }

    public boolean isAllowSetterWithMultipleArgs() {
        return allowSetterWithMultipleArgs;
    }

    public void setAllowSetterWithMultipleArgs(boolean allowSetterWithMultipleArgs) {
        this.allowSetterWithMultipleArgs = allowSetterWithMultipleArgs;
    }

    public boolean isAllowStaticProperties() {
        return allowStaticProperties;
    }

    public void setAllowStaticProperties(boolean allowStaticProperties) {
        this.allowStaticProperties = allowStaticProperties;
    }
}
