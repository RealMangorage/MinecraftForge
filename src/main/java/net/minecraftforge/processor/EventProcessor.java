/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.processor;

import com.sun.source.tree.ModifiersTree;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ApiStatus.Internal
public class EventProcessor extends DefaultProcessor {
    private static final String TARGET_INTERFACE = IModBusEvent.class.getName();

    @Override
    protected Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Set.of(
                Mod.EventBusSubscriber.class
        );
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        getEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, "lol test");

        roundEnv.getElementsAnnotatedWith(SubscribeEvent.class)
                .forEach(event -> {
                    if (event.getKind() == ElementKind.METHOD) {
                        var superType = event.getEnclosingElement();
                        Mod.EventBusSubscriber subscriber = superType.getAnnotation(Mod.EventBusSubscriber.class);
                        if (subscriber != null) {
                            if (event.getModifiers().contains(Modifier.STATIC) && event.getModifiers().contains(Modifier.PRIVATE)) {
                                getEnv()
                                        .getMessager()
                                        .printError(
                                                "EventBusSubscriber present, found method that should be public static",
                                                event
                                        );
                            } else {
                                var bus = subscriber.bus();
                                var param = event.getEnclosedElements()
                                        .stream()
                                        .filter(element -> element.getKind() == ElementKind.PARAMETER)
                                        .findAny();
                                param.ifPresent(p -> {
                                    if (findSpecificInterface(p.asType()) && bus != Mod.EventBusSubscriber.Bus.MOD) {
                                        getEnv()
                                                .getMessager()
                                                .printError(
                                                        "Event Listener using incorrect event, event is a modEvent",
                                                        p
                                                );
                                    }
                                });
                            }
                        }
                    }
                });

        return true;
    }

    private boolean findSpecificInterface(TypeMirror typeMirror) {
        Types types = getEnv().getTypeUtils();
        Element element = types.asElement(typeMirror);

        if (element instanceof TypeElement typeElement) {
            // Check if the type implements the target interface directly
            if (typeElement.getQualifiedName().toString().equals(TARGET_INTERFACE)) {
                System.out.println(typeElement.getSimpleName() + " implements " + TARGET_INTERFACE);
                return true;
            }

            // Otherwise, check if one of the interfaces implemented by the class is the target interface
            List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
            for (TypeMirror iface : interfaces) {
                if (iface.toString().equals(TARGET_INTERFACE)) {
                    System.out.println(typeElement.getSimpleName() + " implements interface: " + TARGET_INTERFACE);
                    return true;
                }
            }
        }

        return false; // No match found
    }
}
