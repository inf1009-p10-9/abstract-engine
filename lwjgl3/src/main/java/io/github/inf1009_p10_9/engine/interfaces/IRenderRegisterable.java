package io.github.inf1009_p10_9.engine.interfaces;
// registers and unregisters renderables with the output manager
public interface IRenderRegisterable {
    void registerRenderable(IRenderable obj);
    void unregisterRenderable(IRenderable obj);
}
