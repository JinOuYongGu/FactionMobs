package com.gmail.scyntrus.ifactions.f2;

import com.gmail.scyntrus.fmob.FactionMob;
import com.gmail.scyntrus.fmob.FactionMobs;
import com.gmail.scyntrus.ifactions.Faction;
import com.gmail.scyntrus.ifactions.FactionsManager;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsNameChange;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Iterator;

public class FactionListener2 implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFactionRename(EventFactionsNameChange e) {
        String oldName = e.getFaction().getName();
        String newName = e.getNewName();
        FactionMobs.factionColors.put(newName,
                FactionMobs.factionColors.containsKey(oldName) ?
                        FactionMobs.factionColors.remove(oldName) :
                        10511680);
        FactionMobs.instance.getServer().getScheduler().runTaskLater(FactionMobs.instance, new Runnable() {

            String oldName;
            String newName;

            public Runnable init(String oldName, String newName) {
                this.oldName = oldName;
                this.newName = newName;
                return this;
            }

            @Override
            public void run() {
                Faction faction = FactionsManager.getFactionByName(newName);
                if (faction == null || faction.isNone()) return;
                for (FactionMob fmob : FactionMobs.mobList) {
                    if (fmob.getFactionName().equals(oldName)) {
                        fmob.setFaction(faction);
                    }
                }
            }
        }.init(oldName, newName), 0);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFactionDisband(EventFactionsDisband e) {
        String factionName = e.getFaction().getName();
        for (Iterator<FactionMob> it = FactionMobs.mobList.iterator(); it.hasNext(); ) {
            FactionMob mob = it.next();
            if (mob.getFactionName().equals(factionName)) {
                mob.forceDie();
                it.remove();
            }
        }
    }
}
