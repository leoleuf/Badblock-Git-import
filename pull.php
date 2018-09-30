<?php

shell_exec('git pull http://websyncbot:WAK2tRsB5jMzredQJ9uYxqyZwQUM6eYWTP74RJPM4rVvrCapLjf4F5SVHrqNkY4X@lusitania.badblock.fr/Website/badblock-website.git');
shell_exec('php composer.phar install');
shell_exec('php composer.phar update');

$data = array("username" => "Logger Site","embeds" => array(0 => array(
    "url" => "https://dev-web.badblock.fr",
    "title" => "GitLab Puller ",
    "description" => "Site Web de démo update !",
    "color" => 65280
)));



?>