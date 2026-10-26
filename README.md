# Compiler in Java

Ein kleiner Compiler mit Interpreter für eine einfache Ausdruckssprache.
Das Projekt zerlegt Quelltext in Tokens, erstellt einen abstrakten Syntaxbaum
(AST), übersetzt ihn in Zwischenbefehle und wertet diese aus.

## Sprachumfang

- Ganze Zahlen und arithmetische Operatoren: `+`, `-`, `*`, `/`, `%`
- Klammern und Operatorrangfolge
- Lokale Variablen mit `let ... in ...`
- Fehlermeldungen für Syntaxfehler und unbekannte Variablennamen

Die Berechnung verwendet Java-`int`-Werte; Divisionen sind ganzzahlig.

## Beispiel

Die Datei `resources/simple.ml` enthält:

```text
let x = 3 in
x / 3 + 3 * 2
```

Das Programm berechnet dafür:

```text
Value: 7
```

## Voraussetzungen

- JDK 23 (damit kompiliert und ausgeführt)
- Keine externen Bibliotheken erforderlich

## Kompilieren und starten

Führe diese Befehle im Projektverzeichnis aus (macOS/Linux):

```bash
javac -d out -sourcepath src src/de/uniwue/jpp/compiler/Main.java
java -cp "out:resources" de.uniwue.jpp.compiler.Main
```

Unter Windows wird der Klassenpfad mit einem Semikolon getrennt: `"out;resources"`.

`Main` lädt beim Start automatisch `resources/simple.ml`. Um einen anderen
Ausdruck auszuprobieren, ändere den Inhalt dieser Datei und starte das Programm
erneut. Dateipfade als Kommandozeilenargument werden derzeit nicht ausgewertet.

## Aufbau

```text
src/de/uniwue/jpp/compiler/
├── Main.java    # Programmeinstieg und Ablauf
├── syntax/      # Lexer, Parser, Tokens und Syntaxbaum
├── lowering/    # Übersetzung in Zwischenbefehle und Speicherverwaltung
├── eval/        # Auswertung der Zwischenbefehle
├── error/       # Fehlermeldungen und Quelltextpositionen
└── util/        # Verwaltung von Bezeichnern

resources/
└── simple.ml    # Beispielprogramm
```

## Bekannte Einschränkungen

Zusätzlich zum Ergebnis gibt das Programm eine formatierte Darstellung des
Ausdrucks aus. Im mitgelieferten Beispiel erscheint der Variablenname in der
`let`-Deklaration dabei derzeit als `null`. Das berechnete Ergebnis des Beispiels
ist trotzdem `7`.
