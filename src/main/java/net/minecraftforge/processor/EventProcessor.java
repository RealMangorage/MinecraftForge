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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@ApiStatus.Internal
public class EventProcessor extends DefaultProcessor {
    enum Bus {
        FORGE,
        MOD,
        UNKNOWN
    }


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
                        var busType = getBus(Mod.EventBusSubscriber.class, superType);
                        System.out.println(busType);
                    }
                });

        return true;
    }

    private Bus getBus(Class<? extends Annotation> annotation, Element element) {
        AtomicReference<Bus> result = new AtomicReference<>(Bus.UNKNOWN);
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getName())) {
                annotationMirror.getElementValues().forEach((k, v) -> {
                    System.out.println(v.getValue());
                });
            }
        }
        return result.get();
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
