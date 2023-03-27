/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

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
import org.json.JSONArray;
import org.json.JSONObject;

public class Character {
    public static final int FIRST_HIT_ANIM = 24;
    private ArrayList<Animation> animations;
    private ArrayList<HitFramesSet> animHits;
    private ArrayList<WeaponFramesSet> animWeapons;
    private boolean modified;
    private boolean spritesModified;
    
    private Comparator<BufferedImage> imageComparator = new Comparator<BufferedImage>() {
    @Override
    public int compare(BufferedImage img1, BufferedImage img2) {
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
    
    
    public JSONObject toJson(int characterId, String romName){
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonAnims = new JSONArray();
        JSONArray jsonAnimationsLogic = new JSONArray();
        TreeMap<BufferedImage, Integer> maps = new TreeMap<BufferedImage, Integer>(imageComparator);
        HashMap<Animation, Integer> processed = new HashMap<Animation, Integer>();
        int artFrame = 0;
        for (int animId = 0; animId < animations.size(); ++animId){
            JSONArray jsonFrames = new JSONArray();
            JSONArray jsonHits = new JSONArray();
            Animation animation = animations.get(animId);
            Integer framePointer = processed.get(animation);
            if (framePointer != null) {
                // Animation pointer
                jsonAnims.put(framePointer);
                jsonAnimationsLogic.put(framePointer);
                continue;
            }
            processed.put(animation, animId);
            int numFrames = animation.getNumFrames();
            int screenFramesCount = 0;
            animation.bufferedFrameIndexes = new ArrayList<Integer>();
            for (int frameId = 0; frameId < numFrames; ++frameId) {
                AnimFrame animFrame = animation.getFrame(frameId);
                HitFrame hitFrame = getHitFrame(animId, frameId);
                WeaponFrame weapFrame = getWeaponFrame(animId, frameId);
                BufferedImage image = animation.getImage(frameId);
                framePointer = maps.get(image);
                if (framePointer == null) {
                    // Frame pointer
                    framePointer = artFrame++;
                    maps.put(image, framePointer);
                }
                JSONObject jsonFrame = animFrame.toJson(framePointer);
                animation.bufferedFrameIndexes.add(framePointer);
                if (hitFrame != null) {
                    JSONObject obj = hitFrame.toJson();                    
                    if (obj != null) {
                        obj.put("frame", screenFramesCount);
                        obj.put("duration", animFrame.delay);
                        jsonHits.put(obj);
                    }                    
                }
                if (weapFrame != null) {
                    JSONObject obj = weapFrame.toJson();
                    if (obj != null) jsonFrame.put("weapon", obj);
                }
                jsonFrames.put(jsonFrame);
                screenFramesCount += animFrame.delay;
                
            }
            JSONObject framesObject = new JSONObject();
            framesObject.put("frames", jsonFrames);
            jsonAnims.put(framesObject);
            JSONObject animationLogicJson = new JSONObject();
            animationLogicJson.put("duration", screenFramesCount);
            if (jsonHits.length() > 0) {
                animationLogicJson.put("hitboxes", jsonHits);
            }
            addThrowLogic(animationLogicJson, characterId, animId, romName);
            jsonAnimationsLogic.put(animationLogicJson);
        }
        clonePointers(jsonAnims);
        clonePointers(jsonAnimationsLogic);
        // Max back slam is a clone of front slam, but throw data is different. This should be done differently, but let's just get it done quick
        if (characterId == 0) {
            JSONObject animationLogicJson = (JSONObject) jsonAnimationsLogic.get(25);
            animationLogicJson = new JSONObject(animationLogicJson.toString());
            addThrowLogic(animationLogicJson, characterId, 25, romName);
            jsonAnimationsLogic.put(25, animationLogicJson);
        }
        
        jsonObj.put("animations", jsonAnims);
        jsonObj.put("animationsLogic", jsonAnimationsLogic);
        return jsonObj;
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

    public static Character fromJson(JSONObject jsonCharacter) {
        JSONArray jsonAnims = (JSONArray) jsonCharacter.get("animations");
        JSONArray jsonAnimationsLogic = (JSONArray) jsonCharacter.get("animationsLogic");
        
        Character character = new Character(jsonAnims.length());
        ArrayList<Animation> animations = character.animations;
        ArrayList<HitFramesSet> animHits = character.animHits;
        ArrayList<WeaponFramesSet> animWeapons = character.animWeapons;
        
        for (int animId = 0; animId < jsonAnims.length(); ++animId){
            JSONObject framesObject = jsonAnims.getJSONObject(animId);
            JSONArray jsonFrames = (JSONArray)framesObject.get("frames");
            JSONObject animationLogicJson = jsonAnimationsLogic.getJSONObject(animId);
            JSONArray jsonHits = animationLogicJson.optJSONArray("hitboxes");
            
            int numFrames = jsonFrames.length();
            Animation animation = new Animation(numFrames, 0);
            HitFramesSet hitFrames = new HitFramesSet(numFrames);
            WeaponFramesSet weaponFrames = new WeaponFramesSet(numFrames);
            
            int screenFramesCount = 0;
            int currentHitIndex = 0;
            
            for (int frameId = 0; frameId < numFrames; ++frameId) {
                JSONObject frameJson = jsonFrames.getJSONObject(frameId);
                        
                int duration = frameJson.getInt("duration");
                animation.bufferedFrameIndexes.add(frameJson.getInt("imageId"));
                
                AnimFrame animFrame = new AnimFrame();
                animation.addFrame(animFrame);
                animFrame.delay = duration;
                HitFrame hitFrame = new HitFrame();
                if (jsonHits != null && currentHitIndex < jsonHits.length())
                {
                    JSONObject currentHit = jsonHits.getJSONObject(currentHitIndex);
                    if (currentHit.getInt("frame") == screenFramesCount)
                    {
                        hitFrame = HitFrame.fromJson(currentHit);
                        ++currentHitIndex;
                    }
                }
                WeaponFrame weapFrame = new WeaponFrame();
                JSONObject weaponJson = frameJson.optJSONObject("weapon");
                if (weaponJson != null)
                {
                    weapFrame = WeaponFrame.fromJson(weaponJson);
                }
                
                hitFrames.frames.add(hitFrame);
                weaponFrames.frames.add(weapFrame);
                screenFramesCount += duration;
            }
            animations.add(animation);
            animHits.add(hitFrames);
            animWeapons.add(weaponFrames);
        }
        return character;
    }
    
}

