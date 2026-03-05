#!/bin/bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"
MG2D_REPO_URL="https://github.com/synave/MG2D.git"

APT_UPDATED=0

apt_install() {
    if [ "$APT_UPDATED" -eq 0 ]; then
        sudo apt-get update
        APT_UPDATED=1
    fi
    sudo apt-get install -y "$@"
}

install_package() {
    local apt_pkg="$1"
    local brew_pkg="$2"

    if command -v apt-get >/dev/null 2>&1; then
        apt_install "$apt_pkg"
        return
    fi

    if command -v brew >/dev/null 2>&1; then
        brew install "$brew_pkg"
        return
    fi

    echo "Aucun gestionnaire de paquets compatible detecte (apt-get ou brew)." >&2
    exit 1
}

ensure_command() {
    local cmd_name="$1"
    local apt_pkg="$2"
    local brew_pkg="$3"

    if command -v "$cmd_name" >/dev/null 2>&1; then
        return
    fi

    echo "Dependance manquante: $cmd_name. Installation en cours..."
    install_package "$apt_pkg" "$brew_pkg"

    if ! command -v "$cmd_name" >/dev/null 2>&1; then
        echo "Impossible d'installer $cmd_name automatiquement." >&2
        exit 1
    fi
}

echo "=== Verification des prerequis ==="
ensure_command git git git
ensure_command java default-jdk openjdk
ensure_command javac default-jdk openjdk
ensure_command python3 python3 python

echo "=== Verification de MG2D ==="
if [ -d "$MG2D_DIR/MG2D" ]; then
    echo "MG2D deja present."
elif [ -e "$MG2D_DIR" ]; then
    echo "Le chemin $MG2D_DIR existe mais ne contient pas une installation MG2D valide." >&2
    echo "Corrige ce dossier puis relance le script." >&2
    exit 1
else
    echo "MG2D absent: clonage depuis $MG2D_REPO_URL"
    git clone "$MG2D_REPO_URL" "$MG2D_DIR"
fi

echo "=== Verification des droits d'execution ==="
chmod +x \
    "$ROOT_DIR/clean.sh" \
    "$ROOT_DIR/compilation.sh" \
    "$ROOT_DIR/lancerBorne.sh" \
    "$ROOT_DIR/setup_dependencies.sh"

echo "=== Lancement automatique de la borne ==="
cd "$ROOT_DIR"
exec ./lancerBorne.sh
