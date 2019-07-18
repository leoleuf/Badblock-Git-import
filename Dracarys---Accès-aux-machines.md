![dracarys](uploads/0dbf223db412c3f1be4f0469f68f7dda/dracarys.gif)

# Objectif

[dracarys.badblock.fr](dracarys.badblock.fr) est la machine de BadBlock permettant d'accéder aux autres machines de l'infrastructure via SSH (et SFTP).

Les intérêts de Dracarys sont multiples:
*  il permet de simplifier les accès: on passe de plusieurs mot de passes par machines à un accès unique
*  donner / retirer dès accès à quelqu'un devient bien plus simple
*  les accès sont personnels et donc mieux tracables

# Utilisation

Pour utiliser Dracarys, il faut un [client SFTP](client SFTP) et/ou un [client SSH](http://ttyplus.com/multi-tabbed-putty/).

### En SFTP (via FileZilla)

Pour utiliser via SFTP, il suffit de se connecter à votre utilisateur:
* **Protocole** SFTP
* **Host** dracarys.badblock.fr
* **Port** 22
* **Login type** Normal

### En SSH

Les informations de connexion sont les même que pour SFTP (voir au-dessus).

Une fois en SSH, la commande 'connect' peut être utilisée pour se connecter à une machine de l'infrastructure:
* **connect list**: affiche les différents utilisateurs auxquels vous avez accès
* **connect mc@game02**: dans cet exemple, on essaye de se connecter à l'utilisateur mc de la machine game02

### Problème d'accès

Si vous n'avez pas de compte où si il manque des accès à votre compte, contactez micro_maniaque.

# Configuration et installation

*  Voir la documentation de [ProxyUsers](https://lusitania.badblock.fr/backendv2/ProxyUsers)