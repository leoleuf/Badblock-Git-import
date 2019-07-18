# ProxyUsers

## Objectif

Ce projet est un utilitaire permettant de gérer une machine proxy, reliant les utilisateurs (administrateurs, développeurs) et les machines internes (site, bungee, mini-jeux, ect).
Chaque utilisateur doit avoir son propre compte.

Cet utilitaire propose deux outils principaux:
- une commande 'connect' pour se connecter aux machines internes depuis la machine proxy
- des 'SSHFS' pour accéder directement aux fichiers des machines internes depuis la machine proxy

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

### Générer une clé SSH

Pour générer une clé SSH utiliser:
```
$ ssh-keygen -t rsa -b 4096 -f ma_cle_ssh
```

Les fichiers ma\_cle\_ssh et ma\_cle\_ssh.pub seront générés.

Le premier est la clé PRIVEE (à ne pas partager), à mettre sur le serveur proxy (voir partie suivante).

Le deuxième est la clé publique, à mettre sur le serveur distant dans ~/.ssh/authorized\_keys.

### Ajouter une clé SSH

Ajouter la clé SSH dans /root/.ssh, puis ajouter les informations suivants à /root/.ssh/config:
```
Host <adresse du serveur>
User <utilisateur>
Port <le port SSH>
IdentityFile ~/.ssh/<nom du fichier de la clé SSH>
```

Très important: pour des raisons de sécurité, changer les permissions de la clé SSH:
```
$ chmod 600 ~/.ssh/<nom du fichier de la clé SSH>
$ chown root:root ~/.ssh/<nom du fichier de la clé SSH>
```

Cela évitera que d'autre utilisateurs lisent/modifient la clé. :)

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

### Forcer des mots de passe complexes

Les mots de passes étant définissables par les utilisateurs, il vaut éviter tout soucis et forcer des mots de passes corcés.
Pour ça, installer:
```
$ apt install libpam-cracklib
```

Et ajouter à /etc/pam.d/common-password:
```
password  sufficient  pam_unix.so md5 shadow nullok try_first_pass use_authtok remember=5
password  requisite   pam_cracklib.so try_first_pass retry=3 minlen=32
```
