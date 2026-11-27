# Konzervgyár – Termelés- és Raktárirányító Szimuláció

Objektumorientált Java konzolos alkalmazás, egy konzervgyár működését, gyártósorainak vezérlését és raktárkészlet-gazdálkodását szimulálja.

---

## Főbb funkciók

* **Több gyártósor párhuzamos kezelése:** Különálló gyártósor-példányok egyedi azonosítóval, állítható ciklussebességgel ($\text{db/ciklus}$) és üzemállapottal (aktív / leállított).
* **Dinamikus gyártási stratégiák:** Futásidőben váltható működési módok soronként:
  * *Manuális mód:* A kezelő által kiválasztott konzervtípus folyamatos gyártása.
  * *Automatikus (hiányalapú) mód:* A raktárkészlet valós idejű elemzése alapján mindig a leghiányosabb termék pótlása.
* **Idő- és műszakszimuláció:** Termelés léptetése egyetlen ciklussal, tetszőleges ciklusszámmal, vagy egy teljes munkanap szimulálásával ($1\text{ nap} = 8\text{ ciklus}$).
* **Raktárkezelés és FEFO kiszállítás:** Maximális raktárkapacitás figyelése, típusonkénti készletnyilvántartás, valamint kiszállításnál a leghamarabb lejáró tételek előnyben részesítése (*First-Expired, First-Out*).
* **Szavatosság-ellenőrzés és selejtezés:** Automatikus lejárati dátum számítás termékkategóriánként (zöldségkonzerv: $+3\text{ év}$, húskonzerv: $+5\text{ év}$) és lejárt készletek selejtezése.
* **Állapotmentés és visszatöltés (JSON):** A teljes gyári állapot (eltelt ciklusok, raktárkészlet, gyártósorok és azok stratégiái) mentése és visszatöltése JSON formátumban.

---

## Alkalmazott OOP elvek és tervezési minták

1. **Absztrakció, öröklődés és futásidejű polimorfizmus:**
   * A `Konzerv` absztrakt osztály definiálja a közös attribútumokat és az absztrakt `getRecommendedStorageTemp()` metódust, amelyet a `ZoldsegKonzerv` és a `HusKonzerv` eltérő üzleti szabályokkal valósít meg.
   * A `Comparable<Konzerv>` interfész a lejárati dátum szerinti természetes rendezést biztosítja, amelyet a `Raktar` `Comparator`-alapú rendezéssel (nettó tömeg szerint) egészít ki.
2. **Enkapszuláció és defenzív másolás:**
   * Minden modellosztály konstruktora validálja a bemeneti paramétereket.
   * A belső kollekciók (`Raktar.getKeszlet()`, `GyarManager.getGyartosorok()`) kizárólag módosíthatatlan nézetként (`Collections.unmodifiableList`) érhetők el kívülről.
3. **Strategy tervezési minta (`strategy` csomag):**
   * A `Gyartosor` osztály nem öröklődéssel, hanem kompozícióval (`GyartasiStrategia` referencia) határozza meg a következő gyártandó terméket, így futásidőben bármelyik gépsor átállítható manuális és automatikus üzemmód között.
4. **Factory tervezési minta (`factory` csomag):**
   * A `KonzervFactory` elfedi a konkrét konzervosztályok példányosítását és az egyedi azonosítók (`nextId`) kiosztását, biztosítva a JSON visszatöltés utáni azonosító-szinkronizációt is.
5. **Egyedi kivételhierarchia (`exception` csomag):**
   * A közös `KonzervGyarException` ősosztályból származó specifikus kivételek (`RaktarMegteltException`, `ErvenytelenGyartasiMuveletException`) tisztán jelzik az üzleti szabályok megsértését a UI réteg felé.
6. **Polimorf JSON perzisztencia (`persistence` csomag):**
   * A `JsonStorageService` `try-with-resources` szerkezettel, UTF-8 kódolással és egyedi Gson `TypeAdapter` megvalósításokkal kezeli az absztrakt `Konzerv` és a `GyartasiStrategia` típusok mentését és visszatöltését.

---

## Követelmények, fordítás és futtatás

### Előfeltételek
* **Java Development Kit (JDK):** Java 27 (a `pom.xml` beállításai szerint; korábbi LTS verzió esetén a `maven.compiler.source` és `target` értékek módosíthatók)
* **Apache Maven:** 3.8+

### Fordítás és tesztek futtatása

A projekt gyökérkönyvtárából (`KonzervGyar/`) futtatható parancsok:

```bash
# Projekt fordítása
mvn clean compile

# Egységtesztek futtatása
mvn test
```

### Alkalmazás indítása

Futtasd az org.example.Main osztályt az IDE-ből.

---

## Konzolos menü

Indítás után az alábbi főmenüpontok érhetők el:

1. **Gyár és raktár aktuális állapota** – Eltelt ciklusok/napok, raktár telítettség, gépsorok státusza és típusonkénti készletösszesítő.
2. **Termelés léptetése** – 1 ciklus, megadott számú ciklus, vagy 1 teljes munkanap ($8\text{ ciklus}$) lefuttatása.
3. **Egy adott gyártósor soron kívüli indítása** – Célzott gyártás egy kiválasztott gépsoron.
4. **Gyártósor beállítása** – Gépsor indítása/leállítása, stratégia váltása vagy ciklussebesség módosítása.
5. **Konzervek kiszállítása** – Adott mennyiségű termék kiadása a raktárból FEFO sorrendben.
6. **Raktárkészlet listázása és selejtezés** – Listázás lejárati dátum vagy nettó tömeg szerint, illetve lejárt termékek eltávolítása.
7. **Gyár állapotának mentése (JSON)** – Aktuális állapot kiírása a `gyar_allapot.json` fájlba.
8. **Gyár állapotának betöltése (JSON)** – Korábban mentett állapot visszaállítása.

---

Készítette: Németh Domonkos (SavedDometh)