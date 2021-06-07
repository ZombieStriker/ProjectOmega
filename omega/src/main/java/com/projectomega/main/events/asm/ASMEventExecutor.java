package com.projectomega.main.events.asm;

import com.projectomega.main.events.Event;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static org.objectweb.asm.Opcodes.*;

public final class ASMEventExecutor {

    public static final AtomicInteger LISTENER_ID = new AtomicInteger(0);

    @SneakyThrows
    public static EventExecutor createEventExecutor(@NotNull Method method, @NotNull Class<? extends Event> eventClass) {
        String name = generateName();
        byte[] genClass = generateEventExecutor(method, name, eventClass);
        Class<? extends EventExecutor> executorClass = GeneratedClassDefiner.INSTANCE
                .defineClass(method.getDeclaringClass().getClassLoader(), name, genClass)
                .asSubclass(EventExecutor.class);
        EventExecutor delegate = executorClass.newInstance();
        return (listener, event) -> {
            if (!eventClass.isInstance(event)) return;
            delegate.execute(listener, event);
        };
    }

    private static byte[] generateEventExecutor(Method m, String name, Class<? extends Event> eventClass) {
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        writer.visit(V1_8, ACC_PUBLIC,
                name.replace('.', '/'), // class name
                null, // method signature
                Type.getInternalName(Object.class), // superclass
                new String[]{Type.getInternalName(EventExecutor.class)} // interfaces
        );

        // create the constructor because that's required by the class bytecode format
        GeneratorAdapter methodGenerator = new GeneratorAdapter(writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null), ACC_PUBLIC, "<init>", "()V");
        methodGenerator.loadThis();
        methodGenerator.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false);
        methodGenerator.returnValue();
        methodGenerator.endMethod();

        // generate the event
        methodGenerator = new GeneratorAdapter(writer.visitMethod(ACC_PUBLIC, "execute",
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Object.class), Type.getType(Event.class)), null, null),
                1,
                "execute",
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Object.class), Type.getType(Event.class)));
        methodGenerator.loadArg(0);
        methodGenerator.checkCast(Type.getType(m.getDeclaringClass()));
        methodGenerator.loadArg(1);
        methodGenerator.checkCast(Type.getType(eventClass));
        methodGenerator.visitMethodInsn(m.getDeclaringClass().isInterface() ? INVOKEINTERFACE : INVOKEVIRTUAL, Type.getInternalName(m.getDeclaringClass()), m.getName(), Type.getMethodDescriptor(m), m.getDeclaringClass().isInterface());
        if (m.getReturnType() != Void.TYPE) {
            methodGenerator.pop();
        }
        methodGenerator.returnValue();
        methodGenerator.endMethod();
        writer.visitEnd();
        return writer.toByteArray();
    }

    public static String generateName() {
        return "com.projectomega.gen.event.GeneratedListener" + LISTENER_ID.getAndIncrement();
    }

    public interface EventExecutor {

        void execute(@NotNull Object listener, @NotNull Event event);
    }

}
