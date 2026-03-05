### 🔍 Analyse des changements  
Ce code initialise un client Ollama avec un timeout accru de 5 minutes (300 secondes), permettant de gérer des réponses plus longues ou des latences accrues. Les autres fichiers mettent à jour les licences, améliorent la documentation et corrigent des formats Markdown.

---

### 📝 Suggestions de mises à jour Markdown  
- **tools/ai_doc_patch.py** : Ajouter une explication sur le paramètre `timeout_s` dans la docstring pour clarifier la durée maximale d'attente pour les réponses du serveur Ollama.  
- **LICENSE** : Mettre à jour la mention de copyright pour refléter l'année 2023.  
- **README.md** : Améliorer la mise en forme des sections (ajouter des emojis, structurer les sous-sections) et corriger les erreurs de syntaxe Markdown.  
- **user_guide.md** : Ajouter une section détaillée sur la navigation dans le menu principal et les contrôles des joueurs.  
- **technical_documentation.md** : Mettre à jour la documentation des classes `OllamaWrapper` et `HighScoreManager` pour inclure des exemples d'utilisation et des explications des paramètres.