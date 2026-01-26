/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.json.JSONObject;

public class WeaponFrame {
    public int x;
    public int y;
    public int angle;
    public boolean showBehind;
    public int unusedByte;
    private boolean isEnabled;

    public WeaponFrame()
    {
        // Nothing to do
    }
    
    public WeaponFrame(WeaponFrame weaponFrame) {
        x = weaponFrame.x;
        y = weaponFrame.y;
        angle = weaponFrame.angle;
        showBehind = weaponFrame.showBehind;
        unusedByte = weaponFrame.unusedByte;
        isEnabled = weaponFrame.isEnabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public static WeaponFrame read(RandomAccessFile rom, long address) throws IOException {
        WeaponFrame frame = new WeaponFrame();
        rom.seek(address);
        frame.x = rom.readByte();
        frame.y = rom.readByte();
        frame.angle = rom.readByte();
        frame.unusedByte = rom.readByte();
        frame.isEnabled = frame.angle >= 0;
        frame.showBehind = frame.angle > 7;
        frame.angle %= 8;
        if (!frame.isEnabled) {
            frame.angle = 0;
        }
        return frame;
    }

    public void write(RandomAccessFile rom, long address) throws IOException {
        WeaponFrame frame;
        rom.seek(address);
        if (this.isEnabled) {
            frame = this;
        } else {
            frame = new WeaponFrame();
            frame.angle = -1;
        }
        rom.writeByte(frame.x);
        rom.writeByte(frame.y);
        if (frame.showBehind) {
            rom.writeByte(frame.angle + 8);
        } else {
            rom.writeByte(frame.angle);
        }
        rom.writeByte(frame.unusedByte);
    }
    
    public JSONObject toJson() {
        if (!isEnabled) return null;
        JSONObject res = new JSONObject();
        JSONObject pos = new JSONObject();
        pos.put("x", x);
        pos.put("y", y);
        res.put("position", pos);
        res.put("direction", angle); // C# expects 'direction' (enum int)
        res.put("showBehind", showBehind);
        return res;
    }
    
    static WeaponFrame fromJson(JSONObject weaponJson) {
        WeaponFrame weapon = new WeaponFrame();
        JSONObject positionJson = weaponJson.optJSONObject("position");
        if (positionJson != null)
        {
            weapon.x = positionJson.optInt("x", 0);
            weapon.y = positionJson.optInt("y", 0);
        }
        weapon.angle = weaponJson.optInt("direction", 0);
        weapon.showBehind = weaponJson.optBoolean("showBehind", false);
        weapon.isEnabled = true;
        return weapon;
    }
}

