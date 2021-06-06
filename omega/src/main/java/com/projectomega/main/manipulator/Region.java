package com.projectomega.main.manipulator;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class Region extends AbstractMap<ChunkPos, Chunk> {

    private final Chunk[] chunks = new Chunk[1024];
    private final Map<ChunkPos, CorruptChunk> corruptChunks = new LinkedHashMap<>();
    private final RegionPos position;

    public Region(RegionPos position) {
        this.position = position;
    }

    public Region(RegionPos position, List<@Nullable Chunk> chunks, List<@Nullable CorruptChunk> corruptChunks) {
        this.position = position;
        addAll(chunks);
        corruptChunks.stream().filter(Objects::nonNull).forEach(this::setCorrupt);
    }

    @Nullable
    public Chunk put(@NonNull ChunkPos key, @NonNull Chunk value) {
        if (key != value.getPosition()) {
            throw new IllegalStateException("The chunk's key doesn't match the chunk's value. Key: " + key + ", Chunk: " + value.getPosition());
        } else {
            checkValid(key);
            int offset = offset(key);
            Chunk before = chunks[offset];
            chunks[offset] = value;
            corruptChunks.remove(key);
            return before;
        }
    }

    private void checkValid(ChunkPos chunk) {
        int regX = (int) Math.floor(chunk.getXPos() / 32.0D);
        int regZ = (int) Math.floor(chunk.getZPos() / 32.0D);
        if (!(regX == position.getXPos() && regZ == position.getZPos())) {
            throw new IllegalStateException("The chunk " + chunk + " is not part of the region " + position + ". It's part of r." + regX + '.' + regZ + ".mca");
        }
    }

    private boolean isInvalid(ChunkPos chunkPos) {
        int regX = (int) Math.floor(chunkPos.getXPos() / 32.0D);
        int regZ = (int) Math.floor(chunkPos.getZPos() / 32.0D);
        return regX != position.getXPos() || regZ != position.getZPos();
    }

    public @Nullable Chunk get(@NonNull ChunkPos key) throws CorruptChunkException {
        if (isInvalid(key)) {
            return null;
        } else {
            CorruptChunk chunk = corruptChunks.get(key);
            if (chunk != null) {
                throw new CorruptChunkException(chunk);
            } else {
                return chunks[offset(key)];
            }
        }
    }

    public @Nullable Chunk setCorrupt(@NonNull CorruptChunk corruptChunk) {
        checkValid(corruptChunk.getPosition());
        Chunk removed = remove((Object) corruptChunk.getPosition());
        corruptChunks.put(corruptChunk.getPosition(), corruptChunk);
        return removed;
    }

    @Nullable
    public final CorruptChunk getCorrupt(@NotNull ChunkPos key) {
        return this.corruptChunks.get(key);
    }

    @Nullable
    public Chunk remove(@NonNull ChunkPos key) {
        if (isInvalid(key)) {
            return null;
        } else {
            int offset = offset(key);
            Chunk before = chunks[offset];
            chunks[offset] = null;
            corruptChunks.remove(key);
            return before;
        }
    }

    public boolean remove(@NonNull ChunkPos key, @NonNull Chunk value) {
        if (isInvalid(key)) {
            return false;
        } else {
            int offset = offset(key);
            Chunk before = chunks[offset];
            boolean removed;
            if (Objects.equals(value, before)) {
                chunks[offset] = null;
                removed = true;
            } else {
                removed = false;
            }
            return removed;
        }
    }

    private int offset(ChunkPos chunkPos) {
        return internalOffset(chunkPos.getXPos() - position.getXPos() * 32, chunkPos.getZPos() - position.getZPos() * 32);
    }

    private int internalOffset(int x, int z) {
        return x % 32 + z % 32 * 32;
    }

    public final void add(@NonNull Chunk chunk) {
        put(chunk.getPosition(), chunk);
    }

    public final void addAll(@NonNull List<@Nullable Chunk> chunks) {
        chunks.stream().filter(Objects::nonNull).forEach(this::add);
    }

    @NotNull @Override public Set<Entry<ChunkPos, Chunk>> entrySet() {
        return new AbstractSet<>() {
            @Override public Iterator<Entry<ChunkPos, Chunk>> iterator() {
                return new Iterator<>() {
                    private Entry<ChunkPos, Chunk> current;

                    private final Iterator<Entry<ChunkPos, Chunk>> iter = (Iterator) Arrays.stream(chunks)
                            .filter(Objects::nonNull).map(chunk -> new Entry<ChunkPos, Chunk>() {

                                private final ChunkPos key = chunk.getPosition();

                                @Override public ChunkPos getKey() {
                                    return key;
                                }

                                @Override public Chunk getValue() {
                                    Chunk chunk = get(key);
                                    return chunk == null ? chunk : chunk;
                                }

                                @Override public Chunk setValue(Chunk value) {
                                    Chunk put = put(key, value);
                                    return put == null ? chunk : put;
                                }
                            }).iterator();

                    @Override public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override public Entry<ChunkPos, Chunk> next() {
                        current = iter.next();
                        return current;
                    }

                    @Override public void remove() {
                        Region.this.remove(current.getKey());
                    }
                };
            }

            @Override public int size() {
                return (int) Arrays.stream(chunks).filter(Objects::nonNull).count();
            }
        };
    }

    public RegionPos getPosition() {
        return position;
    }
}
