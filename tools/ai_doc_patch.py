import sys
from pathlib import Path
from ollama_wrapper_iut import OllamaWrapper

# --- CONFIGURATION ---
# Remplace par le nom exact trouvé via la commande précédente (ex: 'qwen2.5-coder:latest')
MODEL_NAME = "qwen3:8b" 
# ---------------------

def main():
    if len(sys.argv) < 2:
        print("Usage: python tools/ai_doc_patch.py <fichier_diff>")
        sys.exit(1)

    diff_path = Path(sys.argv[1])
    if not diff_path.exists():
        print(f"Erreur : Le fichier {diff_path} n'existe pas.")
        sys.exit(1)

    diff_content = diff_path.read_text(encoding="utf-8")

    # --- LE PROMPT OPTIMISÉ ---
    prompt = f"""Tu es un Lead Tech et Rédacteur Technique expert en Java.
Ta mission est d'analyser un "git diff" d'une borne d'arcade et de déterminer comment la documentation doit être mise à jour.

Voici l'architecture de notre documentation :
- tech.md : Documentation technique de l'architecture et des classes.
- install.md : Guide d'installation matérielle et logicielle.
- add_game.md : Tutoriel expliquant comment ajouter un nouveau jeu sur la borne.
- user_guide.md : Manuel d'utilisation pour les joueurs (contrôles, navigation).

RÈGLES STRICTES :
1. Ne documente QUE ce qui est explicitement présent dans le diff. N'invente aucune fonctionnalité.
2. Si le diff ne contient que du refactoring, des corrections de bugs mineurs ou du code sans impact pour l'utilisateur, indique clairement qu'aucune mise à jour de la documentation globale n'est nécessaire.
3. Sois concis et va à l'essentiel.

=== DÉBUT DU GIT DIFF ===
{diff_content}
=== FIN DU GIT DIFF ===

Format de réponse attendu :
### 🔍 Analyse des changements
[Décris brièvement, en une ou deux phrases, ce que fait ce code techniquement]

### 📝 Suggestions de mises à jour Markdown
- **[Nom du fichier exact parmi la liste fournie]** : [Explication claire de ce qu'il faut ajouter ou modifier]
"""

    client = OllamaWrapper(timeout_s=300.0)

    print(f"Vérification du serveur Ollama à l'IUT...")
    if not client.is_server_running():
        print("Erreur fatale : Le serveur Ollama n'est pas accessible.")
        sys.exit(1)

    print(f"Génération de la documentation par {MODEL_NAME} en cours... (Cela peut prendre un peu de temps)")
    
    try:
        # On utilise une température basse (0.2 ou 0.3) pour rendre l'IA plus factuelle et moins "créative"
        result = client.generate_text(
            model=MODEL_NAME, 
            prompt=prompt,
            options={"temperature": 0.2} 
        )
        
        output_file = Path("docs/IA_SUGGESTIONS.md")
        output_file.write_text(result.response, encoding="utf-8")
        print(f"Succès ! L'analyse optimisée a été enregistrée dans {output_file}")

    except Exception as e:
        print(f"Erreur lors de la génération : {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()