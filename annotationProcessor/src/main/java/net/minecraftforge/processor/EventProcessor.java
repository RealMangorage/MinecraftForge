/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.processor;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@ApiStatus.Internal
public class EventProcessor implements Processor {
    private static final Set<String> SUBSCRIBE_TYPE = Set.of(SubscribeEvent.class.getName());

    private ProcessingEnvironment env;

    ProcessingEnvironment getEnv() {
        return env;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Set.of();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUBSCRIBE_TYPE;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        this.env = processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        getEnv().getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                "TEST"
        );
        roundEnv.getElementsAnnotatedWith(SubscribeEvent.class).forEach(e -> {
            var subscriber = e.getEnclosingElement().getAnnotation(Mod.EventBusSubscriber.class);
            if (subscriber != null) {
//                e.getEnclosingElement().getEnclosedElements()
//                        .stream()
//                        .filter(e2 -> e2.getKind() == ElementKind.METHOD)
//                        .forEach();
            }
        });
        return false;
    }

    @Override
    public List<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return List.of();
    }
}
