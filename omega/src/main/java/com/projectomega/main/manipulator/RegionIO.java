package com.projectomega.main.manipulator;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.NBTWriter;
import me.nullicorn.nedit.type.NBTCompound;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import static java.util.Objects.requireNonNull;

public final class RegionIO {

    private static final int LEVEL = 7;
    private static final Pattern BY_DOT = Pattern.compile("\\.");

    private RegionIO() {
    }

    public static @NotNull Region readRegion(@NonNull File file) {
        List<String> nameParts = Arrays.asList(BY_DOT.split(file.getName()));
        int xPos = Integer.parseInt(nameParts.get(1));
        int zPos = Integer.parseInt(nameParts.get(2));
        RegionPos regionPos = new RegionPos(xPos, zPos);
        return readRegion(file, regionPos);
    }

    public static @NotNull Region readRegion(@NonNull File file, @NonNull RegionPos pos) {
        Region region = null;
        try (RandomAccessFile input = new RandomAccessFile(file, "r")) {
            ChunkInfo[] chunkInfos = new ChunkInfo[1024];

            for (int i = 0; i < chunkInfos.length; ++i) {
                int it = (input.read() << 16) + (input.read() << 8) + input.read();
                ChunkInfo info = new ChunkInfo(it * 4096, input.read() * 4096);
                if (info.size != 0)
                    chunkInfos[i] = info;
            }

            for (int i = 0; i < 1024; ++i) {
                int value = input.readInt();
                if (value != 0) {
                    chunkInfos[i].lastModified = new Date(value * 1000L);
                }
            }

            List<Chunk> chunks = new ArrayList<>();
            List<CorruptChunk> corruptChunks = new ArrayList<>();
            for (int i = 0; i < chunkInfos.length; i++) {
                ChunkInfo info = chunkInfos[i];
                if (info == null) {
                    chunks.add(null);
                } else {
                    int length = 0;
                    int compression = 0;
                    byte[] data = null;
                    try {
                        input.seek(info.location);
                        length = input.readInt();
                        compression = input.read();
                        if (compression != 1 && compression != 2) {
                            throw new IllegalStateException("Bad compression " + compression + ". Chunk index: " + i);
                        }
                        data = new byte[Math.min(length, info.size)];
                        int read = readFullyIfPossible(input, data);
                        if (read < data.length) {
                            data = Arrays.copyOf(data, read);
                        }
                        if (length > data.length) {
                            throw new EOFException("Could not read all " + length + "bytes. Read only " + data.length + " bytes in a sector of " + info.size + " bytes");
                        }
                        InputStream inputStream;
                        switch (compression) {
                            case 1:
                                inputStream = new GZIPInputStream(new ByteArrayInputStream(data));
                                break;
                            case 2:
                                inputStream = new InflaterInputStream(new ByteArrayInputStream(data));
                                break;
                            default:
                                throw new IllegalStateException("Unexpected compression type " + compression);
                        }
                        NBTCompound nbt = NBTReader.read(inputStream);
                        chunks.add(new Chunk(info.lastModified, nbt));
                    } catch (Throwable t) {
                        corruptChunks.add(new CorruptChunk(pos, i, info.lastModified, data,
                                info.location, info.size,
                                length, compression, t));
                    }
                }
            }
            region = new Region(pos, chunks, corruptChunks);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return requireNonNull(region);
    }

    @SneakyThrows
    private static int readFullyIfPossible(RandomAccessFile input, byte[] data) {
        int size = data.length;
        int currentSize = 0;
        do {
            int read = input.read(data, currentSize, size - currentSize);
            if (read < 0) {
                return currentSize;
            }
            currentSize += read;
        } while (currentSize < size);
        return currentSize;
    }

    private static byte[] deflate(byte[] data) {
        Deflater deflater = new Deflater(LEVEL);
        deflater.reset();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];

        try {
            while (!deflater.finished()) {
                int i = deflater.deflate(buf);
                bos.write(buf, 0, i);
            }
        } finally {
            deflater.end();
        }

        return bos.toByteArray();
    }

    public static void writeRegion(@NonNull File file, @NonNull Region region) throws IOException {
        List<ChunkInfo> chunkInfoHeader = new ArrayList<>();
        ByteArrayOutputStream heapData = new ByteArrayOutputStream();
        DataOutputStream heap = new DataOutputStream(heapData);
        int heapPos = 0;
        int z = 0;

        for (int i = 0; i < 32; ++i) {
            for (int x = 0; x < 32; ++x) {
                ChunkPos pos = new ChunkPos(region.getPosition().getXPos() * 32 + x, region.getPosition().getZPos() * 32 + z);
                Chunk chunk = region.get(pos);
                if (chunk == null) {
                    ChunkInfo it = new ChunkInfo(0, 0);
                    chunkInfoHeader.add(it);
                } else {
                    ByteArrayOutputStream chunkData = new ByteArrayOutputStream();
                    NBTWriter.write(chunk.getCompound(), chunkData, false);
                    byte[] uncompressedChunkBytes = requireNonNull(chunkData.toByteArray());
                    byte[] chunkBytes = deflate(uncompressedChunkBytes);
                    byte[] sectionBytes = new byte[(int) Math.ceil((chunkBytes.length + 5) / 4096.0D) * 4096 - 5];

                    for (int a = 0; a < sectionBytes.length; ++a) {
                        sectionBytes[a] = a >= chunkBytes.length ? 0 : chunkBytes[a];
                    }

                    heap.writeInt(chunkBytes.length + 1);
                    heap.writeByte(2);
                    heap.write(sectionBytes);
                    chunkInfoHeader.add(new ChunkInfo(8192 + heapPos, sectionBytes.length + 5, chunk.getLastModified()));
                    heapPos += 5 + sectionBytes.length;
                }
            }
        }
        heap.flush();
        heap.close();
        byte[] heapBytes = heapData.toByteArray();
        ByteArrayOutputStream headerData = new ByteArrayOutputStream();
        DataOutputStream header = new DataOutputStream(headerData);
        for (ChunkInfo info : chunkInfoHeader) {
            if (info.size > 0) {
                if (info.location < 8192) {
                    throw new AssertionError("Header location is too short, it must be >= 8192! Got " + info.location);
                }
                if (!(ByteBuffer.wrap(heapBytes, info.location - 8192, 4).getInt() > 0)) {
                    throw new AssertionError("Header location is pointing to an incorrect heap location");
                }
            }
            int sec = info.location / 4096;
            header.writeByte(sec >> 16 & 255);
            header.writeByte(sec >> 8 & 255);
            header.writeByte(sec & 255);
            int size = info.size / 4096;
            header.writeByte(size);
            header.writeInt((int) (info.lastModified.getTime() / 1000L));
        }
        header.close();
        byte[] headerBytes = headerData.toByteArray();
        if (headerBytes.length != 8192) {
            throw new IllegalStateException("Failed to write the mca header. Size " + header.size() + " != 4096");
        }
        try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(file), 8 * 1024)) {
            stream.write(headerBytes);
            stream.write(heapBytes);
            stream.flush();
        }
    }

    @EqualsAndHashCode
    @ToString
    private static final class ChunkInfo {

        private final int location;
        private final int size;
        private Date lastModified;

        public ChunkInfo(int location, int size) {
            this(location, size, new Date(0));
        }

        public ChunkInfo(int location, int size, Date lastModified) {
            this.location = location;
            this.size = size;
            this.lastModified = lastModified;
        }
    }

}
