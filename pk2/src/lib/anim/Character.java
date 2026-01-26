/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import lib.Rom;
import main.Gui;
import org.json.JSONArray;
import org.json.JSONObject;

public class Character {
    public static final int FIRST_HIT_ANIM = 24;
    private ArrayList<Animation> animations;
    private ArrayList<HitFramesSet> animHits;
    private ArrayList<WeaponFramesSet> animWeapons;
    private boolean modified;
    private boolean spritesModified;
    
    private class ImagePivotPair
    {
        public BufferedImage image;
        public Point pivot;
    }
    
    private Comparator<ImagePivotPair> imageComparator = new Comparator<ImagePivotPair>() {
    @Override
    public int compare(ImagePivotPair img1Pair, ImagePivotPair img2Pair) {
        if (img1Pair.pivot.x != img2Pair.pivot.x)
            return img1Pair.pivot.x - img2Pair.pivot.x;
        if (img1Pair.pivot.y != img2Pair.pivot.y)
            return img1Pair.pivot.y - img2Pair.pivot.y;
        BufferedImage img1 = img1Pair.image;
        BufferedImage img2 = img2Pair.image;
        if (img1 == null && img2 == null)
            return 0;
        if (img1 == null)
            return -1;
        if (img2 == null)
            return 1;
        // Compare the widths and heights of the images
        int widthDiff = img1.getWidth() - img2.getWidth();
        if (widthDiff != 0) {
            return widthDiff;
        }
        int heightDiff = img1.getHeight() - img2.getHeight();
        if (heightDiff != 0) {
            return heightDiff;
        }
        // If the widths and heights are the same, compare the images pixel by pixel
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                if (rgb1 != rgb2) {
                    return rgb1 - rgb2;
                }
            }
        }
        // If the images are identical, return 0
        return 0;
    }
};

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void setSpritesModified(boolean modified) {
        this.spritesModified = modified;
    }

    public boolean wasModified() {
        return this.modified;
    }

    public Character(int numAnimations) {
        this.animations = new ArrayList(numAnimations);
        int numHitFrames = numAnimations;
        this.animHits = new ArrayList(numHitFrames);
        this.animWeapons = new ArrayList(numAnimations);
    }

    public Animation getAnimation(int index) {
        return this.animations.get(index);
    }

    public AnimFrame getAnimFrame(int animId, int frameId) {
        if (animId < 0 || animId >= animations.size())
            return null;
        Animation animation = animations.get(animId);
        if (frameId < 0 || frameId >= animation.getNumFrames())
            return null;
        return animation.getFrame(frameId);
    }

    public HitFrame getHitFrame(int animId, int frameId) {
        int index = animId;
        if (index < 0 || index >= this.animHits.size()) {
            return null;
        }
        HitFramesSet hits = this.animHits.get(index);
        if (hits == null) {
            return null;
        }
        ArrayList<HitFrame> frames = hits.frames;
        if (frames == null || frames.size() <= frameId) {
            return null;
        }
        return frames.get(frameId);
    }
    
    public void setWeaponFrame(int animId, int frameId, WeaponFrame weaponFrame)
    {
        if (animId >= this.animWeapons.size()) {
            return;
        }
        WeaponFramesSet weapon = this.animWeapons.get(animId);
        if (weapon == null) {
            return;
        }
        ArrayList<WeaponFrame> frames = weapon.frames;
        if (frames == null || frameId >= frames.size()) {
            return;
        }
        frames.set(frameId, new WeaponFrame(weaponFrame));
    }
    
    public void setHitFrame(int animId, int frameId, HitFrame hitFrame)
    {
        if (animId < 0 || animId >= this.animHits.size()) {
            return;
        }
        HitFramesSet hits = this.animHits.get(animId);
        if (hits == null) {
            return;
        }
        ArrayList<HitFrame> frames = hits.frames;
        if (frames == null || frames.size() <= frameId) {
            return;
        }
        frames.set(frameId, new HitFrame(hitFrame));
    }

    public WeaponFrame getWeaponFrame(int animId, int frameId) {
        if (animId >= this.animWeapons.size()) {
            return null;
        }
        WeaponFramesSet weapon = this.animWeapons.get(animId);
        if (weapon == null) {
            return null;
        }
        ArrayList<WeaponFrame> frames = weapon.frames;
        if (frames == null || frameId >= frames.size()) {
            return null;
        }
        return frames.get(frameId);
    }

    public int getNumAnimations() {
        return this.animations.size();
    }

    public static Character read(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, int count, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        return Character.read(rom, animListAddress, hitsListAddress, weaponsListAddress, id, count, -1, globalCol, globalWeap, numPlayableChars);
    }

    public static Character read(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int charId, int count, int animsType, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        short offset;
        Character c = new Character(count);
      
        TreeMap<Long, Integer> animIds = null;
        
        rom.seek(animListAddress + (long)(charId * 4));
        long address = rom.readInt();
        for (int i = 0; i < count; ++i) {
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            localAddress += (long)offset;
            Animation anim;
            anim = Animation.read(rom, localAddress, animsType);
            
            c.animations.add(anim);
        }
        
        if (charId > 25 && charId > numPlayableChars) {
            return c;
        }
        if (globalCol) {
            rom.seek(hitsListAddress + (long)(charId * 4));
            address = rom.readInt();
        } else {
            rom.seek(hitsListAddress + (long)(charId * 2));
            offset = rom.readShort();
            address = hitsListAddress + (long)(charId * 2) + (long)offset;
        }
        for (int i = 0; i < FIRST_HIT_ANIM; ++i)
        {
            c.animHits.add(new HitFramesSet(c.animations.get(i).getNumFrames()));
        }
        for (int i = 0; i < count - FIRST_HIT_ANIM; ++i) {
            HitFramesSet set;
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset != 0) {
                localAddress += (long)offset;
                
                int framesCount = c.animations.get(i + FIRST_HIT_ANIM).getNumFrames();
                set = HitFramesSet.read(rom, localAddress, framesCount, new TreeMap<Long, HitFrame>());                
                c.animHits.add(set);
            } else {
                set = new HitFramesSet(0);
                c.animHits.add(set);
            }
            // Have no idea what the following line meant to be, just know that it's broken
            //if (charId == 24 && numPlayableChars < 24 || charId == numPlayableChars - 1) break;
        }
        
        if (globalWeap) {
            rom.seek(weaponsListAddress + (long)(charId * 4));
            address = rom.readInt();
        } else {
            rom.seek(weaponsListAddress + (long)(charId * 2));
            offset = rom.readShort();
            address = weaponsListAddress + (long)(charId * 2) + (long)offset;
        }
        for (int i = 0; i < count; ++i) {
            WeaponFramesSet set;
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset != 0) {
                localAddress += (long)offset;
                int framesCount = c.animations.get(i).getNumFrames();
                set = WeaponFramesSet.read(rom, localAddress, framesCount, new TreeMap<Long, WeaponFrame>());
                c.animWeapons.add(set);
                continue;
            }
            set = new WeaponFramesSet(0);
            c.animWeapons.add(set);
        }
        return c;
    }

    public int getAnimsSize(int animsType) {
        int numAnims = this.animations.size();
        int totalSize = 0;
        for (Animation a : this.animations) {
            int totalFrames = a.getNumFrames();
            totalSize += totalFrames * a.getSizeInBytes();
        }
        return numAnims * 4 + totalSize;
    }

    public void write(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        
    }

    public void writeNewAnimations(RandomAccessFile rom, long animListAddress, long newAnimsAddress, int id, int type) throws IOException {
        
    }

    public void writeNewScripts(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, int type, boolean globalCol, boolean globalWeap, long newAddress, boolean newHits, boolean newWeapons) throws IOException {
        
    }

    public boolean wasSpritesModified() {
        return this.spritesModified;
    }

    public Animation doubleAnim(int i) {
        Animation oldAnim = this.animations.get(i);
        Animation anim = new Animation(1, 0);
        anim.addFrame(oldAnim.getFrame(0));
        this.animations.set(i, anim);
        this.animWeapons.set(i, new WeaponFramesSet(0));
        if (i >= FIRST_HIT_ANIM && this.animHits.get((int)(i - FIRST_HIT_ANIM)).frames.size() > 0) {
            HitFramesSet set = new HitFramesSet(1);
            set.frames.add(this.animHits.get((int)(i - FIRST_HIT_ANIM)).frames.get(0));
            this.animHits.set(i - FIRST_HIT_ANIM, set);
        }
        return anim;
    }

    public int getHitsSize() {
        int res = 0;
        HashSet<HitFramesSet> processed = new HashSet<HitFramesSet>();
        HashSet<HitFrame> processedHits = new HashSet<HitFrame>();
        for (HitFramesSet set : this.animHits) {
            if (processed.contains(set)) continue;
            res += set.countFrames(processedHits);
            processed.add(set);
        }
        return res;
    }

    public int getWeaponsSize() {
        int res = 0;
        HashSet<WeaponFramesSet> processed = new HashSet<WeaponFramesSet>();
        HashSet<WeaponFrame> processedWeapons = new HashSet<WeaponFrame>();
        for (WeaponFramesSet set : this.animWeapons) {
            if (processed.contains(set)) continue;
            res += set.countFrames(processedWeapons);
            processed.add(set);
        }
        return res;
    }

    public void setAnim(int id1, int id2, boolean setWp, boolean setHit) {
        this.animations.set(id1, this.animations.get(id2));
        if (setWp) {
            this.animWeapons.set(id1, this.animWeapons.get(id2));
        } else {
            this.setWp(id1, !this.animWeapons.get((int)id1).frames.isEmpty(), this.animations.get(id1).getNumFrames());
        }
        if (id1 >= FIRST_HIT_ANIM && id2 >= FIRST_HIT_ANIM) {
            int d1 = id1 - FIRST_HIT_ANIM;
            int d2 = id2 - FIRST_HIT_ANIM;
            if (setHit) {
                this.animHits.set(d1, this.animHits.get(d2));
            } else {
                this.setHit(d1, !this.animHits.get((int)d1).frames.isEmpty(), this.animations.get(id1).getNumFrames());
            }
        }
    }

    private void setWp(int id, boolean hasWp, int size) {
        if (!hasWp) {
            this.animWeapons.set(id, new WeaponFramesSet(0));
        } else {
            WeaponFramesSet set = this.animWeapons.get(id);
            while (set.frames.size() < size) {
                set.frames.add(new WeaponFrame());
            }
        }
    }

    private void setHit(int id, boolean hasHit, int size) {
        if (!hasHit) {
            this.animHits.set(id, new HitFramesSet(0));
        } else {
            HitFramesSet set = this.animHits.get(id);
            while (set.frames.size() < size) {
                set.frames.add(new HitFrame());
            }
        }
    }

    public void resizeAnim(int i, int size, boolean hasWp, boolean hasHit) {        
        this.animations.get(i).resize(size);
        this.setHit(i, hasHit, size);
        this.setWp(i, hasWp, size);
        setModified(true);
    }
    
    
    public JSONObject toLogicJson(int characterId, String romName) {
        JSONObject logicJson = new JSONObject();
        JSONArray jsonAnimationsLogic = new JSONArray();

        for (int animId = 0; animId < animations.size(); ++animId) {
            Animation animation = animations.get(animId);
            JSONObject animLogic = new JSONObject();

            int totalDuration = 0;
            JSONArray hitboxes = new JSONArray();

            for (int frameId = 0; frameId < animation.getNumFrames(); ++frameId) {
                AnimFrame frame = animation.getFrame(frameId);
                HitFrame hit = getHitFrame(animId, frameId);

                if (hit != null && hit.isEnabled()) {
                    hitboxes.put(hit.toJson(totalDuration, frame.delay));
                }
                totalDuration += frame.delay;
            }

            animLogic.put("duration", totalDuration);
            if (hitboxes.length() > 0) animLogic.put("hitboxes", hitboxes);

            // This handles the SEGA ROM throw data extraction
            addThrowLogic(animLogic, characterId, animId, romName);

            jsonAnimationsLogic.put(animLogic);
        }

        logicJson.put("animationsLogic", jsonAnimationsLogic);
        return logicJson;
    }

    public JSONObject toAnimationsJson() {
        JSONArray jsonAnims = new JSONArray();
        for (int animId = 0; animId < animations.size(); ++animId) {
            Animation anim = animations.get(animId);
            JSONObject animObj = new JSONObject();
            JSONArray framesArray = new JSONArray();

            for (int f = 0; f < anim.getNumFrames(); f++) {
                AnimFrame frame = anim.getFrame(f);
                JSONObject fObj = new JSONObject();
                fObj.put("duration", frame.delay);
                fObj.put("imageId", anim.bufferedFrameIndexes.get(f));

                WeaponFrame wf = getWeaponFrame(animId, f);
                if (wf != null && wf.isEnabled()) fObj.put("weapon", wf.toJson());

                framesArray.put(fObj);
            }
            animObj.put("name", Gui.instance.getAnimName(0, animId)); // Adjust ID as needed
            animObj.put("frames", framesArray);
            jsonAnims.put(animObj);
        }
        JSONObject res = new JSONObject();
        res.put("animations", jsonAnims);
        return res;
    }
    
    
    private boolean isFrontThrow(int characterId, int animId)
    {
        // #D460
        // Max, Axel, Blaze, Skate, Barbon, Signal, Ninja, Abadede (there are others? D4F8 onwards)
        return characterId <= 2 && animId == 24
            || characterId == 3 && animId == 41 // Skate vault throw
            || characterId == 7 && animId == 28 // Barbon
            || characterId == 9 && animId == 25 // Signal
            || characterId == 11 && animId == 24 // Ninja
            || characterId == 18 && animId == 24 // Abadede
        ;
    }
    
    private boolean isBackThrow(int characterId, int animId)
    {
        // #D558
        // Max, Axel, Blaze, Zamza, Abadede, Shiva
        return characterId <= 2 && animId == 25
            || characterId == 16 && animId == 24 // Zamza
            || characterId == 18 && animId == 34 // Abadede
            || characterId == 21 && animId == 32 // Shiva
        ;
    }
    
    private void addThrowLogic(JSONObject animationLogicJson, int characterId, int animId, String romName)
    {
        int throwPositionsAddress = -1;
        int throwReleaseAddress = -1;
        int opponentAnimationId = 0;
        // Blaze front slam:
        if (characterId == 2 && animId == 39) {
            throwPositionsAddress = 0xD1D6;
            throwReleaseAddress = 0xD558 + characterId * 8;
            opponentAnimationId = 21;
        }
        // Max air throw
        else if (characterId == 0 && animId == 40) {
            throwPositionsAddress = 0xD1DA;
            throwReleaseAddress = 0xD550;
            opponentAnimationId = 22;
        }
        // front throws
        else if (isFrontThrow(characterId, animId)){
            throwPositionsAddress = 0xD1DE + characterId * 4;
            throwReleaseAddress = 0xD460 + characterId * 8;
            opponentAnimationId = 18;
        }
        // back slams
        else if (isBackThrow(characterId, animId)){
            throwPositionsAddress = 0xD3E8 + characterId * 4;
            throwReleaseAddress = 0xD558 + characterId * 8;
            opponentAnimationId = characterId == 1 ? 19 : 20;
        }
        if (throwPositionsAddress > 0) {
            Rom rom = null;
            try {
                rom = new Rom(new File(romName));
                // Position frames data
                rom.rom.seek(throwPositionsAddress);
                int framesDataAddress = throwPositionsAddress + rom.rom.readShort();
                int releaseFrame = rom.rom.readShort() >> 3;
                Animation thrownAnimation = animations.get(opponentAnimationId);
                int totalFrames = thrownAnimation.getNumFrames();
                int startingFrame = 0;
                JSONArray offsetsJson = new JSONArray();
                for (int i = totalFrames; i >= releaseFrame; --i)
                {
                    rom.rom.seek(framesDataAddress + i*8);
                    int dx = rom.rom.readShort();
                    rom.rom.skipBytes(2);
                    int dy = rom.rom.readShort();
                    JSONObject offsetJson = new JSONObject();
                    offsetJson.put("frame", startingFrame);
                    JSONObject frameOffsetJson = new JSONObject();
                    frameOffsetJson.put("x", dx);
                    frameOffsetJson.put("y", dy);
                    offsetJson.put("offset", frameOffsetJson);
                    offsetsJson.put(offsetJson);
                    startingFrame += thrownAnimation.getFrame(totalFrames - i).delay;
                }
                // Release data
                rom.rom.seek(throwReleaseAddress);
                int velY = rom.rom.readInt();
                int velX = rom.rom.readShort();
                int damage = rom.rom.readShort();
                JSONObject throwDataJson = new JSONObject();
                JSONObject releaseVelocityJson = new JSONObject();
                releaseVelocityJson.put("x", velX);
                releaseVelocityJson.put("y", velY);
                throwDataJson.put("offsets", offsetsJson);
                throwDataJson.put("releaseVelocity", releaseVelocityJson);
                throwDataJson.put("releaseDamage", damage);
                animationLogicJson.put("throwLogic", throwDataJson);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            } finally
            {
                if (rom != null)
                {
                    try {
                        rom.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    
    
    
    private void clonePointers(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length() ; ++i)
        {
            Object obj = jsonArray.get(i);
            if (obj instanceof Integer) {
                do {
                    obj = jsonArray.get((Integer)obj);
                } while (obj instanceof Integer);
                jsonArray.put(i, obj);
            }
        }
    }

    public static Character fromJson(JSONObject logicJson, JSONObject animsJson) {
        JSONArray animsArray = animsJson.getJSONArray("animations");
        JSONArray logicArray = logicJson.getJSONArray("animationsLogic");

        Character character = new Character(animsArray.length());

        for (int i = 0; i < animsArray.length(); i++) {
            JSONObject animArt = animsArray.getJSONObject(i);
            JSONObject animLog = logicArray.optJSONObject(i);
            JSONArray framesArt = animArt.getJSONArray("frames");

            Animation animation = new Animation(framesArt.length(), 0);
            HitFramesSet hSet = new HitFramesSet(framesArt.length());

            int currentTime = 0;
            for (int f = 0; f < framesArt.length(); f++) {
                hSet.frames.add(new HitFrame());
                JSONObject fArt = framesArt.getJSONObject(f);
                AnimFrame frame = new AnimFrame();
                frame.delay = fArt.getInt("duration");
                animation.bufferedFrameIndexes.add(fArt.optInt("imageId", 0));
                animation.addFrame(frame);

                // Look for hitboxes starting at this timestamp
                JSONArray hitboxes = animLog != null ? animLog.optJSONArray("hitboxes") : null;
                if (hitboxes != null) {
                    for (int h = 0; h < hitboxes.length(); h++) {
                        JSONObject hJson = hitboxes.getJSONObject(h);
                        if (hJson.optInt("frame", 0) == currentTime) {
                            hSet.frames.set(f, HitFrame.fromJson(hJson));
                        }
                    }
                }
                currentTime += frame.delay;
            }
            character.animations.add(animation);
            character.animHits.add(hSet);
        }
        return character;
    }
}

