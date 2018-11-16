<?php
echo 'PULL';
shell_exec('git pull http://websyncbot:WAK2tRsB5jMzredQJ9uYxqyZwQUM6eYWTP74RJPM4rVvrCapLjf4F5SVHrqNkY4X@lusitania.badblock.fr/Website/minserv.git');
shell_exec('php composer.phar install');
shell_exec('php composer.phar update');

?>