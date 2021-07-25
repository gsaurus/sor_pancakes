/*
 * Copyright 2018 gil.costa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lib.elc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import lib.Rom;
import lib.names.AllEnemyNames;

/**
 *
 * @author gil.costa
 */
public class CharacterObject extends BaseObject {

    public static final int SIZE = 0x18;
    
    private static final String randomizerFileName = "randomizer.txt";

    /**
     * dc.b	; Character ID dc.b 0	; 1 = Level (scene) dc.b 1	; 2 = Spawn Type
     * dc.b 0	; 3 = Palette/Enemy AI dc.b $1C	; 4 = Animation	Status bit 7 is
     * used for enemy that end	the stage ie bosses dc.b 0	; 5 = Starting Obj
     * Mode dc.w $A0	; 6-7 =	Xpos Distance/Timer/Delay dc.w $190	; 8-9 =	XPos
     * dc.w 0	; A-B =	YPos dc.b 2	; C = Health dc.b $E	; D = Hit Width dc.b $48
     * ; E = Hit Height dc.b 6	; F = Vicinty dc.w $10	; Score dc.l Txt_Galsia	;
     * "GALSIA " dc.w $252	; VRAM address
     */
    public long address;

    public int sceneId;          // in what scene this character shows up
    public int triggerType;      // trigger by position, timer, etc
    public int minimumDifficulty;       // Minimum minimumDifficulty for it to show up
    public boolean useAlternativePalette; // if character use secondary palette
    public int enemyAgressiveness;       // ????
    public int initialState;     // Animation status
    public boolean useBossSlot;
    public int introductionType; // If character falls from sky, or out of a sewer etc
    // Flags used in spawn type
    public boolean bikerWeaponFlag;
    public int ninjaHookHeight;
    public int triggerArgument;  // Depending on the trigger, an argument can be used (e.g. timer, distance, etc)    
    public int health;
    // Collision box x, y, z
    public int collisionWidth;
    public int collisionHeight;
    public int collisionDept;
    public int deathScore;      // Score added when enemie dies
    public long nameAddress;     // Address of enemy name
    public int vram;            // We don't use this one

    public CharacterObject(RandomAccessFile rom, long address) throws IOException {
        this.address = address;
        rom.seek(address);
        objectId = rom.read();
        sceneId = rom.read();
        triggerType = rom.read();
        minimumDifficulty = triggerType >> 4 & 0x7;
        triggerType &= 0x7;
        int palleteByte = rom.read();
        enemyAgressiveness = palleteByte & 0xEF;
        useAlternativePalette = ((palleteByte >> 4) & 0x1) == 1;

        initialState = rom.read();
        useBossSlot = ((initialState >> 7) & 0x1) == 1;
        initialState &= 0x7F;
        introductionType = rom.read();
        bikerWeaponFlag = (introductionType >> 7 & 0x1) == 1;
        ninjaHookHeight = (introductionType >> 4 & 0x7);
        introductionType &= 0x0F;
        triggerArgument = rom.readUnsignedShort();

        posX = rom.readShort();
        posY = rom.readShort();

        health = rom.read();
        collisionWidth = rom.read();
        collisionHeight = rom.read();
        collisionDept = rom.read();
        deathScore = rom.readUnsignedShort();

        nameAddress = rom.readInt();
        vram = rom.readUnsignedShort(); // Discarding vram for now..
    }

