package com.isruan.battleshipz.application;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
	private String currentLanguage = "English";
	private final Map<String, Map<String, String>> translations;

	public LanguageManager() {
		translations = new HashMap<>();
		new HashMap<>();
		new HashMap<>();
		loadTranslations();
	}

	public void setLanguage(String language) {
		currentLanguage = language;
		loadTranslations();
	}

	public String getCurrentLanguage() {
		return currentLanguage;
	}

	public String translate(String text) {
		Map<String, String> languageTranslations = translations.get(currentLanguage);
		if (languageTranslations == null) {
			languageTranslations = translations.get("English"); // Fallback to English
		}
		return languageTranslations.getOrDefault(text, text); // Default to original text if translation not found
	}

	private void loadTranslations() {
		// Clear existing translations
		translations.clear();

		// Define translations for English
		Map<String, String> englishTranslations = new HashMap<>();
		englishTranslations.put("File", "File");
		englishTranslations.put("Exit", "Exit");
		englishTranslations.put("Options", "Options");
		englishTranslations.put("Mute Game Music", "Mute Game Music");
		englishTranslations.put("Mute Sound Effects", "Mute Sound Effects");
		englishTranslations.put("Language", "Language");
		englishTranslations.put("English", "English");
		englishTranslations.put("Français", "French"); // Note: You can keep the language names in English for the menu
		englishTranslations.put("Vs Bot", "Vs Bot");
		englishTranslations.put("Vs Friend", "Vs Friend");
		englishTranslations.put("About", "About");
		englishTranslations.put("Help", "Help");
		englishTranslations.put("Main Page", "Main Page");
		englishTranslations.put("Swap", "Swap");
		englishTranslations.put("Daniel’s Turn\nRound 1\nClick on a square to attack it",
				"Daniel’s Turn\nRound 1\nClick on a square to attack it");
		englishTranslations.put("Enemy’s Turn\nRound 1\nClick on a square to attack it",
				"Enemy’s Turn\nRound 1\nClick on a square to attack it");
		englishTranslations.put(
				"Your Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2",
				"Your Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2");
		englishTranslations.put(
				"Enemy Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2",
				"Enemy Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2");
		englishTranslations.put(
				"Initializing game boards..\nSelect where to place your ships.\nStarting combat!\nEnemy has attacked at 6,6 missing",
				"Initializing game boards..\nSelect where to place your ships.\nStarting combat!\nEnemy has attacked at 6,6 missing");
		englishTranslations.put("Battleship Game\nVersion 1.0\nDeveloped by Ruan Simo",
				"Battleship Game\nVersion 1.0\nDeveloped by Ruan Simo");
		englishTranslations.put("Help content goes here.", "Help content goes here.");

		// Define translations for French
		Map<String, String> frenchTranslations = new HashMap<>();
		frenchTranslations.put("File", "Fichier");
		frenchTranslations.put("Exit", "Quitter");
		frenchTranslations.put("Options", "Options");
		frenchTranslations.put("Mute Game Music", "Couper la musique du jeu");
		frenchTranslations.put("Mute Sound Effects", "Couper les effets sonores");
		frenchTranslations.put("Language", "Langue");
		frenchTranslations.put("English", "Anglais");
		frenchTranslations.put("Français", "Français");
		frenchTranslations.put("Vs Bot", "Contre Bot");
		frenchTranslations.put("Vs Friend", "Contre Ami");
		frenchTranslations.put("About", "À propos");
		frenchTranslations.put("Help", "Aide");
		frenchTranslations.put("Main Page", "Page principale");
		frenchTranslations.put("Swap", "Échanger");
		frenchTranslations.put("Daniel’s Turn\nRound 1\nClick on a square to attack it",
				"Tour de Daniel\nRound 1\nCliquez sur une case pour attaquer");
		frenchTranslations.put("Enemy’s Turn\nRound 1\nClick on a square to attack it",
				"Tour de l'ennemi\nRound 1\nCliquez sur une case pour attaquer");
		frenchTranslations.put(
				"Your Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2",
				"Vos Navires\nPorte-avions: 5/5\nCroiseur: 4/4\nDestroyer: 3/3\nFrégate: 3/3\nSous-marin: 2/2");
		frenchTranslations.put(
				"Enemy Ships\nCarrier: 5/5\nCruiser: 4/4\nDestroyer: 3/3\nMissile Frigate: 3/3\nSubmarine: 2/2",
				"Navires ennemis\nPorte-avions: 5/5\nCroiseur: 4/4\nDestroyer: 3/3\nFrégate: 3/3\nSous-marin: 2/2");
		frenchTranslations.put(
				"Initializing game boards..\nSelect where to place your ships.\nStarting combat!\nEnemy has attacked at 6,6 missing",
				"Initialisation des plateaux de jeu..\nSélectionnez où placer vos navires.\nDébut du combat!\nL'ennemi a attaqué en 6,6 en ratant");
		frenchTranslations.put("Battleship Game\nVersion 1.0\nDeveloped by Ruan Simo",
				"Jeu de Bataille Navale\nVersion 1.0\nDéveloppé par Ruan Simo");
		frenchTranslations.put(
				"How to play Battleship:\n1. Place your ships.\n2. Take turns to guess the location of enemy ships.\n3. Sink all enemy ships to win.",
				"Comment jouer à la bataille navale:\n 1. Placez vos navires.\n2. Tournez-vous pour deviner l'emplacement des navires ennemis.\n3. Coulez tous les navires ennemis pour gagner.");

		// Add translations to the main map
		translations.put("English", englishTranslations);
		translations.put("Français", frenchTranslations);
	}
}

