/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.json.JSONObject;

public class HitFrame {

    public int x;
    public int y;
    public int sound;
    public int damage;
    public boolean knockDown;
    private boolean enabled;
    
    public HitFrame()
    {
        // Nothing to do
    }

    public HitFrame(HitFrame hitFrame) {
        x = hitFrame.x;
        y = hitFrame.y;
        sound = hitFrame.sound;
        damage = hitFrame.damage;
        knockDown = hitFrame.knockDown;
        enabled = hitFrame.enabled;
    }

    public void setEnabled(boolean enabledFlag) {
        this.enabled = enabledFlag;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public static HitFrame read(RandomAccessFile rom, long address) throws IOException {
        HitFrame frame = new HitFrame();
        rom.seek(address);
        frame.x = rom.readByte();
        frame.y = rom.readByte();
        frame.damage = rom.readUnsignedByte();
        frame.knockDown = (frame.damage >> 7 & 1) == 1;
        frame.damage &= 127;
        frame.sound = rom.readUnsignedByte();
        frame.enabled = frame.damage > 0;
        return frame;
    }

    public void write(RandomAccessFile rom, long address) throws IOException {
        rom.seek(address);
        HitFrame frame = this.enabled ? this : new HitFrame();
        rom.writeByte(frame.x);
        rom.writeByte(frame.y);
        int damageByte = frame.damage;
        if (frame.knockDown) {
            damageByte += 128;
        }
        rom.writeByte(damageByte);
        rom.writeByte(frame.sound);
    }
    
    public JSONObject toJson(){
        if (!enabled) return null;
        JSONObject res = new JSONObject();
        JSONObject hitBox = new JSONObject();
        hitBox.put("width", x * 2);
        hitBox.put("height", y);
        res.put("hitBox", hitBox);
        res.put("damage", damage);
        res.put("knockDown", knockDown);
        res.put("soundId", sound);
        return res;
    }
    
    static HitFrame fromJson(JSONObject hitJson) {
        HitFrame hitFrame = new HitFrame();
        JSONObject hitBox = hitJson.getJSONObject("hitBox");
        hitFrame.x = hitBox.getInt("width") / 2;
        hitFrame.y = hitBox.getInt("height");
        hitFrame.sound = hitJson.getInt("soundId");
        hitFrame.damage = hitJson.getInt("damage");
        hitFrame.knockDown = hitJson.getBoolean("knockDown");
        hitFrame.enabled = true;
        return hitFrame;
    }
}

