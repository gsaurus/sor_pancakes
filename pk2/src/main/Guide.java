/*
 * Decompiled with CFR 0_132.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

public class Guide {
    public static String GUIDES_DIR = "guides/";
    private long animsListAddress;
    private long hitsListAddress;
    private long weaponsListAddress;
    private long iconsListAddress;
    private long namesListAddress;
    private long statsListAddress;
    private long speedListAddress;
    private String animsListLabel;
    private String hitsListLabel;
    private String weaponsListLabel;
    private String iconsListLabel;
    private String namesListLabel;
    private String statsListLabel;
    private String speedListLabel;
    private int playableChars;
    private int numNameLetters;
    private boolean globalCollisions;
    private boolean globalWeapons;
    private ArrayList<Integer> skips;
    private ArrayList<String> charNames;
    private ArrayList<Long> paletteAddresses;
    private ArrayList<String> paletteLabels;
    private ArrayList<Integer> animTypes;
    private ArrayList<Integer> animsCount;
    private ArrayList<ArrayList<String>> animNames;
    private TreeMap<Integer, Long> compressedArtAddresses;
    private TreeMap<Integer, String> compressedArtLabels;
    private TreeMap<Integer, Long> subArtAddresses;
    private TreeMap<Integer, String> subArtLabels;

    private long hexToLong(String str) {
        return Long.parseLong(str, 16);
    }

    private int stringToInt(String str) {
        return Integer.parseInt(str);
    }

    public void setAnimType(int charId, int newType) {
        this.animTypes.set(charId, newType);
    }

    public int getRealCharId(int id) {
        int nextSkip;
        int res;
        Iterator<Integer> i$ = this.skips.iterator();
        for (res = id; i$.hasNext() && res >= (nextSkip = i$.next().intValue()); ++res) {
        }
        return res;
    }

    public int getFakeCharId(int id) {
        int nextSkip;
        int res = id;
        Iterator<Integer> i$ = this.skips.iterator();
        while (i$.hasNext() && id >= (nextSkip = i$.next().intValue())) {
            --res;
        }
        return res;
    }

    private void initLists(int numChars) {
        this.skips = new ArrayList();
        this.charNames = new ArrayList(numChars);
        this.animNames = new ArrayList(numChars);
        this.animTypes = new ArrayList(numChars);
        this.animsCount = new ArrayList(numChars);
        this.paletteAddresses = new ArrayList(numChars);
        this.paletteLabels = new ArrayList(numChars);
        this.compressedArtAddresses = new TreeMap();
        this.compressedArtLabels = new TreeMap();
        this.subArtAddresses = new TreeMap();
        this.subArtLabels = new TreeMap();
    }

    public Guide(String guideFileName) throws FileNotFoundException, Exception {
        File file = new File(guideFileName);
        String fileDir = file.getParent();
        Scanner sc = new Scanner(file);
        boolean labeled = sc.nextInt() != 0;
        sc.nextLine();
        if (!labeled) {
            this.animsListAddress = this.hexToLong(sc.next());
            sc.nextLine();
            this.globalCollisions = sc.nextInt() != 0;
            sc.nextLine();
            this.hitsListAddress = this.hexToLong(sc.next());
            sc.nextLine();
            this.globalWeapons = sc.nextInt() != 0;
            sc.nextLine();
            this.weaponsListAddress = this.hexToLong(sc.next());
            sc.nextLine();
            this.iconsListAddress = this.hexToLong(sc.next());
            sc.nextLine();
            this.numNameLetters = sc.nextInt();
            sc.nextLine();
            this.namesListAddress = this.hexToLong(sc.next());
            sc.nextLine();
            this.statsListAddress = this.hexToLong(sc.next());
            sc.nextLine();
            this.speedListAddress = this.hexToLong(sc.next());
            sc.nextLine();
        } else {
            this.animsListLabel = sc.next();
            sc.nextLine();
            this.globalCollisions = sc.nextInt() != 0;
            sc.nextLine();
            this.hitsListLabel = sc.next();
            sc.nextLine();
            this.globalWeapons = sc.nextInt() != 0;
            sc.nextLine();
            this.weaponsListLabel = sc.next();
            sc.nextLine();
            this.iconsListLabel = sc.next();
            sc.nextLine();
            this.numNameLetters = sc.nextInt();
            sc.nextLine();
            this.namesListLabel = sc.next();
            sc.nextLine();
            this.statsListLabel = sc.next();
            sc.nextLine();
            this.speedListLabel = sc.next();
            sc.nextLine();
        }
        this.playableChars = sc.nextInt();
        sc.nextLine();
        int numChars = sc.nextInt();
        sc.nextLine();
        this.initLists(++numChars);
        for (int i = 0; i < numChars; ++i) {
            String name = "";
            while (name.isEmpty()) {
                name = sc.nextLine();
            }
            if (name.length() == 1) {
                this.skips.add(i);
                continue;
            }
            this.charNames.add(name);
            if (labeled) {
                this.paletteLabels.add(sc.next());
            } else {
                this.paletteAddresses.add(this.hexToLong(sc.next()));
            }
            sc.nextLine();
            int type = sc.nextInt();
            sc.nextLine();
            this.animTypes.add(type);
            switch (type) {
                case 1: {
                    if (labeled) {
                        this.compressedArtLabels.put(i, sc.next());
                    } else {
                        this.compressedArtAddresses.put(i, this.hexToLong(sc.next()));
                    }
                    sc.nextLine();
                    break;
                }
                case 3: {
                    if (labeled) {
                        this.subArtLabels.put(i, sc.next());
                    } else {
                        this.subArtAddresses.put(i, this.hexToLong(sc.next()));
                    }
                    sc.nextLine();
                }
            }
            int count = sc.nextInt();
            sc.nextLine();
            this.animsCount.add(count);
            ArrayList<String> names = new ArrayList<String>(count);
            this.animNames.add(names);
            String fileName = sc.nextLine();
            try {
                Scanner animsScanner = new Scanner(new File(fileDir + "/" + fileName));
                for (int j = 0; j < count && animsScanner.hasNextLine(); ++j) {
                    names.add(animsScanner.nextLine());
                }
                continue;
            }
            catch (Exception e) {
                // empty catch block
            }
        }
    }

    public String getAnimName(int charId, int animId) {
        ArrayList<String> charAnimNames = this.animNames.get(charId);
        if (charAnimNames.size() <= animId) {
            return "Unknown";
        }
        return charAnimNames.get(animId);
    }

    public long getPaletteAddress(int charId) {
        if (this.paletteAddresses == null || charId >= this.paletteAddresses.size()) {
            return 0L;
        }
        return this.paletteAddresses.get(charId);
    }

    public String getPaletteLabel(int charId) {
        if (this.paletteLabels == null || charId >= this.paletteLabels.size()) {
            return "";
        }
        return this.paletteLabels.get(charId);
    }

    public String getCharName(int charId) {
        return this.charNames.get(charId);
    }

    public int getType(int charId) {
        return this.animTypes.get(charId);
    }

    public int getAnimsCount(int charId) {
        return this.animsCount.get(charId);
    }

    public long getAnimsListAddress() {
        return this.animsListAddress;
    }

    public long getHitsListAddress() {
        return this.hitsListAddress;
    }

    public long getWeaponsListAddress() {
        return this.weaponsListAddress;
    }

    public long getPortraitsListAddress() {
        return this.iconsListAddress;
    }

    public int getNumChars() {
        return this.charNames.size();
    }

    public long getCompressedArtAddress(int charId) {
        Long res = this.compressedArtAddresses.get(charId);
        if (res == null) {
            return 0L;
        }
        return res;
    }

    public String getCompressedArtLabel(int charId) {
        String res = this.compressedArtLabels.get(charId);
        if (res == null) {
            return "";
        }
        return res;
    }

    public long getSubArtAddress(int charId) {
        Long res = this.subArtAddresses.get(charId);
        if (res == null) {
            return 0L;
        }
        return res;
    }

    public String getSubArtLabel(int charId) {
        String res = this.subArtLabels.get(charId);
        if (res == null) {
            return "";
        }
        return res;
    }

    public long getNamesListAddress() {
        return this.namesListAddress;
    }

    public long getStatsListAddress() {
        return this.statsListAddress;
    }

    public long getSpeedListAddress() {
        return this.speedListAddress;
    }

    public String getAnimsListLabel() {
        return this.animsListLabel;
    }

    public String getHitsListLabel() {
        return this.hitsListLabel;
    }

    public String getWeaponsListLabel() {
        return this.weaponsListLabel;
    }

    public String getPortraitsListLabel() {
        return this.iconsListLabel;
    }

    public String getNamesListLabel() {
        return this.namesListLabel;
    }

    public String getStatsListLabel() {
        return this.statsListLabel;
    }

    public String getSpeedListLabel() {
        return this.speedListLabel;
    }

    public boolean isGlobalCollisions() {
        return this.globalCollisions;
    }

    public boolean isGlobalWeapons() {
        return this.globalWeapons;
    }

    public int getNumNameLetters() {
        return this.numNameLetters;
    }

    public int getPlayableChars() {
        return this.playableChars;
    }

    public int getPlayableCharsWithSkips() {
        return this.playableChars + this.skips.size();
    }
}