    public void write(RandomAccessFile rom, long address, int deltaObjectId) throws IOException {
        rom.seek(address);
        rom.writeByte(objectId + deltaObjectId);
        rom.writeByte(sceneId);
        int triggerAndDifficulty = triggerType;
        triggerAndDifficulty += (minimumDifficulty << 4) & 0xF8;
        rom.writeByte(triggerAndDifficulty);

        int palleteByte = enemyAgressiveness;
        if (useAlternativePalette) {
            palleteByte |= 0x10;
        }
        rom.writeByte(palleteByte);

        int initialStateByte = initialState;
        if (useBossSlot) {
            initialStateByte |= 0x80;
        }
        rom.writeByte(initialStateByte);
        int introductionByte = introductionType & 0x0F;
        if (bikerWeaponFlag) {
            introductionByte |= 0x80;
        }
        introductionByte |= (ninjaHookHeight & 0x7) << 4;
        rom.writeByte(introductionByte);
        rom.writeShort(triggerArgument);

        rom.writeShort(posX);
        rom.writeShort(posY);

        rom.writeByte(health);
        rom.writeByte(collisionWidth);
        rom.writeByte(collisionHeight);
        rom.writeByte(collisionDept);
        rom.writeShort(deathScore);

        rom.writeInt((int) nameAddress);
        rom.writeShort(vram);
    }

    public void write(RandomAccessFile rom) throws IOException {
        this.write(rom, address, 0);
    }

