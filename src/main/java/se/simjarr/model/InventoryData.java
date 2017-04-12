package se.simjarr.model;

import com.google.gson.JsonObject;
import se.simjarr.global.Currency;

import java.util.HashMap;
import java.util.Map;

public class InventoryData {

    private int alteration;
    private int fusing;
    private int alchemy;
    private int chaos;
    private int gemcutter;
    private int exalt;
    private int chromatic;
    private int jeweller;
    private int chance;
    private int chisel;
    private int scouring;
    private int blessed;
    private int regret;
    private int regal;
    private int divine;
    private int vaal;

    public InventoryData(JsonObject jsonStash) {
        alteration = jsonStash.get("alteration").getAsInt();
        fusing = jsonStash.get("fusing").getAsInt();
        alchemy = jsonStash.get("alchemy").getAsInt();
        chaos = jsonStash.get("chaos").getAsInt();
        gemcutter = jsonStash.get("gemcutter").getAsInt();
        exalt = jsonStash.get("exalt").getAsInt();
        chromatic = jsonStash.get("chromatic").getAsInt();
        jeweller = jsonStash.get("jeweller").getAsInt();
        chance = jsonStash.get("chance").getAsInt();
        chisel = jsonStash.get("chisel").getAsInt();
        scouring = jsonStash.get("scouring").getAsInt();
        blessed = jsonStash.get("blessed").getAsInt();
        regret = jsonStash.get("regret").getAsInt();
        regal = jsonStash.get("regal").getAsInt();
        divine = jsonStash.get("divine").getAsInt();
        vaal = jsonStash.get("vaal").getAsInt();
    }

    public InventoryData() {}

    public void merge(InventoryData otherInventory) {
        alteration += otherInventory.getAlteration();
        fusing += otherInventory.getFusing();
        alchemy += otherInventory.getAlchemy();
        chaos += otherInventory.getChaos();
        gemcutter += otherInventory.getGemcutter();
        exalt += otherInventory.getExalt();
        chromatic += otherInventory.getChromatic();
        jeweller += otherInventory.getJeweller();
        chance += otherInventory.getChance();
        chisel += otherInventory.getChisel();
        scouring += otherInventory.getScouring();
        blessed += otherInventory.getBlessed();
        regret += otherInventory.getRegret();
        regal += otherInventory.getRegal();
        divine += otherInventory.getDivine();
        vaal += otherInventory.getVaal();
    }

    public Map<Currency, Integer> toMap() {
        Map<Currency, Integer> inventoryMap = new HashMap<>();
        inventoryMap.put(Currency.ORB_OF_ALTERATION, alteration);
        inventoryMap.put(Currency.ORB_OF_FUSING, fusing);
        inventoryMap.put(Currency.ORB_OF_ALCHEMY, alchemy);
        inventoryMap.put(Currency.CHAOS_ORB, chaos);
        inventoryMap.put(Currency.GEMCUTTERS_PRISM, gemcutter);
        inventoryMap.put(Currency.EXALTED_ORB, exalt);
        inventoryMap.put(Currency.CHROMATIC_ORB, chromatic);
        inventoryMap.put(Currency.JEWELLERS_ORB, jeweller);
        inventoryMap.put(Currency.ORB_OF_CHANCE, chance);
        inventoryMap.put(Currency.CARTOGRAPHERS_CHISEL, chisel);
        inventoryMap.put(Currency.ORB_OF_SCOURING, scouring);
        inventoryMap.put(Currency.BLESSED_ORB, blessed);
        inventoryMap.put(Currency.ORB_OF_REGRET, regret);
        inventoryMap.put(Currency.REGAL_ORB, regal);
        inventoryMap.put(Currency.DIVINE_ORB, divine);
        inventoryMap.put(Currency.VAAL_ORB, vaal);
        return inventoryMap;
    }

    public int getAlteration() {
        return alteration;
    }

    public int getFusing() {
        return fusing;
    }

    public int getAlchemy() {
        return alchemy;
    }

    public int getChaos() {
        return chaos;
    }

    public int getGemcutter() {
        return gemcutter;
    }

    public int getExalt() {
        return exalt;
    }

    public int getChromatic() {
        return chromatic;
    }

    public int getJeweller() {
        return jeweller;
    }

    public int getChance() {
        return chance;
    }

    public int getChisel() {
        return chisel;
    }

    public int getScouring() {
        return scouring;
    }

    public int getBlessed() {
        return blessed;
    }

    public int getRegret() {
        return regret;
    }

    public int getRegal() {
        return regal;
    }

    public int getDivine() {
        return divine;
    }

    public int getVaal() {
        return vaal;
    }
}
