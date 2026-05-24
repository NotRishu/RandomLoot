name: Build Plugin

on:
  push:
    branches:
      - main
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Setup Java 21
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 21

      - name: Cache Maven Packages
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-

      - name: Rebuild Maven Structure
        run: |
          mkdir -p src/main/java/com/randomlootpvp/commands
          mkdir -p src/main/java/com/randomlootpvp/listeners
          mkdir -p src/main/java/com/randomlootpvp/loot
          mkdir -p src/main/java/com/randomlootpvp/manager
          mkdir -p src/main/java/com/randomlootpvp/model
          mkdir -p src/main/java/com/randomlootpvp/util
          mkdir -p src/main/resources
          cp java/RandomLootPvP.java         src/main/java/com/randomlootpvp/
          cp java/RandomLootCommand.java     src/main/java/com/randomlootpvp/commands/
          cp java/ChestBreakListener.java    src/main/java/com/randomlootpvp/listeners/
          cp java/ChestInteractListener.java src/main/java/com/randomlootpvp/listeners/
          cp java/LootGenerator.java         src/main/java/com/randomlootpvp/loot/
          cp java/ChestManager.java          src/main/java/com/randomlootpvp/manager/
          cp java/ConfigManager.java         src/main/java/com/randomlootpvp/manager/
          cp java/TrackedChest.java          src/main/java/com/randomlootpvp/model/
          cp java/ChunkSafeUtil.java         src/main/java/com/randomlootpvp/util/
          cp resources/config.yml            src/main/resources/
          cp resources/plugin.yml            src/main/resources/

      - name: Build Plugin
        run: mvn clean package -B

      - name: Upload Plugin Jar
        uses: actions/upload-artifact@v4
        with:
          name: RandomLootPvP
          path: target/*.jar
