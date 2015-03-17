/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.c45y.TheIsland;

import java.util.Random;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.World;

/**
 *
 * @author c45y
 */
public class VoidGen extends ChunkGenerator {

    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        byte[][] result = new byte[256 / 16][]; //world height / chunk part height (=16, look above)
        int x = 0, y = 0, z = 0;
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                for (y = 0; y < 16; y++) {
                    setBlock(result, x, y, z, (byte) 0);
                }
            }
        }
        return result;
    }

    void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }
}
