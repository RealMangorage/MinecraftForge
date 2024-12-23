package net.minecraftforge.processor;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class DefaultProcessor implements Processor {

    private ProcessingEnvironment env;

    protected ProcessingEnvironment getEnv() {
        return env;
    }

    @Override
    public Set<String> getSupportedOptions() {
        System.out.println("Get Options");
        return Set.of();
    }

    protected abstract Set<Class<? extends Annotation>> getSupportedAnnotations();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        System.out.println("Get Types");
        return getSupportedAnnotations()
                .stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        System.out.println("INIT AP");
        this.env = processingEnv;
    }

    @Override
    public List<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        System.out.println("Get Completions");
        return List.of();
    }
}
