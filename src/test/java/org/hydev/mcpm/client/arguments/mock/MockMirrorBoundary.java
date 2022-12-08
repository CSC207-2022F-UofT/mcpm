package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.database.mirrors.Mirror;
import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary;
import org.hydev.mcpm.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Provides a mock implementation of the MirrorSelectBoundary class.
 */
public class MockMirrorBoundary implements MirrorSelectBoundary {
    private Mirror selected;
    private final List<Mirror> mirrors;

    private boolean didUpdateMirrors = false;
    private boolean didPingMirrors = false;
    private boolean throwsException = false;

    public MockMirrorBoundary(List<Mirror> mirrors) {
        this.mirrors = mirrors;
        this.selected = mirrors.stream().findFirst().orElse(null);
    }

    /**
     * Creates a mock Mirror object to pass to this object.
     *
     * @param host The provided host that will be used in the mirror initialzation.
     * @return A Mirror object.
     */
    public static Mirror mockMirror(String host) {
        return new Mirror(
            host,
            Set.of("http", "https"),
            2,
            "Canada",
            300,
            40,
            "https://" + host
        );
    }

    @Override
    public @NotNull List<Mirror> listAvailableMirrors() throws IOException {
        if (throwsException) {
            throw new IOException();
        }

        return mirrors;
    }

    @Override
    public List<Pair<Mirror, Integer>> pingMirrors() throws IOException {
        // This method was really handy in the boundary! I'm sorry for asking it to be moved.

        didPingMirrors = true;

        if (throwsException) {
            throw new IOException();
        }

        return IntStream.range(0, mirrors.size())
            .mapToObj(index -> Pair.of(mirrors.get(index), (index + 1) * 10))
            .toList();
    }

    @Override
    public void updateMirrors() throws IOException {
        didUpdateMirrors = true;

        if (throwsException) {
            throw new IOException();
        }
        /* do nothing... shouldn't this be automatic? */
    }

    @Override
    public Mirror getSelectedMirror() throws IOException {
        if (throwsException) {
            throw new IOException();
        }

        return selected;
    }

    @Override
    public void setSelectedMirror(Mirror mirror) throws IOException {
        if (throwsException) {
            throw new IOException();
        }

        selected = mirror;
    }

    /**
     * Gets if the mock mirrors object was udpated.
     *
     * @return True if the updateMirrors method was invoked at least once.
     */
    public boolean getDidUpdateMirrors() {
        return didUpdateMirrors;
    }

    /**
     * Gets if the mirrors were pinged.
     *
     * @return True if the pingMirrors object was invoked at least once.
     */
    public boolean getDidPingMirrors() {
        return didPingMirrors;
    }

    /**
     * Sets if the methods will throw an exception.
     *
     * @param value If true, every boundary method that can throw an IOException will throw one.
     */
    public void setThrowsException(boolean value) {
        throwsException = value;
    }
}
