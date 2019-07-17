# ProxyUsers

## Objectif

Ce projet est un utilitaire pour gérer une machine 'proxy' SSH avec plusieurs utilisateurs.

## Utilisation

### Requirements
- python3
- sshfs

### Installation
- Clone this repository in /opt/ProxyUsers
- Run (as root) 'make install'
- Add 'ALL ALL=NOPASSWD: /opt/ProxyUsers/command\_connect.py' rule to /etc/sudoers
- Add a crontab (start & each X minutes) on /opt/ProxyUsers/generate\_sshfs.py

### Utilisateur

L'utilisateur peut se connecter à une session SSH via 'connect' (connect list pour voir celles disponible).
Il pourra aussi accèder aux différents SSHFS en fonction de la configuration.

## Configuration

### Ajouter une clé SSH

Ajouter la clé SSH dans /root/.ssh, puis ajouter les informations suivants à /root/.ssh/config:
```
Host <adresse du serveur>
User <utilisateur>
Port <le port SSH>
IdentityFile ~/.ssh/<nom du fichier de la clé SSH>
```

### Ajouter un serveur

Après avoir ajouter la clé SSH, il suffit d'ajouter le serveur dans servers.json.

- shortname correspond au nom que l'on va utiliser avec la commande 'connect'
- users correspond aux différents utilisateurs du serveur (mc, root, ...)
- allowed\_groups correspond aux différents groupes d'utilisateurs ayant le droit d'utiliser cet utilisateur
- allowed\_users correspond aux différents utilisateurs ayant le droit d'utiliser cet utilisateur
- fs correspond aux différents dossier du serveur qui vont être monter, et à quel endroit par rapport au fs\_mountpoint de l'utilisateur

### Ajouter un utilisateur

D'abord ajouter l'utilisateur sur Linux (useradd), avec une home.

- fs\_mountpoint correspond à l'endroit où les différents SSHFS de l'utilisateurs vont être montés, probablement sa home
- groups correspond aux différents groupes auxquels l'utilisateurs appartient
