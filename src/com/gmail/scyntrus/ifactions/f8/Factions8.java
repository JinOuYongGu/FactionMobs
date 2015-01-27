package com.gmail.scyntrus.ifactions.f8;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.scyntrus.fmob.Utils;
import com.gmail.scyntrus.ifactions.Faction;
import com.gmail.scyntrus.ifactions.Factions;
import com.gmail.scyntrus.ifactions.Rank;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.struct.FFlag;
import com.massivecraft.factions.struct.Rel;

public class Factions8 implements Factions {

    private static Factions8 instance;
    private Factions8() {
    }
    public static Factions get() {
        if (instance == null) {
            instance = new Factions8();
        }
        return instance;
    }

    private com.massivecraft.factions.Factions factionsInstance;
    private Method getByTagMethod;
    private Method fPlayerGetRoleMethod;

    @Override
    public boolean init() {
        try {
            Field factionsInstanceField = com.massivecraft.factions.Factions.class.getDeclaredField("i");
            factionsInstanceField.setAccessible(true);
            factionsInstance = (com.massivecraft.factions.Factions) factionsInstanceField.get(null);
            getByTagMethod = com.massivecraft.factions.Factions.class.getMethod("getByTag", new Class<?>[]{String.class});
            fPlayerGetRoleMethod = FPlayer.class.getMethod("getRole");
            Faction8.getRelationTo = com.massivecraft.factions.Faction.class.getMethod("getRelationTo", new Class<?>[]{RelationParticipator.class});
            Faction8.getFlag = com.massivecraft.factions.Faction.class.getMethod("getFlag", new Class<?>[]{FFlag.class});
            return true;
        } catch (Exception e) {
            Utils.handleError(e);
        }
        return false;
    }

    @Override
    public Faction getFactionAt(Location loc) {
        return new Faction8(Board.getFactionAt(new FLocation(loc)));
    }

    @Override
    public Faction getFactionByName(String name) {
        try {
            return new Faction8(getByTagMethod.invoke(factionsInstance, name));
        } catch (Exception e) {
            Utils.handleError(e);
        }
        return null;
    }

    @Override
    public Faction getPlayerFaction(Player player) {
        return new Faction8(FPlayers.i.get(player).getFaction());
    }

    @Override
    public Faction getFactionFromNativeObject(Object nativeObject) {
        return new Faction8(nativeObject);
    }

    @Override
    public Rank getPlayerRank(Player player) {
        try {
            Rel role6 = (Rel) fPlayerGetRoleMethod.invoke(FPlayers.i.get(player));
            return Rank.getByName(role6.name());
        } catch (Exception e) {
            Utils.handleError(e);
        }
        return Rank.MEMBER;
    }

}
