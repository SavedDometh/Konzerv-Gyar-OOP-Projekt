# Konzervgyár – Termelés- és Raktárirányító Szimuláció

Objektumorientált Java konzolos alkalmazás, egy konzervgyár működését, gyártósorainak vezérlését és raktárkészlet kezelését szimulálja.

---

## Leírás

Gyarmanager.java példányosít 2 db Gyartosor osztályt azonosítóval, sebességgel (egy ciklus alatt hány db konzervet gyárt), aktív/leállított, mindkettőnél kiválasztható a gyártási stratégia (Manuálisan beállított konzervtípus/Automatikus - raktárban legkevesebbet gyárt). Lehet léptetni tetszőleges ciklussal a gyártást, a legyártott konzerveket a raktárban tárolja. Gyár állapotát el lehet menteni és visszatölteni JSON formátumban.


* **Több gyártósor párhuzamos kezelése:** Különálló gyártósor-példányok egyedi azonosítóval, állítható ciklussebességgel ($\text{db/ciklus}$) és üzemállapottal (aktív / leállított).
* **Dinamikus gyártási stratégiák:** Futásidőben váltható működési módok soronként:
  * *Manuális mód:* A kezelő által kiválasztott konzervtípus folyamatos gyártása.
  * *Automatikus (hiányalapú) mód:* A raktárkészlet valós idejű elemzése alapján mindig a leghiányosabb termék pótlása.
* **Idő- és műszakszimuláció:** Termelés léptetése egyetlen ciklussal, tetszőleges ciklusszámmal, vagy egy teljes munkanap szimulálásával ($1\text{ nap} = 8\text{ ciklus}$).
* **Raktárkezelés és FEFO kiszállítás:** Maximális raktárkapacitás figyelése, típusonkénti készletnyilvántartás, valamint kiszállításnál a leghamarabb lejáró tételek előnyben részesítése (*First-Expired, First-Out*).
* **Szavatosság-ellenőrzés és selejtezés:** Automatikus lejárati dátum számítás termékkategóriánként (zöldségkonzerv: $+3\text{ év}$, húskonzerv: $+5\text{ év}$) és lejárt készletek selejtezése.
* **Állapotmentés és visszatöltés (JSON):** A teljes gyári állapot (eltelt ciklusok, raktárkészlet, gyártósorok és azok stratégiái) mentése és visszatöltése JSON formátumban.

---

## Technikai követelmények és tervezési minták

1. **Enkapszuláció:**
   Protected (Konzerv.java) konstruktor használata. Osztályok adatait private mezőkben vannak és getterekkel lehet hozzáférni.
2. **Absztrakció:**
   Interface-ek használata (StorageService és GyartasiStrategia), Abstract osztály használata (Konzerv.java).
3. **Inheritance and polymorphism:**
   2 konkrét osztály közös parent osztállyal (HusKonzerv, ZoldsegKonzerv), felülírt működéssel (getRecommendedStorageTemp).
4. **Relationships:**
   GyarManager összefogja a Raktar-at és 2 db Gyartosor-t. A Raktar tárolja a különböző Konzerv objektumokat.
5. **Collections and generics:**
   A raktárkészlet és a gyártósorok kezelése ArrayList listákban. A készlet rendezhető lejárati dátum szerint a Konzerv osztályban Comparable<Konzerv> metódussal.
6. **Exceptions:**
   Létrehoztam saját exception-eket, metódusok bemenetét ahol szükséges ellenőrzi a program.
7. **Persistence:**
   A gyár  állapota JSON formátumban menthető és visszatölthető a Gson könyvtárra épülő JsonStorageService osztályon keresztül.
8. **Build:**
   A projekt Maven (pom.xml) felépítésű.

---

## Fordítás és futtatás

### Előfeltételek
* **Java Development Kit (JDK):** Java 21 (a `pom.xml` beállításai szerint; korábbi LTS verzió esetén a `maven.compiler.source` és `target` értékek módosíthatók)
* **Apache Maven:** 3.8+

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

<img width="472" height="278" alt="kép" src="https://github.com/user-attachments/assets/2ad12852-a20b-40fc-8081-326cebf748c2" />

---

Készítette: SavedDometh
Copyright (c) 2026-present, SavedDometh
