import json

# Ce script permet de charger et utiliser les configurations:
# servers.json & users.json

"""
Représente les différents utilisateurs d'un serveur (mc, root ou autre)
Contient les différents path à monter via SSHFS  & les utilisateurs/groupes autorisés

Chargé depuis servers.json
"""
class ServerUser:
    def __init__(self, name, config):
        self.name = name
        self.allowed_groups = set(config.get("allowed_groups", []))
        self.allowed_users = set(config.get("allowed_users", []))
        self.fs = config["fs"]

    """
    Check if a proxy user is allowed to use this user@host
    """
    def allowed(self, user):
        if user in self.allowed_users:
            return True

        for group in user.groups:
            if group in self.allowed_groups:
                return True

        return False

"""
Représente les différents serveurs (game02 ou autre)
Contient les différents utilisateurs de ce serveur

Chargé depuis servers.json
"""
class Server:
    def __init__(self, name, config):
        self.name = name
        self.shortname = config["shortname"]
        self.users = []

        for user in config["users"]:
            self.users.append(ServerUser(user, config["users"][user]))

    """
    Check if the given hostname match this server
    """
    def match(self, host):
        return host == self.name or host == self.shortname

"""
Représente un utilisateur du proxy (sulfique, micro ou autre)
Contient les différents groupes de l'utilisateur et le dossier où monter les
SSHFS.

Chargé depuis users.json
"""
class User:
    def __init__(self, name, config):
        self.name = name
        self.groups = config.get("groups", [])
        self.fs_mountpoint = config["fs_mountpoint"]

    """
    Iterator on allowed user@host that the user can access to
    """
    def allowed_users(self):
        for server in servers:
            for user in server.users:
                if user.allowed(self):
                    yield (server, user)

    """
    Check if the user is allowed to use user@host
    Raise an exception if the user or the host is unknown
    """
    def get_host(self, host, user):
        for server in servers:
            if not server.match(host):
                continue

            for suser in server.users:
                if suser.name != user:
                    continue

                if not suser.allowed(self):
                    raise Exception('youre are not allowed to use {}@{}'.format(user, host))

                return (server, suser)

            raise Exception('unknown user {} for host {}'.format(user, host))

        raise Exception('unknown host {}'.format(host))

"""
Get an user by is name.
Raise an exception if the user is unknown
"""
def get_user(user):
    for u in users:
        if u.name == user:
            return u

    raise Exception('unknown user {}'.format(user))

servers = []
users = []

# chargement de servers.json
with open('servers.json', 'r') as f:
    config = json.load(f)

    for key in config:
        servers.append(Server(key, config[key]))

# chargement de users.json
with open('users.json', 'r') as f:
    config = json.load(f)

    for key in config:
        users.append(User(key, config[key]))
