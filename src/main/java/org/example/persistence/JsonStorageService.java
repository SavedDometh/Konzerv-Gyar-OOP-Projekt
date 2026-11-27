package org.example.persistence;

import com.google.gson.*;
import org.example.exception.KonzervGyarException;
import org.example.factory.KonzervFactory;
import org.example.model.*;
import org.example.service.GyarManager;
import org.example.strategy.AutomatikusHianyAlapuStrategia;
import org.example.strategy.GyartasiStrategia;
import org.example.strategy.ManualisGyartasStrategia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonStorageService implements StorageService {

    private final Gson gson;

    public JsonStorageService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Konzerv.class, new KonzervAdapter())
                .registerTypeAdapter(GyartasiStrategia.class, new StrategiaAdapter())
                .create();
    }

    @Override
    public void ment(GyarManager gyarManager, String fajlUtvonal) throws KonzervGyarException {
        if (gyarManager == null) {
            throw new IllegalArgumentException("A mentendő GyarManager nem lehet null!");
        }
        if (fajlUtvonal == null || fajlUtvonal.trim().isEmpty()) {
            throw new IllegalArgumentException("A fájl útvonala nem lehet üres!");
        }

        MentettGyarAllapot allapot = new MentettGyarAllapot(
                gyarManager.getElteltCiklusok(),
                gyarManager.getRaktar().getMaxKapacitas(),
                new ArrayList<>(gyarManager.getRaktar().getKeszlet()),
                new ArrayList<>(gyarManager.getGyartosorok())
        );

        Path path = Path.of(fajlUtvonal);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(allapot, writer);
        } catch (IOException e) {
            throw new KonzervGyarException("Hiba történt a JSON fájl mentése közben: " + e.getMessage());
        }
    }

    @Override
    public GyarManager betolt(String fajlUtvonal) throws KonzervGyarException {
        if (fajlUtvonal == null || fajlUtvonal.trim().isEmpty()) {
            throw new IllegalArgumentException("A fájl útvonala nem lehet üres!");
        }

        Path path = Path.of(fajlUtvonal);
        if (!Files.exists(path)) {
            throw new KonzervGyarException("A megadott mentési fájl nem található: " + fajlUtvonal);
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            MentettGyarAllapot allapot = gson.fromJson(reader, MentettGyarAllapot.class);
            if (allapot == null) {
                throw new KonzervGyarException("A JSON fájl üres vagy sérült!");
            }

            Raktar raktar = new Raktar(allapot.raktarMaxKapacitas);
            if (allapot.keszlet != null) {
                for (Konzerv k : allapot.keszlet) {
                    raktar.raktarozas(k);
                }
            }

            return new GyarManager(raktar, allapot.gyartosorok, allapot.elteltCiklusok);
        } catch (IOException | JsonParseException e) {
            throw new KonzervGyarException("Hiba történt a JSON fájl betöltése közben: " + e.getMessage());
        }
    }

    private static class MentettGyarAllapot {
        int elteltCiklusok;
        int raktarMaxKapacitas;
        List<Konzerv> keszlet;
        List<Gyartosor> gyartosorok;

        MentettGyarAllapot(int elteltCiklusok, int raktarMaxKapacitas, List<Konzerv> keszlet, List<Gyartosor> gyartosorok) {
            this.elteltCiklusok = elteltCiklusok;
            this.raktarMaxKapacitas = raktarMaxKapacitas;
            this.keszlet = keszlet;
            this.gyartosorok = gyartosorok;
        }
    }

    // --- Adapterek a LocalDate és a polimorf típusok kezeléséhez ---

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString());
        }
    }

    private static class KonzervAdapter implements JsonSerializer<Konzerv>, JsonDeserializer<Konzerv> {
        @Override
        public JsonElement serialize(Konzerv src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", src.getId());
            obj.addProperty("tipus", src.getTipus().name());
            obj.addProperty("nettoGramm", src.getNettoGramm());
            obj.addProperty("createdAt", src.getCreatedAt().toString());

            if (src instanceof ZoldsegKonzerv zk) {
                obj.addProperty("vegetableType", zk.getVegetableType());
                obj.addProperty("bio", zk.isBio());
            } else if (src instanceof HusKonzerv hk) {
                obj.addProperty("meatType", hk.getMeatType());
            }
            return obj;
        }

        @Override
        public Konzerv deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            int id = obj.get("id").getAsInt();
            KonzervTipus tipus = KonzervTipus.valueOf(obj.get("tipus").getAsString());
            int nettoGramm = obj.get("nettoGramm").getAsInt();
            LocalDate createdAt = LocalDate.parse(obj.get("createdAt").getAsString());

            KonzervFactory.syncNextId(id);

            if (tipus.isZoldseg()) {
                String vegetableType = obj.get("vegetableType").getAsString();
                boolean bio = obj.get("bio").getAsBoolean();
                return new ZoldsegKonzerv(id, tipus, nettoGramm, vegetableType, createdAt, bio);
            } else {
                String meatType = obj.get("meatType").getAsString();
                return new HusKonzerv(id, tipus, nettoGramm, meatType, createdAt);
            }
        }
    }

    private static class StrategiaAdapter implements JsonSerializer<GyartasiStrategia>, JsonDeserializer<GyartasiStrategia> {
        @Override
        public JsonElement serialize(GyartasiStrategia src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            if (src instanceof ManualisGyartasStrategia manualis) {
                obj.addProperty("mod", "MANUALIS");
                obj.addProperty("celTipus", manualis.getCelTipus().name());
            } else {
                obj.addProperty("mod", "AUTOMATIKUS");
            }
            return obj;
        }

        @Override
        public GyartasiStrategia deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String mod = obj.get("mod").getAsString();

            if ("MANUALIS".equals(mod)) {
                KonzervTipus celTipus = KonzervTipus.valueOf(obj.get("celTipus").getAsString());
                return new ManualisGyartasStrategia(celTipus);
            }
            return new AutomatikusHianyAlapuStrategia();
        }
    }
}