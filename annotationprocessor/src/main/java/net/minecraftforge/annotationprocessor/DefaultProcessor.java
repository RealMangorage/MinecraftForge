package net.minecraftforge.annotationprocessor;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApiStatus.Internal
public abstract class DefaultProcessor implements Processor {

    private ProcessingEnvironment env;

    protected ProcessingEnvironment getEnv() {
        return env;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Set.of();
    }

    protected abstract Set<Class<? extends Annotation>> getSupportedAnnotations();

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver())
            getEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, getProcessorInfo());
        return processDefault(annotations, roundEnv);
    }

    public abstract boolean processDefault(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

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
        this.env = processingEnv;
    }

    @Override
    public List<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return List.of();
    }

    public String getProcessorInfo() {
        return "Annotation Processor: %s Version: %s".formatted(getId(), getVersion());
    }

    public abstract String getId();
    public abstract String getVersion();
}