    public static void main(String[] args) {
        try {
            Rom rom = new Rom(new File("sor2.bin"));
            CharacterObject obj = new CharacterObject(rom.getRomFile(), 0x1F0DB4);
            obj.write(rom.getRomFile());
            rom.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
    
    

    // TODO: refactor, move this away from here
    enum SpawnTypeLocation {
        invalid,
        inPlaceLeftRight,
        top,
        middle,
    }
    
    static class RngSettings {
        
        public final int objectId; // done

        public final float minProgress;  // done
        public final float maxProgress;  // done
        // compared to others:
        public final float initialWeight; // done
        public final float finalWeight;   // done

        public final int initialHealth;   // done
        public final int finalHealth;     // done

        public final int initialMaxClock; // done
        public final int finalMaxClock;   // done

        public final float[] initialSpawnTypeAccumProbabilities; // done
        public final float[] finalSpawnTypeAccumProbabilities;   // done
        
        public final SpawnTypeLocation[] spawnTypeLocations;
        
        public final int minNamesIndex; // done
        public final int maxNamesIndex; // done
        
        public final float initialBikerWeaponFlagProbability; // done
        public final float finalBikerWeaponFlagProbability;   // done
        
        public final float initialMaxClones; // done
        public final float finalMaxClones;   // done
        
        public int remainingClones;
        public float relativeProgress;
        
        private float[] lerpedSpawnTypeAccumProbabilities;
        
        public float[] getlerpedSpawnTypeAccumProbabilities() {
            if (lerpedSpawnTypeAccumProbabilities != null)
                return lerpedSpawnTypeAccumProbabilities;
            float[] result = new float[initialSpawnTypeAccumProbabilities.length];
            for(int i = 0; i < initialSpawnTypeAccumProbabilities.length; ++i) {
                result[i] = lerp(initialSpawnTypeAccumProbabilities[i], finalSpawnTypeAccumProbabilities[i], relativeProgress);
            }
            lerpedSpawnTypeAccumProbabilities = result;
            return lerpedSpawnTypeAccumProbabilities;
        }
        
        public RngSettings(int objectId, float minProgress, float maxProgress, float initialWeight, float finalWeight, int initialHealth, int finalHealth, int initialMaxClock, int finalMaxClock, float[] initialSpawnTypeWeight, float[] finalSpawnTypeWeight, SpawnTypeLocation[] spawnTypeLocations, int minNamesIndex, int maxNamesIndex, float initialBikerWeaponFlagProbability, float finalBikerWeaponFlagProbability, int initialMaxClones, int finalMaxClones) {
            this.objectId = objectId;
            
            this.minProgress = minProgress;
            this.maxProgress = maxProgress;
            // compared to others:
            this.initialWeight = initialWeight;
            this.finalWeight = finalWeight;
            

            this.initialHealth = initialHealth;
            this.finalHealth = finalHealth;

            this.initialMaxClock = initialMaxClock;
            this.finalMaxClock = finalMaxClock;
            
            float accumInitial = 0;
            float accumFinal = 0;
            for(int i = 0; i < initialSpawnTypeWeight.length; ++i) {
                initialSpawnTypeWeight[i] += accumInitial;
                accumInitial = initialSpawnTypeWeight[i];
                finalSpawnTypeWeight[i] += accumFinal;
                accumFinal = finalSpawnTypeWeight[i];
            }
            for(int i = 0; i < initialSpawnTypeWeight.length; ++i) {
                initialSpawnTypeWeight[i] /= accumInitial;
                finalSpawnTypeWeight[i] /= accumFinal;
            }
            this.initialSpawnTypeAccumProbabilities = initialSpawnTypeWeight;
            this.finalSpawnTypeAccumProbabilities = finalSpawnTypeWeight;

            this.spawnTypeLocations = spawnTypeLocations;

            this.minNamesIndex = minNamesIndex;
            this.maxNamesIndex = maxNamesIndex;

            this.initialBikerWeaponFlagProbability = initialBikerWeaponFlagProbability;
            this.finalBikerWeaponFlagProbability = finalBikerWeaponFlagProbability;
            
            this.initialMaxClones = initialMaxClones;
            this.finalMaxClones = finalMaxClones;
        }
    }
    
    private static RngSettings[] allRngSettings; 
    
    private static int nextInitialInt(Scanner sc) {
        while (sc.hasNext() && !(sc.hasNext("0x.*") || sc.hasNextInt())) {
            skipTheRestOfTheLine(sc);
        }
        return nextInt(sc);
    }
    
    private static int nextInt(Scanner sc) {
        if (sc.hasNext("0x.*")) {
            String hexValue = sc.next("0x.*").substring(2);
            return Integer.parseInt(hexValue, 16);
        }
         if (sc.hasNextInt()) return sc.nextInt();
        return -1;
    }
    
    private static float nextInitialFloat(Scanner sc) {
        while (sc.hasNext() && !sc.hasNextFloat()) {
            skipTheRestOfTheLine(sc);
        }
        return nextFloat(sc);
    }
    
    private static float nextFloat(Scanner sc) {
        if (sc.hasNextFloat()) return sc.nextFloat();
        return Float.NaN;
    }
    
    private static void skipTheRestOfTheLine(Scanner sc) {
        sc.nextLine();
    }
    
    private static SpawnTypeLocation nextInitialSpawnTypeLocation(Scanner sc) {
        while (sc.hasNext("//.*")) {
            skipTheRestOfTheLine(sc);
        }
        return nextSpawnTypeLocation(sc);
    }
    
    private static SpawnTypeLocation nextSpawnTypeLocation(Scanner sc) {
        if (!sc.hasNext())
            return SpawnTypeLocation.invalid;
        String value = sc.next();
        try {
            return SpawnTypeLocation.valueOf(value);
        }
        catch(Exception ex) {
            // ignore
        }
        return SpawnTypeLocation.invalid;
    }
    
    public static void readRandomizer() throws FileNotFoundException {
        File file = new File(randomizerFileName);
        if (!file.exists()) {
            System.err.println(randomizerFileName + " not found");
            return;
        }
        System.out.println("Reading " + randomizerFileName);
        Scanner sc = new Scanner(file);
        // Settings
        MaxLevels = nextInitialInt(sc) + 1;
        healthMultiplier = nextInitialFloat(sc);
        deathScoreMultiplier = nextInitialFloat(sc);
        alternatePaletteInitialProbability = nextInitialFloat(sc);
        alternatePaletteSequencialProbability = nextInitialFloat(sc);
        
        readAllRngSettings(sc);
        System.out.println("Finished reading randozimer settings");
    }
    
    private static float[] nextListOfFloats(Scanner sc) {
        List<Float> floats = new ArrayList<Float>();
        float value = nextInitialFloat(sc);
        while (!Float.isNaN(value)) {
            floats.add(value);
            value = nextFloat(sc);
        }
        float[] result = new float[floats.size()];
        for (int i = 0 ; i < floats.size(); ++i) {
            result[i] = floats.get(i);
        }
        return result;
    }
    
    private static SpawnTypeLocation[] nextListOfSpawnTypeLocations(Scanner sc) {
        List<SpawnTypeLocation> locations = new ArrayList<SpawnTypeLocation>();
        SpawnTypeLocation value = nextInitialSpawnTypeLocation(sc);
        Scanner restOfTheLine = new Scanner(sc.nextLine());
        while (value != SpawnTypeLocation.invalid) {
            locations.add(value);
            value = nextSpawnTypeLocation(restOfTheLine);
        }
        return locations.toArray(new SpawnTypeLocation[0]);
    }
    
    private static void readAllRngSettings(Scanner sc) {
        List<RngSettings> allSettings = new ArrayList<RngSettings>(20);
        int characterId = nextInitialInt(sc);
        while (characterId >= 0){
            allSettings.add(new RngSettings(
                characterId, // ID
                nextInitialFloat(sc), nextFloat(sc), // Progress
                nextInitialFloat(sc), nextFloat(sc), // Weight 
                nextInitialInt(sc), nextInt(sc),     // Health
                nextInitialInt(sc), nextInt(sc),    // Clock
                nextListOfFloats(sc), // Spawn type weight (min)
                nextListOfFloats(sc),  // Spawn type weight (max)
                nextListOfSpawnTypeLocations(sc),
                nextInitialInt(sc), nextInt(sc), // names
                nextInitialFloat(sc), nextFloat(sc), // biker pipe %
                nextInitialInt(sc), nextInt(sc)  // clones
            ));
            characterId = nextInitialInt(sc);
        }
        allRngSettings = allSettings.toArray(new RngSettings[0]);
    }
    
    
     {
    /*
    = new RngSettings[] {
        // Galsia
        new RngSettings(0xE,
                0f, 0.9f, // Progress
                18, 2,    // Weight 
                2, 4,     // Health
                3, 9,     // Clock
                new float[] { 12, 0, 0, 1, 0, 0, 0, 2}, // Spawn type weight (min)
                new float[] { 1, 0, 0, 4, 0, 0, 0, 2},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                0, 6, // names
                0, 0, // biker pipe %
                2, 1  // clones
        ),
        // Donovan
        new RngSettings(0x10,
                0.0001f, 0.85f, // Progress
                15, 4,    // Weight 
                3, 6,     // Health
                2, 9,     // Clock
                new float[] { 12, 0, 0, 0, 1, 0, 0, 2}, // Spawn type weight (min)
                new float[] { 1, 0, 0, 0, 2, 0, 0, 2},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                7, 13, // names
                0, 0, // biker pipe %
                2, 1  // clones
        ),
        // Signal
        new RngSettings(0x18,
                0.01f, 0.8f, // Progress
                11, 1,     // Weight 
                4, 8,   // Health
                2, 9,     // Clock
                new float[] {1}, // Spawn type weight (min)
                new float[] {1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
                14, 19, // names
                0, 0, // biker pipe %
                1, 2  // clones
        ),
        // Biker
        new RngSettings(0x1A,
                0.15f, 0.9f, // Progress
                5, 1,    // Weight 
                4, 6,    // Health
                3, 12,   // Clock
                new float[] {10, 2, 0, 0, 0}, // Spawn type weight (min)
                new float[] {5, 5, 0, 5, 0}, // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight},
                20, 31, // names
                0, 0.6f, // biker pipe %
                0, 10  // clones
        ),
        // Bongo
        new RngSettings(0x1E,
                0.25f, 0.95f, // Progress
                5, 2,       // Weight 
                8, 0x10,    // Health
                2, 6,    // Clock
                new float[] {1, 0, 0, 1},     // Spawn type weight (min)
                new float[] {0.1f, 0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                32, 41,  // names
                0, 0,    // biker pipe %
                0, 0     // clones
        ),
        // Hakuyo
        new RngSettings(0x20,
                0.07f, 0.87f, // Progress
                5, 3,    // Weight 
                5, 0xA,    // Health
                2, 10,   // Clock
                new float[] {1, 0, 0, 1},     // Spawn type weight (min)
                new float[] {0.1f, 0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                42, 54,  // names
                0, 0,    // biker pipe %
                2, 3     // clones
        ),
        // Kickboxer
        new RngSettings(0x24,
                0.40f, 0.92f, // Progress
                5, 2,    // Weight 
                6, 0x10,    // Health
                2, 7,    // Clock
                new float[] {1, 0, 0, 2},     // Spawn type weight (min)
                new float[] {5, 0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                55, 69,  // names
                0, 0,    // biker pipe %
                1, 4     // clones
        ),
        // Ninja
        new RngSettings(0x1C,
                0.35f, 0.5f, // Progress
                5, 1,    // Weight 
                6, 0x10,    // Health
                2, 7,    // Clock
                new float[] {5, 0, 0, 1},     // Spawn type weight (min)
                new float[] {1, 0, 0, 5},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                70, 84,  // names
                0, 0,    // biker pipe %
                0, 0     // clones
        ),
        // Ninja Kunai
        new RngSettings(0x3A,
                0.45f, 0.91f, // Progress
                1, 3,    // Weight 
                6, 0x10, // Health
                2, 7,    // Clock
                new float[] {5, 0, 0, 1},     // Spawn type weight (min)
                new float[] {1, 0, 0, 2},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                70, 84,  // names
                0, 0,    // biker pipe %
                0, 0     // clones
        ),
        // Ninja Sword
        new RngSettings(0x3C,
                0.5f, 0.88f, // Progress
                1, 3,    // Weight 
                6, 0x10, // Health
                2, 7,    // Clock
                new float[] {5, 0, 0, 1},     // Spawn type weight (min)
                new float[] {1, 0, 0, 2},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                70, 84,  // names
                0, 0,    // biker pipe %
                0, 0     // clones
        ),
        // Jack
        new RngSettings(0x12,
                0.01f, 0.75f, // Progress
                2, 1,     // Weight 
                0xA, 0x18,  // Health
                1, 5,     // Clock
                new float[] {1}, // Spawn type weight (min)
                new float[] {1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
                85, 87, // names
                0, 0,   // biker pipe %
                0, 2    // clones
        ),
        // Electra
        new RngSettings(0x16,
                0.035f, 0.76f, // Progress
                4, 1,     // Weight 
                6, 0x0E,  // Health
                1, 9,     // Clock
                new float[] {5, 0, 0, 1},  // Spawn type weight (min)
                new float[] {1, 0, 0, 5},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                88, 98, // names
                0, 0,   // biker pipe %
                0, 2    // clones
        ),
        // Particle
        new RngSettings(0x32,
                0.20f, 0.75f, // Progress
                1, 1,     // Weight 
                4, 0x10,  // Health
                2, 12,    // Clock
                new float[] {3, 0, 0, 1},  // Spawn type weight (min)
                new float[] {4, 0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.top},
                99, 108, // names
                0, 0,   // biker pipe %
                2, 4    // clones
        ),
        // Barbon
        new RngSettings(0x14,
                0.04f, 0.68f, // Progress
                2, 3,         // Weight 
                0x10, 0x1A,   // Health
                0, 4,     // Clock
                new float[] {0, 0, 2, 0, 1},  // Spawn type weight (min)
                new float[] {0, 0, 2, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.middle},
                110, 112, // names
                0, 0,     // biker pipe %
                0, 2      // clones
        ),
        // Barbon
        new RngSettings(0x14,
                0.68f, 0.975f, // Progress
                3, 1,         // Weight 
                0x10, 0x1A,   // Health
                0, 4,     // Clock
                new float[] {0, 0, 2, 0, 1},  // Spawn type weight (min)
                new float[] {0, 0, 2, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.middle},
                110, 112, // names
                0, 0,     // biker pipe %
                0, 2      // clones
        ),
        // Jet
        new RngSettings(0x26,
                0.6f, 0.95f, // Progress
                1, 4,       // Weight 
                8, 0x10,    // Health
                0, 6,       // Clock
                new float[] {1},  // Spawn type weight (min)
                new float[] {1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
                113, 123, // names
                0, 0,     // biker pipe %
                0, 1      // clones
        ),
        // Zamza
        new RngSettings(0x28,
                0.175f, 0.97f, // Progress
                2, 2,     // Weight 
                8, 0x18,// Health
                0, 4,     // Clock
                new float[] {0, 0, 0, 1},  // Spawn type weight (min)
                new float[] {0, 0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.middle},
                124, 126, // names
                0, 0,     // biker pipe %
                0, 2      // clones
        ),
        // Abadede
        new RngSettings(0x2C,
                0.4f, 0.98f, // Progress
                2, 3,      // Weight 
                0x14, 0x20,// Health
                0, 4,      // Clock
                new float[] {0, 0, 1},  // Spawn type weight (min)
                new float[] {0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.middle},
                127, 128, // names
                0, 0,     // biker pipe %
                0, 1      // clones
        ),
        // R.Bear
        new RngSettings(0x2A,
                0.6f, 0.977f, // Progress
                2, 1,       // Weight 
                0x18, 0x24, // Health
                0, 4,       // Clock
                new float[] {0, 0, 1},  // Spawn type weight (min)
                new float[] {0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.middle},
                129, 130, // names
                0, 0,     // biker pipe %
                0, 0      // clones
        ),
        // Shiva
        new RngSettings(0x38,
                0.85f, 1f,   // Progress
                1, 5,        // Weight 
                0x10, 0x20,  // Health
                0, 7,        // Clock
                new float[] {0, 0, 1},  // Spawn type weight (min)
                new float[] {0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight},
                132, 132, // names
                0, 0,     // biker pipe %
                0, 0      // clones
        ),
        // Mr.X
        new RngSettings(0x36,
                0.999f, 1f, // Progress
                9999, 9999,   // Weight 
                0x35, 0x35,   // Health
                8, 8,         // Clock
                new float[] {0, 0, 1},  // Spawn type weight (min)
                new float[] {0, 0, 1},  // Spawn type weight (max)
                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight, SpawnTypeLocation.inPlaceLeftRight},
                131, 131, // names
                0, 0,     // biker pipe %
                0, 0      // clones
        ),
        // Max
//        new RngSettings(0x0,
//                0f, 1f, // Progress
//                1, 1,    // Weight 
//                4, 4,     // Health
//                3, 9,     // Clock
//                new float[] { 1}, // Spawn type weight (min)
//                new float[] { 1},  // Spawn type weight (max)
//                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
//                0, 0, // names
//                0, 0, // biker pipe %
//                0, 0  // clones
//        ),
//        // Axel
//        new RngSettings(0x2,
//                0f, 1f, // Progress
//                1, 1,    // Weight 
//                4, 4,     // Health
//                3, 9,     // Clock
//                new float[] { 1}, // Spawn type weight (min)
//                new float[] { 1},  // Spawn type weight (max)
//                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
//                0, 0, // names
//                0, 0, // biker pipe %
//                0, 0  // clones
//        ),
//        // Blaze
//        new RngSettings(0x4,
//                0f, 1f, // Progress
//                1, 1,    // Weight 
//                4, 4,     // Health
//                3, 9,     // Clock
//                new float[] { 1}, // Spawn type weight (min)
//                new float[] { 1},  // Spawn type weight (max)
//                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
//                0, 0, // names
//                0, 0, // biker pipe %
//                0, 0  // clones
//        ),
//        // Skate
//        new RngSettings(0x6,
//                0f, 1f, // Progress
//                1, 1,    // Weight 
//                4, 4,     // Health
//                3, 9,     // Clock
//                new float[] { 1}, // Spawn type weight (min)
//                new float[] { 1},  // Spawn type weight (max)
//                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
//                0, 0, // names
//                0, 0, // biker pipe %
//                0, 0  // clones
//        ),
//        // Adam
//        new RngSettings(0x8,
//                0f, 1f, // Progress
//                1, 1,    // Weight 
//                4, 4,     // Health
//                3, 9,     // Clock
//                new float[] { 1}, // Spawn type weight (min)
//                new float[] { 1},  // Spawn type weight (max)
//                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
//                0, 0, // names
//                0, 0, // biker pipe %
//                0, 0  // clones
//        ),
//        // Zan
//        new RngSettings(0xA,
//                0f, 1f, // Progress
//                1, 1,    // Weight 
//                4, 4,     // Health
//                3, 9,     // Clock
//                new float[] { 1}, // Spawn type weight (min)
//                new float[] { 1},  // Spawn type weight (max)
//                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
//                0, 0, // names
//                0, 0, // biker pipe %
//                0, 0  // clones
//        ),
//        // Roo
//        new RngSettings(0xC,
//                0f, 1f, // Progress
//                1, 1,    // Weight 
//                4, 4,     // Health
//                3, 9,     // Clock
//                new float[] { 1}, // Spawn type weight (min)
//                new float[] { 1},  // Spawn type weight (max)
//                new SpawnTypeLocation[] { SpawnTypeLocation.inPlaceLeftRight},
//                0, 0, // names
//                0, 0, // biker pipe %
//                0, 0  // clones
//        ),
        
    };
*/
}     
    
    
    private static float[] accumulatedProbabilities;
    
    private static RngSettings currentRngSetting;
    
    // Configurable
    private static int MaxLevels = 31;
    private static float healthMultiplier = 1.5f;
    private static float deathScoreMultiplier = 8;
    private static float alternatePaletteInitialProbability = 0.6f;
    private static float alternatePaletteSequencialProbability = 0.5f;
    
    private static boolean previousWasAlternatePalette;
    
    private static float lerp(float min, float max, float progress) {
        return min + (max - min) * progress;
    }
    
    
    private static void updateProbabilities(float progress) {
        accumulatedProbabilities = new float[allRngSettings.length];
        float accumulatedWeight = 0;
        for (int i = 0; i < allRngSettings.length; ++i) {
            RngSettings setting = allRngSettings[i];
            if (progress < setting.minProgress || progress > setting.maxProgress) {
                accumulatedProbabilities[i] = 0;
                continue;
            }
            setting.relativeProgress = (progress - setting.minProgress) / (setting.maxProgress - setting.minProgress);
            setting.lerpedSpawnTypeAccumProbabilities = null;
            float weight = lerp(setting.initialWeight, setting.finalWeight, setting.relativeProgress);
            accumulatedWeight += weight;
            accumulatedProbabilities[i] = accumulatedWeight;
        }
        for (int i = 0; i < accumulatedProbabilities.length; ++i) {
            accumulatedProbabilities[i] /= accumulatedWeight;
        }
    }
            
    
    private static RngSettings getNextSetting(Random random, float progress) {
        if (currentRngSetting != null && currentRngSetting.remainingClones > 0) {
            RngSettings returnSetting = currentRngSetting;
            if (--currentRngSetting.remainingClones == 0) {
                currentRngSetting = null;
            }
            return returnSetting;
        }
        updateProbabilities(progress);
        float randomValue = random.nextFloat();
        currentRngSetting = allRngSettings[allRngSettings.length - 1];
        for (int i = 0; i < accumulatedProbabilities.length; ++i) {
            if (accumulatedProbabilities[i] >= randomValue) {
                currentRngSetting = allRngSettings[i];
                break;
            }
        }
        currentRngSetting.remainingClones = 0;
        int maxClones = (int)lerp(currentRngSetting.initialMaxClones, currentRngSetting.finalMaxClones + 1, currentRngSetting.relativeProgress);
        if (maxClones > 0) {
            currentRngSetting.remainingClones = random.nextInt(maxClones + 1);
        }
        return currentRngSetting;
    }
    
    public static void prepareForRandomization() {
        currentRngSetting = null;
    }
    
    private boolean isSpecialEnemy() {
        return (objectId == 0x16 && introductionType == 1) // Electra singing
            || (objectId == 0x14 && introductionType == 0) // Barbon coktailing
            || (objectId == 0x38 && introductionType == 0) // Shiva right hand of Mr.X
            || (objectId == 0x36 && introductionType == 0); // Mr.X final boss
    }
            

    public void randomize(int spawnNum, int totalSpawns, long enemyNamesAddress, boolean updateScene) {
        if (!updateScene && isSpecialEnemy()) {
            return;
        }
        float progress = (float)spawnNum / totalSpawns;
        Random random = new Random();
        RngSettings settings = getNextSetting(random, progress);
        
        if (updateScene) {
            sceneId = (int)(progress * MaxLevels) * 2;
            // Last enemy still fits in last sceneId
            if (sceneId >= MaxLevels * 2) {
                sceneId = MaxLevels * 2 - 2;
            }
        }
        enemyAgressiveness = (int)Math.ceil(progress + random.nextFloat()*(1 - progress) * 0xF);
        objectId = settings.objectId;
        minimumDifficulty = 0;
        triggerType = 2;
        if (previousWasAlternatePalette && random.nextFloat() > alternatePaletteSequencialProbability) {
            useAlternativePalette = true;
        }
        else {
            useAlternativePalette = random.nextFloat() > alternatePaletteInitialProbability;
            previousWasAlternatePalette = useAlternativePalette;
        }
        
        useBossSlot = false;
        collisionWidth = 14;
        collisionHeight = 72;
        collisionDept = 6;
        initialState = 0;
        
        health = (int) lerp(settings.initialHealth * healthMultiplier, settings.finalHealth * healthMultiplier + 1, settings.relativeProgress);
        
        // Name
        int nameIndex = settings.minNamesIndex + random.nextInt(settings.maxNamesIndex - settings.minNamesIndex + 1);
        nameAddress = enemyNamesAddress + nameIndex * AllEnemyNames.NAME_SIZE;
        // Clock
        int clockValue = (int) lerp(settings.initialMaxClock, settings.finalMaxClock + 1, settings.relativeProgress);
        clockValue = (int) Math.ceil(clockValue / 2f);
        if (clockValue > 0) {
            clockValue = clockValue + random.nextInt(clockValue + 1);
        }
        triggerArgument = (clockValue & 0xFF);
        
        float weaponFlagProbability = lerp(settings.initialBikerWeaponFlagProbability, settings.finalBikerWeaponFlagProbability, settings.relativeProgress);
        bikerWeaponFlag = random.nextFloat() < weaponFlagProbability;
        
            
        deathScore = (int)((health + 1) * deathScoreMultiplier); // TODO: better score function?
        
        
        // Spawn type
        float[] lerpedSpawnAccumProbabilities = settings.getlerpedSpawnTypeAccumProbabilities();
        introductionType = 0;
        float randomValue = spawnNum < 4 ? 0 : random.nextFloat();
        for (int i = 0; i < lerpedSpawnAccumProbabilities.length; ++i) {
            if (lerpedSpawnAccumProbabilities[i] > randomValue) {
                introductionType = i;
                break;
            }
        }
        
        // Positioning
        SpawnTypeLocation locationType = settings.spawnTypeLocations[introductionType];
        //System.out.println(introductionType + " ==> " + locationType);
        switch(locationType) {
            case inPlaceLeftRight: {
                posX = -40 -random.nextInt(160);
                if (spawnNum < 4 || random.nextBoolean()) {
                    posX = 400 - posX;
                }
                if (!updateScene) posY = 0;
                else posY = 176 + random.nextInt(230 - 176);
            } break;
            case top: {
                posX = 90 + random.nextInt(330 - 90);
                posY = 0;
            } break;
            case middle: {
                posX = 90 + random.nextInt(400 - 90);
                posY = 176 + random.nextInt(230 - 176);
            } break;
        }
        
        // corrections...
        while (posX > 800)  {
            posX = -40 -random.nextInt(160);
            if (spawnNum < 4 || random.nextBoolean()) {
                posX = 400 - posX;
            }
        }
            
        
    }

}
